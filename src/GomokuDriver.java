import java.util.ArrayList;

public class GomokuDriver {
   private static final int RACKETPORT = 17033;         // uses port 1237 on localhost  
   private static final int GRIDWIDTH  = 11;            // Width of the Gomoku Board
   private static final int GRIDHEIGHT = 11;            // Height of the Gomoku Board
   protected RacketClient rc;
      
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
   
   public void think(){
      ArrayList  status;

      // Start timer
      
      
      status = getStatus();

      // Analyze status for features
      

      // Propogate moves / use Alpha Beta to determine best move before timer runs out
      
      
      // Send move
      
            
   }
   
   public void run() {
    	    while(true){
             think();    
          }
    }
}
