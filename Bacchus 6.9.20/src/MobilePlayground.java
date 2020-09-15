import java.util.*;
import java.util.InputMismatchException;

public class MobilePlayground {

	public static int turnCount = 0;
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		//Scanner creation
		Scanner input = new Scanner(System.in);
		
		//Board creation
		Pieces[][] board = new Pieces[8][8];
		
		//String array stores which moves have been made
		String moveList = "E4";
		
		//System.out.println(computer.getGameNums());
		
		Pawn.placePawn(board);
		
		//Knight placement test
		Knight.placeKnight(board);
		//Bishop placement test
		Bishop.placeBishop(board);
		//Queen placement test
		Queen.placeQueen(board);
		//Rook placement test
		Rook.placeRook(board);
		//King placement test
		King.placeKing(board);
		
		Squares.fillSquares(board);
		
		//Bacchus computer = new Bacchus(board, 'w');
		//computer.updateGameCheck(moveList);
		
		//board[3][1] = new Squares(true);
		//board[3][6] = new Squares(true);
		
		/*
		board[3][3] = new Pawn('b');
		board[1][3] = new Pawn('b');
		board[1][7].move(board, 2, 5);
		System.out.println(board[2][5].threatenedPieces(board));
		*/
		
		Bacchus computer = new Bacchus(board, 'w');
		Bacchus other = new Bacchus(board, 'b');
		//Chess.threatenUpdate(board);
		
		
		//computer.duplicateBoard(board, computer.testBoard);
		//computer.opening(board);
		
		//Bishop to G7
		//board[5][0].move(board, 6, 1);
		
		//computer.opening(board);
		//other.opening(board);
		//other.movePiece(board);
		
		int horzPiece, vertPiece, horzMove, vertMove;
		String piece, move;
		
		do {
			System.out.printf("Piece to move: ");
			piece = input.nextLine();
			horzPiece = Pieces.parseHorz(piece.toLowerCase().charAt(0));
			vertPiece = Pieces.parseVert(piece.charAt(1));
			
			System.out.print("Piece will go to: ");
			move = input.nextLine();
			horzMove = Pieces.parseHorz(move.toLowerCase().charAt(0));
			vertMove = Pieces.parseVert(move.charAt(1));
			
			board[horzPiece][vertPiece].move(board, horzMove, vertMove);
			
			//Bacchus move
			other.movePiece(board);
			
			Pieces.printBoardWhite(board);
			System.out.println();
		} while (true);
		
		
		//input.close();
	}
}

