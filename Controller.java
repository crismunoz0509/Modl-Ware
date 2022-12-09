package javafxtesting;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;

public class Controller extends GraphItem
{   
   @FXML
   private AnchorPane background;
   
   @FXML
   private TextField text_field_node;
   
   @FXML
   private TextField text_field_edge;
   
   private Set<Signature> all_sigs = new HashSet<>();
   private HashMap<String, Integer> sig_count = new HashMap<>();
   private HashMap<String, ArrayList<Nodes>> sorted_nodes = new HashMap<>();
   
   public void CanvasClick(MouseEvent event)
   {
      if(GraphItem.GetNodeButton() && GraphItem.GetNodeTool())
      {
         new Nodes(text_field_node.getText(), event);
      }
   }
   
   public void NodeButton()
   {
      SetBackground(background);
      GraphItem.NodeButtonOn();
   }
   
   public void EdgeButton()
   {
      SetBackground(background);
      GraphItem.EdgeButtonOn();
      Nodes.TextField(text_field_edge);
   }
   
   public void OutputClick() throws FileNotFoundException
   {
      File fh = new File("/Users/crismunoz/Desktop/Research/software-modeling/alloy_files/alloy_model.als");
      PrintWriter out = new PrintWriter(fh);
      
      all_sigs = new HashSet<>();
      sorted_nodes = new HashMap<>();
      
      // sort all nodes by their signature
      for(Nodes it : Nodes.GetAllNodes())
      {
         String node_name = it.GetNodeName();
         if(sorted_nodes.containsKey(node_name))
         {
            ArrayList<Nodes> stored_nodes = sorted_nodes.get(node_name);
            stored_nodes.add(it);
            
            int value = sig_count.get(node_name) + 1;
            sig_count.replace(node_name, value);
         }
         else
         {
            ArrayList<Nodes> stored_nodes = new ArrayList<>();
            stored_nodes.add(it);
            sorted_nodes.put(node_name, stored_nodes);
            
            sig_count.put(node_name, 1);
         }
      }
      
      // create a signature object from each hashmap element < name, list of nodes w/ name >
      for(Map.Entry map_it : sorted_nodes.entrySet())
      {
         String key = (String)map_it.getKey();
         ArrayList<Nodes> value = (ArrayList<Nodes>)map_it.getValue();
         
         Signature new_sig = new Signature(key, value);
         all_sigs.add(new_sig);
      }
      
      
      for(Signature it : all_sigs)
      {
         String full_output = "";
         
         it.CheckRelationType();
         
         //POPUP LONE?? SIG
         full_output = full_output.concat(String.format("sig %s\n{\n", it.GetName()));
         full_output = full_output.concat(it.GetRelationshipPrint());
         full_output = full_output.concat("}\n\n");
         out.print(full_output);
      }
      
      String output = "run {} for ";
      if(all_sigs.size() > 0)
      {
         for(Map.Entry map_it : sig_count.entrySet())
         {
            String key = (String)map_it.getKey();
            int value = (Integer)map_it.getValue();
            
            output = output.concat(String.format("exactly %s %s, ", String.valueOf(value), key));
         }
      }
      
      output = output.trim();
      output = output.substring(0, output.length() - 1);
      out.println(output);
      
      out.close();
   }
}