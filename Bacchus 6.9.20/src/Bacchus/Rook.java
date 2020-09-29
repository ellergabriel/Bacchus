package Bacchus;
import java.util.*;
public class Rook extends Queen {

	/************ CONSTRUCTORS *******************/
	
	//DESCRIPTION:		Constructs Rook object with default values	
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	New rook object is instantiated with default values
	public Rook() {
		super();
		this.value = 7;
		this.piece = 'R';
	}
	
	
	//DESCRIPTION:		Constructs Rook object with passed char color	
	//PRE-CONDITION:	Passed char must be 'w' or 'b'
	//POST-CONDITION:	New Rook object is instantiated with passed color
	public Rook(char color) {
		this();
		this.color = color;
	}
	
	
	//DESCRIPTION:		Constructs Rook object with identical data as passed Rook
	//PRE-CONDITION:	Passed in Rook must be fully instantiated
	//POST-CONDITION:	New Rook object is created with same data as passed Rook
	public Rook(Rook original) {
		super(original);
	}
	
	//DESCRIPTION:		Creates new Rook object usig passed PIeces object
	//PRE-CONDITION:	Passed Pieces object must be valid
	//POST-CONDITION:	New Rook piece is constructed
	public Rook(Pieces original) {
		super(original);
	}
	
	/********** MOVEMENT METHODS ************/
	
	//DESCRIPTION:		Moves calling piece to passed coordinate if coordinate is valid; else, returns false
	//PRE-CONDITION:	Passed ints must be 0 - 7
	//POST-CONDITION:	Moves piece and returns true if coordinate is valid; else, return false
	public boolean move(Pieces[][] board, int horz, int vert) {
		//If coordinate is not valid, return false
		if (!(this.validCoordinate(board, horz, vert))) {
			return false;
		}
		//If coordinate is valid, move piece and return true
		else {
			Pieces.placePiece(board, this, horz, vert);
			return true;
		}
	}
	
	//DESCRIPTION:		Overrides canDiagonal method inherited from Queen class	
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Returns false
	public boolean canDiagonal(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	//DESCRIPTION:		Overrides validCoordinate method inherited from Queen class	
	//PRE-CONDITION:	Passed values must be within range and valid ints
	//POST-CONDITION:	Returns true if coordinate is valid; else false
	public boolean validCoordinate(Pieces[][] board, int horz, int vert) {
		
		//If Rook is pinned diagonally, cannot move at all
		if(this.isPinnedDiagonal) {
			return false;
		}
		//If passed horz is same as calling piece horz, check if pinned vertically
		else if((this.horz == horz) && (this.isPinnedVert)) {
			return false;
		}
		//If passed vert is same, then check if pinned horizontally
		else if ((this.vert == vert) && (this.isPinnedHorz)) {
			return false;
		}
		
		if (this.canLinear(board, horz, vert)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//DESCRIPTION:		Returns number of threatened squares; method inherited from Queen class
	//PRE-CONDITION:	Calling rook must be fully initialized
	//POST-CONDITION:	Returns number of squares threatened by calling Rook
	public int threatenedSquares(Pieces[][] board) {
		
		//Use super method as already defined
		return super.threatenedSquaresLinear(board);
	}

	//DESCRIPTION:		Overrides super class method threatenedPieces from Queen class
	//PRE-CONDITION:	Calling rook must be initialized
	//POST-CONDITION:	Returns number of pieces threatened by calling Rook
	public int threatenedPieces(Pieces[][] board) {
		
		//Reset possibleAttacks before execution
		this.possibleAttacks = new ArrayList<Pieces>();
		//Use super class threatnedPiecesLinear for method
		return super.threatenedPiecesLinear(board);
	}
	

	/******** STATIC/HELPER METHODS *********/
	//DESCRIPTION:		Places all 4 rook objects on board
	//PRE-CONDITION:	Passed board array must be initialized
	//POST-CONDITION:	All 4 rooks are placed on board	
	public static void placeRook(Pieces[][] board) {
		
		//White rooks (0,0) (7,0)
		
		//Queen side rook
		board[0][0] = new Rook('b');
		board[0][0].setHorz(0);
		board[0][0].setVert(0);
		
		//King side rook
		board[7][0] = new Rook('b');
		board[7][0].setHorz(7);
		board[7][0].setVert(0);
		
		
		//Black rooks (0,7) (7,7)
		//Queen side rook
		board[0][7] = new Rook('w');
		board[0][7].setHorz(0);
		board[0][7].setVert(7);
		
		//King side rook 
		board[7][7] = new Rook('w');
		board[7][7].setHorz(7);
		board[7][7].setVert(7);
		
	}
	
	
	
}
