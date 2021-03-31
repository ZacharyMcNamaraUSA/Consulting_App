package sample;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {

    public static Stage myStage = null;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        myStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/UserLoginMain.fxml"));
        primaryStage.setTitle("Consulting App");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.setX(100);
        primaryStage.setY(100);
        primaryStage.show();
    }


    public static void main(String[] args) {

        DBConnection.initiateConnection();
        Timestamp start = new Timestamp(System.currentTimeMillis());

        launch(args);

        DBConnection.endConnection();
        Timestamp end = new Timestamp(System.currentTimeMillis());

        System.out.format("run time: " + (end.getTime() - start.getTime()) + "ms.\n");
    }

}
