/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictictactoe;
import java.util.*;
/**
 *
 * @author Brandon
 */
public class TicTicTacToe {

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//    }
     /********************** YOUR CODE GOES HERE **********************/
 
    /**
    * Computes the heuristic value of the board for a given player.
    * @param board A 4x4 array of characters, either 'X', 'O', or ' '.
    * @param who Either 'X' or 'O'.
    * @return A positive integer giving the value of the board
    */
    public static float evaluate(char[][] board, char who) {
        float heurestic_value = 500;
        Random rand = new Random();
        char player;
        //determines what character the AI and player are
        char computer;
        if(who == 'O'){
            computer = 'O';
            player = 'X';
        }else{
            computer = 'X';
            player = 'O';
        }
        
        
        //----------------------Testing Two In A Row Cases--------------------------------------//
        //Checks two in a row cases for AI
        //Checks all rows for two in a row
            for (int row = 0; row < 4; row++){
                for (int col = 0; col < 3; col++){
                    if (board[row][col] == computer && board[row][col+1] == computer){//changed who to computer
                        heurestic_value = 600 + rand.nextInt(20);//was 700
                    }
                }
            }
            //Checks all columns for two in a row
            for (int row = 0; row < 2; row++){
                for (int col = 0; col < 4; col++){
                    if (board[row][col] == computer && board[row+1][col] == computer){
                        heurestic_value = 600 + rand.nextInt(20);
                    }
                }
            }
            //Checks all diagonals
            for (int row = 0; row < 2; row++){
                for (int col = 0; col < 2; col++){
                    if (board[row][col] == computer && board[row+1][col+1] == computer){
                        heurestic_value = 600 + rand.nextInt(20);
                    }
                }
            }
            for (int row = 0; row < 2; row++){
                for (int col = 2; col < 4; col++){
                    if (board[row][col] == computer && board[row+1][col-1] == computer){
                        heurestic_value = 600 + rand.nextInt(20);
                    }
                }
            }
              

        ///Handels frst move for AI. This will prioritize randomly picking one of the four center pieces on the board
        for(int row = 1; row < 3; row++){
            for(int col = 1; col < 3; col ++){
                if(board[row][col] == computer){
                    heurestic_value += rand.nextInt(20) + 1;
                }
            }
        }
        
       
        //------------------------Test score for AI----------------------------
        //These two if statements checks if the AI has a score. If it does it adds to the initial value of the heuristic value so
        //that the board heuristic value does not always get set to the score if the AI has one point/3 in a row which is 700
        if(getScore(board, computer) == 1){
            heurestic_value += 100;
        }
        if(getScore(board, computer) == 2){
            heurestic_value += 200;
        }
        
        //-----------------------Test Winning cases and three in a rows---------------------------------------
        //test winn case for AI, and returns the max heuristic value based on the scale
        if(winner(board, computer)){
            heurestic_value = 1000;
            //test win case for player and returns min heuristic value based on the range of values
        }else if(winner(board, player)){
            heurestic_value = 0;
            //tests if the the AI already has a point/ 3 in a row
        }else if(getScore(board, computer) == 1){
            if(heurestic_value < 700){
                heurestic_value = 700;
            }
            //tests if the AI has two points/ 3 in a rows
        }else if(getScore(board, computer) == 2){
            if(heurestic_value < 800){
                heurestic_value = 800;
            }
            //tests if the AI has three points/ 3 in a rows
        }else if(getScore(board, computer) == 3){
            heurestic_value = 900;
            //tests if the player has a point/ 3 in a row
        }else if(getScore(board, player) == 1){
            heurestic_value = 300 + rand.nextInt(20);
            //test if the player has two points/ 3 in a rows
        }else if(getScore(board, player) == 2){
            heurestic_value = 200;
            //tests if the player has 3\three points/ 3 in a rows
        }else if(getScore(board, player) == 3){
            heurestic_value = 100;
        }
        
        
        return heurestic_value;
    }






/***************** END OF YOUR CODE ********************************/

    


    static final int MAXLEVEL = 2;

/* This is the main function for playing the game. It alternatively
   prompts the user for a move, and uses the minmax algorithm in 
   conjunction with the given evaluation function to determine the
   opposing move. This continues until the board is full. It returns
   the number scored by X minus the number scored by O. */

