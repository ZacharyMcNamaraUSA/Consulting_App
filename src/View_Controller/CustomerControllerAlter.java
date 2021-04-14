package View_Controller;

import Database.DAO.AppointmentDaoImpl;
import Database.DAO.CountryDaoImpl;
import Database.DAO.CustomerDaoImpl;
import Database.DAO.FirstLevelDivisionDaoImpl;
import Database.Entities.*;
import Utilities.CountryDivisionMap;
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
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerAlterController implements Initializable {
    
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
    private TextField appointmentTextField;
    
    @FXML
    private Button commitCustomerButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private TextField customerIDText;
    
    @FXML
    private TextField customerNameText;
    
    @FXML
    private TextField customerAddressText;
    
    @FXML
    private ComboBox<String> countryComboBox;
    
    @FXML
    private ComboBox<String> firstDivisionComboBox;
    
    @FXML
    private TextField customerPostalCodeText;
    
    @FXML
    private TextField customerPhoneText;
    
    @FXML
    private TextField customerDivision;
    
    @FXML
    private TextField customerAppointmentText;
    
    private static Customer customer = null;
    private FirstLevelDivisionDaoImpl firstLevelDivDao = new FirstLevelDivisionDaoImpl();
    private ArrayList<FirstLevelDivision> limitedFirstDivisions = new ArrayList<>();
    private ArrayList<Appointment> customerAppointments = new ArrayList<>();
    private CountryDivisionMap hashmap = new CountryDivisionMap();
    private CustomerDaoImpl customerDao = new CustomerDaoImpl();
    
    
    /**
     * Default constructor
     */
    public CustomerAlterController() {
    }
    
    
    /** Method is called from outside this class and is used to specify the Customer element to edit, if any.
     * @param editThisCustomer Input Customer element user has selected to edit.
     */
    public static void initData(Customer editThisCustomer) {
        customer = editThisCustomer;
    }
    
    
    /** Method is required to implement Initializable
     * @param url Required input - Directory location to search across.
     * @param resourceBundle Required input - resource needed to have proper text appear.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        
        populateCustomerFields();
        populateCountryOptions(CountryDaoImpl.allCountryList);
        
        populateFirstDivisionOptions(limitedFirstDivisions);
    }
    
    
    /**
     * Method populates all relevant input fields.
     *      If this is a new Customer, method will reset all fields.
     *      If editing an existing Customer, method will populate current Customer data to the appropriate fields.
     */
    private void populateCustomerFields() {
        
        try {
            if (customer == null) {
                System.out.println("NULL / new Customer");
                
                resetPromptText();
                
            }
            else {
                System.out.println("EDIT existing Customer");
                
                customerIDText.setText(String.valueOf(customer.getCustomerId()));
                customerNameText.setText(customer.getCustomerName());
                customerAddressText.setText(customer.getAddress());
                customerPostalCodeText.setText(customer.getPostalCode());
                customerPhoneText.setText(customer.getPhoneNumber());
                commitCustomerButton.setText("Update this Customer");
                
                // use Customer's private int firstDivisionId to get the FirstLevelDivision element.
                FirstLevelDivision firstLevelDivision = firstLevelDivDao.getFirstLevelDivision(customer.getFirstDivisionId());
                
                // use the FirstLevelDivision element and HashMap to get the Country element.
                Country country = hashmap.countryDivisionHashMap.get(firstLevelDivision);
                
                countryComboBox.setValue(country.getCountryName());
                limitedFirstDivisions = hashmap.getEveryFirstDivision(country);
                
                firstDivisionComboBox.setValue(firstLevelDivision.getDivisionName());
                
                System.out.println("Country: " + country.getCountryName());
                System.out.println("1st Division ID: " + customer.getFirstDivisionId());
                System.out.println("1st Division Name: " + firstLevelDivision.getDivisionName());
                
                detectCustomerAppointments();
                if (customerAppointments.size() != 0) {
                    customerAppointmentText.setText("This Customer has an Appointment connected to it and cannot be deleted.");
                    customerAppointmentText.setStyle("-fx-inner-background-color: red");
                }
            }
            
        } catch (NullPointerException nullPointerException) {
            System.out.println("CustomerAlterController.populateCustomerFields has a NULL");
            nullPointerException.getStackTrace();
        }
    }
    
    
    /** Method receives an ArrayList<Country> containing all relevant Country elements to populate to countryComboBox, once sorted.
     * @param allCountries Input ArrayList<Country> which contains all relevant Country elements.
     */
    private void populateCountryOptions(ArrayList<Country> allCountries) {
        ArrayList<String> allCountryNames = new ArrayList<>();
        
        for(Country country: allCountries) {
            allCountryNames.add(country.getCountryName());
        }
        allCountryNames.sort(String::compareToIgnoreCase);
        
        countryComboBox.getItems().addAll(allCountryNames);
    }
    
    
    /**
     * Method receives an ArrayList<FirstLevelDivision> to iterate over, retrieve the String name of each element,
     * then sort and add all Strings to the FirstLevelDivision's ComboBox, used for user selection.
     * @param limitedFirstDivisions Input ArrayList<FirstLevelDivision> used to populate the ComboBox for the Customer's FirstLevelDivision
     */
    private void populateFirstDivisionOptions(ArrayList<FirstLevelDivision> limitedFirstDivisions) {
        ArrayList<String> divisionNameList = new ArrayList<>();
        
        firstDivisionComboBox.getItems().clear();
        
        // if no limit is placed on FirstLevelDivision then all values are acceptable.
        if (limitedFirstDivisions.size() == 0) {
            limitedFirstDivisions = firstLevelDivDao.getAllFirstLevelDivisions();
        }
        
        // for each division in limitedFirstDivisions, add its String name to the divisionNameList.
        for (FirstLevelDivision division: limitedFirstDivisions) {
            divisionNameList.add(division.getDivisionName());
        }
        divisionNameList.sort(String::compareToIgnoreCase);
        firstDivisionComboBox.getItems().addAll(divisionNameList);
    }
    
    
    /**
     * Method resets input fields to default values
     */
    private void resetPromptText() {
        customerIDText.clear();
        customerNameText.clear();
        customerAddressText.clear();
        customerPostalCodeText.clear();
        customerPhoneText.clear();
//        populateCountryOptions();
        firstDivisionComboBox.getItems().clear();
        populateFirstDivisionOptions(FirstLevelDivisionDaoImpl.allFirstLevelDivisions);
        commitCustomerButton.setText("Add new Customer");
    }
    
    /**
     * @param event ActionEvent caused by user selecting the Cancel option from the Button cancelButton.
     */
    @FXML
    void cancelOperations(ActionEvent event) {
        returnToViewAllCustomers();
    }
    
    
    /**
     * Method is called when user wishes to save current Customer and will call the appropriate method for this commit.
     * @param event ActionEvent caused by user selecting the Button commitCustomerButton.
     */
    @FXML
    private void commitCustomer(ActionEvent event) {
        
        // if new Customer, add it
        if(commitCustomerButton.getText().contains("Add")) {
            saveNewCustomer();
        }
        else if (commitCustomerButton.getText().contains("Update")) {
            // if existing Customer, update
            updateCustomer();
        }
        else {
            System.out.println("Something CRAZY went wrong with CustomerAlterController.commitCustomer");
            System.exit(-20);
        }
    }
    
    
    /**
     * Method will attempt to save a new Customer to the database.
     */
    private void saveNewCustomer() {
        try {
            customerDao.addCustomerToDatabase(
                      customerNameText.getText(),
                      customerAddressText.getText(),
                      customerPostalCodeText.getText(),
                      customerPhoneText.getText(),
                      firstLevelDivDao.getFirstLevelDivision(firstDivisionComboBox.getValue()).getDivisionId()
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("That should have created a new Customer object...\nReturning to ViewAllCustomers now!");
        returnToViewAllCustomers();
    }
    
    
    /**
     * Method will attempt to update the current Customer being edited by user.
     */
    private void updateCustomer() {
        // update current Customer element to match current fields then commit the update to the database.
        customer.setCustomerName(customerNameText.getText());
        customer.setAddress(customerAddressText.getText());
        customer.setPostalCode(customerPostalCodeText.getText());
        customer.setPhoneNumber(customerPhoneText.getText());
        customer.setLastUpdate(ZonedDateTime.now());
        customer.setLastUpdatedBy(MainMenuController.activeUser.getUserName());
        
        try {
            customerDao.updateCustomer(customer);
            
        } catch (Exception e) {
            e.getStackTrace();
        }
        
        returnToViewAllCustomers();
    }
    
    
    /**
     * Method is caused by user input to the Country ComboBox.
     *      Once the user designates a Country, only the appropriate FirstLevelDivision should be populated to the First Level Division ComboBox.
     * @param event Input ActionEvent is caused by user selecting a Country from its ComboBox.
     */
    @FXML
    private void initiateDeleteCustomer(ActionEvent event) {
        if (customerAppointments.size() != 0) {
            customerHasAppointmentAlert();
        }
        else {
            System.out.println("No Appointment for this Customer means we can delete!" );
            boolean successfulDelete = false;
            successfulDelete = customerDao.deleteCustomer(customer);
            if (successfulDelete) {
                System.out.println("Deleted Customer!");
                
                displayDeleteAlert(customer);
                
                returnToViewAllCustomers();
            }
            else {
                System.out.println("Could NOT delete Customer");
                Alert failedDeleteAlert = new Alert(Alert.AlertType.ERROR);
                failedDeleteAlert.setTitle("Invalid Customer Delete");
                failedDeleteAlert.setHeaderText("This Delete attempt failed for an unknown reason.");
                failedDeleteAlert.setContentText("Contact your boss with details of what lead up to this error message.");
                failedDeleteAlert.showAndWait();
            }
        }
    }
    
    
    /** Method creates and shows an Alert once a Customer has been deleted from the database.
     * @param customer Input Customer to be deleted from the database.
     */
    private void displayDeleteAlert(Customer customer) {
        Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION);
        deleteAlert.setTitle("Contact Deletion Confirmation");
        deleteAlert.setHeaderText("This Contact has been permanently deleted.");
        deleteAlert.setContentText("Deleted Contact info\nCustomer ID: " + customer.getCustomerId() +
                  "\nCustomer Name: " + customer.getCustomerName() +
                  "\nCustomer Phone number: " + customer.getPhoneNumber());
        deleteAlert.showAndWait();
    }
    
    
    
    /**
     * Method is called when the user has selected a country from its ComboBox and the First Level Division needs to be populated appropriately.
     * @param event The ActionEvent caused by user input to the Country ComboBox
     */
    @FXML
    void userSelectedCountry(ActionEvent event) {
        // user's selected Country's name.
        String selectedCountryName = countryComboBox.getValue();
        Country countrySelected = CountryDaoImpl.getCountry(selectedCountryName);
    
        limitedFirstDivisions = hashmap.getEveryFirstDivision(countrySelected);
        
        populateFirstDivisionOptions(limitedFirstDivisions);
        
    }
    
    
    /**
     * Method changes the stage and control to /View_Controller/ViewAllCustomers.fxml.
     */
    private void returnToViewAllCustomers() {
    
        try {
            Stage primaryStage = Main.myStage;
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ViewAllCustomers.fxml"));
            Parent root = (Parent)loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
        
            primaryStage.show();
        
        } catch (IOException ioException) {
            System.out.println("CustomerAlterController.ioException");
            ioException.getStackTrace();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
    
    /**
     * Method iterates across all Appointments to find if any are attached to the current Customer.
     *      If any match is found, add the Appointment to the list of the customer's appointments.
     */
    private void detectCustomerAppointments() {
        int customerId = customer.getCustomerId();
        
        try {
            AppointmentDaoImpl appointmentDao = new AppointmentDaoImpl();
            for (Appointment appointment: appointmentDao.getAllAppointments()) {
                if (appointment.getCustomerId() == customerId) {
                    customerAppointments.add(appointment);
                }
            }
            
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
    
    /**
     * Method will display a custom Alert when user attempts to delete a Customer with an Appointment.
     */
    private void customerHasAppointmentAlert() {
        Alert appointmentAlert = new Alert(Alert.AlertType.ERROR);
        appointmentAlert.setTitle("Invalid Customer Delete");
        appointmentAlert.setHeaderText("You cannot delete a Customer with any appointments");
        appointmentAlert.setContentText("If desired, delete all relevant appointments first then try to delete this Customer again.");
        appointmentAlert.showAndWait();
    }
}
