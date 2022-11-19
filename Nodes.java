package javafxtesting;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;


public class Nodes extends GraphItem
{
   @FXML
   
   private final int NODEWIDTH = 75;
   private final int NODEHEIGHT = 75;
   private final int TEXTPADDINGHORIZONTAL = 5;
   
   private double translate_startX;
   private double translate_startY;
   
   private String node_name;
   private Pane edit_menu_pane;
   private Group node_group = new Group();
   private Text node_display_text;
   private Rectangle node_display_shape;
   private Edge single_edge_single_relationships;
   private ArrayList<Edge> edge_list = new ArrayList<>();
   
   
   private static int node_count;
   private static double down_points[] = new double[4];
   private static boolean open_menu = false;
   private static boolean node_hover = false;
   
   private static ArrayList<Nodes> relationships = new ArrayList<>();
   private static Nodes node_down;
   private static Nodes node_end;
   
   public Nodes(String node_name, MouseEvent event, AnchorPane background, Pane pane)
   {
      super(background);
      this.node_name = node_name;
      this.edit_menu_pane = pane;
      
      node_display_shape = new Rectangle(event.getSceneX() - (NODEWIDTH / 2), event.getSceneY() - (NODEHEIGHT / 2), NODEWIDTH, NODEHEIGHT);
      node_display_text = new Text(node_display_shape.getX() + TEXTPADDINGHORIZONTAL, node_display_shape.getY() + (NODEHEIGHT / 2), node_name);
      
      node_display_text.setWrappingWidth(NODEWIDTH - TEXTPADDINGHORIZONTAL);
      node_display_text.setFont(Font.font("Times New Roman", 20));
      
      node_display_shape.setFill(Color.ORANGE);
      node_display_shape.setStroke(Color.BLACK);
      node_display_shape.setStrokeWidth(3.0);
      
      node_group.setOnMouseClicked(events -> {
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
      
      DraggableAndDisableNode(node_group);
      
      node_group.getChildren().add(node_display_shape);
      node_group.getChildren().add(node_display_text);
      
      GetBackground().getChildren().add(node_group);
      node_count++;
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
         node_display_text.setText(text_field.getText());
         node_name = text_field.getText();
         node_display_shape.setStroke(Color.BLACK);
         GetBackground().getChildren().remove(edit_menu);
         open_menu = false;
      });
      
      node_display_shape.setStroke(Color.GREY);
      
      AnchorPane.setTopAnchor(text_field, 10.0);
      AnchorPane.setLeftAnchor(text_field, 10.0);
      AnchorPane.setRightAnchor(text_field, 100.0);
      AnchorPane.setBottomAnchor(text_field, 10.0);
      AnchorPane.setTopAnchor(confirm, 10.0);
      AnchorPane.setRightAnchor(confirm, 10.0);
      
      DraggableAndDisableNode(edit_menu);
      
      edit_menu.getChildren().addAll(confirm, text_field);
      GetBackground().getChildren().add(edit_menu);
   }
   
   public void AddEdge(MouseEvent event, Nodes node)
   {
      if(Edge.GetEdgeTool())
      {
         if(node_down == null)
         {
            node_down = node;
            down_points[0] = node.GetShape().getX() + (node.GetShape().getWidth() / 2);
            down_points[1] = node.GetShape().getY() + (node.GetShape().getHeight() / 2);
         }
         else
         {
            node_end = node;
            down_points[2] = node.GetShape().getX() + (node.GetShape().getWidth() / 2);
            down_points[3] = node.GetShape().getY() + (node.GetShape().getHeight() / 2);
            Edge edge = new Edge("relation", down_points, node_down, node_end, GetBackground());
            
            node_down.AddEdge(edge);
            AddEdge(edge);
            
            relationships.add(node_down);
            relationships.add(node_end);

            node_down = null;
            node_end = null;

            single_edge_single_relationships = edge;
            GetBackground().getChildren().add(edge.GetEdgeGroup());
         }
      }
   }
   
   public String GetRelationShip()
   {
      return single_edge_single_relationships.GetRelationName();
   }
   
   public static ArrayList GetRelationships()
   {
      return relationships;
   }
   
   public Rectangle GetShape()
   {
      return node_display_shape;
   }
   
   public void AddEdge(Edge edge)
   {
      edge_list.add(edge);
   }
   
   public void DraggableAndDisableNode(Node node)
   {
      node.setOnMouseEntered(events -> {
         if(GetNodeButton())
         {
            GetBackground().setCursor(Cursor.OPEN_HAND);
         }
      });
      
      node.setOnMouseExited(events -> {
         if(GetNodeButton())
         {
            GetBackground().setCursor(Cursor.DEFAULT);
         }
      });
      
      node.setOnMousePressed(events -> {
         translate_startX = events.getSceneX() - node.getTranslateX();
         translate_startY = events.getSceneY() - node.getTranslateY();
      });
      
      node.setOnMouseDragged(events ->{
         node.setTranslateX(events.getSceneX() - translate_startX);
         node.setTranslateY(events.getSceneY() - translate_startY);
      });
   }
}