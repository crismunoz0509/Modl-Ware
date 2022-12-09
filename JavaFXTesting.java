/*
 * Cristian Munoz 1001817503
 */
package javafxtesting;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Parent;
import javafx.stage.Stage;
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
         Scene scene = new Scene(root, 1920, 1080);
         
         primaryStage.setScene(scene);
         primaryStage.show();
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
