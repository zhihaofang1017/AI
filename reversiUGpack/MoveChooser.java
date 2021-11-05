import java.util.ArrayList;


public class MoveChooser {
    // given board weight
    public static int[][] weight = {{120, -20, 20,  5,  5, 20, -20, 120},
                                    {-20, -40, -5, -5, -5, -5, -40, -20},
                                    { 20,  -5, 15,  3,  3, 15,  -5,  20},
                                    {  5,  -5,  3,  3,  3,  3,  -5,   5},
                                    {  5,  -5,  3,  3,  3,  3,  -5,   5},
                                    { 20,  -5, 15,  3,  3, 15,  -5,  20},
                                    {-20, -40, -5, -5, -5, -5, -40, -20},
                                    {120, -20, 20,  5,  5, 20, -20, 120}};
    
    // calculate the board weight for every step
    public static int evaluation(BoardState boardState_temp) {
        int count = 0;
        for (int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
                if(boardState_temp.getContents(i, j)== 1) {
                    count = count + weight[i][j];
                }
                else if (boardState_temp.getContents(i, j)==-1) {
                    count = count - weight[i][j];
                }   
            }
        }
        return count;
    }
    
    public static int alphaBeta(int depth,int max_temp,int min_temp,BoardState boardState_temp) {
        // when depth is 0, at the bottom of the tree
        if (depth == 0){
            return evaluation(boardState_temp);
        }
        
        // Initialize boradstate
        ArrayList<Move> action_list = boardState_temp.getLegalMoves();
        BoardState boardstate1 = boardState_temp.deepCopy();
        
        // when no legal moves exists
        if(action_list.size()==0) {
            if(boardstate1.colour == 1) {
                boardstate1.colour = -1;
                // when no legal moves exists for the other one
                if (boardstate1.getLegalMoves().size() == 0) {
                    return evaluation(boardState_temp);
                }
                return alphaBeta(depth-1, max_temp, min_temp, boardstate1);

            }
            else {
                boardstate1.colour = 1;
                if (boardstate1.getLegalMoves().size() == 0) {
                    return evaluation(boardState_temp);
                }
                return alphaBeta(depth-1, max_temp, min_temp, boardstate1);
            }
        }
        
        
        for (int i=0;i<action_list.size();i++) {
            // make a legal move
            boardstate1.makeLegalMove(action_list.get(i).x,action_list.get(i).y );
            int current = alphaBeta(depth-1, max_temp, min_temp, boardstate1);
            // undo previous move
            boardstate1 = boardState_temp.deepCopy();
            
            //alpha-beta pruning
            if (boardState_temp.colour == 1) {
                if (current >= min_temp) {
                    return current;
                }
                if(current >= max_temp) {
                    max_temp = current;
                }
            }
            else {
                if (current <= max_temp) {
                    return current;
                }
                if(current <= min_temp) {
                    min_temp = current;
                }
            }
        }
        // judge which turn and return respective value
        // colour -1 is black while 1 is white
        if (boardState_temp.colour == 1) {
            return max_temp;
        }
        else {
            return min_temp;
        }
    }
    
  
    public static Move chooseMove(BoardState boardState){

        int searchDepth= Othello.searchDepth;
        searchDepth = searchDepth - 1;
//        Initialize current max, min
        int Max = -9999999;
        int Min = 9999999;
        Move resultMove = null;
        ArrayList<Move> moves= boardState.getLegalMoves();
        if(moves.isEmpty()){
            return null;
        }
     // implement α-β pruning algorithms
        for (int i = 0;i < moves.size();i++)
        {
            Move move_temp = moves.get(i);
            BoardState boardState1 = boardState.deepCopy();
            if(boardState1.checkLegalMove(move_temp.x, move_temp.y))
            {
                boardState1.makeLegalMove(move_temp.x, move_temp.y);
                int current = alphaBeta(searchDepth,Max, Min, boardState1);
                if (current > Max){
                    Max = current;
                    resultMove = move_temp;
                }
            }
        }
        return resultMove;
    }
}
