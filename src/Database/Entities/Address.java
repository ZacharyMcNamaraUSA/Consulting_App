package Database.Entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Address {

   private String lastUpdatedBy;
   private Timestamp lastUpdate;
   private String createdBy;
   private LocalDateTime createDate;
   private int countryId;
   private String city;
   private int cityId;

   public LocalDateTime getCreateDate() {
      return createDate;
   }
   public void setCreateDate(LocalDateTime createDate) {
      this.createDate = createDate;
   }

   public String getCreatedBy() {
      return createdBy;
   }
   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   public Timestamp getLastUpdate() {
      return lastUpdate;
   }
   public void setLastUpdate(Timestamp lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

   public String getLastUpdatedBy() {
      return lastUpdatedBy;
   }
   public void setLastUpdatedBy(String lastUpdatedBy) {
      this.lastUpdatedBy = lastUpdatedBy;
   }

   public int getCountryId() {
      return countryId;
   }

   public void setCountryId(int countryId) {
      this.countryId = countryId;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public int getCityId() {
      return cityId;
   }

   public void setCityId(int cityId) {
      this.cityId = cityId;
   }
}
