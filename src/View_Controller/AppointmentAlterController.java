package View_Controller;

import Database.DAO.AppointmentDaoImpl;
import Database.DAO.ContactDaoImpl;
import Database.DAO.CustomerDaoImpl;
import Database.Entities.Appointment;
import Database.Entities.Contact;
import Database.Entities.Customer;
import Utilities.MyAlert;
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
   private ComboBox<LocalTime> startTimeComboBox;
   
   @FXML
   private ComboBox<Long> durationTimeComboBox;
   
   @FXML
   private Label endTimeLabel;

   @FXML
   private TextField startTimeText;

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
   
   }
   
   
   /**
    * Required method to implement Initializable
    * @param url Input URL location
    * @param resourceBundle Input ResourceBundle properties
    */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      
      populateContactComboBox();
   }


   /**
    * Method is used to initialize data for this controller.
    * @param appointment Input Appointment that is sent to this controller to be altered.
    */
   public void initData(Appointment appointment) {
      populateContactComboBox();
      populateAppointmentStartTimes();
      populateAppointmentDurations();
      
      if (appointment == null)
         return;
      this.appointment = appointment;
   
      populateFields();
      commitAppointmentButton.setText("Update Appointment");
   
   }
   
   
   /**
    * Method retrieves data for the current Appointment then calls the correct database update.
    * @param event ActionEvent input from user's button press to commit this Appointment.
    * @throws IOException Throws IOException ot track if an error occurs during data I/O.
    */
   @FXML
   private void commitAppointment(ActionEvent event) throws IOException {
   
      try {
         
         Instant startPotential = retrieveStartZonedDateTime().toInstant();
         
         Instant endPotential = retrieveEndZonedDateTime().toInstant();
         
         boolean noOverlappingAppointments = hasNoOverlappingAppointments(startPotential, endPotential);
         
         // if false, there is an overlap in Appointment times, by Customer, the commit fails.
         if (!noOverlappingAppointments) {
            
            appointmentOverlapAlert();
            
            return;
         }
         
         if (appointment != null) {
            updateAppointment();
            System.out.println("This is an existing Appointment I am trying to UPDATE in AppointmentAlterController.commitAppointment(..)");
         }
         else {
            saveNewAppointment();
            System.out.println("This is a new Appointment I am trying to save in AppointmentAlterController.commitAppointment(..)");
         }
      } catch (NullPointerException nullPointerException) {
         System.out.println("some field is causing NullPointerException @ AppointmentAlterController.commitAppointment");
         nullFieldAlert();
         System.out.println(nullPointerException.getMessage() + " @ AppointmentAlterController.commitAppointment");
         
      } catch (Exception e) {
         e.getStackTrace();
      }
   }
   
   
   /**
    * Method is called when user activates the 'Cancel' button.
    * @param event Input ActionEvent is caused by user activating the 'Cancel' button.
    * @throws IOException Throws IOException ot track if an error occurs during data I/O.
   
    * --- LAMBDA EXPRESSION #1 ---
    * A lambda expression here ensures that program flows properly while increasing readability.
    * Additionally, this lambda expression simplifies variable declaration and initialization.
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
    * Method initiates an Appointment end time to be calculated and is called when user selects from from durationTimeComboBox.
    * @param event Input ActionEvent comes from FXML application when a time duration is selected from durationTimeComboBox.
    */
   @FXML
   private void userSelectedDuration(ActionEvent event) {
      // method is called when user selects an Appointment duration from the FXML Scene
      
      calculateEndTime();
      
   }
   
   
   /**
    * Method will initiate Appointment end time calculations but only if a duration has already been selected.
    * @param event Input ActionEvent comes from FXML application when a time duration is selected from durationTimeComboBox.
    */
   @FXML
   private void userSelectedTime(ActionEvent event) {
      // method is called when user selects an Appointment time from the FXML Scene
      
      if (durationTimeComboBox.getValue() == null) {
//         System.out.println("NULL duration value is NULL.");
         return;
      }
      
      calculateEndTime();
   }
   
   
   /**
    * Method calculates and sets the Appointment End time. Method is called when user selects an Appointment start time or duration.
    */
   private void calculateEndTime() {
   
      try {
         // if (startTime is valid)
         LocalTime startTime = startTimeComboBox.getValue();
      
         // retrieve duration
         long duration = durationTimeComboBox.getValue();
      
         // calculate the Appointment End by adding the duration to start time
         LocalTime endTime = startTime.plusMinutes(duration);
      
         // show Appointment End on Label endTimeLabel
//         String endTimeAsString = endTime.f "";
         endTimeLabel.setText(String.valueOf(endTime));
      
      } catch (NullPointerException nullPointerException) {
         setDurationBeforeStartAlert();
         
      } catch (Exception e) {
         e.getStackTrace();
      }
   }
   
   
   /**
    * Method confirms user wishes to delete then deletes the Appointment from database.
    * @param event User button press to delete the appointment.
    * @throws IOException Throws IOException ot track if an error occurs during data I/O.
    *
    * --- LAMBDA EXPRESSION # 3---
    * A lambda expression here ensures that program flows properly while increasing readability.
    * Additionally, this lambda expression does not require a method name and simplifies variable declaration and initialization.
    * Note the presentation difference between lambda expression #1 and #3 caused by handling a potential IOException inline or with a method throw.
    */
   @FXML
   private void initiateDelete(ActionEvent event) throws IOException {
//      System.out.println("DELETE APPOINTMENT");

      if (appointment == null ) {
         // no AppointmentID means there is no appointment to be deleted.
         Alert noAppointmentDelete = new Alert(Alert.AlertType.CONFIRMATION);
         noAppointmentDelete.setTitle("Try again");
         noAppointmentDelete.setHeaderText("You cannot delete an unsaved appointment!");
         noAppointmentDelete.setContentText("Try to Cancel a new Appointment instead.");
         noAppointmentDelete.showAndWait().ifPresent(response -> {
            // do nothing;
         });
         return;
      }
      else {
         Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
         deleteAlert.setTitle("Delete warning!");
         deleteAlert.setHeaderText("Warning! All deletes are final!");
         deleteAlert.setContentText("Are you sure you wish to delete this object? This delete cannot be reversed!");
   
         deleteAlert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteAppointment(appointment));
   
      }


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
      
      confirmDeleteAlert(appointment);
   }
   
   
   /**
    * Method closes this scene and returns to MainMenuController.
    * @throws IOException Throws IOException ot track if an error occurs during data I/O.
    */
   private void returnToMainMenu() throws IOException {
      try {
   
         Stage primaryStage = Main.myStage;
   
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainMenu.fxml"));
         Parent root = loader.load();
         primaryStage.setScene(new Scene(root));
         primaryStage.setResizable(false);
         primaryStage.setTitle("Consulting App - Main Menu");
   
         primaryStage.show();
      } catch (IOException ioException) {
         ioException.getStackTrace();
      } catch (Exception e) {
         e.getStackTrace();
      }
   }


   /**
    * Method saves a new record in the database.
    */
   private void saveNewAppointment() {
      try {
         AppointmentDaoImpl.addAppointmentToDatabase(
                    retrieveTitleText(),
                    retrieveDescription(),
                    retrieveLocation(),
                    retrieveType(),
                    retrieveStartZonedDateTime(),
                    retrieveEndZonedDateTime(),
                    ZonedDateTime.now(),
                    MainMenuController.activeUser.getUserName(),
                    retrieveCustomerID(),
                    retrieveUserID(),
                    retrieveContact().getContactId()
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
    * Method updates all fields for the Appointment then sends it to the DaoImpl to be updated in the database.
    * @throws IOException Throws IOException ot track if an error occurs during data I/O.
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
         appointment.setContactId(retrieveContact().getContactId());
         
         appointmentDao.updateAppointment(appointment);
         
      } catch (Exception e) {
         e.getStackTrace();
      }

      returnToMainMenu();
   }
   
   
   /**
    * Method populates all possible Appointment Start times to the ComboBox.
    */
   private void populateAppointmentStartTimes() {
      
      // LocalDate (set by user)
      LocalDate localDate;
      
      if (datePicker.getValue() == null) {
         localDate = LocalDate.now(TimeUtilities.headquartersZoneId);
      }
      else {
         localDate = datePicker.getValue();
      }
      
      // time interval between each available Appointment Start (in minutes)
      long appointmentSchedulingInterval = 30;
      
      ZoneOffset headquartersOffset = TimeUtilities.headquartersZoneId.getRules().getOffset(TimeUtilities.getBusinessOpenInstant(localDate));
      ZoneOffset currentLocationOffset = ZoneId.systemDefault().getRules().getOffset(TimeUtilities.getBusinessOpenInstant(localDate));
//
//      System.out.println("headquartersOffset: " + headquartersOffset);
//      System.out.println("currentLocationOffset: " + currentLocationOffset);
      int difference = currentLocationOffset.compareTo(headquartersOffset);
//      System.out.println("Difference = " + difference);
      
      LocalTime firstAppointmentStart = LocalTime.of(8,0);
//      System.out.println("firstAppointmentSTart: " + firstAppointmentStart);
      
      firstAppointmentStart = firstAppointmentStart.plusSeconds(-difference);
//      System.out.println("LocalTime of 8am adjusted from New_York to current system location is " + firstAppointmentStart);
      
      // add beginning of business hours to the startTimeComboBox
      startTimeComboBox.getItems().add(firstAppointmentStart);
      // set the default Start time to be the beginning of business hours
      startTimeComboBox.setValue(firstAppointmentStart);
      
      while ((firstAppointmentStart.plusMinutes(appointmentSchedulingInterval)).isBefore(LocalTime.of(22, 0))) {
         
         firstAppointmentStart = firstAppointmentStart.plusMinutes(appointmentSchedulingInterval);
         
//         System.out.println("LocalTime of 8am adjusted from New_York to current system location is " + firstAppointmentStart);
         
         startTimeComboBox.getItems().add(firstAppointmentStart);
      }
      
   }
   
   
   /**
    * Method populates all options for interval durations to the ComboBox.
    */
   private void populateAppointmentDurations() {
      long durationInterval = 15;
      long durationOutput = 0;
      
      while (durationOutput < 60 - durationInterval) {
         durationTimeComboBox.getItems().add(durationOutput+=durationInterval);
      }
   }
   
   
   /**
    * Method determines if Customer has an Appointment which overlaps with current Appointment.
    * @param startPotential Input Instant of potential Appointment's Start time.
    * @param endPotential Input Instant of potential Appointment's End time.
    * @return Returns true if Appointment is clear of time conflicts and may be committed. Returns false if Appointment has a time conflict and cannot be saved.
    */
   private boolean hasNoOverlappingAppointments(Instant startPotential, Instant endPotential) {
   
      // Retrieve Customer
      Customer customer = retrieveCustomer();
      
      Instant scheduledStart;
      Instant scheduledEnd;
   
      // loop through every Appointment
      for (Appointment tAppointment: appointmentDao.getAllAppointments()) {
         
         // skip checks for the current/same Appointment.
         if (tAppointment.getAppointmentID() == Integer.valueOf(appointmentIDText.getText())) {
            continue;
   
         }
   
         scheduledStart = tAppointment.getStartZonedDateTime().toInstant();
         scheduledEnd = tAppointment.getEndZonedDateTime().toInstant();
   
   
         // if appointment is for this customer, check for time overlap.
         if (customerDao.getSingleCustomer(tAppointment.getCustomerId()).equals(customer)) {
//            System.out.println("Customer has another appointment. ID = " + tAppointment.getAppointmentID());
            
            // compare the start time's of scheduled appointment 'tAppointment' with the start time for commit-attempt-Appointment 'start'...
            
            if (startPotential.equals(scheduledStart) || endPotential.equals(scheduledEnd)) {
               // IF startPotential and scheduledStart are the same, there will always be overlap. same for endings too. return false.
               System.out.println("Appointment start/end are the same as ones already scheduled!");
               return false;
            }
            
            // IF startPotential earlier is earlier or same time as scheduledStart
            if (startPotential.isBefore(scheduledStart)) {
               
               // IF end is between scheduledStart and scheduleEnd - conflict
               if (endPotential.isAfter(scheduledStart) && endPotential.isBefore(scheduledEnd)) {
                  System.out.println("returning false cause start is before scheduled start OR equal, AND IF endPotent is after scheduledStart && endPot before ScheduleEnd");
                  return false;        // yellow case
               }
               
            }
            // ELSE IF startPotential is between scheduledStart and scheduledEnd - conflict
            else if (startPotential.isAfter(scheduledStart) && startPotential.isBefore(scheduledEnd)){
               System.out.println("If you start in the middle of another appointment, you have to be in the middle of it. bad!");
                  return false;        // blue and orange case
               
            }
            
            
         }
         
      }
      
      
      return true;
   }


   /**
    *  Method retrieves all relevant Appointment information and fills in the appropriate fields by calling
    *       javafx.scene.control.TextInputControl.setText(...).
    */
   private void populateFields() {
      
      populateContactComboBox(contactDao.getAllContacts());
      
      if (appointment == null ) {
         // If there is no appointment do not edit fields
         return;
      }
      commitAppointmentButton.setText("Save this appointment");
      appointmentIDText.setText(String.valueOf(appointment.getAppointmentID()));
      titleText.setText(appointment.getTitle());
      descriptionText.setText(appointment.getDescription());
      locationText.setText(appointment.getLocation());
      typeText.setText(appointment.getType());
      datePicker.setValue(appointment.getStartZonedDateTime().toLocalDate());
      startTimeComboBox.setValue(appointment.getStartZonedDateTime().toLocalTime());
      Duration duration = Duration.between(appointment.getStartZonedDateTime(), appointment.getEndZonedDateTime());
      if (durationTimeComboBox.getItems().contains(duration.toMinutes())) {
         durationTimeComboBox.setValue(duration.toMinutes());
      }
      customerIDText.setText(String.valueOf(appointment.getCustomerId()));
      contactComboBox.setValue(contactDao.getSingleContact(appointment.getContactId()).getContactName());
      userIDText.setText(String.valueOf(appointment.getUserId()));
   }
   
   
   /** Method sorts then places every String name from the contactList into a ComboBox.
    * @param contactList Input ArrayList<Contact> which contains every Contact to populate.
    */
   private void populateContactComboBox(ArrayList<Contact> contactList) {
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
      
      populateContactComboBox(contactDao.getAllContacts());
   }
   
   
   private String retrieveTitleText() {
      String title = "";
      
      try {
         title = titleText.getText();
         
      } catch (Exception e) {
         System.out.println("Invalid titleText in Appt-Alter-Controller");
         invalidFieldInputs.add("title");
      }
      
      return title;
   }
   
   
   private String retrieveDescription() {
      String description = "";
      
      try {
         description = descriptionText.getText();
         
      } catch (Exception e) {
         System.out.println("Invalid descriptionText in Appt-Alter-Controller");
         invalidFieldInputs.add("description");
      }
      
      return description;
      
   }
   
   
   private String retrieveLocation() {
      String location = "";
      
      try {
         location = locationText.getText();
         
      } catch (Exception e) {
         System.out.println("Invalid locationText in Appt-Alter-Controller");
         invalidFieldInputs.add("location");
      }
      
      return location;
      
   }
   
   
   private String retrieveType() {
      String type = "";
      
      try {
         type = typeText.getText();
         
      } catch (Exception e) {
         System.out.println("Invalid typeText in Appt-Alter-Controller");
         invalidFieldInputs.add("type");
      }
      
      return type;
      
   }
   
   
   /**
    * Method retrieves LocalDate from the DatePicker and LocalTime from a TextField to create, convert, then
    *    return a ZonedDateTime Object.
    * @return Returns the Appointment's start ZonedDateTime, constructed and converted from the FXML input fields.
    */
   private ZonedDateTime retrieveStartZonedDateTime() {
      
      try {
         LocalDate localDate = datePicker.getValue();
//         System.out.println("aac's retrieveStartZDT... LocalDate: " + localDate);
         
         LocalTime localStartTime = startTimeComboBox.getValue();
         LocalDateTime startLDT = localDate.atTime(localStartTime);
//         System.out.println("aac's retrieveStartZDT... LocalDateTime: " + startLDT);
         ZonedDateTime startZDT = startLDT.atZone(ZoneId.systemDefault());
         ZonedDateTime adjustedToHQZone = startZDT.withZoneSameInstant(TimeUtilities.headquartersZoneId);
         return adjustedToHQZone;
         
      } catch (DateTimeParseException dtParseException) {
         
         try {
            
            LocalDate localDate = datePicker.getValue();
            LocalTime localStartTime = LocalTime.parse(startTimeText.getText());
            LocalDateTime startLDT = localDate.atTime(localStartTime);
            System.out.println("aac's retrieveStartZDT... startLDT: " + startLDT);
            ZonedDateTime startZDT = startLDT.atZone(ZoneId.systemDefault());
            ZonedDateTime adjustedToHQZone = startZDT.withZoneSameInstant(TimeUtilities.headquartersZoneId);
            return adjustedToHQZone;
            
         } catch (DateTimeParseException dateParseException) {
            System.out.println("AHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
            invalidFieldInputs.add("Start Date");
         }
         
         
      } catch (NullPointerException nullException) {
         MyAlert myAlert = new MyAlert(Alert.AlertType.ERROR);
         myAlert.invalidSelectionAlert("Date");
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
         LocalTime localEndTime = LocalTime.parse(endTimeLabel.getText());
         LocalDateTime endLDT = localDate.atTime(localEndTime);
         ZonedDateTime endZDT = endLDT.atZone(TimeUtilities.userZoneId);
         ZonedDateTime adjustedToHQZone = endZDT.withZoneSameInstant(TimeUtilities.headquartersZoneId);
         return adjustedToHQZone;
         
      } catch (DateTimeParseException parseException) {
         invalidFieldInputs.add("End Date");
         parseException.getStackTrace();
      }
      return null;
   }


   /** Method parses String to an int to be returned.
    * @return Returns an int for the CustomerID.
    */
   private int retrieveCustomerID() {
      int customerID = 0;
      
      try {
         customerID = Integer.parseInt(customerIDText.getText());

      } catch (NumberFormatException | NullPointerException numberFormatException) {
         invalidFieldInputs.add("Customer ID int");
      } catch (Exception e) {
         invalidFieldInputs.add("Customer ID int");
         e.getStackTrace();
      }
      
      return customerID;
   }
   
   
   /** Method parses String to an int to be returned.
    * @return Returns an int for the ContactID.
    */
   private Contact retrieveContact() {
      Contact contact = null;
      
      try {
         contact = contactDao.getSingleContact(contactComboBox.getValue());
         
      } catch (NullPointerException nullException) {
         invalidFieldInputs.add("Contact Name");
      }
      
      return contact;
   }


   /** Method parses String to an int to be returned.
    * @return Returns an int for the UserID.
    */
   private int retrieveUserID() {
      int userID = 0;
      try {
         userID = Integer.parseInt(userIDText.getText());

      } catch (Exception numberFormatException) {
         invalidFieldInputs.add("User ID");
      }
   
      return userID;
   }
   
   
   /**
    * Method uses parsed int for customer ID to retrieve the Customer.
    * @return Returns the Customer matching the customerIDText.
    */
   private Customer retrieveCustomer() {
      Customer customer = null;
      try {
         customer = customerDao.getSingleCustomer(retrieveCustomerID());
         
      } catch (NullPointerException nullException ) {
         nullException.getStackTrace();
         System.out.println("NULL Customer @ AAC.retrieveCustomer");
         invalidFieldInputs.add("Customer");
   
      }catch (Exception e) {
         e.getStackTrace();
         System.out.println("Generic Exception in AppointmentAlterController.retrieveCustomer!");
         invalidFieldInputs.add("Customer");
      }
      
      return customer;
   }
   
   
   /**
    * Method creates and shows an Alert telling user to select a Start time before picking a duration.
    */
   private void setDurationBeforeStartAlert() {
      Alert durationBeforeStartAlert = new Alert(Alert.AlertType.ERROR);
      durationBeforeStartAlert.setTitle("Improper selection");
      durationBeforeStartAlert.setHeaderText("Pick a Start time before selecting a duration.");
      durationBeforeStartAlert.setContentText("We're sorry for any inconvenience but an Appointment must have a Start time before the duration can be set.");
      durationBeforeStartAlert.showAndWait();
   }
   
   
   /** Method creates and shows an Alert once an Appointment has been deleted.
    * @param appointment Input Appointment that has been deleted from the database.
    */
   private void confirmDeleteAlert(Appointment appointment) {
      Alert confirmDeleteAlert = new Alert(Alert.AlertType.INFORMATION);
      confirmDeleteAlert.setTitle("Appointment Deletion Confirmation");
      confirmDeleteAlert.setHeaderText("This Appointment has been permanently deleted.");
      confirmDeleteAlert.setContentText("Deleted Appointment info\nAppointment ID: " + appointment.getAppointmentID() +
                "\nAppointment Type: " + appointment.getType());
      confirmDeleteAlert.showAndWait();
   }
   
   
   /**
    * Method will display an Alert to the user that data is invalid if one or more FXML fields are left blank.
    */
   private void nullFieldAlert() {
      Alert saveAlert = new Alert(Alert.AlertType.ERROR);
      saveAlert.setTitle("Invalid new Appointment");
      saveAlert.setHeaderText("Fill in all fields. The fields listed below are wrong...?");
      String content = "";
      
      for (var invalidField: invalidFieldInputs) {
         content += invalidField + "\n";
         System.out.println("\tinvalidField: " + invalidField);
      }
      
      saveAlert.setContentText(content);
      saveAlert.showAndWait();
   }
   
   
   /**
    * Method creates and shows an Alert to user when there is a time conflict wth an already existing Appointment for the selected Contact.
    */
   private void appointmentOverlapAlert() {
      Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
      overlapAlert.setTitle("Invalid Appointment Time");
      overlapAlert.setHeaderText("The selected time conflicts with an existing Appointment for " + retrieveCustomer().getCustomerName() + ".");
      overlapAlert.setContentText("Adjust the time then try saving again.");
      overlapAlert.showAndWait();
   }
}
