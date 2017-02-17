import java.util.List;

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
       
       if (depth > depthlimit){ // terminal test
          return boardstate;
       }
          
       v = negative_board;; // Negative infinity
       
       for (int i=0; i < boardstate.getGrid().length; i++){
          for (int j=0; j < boardstate.getGrid()[i].length; j++){
             
             if(boardstate.getGrid()[i][j] == ' '){     
                BoardState mutatedBoard;                            
                int[] thefirstmove;
                thefirstmove = boardstate.getFirstMove();
                if (thefirstmove == null){
                   thefirstmove = new int[]{i,j};
                }
                
                mutatedBoard = new BoardState(boardstate.getGrid(), thefirstmove);
                mutatedBoard.mutateBoard(i,j, player);
                mutatedBoard.evaluate(player, opponent);
                                
                w = minValue(mutatedBoard, alpha, beta, depth+1, depthlimit);
                                
                // get the max between v and w
                if (w.getScore() > v.getScore() || (w.getScore() >= v.getScore() && v.getGrid() == null)){
                   v = w;
                }
               
                if (v.getScore() >= beta.getScore()){ 
                  return v;
                }
                  
                // get the max between alpha and v
                if (v.getScore() > alpha.getScore()){
                   alpha = v;
                }
             }
          }
       }
                           
       return v;       
   }
   
   public static BoardState minValue(BoardState boardstate, BoardState alpha, BoardState beta, int depth, int depthlimit){
       BoardState v;
       BoardState w;
       
       if (depth > depthlimit){ // terminal test
          return boardstate;
       }
            
       v = positive_board; // Positive infinity       
              
       for (int i=0; i < boardstate.getGrid().length; i++){
          for (int j=0; j < boardstate.getGrid()[i].length; j++){
     
             if(boardstate.getGrid()[i][j] == ' '){  
                BoardState mutatedBoard;
                mutatedBoard = new BoardState(boardstate.getGrid(), boardstate.getFirstMove());
                mutatedBoard.mutateBoard(i,j, opponent);
                mutatedBoard.evaluate(player,opponent);
               
                w = maxValue(mutatedBoard, alpha, beta, depth+1, depthlimit);
                               
                // get the min between v and w
                if (w.getScore() < v.getScore() || (w.getScore() <= v.getScore() && v.getGrid() == null)){
                   v = w;
                }
                
                if (v.getScore() <= alpha.getScore()){
                   return v;
                }
                
                // get the min between beta and v
                if (v.getScore() < beta.getScore()){
                   beta = v;
                }
   
             }
          }
       }
       
           
       return v;   
       
    
   }
}