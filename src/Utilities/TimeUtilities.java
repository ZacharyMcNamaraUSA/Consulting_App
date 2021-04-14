package Utilities;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeUtilities {

   public static final ZoneId userZoneId = ZoneId.systemDefault();
   public static final ZoneId homeOfficeZoneId = ZoneId.of("America/New_York");
   public static String databaseDateTimeFormat = "uuuu-MM-dd HH:mm:ss";
   public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(databaseDateTimeFormat);
   public static final DateTimeFormatter timeFormatterLong = DateTimeFormatter.ofPattern("HH:mm:ss");
   public static final DateTimeFormatter timeFormatterShort = DateTimeFormatter.ofPattern("HH:mm");
   
   private static final LocalTime businessHoursStart =  LocalTime.of(8, 0);
   private static final LocalTime businessHoursEnd =  LocalTime.of(22, 0);
   
   private static final ZonedDateTime businessOpenZonedDateTime = ZonedDateTime.of(
             LocalDate.now(homeOfficeZoneId),
             businessHoursStart,
             homeOfficeZoneId);
   private static final ZonedDateTime businessCloseZonedDateTime = ZonedDateTime.of(
             LocalDate.now(homeOfficeZoneId),
             businessHoursEnd,
             homeOfficeZoneId);
   
   public static final Instant businessOpen = businessOpenZonedDateTime.toInstant();
   public static final Instant businessClosed = businessCloseZonedDateTime.toInstant();
   
   
   /** Method tests if input ZonedDateTime is during business hours.
    * @param instant Input Instant to be tested against business hours.
    * @return Returns true if the input is between business hours or false if not.
    */
   public static boolean isDuringBusinessHours(Instant instant) {
      
      return instant.isBefore(businessClosed) && businessOpen.isBefore(instant);
      
   }
   

   /**
    * Method receives a LocalDateTime and converts it to a ZonedDateTime with ZoneId.systemDefault()
    * @param dateTimeBegin LocalDateTime to convert
    * @return The ZonedDateTime calculated from system defaults.
    */
   public static ZonedDateTime convertDatabaseDTtoZonedDT(LocalDateTime dateTimeBegin) {
      ZonedDateTime zonedDateTime = dateTimeBegin.atZone(ZoneOffset.UTC);
      zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());

      return zonedDateTime;
   }

   /**
    * Method receives a ZonedDatetime zdt and returns the data as a String for the use of SQL PreparedStatements.
    * @param zdt The ZonedDateTime is the required input to be printed as a String.
    * @return Returns a String which prints the input ZonedDateTime.
    */
   public static String formatZDTAsDatabaseString(ZonedDateTime zdt) {

      LocalDateTime ldt = LocalDateTime.ofInstant(zdt.toInstant(), ZoneOffset.UTC);
      String databaseString = ldt.format(DateTimeFormatter.ofPattern(databaseDateTimeFormat));

      return databaseString;
   }
   

   public static ZonedDateTime parseStringToZonedDateTime(String dateTime) {
      LocalDateTime ldt = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(databaseDateTimeFormat));
      ZonedDateTime zonedDateTime = ldt.atZone(ZoneId.of("Z"));
      ZonedDateTime adjustedZonedDT =  zonedDateTime.withZoneSameInstant(userZoneId);
      return adjustedZonedDT;
   }


   /**
    * This method receives a ZonedDateTime which is returned as a formatted String.
    * @param zdt The ZonedDateTime to be formatted.
    * @return Returns a String of the formatted ZonedDateTime.
    */
   public static String formatZDTToString(ZonedDateTime zdt) {
      return zdt.format(dateTimeFormatter);
   }
   

}
