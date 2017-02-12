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
   
   public static BoardState AlphaBetaDecide(){
      return maxValue(ab_bs, negative_board, positive_board, 0);
   }
      
   public static BoardState maxValue(BoardState boardstate, BoardState alpha, BoardState beta, int depth){
       BoardState v;
       BoardState w;
       boolean movesavailable;
       
       if (depth > 20  ){
         return boardstate;
       }
       
       v = negative_board;; // Negative infinity
       
       movesavailable = false;       
       for (int i=0; i < boardstate.getGrid().length; i++){
          for (int j=0; j < boardstate.getGrid()[i].length; j++){
             
             if(boardstate.getGrid()[i][j] == ' '){  
                movesavailable = true;   
                BoardState mutatedBoard;
                                           
                mutatedBoard = new BoardState(boardstate.getGrid(), new int[]{i,j});
                mutatedBoard.mutateBoard(i,j, player);
                                
                w = minValue(mutatedBoard, alpha, beta, depth+1);
                
                // get the max between v and w
                if (v.getScore() < w.evaluate(player,opponent) ){
                   v = w;
                }
               
                if (v.getScore() >= beta.getScore()){ return v;}
                  
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
              
       //for (int i=0; i<node.children.size(); i++){
       //    v = Math.max( (int) minValue( (Node) node.children.get(i), alpha, beta), v);
       //    if (v >= beta){ return v;}
       //   alpha = Math.max(alpha, v);
       //}
       
       return v;       
   }
   
   public static BoardState minValue(BoardState boardstate, BoardState alpha, BoardState beta, int depth){
       BoardState v;
       BoardState w;
       boolean movesavailable; 
       movesavailable = false;
       
       if (depth > 20  ){
         return boardstate;
       }
       
       v = positive_board; // Positive infinity
       
       
       for (int i=0; i < boardstate.getGrid().length; i++){
          for (int j=0; j < boardstate.getGrid()[i].length; j++){
     
             if(boardstate.getGrid()[i][j] == ' '){  
                movesavailable = true;
                BoardState mutatedBoard;

                mutatedBoard = new BoardState(boardstate.getGrid(), boardstate.getFirstMove());
                mutatedBoard.mutateBoard(i,j, opponent);
                
                w = maxValue(mutatedBoard, alpha, beta, depth+1);
                
                // get the min between v and w
                if (v.getScore() > w.evaluate(player,opponent)){
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
       //for (int i=0; i<node.children.size(); i++){
       //    v = Math.min( (int) maxValue( (Node) node.children.get(i), alpha, beta), v);
       //   if (v <= alpha){return v;}
       //    beta=Math.min(beta, v);
       //}
       
       return v;       
   }
}