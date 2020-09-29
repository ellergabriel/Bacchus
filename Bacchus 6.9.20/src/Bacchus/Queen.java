package Bacchus;
import java.util.*;
/* UML Class Diagram
---------------------
 * 		Queen		*
---------------------
 */
public class Queen extends Pieces{

	/*********** CONSTRUCTORS ************/
	//DESCRIPTION:		Constructs a Queen piece with default values and D file position
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Queen piece is constructed
	public Queen() {
		super();
		value = 9;
		this.setPiece('Q');
		this.setHorz(3); //D file
	}
	
	
	//DESCRIPTION:		Constructs a queen based on which side of the board
	//PRE-CONDITION:	Passed in char must be 'w' or 'b'
	//POST-CONDITION:	Queen piece is constructed on corresponding side
	public Queen(char color) {
		//Default queen 
		this();
		
		//Switch statement for both sides
		switch(color)
		{
		case 'w':
			this.setColor(color);
			this.setVert(7);//D1
			break;
		
		case 'b':
			this.setColor(color);
			this.setVert(0);//D8
			break;
			
		default:
			break;
		}
	}
	
	
	//DESCRIPTION:		Constructs new Queen piece with identical data to piece passed
	//PRE-CONDITION:	Piece passed in must be fully initialized
	//POST-CONDITION:	New Queen piece is constructed with identical data
	public Queen (Queen original) {
	
		super(original);
	
	}
	
	
	//DESCRIPTION:		Constructs new Queen piece using passed Pieces object
	//PRE-CONDTION:		Passed Pieces object must have valid values
	//POST-CONDITION:	New Queen object is constructed
	public Queen(Pieces original) {
		super(original);
	}
	
	
	/************ MOVEMENT ************/
	
