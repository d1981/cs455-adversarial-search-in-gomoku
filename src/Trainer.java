import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 * class Trainer 
 * 
 * Trainer for learning the evaluation function 
 *
 *@author:  Josh Kapple
 *@date:    3-07-17
 *@version: Beta 0.1
 */
public class Trainer {
   static Connection c = null;
   
	private static final int RACKETPORT = 17033;         // uses port 1237 on localhost  
	
   /*
   *  Update the q table with our reward value
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
   }
 
   /*
   *  Check for, and increment the frequencies table
   */
   public void UpdateFreqTable(String state){
      
   }
    
   /* Setup a sqlite database for storing 
   *  our knowledge
   *
   */
   public static Connection setupDB(){
      Statement stmt = null;
      Statement stmt2 = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:qlearner.db");
         
         // create Q table if they dont exist
         
         // need to add unique constraints for action_x, action_y and state string
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
    return c;
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

	public static void main(String[] args){
		int turn = 0;
      
      Connection c = setupDB();      
      
		Random random  = new Random();
		Random random2 = new Random();

		GomokuDriver player1 = new GomokuDriver("localhost", RACKETPORT);      
		GomokuDriver player2 = new GomokuDriver("localhost", RACKETPORT);
      
		String result1 = "";
		String result2 = "";
		int randomNumber1 = 0;
      int randomNumber2 = 0;

		ArrayList data1;
      ArrayList data2;

      ArrayList moveHistory = new ArrayList<ArrayList<Object>>();
      
      // Loop and play both players moves
		while(true){
         // record the states and moves players made
         

         data1 = player1.getStatus();
         if (data1.get(1).equals("win") == true){
             System.out.println(String.format("Player 1: %s", data1.get(1)));
             
             // Set the terminal q state value
             setQState((String)data1.get(0), 100); 
             
             break;
	      }
         else if (data1.get(1).equals("lose") == true){
             System.out.println(String.format("Player 2: win"));
             break;
         }  
         
         // Didn't win or lose, game is still playable
         
         if (turn == 0){
           // First move of the game, just pick a random number 
           ArrayList instance = new ArrayList<Object>();
           randomNumber1 = random.nextInt(player1.getGridHeight()-1) + 0;
           randomNumber2 = random.nextInt(player1.getGridWidth()-1) + 0;;
           player1.rc.gridOut.println(String.format("%d %d", randomNumber1, randomNumber2));
           instance.add(data1.get(0));
           instance.add(new int[]{randomNumber1,randomNumber2});
           moveHistory.add(instance);
         }
			else{
           // Player 1 normal turn
           // 
           char[][] grid1 = (char[][])data1.get(0);
           while (true){        
              randomNumber1 = random.nextInt(player1.getGridHeight()-1) + 0;
              randomNumber2 = random.nextInt(player1.getGridWidth()-1) + 0;
              
              ArrayList instance = new ArrayList<Object>();
              System.out.println(grid1[randomNumber1][randomNumber2]);   
              if (grid1[randomNumber1][randomNumber2] == ' '){
                 System.out.println(String.format("%d %d", randomNumber1, randomNumber2));
                 player1.rc.gridOut.println(String.format("%d %d", randomNumber1, randomNumber2));
                 
                 instance.add(data1.get(0));
                 instance.add(new int[]{randomNumber1,randomNumber2});
                 moveHistory.add(instance);
                 break;
              }
           }
           // Q[s, a] = Q[s, a] + alpha(Nsa [s, a])(r + gamma max a Q[s', a'] - Q[s, a])
           // where gamma is a discount factor between 0 and 1
           
            
           // get the Q value of the previous state and action
            
           // get the freq value of the previous state and action
            
            
           // s, a, r = s', argmaxa f(Q[s', a'], Nsa [s', a']), r'
         }
			
         data2 = player2.getStatus();
         if (data2.get(1).equals("win") == true){
                System.out.println(String.format("Player 2: %s", data2.get(1)));
	             break;
	      }
         else if (data2.get(1).equals("lose") == true){
            System.out.println(String.format("Player 1: wins"));

            // Set the terminal q state value
            setQState((String)data1.get(0), 100);
            incrementStateFreq(moveHistory.get(moveHistory.size()).get(0), moveHistory.get(moveHistory.size()).get(1)[0], moveHistory.get(moveHistory.size()).get(1)[1] );
            setQState((String)moveHistory.get(moveHistory.size()).get(0), 
            
            
            // Q[s, a] = Q[s, a] + alpha(Nsa [s, a])(r + gamma max a Q[s', a'] - Q[s, a])
            // where gamma is a discount factor between 0 and 1
            
            // get the Q value of the previous state and action
            
            // get the freq value of the previous state and action
            
            
            // s, a, r = s', argmaxa f(Q[s', a'], Nsa [s', a']), r'
            break;
         }
                  
         char[][] grid2 = (char[][])data2.get(0);
			while (true){
              ArrayList instance = new ArrayList<Object>();
              randomNumber1 = random.nextInt(player2.getGridHeight()-1) + 1;
              randomNumber2 = random.nextInt(player2.getGridWidth()-1) + 1;
              System.out.println(grid2[randomNumber1][randomNumber2]);   
              if (grid2[randomNumber1][randomNumber2] == ' '){
                 System.out.println(String.format("%d %d", randomNumber1, randomNumber2));
                 player2.rc.gridOut.println(String.format("%d %d", randomNumber1, randomNumber2));
                 instance.add(data2.get(0));
                 instance.add(new int[]{randomNumber1,randomNumber2});
                 moveHistory.add(instance);
                 break;
              }
         }
	     	      
	     turn++;
		}
	}
}
