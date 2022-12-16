package javafxtesting;

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

public class Nodes extends GraphItem
{   
   // determine the size of the node + text distance from edge
   private final int NODEWIDTH = 100;
   private final int NODEHEIGHT = 100;
   private final int TEXTPADDINGHORIZONTAL = 5;
   private final double STROKEWIDTH = 4;
   private final Color DEFAULTFILL = Color.WHITE;
   private final Color BORDERCOLOR = Color.BLACK;
   private final Color EDITBORDERCOLOR = Color.GAINSBORO;
   
   
   private String node_name;
   private Group javafx_node_group = new Group();
   private Text javafx_node_display_text;
   private Rectangle javafx_node_display_shape;
   private ArrayList<Edges> edge_list = new ArrayList<>();
   private ArrayList<Edges> edge_list_connected_to = new ArrayList<>();
   private ArrayList<Nodes> relationships = new ArrayList<>();
   
   private static double x_y_positions[] = new double[4];
   private static boolean is_edit_menu_open = false;
   private static boolean is_mouse_hovering_node = false;
   
   private static Nodes node_down;
   private static Nodes node_end;
   private static TextField javafx_text_field; 
   private static ArrayList<Nodes> all_nodes = new ArrayList<>();
   
   public Nodes(String node_name, MouseEvent event)
   {
      this.node_name = node_name;
      all_nodes.add(this);
      
      javafx_node_display_shape = new Rectangle(event.getSceneX() - (NODEWIDTH / 2), event.getSceneY() - (NODEHEIGHT / 2), NODEWIDTH, NODEHEIGHT);
      
      javafx_node_display_text = new Text(javafx_node_display_shape.getX() + TEXTPADDINGHORIZONTAL, javafx_node_display_shape.getY() + (NODEHEIGHT / 2), node_name);
      javafx_node_display_text.setWrappingWidth(NODEWIDTH - TEXTPADDINGHORIZONTAL);
      javafx_node_display_text.setFont(Font.font("Times New Roman", 20));
      
      javafx_node_display_shape.setFill(DEFAULTFILL);
      javafx_node_display_shape.setStroke(BORDERCOLOR);
      javafx_node_display_shape.setStrokeWidth(STROKEWIDTH);
      
      javafx_node_group.getChildren().add(javafx_node_display_shape);
      javafx_node_group.getChildren().add(javafx_node_display_text);
      
      // add text and shape to background
      GetBackground().getChildren().add(javafx_node_group);
      ApplyNodeFunctions(javafx_node_group);
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
   
   public static ArrayList<Nodes> GetAllNodesArrayList()
   {
      return all_nodes;
   }
   
   public Rectangle GetRectangle()
   {
      return javafx_node_display_shape;
   }
   
   public ArrayList<Edges> GetEdgeList()
   {
      return edge_list;
   }
   
   public boolean GetNodeHover()
   {
      return is_mouse_hovering_node;
   }
   
   public void SetNodeHoverOn()
   {
      is_mouse_hovering_node = true;
   }
   
   public void SetNodeHoverOff()
   {
      is_mouse_hovering_node = false;
   }
   
   public static void SetTextField(TextField field)
   {
      javafx_text_field = field;
   }
   
   public void AddNodeToList(Nodes new_node)
   {
      all_nodes.add(new_node);
   }
   
   public void AddEdge(MouseEvent event, Nodes node)
   {
      //on the second click of a node, connect both nodes with an edge
      if(GetEdgeTool())
      {
         if(node_down == null)
         {
            node_down = node;
            node_down.GetRectangle().setStroke(EDITBORDERCOLOR);
            x_y_positions[0] = node.javafx_node_display_shape.getX() + (node.javafx_node_display_shape.getWidth() / 2);
            x_y_positions[1] = node.javafx_node_display_shape.getY() + (node.javafx_node_display_shape.getHeight() / 2);
         }
         else
         {
            node_end = node;
            x_y_positions[2] = node.javafx_node_display_shape.getX() + (node.javafx_node_display_shape.getWidth() / 2);
            x_y_positions[3] = node.javafx_node_display_shape.getY() + (node.javafx_node_display_shape.getHeight() / 2);
            Edges edge = new Edges(javafx_text_field.getText(), x_y_positions, node_down, node_end, GetBackground());
            
            node_down.GetRectangle().setStroke(BORDERCOLOR);
            node_down.relationships.add(node_end);
            node_down.edge_list.add(edge);
            node_end.edge_list_connected_to.add(edge);

            node_down = null;
            node_end = null;

            GetBackground().getChildren().add(edge.GetEdgeGroup());
         }
      }
   }
   
   public void ApplyNodeFunctions(Node javafx_node)
   {
      javafx_node.setOnMouseEntered(events -> {
         if(GetNodeButton())
         {
            SetNodeHoverOn();
            SetNodeToolOff();
            GetBackground().setCursor(Cursor.DEFAULT);
         }
      });
      
      javafx_node.setOnMouseExited(events -> {
         if(GetNodeButton())
         {
            SetNodeHoverOff();
            SetNodeToolOn();
            GetBackground().setCursor(Cursor.CROSSHAIR);
         }
      });
      
      javafx_node.setOnMouseClicked(events -> {
         if(GetEdgeTool())
         {
            AddEdge(events, this);
         }
         
         if(GetNodeButton())
         {
            if(GetNodeHover() && !is_edit_menu_open)
            {
               SetNodeToolOff();
               EditNode(events);
               is_edit_menu_open = true;
            }
         }
      });
   }
   
   public void EditNode(MouseEvent event)
   {      
      // create edit menu + edit menu contents
      AnchorPane javafx_edit_menu_background = new AnchorPane();
      TextField javafx_text_field_edit = new TextField();
      Button javafx_confirm_button = new Button("Confirm");
      Button javafx_delete_button = new Button("Delete");
      
      javafx_edit_menu_background.setStyle("-fx-background-color: #CECECE; -fx-border-color: black; -fx-border-width: 1px;");
      javafx_text_field_edit.setText(node_name);
      
      javafx_node_display_shape.setStroke(EDITBORDERCOLOR);
      
      javafx_edit_menu_background.setPrefWidth(210);
      javafx_edit_menu_background.setPrefHeight(80);
      javafx_edit_menu_background.setLayoutX(event.getX());
      javafx_edit_menu_background.setLayoutY(event.getY());
      
      // set positions of the edit menu contents within the background
      AnchorPane.setTopAnchor(javafx_text_field_edit, 10.0);
      AnchorPane.setLeftAnchor(javafx_text_field_edit, 10.0);
      AnchorPane.setRightAnchor(javafx_text_field_edit, 10.0);
      
      AnchorPane.setLeftAnchor(javafx_confirm_button, 10.0);
      AnchorPane.setBottomAnchor(javafx_confirm_button, 10.0);
      
      AnchorPane.setRightAnchor(javafx_delete_button, 10.0);
      AnchorPane.setBottomAnchor(javafx_delete_button, 10.0);
      
      // add all the edit menu contents to the background 
      javafx_edit_menu_background.getChildren().addAll(javafx_confirm_button, javafx_text_field_edit, javafx_delete_button);
      GetBackground().getChildren().add(javafx_edit_menu_background);
      
      // when the confirm button clicked, update the node text, name, and change stroke color back to black
      javafx_confirm_button.setOnAction(eh -> {
         javafx_node_display_text.setText(javafx_text_field_edit.getText());
         node_name = javafx_text_field_edit.getText();
         javafx_node_display_shape.setStroke(BORDERCOLOR);
         GetBackground().getChildren().remove(javafx_edit_menu_background);
         is_edit_menu_open = false;
      });
      
      // when the delete button clicked, remove from the background, remove from the static node list, 
      // and delete the edges its connected to + edges are connected to it
      javafx_delete_button.setOnAction(eh -> {
         GetBackground().getChildren().remove(javafx_node_group);
         System.out.println(all_nodes.size());
         all_nodes.remove(this);
         System.out.println(all_nodes.size());
         for(Edges edge_it : edge_list)
         {
            edge_it.RemoveJavafxEdge();
         }
         for(Edges edge_it : edge_list_connected_to)
         {
            edge_it.RemoveJavafxEdge();
            edge_it.GetNodeDown().GetEdgeList().remove(edge_it);
         }
         is_edit_menu_open = false;
         GetBackground().getChildren().remove(javafx_edit_menu_background);
      });
      
      javafx_edit_menu_background.setOnMouseEntered(events -> {
         SetNodeToolOff();
      });
      
      javafx_edit_menu_background.setOnMouseExited(events -> {
         SetNodeToolOn();
      });
   }
}