	//DESCRIPTION:		Moves calling piece to passed coorindate if passed coordinate is valid; returns false if coordinate is not valid
	//PRE-CONDITION:	Passed ints must be 0 - 7
	//POST-CONDITION:	If piece can move to passed coordinate, move piece and return true; else, false
	public boolean move(Pieces[][] board, int horz, int vert) {
		
		//If not valid coordinate, return false
		if (!(this.validCoordinate(board, horz, vert))) {
			return false;
		}
		else {
			//Place piece and return true
			Pieces.placePiece(board, this, horz, vert);
			//Set isPinning to once piece is moved; isPinning is updated with each threat check
			this.isPinning = false;
			return true;
		}
	}
	
	
	//DESCRIPTION:		Checks Queen's movement path; returns true if path to square is unobstructed
	//PRE-CONDITION:	Queen object must be fully instantiated
	//POST-CONDITION:	Returns true if pathway is unobstructed and valid; else false
	public boolean validCoordinate(Pieces[][] board, int horz, int vert){
	
		//If either coordinate is out of bounds, return false
		if ((horz > 7) || (horz < 0)) {
			return false;
		}
		
		if ((vert > 7) || (vert < 0)) {
			return false;
		}
		
		//Check if Piece can move diagonally; if pinned horz or vert, cannot move
		if (this.canDiagonal(board, horz, vert)) { //True if piece goes diagonal
			return true;
		}
		//Check if piece can move linear/horz; if Pinned diagonally, cant move
		else if ((this.canLinear(board, horz, vert))) {
			return true;
		}
		else { //Failsafe in case both checks are false
			return false;
		}
		
	}
	
	
	//DESCRIPTION:		Checks if diagonal coordinate is obstructed
	//PRE-CONDITION:	Passed coordinates must be within array index range and valid
	//POST-CONDITION:	Returns true if pathway is clear; else false
	public boolean canDiagonal(Pieces[][] board, int horz, int vert) {
		
		//Check linear movement; return false if movement is linear
		if ((this.horz - horz == 0) || (this.vert - vert == 0)) {
			return false;
		}
		
		//Check if movement is in linear progression
		if ((Math.abs(this.horz - horz) != (Math.abs(this.vert - vert)))) {
			return false;
		}
		
		//If piece is pinned linearly, piece cannot travel diagonal
		if((this.isPinnedVert) || (this.isPinnedHorz)) {
			return false;
		}
		
		
		//If piece is pinned diagonally, check if passed coorindate is legal
		if (this.isPinnedDiagonal) {
			return this.canDiagonalPinned(board, horz, vert);
		}
		
		//Calculates how many squares are travelled
		int horzDirection;
		int vertDirection;
		
		//Checks which direction diagonal is pointing
		horzDirection = horz - this.horz;
		vertDirection = vert - this.vert;
		
		
		//Placeholders for horz/vert values of calling object
		int dHorz, dVert;
		
		
		
		//Left diagonal check 
		if (horzDirection < 0) { //Travelling towards A file
			
			//Check left upwards diagonal 
			if (vertDirection < 0) { //Travelling towards 1st rank
				
				//For loop to check each file 
				for (int i = 1; i < Math.abs(horzDirection) + 1; i++) {
					
					//Reset dHorz and dVert values for next loop
					dHorz = this.horz;
					dVert = this.vert;
					
					//Substract values from dummies
					dHorz -= i;
					dVert -= i;
					
					//Negative coordinate check
					if ((dHorz < 0) || (dVert < 0)) {
						return false;
					}
					
					//Checks if final space is occupied by an enemy piece
					if((i == Math.abs(horzDirection)) && (!(Pieces.isEmpty(board, dHorz, dVert))) && (Pieces.isDifferentColor(this, board[dHorz][dVert]))) {
						return true;
					}
					
					//Check if space is occupied by friendly; if true, mark occupying piece isHanging as false;
					else if (!(Pieces.isDifferentColor(this, board[dHorz][dVert]))) {
						
						//Mark occupying piece as not hanging
						board[dHorz][dVert].setIsHanging(false);
						return false;
					}
					
					//Check if moved coordinate is occupied
					else if (Pieces.isEmpty(board, dHorz, dVert)) {
						continue;
					}
					else {
						return false;
					}
				}
				
				//End for loop; if left back diag is complete, return true
				return true;
				
			}
			else {	//vertDirection > 0; travelling towards 8th rank
				
				//For loop to check files
				for (int i = 1; i < Math.abs(horzDirection) + 1; i++) {
					
					//Reset dHorz and dVert variables each iteration
					dHorz = this.horz;
					dVert = this.vert;
					
					//Subtract from dummies for next iteration coordinate
					dHorz -= i;
					dVert += i;
					
					//Negative/out of bounds index check
					if ((dHorz < 0) || (dVert > 7)) {
						return false;
					}
					
					
					//Checks if final space is occupied by an enemy piece
					if((i == Math.abs(horzDirection)) && (!(Pieces.isEmpty(board, dHorz, dVert))) && (Pieces.isDifferentColor(this, board[dHorz][dVert]))) {
						return true;
					}
					
					//Check if space is occupied by friendly; if true, mark occupying piece isHanging as false;
					else if (!(Pieces.isDifferentColor(this, board[dHorz][dVert]))) {
						
						//Mark occupying piece as not hanging
						board[dHorz][dVert].setIsHanging(false);
						return false;
					}
					
					//Check if current iteration coordinate is occupied; if empty, continue to next loop; else return false
					if (Pieces.isEmpty(board, dHorz, dVert)) {
						continue;
					}
					else {
						return false;
					}
				}
				
				//End for loop; if reached, return true
				return true;
			}
			
			//End left diagonal check
		}
		
		
		
		//Right diagonal check
		if (horzDirection > 0) {
			
			//Check right upwards diagonal
			if (vertDirection > 0) { //Travelling towards 8th rank
				
				//For loop for each square
				for (int i = 1; i < Math.abs(horzDirection) + 1; i++) {
					
					//Reset dummy values for each iteration
					dHorz = this.horz;
					dVert = this.vert;
					
					//Adjust dummy values for checked square
					dHorz += i;
					dVert += i;
					
					//Out of bounds index check
					if ((dHorz > 7) || (dVert > 7)) {
						return false;
					}
					
					//Checks if final space is occupied by an enemy piece
					if((i == Math.abs(horzDirection)) && (!(Pieces.isEmpty(board, dHorz, dVert))) && (Pieces.isDifferentColor(this, board[dHorz][dVert]))) {
						return true;
					}
					
					//Check if space is occupied by friendly; if true, mark occupying piece isHanging as false;
					else if (!(Pieces.isDifferentColor(this, board[dHorz][dVert]))) {
						
						//Mark occupying piece as not hanging
						board[dHorz][dVert].setIsHanging(false);
						return false;
					}
					
					//Check if new coordinate is empty; continue if space is empty; else return false
					if (Pieces.isEmpty(board, dHorz, dVert)) {
						continue;
					}
					else {
						return false;
					}
				}
				
				//End for loop; if reached, true is returned
				return true;
			} 
			//Check right downwards diagonal
			else { //vertDirection < 0
				
				//For loop for each iteration
				for (int i = 1; i < Math.abs(horzDirection) + 1; i++) {
					
					//Reset dummy values for each iteration
					dHorz = this.horz;
					dVert = this.vert;
					
					//Adjust dummy coordinates
					dHorz += i;
					dVert -= i;
					
					//Out of bounds check
					if ((dHorz > 7) || (dVert < 0)) {
						return false;
					}
					
					//Checks if final space is occupied by an enemy piece
					if((i == Math.abs(horzDirection)) && (!(Pieces.isEmpty(board, dHorz, dVert))) && (Pieces.isDifferentColor(this, board[dHorz][dVert]))) {
						return true;
					}
					
					//Check if space is occupied by friendly; if true, mark occupying piece isHanging as false;
					else if (!(Pieces.isDifferentColor(this, board[dHorz][dVert]))) {
						
						//Mark occupying piece as not hanging
						board[dHorz][dVert].setIsHanging(false);
						return false;
					}
					
					//Check if new coordinate is empty; continues if empty; else returns false
					if (Pieces.isEmpty(board, dHorz, dVert)) {
						continue;
					}
					else {
						return false;
					}
				}
				
				//End for loop; if reached, true is returned
				return true;
			}
			
			//End right diagonal check
		} 	
		
		//Fail safe if bug occurs
		return false;
		
	}
	
	
	//DESCRIPTION:		Determines if passed coordinate is valid if Piece is pinned diagonally
	//PRE-CONDITION:	Calling piece must be initialized
	//POST-CONDITION:	Returns true is Piece can move to square while pinned; else false
	@Override
	public boolean canDiagonalPinned(Pieces[][] board, int horz, int vert) {
		//If piece isnt pinned diagonally, return true
		if (!(this.isPinnedDiagonal)) {
			return true;
		}
		
		//Dummy copy board for simulation
		Pieces[][] dummyBoard = new Pieces[8][8];
		//Copy loop
		for (int horzIn = 0; horzIn < 8; horzIn++) {
			for (int vertIn = 0; vertIn < 8; vertIn++) {
				dummyBoard[horzIn][vertIn] = new Pieces(board[horzIn][vertIn]);
			}
		}
		
		/*
		 * will this ever work? who knows
		//Move simulation; if move is valid, continue with simulation
		if(this.validCoordinate(dummyBoard, horz, vert)) {
			
			//Test move placement and update threats
			Pieces.placePiece(dummyBoard, this, horz, vert);
			//Update King checks; if King is checked, return false; else return true
			if(King.checkUpdate(dummyBoard, this.getColor())) {
				return false;
			}
			else {
				return true;
			}
		}
		//If coordinate is invalid, return false
		else {
			return false;
		}
		*/
		
		return false;
	}
	
