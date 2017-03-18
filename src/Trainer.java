import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 * class Trainer 
 * 
 * Trainer for learning the evaluation function 
 *
 * All states are stored as the perspective of player 1 being X
 *
 *@author:  Josh Kapple
 *@date:    3-07-17
 *@version: Beta 0.1
 */
public class Trainer {
   static DatabaseInterface db = null;
   
	private static final int RACKETPORT = 17033;         // uses port 1237 on localhost  
	
   public static void handleTerminal(ArrayList moveHistory, ArrayList data){
      // Set the terminal q state value
      db.updateQStateValue(db.convertGridtoString((char[][])data.get(0)), -1, -1, 100); // -1,-1 denotes no action performed
      
      // iterate over the move history and fill out previous state values   
   } 
   
   public static void handleTurn(ArrayList moveHistory, ArrayList data, char player){
      //System.out.println(String.format("Player 1: %s", data.get(1)));

      // go through the rest of the move chain and update the other state
   }
   
   public static void propogateStateLegalActions(ArrayList data, boolean player1isX){
       char[][] grid = (char[][])data.get(0);
       for (int i=0; i < grid.length; i++){
          for (int j=0; j < grid[i].length; j++){
              if (grid[i][j] == ' '){
                 // blank character, this is a valid move
                 if (player1isX){
                    db.insertStateAndActionsQTable(db.convertGridtoString(grid), i, j, (float)0.0);
                    db.insertStateAndActionsFreq(db.convertGridtoString(grid), i, j, 0);
                 }
                 else {
                    db.insertStateAndActionsQTable(db.swapPlayersInGrid(db.convertGridtoString(grid)), i, j, (float)0.0);
                    db.insertStateAndActionsFreq(db.swapPlayersInGrid(db.convertGridtoString(grid)), i, j, 0);
                 }
              }
          }
       }
   }

   public static void run(){
     int turn = 0;
      Random random  = new Random();
		Random random2 = new Random();
      
      // Connect to the database 
      db = new DatabaseInterface();      
   	
      // Start gomoku players
		GomokuDriver player1 = new GomokuDriver("localhost", RACKETPORT);      
		GomokuDriver player2 = new GomokuDriver("localhost", RACKETPORT);
      
		String result1 = "";
		String result2 = "";
		int randomNumber1 = 0;
      int randomNumber2 = 0;

		ArrayList data1;
      ArrayList data2;
      
      ArrayList player1moveHistory = new ArrayList<ArrayList<Object>>();
      ArrayList player2moveHistory = new ArrayList<ArrayList<Object>>();
      
      boolean player1isX;
            
      // Loop and play both players moves
		while(true){
         // record the states and moves players made
         data1 = player1.getStatus();
         //System.out.println(data1.get(2));
         // check if player1 is X
         if (data1.get(2).equals("x")){
            player1isX = true;
         }
         else{
            player1isX = false;
         }
         
         // If this is a new state, build out the QTable and FreqTable
         // TODO add clause that checks if this state is in the database, if so we can assume the possibilities have been propagated already
         propogateStateLegalActions(data1, player1isX);
                  
         // Other player must have made an illegal move for us to be seeing a win state on our turn
         if (data1.get(1).equals("win") == true){     
             // do the terminal test
             //  
             break;
	      }
         
         else if (data1.get(1).equals("lose") == true){
             // player two one
             //System.out.println(String.format("Player 2: win"));
             break;
         }  
         
        // Didn't win or lose, game is still playable        
		  // Player 1 normal turn
        // 
        char[][] grid1 = (char[][])data1.get(0);
        ArrayList instancep1 = new ArrayList<Object>();           
        ArrayList fFunctionResult = db.fFunction(db.convertGridtoString(grid1));
        
        // if previous state is not null, increment frequency table of previous state and previous action
        if (player1moveHistory.size() > 0){
           ArrayList lastmove = (ArrayList)player1moveHistory.get(player1moveHistory.size()-1);
           String previousgridstring = db.convertGridtoString((char[][])lastmove.get(0));
           db.incrementStateFreq(previousgridstring, (int)lastmove.get(1), (int)lastmove.get(2));
           
           // update Qtable
           // Q[s, a] = Q[s, a] + aplha(Nsa [s, a])(r + discount maxa Q[s', a'] - Q[s, a])
           // Q value of last state and action = Qvalue[last state, action] + learningrate*(freq[last state, action)(previous reward + discountfactor 

           // get the previous state action pair q value 
           ArrayList prevQstate = (ArrayList)db.selectStateFromQTable(previousgridstring, (int)lastmove.get(1), (int)lastmove.get(2)).get(0);
           
           // get the previous state action pair freq
           ArrayList prevFstate = (ArrayList)db.selectStateFromFreqTable(previousgridstring, (int)lastmove.get(1), (int)lastmove.get(2)).get(0);
           float r = (float)prevQstate.get(4);
           float gamma = (float)0.8;
           int Nsa =  (int)prevFstate.get(4);
           
           float updateValue = r + db.learningRate(Nsa)*(r + gamma*((int)fFunctionResult.get(2) - r));
           db.updateQStateValue(previousgridstring, (int)lastmove.get(1), (int)lastmove.get(2), updateValue);
        }    
        
        // play the move we retrieved
        // and add it to the move history                
        player1.rc.gridOut.println(String.format("%d %d", fFunctionResult.get(4), fFunctionResult.get(5)));
        instancep1.add(data1.get(0));
        instancep1.add((int)fFunctionResult.get(4));
        instancep1.add((int)fFunctionResult.get(5));
        player1moveHistory.add(instancep1);

        data2 = player2.getStatus();
        if (data2.get(1).equals("win") == true){
                //System.out.println(String.format("Player 2: %s", data2.get(1)));
	             break;
	      }
         else if (data2.get(1).equals("lose") == true){
            //System.out.println(String.format("Player 1: wins"));
            break;
         }
                  
         char[][] grid2 = (char[][])data2.get(0);
			while (true){
              ArrayList instance = new ArrayList<Object>();
              randomNumber1 = random.nextInt(player2.getGridHeight()-1) + 1;
              randomNumber2 = random.nextInt(player2.getGridWidth()-1) + 1;
              //System.out.println(grid2[randomNumber1][randomNumber2]);   
              if (grid2[randomNumber1][randomNumber2] == ' '){
                 //System.out.println(String.format("%d %d", randomNumber1, randomNumber2));
                 player2.rc.gridOut.println(String.format("%d %d", randomNumber1, randomNumber2));
                 instance.add(data2.get(0));
                 instance.add(new int[]{randomNumber1,randomNumber2});
                 player2moveHistory.add(instance);
                 break;
              }
         }
	     	      
	     turn++;
		}

   }

	public static void main(String[] args){
      while(true){
         run();
      }
   
	}
}
