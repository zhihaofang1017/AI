import java.util.ArrayList;

public class MoveChooser {
	
    public int emptylistFlag = 10000000;
	public static int deepth = 0;
	public static int deepth_max;
	public static Move Move_temp = null;
	
	public static int[][] weight = {{120, -20, 20,  5,  5, 20, -20, 120},
							        {-20, -40, -5, -5, -5, -5, -40, -20},
							        { 20,  -5, 15,  3,  3, 15,  -5,  20},
							        {  5,  -5,  3,  3,  3,  3,  -5,   5},
							        {  5,  -5,  3,  3,  3,  3,  -5,   5},
							        { 20,  -5, 15,  3,  3, 15,  -5,  20},
							        {-20, -40, -5, -5, -5, -5, -40, -20},
							        {120, -20, 20,  5,  5, 20, -20, 120}};
	
	
	public static int evaluation(BoardState boardState_temp) {
		int count = 0;
		for (int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				if(boardState_temp.getContents(i, j)== 1) {
					count = count + weight[i][j];
//					System.out.println("("+i+","+j+")  "+count);
				}
				else if (boardState_temp.getContents(i, j)==-1) {
					count = count - weight[i][j];
//					System.out.println("("+i+","+j+")  "+count);
				}	
			}
		}
		return count;
	}
	
	public static int alphaBeta(int max_temp,int min_temp,BoardState boardState_temp) {
		if (deepth > deepth_max){
			return evaluation(boardState_temp);
		}
		
		ArrayList<Move> action_list = boardState_temp.getLegalMoves();
		int max = -9999999;
	    int min = 9999999;
	    BoardState boardstate1 = boardState_temp.deepCopy();
//		System.out.println(action_list.size()+"aaaa");
		if(action_list.size()==0) {
			if(boardstate1.colour == 1) {
				boardstate1.colour = -1;
				if (boardstate1.getLegalMoves().size() == 0) {
					return evaluation(boardState_temp);
				}
				return alphaBeta(max_temp, min_temp, boardstate1);

			}
			else {
				boardstate1.colour = 1;
				if (boardstate1.getLegalMoves().size() == 0) {
					return evaluation(boardState_temp);
				}
				return alphaBeta(max_temp, min_temp, boardstate1);
			}
		}
		
		
	    for (int i=0;i<action_list.size();i++) {
	    	boardstate1.makeLegalMove(action_list.get(i).x,action_list.get(i).y );
//	    	System.out.println("("+action_list.get(i).x + ","+action_list.get(i).y+")");
	    	deepth = deepth + 1;
	    	int current = alphaBeta(max_temp, min_temp, boardstate1);
//	    	System.out.println(current+" i "+i);
	    	deepth = deepth - 1;
	    	boardstate1 = boardState_temp.deepCopy();
	    	
//	    	alpha-beta 剪枝
	    	if (boardState_temp.colour == 1) {
	    		if(current > max_temp) {
	    			if (current > min_temp) {
	    				return current;
	    			}
	    			max_temp = current;
	    			
	    		}
	    		if(current > max) {
	    			max = current;
	    			if(deepth == 0)
	    				Move_temp = new Move(action_list.get(i).x,action_list.get(i).y);
	    		}
	    	}
	    	else {
	    		if(current < min_temp) {
	    			if (current < max_temp) {
	    				return current;
	    			}
	    			min_temp = current;
	    		}
	    		if(current < min) {
	    			min = current;
	    			if(deepth == 0)
	    				Move_temp = new Move(action_list.get(i).x,action_list.get(i).y);
	    		}
	    	}
	    }
	    
	    if (boardState_temp.colour == 1) {
	    	return max;
	    }
	    else {
	    	return min;
	    }
	}
	
  
    public static Move chooseMove(BoardState boardState){

    	int searchDepth= Othello.searchDepth;
    	deepth_max = searchDepth;
    	for(int i=0;i<8;i++) {
    		for (int j=0;j<8;j++) {
    			boardState.getContents(i,j);
    		}
    	}
    	

        ArrayList<Move> moves= boardState.getLegalMoves();
        
        if(moves.isEmpty()){
            return null;
        }
        Move_temp = moves.get(0);
        alphaBeta(-9999999, 9999999, boardState);
//        System.out.println(Move_temp.x);
//        System.out.println(Move_temp.y);
        return Move_temp;
    }
}
