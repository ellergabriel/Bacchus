import java.util.*;
public class Bishop extends Queen {

	/************* CONSTRUCTORS ***********/
	
	//DESCRIPTION:		Constructs default Bishop object
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Bishop object is instantiated with default values
	public Bishop() {
		super();
		this.value = 3.2;
		this.piece = 'B';
	}
	
	
	
	//DESCRIPTION:		Constructs Bishop object with piece color passed in
	//PRE-CONDITION:	Passed in char must be 'w' or 'b'
	//POST-CONDITION:	Bishop is constructed with correct color
	public Bishop(char color) {
		this();
		
		switch(color) {
		
			case 'w': 
				this.color = 'w';
				break;
			case 'b':
				this.color = 'b';
				break;
		}
	}
	
	
	
	//DESCRIPTION:		Creates copy Bishop of passed in Bishop	
	//PRE-CONDITION:	Passed object must be Bishop object and fully initialized
	//POST-CONDITION:	New Bishop object is created with identical data
	public Bishop(Bishop original) {
		super(original);
	}
	
	//DESCRIPTION:		Creates new Bishop object usig passed PIeces object
	//PRE-CONDITION:	Passed Pieces object must be valid
	//POST-CONDITION:	New Bishop piece is constructed
		public Bishop(Pieces original) {
			super(original);
		}
	
	/*********** MOVEMENT METHODS *****************/
	
	//DESCRIPTION:		Moves calling Bishop piece to passed coordinate	
	//PRE-CONDITION:	Passed coordinate must be valid int
	//POST-CONDITION:	Bishop piece is moved to passed coordinate
	public boolean move(Pieces[][] board, int horz, int vert) {
		//Use canDiagonal to check if coordinate is true
		if(this.validCoordinate(board, horz, vert)) {
			Pieces.placePiece(board, this, horz, vert);
			return true;
		}
		else {
			return false;
		}
	}
	
	
	//DESCRIPTION:		Overrides canLinear method inherited from Queen class
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Returns false as Bishop cannot move linear
	public boolean canLinear(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	
	//DESCRIPTION:		Overrides valid coordinate method inherited from Queen class	
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Returns true if coordinate is valid; else false
	public boolean validCoordinate(Pieces[][] board, int horz, int vert) {
		//If coordinate is valid and bishop is not pinned horz or vertically, can move
		if (this.canDiagonal(board, horz, vert) && (!(this.isPinnedHorz) )
				&& (!(this.isPinnedVert))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	//DESCRIPTION:		Returns number of squares threatened by calling Bishop
	//PRE-CONDITION:	Bishop must be fully initialized
	//POST-CONDITION:	Returns the number of squares threatened by the bishop
	public int threatenedSquares(Pieces[][] board) {
		
		//Uses method defined in Queen super class
		return super.threatenedSquaresDiagonal(board);
				
	}
	
	//DESCRIPTION:		Returns number of pieces threatened by calling bishop
	//PRE-CONDITION:	Bishop must be fully initialized
	//POST-CONDITION:	Returns number of pieces threatened
	public int threatenedPieces(Pieces[][] board) {
		//Reset possibleAttacks before execution
		this.possibleAttacks = new ArrayList<Pieces>();
		//Use inherited method from Queen class
		int threats;
		threats = super.threatenedPiecesDiagonal(board);
		
		return threats;
	}
	
	/********** HELPER/STATIC METHODS *************/
	
	//DESCRIPTION:		Places all 4 bishops on passed board
	//PRE-CONDITION:	board array must be initialized
	//POST-CONDITION:	All 4 bishops are placed on the board
	public static void placeBishop(Pieces[][] board) {
		
		//Place white bishops 
		
		//Queen side bishop C1
		board[2][0] = new Bishop('b'); 
		board[2][0].setHorz(2);
		board[2][0].setVert(0);
		
		//King side bishop G1
		board[5][0] = new Bishop('b');
		board[5][0].setHorz(5);
		board[5][0].setVert(0);
		
		//Place black bishops
		
		//Queen side Bishop C8 (2,7)
		board[2][7] = new Bishop('w');
		board[2][7].setHorz(2);
		board[2][7].setVert(7);
		
		//King side bishop
		board[5][7] = new Bishop('w');
		board[5][7].setHorz(5);
		board[5][7].setVert(7);
	}
	
	
	
	
	
	
	
	
}
