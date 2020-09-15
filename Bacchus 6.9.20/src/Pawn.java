import java.util.*;
/* UML Class diagram
--------------------
 * Pawn *
--------------------  
 * 
--------------------
 * + Pawn()
 * + Pawn(color : char, horz : int, vert : int, isTaken : boolean)
 * 
*/

public class Pawn extends Pieces {
	
	
	
	/************** CONSTRUCTORS *****************/
	
	//DESCRIPTION:		Constructs Pawn object with default values
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Pawn object is constructed with default values
	public Pawn() {
		super();
		this.piece = 'P';
		this.vert = 1;
		this.value = 1;
	}
	
	
	
	//DESCRIPTION:		Constructs Pawn object with passed values
	//PRE-CONDITION:	Passed values must be valid
	//POST-CONDITION:	Pawn object is created with passed values
	public Pawn(char color) {
		this();
		this.color = color;
		
		//Switch statements
		switch(color) {
		
		case 'b':
			this.vert = 1;
			break;
		case 'w':
			this.vert = 6;
			break;
		}
	}
	
	
	//DESCRIPTION:		Construct pawn object with same values as passed object
	//PRE-CONDITION:	Passed object must be fully initialized
	//POST-CONDITION:	Pawn object is created with same values
	public Pawn(Pawn original){
		super(original);
	}
	
	
	//DESCRIPTION:		Creates new Pawn object usig passed PIeces object
	//PRE-CONDITION:	Passed Pieces object must be valid
	//POST-CONDITION:	New Pawn piece is constructed
		public Pawn(Pieces original) {
			super(original);
		}
	
	/************ MOVEMENT METHODS **************/
	
	
	
	//DESCRIPTION:		Checks to see if pawn can move
	//PRE-CONDITION:	Pawn object must be instantiated and vert != 0 or 7
	//POST-CONDITION:	Returns true if space ahead is unoccupied; else false
	public boolean canMove(Pieces[][] board) {
		
		//Switch statement for different colors
		switch(this.color) {
			
			//White pawn check
			case 'b':
				
				//Checks next space forward is empty; if not empty, return false; else true
				if (!(Pieces.isEmpty(board, this.horz, this.vert + 1))) {
					return false;
				}
				else {
					return true;
				}
				
			//Black pawn check
			case 'w':
				
				//Checks next space forward isempty; if not empty, return false; else true
				if (!(Pieces.isEmpty(board, this.horz, this.vert - 1))) {
					return false;
				}
				else {
					return true;
				}
			}
		
		//Return false  in case of bug
		return false;
	}
	
	
	
	
	
	//DESCRIPTION:		Moves pawn piece 1 space forward
	//PRE-CONDITION:	Pawn object must be intialized with valid horz and vert values
	//POST-CONDITION:	Pawn object is moved forward on board
	public boolean move(Pieces[][] board, int horz, int vert)
	{
		
		//Checks to see if piece can move
		if (!(this.validCoordinate(board, horz, vert))) {
			return false;
		}
		
		//Different color checks
		if(this.color == 'b') {
			//Place piece; updates vert coordinate of pawn as well
			Pieces.placePiece(board, this, horz, vert);
			
			//If pawn has reached 4th rank (board[][4]), set calling pawn with canPass
			if (this.vert == 4) {
				this.canPass = true;
			}
			
			//If pawn has moved past 4th rank (board[][5]), set canPass as false
			else {
				this.canPass = false;
			}
			
			return true;
		}
		
		//Black moves
		else if (this.color == 'w'){
			
			//Place piece; udpates vert coordinate of calling pawn using passed vert values
			Pieces.placePiece(board, this, horz, vert);
			
			//If white pawn has hit 5th rank (board[][3]), set canPass as true
			if (this.vert == 3) {
				this.canPass = true;
			}
			else {
				this.canPass = false;
			}
		
			
			return true;
		}
		
		//Return false in case of bug
		else {
			return false;
		}
	}
	
	
	
	
	//DESCRIPTION:		Checks to see if pawn can move two spaces
	//PRE-CONDITION:	Pawn object must be fully instantiated
	//POST-CONDITION:	Returns true if pawn can move 2 spaces; else false
	public boolean canDoubleMove(Pieces[][] board) {
		//Checks black pawn
		if (this.color == 'b') {
			//Checks Black pawns in rank 7 (board[][1])
			if (this.vert != 1) {
				return false;
			}
			//Checks if 2 spaces ahead is occupied
			else if (Pieces.isEmpty(board, this.horz, this.vert + 2)) {
				return true;
			}
			else {
				return false;
			}
		}
		//White pawn check
		else {
			//Checks white pawns on rank 2 (board[][6])
			if (this.vert != 6) {
				return false;
			}
			//Checks if 2 spaces ahead is occupied
			else if (Pieces.isEmpty(board, this.horz, this.vert - 2)) {
				return true;
			}
			else {
				return false;
			}
		}
		
	}
	
	
	
