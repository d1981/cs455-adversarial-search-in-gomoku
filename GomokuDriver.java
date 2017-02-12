import java.util.ArrayList;
import java.util.Timer;

public class GomokuDriver {
   private static final int RACKETPORT = 17033;         // uses port 1237 on localhost  
   private static final int GRIDWIDTH  = 11;            // Width of the Gomoku Board
   private static final int GRIDHEIGHT = 11;            // Height of the Gomoku Board
   protected RacketClient rc;
      
   public GomokuDriver(String h, int p){
      rc = new RacketClient(h, p);
   }
  
   public static void main(String[] args){
      //GomokuDriver client = new GomokuDriver("localhost", RACKETPORT);
   	//client.run();
      think();
   }
   
   /**
   * Parses the strings from the server into 
   * 2d char array for analyzing, String for gamestatus and String for player identification
   * 
   * Returns an arraylist of generic objects of the above 3 items
   */   
   public ArrayList<Object> getStatus(){
      // Todo: on first run through, determine the size of the 2d array that represents the Gomoku board, instead of relying on constants      
   
      char[][] gridArray = new char[GRIDHEIGHT][GRIDWIDTH];
            
      String gameStatus = "";
      String player = "";
      
      ArrayList objectList = new ArrayList<Object>();

      try {
         for (int i=0; i<(GRIDHEIGHT + 2); i++){
               if (i == 0){ 
                   gameStatus = rc.gridIn.readLine().toLowerCase();
               }
               else if (i < GRIDHEIGHT + 1){
                   
                   String boardRow = rc.gridIn.readLine().toLowerCase();
                   for (int j=0; j<boardRow.length(); j++){
                      gridArray[i-1][j] = boardRow.charAt(j); // gridArray[i-1] because the grid lines are actually coming from the second rc.gridLn.readLine(); call                   
                   }
               }
               else {
                   player = rc.gridIn.readLine().toLowerCase();
               }
            }
       }
      catch(Exception e){
         e.printStackTrace();
      }
         objectList.add(gridArray);
         objectList.add(gameStatus);
         objectList.add(player);
      
      return objectList;
   }
   
   public static void think(){
      ArrayList status;

      // Start timer
      long endTime = System.currentTimeMillis() + 2000;
      while (System.currentTimeMillis() < endTime) {
      
         //status = getStatus();
      
         char player = 'x';
         char opponent = 'o';   
         // if (String.valueOf(status.get(2)) == "o"){
         //    player   = 'o';
         //    opponent = 'x';
         //}
      
         char[][] grid = new char[11][11];
      
         grid[0] = new char[]{' ','o',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[1] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[2] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[3] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[4] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[5] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[6] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[7] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[8] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[9] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
         grid[10] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};

      
         // Propogate moves / analyze board state / use Alpha Beta to determine best move before timer runs out
         //AlphaBeta ab = new AlphaBeta((char[][])status.get(0), player, opponent);
         AlphaBeta ab = new AlphaBeta(grid, player, opponent);
         int[] result = ab.AlphaBetaDecide().getFirstMove();
         System.out.println(String.format("%d %d", result[0], result[1]));
            
         // Send move
         //rc.gridOut.println(String.format("%d %d", result[0], result[1]));
      }
   }
   
   public void run() {
    	    while(true){
             think();    
          }
    }
}
