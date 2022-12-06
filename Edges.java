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

public class Edges extends GraphItem
{
   private boolean down_point_connection;
   private boolean open_menu;
   
   private String relation_name = null;
   private Nodes node_down;
   private Nodes node_end;
   private Group edge_group = new Group();
   private Text edge_text;
   private Line edge_self;
   
   private static int edge_count = 0;
   
   public Edges(String relation, double pos_arr[], Nodes node_down, Nodes node_end, AnchorPane background)
   {
      this.relation_name = relation;
      this.node_down = node_down;
      this.node_end = node_end;
      
      double adjusted[] = AdjustPosition(pos_arr);
      Line new_edge = new Line(adjusted[0], adjusted[1], adjusted[2], adjusted[3]);
      new_edge.toBack();
      Circle new_circle = new Circle(adjusted[2], adjusted[3], 5);
      
      double copy[] = GetMiddlePosition(adjusted.clone());
      Text edge_text = new Text(copy[0], copy[1], relation);
      edge_text.setFont(Font.font("Times New Roman", 20));
      
      new_edge.setStrokeWidth(5.0);
      new_edge.setStroke(Color.BLACK);
      edge_self = new_edge;
      this.edge_text = edge_text;
      
      edge_group.getChildren().addAll(new_edge, edge_text, new_circle);
      edge_group.setOnMouseClicked(eh -> {
         EditEdge(eh, background);
      });
   }
   
   public Group GetEdgeGroup()
   {
      return edge_group;
   }
   
   public boolean GetDownPointConnection()
   {
      return down_point_connection;
   }
   
   public Nodes GetNodeDown()
   {
      return node_down;
   }
   
   public Nodes GetNodeEnd()
   {
      return node_end;
   }
   
   public int GetEdgeCount()
   {
      return edge_count;
   }
   
   public Line GetEdge()
   {
      return edge_self;
   }
   
   public String GetRelationName()
   {
      return relation_name;
   }
   
   public double[] AdjustPosition(double pos[])
   {
      double startx = pos[0], endx = pos[2], starty = pos[1], endy = pos[3];
      double output_position[] = new double[4];
      
      output_position[0] = startx;
      output_position[1] = starty;
      
      if(startx <= (endx - node_end.GetNodeWidth()))
      {
         output_position[2] = endx - (node_end.GetNodeWidth() / 2);
         output_position[0] = startx + (node_end.GetNodeWidth() / 2);
      }
      else if(startx >= (endx + node_end.GetNodeWidth()))
      {
         output_position[2] = endx + (node_end.GetNodeWidth() / 2);
         output_position[0] = startx - (node_end.GetNodeWidth() / 2);
      }
      else
      {
         output_position[2] = endx;
      }
      
      if(starty <= (endy - node_end.GetNodeHeight()))
      {
         output_position[3] = endy - (node_end.GetNodeHeight() / 2);
         output_position[1] = starty + (node_end.GetNodeHeight() / 2);
      }
      else if(starty >= (endy + node_end.GetNodeHeight()))
      {
         output_position[3] = endy + (node_end.GetNodeHeight() / 2);
         output_position[1] = starty - (node_end.GetNodeHeight() / 2);
      }
      else
      {
         output_position[3] = endy;
      }
      
      return output_position;
   }
   
   public double[] GetMiddlePosition(double pos[])
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
   
   public void EditEdge(MouseEvent event, AnchorPane background)
   {
      AnchorPane edit_menu = new AnchorPane();
      TextField text_field = new TextField();
      Button confirm = new Button("Confirm");
      
      edit_menu.setStyle("-fx-background-color: #CECECE;");
      
      text_field.setText(relation_name);
      
      confirm.setOnAction(eh -> {
         edge_text.setText(text_field.getText());
         relation_name = text_field.getText();
         edge_self.setStroke(Color.BLACK);
         background.getChildren().remove(edit_menu);
      });
      
      edge_self.setStroke(Color.GREY);
      
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
      background.getChildren().add(edit_menu);
   }
   
   public static void EdgeButtonOn()
   {
      Nodes.NodeButtonOff();
      Nodes.NodeToolOff();
      EdgeToolOn();
   }
}