	//DESCRIPTION:		Moves opening pawn piece 2 spaces
	//PRE-CONDITION:	Pawn must be intialized with a vert value of 1
	//POST-CONDITION:	Pawn object is moved two spaces forward
	public boolean doubleMove(Pieces[][] board)
	{
		//Boolean check
		if (!(this.canDoubleMove(board))) {
			return false;
		}
		
		//2 square move for white
		if(this.color == 'b') {
			//Adjust vertical coordinate
			this.vert += 2;
			
			//Remove old pawn from previous square
			board[this.horz][this.vert - 2] = null;
			
			//Add moved pawn to board array via copy
			board[this.horz][this.vert] = new Pawn(this);
		}
		
		//2 square move for black
		else {
			//Adjust vertical coordinate
			this.vert -= 2;
			
			//Remove old pawn from board
			board[this.horz][this.vert + 2] = null;
			
			//Add moved pawn to board
			board[this.horz][this.vert] = new Pawn(this);
			
		}
		
		//Check for adjacent pieces after double move has been made
		if (Pawn.adjacent(board, this, this.horz - 1)) {
			this.canPass = true;
		}
		if (Pawn.adjacent(board, this, this.horz + 1)) {
			this.canPass = true;
		}
		
		return false;
	}

	
	
	//DESCRIPTION:		Checks to see if pawn has available attacks
	//PRE-CONDITION:	Pawn object must be fully initialized
	//POST-CONDITION:	Returns true if pawn can attack; else false
	public boolean validCoordinate(Pieces[][] board, int horz, int vert) {

		//If pawn is pinned horizontally, cannot move; return false
		if ((this.isPinnedHorz) || (this.isPinnedDiagonal)) {
			return false;
		}
		
		//Switch statement for diff colors
		switch(this.color) {
		//White pawn check
		case 'b':
			//A file check
			if (this.horz == 0) {
				if (this.canPass) {
					//Check if B file pawn if canPass; if true, return true
					if ((board[this.horz + 1][this.vert].getCanPass()) && (horz == this.horz + 1)
							&& (vert == this.vert + 1)){
						return true;
					}
				}
				
				//Single move check
				else if (vert == this.vert + 1) {
					
					//Check for straight forward movement; if piece is pinned diagonally, vertical movement is not possible
					if ((this.horz == horz) && ((Pieces.isEmpty(board, horz, vert)) && (!(this.isPinnedDiagonal)))) {
						return true;
					}
					//Check if passed coordinate is to take on B file
					else if ((horz == this.horz + 1) && (Pieces.isEnemyPiece(board, this, horz, vert))){
						return true;
					}
					
				}
				//Double move check 
				else if (vert == this.vert + 2) {
					
					//Check if double move is possible; if possible, return true
					return this.canDoubleMove(board);
				}
				else {
					return false;
				}
			}
			
			//H file check
			else if (this.horz == 7) {
				if (this.canPass) {
					//Check if B file pawn if canPass; if true, return true
					if ((board[this.horz - 1][this.vert].getCanPass()) && (horz == this.horz - 1)
							&& (vert == this.vert + 1)){
						return true;
					}
				}
				
				else if (vert == this.vert + 1) {
					
					//Check for straight forward movement
					if ((this.horz == horz) && ((Pieces.isEmpty(board, horz, vert)) && (!(this.isPinnedDiagonal)))) {
						return true;
					}
					//Check if passed coordinate is to take on B file
					else if ((horz == this.horz + 1) && ((Pieces.isEnemyPiece(board, this, horz, vert)))) {
						return true;
					}
				}
				
				//Double move check 
				else if (vert == this.vert + 2) {
					//Check if double move is possible; if possible, return true
					return this.canDoubleMove(board);
				}
				else {
					return false;
				}
			}
			//Check for all other files for black
			else {
				//En passant check
				if (this.canPass) {
					//Check if B file pawn if canPass; if true, return true
					if ((board[this.horz + 1][this.vert].getCanPass()) && (horz == this.horz + 1)
							&& (vert == this.vert + 1)){
						return true;
					}
					else if ((board[this.horz - 1][this.vert].getCanPass()) && (horz == this.horz - 1)
							&& (vert == this.vert + 1)) {
						return true;
					}
				}
				//Right file check
				else if ((horz == this.horz + 1) && (vert == this.vert + 1)) {
					//If taking right file is valid, return true; else, false
					if (Pieces.isEnemyPiece(board, this, horz, vert)) {
						return true;
					}
					else {
						return false;
					}
				}
				//Left file check
				else if ((horz == this.horz - 1) && (vert == this.vert + 1)) {
					if (Pieces.isEnemyPiece(board, this, horz, vert)) {
						return true;
					}
					else {
						return false;
					}
				}
				//Straight forward check
				else if (((this.horz == horz) && (vert == this.vert + 1)) && (!(this.isPinnedDiagonal))) {
					return true;
				}
				//Double move check 
				else if ((this.horz == horz) && (vert == this.vert + 2)) {
					return this.canDoubleMove(board);
				}
				else {
					return false;
				}
			}			
							
			
		
		//Black pawn check
		case 'w':
			//A file check
			if (this.horz == 0) {
				//Right file en passant check
				if (this.canPass) {
					//Check if B file pawn if canPass; if true, return true
					if ((board[this.horz + 1][this.vert].getCanPass()) && (horz == this.horz + 1)
							&& (vert == this.vert - 1)){
						return true;
					}
				}
				//Single move check
				else if (vert == this.vert - 1) {
					
					//Check for straight forward movement
					if ((this.horz == horz) && ((Pieces.isEmpty(board, horz, vert)))) {
						return true;
					}
					//Check if passed coordinate is to take on B file
					else if ((horz == this.horz + 1) && (Pieces.isEnemyPiece(board, this, horz, vert))) {
						return true;
					}
				}
				
				//Double move check 
				else if (vert == this.vert - 2) {
					
					//Check if double move is possible; if possible, return true
					return this.canDoubleMove(board);
				}
				else {
					return false;
				}
			}
			
			//H file check
			else if (this.horz == 7) {
				//Left file en passant check
				if (this.canPass) {
					//Check if B file pawn if canPass; if true, return true
					if ((board[this.horz - 1][this.vert].getCanPass()) && (horz == this.horz - 1)
							&& (vert == this.vert - 1)){
						return true;
					}
				}
				else if (vert == this.vert - 1) {
					
					//Check for straight forward movement
					if ((this.horz == horz) && ((Pieces.isEmpty(board, horz, vert)))) {
						return true;
					}
					//Check if passed coordinate is to take on B file
					else if ((horz == this.horz - 1) && ((Pieces.isEnemyPiece(board, this, horz, vert)))) {
						return true;
					}
				}
				
				//Double move check 
				else if (vert == this.vert - 2) {
					
					//Check if double move is possible; if possible, return true
					return this.canDoubleMove(board);
				}
				else {
					return false;
				}
			}
			//Check for all other files for black
			else {
				//En passant check on both sides
				if (this.canPass) {
					//Check if B file pawn if canPass; if true, return true
					if ((board[this.horz + 1][this.vert].getCanPass()) && (horz == this.horz + 1)
							&& (vert == this.vert - 1) && Pieces.isEnemyPiece(board, this, this.horz - 1, this.vert - 1)) {
						return true;
					}
				}
				//Right file check
				else if ((horz == this.horz + 1) && (vert == this.vert - 1) && (Pieces.isEnemyPiece(board, this, this.horz + 1, this.vert - 1))) {
					return true;
				}
				//Left file check
				else if ((horz == this.horz - 1) && (vert == this.vert - 1) && (Pieces.isEnemyPiece(board, this, this.horz + 1, this.vert - 1))) {
					return true;
				}
				//Straight forward check
				else if ((this.horz == horz) && (vert == this.vert - 1) && (!(this.isPinnedDiagonal))) {
					return true;
				}
				//Double move check 
				else if ((this.horz == horz) && (vert == this.vert - 2)) {
					return this.canDoubleMove(board);
				}
				else {
					return false;
				}
			}			
		}
		
		return false;
	}
	
	
	
	
	//DESCRIPTION:		Pawn attacks available piece
	//PRE-CONDITION:	Pawn object must be fully instantiated
	//POST-CONDITION:	Other piece is captured and board is adjusted
	public boolean attack(Pieces[][] board, int horz, int vert)
	{
		//Checks if attack can be made; returns false if attack cant be made
		if(!(this.canAttack(board, horz, vert))) {
			return false;
		}
		else {
			//Use static function to move pawn
			Pieces.placePiece(board, this, horz, vert);
			return true;
		}
	}
	
	
	
