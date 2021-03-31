package Database.DAO;

import Database.DBConnection;
import Database.DBQuery;
import Database.Entities.Users;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UsersDaoImpl implements UsersDao {

   Connection conn = DBConnection.getConnection();
   private ArrayList<Users> allUsers = new ArrayList<>();

   /**
    * Default UsersDaoImpl Constructor which calls getAllUsers().
    */
   @Override
   public void UsersDao() {
      getAllUsers();
   }

   /**
    * Creates a List of all current Users
    * @return the List of current Users
    */
   public ArrayList<Users> getAllUsers() {
      // declare and initialize variables
      int userId = 1;
      String userName = "Johnny";
      String password = "Johnny12";
      Timestamp createDate = new Timestamp(System.currentTimeMillis());
      String createdBy = "administration";
      Timestamp lastUpdate = new Timestamp(System.currentTimeMillis());
      String lastUpdatedBy = "administration";

      try {
         String selectUsersStatement = "SELECT * FROM users";
         DBQuery.setPreparedStatement(conn, selectUsersStatement);
         PreparedStatement myPrepStatement = DBQuery.getPreparedStatement();
         myPrepStatement.execute();

         ResultSet rs = myPrepStatement.getResultSet();

         while (rs.next()) {
            userId = rs.getInt("User_Id");
            userName = rs.getString("User_Name");
            password = rs.getString("Password");
            createDate = getTimestamp(rs.getTimestamp("Create_Date"));
            createdBy = rs.getString("Created_By");
            lastUpdate = (Timestamp) getTimestamp(rs.getTimestamp("Last_Update"));
            lastUpdatedBy = rs.getString("Last_Updated_By");

//            // print for testing purposes
//            System.out.println("\nuserId" + userId);
//            System.out.println("userName " + userName);
//            System.out.println("password " + password);
//            System.out.println("createdate " + createDate);
//            System.out.println("CreatedBy " + createdBy);
//            System.out.println("lasupdate " + lastUpdate);
//            System.out.println("lastupdateBY" + lastUpdatedBy);

            allUsers.add(new Users((userId), userName, password, createDate,
                    createdBy, lastUpdate, lastUpdatedBy));

         }

      } catch (Exception e) {
         System.out.println("\terror: " + e.getMessage());
         e.getStackTrace();
      }


      return allUsers;
   }

   /**
    * Method returns designated Users or null.
    * @param usersId Input parameter to select Users.
    * @return Returns designated Users or null if not found.
    */
   @Override
   public Users getSingleUsers(int usersId) {

      for (Users u: allUsers) {
         if (u.getUserId() == usersId)
            return u;
      }

      return null;
   }

   /**
    * Method returns designated Users or null.
    * @param usersName Input parameter to select a Users
    * @return Designated Users or null if not found.
    */
   @Override
   public Users getSingleUsers(String usersName) {
      for (Users u: allUsers) {
         if (u.getUserName() == usersName)
            return u;
      }

      return null;
   }


   /**
    * Attempts to update the given Users User_Name in the database.
    * @param users the Users to be updated in the database.
    */
   public void updateUsersName(@NotNull Users users, String newName, String currentUsersName) {
      int usersId = users.getUserId();
      try {

         String updateStatement = "UPDATE users SET User_Name=" + newName + " WHERE User_ID="+usersId;
         DBQuery.setPreparedStatement(conn, updateStatement);
         PreparedStatement updatePrepStatement = DBQuery.getPreparedStatement();
         updatePrepStatement.execute();

      } catch (Exception e) {
         e.getStackTrace();
      }
   }

   /**
    * Attempts to update the given Users User_Name in the database.
    * @param users the Users to be updated in the database.
    */
   public void updateUsersPassword(@NotNull Users users, String newPassword, String currentUsersName) {
      int usersId = users.getUserId();

      try {

         String updateStatement = "UPDATE users SET Password="+ newPassword + " WHERE User_ID="+usersId;
         DBQuery.setPreparedStatement(conn, updateStatement);
         PreparedStatement updatePrepStatement = DBQuery.getPreparedStatement();
         updatePrepStatement.execute();

      } catch (Exception e) {
         e.getStackTrace();
      }
   }


   /**
    * Attempts to delete the given Users from the database and returns a boolean.
    * @param users the Users to be deleted from database.
    * @return Returns true if the Users is deleted, false otherwise.
    */
   public boolean deleteUsers(@NotNull Users users, String currentUsersName) {
      try {
         String updateStatement = "DELETE FROM users WHERE User_ID="+users.getUserId()  ;
         DBQuery.setPreparedStatement(conn, updateStatement);
         PreparedStatement updatePrepStatement = DBQuery.getPreparedStatement();
         updatePrepStatement.execute();

      } catch (Exception e) {
         e.getStackTrace();
      }
      return false;
   }


   /**
    * Attempts to add the provided users to the database.
    * @param usersName Provided User_Name to be added to database.
    * @param password Provided password for new User
    */
   public void addUsers(@NotNull String usersName, String password, String currentUsersName) {

      String insertPrepStatement = "INSERT INTO users (User_Name, Password, Create_Date, Created_By" +
                     "Last_Update, Last_Updated_By) VALUES (?, ?, ?, ?, ?, ?)";
      String formattedCurrentDateTime = Instant.now().toString();

      try {
         DBQuery.setPreparedStatement(conn, insertPrepStatement);
         PreparedStatement usersInsertPrepStatement = DBQuery.getPreparedStatement();
//         usersInsertPrepStatement.setString(1, Integer.toString(userId));            // User_ID int(10) AI PK
         usersInsertPrepStatement.setString(1, usersName);                 // User_Name varchar
         usersInsertPrepStatement.setString(2, password);                  // Password text
         usersInsertPrepStatement.setString(3, formattedCurrentDateTime);  // Create_Date datetime
         usersInsertPrepStatement.setString(4, currentUsersName);          // Created_By varchar
         usersInsertPrepStatement.setString(5, formattedCurrentDateTime);  // Last_Update timestamp
         usersInsertPrepStatement.setString(6, currentUsersName);          // Last_Update_By varchar
         usersInsertPrepStatement.execute();

      } catch (Exception e) {
         e.getStackTrace();
      }


   } //addUsers method

   /**
    * Method takes in Date object then converts and returns the data as a java.sql.Timestamp
    * @param date Input date to be converted
    * @return The date as a java.sql.Timestamp
    */
   public Timestamp getTimestamp(java.util.Date date) {
      return date == null ? null : new java.sql.Timestamp(date.getTime());
   }
}
