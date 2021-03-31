package Database.Entities;

import javafx.beans.property.Property;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;

public class Appointment {
   private int appointmentId;
   private int customerId;
   private int userId;
   private String title;
   private String description;
   private String location;
//   private String contact;
   private String type;
   private String url;
   private ZonedDateTime start;
   private ZonedDateTime end;
   private ZonedDateTime createDate;
   private String createdBy;
   private Instant lastUpdate;
   private String lastUpdatedBy;
   private int contactId;

   public static final int businessHoursStart = 8;
   public static final int businessHoursEnd = 22;

   // TODO
   public Appointment(int appointmentId, String title, String description, String location, String type,
                      ZonedDateTime start, ZonedDateTime end, ZonedDateTime createDate, String createdBy,
                      Instant lastUpdate, String lastUpdatedBy, int customerId, int userId, int contactId) {
      this.appointmentId = appointmentId;
      this.title = title;
      this.description = description;
      this.location = location;
      this.type = type;
      this.start = start;
      this.end = end;
      this.createDate = createDate;
      this.createdBy = createdBy;
      this.lastUpdate = lastUpdate;
      this.lastUpdatedBy = lastUpdatedBy;
      this.customerId = customerId;
      this.userId = userId;
      this.contactId = contactId;
   }


   public int getCustomerId() {
      return customerId;
   }

   public void setCustomerId(int customerId) {
      this.customerId = customerId;
   }

   public Property customerIdProperty() { return null; }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public int getAppointmentId() {
      return appointmentId;
   }

   public void setAppointmentId(int appointmentId) {
      this.appointmentId = appointmentId;
   }

   public ZonedDateTime getCreateDate() {
      return createDate;
   }

   public void setCreateDate(ZonedDateTime createDate) {
      this.createDate = createDate;
   }

   public String getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   public Instant getLastUpdate() {
      return lastUpdate;
   }

   public void setLastUpdate(Instant lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

   public String getLastUpdatedBy() {
      return lastUpdatedBy;
   }

   public void setLastUpdatedBy(String lastUpdatedBy) {
      this.lastUpdatedBy = lastUpdatedBy;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public int getUserId() {
      return userId;
   }

   public void setUserId(int userId) {
      this.userId = userId;
   }

//   public String getContact() {
//      return contact;
//   }
//
//   public void setContact(String contact) {
//      this.contact = contact;
//   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getLocation() {
      return location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public ZonedDateTime getStart() {
      return start;
   }

   public void setStart(ZonedDateTime start) {
      this.start = start;
   }

   public ZonedDateTime getEnd() {
      return end;
   }

   public void setEnd(ZonedDateTime end) {
      this.end = end;
   }

   /**
    * Method is the getter for Contact_ID from an Appointment Object.
    * @return The contact's ID as an int.
    */
   public int getContactId() {
      return contactId;
   }

   /**
    * Method is the setter for Contact_ID from an Appointment Object.
    * @param contactId The new int to become the new Contact_ID.
    */
   public void setContactId(int contactId) {
      this.contactId = contactId;
   }
}
