package javafxtesting;

import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import static javafxtesting.GraphItem.GetBackground;

public class Edges extends GraphItem
{
   private boolean is_menu_open;
   
   private final double ENDPOINTRADIUS = 5;
   private final double LINEWIDTH = 2;
   private final double LINEWIDTHEDITSIZE = 6;
   private final Color LINECOLOR = Color.BLACK;
   private final Color LINEEDITCOLOR = Color.GAINSBORO;
           
   private String relation_name;
   private Nodes node_start;
   private Nodes node_end;
   private Group javafx_edge_group = new Group();
   private Text javafx_edge_text;
   private Line javafx_edge;
   
   public Edges(String relation_name, double position_array[], Nodes node_start, Nodes node_end, AnchorPane javafx_background)
   {
      this.relation_name = relation_name;
      this.node_start = node_start;
      this.node_end = node_end;
      
      double x_start_position, y_start_position, x_end_position, y_end_position;
      double x_edge_middle_position, y_edge_middle_position;
      double adjusted_position_array[] = null;
      Line javafx_edge = null;
      Text javafx_edge_text = null;
      
      // adjust position array values so lines touch the "edges" of a node and not the node center
      adjusted_position_array= AdjustPositionArray(position_array);
      x_start_position = adjusted_position_array[0];
      y_start_position = adjusted_position_array[1];
      x_end_position = adjusted_position_array[2];
      y_end_position = adjusted_position_array[3];
      
      // create and draw line 
      javafx_edge = new Line(x_start_position, y_start_position, x_end_position, y_end_position);
      
      // move edge z position back
      javafx_edge.toBack();
      
      // create and draw circle
      Circle javafx_circle = new Circle(x_end_position, y_end_position, ENDPOINTRADIUS);
      
      double edge_middle_position_array[] = GetArrayMiddlePosition(adjusted_position_array.clone());
      
      x_edge_middle_position = edge_middle_position_array[0];
      y_edge_middle_position = edge_middle_position_array[1];
      javafx_edge_text = new Text(x_edge_middle_position, y_edge_middle_position, relation_name);
      javafx_edge_text.setFont(Font.font("Times New Roman", 20));
      javafx_edge.setStrokeWidth(LINEWIDTH);
      javafx_edge.setStroke(LINECOLOR);
      
      // add objects to group and display group on the background to make them visible to user
      javafx_edge_group.getChildren().addAll(javafx_edge, javafx_edge_text, javafx_circle);
      javafx_edge_group.setOnMouseClicked(event_handler -> {
         EditEdgeFunction(event_handler, javafx_background);
      });
      
      this.javafx_edge = javafx_edge;
      this.javafx_edge_text = javafx_edge_text;
   }
   
   public Group GetEdgeGroup()
   {
      return javafx_edge_group;
   }
   
   public Nodes GetNodeDown()
   {
      return node_start;
   }
   
   public Nodes GetNodeEnd()
   {
      return node_end;
   }
   
   public String GetRelationName()
   {
      return relation_name;
   }
   
   public void RemoveJavafxEdge()
   {
      GetBackground().getChildren().remove(javafx_edge_group);
   }
   
   public double[] AdjustPositionArray(double pos[])
   {
      double startx = pos[0], endx = pos[2], starty = pos[1], endy = pos[3];
      double output_position[] = new double[4];
      
      double top_start_y = starty - (node_end.GetNodeHeight() / 2);
      double bottom_start_y = starty + (node_end.GetNodeHeight() / 2);
      double right_start_x = startx + (node_end.GetNodeWidth() / 2);
      double left_start_x = startx - (node_end.GetNodeWidth() / 2);
      
      
      if( ( bottom_start_y < endy ))
      {
         if(right_start_x < endx)
         {
            if((Math.sin(45) * (endx - right_start_x)) + bottom_start_y < endy)
            {
               output_position[0] = startx;
               output_position[1] = bottom_start_y;
            }
            else
            {
               output_position[0] = right_start_x;
               output_position[1] = starty;
            }
         }
         else if(left_start_x > endx)
         {
            if((Math.sin(45) * (left_start_x - endx)) + bottom_start_y < endy)
            {
               output_position[0] = startx;
               output_position[1] = bottom_start_y;
            }
            else
            {
               output_position[0] = left_start_x;
               output_position[1] = starty;
            }
         }
         else
         {
            output_position[0] = startx;
            output_position[1] = bottom_start_y;
         }
      }
      else if( top_start_y > endy)
      {
         if(right_start_x < endx)
         {
            if(top_start_y - (Math.sin(45) * (endx - right_start_x)) > endy)
            {
               output_position[0] = startx;
               output_position[1] = top_start_y;
            }
            else
            {
               output_position[0] = right_start_x;
               output_position[1] = starty;
            }
         }
         else if(left_start_x > endx)
         {
            if(top_start_y - (Math.sin(45) * (left_start_x - endx)) >= endy)
            {
               output_position[0] = startx;
               output_position[1] = top_start_y;
            }
            else
            {
               output_position[0] = left_start_x;
               output_position[1] = starty;
            }
         }
         else
         {
            output_position[0] = startx;
            output_position[1] = top_start_y;
         }
      }
      else
      {
         if(right_start_x < endx)
         {
            output_position[0] = right_start_x;
            output_position[1] = starty;
         }
         else if(left_start_x > endx)
         {
            output_position[0] = left_start_x;
            output_position[1] = starty;
         }
      }
      
      output_position[2] = endx;
      output_position[3] = endy;
      
      return output_position;
   }
   