	//DESCRIPTION:		Checks which squares calling pawn threatens
	//PRE-CONDITION:	Calling pawn must be initialized
	//POST-CONDITION:	Returns number of threatened squares and marks each square accordingly
	public int threatenedSquares(Pieces[][] board) {
		
		int threat = 0;
		
		//If pawn is pinned vertically, return 0 as pawn cannot move
		if ((this.isPinnedVert)) {
			return 0;
		}
		
		//Switch statement for both colors
		switch(this.color) {
		
		//Black pawn case
		case 'b':
			
			//A file check
			if (this.horz == 0) {
				
				//Check if friendly piece is in B file; mark as not hanging if true and return 0
				if (!(Pieces.isDifferentColor(this, board[1][this.vert + 1]))) {
					board[1][this.vert + 1].setIsHanging(false);
					return threat;
				}
				
				//If space is empty, mark square as threatened; increment and retrun threat
				else if (Pieces.isEmpty(board, 1, this.vert + 1)) {
					board[1][this.vert + 1] = new Squares(true);
					board[1][this.vert + 1].setIsThreatenedByBlack(true);
					threat++;
					return threat;
				}
			}
			
			//H file check
			else if (this.horz == 7) {
				
				//Check if friendly piece is in G file; mark as not hanging if true and return -
				if (!(Pieces.isDifferentColor(this, board[6][this.vert + 1]))) {
					board[6][this.vert + 1].setIsHanging(false);
					return threat;
				}
				
				//If space is empty, mark square as threatened; increment and return threat
				else if (Pieces.isEmpty(board, 6, this.vert + 1)) {
					board[6][this.vert + 1] = new Squares(true);
					board[6][this.vert + 1].setIsThreatenedByBlack(true);
					threat++;
					return threat;
				}
			}
			
			//B - G file check
			else {
				
				//A file direction check
				//Check if A file has friendly; if true, mark as not hanging
				if (!(Pieces.isDifferentColor(this, board[this.horz - 1][this.vert + 1]))) {
					board[this.horz - 1][this.vert + 1].setIsHanging(false);
				}
				
				//Check H file has friendly; if true, mark as not hanging
				if (!(Pieces.isDifferentColor(this, board[this.horz + 1][this.vert + 1]))) {
					board[this.horz + 1][this.vert + 1].setIsHanging(false);
				}
				
				//Check if A file is empty; if true, increment threat and mark square as threatened
				if (Pieces.isEmpty(board, this.horz - 1, this.vert + 1)) {
					board[this.horz - 1][this.vert + 1] = new Squares(true);
					board[this.horz - 1][this.vert + 1].setIsThreatenedByBlack(true);
					threat++;
				}
				
				//Check if H file is empty; if true, increment threat and mark square as threatened
				if (Pieces.isEmpty(board, this.horz + 1, this.vert + 1)) {
					board[this.horz + 1][this.vert + 1] = new Squares(true);
					board[this.horz + 1][this.vert + 1].setIsThreatenedByBlack(true);
					threat++;
				}
			}
			return threat;
			//End Black pawn case
			
			
			//White pawn case 
		case 'w':
			//A file check
			if (this.horz == 0) {
				
				//Check if friendly piece is in B file; mark as not hanging if true adn return 0
				if (!(Pieces.isDifferentColor(this, board[1][this.vert - 1]))) {
					board[1][this.vert - 1].setIsHanging(false);
					return threat;
				}
				
				//If space is empty, mark square as threatened; increment and return threat
				else if (Pieces.isEmpty(board, 1, this.vert - 1)) {
					board[1][this.vert - 1] = new Squares(true);
					board[1][this.vert - 1].setIsThreatenedByWhite(true);
					threat++;
					return threat;
				}
			}
			
			//H file check
			else if (this.horz == 7) {
				
				//Check if friendly piece is in G file; mark as not hanging if true and return -
				if (!(Pieces.isDifferentColor(this, board[6][this.vert - 1]))) {
					board[6][this.vert - 1].setIsHanging(false);
					return threat;
				}
				
				//If space is empty, mark square as threatened; increment and return threat
				else if (Pieces.isEmpty(board, 6, this.vert - 1)) {
					board[6][this.vert - 1] = new Squares(true);
					board[6][this.vert - 1].setIsThreatenedByWhite(true);
					threat++;
					return threat;
				}
			}
			
			//B - G file check
			else {
				
				//A file direction check
				//Check if A file has friendly; if true, mark as not hanging
				if (!(Pieces.isDifferentColor(this, board[this.horz - 1][this.vert - 1]))) {
					board[this.horz - 1][this.vert - 1].setIsHanging(false);
				}
				
				//Check H file has friendly; if true, mark as not hanging
				if (!(Pieces.isDifferentColor(this, board[this.horz + 1][this.vert - 1]))) {
					board[this.horz + 1][this.vert - 1].setIsHanging(false);
				}
				
				//Check if A file is empty; if true, increment threat and mark square as threatened
				if (Pieces.isEmpty(board, this.horz - 1, this.vert - 1)) {
					board[this.horz - 1][this.vert - 1] = new Squares(true);
					board[this.horz - 1][this.vert - 1].setIsThreatenedByWhite(true);
					threat++;
				}
				
				//Check if H file is empty; if true, increment threat and mark square as threatened
				if (Pieces.isEmpty(board, this.horz + 1, this.vert - 1)) {
					board[this.horz + 1][this.vert - 1] = new Squares(true);
					board[this.horz + 1][this.vert - 1].setIsThreatenedByWhite(true);
					threat++;
				}
			}
			return threat;
		}
		
		return threat;
	}
	
	
	//DESCRIPTION:		Checks if pawn is threatening any pieces and returns number of threatened pieces
	//PRE-CONDITION:	Passed board must be fully initialized and all pieces constructed
	//POST-CONDITION:	Returns number of pieces threatened, as well as marking threatened pieces with boolean flag
	public int threatenedPieces(Pieces[][] board) {
		int threat = 0;
		
		//Reset possibleAttacks before each execution
		this.possibleAttacks = new ArrayList<Pieces>();
		
		//A file check
		if (this.horz == 0) {
			//Switch statement for each color
			switch(this.color) {
			
			case 'w':
				//If piece is different color and square is not empty, mark as threatened and increment threat
				if((Pieces.isDifferentColor(this, board[1][this.vert - 1]) &&
				(!(Pieces.isEmpty(board, 1, this.vert - 1))))) {
					//Mark attacked piece as threatened
					board[1][this.vert - 1].setIsThreatenedByWhite(true);
					this.possibleAttacks.add(board[1][this.vert - 1]);
					threat += board[1][this.vert - 1].getValue();
					return threat;
				}
				//Else if piece is protecting friendly piece, mark piece as not hanging and return 0
				else if(!(Pieces.isDifferentColor(this, board[1][this.vert - 1]))) {
					//Mark square as not hanging
					board[1][this.vert - 1].setIsHanging(false);
					return 0;
				}
				break;//End white A file case
			case 'b':
				//Check for different color piece on non empty square
				if ((Pieces.isDifferentColor(this, board[1][this.vert + 1]) &&
				(!(Pieces.isEmpty(board, 1, this.vert + 1))))) {
					//Mark attacked piece as threatened
					board[1][this.vert + 1].setIsThreatenedByBlack(true);
					threat += board[1][this.vert + 1].getValue();
					this.possibleAttacks.add(board[1][this.vert + 1]);
					return threat;
				}
				//Else if piece is protecting friendly piece, mark piece as not hanging and return 0
				else if (!(Pieces.isDifferentColor(this, board[1][this.vert + 1]))) {
					//Mark square as not hanging\
					board[1][this.vert + 1].setIsHanging(false);
				}
				break;//End black A file case
			} //End switch statement
		}//End A file check
		else if (this.horz == 7) {
			//Switch statement for each color
			switch(this.color) {
			
			case 'w':
				//If piece is different color and square is not empty, mark as threatened and increment threat
				if((Pieces.isDifferentColor(this, board[6][this.vert - 1]) &&
				(!(Pieces.isEmpty(board, 6, this.vert - 1))))) {
					//Mark attacked piece as threatened
					board[6][this.vert - 1].setIsThreatenedByWhite(true);
					threat += board[6][this.vert - 1].getValue();
					this.possibleAttacks.add(board[6][this.vert - 1]);
					return 1;
				}
				//Else if piece is protecting friendly piece, mark piece as not hanging and return 0
				else if(!(Pieces.isDifferentColor(this, board[6][this.vert - 1]))) {
					//Mark square as not hanging
					board[6][this.vert - 1].setIsHanging(false);
					return 0;
				}
				break;//End white H file case
				
			case 'b':
				//Check for different color piece on non empty square
				if ((Pieces.isDifferentColor(this, board[6][this.vert + 1]) &&
				(!(Pieces.isEmpty(board, 6, this.vert + 1))))) {
					//Mark attacked piece as threatened
					board[6][this.vert + 1].setIsThreatenedByBlack(true);
					threat += board[6][this.vert + 1].getValue();
					this.possibleAttacks.add(board[6][this.vert - 1]);
					return threat;
				}
				//Else if piece is protecting friendly piece, mark piece as not hanging and return 0
				else if (!(Pieces.isDifferentColor(this, board[6][this.vert + 1]))) {
					//Mark square as not hanging\
					board[6][this.vert + 1].setIsHanging(false);
					return 0;
				}
				break;//End black H file case
			
			} //End switch statement
		}//End H file Check
		
		//Check all other pawns between A and H
		else {
			//Switch statement for diff colors
			switch(this.color) {
			
			//White pawn case
			case 'w':
				//Check A file direction 7 -> 0
				//If piece is different color and square is not empty, mark as threatened and increment threat
				if((Pieces.isDifferentColor(this, board[this.horz - 1][this.vert - 1]) &&
				(!(Pieces.isEmpty(board, this.horz - 1, this.vert - 1))))) {
					//Mark attacked piece as threatened
					board[this.horz - 1][this.vert - 1].setIsThreatenedByWhite(true);
					threat += board[this.horz - 1][this.vert - 1].getValue();
					this.possibleAttacks.add(board[this.horz - 1][this.vert - 1]);
				}
				//Else if piece is protecting friendly piece, mark piece as not hanging and return 0
				else if(!(Pieces.isDifferentColor(this, board[this.horz - 1][this.vert - 1]))) {
					//Mark square as not hanging
					board[this.horz - 1][this.vert - 1].setIsHanging(false);
				} //End A file direction check 
				
				//Check H file direction 0 -> 7
				//If piece is different color and square is not empty, mark as threatened and increment threat
				if((Pieces.isDifferentColor(this, board[this.horz + 1][this.vert - 1]) &&
				(!(Pieces.isEmpty(board, this.horz + 1, this.vert - 1))))) {
					//Mark attacked piece as threatened
					board[this.horz + 1][this.vert - 1].setIsThreatenedByWhite(true);
					threat += board[this.horz + 1][this.vert - 1].getValue();
					this.possibleAttacks.add(board[this.horz + 1][this.vert - 1]);
				}
				//Else if piece is protecting friendly piece, mark piece as not hanging and return 0
				else if(!(Pieces.isDifferentColor(this, board[this.horz - 1][this.vert - 1]))) {
					//Mark square as not hanging
					board[this.horz + 1][this.vert - 1].setIsHanging(false);
				} //End H file direction check
				break;
			
				//Black pawn case
			case 'b':
				//Check A file direction 7 -> 0
				//If piece is different color and square is not empty, mark as threatened and increment threat
				if((Pieces.isDifferentColor(this, board[this.horz - 1][this.vert + 1]) &&
				(!(Pieces.isEmpty(board, this.horz - 1, this.vert + 1))))) {
					//Mark attacked piece as threatened
					board[this.horz - 1][this.vert + 1].setIsThreatenedByBlack(true);
					threat += board[this.horz - 1][this.vert + 1].getValue();
					this.possibleAttacks.add(board[this.horz - 1][this.vert + 1]);
				}
				//Else if piece is protecting friendly piece, mark piece as not hanging and return 0
				else if(!(Pieces.isDifferentColor(this, board[this.horz - 1][this.vert + 1]))) {
					//Mark square as not hanging
					board[this.horz - 1][this.vert + 1].setIsHanging(false);
				} //End A file direction check 
				
				//Check H file direction 0 -> 7
				//If piece is different color and square is not empty, mark as threatened and increment threat
				if((Pieces.isDifferentColor(this, board[this.horz + 1][this.vert + 1]) &&
				(!(Pieces.isEmpty(board, this.horz + 1, this.vert + 1))))) {
					//Mark attacked piece as threatened
					board[this.horz + 1][this.vert + 1].setIsThreatenedByBlack(true);
					threat += board[this.horz + 1][this.vert + 1].getValue();
					this.possibleAttacks.add(board[this.horz + 1][this.vert + 1]);
				}
				//Else if piece is protecting friendly piece, mark piece as not hanging and return 0
				else if(!(Pieces.isDifferentColor(this, board[this.horz - 1][this.vert + 1]))) {
					//Mark square as not hanging
					board[this.horz + 1][this.vert + 1].setIsHanging(false);
				} //End H file direction check
				break;
			}
			
		}
		//Return final threat count
		return threat;
	}

	
	/********** HELPER/STATIC METHODS ***********/
	
