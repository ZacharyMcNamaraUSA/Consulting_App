package View_Controller;

import Database.DAO.AppointmentDaoImpl;
import Database.DAO.ContactDaoImpl;
import Database.Entities.Appointment;
import Database.Entities.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class ReportContactSchedulesController implements Initializable {
     
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
     private javafx.scene.control.Menu editMenuHelp;
     
     @FXML
     private javafx.scene.control.MenuItem editMenuAbout;
     
     @FXML
     private javafx.scene.control.Button mainMenuButton;
     
     @FXML
     private javafx.scene.control.ComboBox<String> contactComboBox;
     
     @FXML
     private javafx.scene.control.TableView<Appointment> appointmentTableView;
     
     @FXML
     private javafx.scene.control.TableColumn<Appointment, Integer> columnApptId;
     
     @FXML
     private javafx.scene.control.TableColumn<Appointment, String> columnTitle;
     
     @FXML
     private javafx.scene.control.TableColumn<Appointment, String> columnDescription;
     
     @FXML
     private javafx.scene.control.TableColumn<Appointment, String> columnLocation;
     
     @FXML
     private javafx.scene.control.TableColumn<Appointment, String> columnType;
     
     @FXML
     private javafx.scene.control.TableColumn<Appointment, ZonedDateTime> columnStartDateTime;
     
     @FXML
     private javafx.scene.control.TableColumn<Appointment, ZonedDateTime> columnEndDateTime;
     
     @FXML
     private javafx.scene.control.TableColumn<Appointment, Integer> columnCustomerID;
     
     
     private Database.DAO.ContactDaoImpl contactDao = new ContactDaoImpl();
     private Database.Entities.Contact selectedContact = null;
     
     
     /**
      * Default Constructor
      */
     public ReportContactSchedulesController() {
          System.out.println("Report-ContactSchedules def. constructor");
     }
     
     
     /**
      * Required method to implement Initializable then use FXML files and controllers.
      * @param url Input URL is a location - this is called using files not written by team.
      * @param resourceBundle Input ResourceBundle is a collection of properties - called using files not written by team.
      */
     @Override
     public void initialize(URL url, ResourceBundle resourceBundle) {
          System.out.println("initialize");
          
          // initially, only the ComboBox of Contact names is populated.
          populateContactComboBox();
          setCellValueFactories();
     }
     
     /**
      * Method will populate the schedule for the Contact that the user selects from contactComboBox.
      * @param event Input ActionEvent from FXML Application.
      */
     @FXML
     void userSelectedContact(ActionEvent event) {
          // get the Contact the user has selected, via its name, a String.
          selectedContact = retrieveContact();
          
          // populate every Appointment for this Contact to the TableView
          populateAppointmentTableView(selectedContact);
     }
     
     
     /**
      * Method returns control to MainMenuController when user selects mainMenuButton.
      * @param event Input ActionEvent is called by FXML Initializable.
      * @throws IOException Exception throw is required for FXMLLoader's load() method.
      */
     @FXML
     void userSelectedMainMenu(ActionEvent event) throws IOException {
          Stage primaryStage = Main.myStage;
          
          System.out.println("return to MainMenu.fxml    [ReportContactSchedules --> MainMenu]");
     
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainMenu.fxml"));
          Parent root = loader.load();
          primaryStage.setScene(new Scene(root));
          primaryStage.setResizable(false);
          primaryStage.setTitle("Consulting App - Main Menu");
     
          primaryStage.show();
     }
     
     
     
     private void setCellValueFactories() {
          // OK
          columnApptId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("AppointmentID"));
          columnTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Title"));
          columnDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Description"));
          columnLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Location"));
          columnType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Type"));
          columnStartDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, ZonedDateTime>("FormattedStartString"));
          columnEndDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, ZonedDateTime>("FormattedEndString"));
          columnCustomerID.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("CustomerId"));
          
          
     }
     
     
     /**
      * Method will populate Appointment info relevant to the provided Contact to the TableView, after the Database.Entities.Contact is selected.
      * @param contact Input Contact used to filter each Appointment.
      */
     private void populateAppointmentTableView(Contact contact) {
          // clear the current TableView as the selected Contact may be different.
          appointmentTableView.getItems().clear();
          
          // ObservableList to be new items in appointmentTableView
          ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
          
          // add items to appointmentObservableList...
          Database.DAO.AppointmentDaoImpl appointmentDao = new AppointmentDaoImpl();
          // for each Appointment, check to see if the Contact matches user-selected Contact
          for (Database.Entities.Appointment appointment: appointmentDao.getAllAppointments()) {
               if (selectedContact != null && appointment.getContactId() != selectedContact.getContactId()) {
                    // if this Appointment is linked with a non-null selectedContact, add this appointment
                    appointmentObservableList.add(appointment);
                    System.out.println("Report_Contact's popAppt to TableView is adding... Appointment ID = " + appointment.getAppointmentID());
               }
          }
          
          
          
          appointmentTableView.setItems(appointmentObservableList);
     }
     
     
     /**
      * Method receives a List of Contacts and adds each Contact's Name, a String, to the contactComboBox.
      * @param contactList Input Contact List used to retrieve the
      */
     private void populateContactComboBox(java.util.List<Contact> contactList) {
          for (Database.Entities.Contact forEachContact: contactList) {
               try {
                    contactComboBox.getItems().add(forEachContact.getContactName());
                    
               } catch (Exception e) {
                    System.out.println("ReportContactSchedulesController.populateContactComboBox(List<Contact> ...)" +
                              "\nIDK what but something went wrong =\\");
                    e.getStackTrace();
               }
          }
     }
     
     
     /**
      * Method overloading allows for default populating List to include every Contact in Database.DAO.ContactDaoImpl.
      */
     private void populateContactComboBox() {
          populateContactComboBox(contactDao.getAllContacts());
     }
     
     
     /**
      * Method attempts to return a Database.Entities.Contact from the selected String in contactComboBox.
      * @return Returns the relevant Contact or null.
      */
     private Contact retrieveContact() {
          
          Database.Entities.Contact contact = null;
          
          try {
               contact = contactDao.getSingleContact(contactComboBox.getValue());
               
          } catch (NullPointerException e) {
               System.out.println("NullPointerException in ReportContactSchedulesController.retrieveContact()");
               e.getStackTrace();
          }
     
          return contact;
     }
}
