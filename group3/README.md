Group 3: Joshua Kapple, Matt Huff, Sheridan Olds

2/17/2017

I. Acknowledgment
II. How to run the agent
III. What we did

---------------------------------------------------------------------------------
I. Acknowledgment

	The adversarial search in Gomoku uses an implementation of Alpha-Beta pruning. Through this pruning, we were asked to code a program that would select the best move to lead our agent to victory. Alpha-beta pruning is a search algorithm that seeks to decrease the number of nods that are evaluated by the minimax algorithm in its search tree. It is most commonly found in machines for two-player games such as chess, tic-tac-toe, or Gomoku. It stops evaluating a move when at least one possibility has been found that proves the move to be worse than a previously examined move. The benefit of Alpha-Beta pruning over minimax search lies in the fact that branches of the search tree can be eliminated. Further heuristic improvements can be achieved without sacrificing accuracy by ordering heuristics to search pasrts of the tree that are likely to force cutoffs earlier. For example, we are able to evaluate moves that are more important such as blocking an opponent who has three in a row to stop them from getting four in a row which would lead to a loss for our program. Or, vice-versa, we could find where we have three in a row and go next to that, securing a win.

---------------------------------------------------------------------------------
II. How to run the program. 
Must download Racket, the my Gomoku Github repository, and cs455-adversarial-search-in-gomoku

	1) Open GomokuServer.rkt in Dr.racket and run.
	2) Open either RandomPlayer.rkt or ManualPlayer.rkt and run whichever you opened.
	3) Run GomokuDriver.java from cs455-adversarial-search-in-gomoku

---------------------------------------------------------------------------------
III. What we did.

1. Create GomokuDriver.java class and RacketClient.java class which we used to connect to the game. 
2. Next step was to understand how Minimax search worked so we created a minimax.java class. 
3. Add a method to get the information provided by Gomoku and turn it into an arraylist with a 2D char array for the grid and strings for the pieces of info provided.
4. Implemented Alpha-Beta pruning through an alpha-beta class
5. Hooked up Alpha-Beta to propagate the moves and created an evaluation function in BoardState.java to calculate scores for horizontal rows.
	Had to create an AlphaBetaDriver to test/debug without using racket.
6. Had to edit BoardState.java to add vertical, horizontal, forwards/backwards, and diagonal checks.
7. Spent a lot of time debugging the program.
8. Refactored GomokuDriver to adapt to the grid size without using hardcoded values.
9. Fixed a bug preventing beta from being set correctly.
10. Refactored evaluate to use two switch statements instead of repeating them.
11. Implemented a time limit.
12. Implemented code used to block opponents.