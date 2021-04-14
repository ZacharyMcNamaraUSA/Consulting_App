package Database.DAO;

import Database.DBQuery;
import Database.Entities.Appointment;
import Utilities.TimeUtilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;

public class AppointmentDaoImpl implements AppointmentDao {
   public ArrayList<Appointment> allAppointments = new ArrayList<>();
   
   /**
    * Default constructor that retrieves all Appointment records.
    */
   public AppointmentDaoImpl() {
      readDatabaseAppointments();
   }
   
   /** Method returns an ArrayList<Appointment> of every currently stored Appointment.
    * @return Returns the ArrayList<Appointment> of every Appointment.
    */
   public ArrayList<Appointment> getAllAppointments() {
      return allAppointments;
      
   }
   
   /** Method creates a new Appointment record in the database.
    * @param title Input String to be the Appointment's Title.
    * @param description Input String to be the Appointment's Description.
    * @param location Input String to be the Appointment's Location.
    * @param type Input String to be the Appointment's Type.
    * @param startZDT Input ZonedDateTime to be the Appointment's Start.
    * @param endZDT Input ZonedDateTime to be the Appointment's End.
    * @param createZDT Input ZonedDateTime to be the Appointment's Create_Date.
    * @param createdByUserName Input String to be the Appointment's Created_By.
    * @param customerId Input int to be the Appointment's Customer_ID.
    * @param userId Input int to be the Appointment's User_ID.
    * @param contactId Input int to be the Appointment's Contact_ID.
    * @return Returns true if a new Appointment record is created, otherwise returns false.
    */
   public static boolean addAppointmentToDatabase(String title, String description, String location, String type,
                                                  ZonedDateTime startZDT, ZonedDateTime endZDT,
                                                  ZonedDateTime createZDT, String createdByUserName,
                                                  int customerId, int userId, int contactId) {
      String insertStatement = "INSERT INTO appointments (Title, Description, Location, Type, " +
              "Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      DBQuery.setPreparedStatement(insertStatement);
      PreparedStatement myPreppedInsert = DBQuery.getPreparedStatement();
      try {

         myPreppedInsert.setString(1, title);
         myPreppedInsert.setString(2, description);
         myPreppedInsert.setString(3, location);
         myPreppedInsert.setString(4, type);
         myPreppedInsert.setString(5, TimeUtilities.formatZDTAsDatabaseString(startZDT));
         myPreppedInsert.setString(6, TimeUtilities.formatZDTAsDatabaseString(endZDT));
         myPreppedInsert.setString(7, TimeUtilities.formatZDTAsDatabaseString(createZDT));
         myPreppedInsert.setString(8, createdByUserName);
         myPreppedInsert.setString(9, TimeUtilities.formatZDTAsDatabaseString(ZonedDateTime.now()));
         myPreppedInsert.setString(10, createdByUserName);
         myPreppedInsert.setString(11, java.lang.String.valueOf(customerId));
         myPreppedInsert.setString(12, java.lang.String.valueOf(userId));
         myPreppedInsert.setString(13, java.lang.String.valueOf(contactId));

         return myPreppedInsert.execute();
      } catch (Exception e) {
         e.getStackTrace();
      }

      return false;
   }
   
