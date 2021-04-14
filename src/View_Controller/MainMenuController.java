package View_Controller;

import Database.DAO.AppointmentDaoImpl;
import Database.DAO.CustomerDaoImpl;
import Database.Entities.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Database.Entities.User;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

   @FXML
   private TableColumn<Appointment, String> columnUserId;

   @FXML
   private RadioButton weeklyCheckBox;

   @FXML
   private RadioButton monthlyRadioButton;

   @FXML
   private Button viewCustomersButton;
   
   
   public static User activeUser = null;
   private CustomerDaoImpl customerDaoImpl = new CustomerDaoImpl();
   private AppointmentDaoImpl appointmentsDaoImpl = new AppointmentDaoImpl();
   private final int appointmentReminderWindow = 15;
   private static boolean firstLogin = false;
   
   
   /**
    * Method is required to implement Initializable.
    *
    * @param url Required parameter from Override that holds the file's Location.
    * @param resourceBundle Required parameter from Override
    */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      populateAppointmentTable();
   }

   /**
    * Default MainMenuController constructor
    * // @param activeUser The User that is logging into the system
    */
   public MainMenuController () {
      if (firstLogin) {

         checkAppointmentsOnUserLogin();
//         System.out.println("Active User: " + activeUser.getUserName());
         firstLogin = false;
      }
   }

   /**
    * Method creates a Dialog window to inform the user if they have an appointment soon or not.
    * This method uses findNextAppointment to find the upcoming appointment for the activeUser then calculates the difference in time and date between then and now.
    */
   private void checkAppointmentsOnUserLogin() {
      Instant now = Instant.now();

      Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);

      try {
         Appointment nextAppointment = findNextAppointment(activeUser);
         
         if (nextAppointment != null) {
            Instant nextApptInstant = nextAppointment.getStartZonedDateTime().toInstant();

            // if nextAppointment's start is within appointmentReminderWindow minutes of now
            if (now.isBefore(nextApptInstant) &&
                    nextApptInstant.isBefore(now.plus(appointmentReminderWindow, ChronoUnit.MINUTES))) {
               appointmentAlert.setTitle("Upcoming appointment!");
               appointmentAlert.setHeaderText("You have an appointment soon!");
               appointmentAlert.setContentText("" +
                       "Appointment ID: " + nextAppointment.getAppointmentID() + "\n" +
                       "Date: " + nextAppointment.getStartZonedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\n" +
                       "Time: " + nextAppointment.getStartZonedDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
            else {
               appointmentAlert.setTitle("No upcoming appointments");
               appointmentAlert.setHeaderText("You have no immediate appointments!");
               appointmentAlert.setContentText("" +
                       "Maybe enjoy a scone or say to Susan in accounting... Nobody ever talks to her.");
            }
            appointmentAlert.showAndWait();

         }

      } catch (Exception e) {
         e.getStackTrace();
      }
   }

   /**
    * Method receives a User parameter to be compared against all appointments and returns the
    * @param user Receives a User to compare IDs with all appointments.
    * @return Returns an Appointment the User has soon or null if the User has no scheduled appointments.
    */
   public Appointment findNextAppointment(User user) {
      Appointment nextAppointment = null;
      
      for (Appointment appt: appointmentsDaoImpl.allAppointments) {
         if (appt.getUserId() == user.getUserId()) {
            Appointment tAppointment = nextAppointment;
            
            nextAppointment = appt;
   
            // if appt starts before nextAppointment, switch them back using tAppointment.
            if (appt.getStartZonedDateTime().isBefore(nextAppointment.getStartZonedDateTime())) {
               nextAppointment = tAppointment;
            }
         }
      }

      return nextAppointment;
   }


   /**
    * Method displays relevant data to the TableView by calling limitAppointments(...)
    */
   private void populateAppointmentTable() {

      ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
      if (appointmentsDaoImpl.allAppointments != null) {
         ArrayList<Appointment> limitedAppointments = null;

         // check if
         if (weeklyCheckBox.isSelected() ) {
            limitedAppointments = limitAppointments(ZonedDateTime.now().plusWeeks(1));
         }
         else if (monthlyRadioButton.isSelected()) {
            limitedAppointments = limitAppointments(ZonedDateTime.now().plusMonths(1));
         }
         appointmentObservableList.addAll(limitedAppointments);

//         for (Appointment appt: appointmentsDaoImpl.allAppointments) {
//            appointmentObservableList.add(appt);
//         }
      }
      else
         System.out.println("getAllAppointments() is NULL =( ");

      setCellValueFactories();
      appointmentsTableView.setItems(appointmentObservableList);

   }

   private void setCellValueFactories() {


      columnApptId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("AppointmentID"));
      columnTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Title"));
      columnDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Description"));
      columnLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Location"));
      columnType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Type"));
      columnStartDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, ZonedDateTime>("FormattedStartString"));
      columnEndDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, ZonedDateTime>("FormattedEndString"));
      columnCustomerID.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("CustomerId"));
      columnContactID.setCellValueFactory(new PropertyValueFactory<>("ContactId"));
      columnUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
   }
   

   /**
    * Method receives a ZonedDateTime and returns scheduled Appointments between current time and then
    * @param zdt ZonedDateTime input used to limit every Appointment
    * @return Returns ArrayLIst<Appointment> of every applicable Appointment
    */
   private ArrayList<Appointment> limitAppointments(ZonedDateTime zdt) {
      ArrayList<Appointment> limitedAppointments = new ArrayList<>();
      for (Appointment appt: appointmentsDaoImpl.getAllAppointments()) {

         // if the Appointment's start is after now and before the input ZonedDateTime
         if (ZonedDateTime.now().isBefore(appt.getStartZonedDateTime()) && appt.getStartZonedDateTime().isBefore(zdt))
            limitedAppointments.add(appt);
      }

      return limitedAppointments;
   }


   /**
    * Method returns which Appointment in the TableView the user has selected
    * @return Returns the selected Appointment in the TableView, or creates an Alert if no Appointment is selected.
    */
   private Appointment getSelectedAppointment() {
      Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
      if (selectedAppointment == null) {
         Utilities.MyAlert.invalidSelectionAlert("Appointment");
         
         return null;
      }
      else
         return selectedAppointment;

   }





   /**
    *  Method initData will store the User that has logged in to the system, while being called from an outside class.
    * @param activeUser The User that has successfully logged in.
    */
   public static void initData(User activeUser) {
      MainMenuController.activeUser = activeUser;
//      System.out.println("MainMenuController has received activeUser: " + activeUser.getUserName());
      firstLogin = true;
   }

   /**
    * @param actionEvent
    * @throws IOException
    */
   public void editSelectedAppointment(ActionEvent actionEvent) throws IOException {
      Stage primaryStage = Main.myStage;

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AppointmentAlter.fxml"));
      AppointmentAlterController aac = new AppointmentAlterController();
      loader.setController(aac);
      Parent root = loader.load();
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      aac.initData(getSelectedAppointment());

      primaryStage.show();

   }

   /**
    * Method is called by user action onto the Add New Appointment Button.
    * @param event Input ActionEvent causes this method to run when user selects the appropriate Button.
    * @throws IOException Method throws IOException as it is required for loading FXMLLoaders.
    */
   public void addNewAppointment(ActionEvent event) throws IOException {
      Stage primaryStage = Main.myStage;

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AppointmentAlter.fxml"));
      AppointmentAlterController aac = new AppointmentAlterController();
      loader.setController(aac);
      Parent root = loader.load();
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);

      primaryStage.show();
   }


   @FXML
   private void viewAllCustomers(ActionEvent actionEvent) {
      try {
         Stage primaryStage = Main.myStage;

         FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ViewAllCustomers.fxml"));
         Parent root = (Parent)loader.load();
         primaryStage.setScene(new Scene(root));
         primaryStage.setResizable(false);
         
         primaryStage.show();
         
      } catch (IOException ioException) {
         System.out.println("MainMenuController.ioException");
         ioException.getStackTrace();
      } catch (Exception e) {
         e.getStackTrace();
      }

   }
}
