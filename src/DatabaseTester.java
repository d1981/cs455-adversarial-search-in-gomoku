import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

class DatabaseTester{
   

   static private Connection connect(){
      Connection c = null;
      try {        
         c = DriverManager.getConnection("jdbc:sqlite:qlearner.db");
      } 
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return c;
   }

   static public ArrayList GetStateFromQTable(Connection c, String state){
      String sql = String.format("SELECT * FROM qtable WHERE state_string = %s", state);
      ArrayList result = new ArrayList<Object>();
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
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
 
      return result;
   }
   
   static public ArrayList selectStateFromFreq(Connection c, String state, int x, int y){
     String sql = "SELECT id, state_string, action_x, action_y, count FROM freq WHERE state_string = ? AND action_x = ? AND action_y = ?";
     ArrayList result = new ArrayList<Object>();
     
     try{
        PreparedStatement pstmt = c.prepareStatement(sql);
        pstmt.setString(1, state);
        pstmt.setInt(2, x);
        pstmt.setInt(3, y);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
           ArrayList instance = new ArrayList<Object>();
           instance.add(rs.getString("state_string")); 
           instance.add(rs.getFloat("count"));
           instance.add(rs.getInt("action_x"));
           instance.add(rs.getInt("action_y"));
           result.add(instance);
        }
        
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return result;
   }
   
   static public void incrementStateFreq(Connection c, String state, int x, int y){
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
         String result = insertStateAndFreq(c, (String)instance.get(1), (int)instance.get(3), (int)instance.get(4), 1);
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
   
   static public String insertStateAndFreq(Connection c, String state, int action_x, int action_y, int count){
      String sql = "INSERT INTO freq(state_string, action_x, action_y, count) VALUES(?,?,?,?)";
      System.out.println(String.format("%d %d %d \n%s", action_x,action_y,count,state));
      try{
         PreparedStatement pstmt = c.prepareStatement(sql);
         pstmt.setString(1, state);
         pstmt.setInt(2, action_x);
         pstmt.setInt(3, action_y);
         pstmt.setInt(4, count);
         pstmt.executeUpdate();
      }
      catch (SQLException e){
         System.out.println(String.format("Error: %s", e.getMessage()));
      }
      
      return new String("Update successful");
   }
   
   public static void main(String[] args){
      try {
         Class.forName("org.sqlite.JDBC");
         
         Connection conn = connect();
         
         // try and get the existing state
         ArrayList result = selectStateFromFreq(conn, new String("     x\n     x\n"), 1, 1);
         if (result.size() == 1){
            // increment the frequency table because it exists
            incrementStateFreq(conn, new String("     x\n     x\n"), 1, 1);
         }

         //System.out.println(result);
         
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      
   
   }
 
}