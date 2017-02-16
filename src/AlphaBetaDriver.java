class AlphaBetaDriver{
      
   public static void setGrid(char[][] grid){
      grid = grid;
   }

   public static void main(String[] args){
      char[][] grid = new char[3][3];
      
      grid[0] = new char[]{'x', ' ', ' ',};
      grid[1] = new char[]{' ', 'o', ' ',};
      grid[2] = new char[]{'x', 'x', 'x',};
      
      //grid[3] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
      //grid[4] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
      //grid[5] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
      //grid[6] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
      //grid[7] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
      //grid[8] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
      //grid[9] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
      //grid[10] = new char[]{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};

      char player = 'x';
      char opponent = 'o';
      
      
      
      
      
      // Propogate moves / analyze board state / use Alpha Beta to determine best move before timer runs out
      //AlphaBeta ab = new AlphaBeta((char[][])status.get(0), player, opponent);
      AlphaBeta ab = new AlphaBeta(grid, player, opponent);
      BoardState result = ab.AlphaBetaDecide();
      
      System.out.println(String.format("%d %d", result.getFirstMove()[0], result.getFirstMove()[1]));
   }
}