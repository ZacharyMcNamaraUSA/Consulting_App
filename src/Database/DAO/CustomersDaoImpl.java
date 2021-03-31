package Database.DAO;

import Database.DBConnection;
import Database.DBQuery;
import Database.Entities.Customers;
import Utilities.TimeUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CustomersDaoImpl {

   private ArrayList<Customers> allCustomers = new ArrayList<>();
   private Connection conn = DBConnection.getConnection();

   /**
    * Default constructor that populates the list of all Customers
    */
   public CustomersDaoImpl() {
      readAllCustomers();
   }


   /**
    * Method returns the ArrayList containing all Customers.
    * @return Returns the ArrayList containing all Customers.
    */
   public ArrayList<Customers> getAllCustomers() {
      return allCustomers;
   }

   public Customers getSingleCustomers(int customersId) {
      for (Customers tCustomers: allCustomers) {
         if (tCustomers.getCustomerId() == customersId)
            return tCustomers;
      }

      return null;
   }

   public boolean deleteCustomer(Customers deleteCustomer){
      String deleteStatement = "DELETE FROM customers WHERE Customer_ID=" + deleteCustomer.getCustomerId();
      DBQuery.setPreparedStatement(deleteStatement);
      PreparedStatement myPrepDelete = DBQuery.getPreparedStatement();

      try {
         return myPrepDelete.execute();
      } catch (Exception e) {
         e.getStackTrace();
      }

      return false;
   }

   /**
    * Method receives customers ID then updates all applicable fields.
    * @param currentCustomerID The int Customer_ID which designates the Customers to update.
    * @param newCustomer The Customers object containing all info sent to database.
    */
   public void updateCustomer(int currentCustomerID, Customers newCustomer){

   }

   /**
    *  Method uses DBConnection to access database, read all customers info, and store the info in allCustomers
    */
   private void readAllCustomers() {
//      int customerId = 0;
//      String customerName = "";
//      String address = "";
//      String postalCode = "";
//      String phoneNumber = "";
//      LocalDateTime createDate = null;
//      String createdBy = "";
//      Instant lastUpdate = null;
//      String lastUpdatedBy = "";

      String selectStatement = "SELECT * FROM customers";
      DBQuery.setPreparedStatement(selectStatement);
      PreparedStatement myPrepStatement = DBQuery.getPreparedStatement();

      try {
         ResultSet rs = myPrepStatement.executeQuery();
         while (rs.next()) {
            allCustomers.add(new Customers(rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    (LocalDateTime.parse(rs.getString("Create_Date"))).atZone(TimeUtilities.zoneIdUTC),
                    rs.getString("Created_By"),
                    rs.getObject("Last_Update", Instant.class),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Division_ID")));
         }
      } catch (Exception e) {
         e.getStackTrace();
      }

   }
}
