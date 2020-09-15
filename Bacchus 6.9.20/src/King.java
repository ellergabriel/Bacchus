 
public class King extends Queen{
	
	/*********** CONSTRUCTORS ************/
	
	//DESCRIPTION:		Constructs a default King piece
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Default King piece is created on E file [4]
	public King() {
		super();
		this.value = 12;
		this.piece = 'K';
		this.horz = 4;
	}
	
	
	//DESCRIPTION:		Constructs King piece based on player color	
	//PRE-CONDITION:	Passed char must be 'w' or 'b'
	//POST-CONDITION:	King piece is constructed for corresponding side
	public King(char color) {
		this();//Constructs itself
		
		//Switch statement for different colors
		switch(color) {
		
		//White king
		case 'w':
			this.color = color;
			this.vert = 7; //E1
			break;
		case 'b':
			this.color = color;
			this.vert = 0; //E8
			break;
		} //End switch
	}
	
	
	//DESCRIPTION:		Constructs new King piece with identical data as passed King	
	//PRE-CONDITION:	Passed King piece must be fully initialized
	//POST-CONDITION:	New King piece is created with identical data
	public King(King original) {
		super(original);
	}
	
	//DESCRIPTION:		Creates new King object using passed PIeces object
	//PRE-CONDITION:	Passed Pieces object must be valid
	//POST-CONDITION:	New King piece is constructed
	public King(Pieces original) {
		super(original);
	}
	/************* METHODS *********************/
	//DESCRIPTION:		Returns isChecked boolean value of calling king
	//PRE-CONDITION:	Calling piece must be fully initialized
	//POST-CONDITION:	Returns isChecked boolean value
	@Override
	public boolean getIsChecked() {
		return this.isChecked;
	}
	
	
	/************* MOVEMENT METHODS ************/
	
	//DESCRIPTION:		Moves King piece to passed coordinate 
	//PRE-CONDITION:	Passed coordinate must be valid ints
	//POST-CONDITION:	Calling King piece is moved to coordinat	
	public boolean move(Pieces[][] board, int horz, int vert) {
		
		//Check for valid coordinates; if true, move King; else return false
		if (this.validCoordinate(board, horz, vert)) {
			Pieces.placePiece(board, this, horz, vert);
			return true;
		}
		else {
			return false;
		}
	}
	
	
	//DESCRIPTION:		Method checks if passed coordinate is valid for king; returns true if valid
	//PRE-CONDITION:	Passed coordinate must be valid ints
	//POST-CONDITION:	Returns true if King can move to coordinate
	public boolean validCoordinate(Pieces[][] board, int horz, int vert) {
		
		//Check if coordinates are within 1 space of king
		//Dummy variables
		int dHorz;
		int dVert;
		
		dHorz = this.horz - horz;
		dVert = this.vert - vert;
		
		//Check if either are > 1
		if ((Math.abs(dHorz) > 1) ||(Math.abs(dVert) > 1)) {
			return false;
		}
		//Use super/Queen validCoordinate to check coordinate
		else {
			
			//Check if coordinate is threatened by enemy; return false if threatened
			switch(this.color) {
			case 'w':
				if (board[horz][vert].getIsThreatenedByBlack()) {
					return false;
				}
				break;
			case 'b':
				if (board[horz][vert].getIsThreatenedByWhite()) {
					return false;
				}
				break;
			}
			
			if (super.validCoordinate(board, horz, vert)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	//DESCRIPTION:		Castles king based on passed coordinate
	//PRE-CONDITION:	Passed cooridinates and pieces must be appropriate
	//POST-CONDITION;	King is castled based on passed coordinate
	public void castle(Pieces[][] board, int horz) {
		
		//switch statement for different side castling
		switch(horz) {
		
		//Queen side castle
		case 2:
			if (Chess.canCastle(this, board, horz)) {
				//Move King to C file
				Pieces.placePiece(board, this, horz, this.vert);
				//Move A file rook to D file 
				Pieces.placePiece(board, board[0][this.vert], 3, this.vert);	
			} //End queen side case
		break;
		//King side castle
		case 6:
			if (Chess.canCastle(this, board, horz)) {
				//Move King to G file
				Pieces.placePiece(board, this, horz, this.vert);
				//Move H file rook to F file
				Pieces.placePiece(board, board[7][this.vert], 5, this.vert);
			}
		break;
		} // End King side case
		
	}
	
	/*********** STATIC/HELPER METHODS ***********/
	
	//DESCRIPTION:		Places both kings onto passed board array
	//PRE-CONDITION:	Passed board array must be initialized
	//POST-CONDITION:	Both kings are placed on the board
	public static void placeKing(Pieces[][] board) {
		board[4][0] = new King('b');
		board[4][7] = new King('w');
	}
	
	//DESCRIPTION:		Checks if King is checked with current board; updates all threats and updates King chekc
	//PRE-CONDITION:	Passed Pieces[][] array must be valid
	//POST-CONDITION:	Updates which squares are dangerous and boolean value of isChecked for King piece
	public static boolean checkUpdate(Pieces[][] board, char color) {
		
		//ints store where calling king is placed
		int dummyH = 0;
		int dummyV = 0;
		
		//Search for King and set check boolean to false
		//Check each piece for threatened squares and threatened pieces
		for (int horz = 0; horz < 8; horz++) {
			for (int vert = 0; vert < 8; vert++) {
				if ((board[horz][vert].getPiece() == 'K') && (board[horz][vert].getColor() == color)) {
					//Store coordinates for later use
					dummyH = horz;
					dummyV = vert;
					//Set check to false for calling color king
					board[horz][vert].setIsChecked(false);
				}
			}
		}
		
		//Check all squares for threats
		Chess.threatenUpdate(board);
		
		//Return boolean isChecked from found king
		return board[dummyH][dummyV].getIsChecked();
	}
	
}
