package javafxtesting;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

class GraphItem
{
   private static AnchorPane background;
   private static Pane edit_menu;
   
   private double translate_startX;
   private double translate_startY;
   
   private static boolean node_button;
   private static boolean node_tool;
   private static boolean edge_button;
   private static boolean edge_tool;
   
   public static void SetNodeButtonOn()
   {
      node_button = true;
      SetNodeToolOn();
      Edges.SetEdgeToolOff();
   }
   
   public static void SetNodeButtonOff()
   {
      node_button = false;
   }
   
   public static void SetNodeToolOn()
   {
      node_tool = true;
   }
   
   public static void SetNodeToolOff()
   {
      node_tool = false;
   }
   
   public static void SetEdgeButtonOn()
   {
      SetNodeButtonOff();
      SetNodeToolOff();
      SetEdgeToolOn();
      edge_button = false;
   }
   
   public static void SetEdgeButtonOff()
   {
      SetEdgeToolOff();
      edge_button = false;
   }
   
   public static void SetEdgeToolOn()
   {
      edge_tool = true;
   }
   
   public static void SetEdgeToolOff()
   {
      edge_tool = false;
   }
   
   public void SetStartX(double new_x_position)
   {
      translate_startX = new_x_position;
   }
   
   public void SetStartY(double new_y_position)
   {
      translate_startY = new_y_position;
   }
   
   public static void SetBackground(AnchorPane new_background)
   {
      background = new_background;
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
   
   public double GetStartX()
   {
      return translate_startX;
   }
   
   public double GetStartY()
   {
      return translate_startY;
   }
   
   public static AnchorPane GetBackground()
   {
      return background;
   }
   
   public static Pane GetEditPane()
   {
      return edit_menu;
   }
}