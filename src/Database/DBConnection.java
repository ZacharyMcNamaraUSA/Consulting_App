package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Zachary McNamara WGU #001182706
 */

public class DBConnection {

   // JDBC URL parts
   private static final String protocol = "jdbc";
   private static final String vendorName = ":mysql:";
   private static final String ipAddress = "//wgudb.ucertify.com/WJ06nRO";
   private static final String username = "U06nRO";
   private static final String password = "53688818470";

   // JDBC URL
   private static final String jdbcUrl = protocol + vendorName + ipAddress;

   // Driver and Connection Interface reference
   private static final String MYSQLJDBCDRIVER = "com.mysql.cj.jdbc.Driver";
   private static  Connection conn = null;

   public static void initiateConnection() {
      try {
         Class.forName(MYSQLJDBCDRIVER);
         conn = (Connection) DriverManager.getConnection(jdbcUrl, username, password);
         System.out.println("Connected!");

         } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }

   }

   public static Connection getConnection() {
      return conn;
   }

   public static void endConnection() {
      try {
         conn.close();
         System.out.println("Closing connection.");
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }
}
