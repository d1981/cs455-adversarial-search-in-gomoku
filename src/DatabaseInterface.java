import java.sql.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

class DatabaseInterface{
   Connection c = null;
  
   /* Contructor for database interface
   *  Creates a sqlite interface for use by its 
   *  other methods */
   public DatabaseInterface(){
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:qlearner.db");
     
         // create Q table if they dont exist         
         // need to add unique constraints for action_x, action_y and state string

         Statement stmt = null;
         Statement stmt2 = null;
      
         stmt = c.createStatement();
         String sql = "CREATE TABLE if not exists qtable" +
                   "(id INTEGER PRIMARY KEY NOT NULL ," +
                   "state_string CHAR(128),"+
                   "action_x INT NOT NULL," +
                   "action_y INT NOT NULL," +
                   "value FLOAT NOT NULL," +
                   "UNIQUE(state_string,action_x,action_y) ON CONFLICT IGNORE)";
         stmt.executeUpdate(sql);
         stmt.close();
      
         // create frequency table
         // need to add unique constraints for action_x, action_y and state string
         stmt2 = c.createStatement();
         String sql2 = "CREATE TABLE if not exists freq" +
                   "(id INTEGER PRIMARY KEY NOT NULL ," +
                   "state_string CHAR(128),"+
                   "action_x INT NOT NULL," +
                   "action_y INT NOT NULL," +
                   "COUNT INT NOT NULL," +
                   "UNIQUE(state_string,action_x,action_y) ON CONFLICT IGNORE)";
         stmt2.executeUpdate(sql2);
         stmt2.close();

         } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
         }
         System.out.println("Opened database successfully");
      } // end Database constructor
      
      /*
      *  Retrieve the state from the Qtable
      */
   public ArrayList SelectStateFromQTable(String state){
      ArrayList result = new ArrayList<Object>();
      String sql = String.format("SELECT * FROM qtable WHERE state_string = %s", state);
      try{
         PreparedStatement pstmt = c.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();
         while (rs.next()) {
            System.out.println(rs.getInt("id") +  "\t" + 
            rs.getString("state_string") + "\t" +
            rs.getFloat("value"));
         }   
         result.add(rs.getString("state_string")); 
         result.add(rs.getFloat("value"));
      }
      catch (SQLException e){
         System.out.println(e.getMessage());
      }
   return result;
   } // end SelectStateFromQTable
      
   /* Increments the frequency table given the unqiue
   *  combination of state and action (x,y)
   *  If row doesn't exist yet, it creates it
   */
   public void incrementStateFreq(Connection c, String state, int x, int y){
      // Check if state action pair exists
      String sql = "SELECT id, state_string, action_x, action_y, count FROM freq WHERE state_string = ? AND action_x = ? AND action_y = ?";
      ArrayList instance = new ArrayList<Object>();
      
      try{
        PreparedStatement pstmt = c.prepareStatement(sql);
        pstmt.setString(1, state);
        pstmt.setInt(2, x);
        pstmt.setInt(3, y);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
           instance.add(rs.getString("state_string")); 
           instance.add(rs.getInt("count"));
           instance.add(rs.getInt("action_x"));
           instance.add(rs.getInt("action_y"));
        }
        
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      
      // If not, create it and set it to 1
      if (instance.size() == 0){
         String result = incrementStateFreq(c, (String)instance.get(1), (int)instance.get(3), (int)instance.get(4), 1);
      }
      // Else, increment the value and update the row
      try{
         String sqlupdate = "UPDATE freq SET count = ? " +
                            "WHERE state_string = ? AND action_x = ? AND action_y = ?";  
                   
         PreparedStatement pstmtupdate = c.prepareStatement(sqlupdate);
         pstmtupdate.setInt(1, (int)instance.get(1)+1);
         pstmtupdate.setString(2, state);
         pstmtupdate.setInt(3, x);
         pstmtupdate.setInt(4, y);
         pstmtupdate.executeUpdate();
      }
      catch (SQLException e){
         System.out.println(e.getMessage());
      }
   }
}