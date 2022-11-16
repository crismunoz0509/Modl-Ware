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

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;


public class Controller
{   
   @FXML
   private AnchorPane background;
   private Pane editpane;
   
   public void CanvasClick(MouseEvent event)
   {
      if(Nodes.GetNodeButton() && Nodes.GetNodeTool())
      {
         Nodes new_node = new Nodes("random_name", event, background, editpane);
      }
   }
   
   public void NodeButton()
   {
      Nodes.NodeButtonOn();
   }
   
   public void EdgeButton()
   {
      Edge.EdgeButtonOn();
   }
   
   public void OutputClick() throws FileNotFoundException
   {
      File fh = new File("alloy_model.als");
      PrintWriter out = new PrintWriter(fh);
      
      ArrayList<Nodes> thingy = Nodes.GetRelationships();
      ArrayList<String> names = new ArrayList<>();
      ArrayList<String> seen_names = new ArrayList<>();
      
      int count = 1;
      for(Nodes it : thingy)
      {
         names.add(it.GetNodeName());
         if((count % 2) == 0) 
         {
            names.add(it.GetRelationShip());
         }
         count += 1;
      }
      
      for(int x = 0; x < names.size(); x += 3)
      {
         out.printf("sig %s { %s : %s }\n", names.get(x), names.get(x+2), names.get(x+1));

         if (!seen_names.contains(names.get(x+2)))
         {
            out.printf("sig %s { }\n", names.get(x+1));
            seen_names.add(names.get(x+1));
         }
      }
      
      out.close();
   }
}