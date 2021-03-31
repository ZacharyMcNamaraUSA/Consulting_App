package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Database.DBConnection;

public class DBQuery {

   public static PreparedStatement statement;
   private static Connection conn = DBConnection.getConnection();

   /**
    * Method overloads setPreparedStatement with default Connection from DBConnection.
    * @param sqlStatement The SQL Statement to be executed.
    */
   public static void setPreparedStatement(String sqlStatement) {
      try {
         statement = conn.prepareStatement(sqlStatement);
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }


   /**
    * Method overloads setPreparedStatement with option to send a Connection.
    * @param conn The Connection to be used with the sqlStatement
    * @param sqlStatement The SQL Statement to be executed.
    */
   public static void setPreparedStatement(Connection conn, String sqlStatement) {
      try {
         statement = conn.prepareStatement(sqlStatement);
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }


   /**
    * Method returns the created PreparedStatement in a static context.
    * @return The created PreparedStatement.
    */
   public static PreparedStatement getPreparedStatement() {
      return statement;
   }

}