	//DESCRIPTION:		Checks if linear coordinate is obstructed
	//PRE-CONDITION:	Passed coordinates must be within the array index range and valid ints
	//POST-CONDITION:	Returns true if path to coordinate is clear; else false
	public boolean canLinear(Pieces[][] board, int horz, int vert) {
		
		//First check if coordinate is linear; returns false if coordinate is invalid
		if ((this.horz != horz) && (this.vert != vert)) {
			return false; 	//One of the passed coordinates must remain the same as calling piece
		}
		
		//If piece is pinned diagonally, cannot move linearly 
		if (this.isPinnedDiagonal) {
			return false;
		}
		
		//Forward/backwards movement
		if (this.horz == horz) {
			
			//If piece is pinned horizontally, return false
			if (this.isPinnedHorz) {
				return false;
			}
			
			//vertDistance determines direction and how far piece travels
			int vertDistance;
			vertDistance = vert - this.vert;
			
			//dummy variable used for iteration looping
			int dVert;
			
			//Piece is traveling backwards
			if (vertDistance < 0) { 
				
				//For loop to loop through each backwards square
				for (int i = 1; i < Math.abs(vertDistance) + 1; i++) {
					
					
					//Reset dummy variable value for next coordinate check
					dVert = this.vert - i;
					
					//Error check dVert to be in bounds of array
					if (dVert < 0) {
						return false;
					}
					
					//Check if final coordinate is enemy piece; returns true if last coordinate is enemy
					if ((i == Math.abs(vertDistance)) && (!(Pieces.isEmpty(board, horz, dVert))) && (Pieces.isDifferentColor(this, board[horz][dVert]))) {
						return true;
					}
					
					//Check if space is occupied by friendly; if true, mark occupying piece isHanging as false;
					else if (!(Pieces.isDifferentColor(this, board[horz][dVert]))) {
						
						//Mark occupying piece as not hanging
						board[horz][dVert].setIsHanging(false);
						return false;
					}
					
					//Check if iterated square is empty; continue if empty, return false otherwise
					else if (Pieces.isEmpty(board, horz, dVert)) {
						continue;
					}
					else {
						return false;
					}
				
				}
				
				//End for loop; return true is code block is reached
				return true;	
			}
			
		
			//Piece is traveling forward 
			else { //if (vertDistance > 0) 
				
				//For loop for each forward square
				for (int i = 1; i < Math.abs(vertDistance) + 1; i++) {
					
					//Reset dummy variable for each loop
					dVert = this.vert + i;
					
					
					//Error check for 8th file; returns false if dVert is greater than 7
					if (dVert > 7) {
						return false;
					}
					
					
					//Check if final coordinate is enemy piece; returns true if last coordinate is enemy
					if ((i == Math.abs(vertDistance)) && (!(Pieces.isEmpty(board, horz, dVert))) && (Pieces.isDifferentColor(this, board[horz][dVert]))) {
						return true;
					}
					//Check if space is occupied by friendly; if true, mark occupying piece isHanging as false;
					else if (!(Pieces.isDifferentColor(this, board[horz][dVert]))) {
						
						//Mark occupying piece as not hanging
						board[horz][dVert].setIsHanging(false);
						return false;
					}
					
					//Check if iterated coordinate is empty; continues if empty,else returns false
					if (Pieces.isEmpty(board, horz, dVert)) {
						continue;
					}
					else {
						return false;
					}
				}
				
				//End for loop; return true if code block is reached
				return true;
			}
		}		//End linear movement check
		
		
		//Horizontal movement check 
		else if (this.vert == vert) {
			
			//If piece is pinned vertically, cannot move horizontally; return false
			if (this.isPinnedHorz) {
				return false;
			}
			
			//Variable to determine how far piecce travels
			int horzDistance;
			horzDistance = horz - this.horz;
					
			//Dummy variable for iteration
			int dHorz;
			
			
			//Checks if piece is going towards A file
			if (horzDistance < 0) {
				
				//For loop for squares to the left
				for (int i = 1; i < Math.abs(horzDistance) + 1; i++) {
					
					//Reset dummy variable each iteration
					dHorz = this.horz - i;
					
					//Check if dHorz is within range
					if (dHorz < 0) {
						return false;
					}
					
					//Check if final coordinate is enemy piece; returns true if last coordinate is enemy
					if ((i == Math.abs(horzDistance)) && (!(Pieces.isEmpty(board, dHorz, vert))) && (Pieces.isDifferentColor(this, board[dHorz][vert]))) {
						return true;
					}
					
					//Check if space is occupied by friendly; if true, mark occupying piece isHanging as false;
					else if (!(Pieces.isDifferentColor(this, board[dHorz][vert]))) {
						
						//Mark occupying piece as not hanging
						board[dHorz][vert].setIsHanging(false);
						return false;
					}
					
					//Check if new coodinate is occupied; continue if unoccupied, else return false
					if (Pieces.isEmpty(board, dHorz, vert)) {
						continue;
					}
					else {
						return false;
					}
				}
				
				//End for loop; if code block is reached, return true
				return true;
			}
			
			//Piece is traveling towards H file
			else { //if (horzDistance > 0)
				
				//For loop for squares to the right
				for (int i = 1; i < Math.abs(horzDistance) + 1; i++) {
					
					//Reset dummy variable each iteration
					dHorz = this.horz + i;
					
					//Check if dHorz is within array range 
					if (dHorz > 7) {
						return false;
					}
					
					
					//Check if final coordinate is enemy piece; returns true if last coordinate is enemy
					if ((i == Math.abs(horzDistance)) && (!(Pieces.isEmpty(board, dHorz, vert))) && (Pieces.isDifferentColor(this, board[dHorz][vert]))) {
						return true;
					}
					
					//Check if space is occupied by friendly; if true, mark occupying piece isHanging as false;
					else if (!(Pieces.isDifferentColor(this, board[dHorz][vert]))) {
						
						//Mark occupying piece as not hanging
						board[dHorz][vert].setIsHanging(false);
						return false;
					}
					
					//Check if next coordinate is empty; continue if unoccupied, else return false
					if (Pieces.isEmpty(board, dHorz, vert)) {
						continue;
					}
					else {
						return false;
					}
				}
				
				//End for loop; if code block is reached, return true;
				return true;
			}
		}	//End horizontal movement check
	
		//Return false in case of bugs
		return false;
	}

	
	//DESCRIPTION:		Method returns how many linear squares are threatened by Queen
	//PRE-CONDITION:	Calling piece must be fully initialized
	//POST-CONDITION:	Returns number of linear squares threatened
	@Override
	public int threatenedSquaresLinear(Pieces[][] board) {
		
		//Tracks number of threatened squares
		int threat = 0;
		
		//For loop to track each index
		for (int i = 1; i < 8; i++) {
			
			//Linear left
			if ((this.validCoordinate(board, this.horz - i, this.vert) && (this.horz - i) > -1)) {
				threat++;
				//Check if space is occupied; if not, update square with threats
				if (board[this.horz - i][this.vert] instanceof Squares) {
					//board[this.horz - i][this.vert] = new Squares(true);
					board[this.horz - i][this.vert].updateThreats();
					Pieces.markThreat(board, this, this.horz - i, this.vert);
				}
				else if (!(Pieces.isDifferentColor(this, board[this.horz - i][this.vert]))) {
					//If piece is same color, mark piece as not hanging
					board[this.horz - i][this.vert].setIsHanging(false);
				}
			}
			else {
				break;
			}
		}
		for (int i = 1; i < 8; i++) {
			//Linear Right
			if (this.validCoordinate(board, this.horz + i, this.vert)) {
				threat++;
				//Check if space is occupied; if not, create Squares object with 
				//isThreatened set to true
				if (board[this.horz + i][this.vert] instanceof Squares) {
					//board[this.horz + i][this.vert] = new Squares(true);
					board[this.horz + i][this.vert].updateThreats();
					Pieces.markThreat(board, this, this.horz + i, this.vert);
				}
				else if (!(Pieces.isDifferentColor(this, board[this.horz + i][this.vert]))) {
					//If piece is same color, mark piece as not hanging
					board[this.horz - i][this.vert].setIsHanging(false);
				}
			}
			//If not valid coordinate, end loop
			else {
				break;
			}
		}
			
		//Up/Down linear checks
		for (int i = 1; i < 8; i++) {
			//Upwards board (BACKWARDS BOARD PRINT)
			if(this.validCoordinate(board, this.horz, this.vert - i)) {
				threat++;
				//Check if space is occupied; if not, create Squares object with 
				//isThreatened set to true
				if (board[this.horz][this.vert - i] instanceof Squares) {
					//board[this.horz][this.vert - i] = new Squares(true);
					board[this.horz][this.vert - i].updateThreats();
					Pieces.markThreat(board, this, this.horz, this.vert - i);
				}
				else if (!(Pieces.isDifferentColor(this, board[this.horz][this.vert - i]))) {
					//If piece is same color, mark piece as not hanging
					board[this.horz - i][this.vert].setIsHanging(false);
				}
			}
			//If invalid coordinate is reached, end loop
			else {
				break;
			}
		}
		
		for (int i = 1; i < 8; i++) {
			//Downwards board
			if (this.validCoordinate(board, this.horz, this.vert + i)) {
				threat++;
				//Check if space is occupied; if not, create Squares object with 
				//isThreatened set to true
				if (board[this.horz][this.vert + i] instanceof Squares) {
					//board[this.horz][this.vert + i] = new Squares(true);
					board[this.horz][this.vert + i].updateThreats();
					Pieces.markThreat(board, this, this.horz, this.vert + i);
				}
				else if (!(Pieces.isDifferentColor(this, board[this.horz][this.vert + i]))) {
					//If piece is same color, mark piece as not hanging
					board[this.horz - i][this.vert].setIsHanging(false);
				}
			}
			//If invalid coordinate is reached, end loop
			else {
				break;
			}
		}
		return threat;
	}
	
	
	//DESCRIPTION:		Method returns how many squares the Queen threatens diagonally
	//PRE-CONDITION:	Calling queen must be initialized 
	//POST-CONDITION:	Number of threatened squares is returned
	@Override
	public int threatenedSquaresDiagonal(Pieces[][] board) {
		
		//Tracks threatened squares
		int threat = 0;
		
		//For loop to check each diagonal
		for (int i = 1; i < 8; i++) {
			
			//Check for inbound index
			if ((this.horz + i) <= 7) {
				//LeftDown Diag
				
				//Check for inbounds vert value
				if (this.vert + i <= 7) {
					if(this.validCoordinate(board, this.horz + i, this.vert + i)) {
						threat++;
						if(board[this.horz + i][this.vert + i] instanceof Squares) {
							//board[this.horz + i][this.vert + i] = new Squares(true);
							board[this.horz + i][this.vert + i].updateThreats();
							Pieces.markThreat(board, this, this.horz + i, this.vert + i);
						}
					}
				}
				else {
					break;
				}
				
			}
		}
		
		for (int i = 1; i < 8; i++) {
				//LeftUp Diag
		
				//Check for inbounds vert value
				if ((this.vert - i) > -1) {
					if (this.validCoordinate(board, this.horz + i, this.vert - i)) {
						threat++;
						if(board[this.horz + i][this.vert - i] instanceof Squares) {
							//board[this.horz + i][this.vert - i] = new Squares(true);
							board[this.horz + i][this.vert - i].updateThreats();
							Pieces.markThreat(board, this, this.horz + i, this.vert - i);
						}
					}
				}
				else {
					break;
				}
			}
		
		for (int i = 1; i < 8; i++) {
			
			//Check for inbounds horz coordinate
			if ((this.horz - i) > -1) {
				//RightDown diag
				
				//Check for inbounds vert value
				if (this.vert + i <= 7) {
					if (this.validCoordinate(board, this.horz - i, this.vert + i)) {
						threat++;
						if(board[this.horz - i][this.vert + i] instanceof Squares) {
							//board[this.horz - i][this.vert + i] = new Squares(true);
							board[this.horz - i][this.vert + i].updateThreats();
							Pieces.markThreat(board, this, this.horz - i, this.vert + i);
						}
					}
				}
				else {
					break;
				}
			}
		}
			
		for (int i = 1; i < 8; i++){
			//RightUp diag
				
				//Check for inbounds vert value
				if (this.vert - i > -1) {
					if (this.validCoordinate(board, this.horz - i, this.vert - i)) {
					threat++;
					if(board[this.horz - i][this.vert - i] instanceof Squares) {
						//board[this.horz - i][this.vert - i] = new Squares(true);
						board[this.horz - i][this.vert - i].updateThreats();
						Pieces.markThreat(board, this, this.horz - i, this.vert - i);
						}
					}
					else {
						break;
					}
				}
			}
		
		//Return total number of threats
		return threat;
		}
		
		
	