	//DESCRIPTION:		Instantiates all 8 pawns for passed color's side
	//PRE-CONDITION:	Passed char must be 'w' or 'b'
	//POST-CONDITION:	8 pawns are instantiated and placed on board according to color
	public static void placePawn(Pieces[][] board) {
		
		
		//PLaces white pawns	
			//For loop to place each piece
			for (int i = 0; i < 8; i++) {
				//A-H 2
				board[i][1] = new Pawn('b');
				board[i][1].setHorz(i);
				
			} //End white case 
		
			
		//Places black pawns
			//for loop for place
			for (int i = 0; i < 8; i++) {
				//A-H 7
				board[7 - i][6] = new Pawn('w');
				board[7 - i][6].setHorz(7 - i);
			}
			
	} //End switch statement
	
	//DESCRIPTION:		Method checks if passed pawn has pawns on either side; if Pawn is present, method returns true
	//PRE-CONDITION:	Passed pawn and board must be fully instantiated
	//POST-CONDITION:	Returns true if passed pawn has adjacent pawn in passed horz file
	public static boolean adjacent(Pieces[][] board, Pawn movedPawn, int horzCheck) {
		
		//If passed pawn is not on appropriate file for en passant, return -1
		switch(movedPawn.getColor()) {
		
		//White pawn case
		case 'w':
			//If pawn is not on 5th rank(board[][3]), return false
			if (movedPawn.getVert() != 3) {
				return false;
			}
			break;
		case 'b':
			//If pawn is not on 4th rank (board[][4]), return false
			if (movedPawn.getVert() != 4) {
				return false;
			}
			break;
		}
		
		//Try catch block in case out of bounds index
		try {
			
			//If passed horz is occupied by other pawn of different color, return true
			if ((board[horzCheck][movedPawn.getVert()] instanceof Pawn) &&
					Pieces.isDifferentColor(movedPawn, board[horzCheck][movedPawn.getVert()])) {
				return true;
			}
			else {
				return false;
			}
		} catch (IndexOutOfBoundsException i) {
			//If index out of bounds is caught, return false
			return false;
		}
	}
}
		


