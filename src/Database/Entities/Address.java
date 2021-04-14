package Database.Entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Address {

   private String lastUpdatedBy;
   private ZonedDateTime lastUpdate;
   private String createdBy;
   private ZonedDateTime createDate;
   private int countryId;
   private String city;
   private int cityId;
   
   /** Getter for ZonedDateTime createDate.
    * @return the ZonedDateTime of this record's initialization.
    */
   public ZonedDateTime getCreateDate() {
      return createDate;
   }
   
   /** Setter for ZonedDateTime createDate.
    * @param createDate Input ZonedDateTime to be new value.
    */
   public void setCreateDate(ZonedDateTime createDate) {
      this.createDate = createDate;
   }
   
   /** Getter for createdBy
    * @return String value of the username that initialized this record.
    */
   public String getCreatedBy() {
      return createdBy;
   }
   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   public ZonedDateTime getLastUpdate() {
      return lastUpdate;
   }
   public void setLastUpdate(ZonedDateTime lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

   public String getLastUpdatedBy() {
      return lastUpdatedBy;
   }
   public void setLastUpdatedBy(String lastUpdatedBy) {
      this.lastUpdatedBy = lastUpdatedBy;
   }
   
   /** Getter for int countryId.
    * @return Returns the countryId as an int.
    */
   public int getCountryId() {
      return countryId;
   }
   
   /** Setter for int countryId.
    * @param countryId Input int to be new countryId value.
    */
   public void setCountryId(int countryId) {
      this.countryId = countryId;
   }
   
   /** Getter for String city.
    * @return Returns String containing the city.
    */
   public String getCity() {
      return city;
   }
   
   /** Setter for the String city
    * @param city String input to be new city value.
    */
   public void setCity(String city) {
      this.city = city;
   }
   
   /** Getter for cityId value.
    * @return Returns the cityId value as an int.
    */
   public int getCityId() {
      return cityId;
   }
   
   /** Setter for cityId value.
    * @param cityId Input int to be new cityId value.
    */
   public void setCityId(int cityId) {
      this.cityId = cityId;
   }
}
