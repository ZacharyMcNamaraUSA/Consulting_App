package sample;

import Database.DAO.CustomerDaoImpl;
import Database.DBConnection;
import Utilities.TimeUtilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage myStage = null;
    
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        myStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/UserLoginMain.fxml"));
        primaryStage.setTitle("Consulting App");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.setResizable(false);
        primaryStage.setX(100);
        primaryStage.setY(100);
        primaryStage.show();
    }
    
    


    public static void main(String[] args) {        
        
        DBConnection.initiateConnection();
        Timestamp start = new Timestamp(System.currentTimeMillis());
    
        
        // TODO limit Appointment scheduling to be within business hours 0800-2200 EST w/ weekends
        
        // TODO prevent overlapping scheduling of Appointment for Customer
        
        // TODO add report to UI of total number of Customer Appointments by type and month
        
        // TODO add schedule for each contact in the org w/ Appointment ID, title, type & description, start/end date + time, and customer ID.
        
        // TODO an additional report of my choosing!
        
        // TODO 2 or more lambda expressions to improve code, with explanations.
        
        // TODO
        
        
        launch(args);
        
        DBConnection.endConnection();
        Timestamp end = new Timestamp(System.currentTimeMillis());
        
        System.out.format("run time: " + (end.getTime() - start.getTime()) + "ms.\n");
    }

}
