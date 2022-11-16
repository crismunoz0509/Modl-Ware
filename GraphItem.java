package javafxtesting;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import static javafxtesting.Nodes.GetNodeButton;

class GraphItem
{
   @FXML
   
   GraphItem(Anchor)
   {
      
   }
   
   public void DraggableAndDisableNode(Node node)
   {
      node.setOnMouseEntered(events -> {
         if(GetNodeButton())
         {
            background.setCursor(Cursor.OPEN_HAND);
         }
      });
      
      node.setOnMouseExited(events -> {
         if(GetNodeButton())
         {
            background.setCursor(Cursor.DEFAULT);
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