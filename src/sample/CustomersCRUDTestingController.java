package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import Database.DBConnection;
import Database.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomersCRUDTestingController {

   @FXML
   private Font x1;

   @FXML
   private Color x2;

   @FXML
   private Button CustomersAddButton;

   @FXML
   private Button CustomersDeleteButton;

   @FXML
   private Font x3;

   @FXML
   private Color x4;

   @FXML
   void OpenAlterCustomers(ActionEvent event) {
      Connection conn = DBConnection.getConnection();

      System.out.println("Add pressed");


      try {
         PreparedStatement statement = DBQuery.getPreparedStatement();

         String selectStatement = "SELECT * FROM contacts";

         DBQuery.setPreparedStatement(conn, selectStatement);

         PreparedStatement psSelect = DBQuery.getPreparedStatement();

         psSelect.execute();

         ResultSet rs = psSelect.getResultSet();
         int idTracker = 0;

         while (rs.next())   {
            System.out.println("");
            int contID = rs.getInt("Contact_ID");
            idTracker = contID;
            String contName = rs.getString("Contact_Name");
            String contEmail = rs.getString("Email");
            System.out.println("ID #" + contID + "\t\tName: " + contName + "\t\tEmail: " + contEmail );
         }

//         String idTrackerString = Integer.toString((++idTracker));
//         String contactID = idTrackerString;
//         String contactName = "John Smith" + idTrackerString;
//         String contactEmail = "jsmith" + idTrackerString + "@company.com";

         String insertStatement = "INSERT INTO contacts(Contact_ID, Contact_Name, Email)" +
                 " VALUES(?,?,?)";

         //Create PreparedStatement
         DBQuery.setPreparedStatement(conn, insertStatement);

         PreparedStatement ps = DBQuery.getPreparedStatement();
//         ps.setString(1, contactID);
//         ps.setString(2, contactName);
//         ps.setString(3, contactEmail);
         ps.execute();           // Execute PreparedStatement



         //Confirm rows affected
         int affectedRows = ps.getUpdateCount();
         if(affectedRows > 0)
            System.out.println(affectedRows + " row(s) affected!");
         else
            System.out.println("NO AFFECTED ROWS");
      } catch (Exception e) {
         e.getStackTrace();
      }




   }

   @FXML
   void attemptCustomersDelete(ActionEvent event) {
      System.out.println("Attempting delete...");
   }

}

