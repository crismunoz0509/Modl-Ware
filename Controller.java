package javafxtesting;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;


public class Controller extends GraphItem
{   
   @FXML
   private AnchorPane background;
   private ArrayList<Nodes> all_nodes = new ArrayList<>();
   private HashMap<String, Integer> name_count = new HashMap<>();
   
   public void CanvasClick(MouseEvent event)
   {
      if(GraphItem.GetNodeButton() && GraphItem.GetNodeTool())
      {
         Nodes new_node = new Nodes("random_name", event);
         all_nodes.add(new_node);
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
   }
   
   public void OutputClick() throws FileNotFoundException
   {
      File fh = new File("alloy_model.als");
      PrintWriter out = new PrintWriter(fh);
      
      for(Nodes it : all_nodes)
      {
         if(name_count.containsKey(it.GetNodeName()))
         {
            name_count.replace(it.GetNodeName(), name_count.get(it.GetNodeName()) + 1);
         }
         else
         {
            name_count.put(it.GetNodeName(), 1);
         }
      }
      
      for(Nodes it : all_nodes)
      {
         String key = null;
         ArrayList<String> value = null;
         HashMap<String, ArrayList<String>> hash = it.GetAllRelationships(it);
         
         if(name_count.get(it.GetNodeName()) == 1)
         {
            out.printf("lone sig %s { ", it.GetNodeName());
         }
         else
         {
            out.printf("some sig %s { ", it.GetNodeName());
         }
         
         for(Map.Entry iterator : hash.entrySet())
         {
            key = (String)iterator.getKey();
            value = (ArrayList)iterator.getValue();
            
            out.printf("%s : ", key);
            for(int index = 0; index < value.size(); index++)
            {
               if(index == value.size() - 1)
               {
                  out.printf("%s", value.get(index));
               }
               else
               {
                  out.printf("%s + ", value.get(index));
               }
            }
            out.print(", ");
         }
         
         out.print("}\n");
      }
      
      out.close();
   }
}