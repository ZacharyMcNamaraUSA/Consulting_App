package Utilities;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeUtilities {

   public static final ZoneId userZoneId = ZoneId.systemDefault();
   public static final ZoneId zoneIdUTC = ZoneId.of("UTC");
   Instant instantTest = Instant.now();
   ZoneId zoneIdAmericaChicago = ZoneId.of("America/Chicago");
   ZoneId zoneIdAmericaNewYork = ZoneId.of("America/NewYork");
   ZoneId zoneIdEuropeLondon = ZoneId.of("Europe/London");
   ZoneId zoneIdAmericaPhoenix = ZoneId.of("America/Phoenix");
   ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instantTest, zoneIdAmericaChicago);


   /**
    * Method receives a LocalDateTime and converts it to a ZonedDateTime with ZoneId.systemDefault()
    * @param dateTimeBegin LocalDateTime to convert
    * @return The ZonedDateTime calculated from system defaults.
    */
   public static ZonedDateTime localDTtoZonedDT(LocalDateTime dateTimeBegin) {
      ZonedDateTime zonedDateTime = dateTimeBegin.atZone(userZoneId);

      return zonedDateTime;
   }


   /**
    * Method receives a LocalDateTime to be converted to an Instant
    * @param ldt The input LocalDateTime Object
    */
   public static Instant localDTtoInstant(LocalDateTime ldt) {
      ZonedDateTime zdt =  ldt.atZone(userZoneId);
      return zdt.toInstant();
   }

   /**
    * Method receives a ZonedDatetime zdt and returns the data as a String for the use of SQL PreparedStatements.
    * @param zdt The ZonedDateTime is the required input to be printed as a String.
    * @return Returns a String which prints the input ZonedDateTime.
    */
   public static String convertZDTtoLDTString(ZonedDateTime zdt) {

      LocalDateTime ldt = LocalDateTime.ofInstant(zdt.toInstant(), ZoneOffset.UTC);
      String databaseString = ldt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));

      return databaseString;
   }






   /**
    * Converts Date to Timestamp
    * @param date Date to be converted to Timestamp, if not null.
    * @return
    */
   public Timestamp convertDateToTimestamp(java.util.Date date) {
      return date == null ? null : new java.sql.Timestamp(date.getTime());
   } //getTimestamp


   /**
    * Converts Instant to Timestamp
    * @param instantParam The Instant to be converted to Timestamp, if not null.
    * @return Retruns the converted SQL Timestamp.
    */
   public static Timestamp convertDateToTimestamp(Instant instantParam) {
//      return (instantParam == null) ? null : new Timestamp.valueOf(instantParam);
      return null;
   } //getTimestamp
}