	//DESCRIPTION:		Method returns how many square the Queen threatens	
	//PRE-CONDITION:	Queen object must be fully initialized
	//POST-CONDITION:	Returns number of squares Queen threatens
	@Override
	public int threatenedSquares(Pieces[][] board) {
		
		//Variables to track # of threatened squares
		int threat = 0;
		
		//Add linear threats 
		threat += this.threatenedSquaresLinear(board);
		
		//Add diagonal threats
		threat += this.threatenedSquaresDiagonal(board);
		
		//Return total number of squares threatened
		return threat;
	}
	
	
	//DESCRIPTION:		Methods returns number of pieces threatened by calling Queen linear path	
	//PRE-CONDITION:	Calling Queen and board array must be initialized
	//POST-CONDITION:	Returns number of pieces threatened by Queen
	public int threatenedPiecesLinear(Pieces[][] board) { 
		int threat = 0;
		
		//Check fowards/backwards pathways
		//Variables for distance
		int downVert, upVert;
		int leftHorz, rightHorz;
		
		//Set values before for loops
		downVert = upVert = leftHorz = rightHorz = 0;
		
		
		//For loop to check increasing coordinates; downVert and leftHorz (Towards H1)
		//downVert checks
		for (int index = 1; index < 8; index++) {
			//new downVert values; approaches 8th rank [][7]
			downVert = this.vert + index;
			
			//Check if downVert is out of bounds; if in bounds, checks if next coordinate is friendly; breaks if either is true
			if (downVert > 7)  {
				break;
			}
			//If coordinate is friendly, mark coordinate as not hanging
			else if (!(Pieces.isDifferentColor(this, board[this.horz][downVert]))) {
				board[this.horz][downVert].setIsHanging(false);
				break;
			}
			
			//If in bounds, check if coordinate is valid; if not, break;
			else if (this.validCoordinate(board, this.horz, downVert)){
				//Check if coordinate is occupied, and diff color
				if (Pieces.isEnemyPiece(board, this, this.horz, downVert)) {
				//if ((!(Pieces.isEmpty(board, this.horz, downVert))) && (Pieces.isDifferentColor(this, board[this.horz][downVert]))) {
					//Increment threats; break to move onto next for loop
					threat += board[this.horz][downVert].getValue();
					board[this.horz][downVert].updateThreats();
					Pieces.markThreat(board, this, this.horz, downVert);
					this.possibleAttacks.add(board[this.horz][downVert]);
					//Check if threatened piece is king; if true, check King
					if (board[this.horz][downVert].getPiece() == 'K') {
						//Set isChecked boolean to true
						board[this.horz][downVert].setIsChecked(true);
					}
					//Pinned piece check; continue checking coordinates after attacked piece
					else {
						//For loop to check spaces behind threatened piece
						for (int i = downVert; i < 8; i++) {
							//If pieces behind attacked piece is King, set piece as pinned
							if ((i < 8) && (board[this.horz][i].getPiece() == 'K') 
									&& (Pieces.isDifferentColor(this, board[this.horz][i]))){
								board[this.horz][downVert].setIsPinnedVert(true);
								//Set isPinning of calling piece to true
								this.isPinning = true;
							}
						}
					}
					break;
				}
				
				//If square is valid but not occupied, continue
				else {
					continue;
				}
			}
			
			//If coordinate is not valid, break loop
			else {
				break;
			}
		} 	//End downVert checks; downward linear file completed
		
		
		//leftHorz check
		for (int index = 1; index < 8; index++) {
			//new leftHorz value for iteration
			leftHorz = this.horz + index;
			
			//Check if leftHorz is within array bounds; if in bounds, checks if coordinate is friendly; breaks if either is true
			if (leftHorz > 7) {
				break;
			}
			//If piece is friendly, mark as not hanging
			else if (!(Pieces.isDifferentColor(this, board[leftHorz][this.vert]))) {
				board[leftHorz][this.vert].setIsHanging(false);
				break;
			}
			
			//If in bounds, check next square towards H file; vert stays the same
			else if ((this.validCoordinate(board, leftHorz, this.vert))) {
				
				//Check if coordinate is occupied, and diff color
				if (Pieces.isEnemyPiece(board, this, leftHorz, this.vert)) {
				//if ((!(Pieces.isEmpty(board, leftHorz, this.vert))) && (Pieces.isDifferentColor(this, board[leftHorz][this.vert]))) {
					//Increment threats; break to move onto next loop
					threat += board[leftHorz][this.vert].getValue();
					board[leftHorz][this.vert].updateThreats();
					Pieces.markThreat(board, this, leftHorz, this.vert);
					this.possibleAttacks.add(board[leftHorz][this.vert]);
					//King check
					if (board[leftHorz][this.vert].getPiece() == 'K') {
						//Set King as checked
						board[leftHorz][this.vert].setIsChecked(true);
					}
					//If opposing piece is attacked, check if piece is pinned
					else {
						for (int i = leftHorz; i < 8; i++) {
							if ((i < 8) && (board[i][this.vert].getPiece() == 'K') 
									&& (Pieces.isDifferentColor(this, board[i][this.vert]))) {
								board[i][this.vert].setIsPinnedHorz(true);
								//Set isPinning of calling piece to true
								this.isPinning = true;
							}
						}
					}
					break;
				}
				
				//If square coordinate is valid but no piece to threaten, continue;
				else {
					continue;
				}
			}
			
			//If coordinate is not valid, break loop
			else {
				break;
			}
		}
		
		
		//For loop to check decreasing coordinates
		//For loop to check upVert; closer to 0/rank 8
		for (int index = 1; index < 8; index++) {
			
			//Reset value upVert value each iteration
			upVert = this.vert - index;
			
			//Check if upVert is out of bounds; if in bounds, check if space is occupied by friendly; break if either are true
			if (upVert < 0) {
				break;
			}
			//If piece is friendly, mark as not hanging
			else if ((!(Pieces.isDifferentColor(this, board[this.horz][upVert])))) {
				board[this.horz][upVert].setIsHanging(false);
				break;
			}
			
			//Check if coordinate is valid
			else if (this.validCoordinate(board, this.horz, upVert)) {
				//If coordinate is valid, check if coordinate is occupied and diff color
				if (Pieces.isEnemyPiece(board, this, this.horz, upVert)) {
				//if ((!(Pieces.isEmpty(board, this.horz, upVert))) && (Pieces.isDifferentColor(this, board[this.horz][upVert]))) {
					threat += board[this.horz][upVert].getValue();
					board[this.horz][upVert].updateThreats();
					Pieces.markThreat(board, this, this.horz, upVert);
					this.possibleAttacks.add(board[this.horz][upVert]);
					//King check; if King is threatened, set as checked 
					if (board[this.horz][upVert].getPiece() == 'K') {
						board[this.horz][upVert].setIsChecked(true);
					}
					//Check if opposing piece is pinned
					else {
						for (int i = upVert; i > -1; i--) {
							if ((i > -1) && (board[this.horz][i].getPiece() == 'K') 
									&& (Pieces.isDifferentColor(this, board[this.horz][i]))) {
								board[this.horz][upVert].setIsPinnedVert(true);
								//Set isPinning of calling piece to true
								this.isPinning = true;
							}
						}
					}
					break;
				}
				//If coordinate is valid but empty; continue
				else {
					continue;
				}
			}
			//If coordinate is not valid, break
			else {
				break;
			}
		}
		
		
		//For loop to check rightHorz, closer to horz 0/ file A
		for (int index = 1; index < 8; index++) {
			
			//Reset value for each iteration
			rightHorz = this.horz - index;
			
			//Check if rightHorz is out of bounds; if in bounds, check if coordinate is same color; if either are true, break
			if (rightHorz < 0) {
				break;
			}
			//If piece is friendly, mark as not hanging
			else if (!(Pieces.isDifferentColor(this, board[rightHorz][this.vert]))) {
				board[rightHorz][this.vert].setIsHanging(false);
				break;
			}
			//Check if coordinate is valid
			else if (this.validCoordinate(board, rightHorz, this.vert)) {
				//If coordinate is valid, check if coordinate is occupied and diff color
				if (Pieces.isEnemyPiece(board, this, rightHorz, this.vert)) {
				//if ((!(Pieces.isEmpty(board, rightHorz, this.vert))) && (Pieces.isDifferentColor(this, board[rightHorz][this.vert]))) {
					threat += board[rightHorz][this.vert].getValue();
					board[rightHorz][this.vert].updateThreats();
					Pieces.markThreat(board, this, rightHorz, this.vert);
					this.possibleAttacks.add(board[rightHorz][this.vert]);
					//King check 
					if (board[rightHorz][this.vert].getPiece() == 'K') {
						board[rightHorz][this.vert].setIsChecked(true);
					}
					//Check if piece is pinned protecting King
					else {
						for (int i = rightHorz; i > -1; i--) {
							if (( i < 8) && (board[i][this.vert].getPiece() == 'K')
									&& (Pieces.isDifferentColor(this, board[i][this.vert]))) {
								board[rightHorz][this.vert].setIsPinnedHorz(true);
								//Set isPinning of calling piece to true
								this.isPinning = true;
							}
						}
					}
					break;
				}
				//If coordinate is valid but empty; continue
				else {
					continue;
				}
			}
			//If coordinate is not valid, break loop
			else {
				break;
			}
		}
		//Return total number of threats
		return threat;
	}
	
