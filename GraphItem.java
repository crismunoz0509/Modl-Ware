package javafxtesting;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

class GraphItem
{
   @FXML
           
   private static AnchorPane background;
   
   private static boolean node_button;
   private static boolean node_tool;
   private static boolean edge_button;
   private static boolean edge_tool;
   
   GraphItem(AnchorPane background)
   {
      this.background = background;
   }
   
   public static void NodeButtonOn()
   {
      node_button = true;
      NodeToolOn();
      Edge.EdgeToolOff();
   }
   
   public static void NodeButtonOff()
   {
      node_button = false;
   }
   
   public static void NodeToolOn()
   {
      node_tool = true;
   }
   
   public static void NodeToolOff()
   {
      node_tool = false;
   }
   
   public static void EdgeButtonOn()
   {
      NodeButtonOff();
      NodeToolOff();
      EdgeToolOn();
      edge_button = false;
   }
   
   public static void EdgeButtonOff()
   {
      EdgeToolOff();
      edge_button = false;
   }
   
   public static void EdgeToolOn()
   {
      edge_tool = true;
   }
   
   public static void EdgeToolOff()
   {
      edge_tool = false;
   }
   
   public static boolean GetEdgeButton()
   {
      return edge_button;
   }
   
   public static boolean GetNodeButton()
   {
      return node_button;
   }
   
   public static boolean GetNodeTool()
   {
      return node_tool;
   }
   
   public static boolean GetEdgeTool()
   {
      return edge_tool;
   }
   
   public static AnchorPane GetBackground()
   {
      return background;
   }
}