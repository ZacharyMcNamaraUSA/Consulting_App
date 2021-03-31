package Database.DAO;

import Database.DBConnection;
import Database.DBQuery;
import Database.Entities.Countries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class CountriesDaoImpl implements CountriesDao {

   private ArrayList<Countries> allCountriesList = new ArrayList<Countries>();
   private Connection conn = DBConnection.getConnection();

   /** Default constructor of CountriesDaoImpl
    *  Constructor calls retrieveAllCountries() to populate the list
    */
   public CountriesDaoImpl() {
      readAllCountries();
   }

   /** Populates the allCountriesList from data in the database.
    *
    */
   private void readAllCountries() {
      try {
         String selectAllCountries = "Select * FROM countries";
         DBQuery.setPreparedStatement(conn, selectAllCountries);
         PreparedStatement myPrepStatement = DBQuery.getPreparedStatement();
         myPrepStatement.execute();
         ResultSet rs = myPrepStatement.getResultSet();

         while (rs.next()) {
            allCountriesList.add(new Countries(rs.getInt("Country_ID"), rs.getString("Country"),
                    rs.getObject("Create_Date", LocalDateTime.class), rs.getString("Created_By"),
                    rs.getObject("Last_Update", Instant.class),
                    rs.getString("Last_Updated_By")));
         }

      } catch (SQLException e) {
         System.out.println("SQL ERROR: " + e.getLocalizedMessage());
         e.getStackTrace();
      } catch (Exception e) {
         e.getStackTrace();
      }
   }

   /** Method returns an ArrayList with every country in the database.
    * @return The ArrayList with every country in the database.
    */
   @Override
   public ArrayList<Countries> getAllCountries() {
      return allCountriesList;
   }


   /** Method
    * @param countriesOldID The Country_ID int of the Countries to be updated in the database.
    * @param countriesNameNew The new name for the designated Countries.
    * @return Returns true if update is successful or false if it fails.
    */
   @Override
   public boolean updateCountries(int countriesOldID, String countriesNameNew) {
      String updateStatement = "UPDATE countries SET Country = '" + countriesNameNew + "', " +
              " WHERE Country_ID=" + countriesOldID;
      DBQuery.setPreparedStatement(conn, updateStatement);
      PreparedStatement updatePrepStatement = DBQuery.getPreparedStatement();
      try {
         ResultSet rs = updatePrepStatement.executeQuery();
         if (rs.next())
            return true;
      } catch (Exception e) {
         e.getStackTrace();
      }

      return false;
   }


   /**
    *  Method resets then repopulates the allCountriesList with retrieveAllCountries()
    */
   @Override
   public void updateCountriesList() {
      allCountriesList.clear();

      readAllCountries();
   }

   /** Method receives a Countries object to be added to the database table countries.
    * @param countriesToAdd The new Countries object to be added to countries.
    * @return Returns true on success, false on failure.
    */
   @Override
   public boolean addCountries(Countries countriesToAdd, String currentUsersName) {

      String insertStatement = "INSERT INTO countries (Country, Create_Date, Created_By" +
              "Last_Update, Last_Updated_By) VALUES (?, ?, ?, ?, ?)";
      String formattedCurrentDateTime = Instant.now().toString();
      DBQuery.setPreparedStatement(conn, insertStatement);
      PreparedStatement insertPrepStatement = DBQuery.getPreparedStatement();

      try {
//         usersInsertPrepStatement.setString(1, Integer.toString(userId));            // User_ID int(10) AI PK
         insertPrepStatement.setString(1, countriesToAdd.getCountryName());                 // Country varchar
         insertPrepStatement.setString(3, LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).
                 format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH-mm-ss")));  // Create_Date datetime
         insertPrepStatement.setString(4, currentUsersName);          // Created_By varchar
         insertPrepStatement.setString(5, (LocalDateTime.now().format(
                 DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss"))).toString());  // Last_Update timestamp
         insertPrepStatement.setString(6, currentUsersName);          // Last_Update_By varchar
         insertPrepStatement.execute();

         return true;
      } catch (Exception e) {
         e.getStackTrace();
      }


      return false;
   }

   /** Method attempts to remove a countries record from the database.
    * @param countriesToDelete The CountryName used to specify which countries to delete.
    * @return True if delete is successful or false if not.
    */
   @Override
   public boolean deleteCountries(Countries countriesToDelete) {
      String deleteStatement = "DELETE FROM countries WHERE Country_ID=" + countriesToDelete.getCountryId();
      DBQuery.setPreparedStatement(conn, deleteStatement);
      PreparedStatement deletePrepStatement = DBQuery.getPreparedStatement();

      try {
         deletePrepStatement.executeQuery();
         return true;
      } catch (Exception e) {
         e.getStackTrace();
      }

      return false;
   }
}
