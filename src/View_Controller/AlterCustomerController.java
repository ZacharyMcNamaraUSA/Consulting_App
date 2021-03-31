package View_Controller;

import Database.Entities.Customers;
import Database.Entities.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AlterCustomerController implements Initializable {

   private ObservableList<Customers> tableData = FXCollections.observableArrayList();

   /**
    * Default AlterCustomerController constructor
    * @param activeUsers The currently active Users - used for Last_Updated_By
    */
   public AlterCustomerController(Users activeUsers) {

   }

   /**
    * Method is required to implement Initializable  - runs before stage is shown.
    *
    * @param url Required param from Override - holds Location
    * @param resourceBundle Required param from Override
    */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      // TODO
      System.out.println("AlterCustomerController.initialize() printed this");
   }




}
