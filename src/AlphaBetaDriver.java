/* Manual class for testing Boardstate / AlphaBeta evalutions
*  
*/
class AlphaBetaDriver{
      
   public static void setGrid(char[][] grid){
      grid = grid;
   }

   public static void main(String[] args){
      char[][] grid = new char[9][9];
      
      //grid[0] = new char[]{'x', ' ', ' ',};
      //grid[1] = new char[]{' ', 'o', ' ',};
      //grid[2] = new char[]{'x', 'x', 'x',};
      //                    0   1   2   3   4   5   6   7   8   9   10
      grid[0] = new char[]{' ',' ',' ',' ','x',' ',' ',' ',' '};//,' ',' '};
      grid[1] = new char[]{' ',' ',' ',' ','x',' ',' ',' ',' '};//,' ',' '};
      grid[2] = new char[]{' ',' ','x','x','x','x',' ',' ',' '};//,' ',' '};
      grid[3] = new char[]{' ',' ',' ',' ','x',' ',' ',' ',' '};//,' ',' '};
      grid[4] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' '};//,' ',' '};
      grid[5] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' '};//,' ',' '};
      grid[6] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' '};//,' ',' '};
      grid[7] = new char[]{'o','o','o',' ','o',' ',' ',' ',' '};//,' ',' '};
      grid[8] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' '};//,' ',' '};
      //grid[9] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
      //grid[10] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};

      char player = 'x';
      char opponent = 'o';
      
      // Propogate moves / analyze board state / use Alpha Beta to determine best move before timer runs out
      //AlphaBeta ab = new AlphaBeta((char[][])status.get(0), player, opponent);
      AlphaBeta ab = new AlphaBeta(grid, player, opponent);
      BoardState result = ab.AlphaBetaDecide(2);
      System.out.println(result.getScore());
      
      System.out.println(String.format("%d %d", result.getFirstMove()[0], result.getFirstMove()[1]));
   }
}