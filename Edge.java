package javafxtesting;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;


import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;


public class Edge extends GraphItem
{
   private int edge_weight;
   private int edge_num;
   private boolean down_point_connection;
   private boolean open_menu;
   
   
   private String relation_name = null;
   private Nodes node_down;
   private Nodes node_end;
   private Group edge_group = new Group();
   private Text edge_text;
   private Line edge_self;
   
   private static int edge_count = 0;
   
   public Edge(String relation, double pos_arr[], Nodes node_down, Nodes node_end, AnchorPane background)
   {
      this.relation_name = relation;
      Line new_edge = new Line(pos_arr[0], pos_arr[1], pos_arr[2], pos_arr[3]);
      Text edge_text = new Text(Math.abs((pos_arr[0] - pos_arr[2]) / 2) + pos_arr[0], Math.abs((pos_arr[1] - pos_arr[3]) / 2) + pos_arr[1], relation);
      edge_text.setFont(Font.font("Times New Roman", 20));
      
      new_edge.setStrokeWidth(5.0);
      new_edge.setStroke(Color.BLACK);
      edge_self = new_edge;
      edge_num = edge_count++;
      this.node_down = node_down;
      this.node_end = node_end;
      this.edge_text = edge_text;
      
      edge_group.getChildren().addAll(new_edge, edge_text);
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
         open_menu = false;
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