   /** Method updates an existing Appointment record in the database.
    * @param updateAppointment Input Appointment object that is already updated to the new values.
    * @throws SQLException if an error occurs during the database connections.
    */
   public static void updateAppointment(Appointment updateAppointment) throws SQLException {

      String updateStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, " +
              "End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, " +
              "User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

      DBQuery.setPreparedStatement(updateStatement);
      PreparedStatement myPrepUpdate = DBQuery.getPreparedStatement();

      // Title
      myPrepUpdate.setString(1, updateAppointment.getTitle());

      //Description
      myPrepUpdate.setString(2, updateAppointment.getDescription());

      // Location
      myPrepUpdate.setString(3, updateAppointment.getLocation());

      // Type
      myPrepUpdate.setString(4, updateAppointment.getType());

      // Start
      String startString = TimeUtilities.formatZDTAsDatabaseString(updateAppointment.getStartZonedDateTime());
      myPrepUpdate.setString(5, startString);

      // End
      String endString = TimeUtilities.formatZDTAsDatabaseString(updateAppointment.getEndZonedDateTime());
      myPrepUpdate.setString(6, endString);

      // Create_Date
      myPrepUpdate.setString(7,
              TimeUtilities.formatZDTAsDatabaseString(updateAppointment.getCreateDate()));

      // Created_By
      myPrepUpdate.setString(8, updateAppointment.getCreatedBy());

      // Last_Update
      myPrepUpdate.setString(9, TimeUtilities.formatZDTAsDatabaseString(ZonedDateTime.now()));

      // Last_Updated_By
      myPrepUpdate.setString(10, updateAppointment.getLastUpdatedBy());

      // Customer_ID
      myPrepUpdate.setString(11, String.valueOf(updateAppointment.getCustomerId()));

      // User_ID
      myPrepUpdate.setString(12, String.valueOf(updateAppointment.getUserId()));

      // Contact_ID
      myPrepUpdate.setString(13, String.valueOf(updateAppointment.getContactId()));

      // Appointment_ID
      myPrepUpdate.setString(14, String.valueOf(updateAppointment.getAppointmentID()));

      try {
         myPrepUpdate.executeUpdate();

      } catch (Exception e) {
         e.getStackTrace();
      }


   }
   
   
   /** Method attempts to delete an Appointment record.
    * @param apptToDelete Input Appointment object with the new values to update the record to.
    * @return Returns true if an Appointment record is deleted and false if not.
    */
   public static boolean deleteAppointment(Appointment apptToDelete) {
      String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID=" + apptToDelete.getAppointmentID();
      DBQuery.setPreparedStatement(deleteStatement);
      PreparedStatement myPreppedDelete = DBQuery.getPreparedStatement();
      
      try {
            myPreppedDelete.execute();
            return true;
      } catch (Exception e) {
         e.getStackTrace();
      }

      return false;
   }
   
   /**
    * Method reads all Appointment records from the database and stores them in an ArrayList<Appointment> named 'allAppointments'.
    */
   private void readDatabaseAppointments() {
      String selectStatement = "SELECT * FROM appointments";
      DBQuery.setPreparedStatement(selectStatement);
      PreparedStatement myPrepStatement = DBQuery.getPreparedStatement();

      int apptId;
      int custId;
      int contactId;
      int userId;
      String title;
      String descrip;
      String loc;
      String typ;
      String startString;
      String endString;
      String createDateString;
      String createdBy;
      String lastUpdateString;
      String lastUpdatedBy;

      try {
         ResultSet rs = myPrepStatement.executeQuery();
         while (rs.next()) {

            apptId = rs.getInt("Appointment_ID");
            title = rs.getString("Title");
            descrip = rs.getString("Description");
            loc = rs.getString("Location");
            typ = rs.getString("Type");

            startString = rs.getString("Start");
            ZonedDateTime startZDT = TimeUtilities.parseStringToZonedDateTime(startString);

            endString = rs.getString("End");
            ZonedDateTime endZDT = TimeUtilities.parseStringToZonedDateTime(endString);

            createDateString = rs.getString("Create_Date");
            ZonedDateTime createZDT = TimeUtilities.parseStringToZonedDateTime(createDateString);

            createdBy = rs.getString("Created_By");

            lastUpdateString = rs.getString("Last_Update");
            LocalDateTime lUpdate = LocalDateTime.parse(lastUpdateString, TimeUtilities.dateTimeFormatter);
            ZonedDateTime lastUpdateZDT = lUpdate.atZone(ZoneId.of("Z"));
            lastUpdateZDT = lastUpdateZDT.withZoneSameInstant(ZoneId.systemDefault());

            lastUpdatedBy = rs.getString("Last_Updated_By");
            custId = rs.getInt("Customer_ID");
            userId = rs.getInt("User_ID");
            contactId = rs.getInt("Contact_ID");

//   public Appointment(int appointmentId, String title, String description, String location, String type,
//                    ZonedDateTime start, ZonedDateTime end, ZonedDateTime createDate, String createdBy,
//                    Instant lastUpdate, String lastUpdatedBy, int customerId, int userId, int contactId) {

            Appointment nAppointment = new Appointment(apptId, title, descrip, loc, typ, startZDT, endZDT,
                    createZDT, createdBy, lastUpdateZDT, lastUpdatedBy, custId, userId, contactId);

            allAppointments.add(nAppointment);

         }
      } catch (Exception e) {
         e.getStackTrace();
      }
   }
}