    public static boolean run(int[] scores, char who) { 
        int i, j;
        char current, other;
        int playerrow, playercol;
        int[] location = new int[2];  // Allows us to pass row, col by reference
        int move = 1;

        /* Initialize the board */
        char[][] board = new char[4][4];
        for (i = 0; i < 4; i++) { 
            for (j = 0; j < 4; j++) {
                board[i][j] = ' ';
            }
        }

        if (who == 'O') display(board);

        while (move <= 16) {
            if (move % 2 == 1) {
                current = 'X';
                other = 'O';
            }
            else {                
                current = 'O';
                other = 'X';
            }

            if (current == who) {        /* The computer's move */
                choose(location, board, who);  /* Call function to compute move */
                System.out.println("Computer chooses " + (location[0]+1) + ", " + (location[1]+1));
                if (board[location[0]][location[1]] == ' ') 
                    board[location[0]][location[1]] = current;
                else {
                    System.out.println("BUG! " + (location[0]+1) + ", " + (location[1]+1) + " OCCUPIED!!!");
                    System.exit(0);
                }
                if (winner(board, who)) {
                    System.out.println("Computer has 4 in a row! Computer wins!");
                    display(board);
                    return true;
                }
            }

            else {                       /* Ask for player's move */
                Scanner in = new Scanner(System.in);
                System.out.print("Player " + current + ", enter row: ");
                playerrow = in.nextInt();
                System.out.print("Player " + current + ", enter column: ");
                playercol = in.nextInt();
                while (board[playerrow-1][playercol-1] != ' ' ||
                    playerrow < 1 || playerrow > 4 ||
                    playercol < 1 || playercol > 4) {
                        System.out.println("Illegal move! You cannot use that square!");
                
                        System.out.print("Player " + current + ", enter row: ");
                        playerrow = in.nextInt();
                        System.out.print("Player " + current + ", enter column: ");
                        playercol = in.nextInt();
                }
                playercol--; playerrow--;
                board[playerrow][playercol] = current;
                if (winner(board, current)) {
                    System.out.println("Player has 4 in a row! Player wins!");
                    display(board);
                    return true;            
                }
            }
 
            display(board);    /* Redisplay board to show the move */

            move++; /* Increment the move number and do next move. */
        }
        scores[0] = getScore(board, 'X');
        scores[1] = getScore(board, 'O');
        return false;
    }

    /* This function determines if there is a winner (based on 4 in a row) */
    public static boolean winner(char[][] board, char who) {
        for (int i = 0; i < 4; i++) {
            if (board[i][0] == who && 
                board[i][1] == who &&
                board[i][2] == who &&
                board[i][3] == who) {
                //cout << "Win in row " << i << "\n";
                return true;
            }
        }
        for (int i = 0; i < 4; i++) {
            if (board[0][i] == who && 
                board[1][i] == who &&
                board[2][i] == who &&
                board[3][i] == who) {            
                //cout << "Win in row " << i << "\n";
                return true;
            }   
        }
        if (board[0][0] == who && 
            board[1][1] == who &&
            board[2][2] == who &&
            board[3][3] == who) {
            // cout << "Win along main diagonal\n";
            return true;
            }
        
        if (board[0][3] == who && 
            board[1][2] == who &&
            board[2][1] == who &&
            board[3][0] == who) {
            // cout << "Win along other diagonal\n";
            return true;
            }
        return false;
    }


/* This function exhaustively searches the current board to count the
   sets of 3 each player has in a row. */

    public static int getScore(char[][] board, char who) {
      int row, col;
      int X, O;
      int score = 0;

      /* check all rows */
      for (row = 0; row < 4; row++)
        for (col = 0; col < 2; col++)
          if (board[row][col] == who &&
              board[row][col+1] == who &&
              board[row][col+2] == who) score++;

      /* check all columns */
      for (row = 0; row < 2; row++)
        for (col = 0; col < 4; col++)
          if (board[row][col] == who &&
              board[row+1][col] == who &&
              board[row+2][col] == who) score++;

      /* check all diagonals */
      for (row = 0; row < 2; row++)
        for (col = 0; col < 2; col++)
          if (board[row][col] == who &&
              board[row+1][col+1] == who &&
              board[row+2][col+2] == who) score++;
      for (row = 0; row < 2; row++)
        for (col = 2; col < 4; col++)
          if (board[row][col] == who &&
              board[row+1][col-1] == who &&
              board[row+2][col-2] == who) score++;
      return score;
      }

    
/* This displays the current configuration of the board. */

