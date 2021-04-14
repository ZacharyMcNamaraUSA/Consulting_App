package View_Controller;

import Database.DAO.UserDaoImpl;
import Database.Entities.User;
import Utilities.TimeUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import sample.Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserLoginMainController implements Initializable {

   @FXML
   private Label loginUserNameLabel;

   @FXML
   private ChoiceBox<String> loginUserNameChoiceBox;

   @FXML
   private Label loginPasswordLabel;

   @FXML
   private PasswordField loginPasswordField;

   @FXML
   private Button loginEnterButton;

   @FXML
   private Label userLoginTimeLabel;

   @FXML
   private Label userLoginLocationLabel;

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
   private Label userLoginDateLabel;


   private final Locale userLocale = Locale.getDefault();
   private UserDaoImpl usersDaoImpl = new UserDaoImpl();
   private String colonSpacer = ": ";
   private String lineSpacer = " | ";


   /**
    * Method populates the ChoiceBox with all users from the database and
    *    adjusts text to match user's default configuration.
    * @param url Required param from Override - holds Location
    * @param resourceBundle Required param from Override
    */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {

      adjustTextToUserSettings();
      populateUserNames();

      // for testing purposes
      // TODO REMOVE AUTO-ENTERED PASSWORD
      loginUserNameChoiceBox.setValue("test");
      loginPasswordField.setText("test");
      
   }


   /**
    * Invoked by user attempting to log-in, this method compares the selected Username
    * and entered Password with the database record.
    * Record all attempts in "login_activity.txt"
    *
    * @param event The ActionEvent from loginEnterButton that calls this method.
    */
   @FXML
   private void initiateUserLogin(ActionEvent event) {

      String username = loginUserNameChoiceBox.getValue();
      String enteredPassword = loginPasswordField.getText();
      String storedPassword = "";
      boolean successfulLogin = false;
      User loginUser = null;

      updateDateTime();

      try {
         storedPassword = (usersDaoImpl.getSingleUsers(username)).getPassword();
      } catch (Exception e) {
         loginFail();
      }

//      System.out.println("Initiate login for user '" + username + "'!");
//      System.out.println("\tPassword entered = " + enteredPassword);
      System.out.println((storedPassword.equals(enteredPassword)) ? "\tPasswords match!" : "\tPasswords do not match.");

      if (enteredPassword.equals(storedPassword)) {
         successfulLogin = true;
         loginUser = usersDaoImpl.getSingleUsers(username);
      }
      else {     //login failed

         loginFail();
      }

      if(loginUser != null)
         MainMenuController.initData(loginUser);
      else
         throw(new NullPointerException());

      recordLoginAttempt(successfulLogin, username);

      if (successfulLogin) {
         try {
               Stage primaryStage = Main.myStage;

               FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainMenu.fxml"));
//               URL fxmlLocation = getClass().getResource("/View_Controller/MainMenu.fxml");
//               loader.setLocation(fxmlLocation);
//               loader.setController(mainMenuController);
               Parent root = loader.load();
               primaryStage.setScene(new Scene(root));
               primaryStage.setResizable(false);

            primaryStage.show();

         } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
         }
         catch (Exception e) {
            e.getStackTrace();
         }
      }


      } // initiateUserLogin

   /**
    * Method appends login attempts with outcome and timestamps to "login_attempt.txt"
    * @param successfulLogin This boolean tracks whether the login was a success or not.
    * @param username The User_Name that attempted to login.
    */
   private void recordLoginAttempt(boolean successfulLogin, String username) {

      try (FileWriter fw = new FileWriter("login_attempt.txt", true);
           BufferedWriter bw = new BufferedWriter(fw);
           PrintWriter printWriter = new PrintWriter(bw)) {
         printWriter.println(username + lineSpacer + userLoginDateLabel.getText() + lineSpacer +
                 ZonedDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + lineSpacer +
                 (successfulLogin ? "Successful login":"Failure to login"));
         // attempts, dates, timestamps, success/fail

      } catch (IOException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    *  Method presents the user with an AlertType.ERROR Alert, in the appropriate language, telling them the login info
    *  was incorrect and login must be re-attempted.
    */
   private void loginFail() {
      Alert loginFail = new Alert(Alert.AlertType.ERROR);
      loginFail.setTitle(translateText("loginfail"));
      loginFail.setContentText(translateText("loginagain"));
      loginFail.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> resetApp());
   }

   /**
    *  Method to be used after an unsuccessful login attempt - empties loginPasswordField and calls updateDateTime()
    *
    */
   private void resetApp() {
      loginPasswordField.setText("");
      updateDateTime();
   }


   /**
    * Method changes all applicable text to match user's display language using translateText(keyText).
    */
   private void adjustTextToUserSettings() {

      updateDateTime();

      // Determine user country and set userLoginLocationLabel
      String userLocation = TimeUtilities.userZoneId.toString();
      userLoginLocationLabel.setText(translateText("Location") + colonSpacer + userLocation) ;

      loginUserNameLabel.setText(translateText("Username"));
      loginPasswordLabel.setText(translateText("Password"));
      loginEnterButton.setText(translateText("Login"));
      fileMenu.setText(translateText("File"));
      fileMenuNew.setText(translateText("New"));
      fileMenuOpen.setText(translateText("Open"));
      fileMenuOpenRecent.setText(translateText("OpenRecent"));
      fileMenuClose.setText(translateText("Close"));
      fileMenuSave.setText(translateText("Save"));
      fileMenuSaveAs.setText(translateText("SaveAs"));
      fileMenuRevert.setText(translateText("Revert"));
      fileMenuPreferences.setText(translateText("Preferences"));
      fileMenuQuit.setText(translateText("Quit"));
      editMenu.setText(translateText("Edit"));
      editMenuUndo.setText(translateText("Undo"));
      editMenuRedo.setText(translateText("Redo"));
      editMenuCut.setText(translateText("Cut"));
      editMenuCopy.setText(translateText("Copy"));
      editMenuPaste.setText(translateText("Paste"));
      editMenuDelete.setText(translateText("Delete"));
      editMenuSelectAll.setText(translateText("SelectAll"));
      editMenuUnselectAll.setText(translateText("UnselectAll"));
      editMenuHelp.setText(translateText("Help"));
      editMenuAbout.setText(translateText("About"));

   } // adjustTextToUserSettings

   /**
    * Method is useful for changing the date in userLoginDateLabel and time in userLoginTimeLabel.
    * Method is called on unsuccessful login attempts.
    */
   private void updateDateTime() {

      // Change date format based on location - only USA wants to be different
      Instant instantTest = Instant.now();
      ZoneId zoneId = ZoneId.of("America/Chicago");
      ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instantTest, zoneId);

      if (userLocale.toString().equals("en_US")) {
         userLoginDateLabel.setText(translateText("Date") + colonSpacer +
                 ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      } else {
         userLoginDateLabel.setText(translateText("Date") + colonSpacer +
                 ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
      }

      // Display time to userLoginTimeLabel
      userLoginTimeLabel.setText(translateText("Time") + colonSpacer +
              zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
   } //updateDateTime


   /**
    * Populates every User_Name into the ChoiceBox loginUserNameChoiceBox
    */
   private void populateUserNames() {

      ObservableList<String> allUserNames = FXCollections.observableArrayList();

      for (User user : usersDaoImpl.getAllUsers()) {
         allUserNames.add(user.getUserName());
      }

      loginUserNameChoiceBox.setItems(allUserNames);

   } //populateUserNames


   /**
    * Method receives keyText and translates it to the userLocale display language.
    *    Change Locale.getDefault() if you wish to test other languages.
    * @param keyText the text to be translated
    */
   private String translateText(@NotNull String keyText) {

      return (ResourceBundle.getBundle("LabelsBundle", Locale.getDefault())).getString(keyText);

      // optional return for testing French translation
//      return (ResourceBundle.getBundle("LabelsBundle", Locale.CANADA_FRENCH)).getString(keyText);

   } //translateText



} //class
