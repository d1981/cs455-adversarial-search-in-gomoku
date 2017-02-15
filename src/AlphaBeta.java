import java.util.ArrayList;

class AlphaBeta{
   static BoardState ab_bs;
   static char player;
   static char opponent;
   static int starTime;
   static BoardState negative_board;
   static BoardState positive_board;
        
   public AlphaBeta(char[][] gridArray, char myplayer, char myopponent){
      ab_bs = new BoardState(gridArray, null);
      player = myplayer;
      opponent = myopponent; 
      negative_board = new BoardState(-Integer.MAX_VALUE);
      positive_board = new BoardState(Integer.MAX_VALUE);
   }   
   
   public static BoardState AlphaBetaDecide(int depthlimit){
      return maxValue(ab_bs, negative_board, positive_board, 0, depthlimit);
   }
      
   public static BoardState maxValue(BoardState boardstate, BoardState alpha, BoardState beta, int depth, int depthlimit){
       BoardState v;
       BoardState w;
       boolean movesavailable;
              
       v = negative_board;; // Negative infinity
       if (depth > depthlimit){
          return boardstate;
       }
       movesavailable = false;
       for (int i=0; i < boardstate.getGrid().length; i++){
          for (int j=0; j < boardstate.getGrid()[i].length; j++){
             
             if(boardstate.getGrid()[i][j] == ' '){     
                ArrayList<int[]> moveChain;
                movesavailable = true;
                
                // Create new move chains for the first nodes
                // these will continue to get reused as they are passed down to 
                // new boardstates. 
                // This way a complete chain of moves that lead to a high value state 
                // can be preserved. 
                if (depth == 0){
                    moveChain = new ArrayList<int[]>();
                }
                else {
                    moveChain = boardstate.getMoveChain();
                }                
                BoardState mutatedBoard;                                           
                mutatedBoard = new BoardState(boardstate.getGrid(), moveChain, new int[]{i,j});
                mutatedBoard.mutateBoard(i,j, player);
                mutatedBoard.evaluate(player, opponent);
                
                if (mutatedBoard.getScore() > 1000){ // terminal test
                   return mutatedBoard;
                }
                
                w = minValue(mutatedBoard, alpha, beta, depth+1, depthlimit);
                
                // get the max between v and w
                if (v.getScore() < w.getScore() ){
                   v = w;
                }
               
                if (v.getScore() >= beta.getScore()){ 
                  return v;
                }
                  
                // get the max between alpha and v
                if (alpha.getScore() < v.getScore()){
                   alpha = v;
                }
             }
          }
       }
       if (movesavailable == false){
          return boardstate;
       }                      
       return v;       
   }
   
   public static BoardState minValue(BoardState boardstate, BoardState alpha, BoardState beta, int depth, int depthlimit){
       BoardState v;
       BoardState w;
       boolean movesavailable; 
                     
       v = positive_board; // Positive infinity
       if (depth > depthlimit){
          return boardstate;
       }
       
       movesavailable = false;
       for (int i=0; i < boardstate.getGrid().length; i++){
          for (int j=0; j < boardstate.getGrid()[i].length; j++){
     
             if(boardstate.getGrid()[i][j] == ' '){  
                movesavailable = true;
                
                BoardState mutatedBoard;
                mutatedBoard = new BoardState(boardstate.getGrid(), boardstate.getMoveChain(), new int[]{i,j});
                mutatedBoard.mutateBoard(i,j, opponent);
                mutatedBoard.evaluate(player,opponent);
                
                if (mutatedBoard.getScore() < -1000){ //terminal test
                   return mutatedBoard;
                }
                
                w = maxValue(mutatedBoard, alpha, beta, depth+1, depthlimit);
                
                // get the min between v and w
                if (v.getScore() > w.getScore()){
                   v = w;
                }
                
                if (v.getScore() <= alpha.getScore()){
                   return v;
                }
                
                // get the min between beta and v
                if (beta.getScore() > v.getScore()){
                   beta = v;
                }     
             }
          }
       }
       if (movesavailable == false){
          return boardstate;
       }        
       return v;       
   }
}