    public static void display(char[][] board) {
        int row, col;  
        int scores[] = new int[2];
        System.out.print("\n");
        for (row = 3; row >= 0; row--) {
            System.out.print("  +-+-+-+-+\n");
            System.out.print((row+1) + " ");
            for (col = 0; col < 4; col++) {
            if (board[row][col] == 'X')  /* if contents are 0, print space */
                System.out.print("|X");
            else if (board[row][col] == 'O')
                System.out.print("|0");
            else System.out.print("| ");
            }
            System.out.print("|\n");
        }
        System.out.print("  +-+-+-+-+\n");  /* print base, and indices */
        System.out.print("   1 2 3 4\n");
        scores[0] = getScore(board, 'X');
        scores[1] = getScore(board, 'O');
        System.out.println("X: " + scores[0]);
        System.out.println("O: " + scores[1]);
    }
   
/* Basic function for choosing the computer's move. It essentially
   initiates the first level of the MINMAX algorithm, and returns
   the column number it chooses. */

    public static void choose(int[] location, char[][] board, char who) {
        int move; 
        float value;
        getmax(location, board, 1, who);
    }


/* This handles any MAX level of a MINMAX tree. Using a pointer to move
   is a hack to deal with not being able to return both the chosen move
   and its evaluation number. 

   This essentially handles moves for the computer. */

    public static float getmax(int[] location, char[][] board, int level, char who) {
        char[][] tempboard = new char[4][4];
        int r,c = 0;
        float max = -1;
        float val;
        int[] tempLocation = new int[2];
        for (r = 0; r < 4; r++)
            for (c = 0; c < 4; c++) {  /* Try each row and column in board */
                if (board[r][c] == ' ') {     /* Make sure square not full */
                   //System.out.println("Row: " + (r+1));
                   //System.out.println("Col: " + (c+1));
                /* To avoid changing original board  during tests, make a copy */
                copy(tempboard, board); 

                /* Find out what would happen if we chose this column */
                tempboard[r][c] = who;

                /* If this is the bottom of the search tree (that is, a leaf) we need
                    to use the evaluation function to decide how good the move is */
                if (level == MAXLEVEL) 
                    val = evaluate(tempboard, who);

                /* Otherwise, this move is only as good as the worst thing our
                    opponent can do to us. */
                else
                    val = getmin(tempLocation, tempboard, level+1, who);

                /* Return the highest evaluation, and set call by ref. parameter
                    "move" to the corresponding column */
                if (val > max) {
                    max = val;
                    if (level==1) {location[0] = r; location[1] = c;}
                 }
                 //System.out.println(val);
            }
        }
        return max;
    }

/* This handles any MIN level of a MINMAX tree. Using a pointer to move
   is a hack to deal with not being able to return both the chosen move
   and its evaluation number. 

   This essentially handles moves for the opponent. */

    public static float getmin(int[] location, char[][] board, int level, char who) {
        char[][] tempboard = new char[4][4];
        int r,c = 0;   
        int[] tempLocation = new int[2];
        float min = 10001;
        float val;

        /* Since this is opponent's move, we need to figure out which they are */
        char other;
        if (who == 'X') other = 'O'; else other = 'X'; 

        for (r = 0; r < 4; r++)
            for (c = 0; c < 4; c++) {  /* Try each row and column in board */
                if (board[r][c] == ' ') {     /* Make sure square not full */

                    /* To avoid changing original board  during tests, make a copy */
                    copy(tempboard, board);

                    /* Find out what would happen if opponent chose this column */
                    tempboard[r][c] = other;

                    /* If this is the bottom of the search tree (that is, a leaf) we need
                    to use the evaluation function to decide how good the move is */
                    if (level == MAXLEVEL)  
                        val = evaluate(tempboard, who);

                    /* Otherwise, find the best thing that we can do if opponent
                        chooses this move. */
                    else
                        val = getmax(tempLocation, tempboard, level+1, who);

                    /* Return the lowest evaluation (which we will assume will be 
                        chosen by opponent, and set call by ref. parameter
                        "move" to the corresponding column */
                    if (val < min) {
                        min = val;
                        // *move = col;
                    }
                }
            }
        return min;
   }


/* This function makes a copy of a given board. This is necessary to be
   able to "try out" the effects of different moves without messing up
   the actual current board. */

    public static void copy(char[][] a, char[][] b) {
        int i, j;
        for (i = 0; i < 4; i++) { 
            for (j = 0; j < 4; j++) {
                a[i][j] = b[i][j];  
            }
        }
    }
     


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        char player, computer;
        int[] scores = new int[2];
        /* Decide who goes first */
        System.out.print("Do you want to play X or O: ");
        Scanner in = new Scanner(System.in);
        player = in.nextLine().charAt(0);
        if (player == 'X') computer = 'O';
        else computer = 'X';
        boolean win = false;
        win = run(scores, computer);
        if (!win)
            System.out.println("\nFinal score: \nX: " + scores[0] + "\nO: " + scores[1] + "\n");
        }
}
    
