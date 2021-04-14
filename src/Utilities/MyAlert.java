package Utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MyAlert extends Alert {

   public MyAlert(AlertType alertType) {
      super(alertType);
   }


   /**
    * Method receives a String which is an Object in the GUI that is being improperly selected.
    * @param missingSelection String input designating the Object that was not properly selected.
    */
   public static void invalidSelectionAlert(String missingSelection) {
      Alert selectionAlert = new Alert(Alert.AlertType.ERROR);
      selectionAlert.setTitle("Invalid " + missingSelection + " selection.");
      selectionAlert.setHeaderText("Please ensure you properly selected the intended " + missingSelection + " and try again.");
      selectionAlert.setContentText(null);
      selectionAlert.showAndWait();
   }



//   private void loginFail() {
//      Alert loginFail = new Alert(Alert.AlertType.ERROR);
//      loginFail.setTitle(translateText("loginfail"));
//      loginFail.setContentText(translateText("loginagain"));
//      loginFail.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> resetApp());
}
