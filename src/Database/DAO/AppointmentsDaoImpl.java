package Database.DAO;

import Database.DBQuery;
import Database.Entities.Appointment;
import Utilities.TimeUtilities;
import View_Controller.MainMenuController;

import javax.swing.text.Utilities;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AppointmentsDaoImpl implements AppointmentsDao {
   private ArrayList<Appointment> allAppointments = null;

   public AppointmentsDaoImpl() {
      readDatabaseAppointments();
   }

   public ArrayList<Appointment> getAllAppointments() {
      return allAppointments;
      
   }

   public boolean addAppointmentToDatabase(Appointment appointment) {
      String insertStatement = "INSERT INTO appointments (Title, Description, Location, Type, " +
              "Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID" +
              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      DBQuery.setPreparedStatement(insertStatement);
      PreparedStatement myPreppedInsert = DBQuery.getPreparedStatement();
      try {

         myPreppedInsert.setString(1, appointment.getTitle());
         myPreppedInsert.setString(2, appointment.getDescription());
         myPreppedInsert.setString(3, appointment.getLocation());
         myPreppedInsert.setString(4, appointment.getType());
         myPreppedInsert.setString(5, TimeUtilities.convertZDTtoLDTString((appointment.getStart())));
         myPreppedInsert.setString(6, TimeUtilities.convertZDTtoLDTString(appointment.getEnd()));
         myPreppedInsert.setString(7, TimeUtilities.convertZDTtoLDTString(appointment.getCreateDate()));
         myPreppedInsert.setString(8, appointment.getCreatedBy());
         myPreppedInsert.setString(9, TimeUtilities.convertZDTtoLDTString(ZonedDateTime.now()));
         myPreppedInsert.setString(10, MainMenuController.activeUsers.getUserName());
         myPreppedInsert.setString(11, Integer.toString(appointment.getCustomerId()));
         myPreppedInsert.setString(12, Integer.toString(appointment.getUserId()));
         myPreppedInsert.setInt(13, appointment.getContactId());

         return myPreppedInsert.execute();
      } catch (Exception e) {
         e.getStackTrace();
      }

      return false;
   }

   public void updateAppointment(int oldAppointmentId, Appointment newAppointment) {
      String updateStatement = "UPDATE appointments SET Title = '" + newAppointment.getTitle() + "', " +
              "Description = '" + newAppointment.getDescription() + "', " +
              "Location = '" + newAppointment.getLocation() + "', " +
              "Type = '" + newAppointment.getType() + "', " +
              "Start '" + TimeUtilities.convertZDTtoLDTString(newAppointment.getStart()) + "', " +
              "End '" + TimeUtilities.convertZDTtoLDTString(newAppointment.getEnd()) + "', " +
              "Last_Update '" + TimeUtilities.convertZDTtoLDTString(ZonedDateTime.now()) + "', " +
              "Last_Updated_By'" + MainMenuController.activeUsers.getUserName() + "', " +
              "Customer_ID '" + newAppointment.getCustomerId() + "', " +
              "User_ID '" + newAppointment.getUserId() + "', " +
              "Contact_ID '" + newAppointment.getContactId() +
              " WHERE Appointment_ID = "+ oldAppointmentId;

   }


   public boolean deleteAppointment(Appointment apptToDelete) {
      String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID=" + apptToDelete.getAppointmentId();
      DBQuery.setPreparedStatement(deleteStatement);
      PreparedStatement myPreppedDelete = DBQuery.getPreparedStatement();
      try {
         if (myPreppedDelete.execute())
            return true;
      } catch (Exception e) {
         e.getStackTrace();
      }

      return false;
   }

   void readDatabaseAppointments() {
      String selectStatement = "SELECT * FROM appointments";
      DBQuery.setPreparedStatement(selectStatement);
      PreparedStatement myPrepStatement = DBQuery.getPreparedStatement();

      try {
         ResultSet rs = myPrepStatement.executeQuery();
         while (rs.next()) {

            int apptId = 0;
            String title = "";
            String descrip = "";
            String loc = "";
            String typ = "";
            String startString = "";
            LocalDateTime startLDT = null;
            String endString = "";
            LocalDateTime endLDT = null;
            LocalDateTime createDate = (LocalDateTime.now());
            String createdBy = "admin";
            Instant lastUpdate = Instant.now();
            String lastUpdatedBy = "admin";

            allAppointments.add(new Appointment(rs.getInt("Appointment_ID"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    (LocalDateTime.parse(rs.getString("Start"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).atZone(TimeUtilities.zoneIdUTC),
                    (LocalDateTime.parse(rs.getString("End"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).atZone(TimeUtilities.zoneIdUTC),
                    (LocalDateTime.parse(rs.getString("Create_Date"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).atZone(TimeUtilities.zoneIdUTC),
                    rs.getString("Created_By"),
                    rs.getObject("Last_Update", Instant.class),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID")));
         }
      } catch (Exception e) {
         e.getStackTrace();
      }
   }
}
