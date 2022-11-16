package javafxtesting;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Nodes
{
   @FXML
   
   private int node_num;
   private double startX;
   private double startY;
   private final int NODEWIDTH = 75;
   private final int NODEHEIGHT = 75;
   private final int TEXTPADDINGHORIZONTAL = 5;
   
   private static int node_count;
   
   private static double down_points[] = new double[4];
   private static boolean open_menu = false;
   private static boolean node_tool = true;
   private static boolean node_hover = false;
   private static boolean node_button = false;
   private static ArrayList<Nodes> relationships = new ArrayList<>();
   
   private String node_name;
   private Rectangle node_shape;
   private ArrayList<Edge> edge_list = new ArrayList<>();
   private ArrayList<Boolean> down_point_connection = new ArrayList<>();
   private AnchorPane background;
   private Pane editpane;
   private Group group = new Group();
   private Node selected_node;
   private Edge single_edge;
   private static Nodes node_down;
   private static Nodes node_end;
   private Text node_text;
   
   public Nodes(String node_name, MouseEvent event, AnchorPane background, Pane pane)
   {
      this.node_name = node_name;
      this.background = background;
      this.editpane = pane;
      
      Rectangle new_node = new Rectangle(event.getSceneX() - (NODEWIDTH / 2), event.getSceneY() - (NODEHEIGHT / 2), NODEWIDTH, NODEHEIGHT);
      Text node_text = new Text(new_node.getX() + TEXTPADDINGHORIZONTAL, new_node.getY() + (NODEHEIGHT / 2), node_name);
      
      node_text.setWrappingWidth(NODEWIDTH - TEXTPADDINGHORIZONTAL);
      node_text.setFont(Font.font("Times New Roman", 20));
      
      new_node.setFill(Color.ORANGE);
      new_node.setStroke(Color.BLACK);
      new_node.setStrokeWidth(3.0);
      
      group.setOnMouseClicked(events -> {
         if(Edge.GetEdgeTool())
         {
            AddEdge(events, this);
         }
         
         if(GetNodeButton())
         {
            if(CheckNodeHover() && !open_menu)
            {
               EditNode(events);
               open_menu = true;
            }
         }
      });
      
      DraggableAndDisableNode(group);
      
      group.setOnMousePressed(events -> {
         startX = events.getSceneX() - group.getTranslateX();
         startY = events.getSceneY() - group.getTranslateY();
      });
      
      group.setOnMouseDragged(events ->{
         group.setTranslateX(events.getSceneX() - startX);
         group.setTranslateY(events.getSceneY() - startY);
         
         int count = 0;
         System.out.print(edge_list);
         System.out.print(down_point_connection);
         for(Edge it : edge_list)
         {
            
            if(down_point_connection.get(count))
            {
               it.GetEdge().setStartX(node_shape.getX() + (node_shape.getWidth() / 2));
               it.GetEdge().setStartY(node_shape.getY() + (node_shape.getHeight() / 2));
            }
            else
            {
               it.GetEdge().setEndX(node_shape.getX() + (node_shape.getWidth() / 2));
               it.GetEdge().setEndY(node_shape.getY() + (node_shape.getHeight() / 2));
            }
            count += 1;
         }
      });
      
      node_shape = new_node;
      this.node_text = node_text;
      
      group.getChildren().add(node_shape);
      group.getChildren().add(node_text);
      
      this.background.getChildren().add(group);
      node_num = node_count++;
   }
   
   public String GetNodeName()
   {
      return node_name;
   }
   
   public void NodeHoverOn()
   {
      node_hover = true;
   }
   
   public void NodeHoverOff()
   {
      node_hover = false;
   }
   
   public boolean CheckNodeHover()
   {
      return node_hover;
   }
   
   public void EditNode(MouseEvent event)
   {
      AnchorPane edit_menu = new AnchorPane();
      TextField text_field = new TextField();
      Button confirm = new Button("Confirm");
      
      edit_menu.setStyle("-fx-background-color: #CECECE;");
      
      text_field.setText(node_name);
      
      confirm.setOnAction(eh -> {
         node_text.setText(text_field.getText());
         node_name = text_field.getText();
         node_shape.setStroke(Color.BLACK);
         background.getChildren().remove(edit_menu);
         open_menu = false;
      });
      
      node_shape.setStroke(Color.GREY);
      
      AnchorPane.setTopAnchor(text_field, 10.0);
      AnchorPane.setLeftAnchor(text_field, 10.0);
      AnchorPane.setRightAnchor(text_field, 100.0);
      AnchorPane.setBottomAnchor(text_field, 10.0);
      AnchorPane.setTopAnchor(confirm, 10.0);
      AnchorPane.setRightAnchor(confirm, 10.0);
      
      DraggableAndDisableNode(edit_menu);
      
      edit_menu.getChildren().addAll(confirm, text_field);
      background.getChildren().add(edit_menu);
   }
   
   public void AddEdge(MouseEvent event, Nodes node)
   {
      if(Edge.GetEdgeTool())
      {
         if(node_down == null)
         {
            node_down = node;
            down_points[0] = node.node_shape.getX() + (node.node_shape.getWidth() / 2);
            down_points[1] = node.node_shape.getY() + (node.node_shape.getHeight() / 2);
         }
         else
         {
            node_end = node;
            down_points[2] = node.node_shape.getX() + (node.node_shape.getWidth() / 2);
            down_points[3] = node.node_shape.getY() + (node.node_shape.getHeight() / 2);
            Edge edge = new Edge("relation", down_points, node_down, node_end, background);
            
            node_down.AddEdge(edge);
            node_down.down_point_connection.add(true);
            AddEdge(edge);
            down_point_connection.add(false);
            
            
            relationships.add(node_down);
            relationships.add(node_end);

            node_down = null;
            node_end = null;

            single_edge = edge;
            background.getChildren().add(edge.GetEdgeGroup());
         }
      }
   }
   
   public String GetRelationShip()
   {
      return single_edge.GetRelationName();
   }
   
   public static ArrayList GetRelationships()
   {
      return relationships;
   }
   
   public Rectangle GetShape()
   {
      return node_shape;
   }
   
   public void AddEdge(Edge edge)
   {
      edge_list.add(edge);
   }
   
   public static void NodeButtonOn()
   {
      node_button = true;
   }
   
   public static void NodeButtonOff()
   {
      node_button = false;
   }
   
   public static boolean GetNodeButton()
   {
      return node_button;
   }
   
   public static void NodeToolOn(AnchorPane background)
   {
      node_tool = true;
      background.setCursor(Cursor.CROSSHAIR);
   }
   
   public static void NodeToolOff(AnchorPane background)
   {
      node_tool = false;
      background.setCursor(Cursor.DEFAULT);
   }
   
   public static boolean GetNodeTool()
   {
      return node_tool;
   }
   
   public static int GetNodeCount()
   {
      return node_count;
   }
   
   public void DraggableAndDisableNode(Node node)
   {
      node.setOnMouseEntered(events -> {
         if(GetNodeButton())
         {
            background.setCursor(Cursor.DEFAULT);
            NodeToolOff(background);
            NodeHoverOn();
         }
      });
      
      node.setOnMouseExited(events -> {
         if(GetNodeButton())
         {
            background.setCursor(Cursor.CROSSHAIR);
            NodeToolOn(background);
            NodeHoverOff();
         }
      });
      
      node.setOnMousePressed(events -> {
         startX = events.getSceneX() - node.getTranslateX();
         startY = events.getSceneY() - node.getTranslateY();
      });
      
      node.setOnMouseDragged(events ->{
         node.setTranslateX(events.getSceneX() - startX);
         node.setTranslateY(events.getSceneY() - startY);
      });
   }
}