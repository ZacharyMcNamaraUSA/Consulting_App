package View_Controller;

import Database.DAO.CountryDaoImpl;
import Database.DAO.FirstLevelDivisionDaoImpl;
import Database.Entities.Appointment;
import Utilities.MyAlert;
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
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import sample.Main;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
   private RadioButton weeklyRadioButton;

   @FXML
   private RadioButton monthlyRadioButton;

   @FXML
   private Button viewCustomersButton;
   
   @FXML
   private Button generateReportButton;
   
   @FXML
   private CheckBox typeCheckBox;
   
   @FXML
   private CheckBox monthCheckBox;
   
   @FXML
   private CheckBox contactScheduleCheckBox;
   
   @FXML
   private CheckBox countryCheckBox;
   
   
   public static User activeUser = null;
   private Database.DAO.CustomerDaoImpl customerDaoImpl = new Database.DAO.CustomerDaoImpl();
   private Database.DAO.AppointmentDaoImpl appointmentsDaoImpl = new Database.DAO.AppointmentDaoImpl();
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
      initialPopulateAppointmentTable();
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
    * Method displays relevant data to appointmentsTableView, limited by RadioButton by calling limitAppointments(...)
    */
   private void initialPopulateAppointmentTable() {
      
      // create the factory for each cell's value
      setCellValueFactories();

      ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
      
      if (appointmentsDaoImpl.getAllAppointments() != null) {
         
         ArrayList<Appointment> limitedAppointments = getLimitedAppointments();
         appointmentObservableList.addAll(limitedAppointments);

      }

      appointmentsTableView.setItems(appointmentObservableList);
   }
   
   
   /**
    * Method will clear then repopulate appointmentsTableView.
    */
   private void resetAppointmentsTableView() {
      // create appointmentObservableList and add each relevant Appointment.
      ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
      appointmentObservableList.addAll(getLimitedAppointments());
      
      // remove current items
      appointmentsTableView.getItems().clear();
      // add all new items
      appointmentsTableView.getItems().addAll(appointmentObservableList);
      
   }
   
   
   /**
    * Method retrieves the latest acceptable ZonedDateTime for an Appointment to be shown,
    * @return Returns an ArrayList<Appointment> of every Appointment that meets the currently selected criteria.
    */
   private ArrayList<Appointment> getLimitedAppointments() {
      ArrayList<Appointment> appointments = new ArrayList<>();
      
      ZonedDateTime tableViewEnd = getTableViewEnd();
      
      appointments = limitAppointments(tableViewEnd);
      
      return appointments;
   }
   
   
   /**
    * Method detects which RadioButton is selected then adds the relevant time from now to specify which Appointment(s) to populate.
    * @return Returns the appropriate ZonedDateTime to be the furthest acceptable Appointment start from runtime.
    */
   private ZonedDateTime getTableViewEnd() {
      
      ZonedDateTime end = null;
   
      // check if weekly is selected
      if (weeklyRadioButton.isSelected() ) {
         end = (ZonedDateTime.now().plusWeeks(1));
      }
      // ELSE the monthlyRadioButton must be selected.
      else {
         end = (ZonedDateTime.now().plusMonths(1));
      }
      
      return end;
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
    * Method is called by user's action on UI - method will call for appointmentsTableView to be adjusted, if appropriate.
    * @param event Input ActionEvent caused by user's select on UI.
    */
   @FXML
   private void weeklyRadioButtonSelected(ActionEvent event) {
      
      System.out.println("MMC is resetting appointmentsTableView - view by WEEK");
      resetAppointmentsTableView();
   }
   
   
   /**
    * Method is called by user's action on UI - method will call for appointmentsTableView to be adjusted, if appropriate.
    * @param event Input ActionEvent caused by user's select on UI.
    */
   @FXML
   private void monthlyRadioButtonSelected(ActionEvent event) {
   
      System.out.println("MMC is resetting appointmentsTableView - view by MONTH");
      resetAppointmentsTableView();
   }
   
   
   /**
    * Method uses property value factory on appointmentTableView.
    */
   private void setCellValueFactories() {
      
      columnApptId.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
      columnTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
      columnDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
      columnLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
      columnType.setCellValueFactory(new PropertyValueFactory<>("Type"));
      columnStartDateTime.setCellValueFactory(new PropertyValueFactory<>("FormattedStartString"));
      columnEndDateTime.setCellValueFactory(new PropertyValueFactory<>("FormattedEndString"));
      columnCustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
      columnContactID.setCellValueFactory(new PropertyValueFactory<>("ContactId"));
      columnUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
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
    * Method populates the selected Appointment to the
    * @param actionEvent Input actionEvent is send by FXML Application.
    * @throws IOException Throws IOException, as required, if this occurs during loading.
    */
   public void editSelectedAppointment(ActionEvent actionEvent) throws IOException {
      Stage primaryStage = Main.myStage;

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AppointmentAlter.fxml"));
      AppointmentAlterController aac = new AppointmentAlterController();
      loader.setController(aac);
      Parent root = loader.load();
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.setTitle("Consulting App - Edit an Appointment");
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
      primaryStage.setTitle("Consulting App - Add an Appointment");
      aac.initData(null);

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
         primaryStage.setTitle("Consulting App - View All Customers");
         
         primaryStage.show();
         
      } catch (IOException ioException) {
         System.out.println("MainMenuController.ioException");
         ioException.getStackTrace();
      } catch (Exception e) {
         e.getStackTrace();
      }

   }
   
   
   /**
    * Method ensures one or more report variation is checked via its CheckBox then calls the report display.
    * @param event Input ActionEvent from FXML's Application when user activates the Button generateReportButton.
    */
   @FXML
   private void attemptReportGeneration(ActionEvent event) {
      
      System.out.println("generate report(s)!");
   
      // if no CheckBox is selected, show Alert then exit method
      if (!typeCheckBox.isSelected() && !monthCheckBox.isSelected() && !contactScheduleCheckBox.isSelected() && !countryCheckBox.isSelected()) {
         MyAlert unknownAlert = new MyAlert(Alert.AlertType.ERROR);
         unknownAlert.invalidSelectionAlert("Report option");
         return;
      }
   
      // else, for any selected CheckBox, display relevant report.
      if(typeCheckBox.isSelected()) {
         typeReport();
      }
      else if (monthCheckBox.isSelected()) {
         monthReport();
      }
      else if(contactScheduleCheckBox.isSelected()) {
         contactScheduleReport();
      }
      else if(countryCheckBox.isSelected()) {
         countryReport();
      }
      
   }
   
   
   /**
    * Method creates and shows Appointment report, sorted by Type.
    */
   private void typeReport() {
      
      // TODO add report to UI of total number of Customer Appointments by type and month
      System.out.println("report by Type\n");
      
      ArrayList<String> typeList = new ArrayList<>();
      String contextText = "";
      
      for (Appointment appointment: appointmentsDaoImpl.getAllAppointments() ) {
         typeList.add(appointment.getType());
      }
      
      // create a temporary copy of the ArrayList containing every Type, so we do not show duplicates.
      @SuppressWarnings("unchecked")
      ArrayList<String> typeListClone = (ArrayList<String>) typeList.clone();
      System.out.println("typeListClone original size is same as typeList at " + typeListClone.size() + " elements.");
      
      
      for (String currentType: typeListClone) {
         
         int frequency = Collections.frequency(typeListClone, currentType);
         
         if (frequency > 0) {
            System.out.println("Printing then removing type: " + currentType + ".");
            System.out.println("\tfrequency: " + frequency);
            
            contextText += "\tCount: " + frequency + "\tof Type " + currentType + ".\n";
         }
         
         // creating ANOTHER temp clone...
         @SuppressWarnings("unchecked")
         ArrayList<String> typeRemovingDuplicates = (ArrayList<String>) typeListClone.clone();
         // remove all elements matching the current String.
         while (typeRemovingDuplicates.contains(currentType)) {
            typeRemovingDuplicates.remove(currentType);
         }
         
         // set the ArrayList, that is originally being iterated across, equal to the ArrayList with duplicates of currentType removed.
         typeListClone = (ArrayList<String>) typeRemovingDuplicates.clone();
      }
      
      //Create dialog
      myDialog("Appointments by Type", "Below is each Appointment type and its count.", contextText);
   }
   
   
   /**
    * Method creates and shows Appointment report, sorted by Month.
    */
   private void monthReport() {
      
      // TODO add report to UI of total number of Customer Appointments by type and month
      System.out.println("report by Month\n");
      ArrayList<String> monthsList = new ArrayList<>();
      String contentText = "";
      
      for (Appointment appointment: appointmentsDaoImpl.getAllAppointments() ) {
         monthsList.add(appointment.getStartZonedDateTime().format(DateTimeFormatter.ofPattern("MMMM")));
      }
      
      // create a temporary copy of the ArrayList containing every Type, so we do not show duplicates.
      ArrayList<String> monthsListClone = (ArrayList<String>) monthsList.clone();
      System.out.println("monthsListClone original size is same as typeList at " + monthsListClone.size() + " elements.");
      
      for (String currentMonth: monthsListClone) {
         
         int frequency = Collections.frequency(monthsListClone, currentMonth);
         
         if (frequency > 0) {
            System.out.println("Printing then removing month: " + currentMonth + ".");
            System.out.println("\tfrequency: " + frequency);
            
            contentText += "\t" + currentMonth + " has " + frequency +  " Appointment(s).\n";
         }
         
         // creating ANOTHER temp clone...
         ArrayList<String> monthsRemovingDuplicates = (ArrayList<String>) monthsListClone.clone();
         // remove all elements matching the current String.
         while (monthsRemovingDuplicates.contains(currentMonth)) {
            monthsRemovingDuplicates.remove(currentMonth);
         }
         
         // set the ArrayList, that is originally being iterated across, equal to the ArrayList with duplicates of currentMonth removed.
         monthsListClone = (ArrayList<String>) monthsRemovingDuplicates.clone();
      }
      
      //Create dialog
      myDialog("Appointments by Month",
                "Below, every Appointment is grouped by their month then counted.",
                contentText);
   }
   
   
   /**
    * Passes control to ReportContactSchedulesController.java when user selects to generate this report.
    */
   private void contactScheduleReport() {
      // TODO add schedule for each Contact in the org w/ Appointment ID, title, type & description, start/end date + time, and customer ID.
   
      System.out.println("report of each Contact's schedule");
   
   
      try {
         Stage primaryStage = Main.myStage;
   
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ReportContactSchedules.fxml"));
         ReportContactSchedulesController controller = new ReportContactSchedulesController();
         loader.setController(controller);
         Parent root = loader.load();
         primaryStage.setScene(new Scene(root));
         primaryStage.setResizable(false);
         primaryStage.setTitle("Consulting App - Schedule by Contact");
         
         primaryStage.show();
      
      } catch (IOException ioException) {
         System.out.println("MainMenuController.ioException");
         ioException.getStackTrace();
      } catch (Exception e) {
         e.getStackTrace();
      }
      
      
   }
   
   
   /**
    * Method passes control to ReportCountryController.java when user selects to generate this report.
    */
   private void countryReport() {
      // TODO an additional report of every scheduled Appointment by Country the Customer is in.
   
      System.out.println("report grouping every Appointment by the Country the Customer is in");
      
      ArrayList<String> countryList = new ArrayList<>();
      String contentText = "";
      Database.Entities.Country country = null;
      Database.DAO.FirstLevelDivisionDaoImpl firstLevelDivisionDao = new FirstLevelDivisionDaoImpl();
      CountryDaoImpl countryDao = new CountryDaoImpl();
   
      // for each Appointment
      for (Appointment appointment: appointmentsDaoImpl.getAllAppointments() ) {
         
         // Find the Customer from the Appointment
         Database.Entities.Customer customer = customerDaoImpl.getSingleCustomer(appointment.getCustomerId());
         
         // Find the Country from the Customer's FirstDivisionId
         Database.Entities.FirstLevelDivision firstLevelDivision = firstLevelDivisionDao.getFirstLevelDivision(customer.getFirstDivisionId());
         country = countryDao.getCountry(firstLevelDivision.getCountryId());
         
         // add country's name (String) to countryList
         countryList.add(country.getCountryName());
         
         // reset country for next iteration
         country = null;
         
      }
   
      // create a temporary copy of the ArrayList containing every Type, so we do not show duplicates.
      ArrayList<String> countryListClone = (ArrayList<String>) countryList.clone();
      System.out.println("countryListClone original size is same as countryList at " + countryListClone.size() + " elements.");
   
   
      for (String currentCountry: countryListClone) {
      
         int frequency = Collections.frequency(countryListClone, currentCountry);
      
         if (frequency > 0) {
            System.out.println("Printing then removing type: " + currentCountry + ".");
            System.out.println("\tfrequency: " + frequency);
         
            contentText += "" + frequency + "\t Appointment(s) in " + currentCountry + ".\n";
         }
      
         // creating ANOTHER temp clone...
         ArrayList<String> typeRemovingDuplicates = (ArrayList<String>) countryListClone.clone();
         // remove all elements matching the current String.
         while (typeRemovingDuplicates.contains(currentCountry)) {
            typeRemovingDuplicates.remove(currentCountry);
         }
      
         // set the ArrayList, that is originally being iterated across, equal to the ArrayList with duplicates of currentCountry removed.
         countryListClone = (ArrayList<String>) typeRemovingDuplicates.clone();
      }
   
      //Create dialog
      myDialog("Appointments by Country",
                "Each Appointment is sorted by the Country of the Customer \n\t- useful global view of market share.",
                contentText);
   }
   
   
   /**
    * Method creates a Dialog, using parameters, and resizes it.
    *    This seemed like a cleaner solution.
    * @param title Input String to be the Dialog's title text.
    * @param header Input String to be the Dialog's header text.
    * @param content Input String to be the Dialog's content text.
    */
   private void myDialog(String title, String header, String content) {
      
      //Creating a dialog
      Dialog<String> dialog = new Dialog<String>();
      
      //Setting the title
      dialog.setTitle(title);
      
      // Setting the header of the dialog
      dialog.setHeaderText(header);
      //Setting the content of the dialog
      dialog.setContentText(content);
      
      //Adding done/close button to the dialog pane
      ButtonType type = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().add(type);
      
      // resize dialog
      dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      
      dialog.showAndWait();
   }
}
