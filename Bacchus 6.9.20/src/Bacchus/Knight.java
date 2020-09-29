package Bacchus;
import java.util.*;
public class Knight extends Pieces{

	/******** CONSTRUCTORS ***********/

	
	//DESCRIPTION:		Instantiates default knight object
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Knight object is instantiated
	public Knight() {
		super();
		this.value = 3;
		this.piece = 'N';
	}
	
	//DESCRIPTION:		Instantiates knight object with color passed in
	//PRE-CONDITION:	Passed char must be 'w' or 'b'
	//POST-CONDITION:	Knight with passed color is created
	public Knight(char color) {
		this();
		this.color = color;
		
		//Switch for both sides
		switch(color) {
		
		//Black knights
		case 'b':
			this.vert = 0;
			break;
			
		//White knights
		case 'w':
			this.vert = 7;
			break;
		} //End switch
	}
	
	
	//DESCRIPTION:		Instantiated knight object identical to passed object
	//PRE-CONDITION:	Passed object must be fully instantiated
	//POST-CONDITION:	Clone knight object is created
	public Knight(Knight original) {
		super(original);
	}

	//DESCRIPTION:		Creates new Knight object usig passed PIeces object
	//PRE-CONDITION:	Passed Pieces object must be valid
	//POST-CONDITION:	New Knight piece is constructed
	public Knight(Pieces original) {
		super(original);
	}
	
	
	/********* MOVEMENT METHODS **********/
	
