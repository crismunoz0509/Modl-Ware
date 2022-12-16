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
   // variable to access the background of the javafx scene
   @FXML
   private AnchorPane javafx_background;
   
   // variable to access the javafx text field for the user to type their node's name
   @FXML
   private TextField text_field_node;
   
   // variable to access the javafx text field for the user to type their edge's name
   @FXML
   private TextField text_field_edge;
   
   // set of all signatures in the model
   private Set<Signature> sorted_signatures = new HashSet<>();
   
   // HashMap, the key is the name of a signature. While the value is the # of times a node has that name
   // used to print the " run {} for exactly signature_count signature_name"
   private HashMap<String, Integer> signature_counts = new HashMap<>();
   
   // HashMap, the key is the name of a signature. While the value is an ArrayList that stores Node objects
   // nodes stored in the ArrayList all have the same name/are part of the same signature
   private HashMap<String, ArrayList<Nodes>> sorted_nodes = new HashMap<>();
   
   // Function to place a node at the mouse position
   public void PlaceNodeOnClick(MouseEvent event)
   {
      // Only place a node if the Node Button has been pressed AND the Node Tool is active
      if(GetNodeButton() && GetNodeTool())
      {
         new Nodes(text_field_node.getText(), event);
      }
   }
   
   // Function runs when the node button is pressed 
   public void NodeButtonPressed()
   {
      // Gives other GraphItem objects access to the background AnchorPane object
      SetBackground(javafx_background);
      SetNodeButtonOn();
   }
   
   // Function runs when the edge button is pressed
   public void EdgeButtonPressed()
   {
      SetBackground(javafx_background);
      SetEdgeButtonOn();
      // Give Node objects access to the edge text field
      Nodes.SetTextField(text_field_edge);
   }
   
   // Runs when output button is pressed
   public void OutputButtonPressed() throws FileNotFoundException
   {
      // Location where the .als file will be saved and the name of the file
      File file_handler = new File("/Users/crismunoz/Desktop/Research/software-modeling/alloy_files/alloy_model.als");
      PrintWriter file_output = new PrintWriter(file_handler);
      
      sorted_signatures = new HashSet<>();
      sorted_nodes = new HashMap<>();
      
      // For each node that has been placed, sort by the node name 
      for(Nodes current_node : Nodes.GetAllNodesArrayList())
      {
         String node_name = current_node.GetNodeName();
         
         // if the sorted_nodes hashmap contain sthe current node name
         //    access the ArrayList that stores the nodes that belong to this signature and add the current node
         //    increment signature_counts value for the node of this signature
         // else
         //    create a new ArrayList
         //    add the current node to the ArrayList
         //    put a element in the sorted_nodes hashmap w/ a key value pair of <the name of the node, and the new ArrayList>
         //    put a element in the signature_counts hashmap w/ a 
         if(sorted_nodes.containsKey(node_name))
         {
            ArrayList<Nodes> stored_nodes = sorted_nodes.get(node_name);
            stored_nodes.add(current_node);
            
            int value = signature_counts.get(node_name) + 1;
            signature_counts.replace(node_name, value);
         }
         else
         {
            ArrayList<Nodes> stored_nodes = new ArrayList<>();
            stored_nodes.add(current_node);
            sorted_nodes.put(node_name, stored_nodes);
            
            signature_counts.put(node_name, 1);
         }
      }
      
      // Create a signature object from each sorted_nodes key. Store all the Signature objects in a set
      for(Map.Entry hashmap_iterator : sorted_nodes.entrySet())
      {
         // get the key of the sorted_nodes HashMap (its the name of the signature)
         String key = (String)hashmap_iterator.getKey();
         // get the value of the sorted_nodes HashMap (its the ArrayList storing nodes that belong to said signature)
         ArrayList<Nodes> value = (ArrayList<Nodes>)hashmap_iterator.getValue();

         // create a new signature object from the key value pair
         Signature new_sig = new Signature(key, value);
         // add the signature object to an ArrayList
         sorted_signatures.add(new_sig);
      }
      
      // For each Signature write the output to the file
      for(Signature current_signature : sorted_signatures)
      {
         String full_output = "";
         
         // Determines the relationship type for each relationship a signature has
         current_signature.CheckRelationType();
         
         // Write " sig { " followed by the name of the signature to full_output
         full_output = full_output.concat(String.format("sig %s\n{\n", current_signature.GetName()));
         // Write each relationship and relationship type ( relation : disj lone/some/set/one name) to full_output
         full_output = full_output.concat(current_signature.GetRelationshipPrint());
         // close out the full_output signature using " } "
         full_output = full_output.concat("}\n\n");
         
         // write the full_output to the output object
         file_output.print(full_output);
      }
      
      // Write to the file "run {} for exactly #_of_signatures" 
      String output = "run {} for ";
      if(sorted_signatures.size() > 0)
      {
         for(Map.Entry hashmap_iterator : signature_counts.entrySet())
         {
            String key = (String)hashmap_iterator.getKey();
            int value = (Integer)hashmap_iterator.getValue();
            
            output = output.concat(String.format("exactly %s %s, ", String.valueOf(value), key));
         }
      }
      
      output = output.trim();
      output = output.substring(0, output.length() - 1);
      file_output.println(output);
      
      file_output.close();
   }
}