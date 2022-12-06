package javafxtesting;

import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Cursor;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class Nodes extends GraphItem
{
   @FXML
   
   private final int NODEWIDTH = 75;
   private final int NODEHEIGHT = 75;
   private final int TEXTPADDINGHORIZONTAL = 5;
   
   private String node_name;
   private Group node_group = new Group();
   private Text node_display_text;
   private Rectangle node_display_shape;
   private ArrayList<Edges> edge_list = new ArrayList<>();
   private ArrayList<Nodes> relationships = new ArrayList<>();
   
   private static double down_points[] = new double[4];
   private static boolean open_menu = false;
   private static boolean node_hover = false;
   
   private static Nodes node_down;
   private static Nodes node_end;
   private static ArrayList<Nodes> all_nodes = new ArrayList<>();
   
   public Nodes(String node_name, MouseEvent event)
   {
      this.node_name = node_name;
      AddNodeToList(this);
      
      node_display_shape = new Rectangle(event.getSceneX() - (NODEWIDTH / 2), event.getSceneY() - (NODEHEIGHT / 2), NODEWIDTH, NODEHEIGHT);
      
      node_display_text = new Text(node_display_shape.getX() + TEXTPADDINGHORIZONTAL, node_display_shape.getY() + (NODEHEIGHT / 2), node_name);
      node_display_text.setWrappingWidth(NODEWIDTH - TEXTPADDINGHORIZONTAL);
      node_display_text.setFont(Font.font("Times New Roman", 20));
      
      node_display_shape.setFill(Color.ORANGE);
      node_display_shape.setStroke(Color.BLACK);
      node_display_shape.setStrokeWidth(3.0);
      
      node_group.getChildren().add(node_display_shape);
      node_group.getChildren().add(node_display_text);
      
      GetBackground().getChildren().add(node_group);
      NodeFunctions(node_group);
   }
   
   public String GetNodeName()
   {
      return node_name;
   }
   
   public int GetNodeWidth()
   {
      return NODEWIDTH;
   }
   
   public int GetNodeHeight()
   {
      return NODEWIDTH;
   }
   
   public static ArrayList<Nodes> GetAllNodes()
   {
      return all_nodes;
   }
   
   public ArrayList<Edges> GetEdgeList()
   {
      return edge_list;
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
   
   public void AddNodeToList(Nodes new_node)
   {
      all_nodes.add(new_node);
   }
   
   public HashMap<String, Integer> ReturnEmptyRelationShipCounts()
   {
      HashMap<String, Integer> empty_count = new HashMap<>();
      
      
      
      return empty_count;
   }
   
   public void AddEdge(MouseEvent event, Nodes node)
   {
      if(Edges.GetEdgeTool())
      {
         if(node_down == null)
         {
            node_down = node;
            down_points[0] = node.node_display_shape.getX() + (node.node_display_shape.getWidth() / 2);
            down_points[1] = node.node_display_shape.getY() + (node.node_display_shape.getHeight() / 2);
         }
         else
         {
            node_end = node;
            down_points[2] = node.node_display_shape.getX() + (node.node_display_shape.getWidth() / 2);
            down_points[3] = node.node_display_shape.getY() + (node.node_display_shape.getHeight() / 2);
            Edges edge = new Edges("relation", down_points, node_down, node_end, GetBackground());
            
            node_down.relationships.add(node_end);
            node_down.edge_list.add(edge);

            node_down = null;
            node_end = null;

            GetBackground().getChildren().add(edge.GetEdgeGroup());
         }
      }
   }
   
   public void NodeFunctions(Node node)
   {
      node.setOnMouseEntered(events -> {
         if(GetNodeButton())
         {
            NodeHoverOn();
            NodeToolOff();
            GetBackground().setCursor(Cursor.DEFAULT);
         }
      });
      
      node.setOnMouseExited(events -> {
         if(GetNodeButton())
         {
            NodeHoverOff();
            NodeToolOn();
            GetBackground().setCursor(Cursor.CROSSHAIR);
         }
      });
      
      node.setOnMouseClicked(events -> {
         if(Edges.GetEdgeTool())
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
      
      edit_menu.setOnMousePressed(events -> {
         SetStartX(events.getSceneX() - edit_menu.getTranslateX());
         SetStartY(events.getSceneY() - edit_menu.getTranslateY());
      });
      
      edit_menu.setOnMouseDragged(events ->{
         edit_menu.setTranslateX(events.getSceneX() - GetStartX());
         edit_menu.setTranslateY(events.getSceneY() - GetStartY());
      });
      
      edit_menu.getChildren().addAll(confirm, text_field);
      GetBackground().getChildren().add(edit_menu);
   }
}