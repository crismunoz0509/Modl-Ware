package javafxtesting;

import javafx.scene.paint.Color;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Signature 
{
   private String signature_name;
   private Color node_color;
   
   private ArrayList<Nodes> all_nodes = new ArrayList<>();
   private HashMap<String, String> output_relationships = new HashMap<>();
   
   private HashMap<String, HashMap<Nodes, Boolean>> track_disjoint = new HashMap<>();
   private HashMap<String, HashMap<String, Integer>> track_relation_count = new HashMap<>();
   private HashMap<String, HashMap<String, ArrayList<Boolean>>> relation_type = new HashMap<>();
   
   public Signature(String name, ArrayList<Nodes> list_of_nodes)
   {
      Random random = new Random();
      String colors[] = {"aquamarine", "azure", "cadetblue", "darkgrey", "darksalmon", "darkturquoise", "gold", "lightpink", "firebrick", "LavenderBlush", "Azure", "brown", "peru", "maroon", "rosybrown", "tan", "cornsilk", "deepskyblue", "lightblue", "lightsteelblue", "lightcyan", "olivedrab", "seagreen", "forestgreen", "yellowgreen", "lightgreen", "mediumslateblue", "orchid", "thisle", "lavender", "khaki", "papayawhip", "lightyellow", "peachpuff", "orange", "tomato", "darkorange", "pink", "palevioletred", "salmon", "lightsalmon", "indianred"}; 
      signature_name = name;
      all_nodes = list_of_nodes;
      node_color = Color.valueOf(colors[random.nextInt(colors.length)]);
   }
   
   public String GetName()
   {
      return signature_name;
   }
   
   // returns all the relationships of this signature
   public String GetRelationshipPrint()
   {
      String output_print = "";
      
      for(Map.Entry map_it : output_relationships.entrySet())
      {
         String key = (String)map_it.getKey();
         String value = (String)map_it.getValue();
         
         output_print = output_print.concat(String.format("\t%s : %s ,\n", key, value));
      }
      
      return output_print;
   }
   
   // return all the nodes a relationship connects to
   public ArrayList<String> GetAllNodeEnds(HashMap<String, ArrayList<Boolean>> value)
   {
      ArrayList<String> temp_list = new ArrayList<>();
      for(Map.Entry map_it : value.entrySet())
      {
         temp_list.add((String)map_it.getKey());
      }
      
      return temp_list;
   }
   
   //fill output_relationships with relationship types
   public void CheckRelationType()
   {
      StoreRelationShips();
      for(Nodes current_node : all_nodes)
      {
         current_node.GetRectangle().setFill(node_color);
         
         String node_name = current_node.GetNodeName();
         HashMap<String, HashMap<String, Integer>> counts_hash = CountRelationships(current_node);
         
         // < false, false > default
         
         // for each relationship
         // "flip the switches" for each individual node, depending on the node's relationship count
         // < relationship name < node name, < false, false >>>
         for(Map.Entry map_relations : relation_type.entrySet())
         {
            String key = (String)map_relations.getKey();
            HashMap<String, ArrayList<Boolean>> value = (HashMap<String, ArrayList<Boolean>>)map_relations.getValue();
           
            for(Map.Entry map_end_points : value.entrySet())
            {
               String key_end = (String)map_end_points.getKey();
               ArrayList<Boolean> value_end = (ArrayList<Boolean>)map_end_points.getValue();
               
               if(counts_hash.containsKey(key))
               {
                  if(counts_hash.get(key).containsKey(key_end))
                  {
                     if(counts_hash.get(key).get(key_end) > 1)
                     {
                        value_end.set(0, true);
                     }
                  }
                  else
                  {
                     if(!value_end.get(1))
                     {
                        value_end.set(1, true);
                     }
                  }
               }
               else
               {
                  if(!value_end.get(1))
                  {
                     value_end.set(1, true);
                  }
               }
            }
         }
      }
      
      // go through the "flipped switches" and determine if the entire relationship is of
      // one, lone, some, or set type
      // < true, false > SOME
      // < false, true > LONE
      // < false, false > ONE
      // < true, true > SET
      
      // check if the relationship is disjoint
      
      // Then creating a string with the relationship type (disj, some, lone etc) as well as 
      // what signatures it's related to
      // finally storing that string in output_relationships
      for(Map.Entry map_relations : relation_type.entrySet())
      {
         String key = (String)map_relations.getKey();
         HashMap<String, ArrayList<Boolean>> value = (HashMap<String, ArrayList<Boolean>>)map_relations.getValue();
         
         ArrayList<String> store_sigs = new ArrayList<>();
         ArrayList<Boolean> store_relation_type = new ArrayList<>();
         
         store_relation_type.add(false);
         store_relation_type.add(false);
         
         String output = "";
         output_relationships.put(key, output);
         
         for(Map.Entry map_end_points : value.entrySet())
         {
            String key_end = (String)map_end_points.getKey();
            ArrayList<Boolean> value_end = (ArrayList<Boolean>)map_end_points.getValue();
            
            store_sigs.add(key_end);
            
            if(value_end.get(0))
            {
               store_relation_type.set(0, true);
            }
            if(value_end.get(1))
            {
               store_relation_type.set(1, true);
            }
         }
         
         CheckDisjoint();
         
         if(track_disjoint.containsKey(key))
         {
            if(!track_disjoint.get(key).containsValue(false))
            {
               output = output.concat("disj ");
            }
         }
         
         if(store_relation_type.get(0) && store_relation_type.get(1))
         {
            output = output.concat("set (");
         }
         else if(!store_relation_type.get(0) && store_relation_type.get(1))
         {
            output = output.concat("lone (");
         }
         else if(store_relation_type.get(0) && !store_relation_type.get(1))
         {
            output = output.concat("some (");
         }
         else
         {
            output = output.concat("one (");
         }
         
         ArrayList<String> arr_list = GetAllNodeEnds(value);
         output = output.concat(arr_list.get(0));
         for(int index = 1; index < arr_list.size(); index++)
         {
            output = output.concat(" + " + arr_list.get(index));
         }
         output = output.concat(")");
         output_relationships.replace(key, output);
      }
   }
   
   // go through all nodes to store all possible relationships a signature can have or what nodes it can connect to
   public void StoreRelationShips()
   {      
      // fill relation_type with starting values for every relation and 
      // < relation name, < node name , < false, false >>>
      for(Nodes it : all_nodes)
      {
         for(Edges selected_edge : it.GetEdgeList())
         {
            String relation_name = selected_edge.GetRelationName();
            String end_node_name = selected_edge.GetNodeEnd().GetNodeName();
            
            if(relation_type.containsKey(relation_name))
            {
               HashMap<String, ArrayList<Boolean>> end_node_hash = relation_type.get(relation_name);
               if(!end_node_hash.containsKey(end_node_name))
               {
                  ArrayList<Boolean> temp_list = new ArrayList<>();
                  temp_list.add(false);
                  temp_list.add(false);
                  end_node_hash.put(end_node_name, temp_list);
               }
            }
            else
            {
               HashMap<String, ArrayList<Boolean>> temp_hash = new HashMap<>();
               ArrayList<Boolean> temp_list = new ArrayList<>();
               temp_list.add(false);
               temp_list.add(false);
               relation_type.put(relation_name, temp_hash);
               temp_hash.put(end_node_name, temp_list);
            }
         }
      }
   }
   
   // for each node in a certain relationship, count the # of times that node repeats
   public HashMap<String, HashMap<String, Integer>> CountRelationships(Nodes current_node)
   {
      HashMap<String, HashMap<String, Integer>> counts_hash = new HashMap<>();
      
      // for each relationship, count the # of relationships which correspond with a specific node
      // < relation name , < node name , # of nodes> >
      for(Edges selected_edge : current_node.GetEdgeList())
      {
         String relation_name = selected_edge.GetRelationName();
         String end_node_name = selected_edge.GetNodeEnd().GetNodeName();

         if(counts_hash.containsKey(relation_name))
         {
            HashMap<String, Integer> end_node_hash = counts_hash.get(relation_name);
            if(end_node_hash.containsKey(end_node_name))
            {
               int val = end_node_hash.get(end_node_name) + 1;
               end_node_hash.put(end_node_name, val);
            }
            else
            {
               end_node_hash.put(end_node_name, 1);
            }
         }
         else
         {
            HashMap<String, Integer> temp_hash = new HashMap<>();
            temp_hash.put( end_node_name, 1);
            counts_hash.put(relation_name, temp_hash);
         }
      }
      return counts_hash;
   }
   
   // determine if a relationship is disjoint
   public void CheckDisjoint()
   {
      for(Nodes all_it : all_nodes)
      {
         for(Edges it : all_it.GetEdgeList())
         {
            String relation_name = it.GetRelationName();
            Nodes end_node = it.GetNodeEnd();

            if(track_disjoint.containsKey(relation_name))
            {
               HashMap<Nodes, Boolean> find_hash = track_disjoint.get(relation_name);
               if(find_hash.containsKey(end_node))
               {
                  find_hash.replace(end_node, false);
               }
               else
               {
                  find_hash.put(end_node, true);
               }
            }
            else
            {
               HashMap<Nodes, Boolean> temp_hash = new HashMap<>();
               temp_hash.put(end_node, true);
               track_disjoint.put(relation_name, temp_hash);
            }
         }
      }
   }
}