   public double[] GetArrayMiddlePosition(double pos[])
   {
      double startx = pos[0], endx = pos[2], starty = pos[1], endy = pos[3];
      double halfwayx = 0, halfwayy = 0;
      double output_position[] = new double[2];
      
      if(startx <= endx)
      {
         halfwayx = (endx - startx) / 2;
         output_position[0] = startx + halfwayx;
      }
      else if(startx >= endx)
      {
         halfwayx = (startx - endx) / 2;
         output_position[0] = endx + halfwayx;
      }
      
      if(starty <= endy)
      {
         halfwayy = (endy - starty) / 2;
         output_position[1] = starty + halfwayy;
      }
      else if(starty >= endy)
      {
         halfwayy = (starty - endy) / 2;
         output_position[1] = endy + halfwayy;
      }
      
      return output_position;
   }
   
   public void EditEdgeFunction(MouseEvent event, AnchorPane background)
   {
      // create edit menu + edit menu contents
      AnchorPane javafx_edit_menu_background = new AnchorPane();
      TextField javafx_text_field = new TextField();
      Button javafx_confirm_button = new Button("Confirm");
      Button javafx_delete_button = new Button("Delete");
      
      javafx_edit_menu_background.setStyle("-fx-background-color: #CECECE; -fx-border-color: black; -fx-border-width: 1px;");
      javafx_text_field.setText(relation_name);
      
      javafx_edge.setStroke(LINEEDITCOLOR);
      javafx_edge.setStrokeWidth(LINEWIDTHEDITSIZE);
      
      javafx_edit_menu_background.setPrefWidth(210);
      javafx_edit_menu_background.setPrefHeight(80);
      javafx_edit_menu_background.setLayoutX(event.getX());
      javafx_edit_menu_background.setLayoutY(event.getY());
      
      // set positions of the edit menu contents within the background
      AnchorPane.setTopAnchor(javafx_text_field, 10.0);
      AnchorPane.setLeftAnchor(javafx_text_field, 10.0);
      AnchorPane.setRightAnchor(javafx_text_field, 10.0);
      
      AnchorPane.setLeftAnchor(javafx_confirm_button, 10.0);
      AnchorPane.setBottomAnchor(javafx_confirm_button, 10.0);
      
      AnchorPane.setRightAnchor(javafx_delete_button, 10.0);
      AnchorPane.setBottomAnchor(javafx_delete_button, 10.0);
      
      // add all the edit menu contents to the background 
      javafx_edit_menu_background.getChildren().addAll(javafx_confirm_button, javafx_text_field, javafx_delete_button);
      GetBackground().getChildren().add(javafx_edit_menu_background);
      
      // when the confirm button clicked, update the edge text, name, and change color back to black
      javafx_confirm_button.setOnAction(event_handler -> {
         javafx_edge_text.setText(javafx_text_field.getText());
         relation_name = javafx_text_field.getText();
         javafx_edge.setStroke(LINECOLOR);
         javafx_edge.setStrokeWidth(LINEWIDTH);
         
         is_menu_open = false;
         background.getChildren().remove(javafx_edit_menu_background);
      });
      
      // when the delete button clicked, delete the object from the background & remove this edge from edge lists
      javafx_delete_button.setOnAction(event_handler -> {
         GetBackground().getChildren().remove(javafx_edge_group);
         node_start.GetEdgeList().remove(this);
         node_end.GetEdgeList().remove(this);

         is_menu_open = false;
         GetBackground().getChildren().remove(javafx_edit_menu_background);
      });
   }
}