	//DESCRIPTION:		Method returns number of pieces threatened by calling Queen diag path
	//PRE-CONDITION:	Calling queen and board array must be initialized
	//POST-CONDITION:	Returns number of pieces threatened by queen diagonally
	@Override
	public int threatenedPiecesDiagonal(Pieces[][] board) {
		int threat = 0;
		//Variables used for each loop
		int leftHorz, rightHorz;
		int upVert, downVert;
		
		leftHorz = rightHorz = upVert = downVert = 0;
		
		//Check RightDown diag (Towards H1/ 7,7); cannot exceed 7
		for (int index = 1; index < 8; index++) {
			//Adjust coordinates each loop
			//rightHorz and downVert(VERT IS FLIPPED)
			rightHorz = this.horz + index;
			downVert = this.vert + index;
						
			//Check if either are out of bounds/ > 7; break loop if out of bounds
			if ((rightHorz > 7) || (downVert > 7)) {
				break;
			}
			
			//Check if space is occupied by friendly; loop will end if occupied
			else if (!(Pieces.isEmpty(board, rightHorz, downVert))) {
				//If occupied by enemy, increment threat, add to possibleAttacks, and check for pinning
				if (Pieces.isDifferentColor(this, board[rightHorz][downVert])) {
					threat += board[rightHorz][downVert].getValue();
					Pieces.markThreat(board, this, rightHorz, downVert);
					this.possibleAttacks.add(board[rightHorz][downVert]);
					board[rightHorz][downVert].updateThreats();
					//Check if threatened piece is King
					if (board[rightHorz][downVert].getPiece() == 'K') {
						//Set King as checked
						board[rightHorz][downVert].setIsChecked(true);
					}
					//Check for pinned pieces
					else {
						//Iterate over for loop to continue checking
						for (int i = 1; i < 8; i++) {
							//If King is found after attacked piece, set attacked piece as pinned
							//Error check for inbounds indices
							if (((rightHorz + index) < 8) && ((downVert + index) < 8)) {
								if ((board[rightHorz + index][leftHorz + index].getPiece() == 'K') 
										&& (Pieces.isDifferentColor(this, board[rightHorz + index][downVert + index]))) {
										//Set attacked piece as pinned 
									board[rightHorz][downVert].setIsPinnedDiagonal(true);
									}
							}
						}
					}
					break;
				}
				
				//Else space is friendly, mark as not hanging, break loop as diag cannot continue
				else {
					board[rightHorz][downVert].setIsHanging(false);
					break;
				}
			}
			
			//If iteration coordinate is not occupied; continue to next loop
			else {
				continue;
			}
		}
		
		
		//Check RightUp diag; rightHorz and upVert(VERT IS FLIPPED)
		for (int index = 1; index < 8; index++) {
			
			//Adjust coordinates for each iteration
			rightHorz = this.horz + index;
			upVert = this.vert - index;
			
			//Check if either are out of bounds; if out, break loop
			if ((rightHorz > 7) || (upVert < 0)) {
				break;
			}
			
			//Check if coordinate is occupied; if true, check piece color
			else if (!(Pieces.isEmpty(board, rightHorz, upVert))) {
				
				//If piece is diff color, increment and break loop
				if (Pieces.isDifferentColor(this, board[rightHorz][upVert])) {
					threat += board[rightHorz][upVert].getValue();
					board[rightHorz][upVert].updateThreats();
					Pieces.markThreat(board, this, rightHorz, upVert);
					this.possibleAttacks.add(board[rightHorz][upVert]);
					//Check if threatened Piece is king
					if(board[rightHorz][upVert].getPiece() == 'K') {
						//Set King as checked
						board[rightHorz][upVert].setIsChecked(true);
					}
					//Check if attacked piece is pinned
					else {
						//For loop to iterate over remaining squares
						for (int i = 1; i < 8; i++) {
							//Check for inbounds coordinates
							if (((rightHorz + i) < 8) && (upVert - i) > -1) {
								//Check if King is in diagonal
								if ((board[rightHorz + i][upVert - i].getPiece() == 'K')
									&& (Pieces.isDifferentColor(this, board[rightHorz + i][upVert - i]))) {
									//Set attacked piece as pinned
									board[rightHorz][upVert].setIsPinnedDiagonal(true);
								}
								
							}
						}
					}
					break;
				}
				
				//If not diff color, mark as not hanging, break loop as no more pieces an be attack in this diag
				else {
					board[rightHorz][upVert].setIsHanging(false);
					break;
				}
			}
			
			//If coordinate is empty, continue to next iteration
			else {
				continue;
			}
		}
		
		
		//Check LeftDown diag; leftHorz and downVert(VERT VARIABLE IS INCREASED)
		for (int index = 1; index < 7; index++) {
			
			//Set values for each iteration
			leftHorz = this.horz - index;
			downVert = this.vert + index;
			
			//Check if either are out of bounds; break loop if either are out
			if ((leftHorz < 0) || (downVert > 7)) {
				break;
			}
			
			//Check if coordinate is occupied; if true, check for color
			else if (!(Pieces.isEmpty(board, leftHorz, downVert))) {
				
				//If piece is diff color, increment threat and break loop;
				if (Pieces.isDifferentColor(this, board[leftHorz][downVert])) {
					threat += board[leftHorz][downVert].getValue();
					board[leftHorz][downVert].updateThreats();
					Pieces.markThreat(board, this, leftHorz, downVert);
					this.possibleAttacks.add(board[leftHorz][downVert]);
					//Check if attacked piece is king
					if (board[leftHorz][downVert].getPiece() == 'K') {
						//Set King as checked
						board[leftHorz][downVert].setIsChecked(true);
					}
					//Check if attacked piece is pinned
					else {
						//Iterate over next few scares
						for (int i = 1; i < 8; i++) {
							if (((leftHorz - i) > -1) && ((downVert + i) < 8)) {
								//Check if piece is different color King
								if ((board[leftHorz - i][downVert + i].getPiece() == 'K')
									&& (Pieces.isDifferentColor(this, board[leftHorz - i][downVert + i]))) {
									//Set attacked piece as pinned
									board[leftHorz][downVert].setIsPinnedDiagonal(true);
								}
							}
						}
					}
					break;
				}
				
				//If not diff color, mark as not hanging, break loop
				else {
					board[leftHorz][downVert].setIsHanging(false);
					break;
				}
			}
			
			//Continue loop if coordinate is empty
			else {
				continue;
			}
		}
		
		
		//Check LeftUp diag; leftHorz and upVert(Both decreasing)
		for (int index = 1; index < 8; index++) {
			
			//Set values for each iteration
			leftHorz = this.horz - index;
			upVert = this.vert - index;
			
			//Check if either are out of bounds; break loop if true
			if ((leftHorz < 0) || (upVert < 0)) {
				break;
			}
			
			//Check if coordinate is occupied
			else if (!(Pieces.isEmpty(board, leftHorz, upVert))) {
				
				//if coordinate has enemy piece, increment threat and break
				if (Pieces.isDifferentColor(this, board[leftHorz][upVert])) {
					threat += board[leftHorz][upVert].getValue();
					board[leftHorz][upVert].updateThreats();
					Pieces.markThreat(board, this, leftHorz, upVert);
					this.possibleAttacks.add(board[leftHorz][upVert]);
					//Check if threatened piece is King
					if (board[leftHorz][upVert].getPiece() == 'K') {
						//Set King as checked
						board[leftHorz][upVert].setIsChecked(true);
					}
					//Check if attacked piece is pinned
					else {
						for (int i = 1; i < 8; i++) {
							//Check for inbound indices
							if (((leftHorz - index) > -1) &&(upVert - index) > -1) {
								//Check if piece is differetn colored King
								if ((board[leftHorz - index][upVert - index].getPiece() == 'K')
									&& (Pieces.isDifferentColor(this, board[leftHorz - i][upVert - i]))) {
									//Set attacked piece as pinned
									board[leftHorz][upVert].setIsPinnedDiagonal(true);
								}
							}
						}
					}
					break;
				}
				
				//If coordinate has friendly piece, mark as not hanging, break loop
				else {
					board[leftHorz][upVert].setIsHanging(false);
					break;
				}
			}
			
			//If coordinate is empty, continue loop
			else {
				continue;
			}
		}
		//Return total number of pieces threatened
		return threat;
	}
	
	
	//DESCRIPTION:		Method returns the number of pieces calling Queen can attack	
	//PRE-CONDITION:	Board array must be fully initialized
	//POST-CONDITION:	Returns number of pieces threatened by the queen
	public int threatenedPieces(Pieces[][] board) {
		int threat = 0;
		
		//Reset possibleAttacks before each call
		this.possibleAttacks = new ArrayList<Pieces>();
		
		//Adds returned number of threats from linear method
		threat += this.threatenedPiecesLinear(board);
		
		//Adds returned number of threats from diagonal method
		threat += this.threatenedPiecesDiagonal(board);
		
		return threat;
	}
	
	/******** STATIC/HELPER METHODS ************/
	//DESCRIPTION:		Places both queen on passed board array
	//PRE-CONDITION:	Passed board must be initialized
	//POST-CONDITION:	Both queen are placed on board array
	public static void placeQueen(Pieces[][] board) {
		
		//Place white queen D1 (3,0)
		board[3][0] = new Queen('b');
		
		//Place black queen D8 (3,7)
		board[3][7] = new Queen('w');
	}
	
	
	
}	