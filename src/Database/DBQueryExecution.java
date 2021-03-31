package Database;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBQueryExecution {

   Connection conn = DBConnection.getConnection();

   public DBQueryExecution(String statementSQL, int typeOfStatement ) {

      try {

         DBQuery.setPreparedStatement(conn, statementSQL);
         PreparedStatement updatePrepStatement = DBQuery.getPreparedStatement();
         updatePrepStatement.execute();

      } catch (Exception e) {
         e.getStackTrace();
      }
   }

}