	//DESCRIPTION:		Moves calling Knight object to passed coordinate
	//PRE-CONDITION:	Passed coordinates must be valid ints
	//POST-CONDITION:	Moves knight to coordinate passed
	public boolean move(Pieces[][] board, int horz, int vert) {
		//Check if passed coordinates are valid
		if (this.validCoordinate(board, horz, vert)) {
			Pieces.placePiece(board, this, horz, vert);
			return true;
		}
		//Return false if coordinate is not valid
		else {
			return false;
		}
	}
	
	
	//DESCRIPTION:		Checks if passed coordinate is valid for Knight to move to
	//PRE-CONDITION:	Passed coordinates must be valid ints
	//POST-CONDITION:	Returns true if Knight can go to coordinate; else false
	public boolean validCoordinate(Pieces[][] board, int horz, int vert) {
		
		//If knight is pinned at all, cannot move
		if ((this.isPinnedDiagonal) || (this.isPinnedHorz) || (this.isPinnedVert)) {
			return false;
		}
		if (this.canLong(board, horz, vert)) {
			return true;
		}
		else if (this.canTall(board, horz, vert)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//DESCRIPTION:		Checks if Knight can make Long move to passed coordinate
	//PRE-CONDITION:	Passed coordinate must be valid and within range of board array
	//POST-CONDITION:	Returns true if knight can move to passed Long coordinate
	public boolean canLong(Pieces[][] board, int horz, int vert) {
		
		try {
			//Check if passed coordinates are valid w/ dummies
			int dHorz, dVert;
			dHorz = horz - this.horz;
			dVert = vert - this.vert;
			
			//Error check for (2,1) distance for Long move 
			if ((Math.abs(dHorz) != 2) || (Math.abs(dVert) != 1)) {
				return false;
			}
			
			//Error check for out of bound values
			if ((horz < 0) || (horz > 7)) {
				return false;
			}
			
			if ((vert < 0) || (vert > 7)) {
				return false;
			}
			
			/*
			//Check if space is empty
			if (Pieces.isEmpty(board, horz, vert)) {
				//Mark Squares object as threatened
				board[horz][vert].setIsThreatened(true);
				return true;
			}
			*/
			
			//Check if coordinate is occupied by enemy piece
			else if ((Pieces.isDifferentColor(this, board[horz][vert])) || (Pieces.isEmpty(board, horz, vert))) {
				//If enemy piece is on square, mark piece as threatened using switch statement
				switch(this.color) {
				case 'w':	board[horz][vert].setIsThreatenedByWhite(true);
							break;
				case 'b':	board[horz][vert].setIsThreatenedByBlack(true);
							break;
				}
				return true;
			}
			//If space is not empty and not occupied by enemy piece, mark occupying friendly piece as not hanging and return false
			else {
				board[horz][vert].setIsHanging(false);
				return false;
			}
		} catch (NullPointerException n) {
			//If null pointer is caught, passed coordinate is false
			return false;		
		} catch (IndexOutOfBoundsException i) {
			//If index used is out of bounds, return false
			return false;
		}
		
		
	}
		
	
	//DESCRIPTION:		Checks if Knight can make Tall move to passed coordinate	
	//PRE-CONDITION:	Passed coordinate must be valid int and within range of board array
	//POST-CONDITION:	Returns true if knight can move to passed Tall coordinate
	public boolean canTall(Pieces[][] board, int horz, int vert) {
		
		try {
			//Dummy coordinates for checking passed ints
			int dHorz;
			int dVert;
			
			dHorz = this.horz - horz;
			dVert = this.vert - vert;
			
			//Check if passed ints have (1,2) relationship; returns false if not
			if ((Math.abs(dHorz) != 1) || (Math.abs(dVert) != 2)) {
				return false;
			}
			
			//Check if passed coordinates are in range of board array
			if ((horz < 0) || (horz > 7)) {
				return false;
			}
			else if ((vert < 0) || (vert > 7)) {
				return false;
			}
			
			/*
			//Check if coordinate is empty; if empty, return true
			else if (Pieces.isEmpty(board, horz, vert)) {
				//Mark Squares object as threatened
				board[horz][vert].setIsThreatened(true);
				return true;
			}
			*/
			
			//Check if coordinate has enemy piece or is empty; returns true if either condition is met
			else if ((Pieces.isDifferentColor(this, board[horz][vert]) || (Pieces.isEmpty(board, horz, vert)))) {
				//Switch statement to set appropriate color threats
				switch(this.color) {
				case 'w':	board[horz][vert].setIsThreatenedByWhite(true);
							break;
				case 'b':	board[horz][vert].setIsThreatenedByBlack(true);
							break;
				}
				
				return true;
			}
			//If square is not empty and doesnt have enemy piece, mark friendly piece as not hanging and return false
			else {
				board[horz][vert].setIsHanging(false);
				return false;
			}
		} catch (NullPointerException n) {
			//If null pointer is caught, passed coorindate is not valid; return false
			return false;
		} catch (IndexOutOfBoundsException i) {
			//If index out of bounds is caught, passed coordinate is not valid; return false
			return false;
		}
	}
	
	//DESCRIPTION:		Method calculates number of squares threatened by calling knight
	//PRE-CONDITION:	Passed board and coordinate must be valid and initialized
	//POST-CONDITION:	Returns number of squares threatened by knight
	public int threatenedSquares(Pieces[][] board) {
		
		int threat = 0;
		
		//Check all 8 possible squares
		//Exceptions are handled in helper methods, so no need to check coordinates
		
		//A1 direction
		//Long
		if (this.canLong(board, this.horz - 2, this.vert + 1)) {
			threat++;
			Pieces.markThreat(board, this, this.horz - 2, this.vert + 1);
		}
		//Tall
		if (this.canTall(board, this.horz - 1, this.vert + 2)) {
			threat++;
			switch(this.color) {
			case 'w':	board[this.horz - 1][this.vert + 2].setIsThreatenedByWhite(true);
						break;
			case 'b':	board[this.horz - 1][this.vert + 2].setIsThreatenedByBlack(true);
						break;
			}
		}
		
		
		
		//A8 direction
		//Long
		if (this.canLong(board, this.horz - 2, this.vert - 1)) {
			threat++;
			switch(this.color) {
			case 'w':	board[this.horz - 2][this.vert - 1].setIsThreatenedByWhite(true);
						break;
			case 'b':	board[this.horz - 2][this.vert - 1].setIsThreatenedByBlack(true);
						break;
			}
		}
		//Tall
		if (this.canTall(board, this.horz - 1, this.vert - 2)) {
			threat++;
			switch(this.color) {
			case 'w':	board[this.horz - 1][this.vert - 2].setIsThreatenedByWhite(true);
						break;
			case 'b':	board[this.horz - 1][this.vert - 2].setIsThreatenedByBlack(true);
						break;
			}		}
		
		
		//H1 direction
		//Long
		if (this.canLong(board, this.horz + 2, this.vert + 1)) {
			threat++;
			switch(this.color) {
			case 'w':	board[this.horz + 2][this.vert + 1].setIsThreatenedByWhite(true);
						break;
			case 'b':	board[this.horz + 2][this.vert + 1].setIsThreatenedByBlack(true);
						break;
			}
		}
		//Tall
		if (this.canTall(board, this.horz + 1, this.vert + 2)) {
			threat++;
			switch(this.color) {
			case 'w':	board[this.horz + 1][this.vert + 2].setIsThreatenedByWhite(true);
						break;
			case 'b':	board[this.horz + 1][this.vert + 2].setIsThreatenedByBlack(true);
						break;
			}
		}
		
		
		//H8 direction
		//Long
		if (this.canLong(board, this.horz + 2, this.vert - 1)) {
			threat++;
			switch(this.color) {
			case 'w':	board[this.horz + 2][this.vert - 1].setIsThreatenedByWhite(true);
						break;
			case 'b':	board[this.horz + 2][this.vert - 1].setIsThreatenedByBlack(true);
						break;
			}
		}
		//Tall
		if (this.canTall(board, this.horz + 1, this.vert - 2)) {
			threat++;
			switch(this.color) {
			case 'w':	board[this.horz + 1][this.vert - 2].setIsThreatenedByWhite(true);
						break;
			case 'b':	board[this.horz + 1][this.vert - 2].setIsThreatenedByBlack(true);
						break;
			}
		}
		
		//After all checks, return number of threatened squares
		return threat;
	}
	
	//DESCRIPTION:		Method calculates number of pieces threatened by calling Knight
	//PRE-CONDITION:	Passed board must be fully initialized and calling knight must be constructed
	//POST-CONDITION:	Number of pieces threatened by calling knight is returned
	public int threatenedPieces(Pieces[][] board) {
		int dummy = 0;
		int lcv = 0;
		//Check all 8 coordinates using lcv controlled while loop
		
		//Reset pieces ArrayList to prevent duplicate MoveList objects
		this.possibleAttacks = new ArrayList<Pieces>();
		
		while (lcv < 8) {
			try {
				//A1 direction
				//Long
				if (lcv == 0) {
					if ((this.canLong(board, this.horz - 2, this.vert + 1) && 
							Pieces.isEnemyPiece(board, this, horz - 2, vert + 1))) {
						dummy += board[this.horz - 2][this.vert + 1].getValue();
						Pieces.markThreat(board, this, this.horz - 2, this.vert + 1);
						//Add new MoveList object to pieces using found coordinates
						this.possibleAttacks.add(board[this.horz - 2][this.vert + 1]);
						lcv++;
					}
					else {
						lcv++;
					}
				}
				
				//Tall
				else if (lcv == 1) {
					if ((this.canTall(board, this.horz - 1, this.vert + 2) && 
							Pieces.isEnemyPiece(board, this, horz - 1, vert + 2))) {
						dummy += board[this.horz - 1][this.vert + 2].getValue();
						Pieces.markThreat(board, this, this.horz - 1, this.vert + 2);
						this.possibleAttacks.add(board[this.horz - 1][this.vert + 2]);
						lcv++;
					} 
					else {
						lcv++;
					}
				}
				
				else if (lcv == 2){
					if ((this.canLong(board, this.horz - 2, this.vert - 1)) && 
							Pieces.isEnemyPiece(board, this, horz - 2, vert - 1)) {
						dummy += board[this.horz - 2][this.vert - 1].getValue();
						Pieces.markThreat(board, this, this.horz - 2, this.vert - 1);
						this.possibleAttacks.add(board[this.horz - 2][this.vert - 1]);
						lcv++;
					}
					else {
						lcv++;
					}
				}
				
				//Tall
				else  if (lcv == 3) {
					if ((this.canTall(board, this.horz - 1, this.vert - 2) && 
							Pieces.isEnemyPiece(board, this, horz - 1, vert - 2))) {
						dummy+= board[this.horz - 1][this.vert - 2].getValue();
						Pieces.markThreat(board, this, this.horz - 1, this.vert - 2);
						this.possibleAttacks.add(board[this.horz - 1][this.vert - 2]);
						lcv++;
					} 
					else {
						lcv++;
					}
				}
				
				else if (lcv == 4) {
					if ((this.canLong(board, this.horz + 2, this.vert + 1) && 
							Pieces.isEnemyPiece(board, this, horz + 2, vert + 1))) {
						dummy += board[this.horz + 2][this.vert + 1].getValue();
						Pieces.markThreat(board, this, this.horz + 2, this.vert + 1);
						this.possibleAttacks.add(board[this.horz + 2][this.vert + 1]);
						lcv++;
					}
					else {
						lcv++;
					}
				}
				//Tall
				else if (lcv == 5) {
					if ((this.canTall(board, this.horz + 1, this.vert + 2) && 
							Pieces.isEnemyPiece(board, this, horz + 1, vert + 2))) {
						dummy+= board[this.horz + 1][this.vert + 2].getValue();
						Pieces.markThreat(board, this, this.horz + 2, this.vert + 1);
						this.possibleAttacks.add(board[this.horz + 1][this.vert + 2]);
						lcv++;
					}
					else {
						lcv++;
					}
				}
				
				//H8 direction
				
				//Long
			else if (lcv == 6) {
				if ((this.canLong(board, this.horz + 2, this.vert - 1) && 
						Pieces.isEnemyPiece(board, this, horz + 2, vert - 1))) {
					dummy += board[this.horz + 2][this.vert - 1].getValue();
					Pieces.markThreat(board, this, this.horz + 2, this.vert - 1);
					this.possibleAttacks.add(board[this.horz + 2][this.vert - 1]);
					lcv++;
				}
				else {
					lcv++;
				}
			}
			else if (lcv == 7) {
				//Tall
				if ((this.canTall(board, this.horz + 1, this.vert - 2) && 
						Pieces.isEnemyPiece(board, this, horz + 1, vert - 2))) {
					dummy += board[this.horz + 1][this.vert - 2].getValue();
					Pieces.markThreat(board, this, this.horz + 1, this.vert - 2);
					this.possibleAttacks.add(board[this.horz + 1][this.vert - 2]);
					lcv++;
				}
				else {
					lcv++;
				}
			}
			//If either exception is caught, increment lcv to go to next condition check
			} catch (IndexOutOfBoundsException i) {
				
				lcv++;
			} catch (NullPointerException n) {
				lcv++;
			}
		}
		
		return dummy;
	}
	
	//DESCRIPTION:		Method determines if calling knight is forking 2+ pieces
	//PRE-CONDITION:	Passed board and calling knight must both be initialized
	//POST-CONDITION:	If 2+ pieces are attack on square, return true; else, false
	public boolean canFork(Pieces[][] board) {
		try { //If exception is caught, pieces is not filled; return false
			if (this.possibleAttacks.size() > 1) {
				return true;
			}
			else {
				return false;
			}
		} catch (NullPointerException n) {
			return false;
		}
	}
	
	
	/********* GAME METHODS **************/
	
	
	
	
	/********* HELPER METHODS ***********/
	//DESCRIPTION:		Method checks if knight can fork king safely; returns true if safe fork is possible at passed coordinate
	//PRE-CONDITION:	Passed coordinate must be valid, with pieces filled with at least one MoveList; called when knight is moved
	//POST-CONDITION:	Returns true if king can be forked; else, false
	public boolean canForkKing(Pieces[][] board) {
		//Try catch block in case NullPointer is thrown; return false if thrown
		try {
			//If pieces is not greater than 1, fork is not possible; return false
			if (this.possibleAttacks.size() < 2) {
				return false;
			}
			//Search for King; if not present in pieces array, return false
			
				//For loop to check coordinates stored in arraylist
			for(int i = 0; i < this.possibleAttacks.size(); i++) {
				//If king is at passed coordinate, return true;
				if (board[this.possibleAttacks.get(i).getHorz()][this.possibleAttacks.get(i).getVert()] instanceof King) {
					return true;
				}
				else {
					continue;
				}
			}
		} catch (NullPointerException n) {
			//if null pointer is caught, no pieces are threatened; return false
			return false;
		}
		
		//If King is not found in loop, return false
		return false;
	}
	
	
	/*
	//DESCRIPTION:		Method checks value of pieces forked by calling knight; returns MoveList object with highest value
	//PRE-CONDITION:	pieces ArrayList must have at least 2 objects stored; only called when knight has moved
	//POST-CONDITION:	Returns MoveList object of piece with highest value
	public MoveList fork(Pieces[][] board) {
		//If fork is not possible, return null
		if (!(this.canFork(board))) {
			return null;
		}
		
		double value = 0;
		int index = 0;
		try {
			//for loop to check pieces
			for (int i = 0; i < this.pieces.size(); i++) {
				//Add value of all forked pieces to 
				value += board[this.pieces.get(i).getPositionHorz()][this.pieces.get(i).getPositionVert()].getValue();
			}
		} catch (NullPointerException n) {
			//Try catch just in case
			return null;
		}
		//Check if king can be forked; if can be forked and calling piece is not threatened, return highest value
		//piece that is being forked
		if (this.canForkKing(board)) {
			//Check for highest value piece; save value for later
			//Check if current knight square is safe based on color
			switch (this.color) {
			case 'w':
				if (this.isThreatenedByBlack) {
					//if piece is threatened, subtract knight value from move value
					bestValue -= this.value;
					break;
				}
			case 'b':
				if (this.isThreatenedByWhite) {
					//Decrease value by value of knight
					bestValue -= this.value;
					break;
				}
			}
		}
		
		//Return MoveList containing coordinates for fork
		MoveList dummy = new MoveList(this.horz, this.vert);
		dummy.setMoveValue(bestValue);
		return new MoveList(dummy);
		
	}
	*/
	
	//DESCRIPTION:		Instantiates starting knight pieces based on color passed	
	//PRE-CONDITION:	char passed must be 'w' or 'b'
	//POST-CONDITION:	Two knight pieces are created and placed on the board array
	public static void placeKnight(Pieces[][] board) {
		
		//Create white side knights B1 (1,0) G1 (6,0)
		//Queen side knight
		board[1][0] = new Knight('b');
		board[1][0].setHorz(1);
		
		//King side knight
		board[6][0] = new Knight('b');
		board[6][0].setHorz(6);
		
		
		//Create black side knights B8 (1,7) G8(6,7)
		//Queen side knight
		board[1][7] = new Knight('w');
		board[1][7].setHorz(1);
		
		//King side knights
		board[6][7] = new Knight('w');
		board[6][7].setHorz(6);
	}




}
