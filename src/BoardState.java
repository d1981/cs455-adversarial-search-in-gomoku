class BoardState{
   private char[][] grid; 
   private int[] firstMove;
   private int score; 
   
   public BoardState(char[][] gridArray, int[] myFirstMove){
      grid = new char[gridArray.length][gridArray[0].length];
      
      // deep copy the gridArray values
      for (int i = 0; i<gridArray.length; i++){
         System.arraycopy(gridArray[i], 0, grid[i], 0, gridArray[i].length );   
      }           
      
      firstMove = myFirstMove;
      score = 0;
   }
   
     
   public BoardState(int myscore){
      grid = null;
      firstMove = new int[2];
      score = myscore;
   }
   
   public char[][] getGrid(){
      return grid;
   }
   
   public int[] getFirstMove(){
      return firstMove;
   }
   
   public int getScore(){     
      return score;
   }
   
   public void setScore(int evaluatedscore){
      score = evaluatedscore;
   }
   
   public void setFirstMove(int row, int column){
      firstMove[0] = row;
      firstMove[1] = column;
   }
   
   /**
   * Looks for openness on a run 
   * Returns 1 if one end is open, 2 if both ends are open and 0 if neither are open
   * direction represents if we're looking horizontal:0, vertical:1, 
   *    diagonal increasing:2, or diagonal decreasing:3
   *
   */
   public int checkOpenness(int row, int column, int direction, int length){
       int openness = 0;
       
       if (direction == 0){ //Check openness in a horizontal pattern
           // check left hand side
           try {
              if (grid[row][column-(length + 1)] == ' '){openness+=1;}
              else if (grid[row][column-(length + 1)] != grid[row][column]){openness+=0;}
              else {openness+=1;};
           } catch (IndexOutOfBoundsException e){
              //System.err.println("Index error: " + e.getMessage());
           }
               
           // check right hand side 
           try {
              if (grid[row][column] == ' '){openness+=1;}
              else if (grid[row][column-length] != grid[row][column]){openness+=0;}
              else {openness+=1;};
              
           } catch (IndexOutOfBoundsException e){
              //System.err.println("Index error: " + e.getMessage());
           }
       }
       
       else if (direction == 1){ //Check openness in a vertical pattern
           // check above
           try {
              if (grid[row - 1][column] == ' '){openness+=1;}
              else if (grid[row - 1][column] != grid[row][column]){openness+=0;}
              else {openness+=1;};
           } catch (IndexOutOfBoundsException e){
              //System.err.println("Index error: " + e.getMessage());
           }
           // check below
           try {
              if (grid[row + 1][column] == ' '){openness+=1;}
              else if (grid[row + 1][column] != grid[row][column]){openness+=0;}
              else {openness+=1;};
           } catch (IndexOutOfBoundsException e){
              //System.err.println("Index error: " + e.getMessage());
           }
       }
       
       else if (direction == 2) { //Check openness in a diagonal increasing pattern
           // diagonal backwards up(\)
           try {
              if (grid[row - 1][column - 1] == ' '){openness+=1;}
              else if (grid[row - 1][column - 1] != grid[row][column]){openness+=0;}
              else {openness+=1;};
           } catch (IndexOutOfBoundsException e){
              //System.err.println("Index error: " + e.getMessage());
           }
           // diagonal forward up(/)
           try {
              if (grid[row - 1][column + 1] == ' '){openness+=1;}
              else if (grid[row - 1][column + 1] != grid[row][column]){openness+=0;}
              else {openness+=1;};
           } catch (IndexOutOfBoundsException e){
              //System.err.println("Index error: " + e.getMessage());
           }       
       }
       
       else if (direction == 3) { //Check openness in a diagonal decreasing pattern
           // diagonal backwards down(\)
           try {
              if (grid[row + 1][column + 1] == ' '){openness+=1;}
              else if (grid[row + 1][column + 1] != grid[row][column]){openness+=0;}
              else {openness+=1;};
           } catch (IndexOutOfBoundsException e){
              //System.err.println("Index error: " + e.getMessage());
           }
           // diagonal forward down(/)
           try {
              if (grid[row + 1][column - 1] == ' '){openness+=1;}
              else if (grid[row + 1][column - 1] != grid[row][column]){openness+=0;}
              else {openness+=1;};
           } catch (IndexOutOfBoundsException e){
              //System.err.println("Index error: " + e.getMessage());
           }       
       }    
       return openness;
   }
   
   public void mutateBoard(int row, int column, char player){
      grid[row][column] = player;
   }
   
   public int evaluate(char myplayer, char myopponent){
      int twoinrow_open1, threeinrow_open1, fourinrow_open1;
      int twoinrow_open2, threeinrow_open2, fourinrow_open2;
      
      int opponenttwoinrow_open1, opponentthreeinrow_open1, opponentfourinrow_open1;
      int opponenttwoinrow_open2, opponentthreeinrow_open2, opponentfourinrow_open2;
      
      twoinrow_open1 = threeinrow_open1 = fourinrow_open1 = twoinrow_open2 = threeinrow_open2 = fourinrow_open2 = 0;
      opponenttwoinrow_open1 = opponentthreeinrow_open1 = opponentfourinrow_open1 = opponenttwoinrow_open2 = opponentthreeinrow_open2 = opponentfourinrow_open2 =0;
      
      int inarowcount = 0;
      int opponentinarowcount = 0;
      
      int score = 0;
      
      // Evaluate the horizontal rows for 2,3,4 or 5 in a row matches
      for (int i=0; i < grid.length; i++){
         for (int j=0; j < grid[i].length; j++){
            if (grid[i][j] == myplayer){
               
               inarowcount++;
               
               if(opponentinarowcount > 0){ 
                  int openness = checkOpenness(i,j,0,opponentinarowcount);
                               
                  switch(opponentinarowcount){
                     case 2: if (openness == 1){opponenttwoinrow_open1++;}
                             else if (openness == 2){opponenttwoinrow_open2++;}
                             break;
                             
                     case 3: if (openness == 1){opponentthreeinrow_open1++;}
                             else if (openness == 2){opponentthreeinrow_open2++;}
                             else if (openness == 0){score += 2;} // Block opponents moves!!! 
                             break;
                             
                     case 4: if (openness == 1){opponentfourinrow_open1++;}
                             else if (openness == 2){opponentfourinrow_open2++;}
                             else if (openness == 0){score += 3;} // Block opponents moves!!!                             
                             break;
                     case 5: score -= Integer.MAX_VALUE; break;
                     case 6: score -= 10; break;
                     case 7: score -= 10; break;

                  }
                  opponentinarowcount=0;
               }
            } // end myplayer check
            
            else if(grid[i][j] == myopponent){
               opponentinarowcount++;
               
               if (inarowcount > 0){
                  int openness = checkOpenness(i,j,0,inarowcount);
                  
                  switch(inarowcount){
                     case 2: if (openness == 1){twoinrow_open1++;}
                             else if (openness==2){twoinrow_open2++;}
                             break;
                     case 3: if (openness == 1){threeinrow_open1++;}
                             else if (openness==2){threeinrow_open2++;}
                             else if (openness == 0){score -= 2;} // Block opponents moves!!!
                             break;
                     case 4: if (openness == 1){fourinrow_open1++;}
                             else if (openness==2){fourinrow_open2++;}
                             else if (openness == 0){score -= 3;} // Block opponents moves!!!
                             break;
                     case 5: score += Integer.MAX_VALUE; break;
                     case 6: score += 10; break;
                     case 7: score += 10; break;
                  }
                  inarowcount=0;  
               }         
            } // end myopponent check
            
            else if(grid[i][j] == ' '){
               if (inarowcount > 0){
                  int openness = checkOpenness(i,j,0,opponentinarowcount);
                  
                  switch(inarowcount){
                     case 2: if (openness == 1){twoinrow_open1++;}
                             else if (openness==2){twoinrow_open2++;}
                             break;
                     case 3: if (openness == 1){threeinrow_open1++;}
                             else if (openness==2){threeinrow_open2++;}
                             else if (openness == 0){score -= 2;} // Block opponents moves!!!
                             break;
                     case 4: if (openness == 1){fourinrow_open1++;}
                             else if (openness==2){fourinrow_open2++;}
                             else if (openness == 0){score -= 3;} // Block opponents moves!!!
                             break;
                     case 5: score += Integer.MAX_VALUE; break;
                     case 6: score += 10; break;
                     case 7: score += 10; break;
                     
                  }
                  inarowcount=0;  
               } 
                              
               if(opponentinarowcount > 0){ 
                  int openness = checkOpenness(i,j,0,opponentinarowcount);
                               
                  switch(opponentinarowcount){
                     case 2: if (openness == 1){opponenttwoinrow_open1++;}
                             else if (openness == 2){opponenttwoinrow_open2++;} 
                             break;
                             
                     case 3: if (openness == 1){opponentthreeinrow_open1++;}
                             else if (openness == 2){opponentthreeinrow_open2++;}
                             else if (openness == 0){score += 2;} // Block opponents moves!!! 
                            break;
                     case 4: if (openness == 1){opponentfourinrow_open1++;}
                             else if (openness == 2){opponentfourinrow_open2++;}
                             else if (openness == 0){score += 3;} // Block opponents moves!!! 
                             break;
                     case 5: score -= Integer.MAX_VALUE; break;
                     case 6: score -= 10; break;
                     case 7: score -= 10; break;

                  }
                  opponentinarowcount=0;
               }               
               inarowcount=0;  
            } // end white space check
   
         }// Row changed, check both again

            if(opponentinarowcount > 0){ 
                     int openness = checkOpenness(i,grid[i].length,0,opponentinarowcount);
                                  
                     switch(opponentinarowcount){
                        case 2: if (openness == 1){opponenttwoinrow_open1++;}
                                else if (openness == 2){opponenttwoinrow_open2++;}
                                else if (openness == 0){score += 1;} // Block opponents moves!!! 
                                break;
                                
                        case 3: if (openness == 1){opponentthreeinrow_open1++;}
                                else if (openness == 2){opponentthreeinrow_open2++;}
                                else if (openness == 0){score += 2;} // Block opponents moves!!! 
                                break;
                        case 4: if (openness == 1){opponentfourinrow_open1++; score -= 10000;}
                                else if (openness == 2){opponentfourinrow_open2++;}
                                else if (openness == 0){score += 3;} // Block opponents moves!!! 
                                break;
                        case 5: score -= Integer.MAX_VALUE; break;
                        case 6: score -= 10; break;
                        case 7: score -= 10; break;

                     }
                     opponentinarowcount=0;
            }
                  
            if (inarowcount > 0){
                     int openness = checkOpenness(i,grid[i].length,0,opponentinarowcount);
                     
                     switch(inarowcount){
                        case 2: if (openness == 1){twoinrow_open1++;}
                                else if (openness==2){twoinrow_open2++;}
                                break;
                        case 3: if (openness == 1){threeinrow_open1++;}
                                else if (openness==2){threeinrow_open2++;}
                                else if (openness == 0){score -= 2;} // Block opponents moves!!!
                                break;
                        case 4: if (openness == 1){fourinrow_open1++;}
                                else if (openness==2){fourinrow_open2++;}
                                else if (openness == 0){score -= 3;} // Block opponents moves!!!
                                break;
                        case 5: score += Integer.MAX_VALUE; break;
                        case 6: score += 10; break;
                        case 7: score += 10; break;

                     }
                     inarowcount=0;  
           }
      }

      score += twoinrow_open1*1 + threeinrow_open1*3 + fourinrow_open1*9;
      score += twoinrow_open2*2 + threeinrow_open2*4 + fourinrow_open2*10;
      
      score -= opponenttwoinrow_open1*1 + opponentthreeinrow_open1*3 + opponentfourinrow_open1*9;
      score -= opponenttwoinrow_open2*2 + opponentthreeinrow_open2*4 + opponentfourinrow_open2*10;
      
      
      setScore(score);
      
      return score;
   }
   
   //public interateMove(char player){
   //
   //}
   
   /**
   public static void main(String[] args){
      char[][] grid = new char[11][11];
      
      grid[0] = new char[]{'o','x','x','x','x','o','o','o','x','x',' '};
          
      BoardState bs = new BoardState(grid);
      
      System.out.println(bs.evaluate('x','o'));
   
   }
   */
}