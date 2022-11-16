/*
 * Cristian Munoz 1001817503
 */
package javafxtesting;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class JavaFXTesting extends Application
{
   public static void main(String[] args)
   {
      launch(args);
   }
   
   @Override
   public void start(Stage primaryStage) throws Exception
   {
      try
      {
         Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
         Scene scene = new Scene(root, 400, 400);
         
         primaryStage.setScene(scene);
         primaryStage.show();
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }
}
