package Database.Entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Users {
   private int userId;
   private String userName;
   private String password;
//   private String active;
   private Timestamp createDate;
   private String createdBy;
   private Timestamp lastUpdate;
   private String lastUpdatedBy;

   public Users(int userId, String userName, String password, Timestamp createDate,
                String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
      this.userId = userId;
      this.userName = userName;
      this.password = password;
//      this.active = active;
      this.createDate = createDate;
      this.createdBy= createdBy;
      this.lastUpdate = lastUpdate;
      this.lastUpdatedBy = lastUpdatedBy;
   }

   public int getUserId() {
      return userId;
   }

   public void setUserId(int userId) {
      this.userId = userId;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public Timestamp getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Timestamp createDate) {
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

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

//   public String getActive() {
//      return active;
//   }
//
//   public void setActive(String active) {
//      this.active = active;
//   }
}
