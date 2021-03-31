package sample;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

   Stage stage;
   Parent scene;

   @FXML
   private Button beginCountriesTesting;


   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      // TODO
   }

   public void openCountriesCRUDTesting(javafx.event.ActionEvent actionEvent) throws IOException {
      System.out.println("Pressed the button!\nBegin testing NOW!");

      stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/sample/CustomersCRUDTesting.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
   }
}
