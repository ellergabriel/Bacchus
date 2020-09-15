
public class Playground {

	public static void main(String[] args) {
		
		Pieces[][] board = new Pieces[8][8];
		
		//Pawn.placePawn(board);
		//Knight.placeKnight(board);
		//Bishop.placeBishop(board);
		Rook.placeRook(board);
		Queen.placeQueen(board);
		King.placeKing(board);
		//Squares.fillSquares(board);
		
		//System.out.println(board[0][7]);
		
		
		//Pieces.printBoard(board);
		
		System.out.println(board[0][7].threatenedSquares(board));
		board[3][0].move(board, 3, 6);
		Squares.fillSquares(board);
		Pieces.printBoardWhite(board);
		
		}

}
