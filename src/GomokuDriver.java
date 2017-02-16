import java.util.ArrayList;

public class GomokuDriver {
   private static final int RACKETPORT = 17033;         // uses port 1237 on localhost  
   private static final int GRIDWIDTH  = 11;            // Width of the Gomoku Board
   private static final int GRIDHEIGHT = 11;            // Height of the Gomoku Board
   protected static RacketClient rc;
      
   public GomokuDriver(String h, int p){
      rc = new RacketClient(h, p);
   }
  
   public static void main(String[] args){
      GomokuDriver client = new GomokuDriver("localhost", RACKETPORT);
   	client.run();
   }
   
   /**
   * Parses the strings from the server into 
   * 2d char array for analyzing, String for gamestatus and String for player identification
   * 
   * Returns an arraylist of generic objects of the above 3 items
   */   
   public static ArrayList<Object> getStatus(){
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
   
   public static String think(int turn){
      ArrayList  status;
      int depthlimit = 0;
      
      // Start timer
      
      
      status = getStatus();
      char player = 'x';
      char opponent = 'o';   
      if (String.valueOf(status.get(2)).equals("o")){
         player   = 'o';
         opponent = 'x';
      }
      
      if (turn < 5){
         depthlimit = 2;
      }
      else if (turn > 5 ){
         depthlimit = 3;
      }
      else if (turn > 8 ){
         depthlimit = 4;
      }

          
      // Propogate moves / analyze board state / use Alpha Beta to determine best move before timer runs out
      AlphaBeta ab = new AlphaBeta((char[][])status.get(0), player, opponent);
      BoardState result = ab.AlphaBetaDecide(depthlimit);   
      System.out.println(String.format("%d %d", result.getFirstMove()[0], result.getFirstMove()[1]));
         
      // Send move
      rc.gridOut.println(String.format("%d %d", result.getFirstMove()[0], result.getFirstMove()[1]));
      return (String)status.get(1);
   }
   
   public void run() {
      int turn = 0;
      String result = "";
      
    	    while(true){
             result = think(turn);    
             if (!result.equals("continuing")){
                break;
             }
             turn += 1; 
             System.out.println(String.format("Turn: %s", turn));
          }
    }
}
