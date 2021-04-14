package View_Controller;

import Database.DAO.AppointmentDaoImpl;
import Database.DAO.ContactDaoImpl;
import Database.DAO.CustomerDaoImpl;
import Database.Entities.Appointment;
import Database.Entities.Contact;
import Database.Entities.Customer;
import Utilities.TimeUtilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Main;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppointmentAlterController implements Initializable {

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
   private Button commitAppointmentButton;

   @FXML
   private Button cancelButton;

   @FXML
   private Button deleteAppointmentButton;

   @FXML
   private TextField titleText;

   @FXML
   private TextField descriptionText;

   @FXML
   private TextField locationText;

   @FXML
   private TextField customerIDText;
   
   @FXML
   private ComboBox<String> contactComboBox;

   @FXML
   private TextField userIDText;

   @FXML
   private TextField typeText;

   @FXML
   private DatePicker datePicker;

   @FXML
   private TextField startTimeText;

   @FXML
   private TextField endTimeText;

   @FXML
   private TextField appointmentIDText;
   
   
   private Appointment appointment;
   private ArrayList<String> invalidFieldInputs = new ArrayList<>();
   private AppointmentDaoImpl appointmentDao = new AppointmentDaoImpl();
   private ContactDaoImpl contactDao = new ContactDaoImpl();
   private CustomerDaoImpl customerDao = new CustomerDaoImpl();
   
   
   /**
    * Default constructor.
    */
   public AppointmentAlterController()  {
      System.out.println("AAC constructor");
      
   }
   
   
   /**
    * Required method to implement Initializable
    * @param url Input URL location
    * @param resourceBundle Input ResourceBundle properties
    */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      System.out.println("AAC initialize");
      
      populateContactComboBox();
   }


   /**
    * Method is used to initialize data for this controller.
    * @param appointment Input Appointment that is sent to this controller to be altered.
    */
   public void initData(Appointment appointment) {
      System.out.println("AAC initData");
      populateContactComboBox();
      
      if (appointment == null)
         return;
      this.appointment = appointment;
   
      populateFields();
      commitAppointmentButton.setText("Update Appointment");
   }


   /**
    * Method retrieves data for the current Appointment then calls the correct database update.
    * @param event ActionEvent input from user's button press to commit this Appointment.
    */
   @FXML
   private void commitAppointment(ActionEvent event) throws IOException {
   
      Instant start = retrieveStartZonedDateTime().toInstant();
      Instant end = retrieveEndZonedDateTime().toInstant();
   
      // TODO Appointment must be during business hours
      // TODO Appointment cannot schedule overlap any other Appointment, by Customer.
      if (!testForBusinessHours(start, end) || !hasNoOverlappingAppointments(start, end)) {
         invalidDateTimeAlert();
         
         return;
      }
   
      if (appointment != null) {
         updateAppointment();
      }
      else
         saveNewAppointment();
   }
   
   
   private boolean testForBusinessHours(Instant start, Instant end) {
   
      if (!TimeUtilities.isDuringBusinessHours(start) || !TimeUtilities.isDuringBusinessHours(end)) {
         // if either the start or end of the Appointment is outside of business hours, cancel commit.
         invalidDateTimeAlert();
         return false;
      }
      
      return true;
   }
   
   
   private boolean hasNoOverlappingAppointments(Instant start, Instant end) {
      
      // Ensure user-input leads to a valid Customer then the Appointment times.
      if (retrieveCustomer() != null && customerDao.getAllCustomers().contains(retrieveCustomer())) {
   
         // Then create Customer for current Appointment
         Customer customer = retrieveCustomer();
         
         for (Appointment appointment: appointmentDao.getAllAppointments()) {
            
            // if appointment is for this customer
            if (customerDao.getSingleCustomer(appointment.getCustomerId()).equals(customer)) {
   
               // if test Appointment has an earlier start time OR later end time, it's an overlap.
               if(start.isBefore(appointment.getStartZonedDateTime().toInstant()) ||
                         end.isBefore(appointment.getEndZonedDateTime().toInstant())) {
                  return false;
               }
            }
         
         }
      }
      else {      // if the Customer is null or it is not in the list of all Customers.
         return false;
      }
      
      return true;
   }
   
   
   private Customer retrieveCustomer() {
      Customer customer = null;
      try {
         customer = customerDao.getSingleCustomer(Integer.getInteger(customerIDText.getText()));
         
      } catch (NumberFormatException numberFormat) {
         numberFormat.getStackTrace();
         System.out.println("invalid CustomerID in this AppointmentAlter field!");
      }
      
      return customer;
   }
   
   
   /**
    * Method is called when user activates the 'Cancel' button.
    * @param event Input ActionEvent is caused by user activating the 'Cancel' button.
    * @throws IOException
    */
   @FXML
   private void cancelAppointmentAlter(ActionEvent event) {

      Alert confirmCancelAlert = new Alert(Alert.AlertType.ERROR);
      confirmCancelAlert.setTitle("Cancellation confirmation");
      confirmCancelAlert.setHeaderText("Are you sure you want to cancel and return to the main menu?");
      confirmCancelAlert.setContentText("Any unsaved changes to this Appointment will be cancelled!");

      ButtonType okayButton = new ButtonType("YES");
      ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmCancelAlert.getButtonTypes().setAll(okayButton, cancelButton);
   
      /**
       * --- LAMBDA EXPRESSION #1 ---
       * A lambda expression here ensures that program flows properly while increasing readability.
       * Additionally, this lambda expression simplifies variable declaration and initialization.
       */
      confirmCancelAlert.showAndWait().ifPresent(response -> {
         if (response == okayButton) {
            try {
               returnToMainMenu();

            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      });
   }


   /**
    * Method confirms user wishes to delete then deletes the Appointment from database.
    * @param event User button press to delete the appointment.
    */
   @FXML
   private void initiateDelete(ActionEvent event) throws IOException {
      System.out.println("DELETE APPOINTMENT");

      if (appointment == null ) {
         // no AppointmentID means there is no appointment to be deleted.
         Alert noAppointmentDelete = new Alert(Alert.AlertType.CONFIRMATION);
         noAppointmentDelete.setTitle("Try again");
         noAppointmentDelete.setContentText("You cannot delete an unsaved appointment!");
         noAppointmentDelete.setContentText("Did you mean to Cancel this new appointment?");
         noAppointmentDelete.showAndWait().ifPresent(response -> {
            System.out.println(Alert.AlertType.CONFIRMATION.values());
         });
         return;
      }

      Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
      deleteAlert.setTitle("Delete warning!");
      deleteAlert.setHeaderText("Warning! All deletes are final!");
      deleteAlert.setContentText("Are you sure you wish to delete this object? This delete cannot be reversed!");
      /**
       * --- LAMBDA EXPRESSION # 3---
       * A lambda expression here ensures that program flows properly while increasing readability.
       * Additionally, this lambda expression simplifies variable declaration and initialization.
       * Note the presentation difference between lambda expression #1 and #3 caused by handling a potential IOException inline or with a method throw.
       */
      deleteAlert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteAppointment(appointment));

      returnToMainMenu();
   }
   
   /** Method attempts to delete the Appointment from the database then shows user a confirmation message.
    * @param appointment Input Appointment to be deleted.
    */
   private void deleteAppointment(Appointment appointment) {
      try {
         if(!appointmentDao.deleteAppointment(appointment))
            System.out.println("Something went wrong with AppointmentAlterController.deleteAppointment");
      } catch (Exception e) {
         e.getMessage();
         e.getStackTrace();
      }
      
      confirmDelete(appointment);
   }
   
   
   /** Method creates and shows an Alert once an Appointment has been deleted.
    * @param appointment Input Appointment that has been deleted from the database.
    */
   private void confirmDelete(Appointment appointment) {
      Alert confirmDeleteAlert = new Alert(Alert.AlertType.INFORMATION);
      confirmDeleteAlert.setTitle("Appointment Deletion Confirmation");
      confirmDeleteAlert.setHeaderText("This Appointment has been permanently deleted.");
      confirmDeleteAlert.setContentText("Deleted Appointment info\nAppointment ID: " + appointment.getAppointmentID() +
                "\nAppointment Type: " + appointment.getType());
      confirmDeleteAlert.showAndWait();
   }
   
   
   /**
    * Method closes this scene and returns to MainMenuController.
    */
   private void returnToMainMenu() throws IOException {
      Stage primaryStage = Main.myStage;

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainMenu.fxml"));
      Parent root = loader.load();
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);

      primaryStage.show();
   }


   /**
    * Method saves a new record in the database.
    */
   private void saveNewAppointment() {
      try {
         AppointmentDaoImpl.addAppointmentToDatabase(
                    titleText.getText(),
                    descriptionText.getText(),
                    locationText.getText(),
                    typeText.getText(),
                    retrieveStartZonedDateTime(),
                    retrieveEndZonedDateTime(),
                    ZonedDateTime.now(),
                    MainMenuController.activeUser.getUserName(),
                    retrieveCustomerID(),
                    retrieveUserID(),
                    retrieveContactID()
         );

         returnToMainMenu();

      } catch (NullPointerException nullPointerException) {
         System.out.println("AppointmentAlterController.java saveNewAppointment() NullPointerException");
         System.out.println(nullPointerException.getMessage());

         nullFieldAlert();
      } catch (Exception e) {
         System.out.println("AppointmentAlterController.java saveNewAppointment() Exception");
         e.getStackTrace();
      }

   }


   /**
    * Method will display an Alert to the user that data is invalid if one or more FXML fields are left blank.
    */
   private void nullFieldAlert() {
      Alert saveAlert = new Alert(Alert.AlertType.ERROR);
      saveAlert.setTitle("Invalid new Appointment");
      saveAlert.setHeaderText("Fill in all fields.");
      saveAlert.setContentText("Please try again with all Appointment fields correctly inputted.");
      saveAlert.showAndWait();
   }


   /**
    * Method updates all fields for the Appointment then sends it to the DaoImpl to be updated in the database.
    */
   private void updateAppointment() throws IOException {
      
      try {
         
         appointment.setTitle(titleText.getText());
         appointment.setDescription(descriptionText.getText());
         appointment.setLocation(locationText.getText());
         appointment.setType(typeText.getText());
         appointment.setStartZonedDateTime(retrieveStartZonedDateTime());
         appointment.setEndZonedDateTime(retrieveEndZonedDateTime());
         appointment.setLastUpdate(ZonedDateTime.now());
         appointment.setLastUpdatedBy(MainMenuController.activeUser.getUserName());
         appointment.setCustomerId(retrieveCustomerID());
         appointment.setUserId(retrieveUserID());
         appointment.setContactId(retrieveContactID());
         
         appointmentDao.updateAppointment(appointment);
         
      } catch (Exception e) {
         e.getStackTrace();
      }

      returnToMainMenu();
   }


   /**
    * Method retrieves LocalDate from the DatePicker and LocalTime from a TextField to create, convert, then
    *    return a ZonedDateTime Object.
    * @return Returns the Appointment's start ZonedDateTime, constructed and converted from the FXML input fields.
    */
   private ZonedDateTime retrieveStartZonedDateTime() {

      try {
         LocalDate localDate = datePicker.getValue();
         LocalTime localStartTime = LocalTime.parse(startTimeText.getText());
         LocalDateTime startLDT = localDate.atTime(localStartTime);
         return TimeUtilities.convertDatabaseDTtoZonedDT(startLDT);

      } catch (DateTimeParseException dtParseException) {
         invalidFieldInputs.add("Start Date");
      }

      return null;
   }


   /**
    * Method retrieves LocalDate from the DatePicker and LocalTime from a TextField to create, convert, then
    *    return a ZonedDateTime Object.
    * @return Returns the Appointment's end ZonedDateTime, constructed and converted from the FXML input fields.
    */
   private ZonedDateTime retrieveEndZonedDateTime() {

         try {
            LocalDate localDate = datePicker.getValue();
            LocalTime localEndTime = LocalTime.parse(endTimeText.getText());
            LocalDateTime endLDT = localDate.atTime(localEndTime);
            return endLDT.atZone(ZoneId.systemDefault());

         } catch (DateTimeParseException parseException) {
            invalidFieldInputs.add("End Date");
            parseException.getStackTrace();
         }
      return null;
   }


   /**
    *  Method retrieves all relevant Appointment information and fills in the appropriate fields by calling
    *       javafx.scene.control.TextInputControl.setText(...).
    */
   private void populateFields() {
      
      populateContactComboBox(contactDao.getAllContacts());
      
      if (appointment == null ) {
         // If there is no appointment leave the remaining default fields.
         return;
      }
      commitAppointmentButton.setText("Save this appointment");
      appointmentIDText.setText(String.valueOf(appointment.getAppointmentID()));
      titleText.setText(appointment.getTitle());
      descriptionText.setText(appointment.getDescription());
      locationText.setText(appointment.getLocation());
      typeText.setText(appointment.getType());
      datePicker.setValue(appointment.getStartZonedDateTime().toLocalDate());
      startTimeText.setText(appointment.getStartZonedDateTime().format(TimeUtilities.timeFormatterLong));
      endTimeText.setText(appointment.getEndZonedDateTime().format(TimeUtilities.timeFormatterLong));
      customerIDText.setText(String.valueOf(appointment.getCustomerId()));
      userIDText.setText(String.valueOf(appointment.getUserId()));
      contactComboBox.setValue(contactDao.getSingleContact(appointment.getContactId()).getContactName());
   }
   
   
   /** Method sorts then places every String name from the contactList into a ComboBox.
    * @param contactList Input ArrayList<Contact> which contains every Contact to populate.
    */
   private void populateContactComboBox(ArrayList<Contact> contactList) {
      System.out.println("AAC.populateContactComboBox(...)");
      ArrayList<String> allNames = new ArrayList<>();
      
      contactComboBox.getItems().clear();
      
      for (Contact contact: contactList) {
         allNames.add(contact.getContactName());
      }
      
      allNames.sort(String::compareToIgnoreCase);
      
      contactComboBox.getItems().addAll(allNames);
   }
   
   
   /**
    * Method sorts then places every String name from the contactList into a ComboBox.
    *    Method overload for no input.
    *    The default population of ComboBox will be every contact from ContactDaoImpl.
    */
   private void populateContactComboBox() {
      System.out.println("AAC.populateContactComboBox()");
      
      ArrayList<String> allNames = new ArrayList<>();
      
      contactComboBox.getItems().clear();
      
      for (Contact contact: contactDao.getAllContacts()) {
         allNames.add(contact.getContactName());
      }
      
      allNames.sort(String::compareToIgnoreCase);
      
      contactComboBox.getItems().addAll(allNames);
   }
   
   
   /**
    * Method shows an Alert to user with acceptable Appointment start and end times.
    */
   private void invalidDateTimeAlert() {
      Alert inoperableDateTimeAlert = new Alert(Alert.AlertType.ERROR);
      ZonedDateTime startZDT = ZonedDateTime.ofInstant(TimeUtilities.businessOpen, ZoneId.systemDefault());
      ZonedDateTime endZDT = ZonedDateTime.ofInstant(TimeUtilities.businessClosed, ZoneId.systemDefault());
      
      inoperableDateTimeAlert.setTitle("Invalid Appointment");
      inoperableDateTimeAlert.setHeaderText("Appointment could not be saved because it is outside business hours.");
      inoperableDateTimeAlert.setContentText("Adjusted for your time zone, we open at " +
                     TimeUtilities.timeFormatterLong.format(startZDT) +
                " and close at " +
                     TimeUtilities.timeFormatterLong.format(endZDT) + ".");
      
      inoperableDateTimeAlert.showAndWait();
   }


   /** Method parses String to an int to be returned.
    * @return Returns an int for the CustomerID.
    */
   private int retrieveCustomerID() {
      int customerID = 0;
      
      try {
         customerID = Integer.parseInt(customerIDText.getText());

      } catch (NumberFormatException numberFormatException) {
         invalidFieldInputs.add("Customer ID");
      } catch (NullPointerException nullPointerException) {
         invalidFieldInputs.add("Customer ID");
      }
      
      return customerID;
   }


   /** Method parses String to an int to be returned.
    * @return Returns an int for the UserID.
    */
   private int retrieveUserID() {
      int userID = 0;
      try {
         userID = Integer.parseInt(userIDText.getText());

      } catch (NumberFormatException numberFormatException) {
         invalidFieldInputs.add("User ID");
      } catch (NullPointerException nullPointerException) {
         invalidFieldInputs.add("User ID");
      }

      return userID;
   }
   
   
   /** Method parses String to an int to be returned.
    * @return Returns an int for the ContactID.
    */
   private int retrieveContactID() {
      int contactID = 0;

      try {
         Contact contact = contactDao.getSingleContact(contactComboBox.getValue());
         contactID = contact.getContactId();

      } catch (NullPointerException nullException) {
         invalidFieldInputs.add("Contact Name");
      }

      return contactID;
   }
}
