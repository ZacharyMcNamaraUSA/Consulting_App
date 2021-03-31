package View_Controller;

import Database.DAO.AppointmentsDaoImpl;
import Database.DAO.CustomersDaoImpl;
import Database.Entities.Appointment;
import Database.Entities.Customers;
import Utilities.TimeUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import Database.Entities.Users;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

   @FXML
   private TableView<Appointment> appointmentsTableView;

   @FXML
   private Menu fileMenu;

   @FXML
   private MenuItem fileMenuNew;

   @FXML
   private MenuItem fileMenuOpen;

   @FXML
   private Menu fileMenuOpenRecent;

   @FXML
   private MenuItem fileMenuClose;

   @FXML
   private MenuItem fileMenuSave;

   @FXML
   private MenuItem fileMenuSaveAs;

   @FXML
   private MenuItem fileMenuRevert;

   @FXML
   private MenuItem fileMenuPreferences;

   @FXML
   private MenuItem fileMenuQuit;

   @FXML
   private Menu editMenu;

   @FXML
   private MenuItem editMenuUndo;

   @FXML
   private MenuItem editMenuRedo;

   @FXML
   private MenuItem editMenuCut;

   @FXML
   private MenuItem editMenuCopy;

   @FXML
   private MenuItem editMenuPaste;

   @FXML
   private MenuItem editMenuDelete;

   @FXML
   private MenuItem editMenuSelectAll;

   @FXML
   private MenuItem editMenuUnselectAll;

   @FXML
   private Menu editMenuHelp;

   @FXML
   private MenuItem editMenuAbout;

   @FXML
   private TableColumn<Appointment, Integer> columnApptId;

   @FXML
   private TableColumn<Appointment, String> columnTitle;

   @FXML
   private TableColumn<Appointment, String> columnDescription;

   @FXML
   private TableColumn<Appointment, String> columnLocation;

   @FXML
   private TableColumn<Appointment, Integer> columnContactID;

   @FXML
   private TableColumn<Appointment, String> columnType;

   @FXML
   private TableColumn<Appointment, ZonedDateTime>  columnStartDateTime;

   @FXML
   private TableColumn<Appointment, ZonedDateTime>  columnEndDateTime;

   @FXML
   private TableColumn<Appointment, Integer> columnCustomerID;



   public static Users activeUsers = null;
   private CustomersDaoImpl customersDaoImpl = new CustomersDaoImpl();
   private ArrayList<Customers> allCustomers = customersDaoImpl.getAllCustomers();
   private AppointmentsDaoImpl appointmentsImpl = new AppointmentsDaoImpl();
   private ArrayList<Appointment> allAppointments = appointmentsImpl.getAllAppointments();

   /**
    * Method is required to implement Initializable.
    *
    * @param url Required parameter from Override that holds the file's Location.
    * @param resourceBundle Required parameter from Override
    */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      populateAppointmentTable();
      System.out.println("Populating ApptTable...");
   }

   /**
    * Default MainMenuController constructor
    * // @param activeUsers The Users that is logging into the system
    */
   public MainMenuController () {
      // TODO see if activeUsers has an appt within 15 minutes of logging in.
      //    Show a confirmation window if they do.
      ZonedDateTime zdtNow = ZonedDateTime.now(TimeUtilities.userZoneId);

   }


   /**
    * Method displays relevant data to the table
    */
   private void populateAppointmentTable() {
      appointmentsTableView.setEditable(true);

      TableColumn<Appointment, String> columnTitle = new TableColumn("Title");
//
      ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
      allAppointments = appointmentsImpl.getAllAppointments();

      for (Appointment appt: allAppointments) {
         System.out.println("Working with Appt ID #'" + appt.getAppointmentId() + "' right now.");
         appointmentObservableList.add(appt);
      }

//      columnApptId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("Appointment ID"));
//      columnTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Title"));
//      columnDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Description"));
//      columnLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Location"));
//      columnType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Type"));
//      columnStartDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, ZonedDateTime>("Start"));
//      columnEndDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, ZonedDateTime>("End"));
//      columnCustomerID.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("Customer ID"));
//      columnContactID.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("Contact ID"));
//      appointmentsTableView.setItems(appointmentObservableList);

   }







   /**
    *  Method initData will store the User that has logged in to the system, while being called from an outside class.
    * @param activeUsers The User that has successfully logged in.
    */
   public void initData(Users activeUsers) {
      this.activeUsers = activeUsers;
      System.out.println("MainMenuController has received activeUsers: " + activeUsers.getUserName());
   }

}
