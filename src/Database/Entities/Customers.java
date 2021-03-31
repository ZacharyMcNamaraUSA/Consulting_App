package Database.Entities;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Customers {

   private String lastUpdatedBy;
   private Instant lastUpdate;
   private String createdBy;
   private ZonedDateTime createDate;
   private String address;
   private String customerName;
   private int customerId;
   private int divisionId;
   private String postalCode;
   private String phoneNumber;

   /** Default Constructor for Customers.class
    * @param customerName
    * @param address
    * @param postalCode
    * @param phoneNumber
    * @param divisionId
    */
   public Customers(int customerId, String customerName, String address, String postalCode, String phoneNumber,
                    ZonedDateTime createDate, String createdBy, Instant lastUpdate, String lastUpdatedBy, int divisionId) {
      this.customerId = customerId;
      this.divisionId = divisionId;
      this.customerName = customerName;
      this.address = address;
      this.postalCode = postalCode;
      this.phoneNumber = phoneNumber;
      this.createDate = createDate;
      this.createdBy = createdBy;
      this.lastUpdate = lastUpdate;
      this.lastUpdatedBy = lastUpdatedBy;
   }

   /** Getter for customers column Customer_ID
    * @return Customer_ID as int
    */
   public int getCustomerId() {
      return customerId;
   }

   /** Setter for customers column Customer_ID
    * @param customerId int Customer_ID
    */
   public void setCustomerId(int customerId) {
      this.customerId = customerId;
   }

   /** Getter for the customers column Customer_Name
    * @return String of Customer_Name
    */
   public String getCustomerName() {
      return customerName;
   }

   /** Setter for customers column Customer_Name
    * @param customerName String to be set as Customer_Name
    */
   public void setCustomerName(String customerName) {
      this.customerName = customerName;
   }

   /** Getter for customers column Address
    * @return String address
    */
   public String getAddress() {
      return address;
   }

   /** Setter for customers Address
    * @param address String to be set as Address
    */
   public void setAddress(String address) {
      this.address = address;
   }

   /** Getter for customers Create_Date
    * @return ZonedDateTime is converted from database's DateTime - java equivalent is LocalDateTime
    */
   public ZonedDateTime getCreateDate() {
      return createDate;
   }

   /** Setter for customers Create_Date
    * @param createDate Requires a ZonedDateTime to replace the originally converted ZoneDateTime
    */
   public void setCreateDate(ZonedDateTime createDate) {
      this.createDate = createDate;
   }

   /** Getter for customers Created_By column
    * @return Returns String of user's name that created this record
    */
   public String getCreatedBy() {
      return createdBy;
   }

   /** Setter for customers Created_By column
    * @param createdBy Requires input string of the creating user's name
    */
   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   /** Getter for customers column Last_Update
    * @return Returns an Instant when the last update occured.
    */
   public Instant getLastUpdate() {
      return lastUpdate;
   }

   /** Setter for customers column Last_Update
    * @param lastUpdate Requires input of a new Instant to replace previous.
    */
   public void setLastUpdate(Instant lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

   /** Getter for customers column Last_Updated_By
    * @return Returns a string of the user's name to perform the last update on this record
    */
   public String getLastUpdatedBy() {
      return lastUpdatedBy;
   }

   /** Setter for Last_Update_By in customers
    * @param lastUpdatedBy Required input is String to be new user's name to perform last update
    */
   public void setLastUpdatedBy(String lastUpdatedBy) {
      this.lastUpdatedBy = lastUpdatedBy;
   }

   /** Getter for customers column Division_ID
    * @return Returns an int of the Division_ID
    */
   public int getDivisionId() {
      return divisionId;
   }

   /** Setter for customers Division_ID
    * @param divisionId Require int input of new Division_ID
    */
   public void setDivisionId(int divisionId) {
      this.divisionId = divisionId;
   }


   /** Getter for customers Postal_Code
    * @return Returns a String of the customer's Postal_Code
    */
   public String getPostalCode() {
      return postalCode;
   }

   /** Setter for customers Postal_Code
    * @param postalCode Input parameter must be a String to be new Postal_Code
    */
   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }

   /** Getter for customers Phone (number)
    * @return Returns a customers Phone (number) as a String
    */
   public String getPhoneNumber() {
      return phoneNumber;
   }

   /** Setter for customers Phone (number)
    * @param phoneNumber Required input parameter String to be new Phone (number)
    */
   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }
}
