package Bacchus;
import java.io.File;
import java.util.*;
//import java.math.*;

public class Bacchus {
	
	/********** INSTANCE VARIABLES *************/
	protected File openings;
	protected Scanner reader;
	protected String[][][] knownGames;
	protected int gameNums;
	protected int[] gameCheck;
	protected char bacchusColor;
	protected char playerColor;
	protected MoveList bestMove;
	
	protected Pieces[][] testBoard;
	
	protected ArrayList<MoveList> pieces;
	protected ArrayList<MoveList> enemyPieces;
	protected ArrayList<MoveList> possibleMoves;
	protected ArrayList<MoveList> calculatedMoves;

	protected Defense defense;
	protected Offense offense;
		
			
		/********* DEFENSE CLASS *******/

			public class Defense {
				protected double total;
				protected double hanging;
				protected double threatened;
				protected boolean canBeChecked;
				protected Pieces[][] defenseBoard;
				//defenseMoves tests set of moves and stores values; highest 3 values are stored in possibleDefLines
				//protected LinkedList<MoveList> defenseMoves;
				//protected LinkedList<MoveList>[] possibleDefLines;
				
				//testMoves 
				protected ArrayList<MoveList> testMoves;
				protected ArrayList<MoveList> defensePieces;
				/**** CONSTRUCTORS *******/
				public Defense() {
					total = 0;
					hanging = 0;
					threatened = 0;
					canBeChecked = false;
					defenseBoard = new Pieces[8][8];
					//When defense is instantiated, automatically duplicate Bacchus board
					Bacchus.this.duplicateBoard(Bacchus.this.testBoard, this.defenseBoard);
					//Linked lists stores chains of possible moves, 
					//defenseMoves = new LinkedList<MoveList>();
					//possibleDefLines = new LinkedList[3];
					//ArrayLists store which pieces can move and values of their moves
					testMoves = new ArrayList<MoveList>();
					//Holds coordinates of all friendly pieces
					//pieces = new ArrayList<MoveList>();
					//Bacchus.this.duplicatePieces(Bacchus.this.pieces, this.defensePieces);
				}
				
				//DESCRIPTION:		Method calculates value of hanging pieces
				//PRE-CONDITION:	Defense object must be fully instantiated with complete ArrayLists
				//POST-CONDITION:	Value of all friendly pieces is returned
				public int hangingPieces(Pieces[][] board, ArrayList<MoveList> pieceList, char color) {
					//Loop through pieces ArrayList to find hanging pieces
					int hanging = 0;
					//Use threatenUpdate to mark pieces as hanging or not
					Chess.threatenUpdate(board);
					//Variables for storing piece coordinates to prevent stroke
					int horz = 0;
					int vert = 0;
					for (int i = 0; i < pieceList.size(); i++) {
						//Reset value of horz and vert with each loop
						horz = pieceList.get(i).getPositionHorz();
						vert = pieceList.get(i).getPositionVert();
						//If piece is hanging and has same color, add value to hanging
						if (board[horz][vert].getIsHanging() && (board[horz][vert].getColor() == color)) {
							hanging += board[horz][vert].getValue() * 2;
							//If piece is queen, subtract extra 9
							if (board[horz][vert] instanceof Queen) {
								hanging += 9;
							}
						}
					}
					
					return hanging;
				}
				
				
				//DESCRIPTION:		Method calculates number of threatened pieces
				//PRE-CONDITION:	pieces must be initialized and defenseBoad must be already duplicated
				//POST-CONDITION:	Value of all threatened pieces is returned
				public int threatenedPieces(Pieces[][] board, ArrayList<MoveList> pieceList, char color) {
					//Loop through pieces to find threatened pieces
					int threatened = 0;
					
					//Variable for storing horz and vert values so no stroking out
					int horz, vert;

					for (int i = 0; i < pieceList.size(); i++) {
						horz = pieceList.get(i).getPositionHorz();
						vert = pieceList.get(i).getPositionVert();
						
						//If piece is threatened and is same color, add piece value to threatened
						switch(board[horz][vert].getColor()) {
						case 'w':
							//If coordinate is threatened by black, add piece value to threatened
							if (board[horz][vert].getIsThreatenedByBlack()) {
								threatened += board[horz][vert].getValue() * 2;
							}
							break;
						case 'b':
							if (board[horz][vert].getIsThreatenedByWhite()) {
								threatened += board[horz][vert].getValue() * 2;
							}
							break;
						}
					}
					
					return threatened;
				}
				
				//DESCRIPTION:		
				//PRE-CONDITION:	
				//POST-CONDITION:	

			}

		
		
		/***** OFFENSE CLASS **********/
			public class Offense {
				protected double total;			
				protected Pieces[][] offenseBoard;
				//protected LinkedList<MoveList> offenseMoves;
				//protected LinkedList<MoveList>[] possibleOffMoves;
				
				protected ArrayList<MoveList> pieces;
				protected ArrayList<MoveList> testMoves;
				/**** CONSTRUCTORS *********/
				public Offense() {
					total = 0;
					offenseBoard = new Pieces[8][8];
					Bacchus.this.duplicateBoard(Bacchus.this.testBoard, this.offenseBoard);
					//offenseMoves = new LinkedList<MoveList>();
					//possibleOffMoves = new LinkedList[3];
					
					pieces = new ArrayList<MoveList>();
					Bacchus.this.duplicatePieces(Bacchus.this.pieces, this.pieces);
					testMoves = new ArrayList<MoveList>();
				}
				
				//DESCRIPTION:		Method calculates value of enemy pinned pieces
				//PRE-CONDITION:	Calling object and passed boards must be initialized
				//POST-CONDITION:	Value of pinned enemy pieces is returned
				public int pinnedPieces(Pieces[][] board, ArrayList<MoveList> pieceList, char enemyColor) {
					int pinnedValue = 0;
					//For loop to search each piece
					//variables to store piece coordinates
					int horz, vert;
					horz = vert = 0;
					for (int i = 0; i < pieceList.size(); i++) {
						horz = pieceList.get(i).getPositionHorz();
						vert = pieceList.get(i).getPositionVert();
						
						//If passed coordinate is pinned, add piece value
						if ((board[horz][vert].getIsPinnedDiagonal()) || (board[horz][vert].getIsPinnedHorz()) 
								|| (board[horz][vert].getIsPinnedVert())) {
							//If found piece has same color as passed enemyColor, add value of piece
							if (board[horz][vert].getColor() == enemyColor) {
								pinnedValue += board[horz][vert].getValue();
							}
						}
					}//End for loop
					return pinnedValue;
				}
			}
	
			
	
	/********* CONSTRUCTORS ***********/
	
	//DESCRIPTION:		Default constructor for Bacchus objects
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	New Bacchus object is constructed
	public Bacchus(Pieces[][] board, char color) throws Exception{
		
		gameNums = 0;
		//Switch statement to determine which color 
		switch(color) {
		case 'w': this.bacchusColor = 'w';
				  this.playerColor = 'b';
				  break;
		case 'b': this.bacchusColor = 'b';
				  this.playerColor = 'w';
				  break;
		//If invalid color is passed, exit program
		default:
			System.out.println("ERROR: Invalid colors chosen. Exiting...");
			System.exit(0);
		}
		
		testBoard = new Pieces[8][8];
		this.duplicateBoard(board, testBoard);
		pieces = new ArrayList<MoveList>();
		enemyPieces = new ArrayList<MoveList>();
		possibleMoves = new ArrayList<MoveList>();
		bestMove = new MoveList();
		calculatedMoves = new ArrayList<MoveList>();

		//Switch statement to populate both MoveList ArrayLists
		switch (color) {
		//White Bacchus case
		case 'w': this.populatePieces(board, this.pieces, color);
				  this.populatePieces(board, this.enemyPieces, 'b');
				  break;
		case 'b': this.populatePieces(board, this.pieces, color);
				  this.populatePieces(board, this.enemyPieces, 'w');
				  break;
	    default: //If neither color passed is b or w, exit program
	    		  System.out.println("ERROR: Cannot initialize Bacchus. Exiting...");
	    		  System.exit(0);
		}
		defense = new Defense();
		offense = new Offense();
		
		/*
		openings = new File("openings.txt");

		//Instantiates Scanner object if text file is found
		if (openings.exists()) {
			reader = new Scanner(openings);
			
			
			//Scanner reads each line to determine how many lines there are 
			while (reader.hasNextLine()) {
				//String pieceList = "";
				
				//pieceList = reader.nextLine();
				gameNums++;
			}
			
			//Instantiate knownGames using number of lines recorded; knownGames[gameNumber][turnNumber][0 for white/ 1 for black]
			knownGames = new String[gameNums][40][2];
			
			//Instantiate gameIndex using gameNums
			gameCheck = new int[gameNums];
			//Close Scanner object once done with construction
			reader.close();
		
		}
		
		*/
	}
	
	
	
	/********** ACCESSORS ************/
	//DESCRIPTION:		Returns gameNums value of calling object
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	gameNums value is returned
	public int getGameNums() {
		return gameNums;
	}

	//DESCRIPTION:		Returns move made at a specific turn in a game
	//PRE-CONDITION:	Game position must be same, and index passed must be less than 40
	//POST-CONDITION:	Move made during that turn is returned
	public String getTurnMove(int turn, char color) {
		//Check which turn move has been made, use 0/1 for w/b turns
		try { 
			switch(color) {
			
			case'w':
				return knownGames[0][turn][0];
			case 'b':
				return knownGames[0][turn][1];
			default:
				return "Invalid";
			}
		//If null pointer is caught, move does not exist
		} catch (NullPointerException n) {
			return "null";
		//If index out of bounds, no games match length
		} catch (IndexOutOfBoundsException i) {
			return "null";
		}
	}
	
	//DESCRIPTION:		Returns number of games being tracked during match
	//PRE-CONDITION:	Calling Bacchus object must be fully initialized
	//POST-CONDITION:	Returns number of games being tracked
	public int getTrackedGames() {
		return this.gameCheck.length;
	}
	
	//DESCRIPTION:		Returns size of pieces ArrayList
	//PRE-CONDITION:	pieces ArrayList must be filled with valid MoveList objects
	//POST-CONDITION:	Size of pieces ArrayList is returned
	public int getNumPieces() {
		return this.pieces.size();
	}
	
	//DESCRIPTION:		Returns MoveList object in pieces ArrayList using passed int index
	//PRE-CONDITION:	pieces must be populated with valid MoveList objects
	//POST-CONDITION:	MoveList object at passed index is returned
	public MoveList getPiece(int index) {
		return this.pieces.get(index);
	}
	
	//DESCRIPTION:		Returns memory address of Bacchus pieces ArrayList
	//PRE-CONDITION:	Bacchus must be fully instantiated with pieces populated
	//POST-CONDITION:	Memory address of pieces is returned
	public ArrayList<MoveList> getPiecesArray(){
		return this.pieces;
	}
	
	/********** ENGINE METHODS ***************/
	//DESCRIPTION:		Method takes in board and moves piece on board after calculation
	//PRE-CONDITION:	Bacchus must be fully instantiated and board must be valid
	//POST-CONDITION:	Piece that Bacchus selects is moved on passed board
	public void movePiece(Pieces[][] board) {
		//Variables for selecting which of the three moves to perform
		double rando = Math.abs((Math.random() * 10) - 6);
		//While loop to check for valid rando values
		while ((rando < 0) || (rando > 3)) {
			rando = Math.abs((Math.random() * 10) - 6);
		}
		//Run best moves before calculation
		this.bestMove(board, this.pieces);
		MoveList pieceList = this.calculatedMoves.get((int)rando);
		//After calculation, move piece using stored coordinates
		board[pieceList.getPositionHorz()][pieceList.getPositionVert()].move(board, pieceList.getMoveHorz(), pieceList.getMoveVert());
		//Chess.threatenUpdate(board);
	}
	
	//DESCRIPTION:		Method uses LinkedList object to calculate best string of moves; returns first move with best total value
	//PRE-CONDITION:	Bacchus must be fully instantiated and possibleMoves, pieces, and enemy pieces must be populated
	//POST-CONDITION:	MoveList with best total value is returned
	public MoveList bestMoveString(Pieces[][] board, char color) {
		//LinkedList used for keeping tally of moves
		LinkedList moveString = new LinkedList();
		//Duplicate pieces, enemyPieces, and testBoard before each calculation
		this.populatePieces(board, this.pieces, this.bacchusColor);
		this.populatePieces(board, this.enemyPieces, this.playerColor);
		//Reset possibleMoves before next calculation
		//this.possibleMoves = new ArrayList<MoveList>();
		//Populate possible moves before full calculation
		this.populatePossibleMoves(board, this.pieces, color);
		//pieceList MoveList for pointing at returned MoveList objects
		MoveList placeholder;
		//Int stores index of best move in possibleMoves and total values
		double bestValue, currentValue;
		bestValue = currentValue = 0;
		int index = 0;
		//For loop to loop through all possible moves
		for (int i = 0; i < this.possibleMoves.size(); i++) {
			//Duplicate board before each individual calculation
			this.duplicateBoard(board, this.testBoard);
			//Add current possibleMove to moveString list
			moveString.addNode(this.possibleMoves.get(i));
			//Move piece after each duplication
			this.testBoard[this.possibleMoves.get(i).getPositionHorz()][this.possibleMoves.get(i).getPositionVert()].move(this.testBoard, 
						this.possibleMoves.get(i).getMoveHorz(), this.possibleMoves.get(i).getMoveVert());
			//For loop to calculate first few moves
			for (int n = 0; n < 2; n++) {
				//Try catch block for any shenanigans
				try {
					//Calculate next enemy move
					placeholder = this.bestMove(this.testBoard, this.enemyPieces);
					//Append placeholder to LinkedList for storage
					moveString.addNode(placeholder);
					//Move enemy piece
					this.testBoard[placeholder.getPositionHorz()][placeholder.getPositionVert()].move(this.testBoard,
								placeholder.getMoveHorz(), placeholder.getMoveVert());
					//Update both pieces ArrayLists for next move Calculation
					this.updatePieces(this.testBoard);
					//Calculate next Bacchus move
					placeholder = this.bestMove(this.testBoard, this.pieces);
					//Move Bacchus piece
					this.testBoard[placeholder.getPositionHorz()][placeholder.getPositionVert()].move(this.testBoard,
									placeholder.getMoveHorz(), placeholder.getMoveVert());
					moveString.addNode(placeholder);
					//Update pieces for next calculation
					this.updatePieces(this.testBoard);
				} catch (NullPointerException nu) {
					break;
				}
			}
			//After all movement calculations are done, find total value
			currentValue = moveString.getTotalValue();
			//If current moveString has higher value, save index, set new best value, and reset currentValue and linkedlist
				if (currentValue > bestValue) {
					index = i;
					bestValue = currentValue;
					currentValue = 0;
					moveString = new LinkedList();
				}
				//If no new best is found, reset LinkedList
				else {
					moveString = new LinkedList();
				}
			}
		//After all calculations are done, return MoveList with best value at index
		return new MoveList(this.possibleMoves.get(index));
	}
	
	//DESCRIPTION:		Main method for Bacchus to calculate first set of possible moves with different values based on passed color
	//PRE-CONDITION:	Bacchus must be fully initialized with a proper board array passed
	//POST-CONDITION:	possibleMoves is populated with highest value moves
	public void populatePossibleMoves(Pieces[][] board, ArrayList<MoveList> pieceList, char color) {
		//Reset possibleMoves before entry
		this.possibleMoves = new ArrayList<MoveList>();
		//Populate friendly moves
		if (color == this.bacchusColor) {
			//Populate friendly pieces before each loop
			this.populatePieces(board, pieceList, color);
			//Loop through friendly pieces, use switch statement to use proper methods
				for (int i = 0; i < pieceList.size(); i++) {
					try {
						//Duplicate board before each iteration
						this.duplicateBoard(board, this.testBoard);
						//Switch statement using piece char in each piece
						switch (this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()].getPiece()) {
							//Pawn case
						case 'P':
							this.possibleMoves.add(this.pawnHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						case 'N':
							this.possibleMoves.add(this.knightHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						case 'B':
							this.possibleMoves.add(this.bishopHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						case 'R':
							this.possibleMoves.add(this.rookHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						case 'Q':
							this.possibleMoves.add(this.queenHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						//King case; using queen movement method as movement is identical
						case 'K':
							this.possibleMoves.add(this.kingPossibleMoves(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						//If all fails, break;
						default:
							break;
						}
						//If either exception is caught, continue in loop
					} catch (NullPointerException n) {
						continue;
					} catch (IndexOutOfBoundsException ind) {
						continue;
				}
			}
		}
		//If color passed is not Bacchus color, must be enemy move
		else  {
			//Populate friendly pieces before each loop
			this.populatePieces(board, pieceList, color);
			//Loop through friendly pieces, use switch statement to use proper methods
				for (int i = 0; i < pieceList.size(); i++) {
					try {
						//Duplicate board before each iteration
						this.duplicateBoard(board, this.testBoard);
						//Switch statement using piece char in each piece
						switch (this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()].getPiece()) {
							//Pawn case
						case 'P':
							this.possibleMoves.add(this.pawnHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						case 'N':
							this.possibleMoves.add(this.knightHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						case 'B':
							this.possibleMoves.add(this.bishopHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						case 'R':
							this.possibleMoves.add(this.rookHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						case 'Q':
							this.possibleMoves.add(this.queenHighestMove(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						//King case; using queen movement method as movement is identical
						case 'K':
							this.possibleMoves.add(this.kingPossibleMoves(this.testBoard, 
									this.testBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()], pieceList));
							break;
						//If all fails, break;
						default:
							break;
						}
						//If either exception is caught, continue in loop
					} catch (NullPointerException n) {
						continue;
					} catch (IndexOutOfBoundsException ind) {
						continue;
				}
			}
		}
		
		//Once all calculations are complete, return testBoard back to original board state
		this.duplicateBoard(board, this.testBoard);
	}
	
	//DESCRPITION:		Bacchus analyzes each piece and position of board using numerical values assigned to each situation
	//PRE-CONDITION:	Passed board must be fully initialized with no null indices
	//POST-CONDITION:	Total assessment value of the board is returned
	public double calculateOffense(Pieces[][] board, ArrayList<MoveList> pieceList) {
		//Tracks value of move thus far
		double boardValue = 0;
		
		//Variables to store coordinate found in MoveList objects
		int horz, vert;
		horz = vert = 0;
		
		//Duplicate board to prevent overriding of passed board
		Pieces[][] dummyBoard = new Pieces[8][8];
		this.duplicateBoard(board, dummyBoard);
		
		ArrayList<MoveList> doublepieceList = new ArrayList<MoveList>();
		this.populatePieces(dummyBoard, doublepieceList, dummyBoard[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor());
		
		for (int i = 0; i < pieceList.size(); i++) {
			//Reset horz and vert values before each iteration
			horz = doublepieceList.get(i).getPositionHorz();
			vert = doublepieceList.get(i).getPositionVert();
			
			//Add values of threatenedSquares and threatenedPieces to boardValue
			boardValue += ((double)dummyBoard[horz][vert].threatenedSquares(dummyBoard) / 8) + dummyBoard[horz][vert].threatenedPieces(dummyBoard);
			//If piece can attack other pieces, check conditions
			//Try catch in case of janky pointers
			try {
				
				if (dummyBoard[horz][vert].getPossibleAttacks().size() > 0) {
				//If piece can attack other piece, check if piece is hanging; else, if it's a good trade
					for (int n = 0; i < dummyBoard[horz][vert].getPossibleAttacks().size(); n++) {
						int enemyHorz = dummyBoard[horz][vert].getPossibleAttackedPiece(n).getHorz();
						int enemyVert = dummyBoard[horz][vert].getPossibleAttackedPiece(n).getVert();
					
					//Hanging piece check
						if (dummyBoard[enemyHorz][enemyVert].getIsHanging()) {
						//If piece is hanging, add value of piece to attack
							boardValue += dummyBoard[enemyHorz][enemyVert].getValue();
						}
					//Else if piece is not hanging, check if trade would be good
						else if (dummyBoard[horz][vert].getValue() < dummyBoard[enemyHorz][enemyVert].getValue()) {
							boardValue += dummyBoard[enemyHorz][enemyVert].getValue() - dummyBoard[horz][vert].getValue();
						}
					}
				}
			} catch (IndexOutOfBoundsException ni) {
				
			} catch (NullPointerException nu) {
				
			}
		}
		
		return boardValue;
	}

	//DESCRIPTION:		Bacchus analyzes board for defensive value of current position 
	//PRE-CONDITION:	Passed board and ArrayList must be valid with no null indices
	//POST-CONDITION:	Defense value for passed ArrayList is calculated and returned
	public double calculateDefense(Pieces[][] board, ArrayList<MoveList> pieceList) {
		double boardValue = 0;
		char color;
		//Determine color to calculate based on color in pieceList ArrayList; use first index to check
		//try catch in case passed ArrayList is null
		try {
			color = board[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor();
		} catch (NullPointerException n) {
			//If null pointer is caught, return stupid value 
			return -200;
		} catch (IndexOutOfBoundsException i) {
			return -200;
		}
		
		//Duplicate board for testing
		Pieces[][] dummyBoard = new Pieces[8][8];
		this.duplicateBoard(board, dummyBoard);
		
		//Use defense methods to calculate values
		boardValue -= this.defense.hangingPieces(dummyBoard, pieceList, color);
		boardValue -= this.defense.threatenedPieces(dummyBoard, pieceList, color);
		
		//If piece is moving to safe square but will be threatened by piece with less value, subtract difference of piece value from moveValue
		//For loop to check all pieces, using enemyPieces and try/catch for any weird stuff
		int enemyHorz, enemyVert, friendlyHorz, friendlyVert;
		for (int i = 0; i < this.enemyPieces.size(); i++) {
			enemyHorz = this.enemyPieces.get(i).getPositionHorz();
			enemyVert = this.enemyPieces.get(i).getPositionVert();
			//Check if passed piece is in any of the threatenedPiece lists; if present after moving to coordinate, subtract difference of value
			//Nested for loop to check possibleAttacks ArrayList in referenced enemy piece
			try {
				for (int n = 0; n < dummyBoard[enemyHorz][enemyVert].getPossibleAttacks().size(); n++) {
						friendlyHorz = dummyBoard[enemyHorz][enemyVert].getPossibleAttackedPiece(n).getHorz();
						friendlyVert = dummyBoard[enemyHorz][enemyVert].getPossibleAttackedPiece(n).getVert();
						//If value of of enemy piece is less than threatened friendly, subtract difference from boardValue
						if (dummyBoard[enemyHorz][enemyVert].getValue() < dummyBoard[friendlyHorz][friendlyVert].getValue()) {
							boardValue -= (dummyBoard[friendlyHorz][friendlyVert].getValue() - dummyBoard[enemyHorz][enemyVert].getValue());
						}
					}
				}
			//If either exception is caught while checking, continue in outer for loop
			catch (IndexOutOfBoundsException ind) {
				continue;
			} catch (NullPointerException nu) {
				continue;
			}
		}
		
		
		return boardValue;
	}
	
	//DESCRIPTION:		Method calculates single highest move among all pieces in passed ArrayList
	//PRE-CONDITION:	All passed parameters must be fully instantiated and initialized
	//POST-CONDITION:	Returns MoveList with single highest move of all pieces
	public MoveList bestMove(Pieces[][] board, ArrayList<MoveList> pieceList) {
		//Error check for empty array list; try catch for null array list
		try {
			if (pieceList.size() == 0) {
				return new MoveList();
			}
		} catch (NullPointerException n) {
			return new MoveList();
		}
		
		//pieceList MoveList object for pointing at current best move
		MoveList pieceListMove =  new MoveList();
		pieceListMove.setMoveValue(0);
		MoveList testMove = new MoveList();
		//pieceList piece for easy storage of piece coordinates
		Pieces pieceListPiece;
		//pieceList board for testing moves
		Pieces[][] dummyBoard = new Pieces[8][8];
		this.duplicateBoard(board, dummyBoard);
		
		//For loop to check best move for each piece; if move has higher value, pieceListMove points at that move
		for (int i = 0; i < pieceList.size(); i++) {
			//Duplicate board before each iteration
			this.duplicateBoard(board, dummyBoard);
			//Change values of pieceListPiece
			pieceListPiece = dummyBoard[pieceList.get(i).getPositionHorz()][pieceList.get(i).getPositionVert()];
			//Switch statement for different piece types
			try {
				switch(pieceListPiece.getPiece()) {
				//Pawn case 
				case 'P':
					testMove = this.pawnHighestMove(dummyBoard, pieceListPiece, pieceList);
					//If move has higher value, duplicate testMove onto pieceListMove
					if (testMove.getMoveValue() > pieceListMove.getMoveValue()) {
						pieceListMove = new MoveList(testMove);
					}
					break;
				case 'N':
					testMove = this.knightHighestMove(dummyBoard, pieceListPiece, pieceList);
					//If move has higher value, duplicate testMove onto pieceListMove
					if (testMove.getMoveValue() > pieceListMove.getMoveValue()) {
						pieceListMove = new MoveList(testMove);
					}
					break;
				case 'B':
					testMove = this.bishopHighestMove(dummyBoard, pieceListPiece, pieceList);
					//If move has higher value, duplicate testMove onto pieceListMove
					if (testMove.getMoveValue() > pieceListMove.getMoveValue()) {
						pieceListMove = new MoveList(testMove);
					}
					break;
				case 'R':
					testMove = this.rookHighestMove(dummyBoard, pieceListPiece, pieceList);
					//If move has higher value, duplicate testMove onto pieceListMove
					if (testMove.getMoveValue() > pieceListMove.getMoveValue()) {
						pieceListMove = new MoveList(testMove);
					}
					break;
				case 'Q':
					testMove = this.queenHighestMove(dummyBoard, pieceListPiece, pieceList);
					//If move has higher value, duplicate testMove onto pieceListMove
					if (testMove.getMoveValue() > pieceListMove.getMoveValue()) {
						pieceListMove = new MoveList(testMove);
					}
					break;
				//King case; use same as queen methods
				case 'K':
					testMove = this.kingPossibleMoves(dummyBoard, pieceListPiece, pieceList);
					//If move has higher value, duplicate testMove onto pieceListMove
					if (testMove.getMoveValue() > pieceListMove.getMoveValue()) {
						pieceListMove = new MoveList(testMove);
					}
					break;
				}
			} catch (NullPointerException n) {
				continue;
			}
			
			//Check if new move might be good for calculatedMoves
			this.addCalculatedMoves(testMove);
		}
		//Return duplicate of pieceListMove
		return new MoveList(pieceListMove);
	}
	
	//DESCRIPTION:		Method returns value of single move, taking in board, both piece ArrayLists, and coordinate to check
	//PRE-CONDITION:	All passed objects must be valid and initialized
	//POST-CONDITION:	Value of passed move is returned, via calculate Offense - calculateDefense
	public double calculateSingleMove(Pieces[][] board, Pieces piece, ArrayList<MoveList> pieceList, int horzCoordinate, int vertCoordinate) {
		double moveValue = 0;
		
		//Duplicate passed board for testing
		Pieces[][] dummyBoard = new Pieces[8][8];
		this.duplicateBoard(board, dummyBoard);
		
		//If passed piece and pieceList ArrayList are not same colored pieces, return exit value
		try {
			if (piece.getColor() != dummyBoard[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor()) {
				return -200;
			}
		} catch (NullPointerException n) {
			//If null pointer is caught, passed ArrayList is not initialized; return stupid value
			return -200;
		}
		
		//Offense calculation
		if (piece.validCoordinate(dummyBoard, horzCoordinate, vertCoordinate)) {
			//If enemy piece occupies coordinate, add value of piece to moveValue
			if (Pieces.isEnemyPiece(dummyBoard, dummyBoard[piece.getHorz()][piece.getVert()], horzCoordinate, vertCoordinate)) {
				moveValue += dummyBoard[horzCoordinate][vertCoordinate].getValue() + 1;
			}
			//Check if move is safe; it not, subtract value of passed piece from moveValue
			if (!(this.isMoveSafe(dummyBoard, piece, horzCoordinate, vertCoordinate))) {
				moveValue -= piece.getValue() * 2;
			}
			//If move is safe but square would be threatened by piece with lower value, subtract value of piece
			//If coordinate is valid, move piece to coordinate and calculate board value
			dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horzCoordinate, vertCoordinate);
			//If coordinate is occupied by enemy piece, add value of piece to move
			moveValue += this.calculateOffense(dummyBoard, pieceList);
			//Once offense is calculated, check if moved piece is hanging by running threatened squares and pieces
			Chess.threatenUpdate(dummyBoard);
			//If piece is moving to safe square but will be threatened by piece with less value, subtract difference of piece value from moveValue
			//For loop to check all pieces, using enemyPieces and try/catch for any weird stuff
			/*
			int enemyHorz, enemyVert;
			for (int i = 0; i < this.enemyPieces.size(); i++) {
				enemyHorz = this.enemyPieces.get(i).getPositionHorz();
				enemyVert = this.enemyPieces.get(i).getPositionVert();
				//Check if passed piece is in any of the threatenedPiece lists; if present after moving to coordinate, subtract difference of value
				//Nested for loop to check possibleAttacks ArrayList in referenced enemy piece
				try {
					for (int n = 0; n < dummyBoard[enemyHorz][enemyVert].getPossibleAttacks().size(); n++) {
						//If passed piece is present in list, subtract difference of piece value from moveValue
						if (dummyBoard[enemyHorz][enemyVert].getPossibleAttackedPiece(n).equals(dummyBoard[horzCoordinate][vertCoordinate])) {
							double badValue = dummyBoard[horzCoordinate][vertCoordinate].getValue() - dummyBoard[enemyHorz][enemyVert].getValue();
							moveValue -= badValue;
						}
					}
				//If either exception is caught while checking, continue in outer for loop
				} catch (IndexOutOfBoundsException ind) {
					continue;
				} catch (NullPointerException nu) {
					continue;
				}
			}
			*/
			
			//If piece is moved to threatened square, automatically adjust move to losing piece
			switch(piece.getColor()) {
			case 'w':
				if (dummyBoard[horzCoordinate][vertCoordinate].getIsThreatenedByBlack()) {
					moveValue = 0 - piece.getValue();
				}
				break;
			case 'b':
				if (dummyBoard[horzCoordinate][vertCoordinate].getIsThreatenedByWhite()) {
					moveValue = 0 - piece.getValue();
				}
				break;
				
			}
		
		}
		//If block is reached, coordinate is invalid; return -200
		else {
			return -200;
		}
		
		//Defense calculation; code block can only be reached if piece coordinate is valid
		moveValue += this.calculateDefense(dummyBoard, pieceList);
		
		/*
		//Check if moce is safe; it not, subtract value of passed piece from moveValue
		if (!(this.isMoveSafe(dummyBoard, piece, horzCoordinate, vertCoordinate))) {
			moveValue -= piece.getValue();
		}
		*/
		return moveValue;
	}
	
	//DESCRIPTION:		Method returns MoveList object with highest value for Queen or Bishop
	//PRE-CONDITION:	All passed objects must be valid; ArrayList and passed piece must be same color
	//POST-CONDITION:	MoveList object with highest value is returned; instantiated with coordinate and move value
	public MoveList diagonalHighestMove(Pieces[][] board, Pieces piece, ArrayList<MoveList> pieceList) {
		
		//Ints for storing current best coordinate and moveValues
		int bestHorz, bestVert;
		bestHorz = bestVert  = 0;
		
		double bestValue = 0;
		//Other ints for later storage
		double currentValue = -100;
		
		//Int controls if coordinates are for King
		int king = 100;
		King meme = new King();
		if (piece.getClass() == meme.getClass()) {
			king = 1;
		}
		
		//Check if passed piece is either Queen or Bishop; if not, return default MoveList object
		if (!(piece instanceof Queen) && (!(piece instanceof Bishop)) && (!(piece instanceof King))) {
			return new MoveList();
		}
		//If colors are not identical, return default object
		if (piece.getColor() != board[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor()) {
			return new MoveList();
		}
		
		//dummyBoard for calculation in for loops
		Pieces[][] dummyBoard = new Pieces[8][8];
		
		//Ints to store original location of passed piece
		int horz, vert;
		horz = piece.getHorz();
		vert = piece.getVert();
		
		//For loop to check H1 direction
		for (int i = 1; i < 8; i++) {
			//Check for passed king
			if (i > king) {
				break;
			}
			//Duplicate dummyBoard for each loop check
			this.duplicateBoard(board, dummyBoard);
			
			try {
				//If coordinate is invalid, break loop
				if (dummyBoard[horz][vert].validCoordinate(dummyBoard, horz + i, vert + i)) {
					//If coordinate is valid, move piece to coordinate and begin calculation
					currentValue = this.calculateSingleMove(dummyBoard, piece, pieceList, horz + i, vert + i);
					//If newly calculated move is higher than current best, adjust best coordinates and value
					if (currentValue > bestValue) {
						bestValue = currentValue;
						//reset current value for next iteration
						currentValue = 0;
						bestHorz = horz + i;
						bestVert = vert + i;
					}
					//Check for identical values
					else if (currentValue == bestValue) {
						//Other doubles to hold values
						double current, best;
						this.duplicateBoard(board, dummyBoard);
						//Move selected piece back to best coordinate for testing
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horz + i, vert + i);
						current = dummyBoard[horz + i][vert + i].threatenedSquares(dummyBoard) + dummyBoard[horz + i][vert + i].threatenedPieces(dummyBoard);
						
						//Duplicate pieceList board again for fresh counts
						this.duplicateBoard(board, dummyBoard);
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, bestHorz, bestVert);
						best = dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard) + dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard);
						//If current iteration has higher score, set MoveList parameters to current move
						if (current > best) {
							bestValue = currentValue;
							bestHorz = horz + i;
							bestVert = vert + i;
						}
					}
					//If moves have equal value, determine winner via moved piece threat
					else {
						//Reset value for next iteration
						currentValue = 0;
					}
				}
				//If coordinate is invalid, break loop and continue to next loop
				else {
					break;
				}
			} catch (IndexOutOfBoundsException n) {
				//If exception is caught, loop must end
				i = 8;
				break;
			} catch (NullPointerException nu) {
				//Exception catch for safety sake
				i = 8;
				break;
			}
		}
		
		//For loop for H8 direction 
		for (int i = 1; i < 8; i++) {
			//Check for passed king
			if (i > king) {
				break;
			}
			//Duplicate dummyBoard for each loop check
			this.duplicateBoard(board, dummyBoard);
			
			try {
				//If coordinate is invalid, break loop
				if (dummyBoard[horz][vert].validCoordinate(dummyBoard, horz + i, vert - i)) {
					//If coordinate is valid, move piece to coordinate and begin calculation
					currentValue = this.calculateSingleMove(dummyBoard, piece, pieceList, horz + i, vert - i);
					//If newly calculated move is higher than current best, adjust best coordinates and value
					if (currentValue > bestValue) {
						bestValue = currentValue;
						//reset current value for next iteration
						currentValue = 0;
						bestHorz = horz + i;
						bestVert = vert - i;
					}
					//Check for identical move values
					else if (currentValue == bestValue) {
						//Other doubles to hold values
						double current, best;
						this.duplicateBoard(board, dummyBoard);
						//Move selected piece back to best coordinate for testing
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horz + i, vert - i);
						current = dummyBoard[horz + i][vert - i].threatenedSquares(dummyBoard) + dummyBoard[horz + i][vert - i].threatenedPieces(dummyBoard);
						
						//Duplicate pieceList board again for fresh counts
						this.duplicateBoard(board, dummyBoard);
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, bestHorz, bestVert);
						best = dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard) + dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard);
						//If current iteration has higher score, set MoveList parameters to current move
						if (current > best) {
							bestValue = currentValue;
							bestHorz = horz + i;
							bestVert = vert - i;
						}
					}
					else {
						//Reset value for next iteration
						currentValue = 0;
					}
				}
				//If coordinate is invalid, break loop and continue to next loop
				else {
					break;
				}
			} catch (IndexOutOfBoundsException n) {
				//If exception is caught, loop must end
				break;
			} catch (NullPointerException nu) {
				//Exception catch for safety sake
				break;
			}
		}
		
		//For loop for A8 direction 
		//For loop to check H1 direction
		for (int i = 1; i < 8; i++) {
			//Check for passed king
			if (i > king) {
				break;
			}
			//Duplicate dummyBoard for each loop check
			this.duplicateBoard(board, dummyBoard);
			
			try {
				//If coordinate is invalid, break loop
				if (dummyBoard[horz][vert].validCoordinate(dummyBoard, horz - i, vert - i)) {
					//If coordinate is valid, move piece to coordinate and begin calculation
					currentValue = this.calculateSingleMove(dummyBoard, piece, pieceList, horz - i, vert - i);
					//If newly calculated move is higher than current best, adjust best coordinates and value
					if (currentValue > bestValue) {
						bestValue = currentValue;
						//reset current value for next iteration
						currentValue = 0;
						bestHorz = horz - i;
						bestVert = vert - i;
					}
					//If moves have equal value, determine winner via moved piece threats
					else if (currentValue == bestValue) {
						//Other doubles to hold values
						double current, best;
						this.duplicateBoard(board, dummyBoard);
						//Move selected piece back to best coordinate for testing
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horz - i, vert - i);
						current = dummyBoard[horz - i][vert - i].threatenedSquares(dummyBoard) + dummyBoard[horz - i][vert - i].threatenedPieces(dummyBoard);
						
						//Duplicate pieceList board again for fresh counts
						this.duplicateBoard(board, dummyBoard);
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, bestHorz, bestVert);
						best = dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard) + dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard);
						//If current iteration has higher score, set MoveList parameters to current move
						if (current > best) {
							bestValue = currentValue;
							bestHorz = horz - i;
							bestVert = vert - i;
						}
					}
					else {
						//Reset value for next iteration
						currentValue = 0;
					}
				}
				//If coordinate is invalid, break loop and continue to next loop
				else {
					break;
				}
			} catch (IndexOutOfBoundsException n) {
				//If exception is caught, loop must end
				break;
			} catch (NullPointerException nu) {
				//Exception catch for safety sake
				break;
			}
		}
		
		//For loop to check A1 direction
		for (int i = 1; i < 8; i++) {
			//Check for passed king
			if (i > king) {
				break;
			}
			//Duplicate dummyBoard for each loop check
			this.duplicateBoard(board, dummyBoard);
			
			try {
				//If coordinate is invalid, break loop
				if (dummyBoard[horz][vert].validCoordinate(dummyBoard, horz - i, vert + i)) {
					//If coordinate is valid, move piece to coordinate and begin calculation
					currentValue = this.calculateSingleMove(dummyBoard, piece, pieceList, horz - i, vert + i);
					//If newly calculated move is higher than current best, adjust best coordinates and value
					if (currentValue > bestValue) {
						bestValue = currentValue;
						//reset current value for next iteration
						currentValue = 0;
						bestHorz = horz - i;
						bestVert = vert + i;
					}
					//Check for identical case values
					else if (currentValue == bestValue) {
						//Other doubles to hold values
						double current, best;
						this.duplicateBoard(board, dummyBoard);
						//Move selected piece back to best coordinate for testing
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horz - i, vert + i);
						current = dummyBoard[horz - i][vert + i].threatenedSquares(dummyBoard) + dummyBoard[horz - i][vert + i].threatenedPieces(dummyBoard);
						
						//Duplicate pieceList board again for fresh counts
						this.duplicateBoard(board, dummyBoard);
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, bestHorz, bestVert);
						best = dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard) + dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard);
						//If current iteration has higher score, set MoveList parameters to current move
						if (current > best) {
							bestValue = currentValue;
							bestHorz = horz - i;
							bestVert = vert + i;
						}
					}
					else {
						//Reset value for next iteration
						currentValue = 0;
					}
				}
				//If coordinate is invalid, break loop and continue to next loop
				else {
					break;
				}
			} catch (IndexOutOfBoundsException n) {
				//If exception is caught, loop must end
				break;
			} catch (NullPointerException nu) {
				//Exception catch for safety sake
				break;
			}
		}
		
		//With all diagonals calculated, return MoveList object with best coordinates and value
		return new MoveList(piece.getHorz(), piece.getVert(), bestHorz, bestVert, bestValue);
		
	}

	//DESCRIPTION:		Method calculates which linear move has highest potential, returns MoveList object with value and coordinates
	//PRE-CONDITION:	Bacchus must be fully instantiated and all objects constructed
	//POST-CONDITION:	MoveList object with highest value and coordinates is returned
	public MoveList linearHighestMove(Pieces[][] board, Pieces piece, ArrayList<MoveList> pieceList) {
		
		//Int for controlling King calculation
		int king = 100;
		King meme = new King();
		if (piece.getClass() == meme.getClass()) {
			king = 1;
		}
		
		//int for controlling next loop
		//int lcv = 0;
		//Ints for storing current best coordinate and moveValues
		int bestHorz, bestVert;
		bestHorz = bestVert = 0;
		
		//Other ints for later storage
		double currentValue = -100;
		double bestValue = 0;
		
		//Check if passed piece is either Queen or Rook; if not, return default MoveList object
		if (!(piece instanceof Queen) && (!(piece instanceof Rook))) {
			return new MoveList();
		}
		//If colors are not identical, return default object
		if (piece.getColor() != board[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor()) {
			return new MoveList();
		}
		
		//dummyBoard for calculation in for loops
		Pieces[][] dummyBoard = new Pieces[8][8];
		
		//Ints to store original location of passed piece
		int horz, vert;
		horz = piece.getHorz();
		vert = piece.getVert();
		
		//For loop to check towards rank 1 direction
		for (int i = 1; i < 8; i++) {
			//Check for passed king
			if (i > king) {
				break;
			}
			//Duplicate dummyBoard for each loop check
			this.duplicateBoard(board, dummyBoard);
			
			try {
				//If coordinate is invalid, break loop
				if (dummyBoard[horz][vert].validCoordinate(dummyBoard, horz, vert + i)) {
					//If coordinate is valid, move piece to coordinate and begin calculation
					currentValue = this.calculateSingleMove(dummyBoard, piece, pieceList, horz, vert + i);
					//If newly calculated move is higher than current best, adjust best coordinates and value
					if (currentValue > bestValue) {
						bestValue = currentValue;
						//reset current value for next iteration
						currentValue = 0;
						bestHorz = horz;
						bestVert = vert + i;
					}
					//Check for identical values
					else if (currentValue == bestValue) {
						//Other doubles to hold values
						double current, best;
						this.duplicateBoard(board, dummyBoard);
						//Move selected piece back to best coordinate for testing
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horz, vert + i);
						current = dummyBoard[horz][vert + i].threatenedSquares(dummyBoard) + dummyBoard[horz][vert + i].threatenedPieces(dummyBoard);
						
						//Duplicate pieceList board again for fresh counts
						this.duplicateBoard(board, dummyBoard);
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, bestHorz, bestVert);
						best = dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard) + dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard);
						//If current iteration has higher score, set MoveList parameters to current move
						if (current > best) {
							bestValue = currentValue;
							bestHorz = horz;
							bestVert = vert + i;
						}
					}
					else {
						//Reset value for next iteration
						currentValue = 0;
					}
				}
				//If coordinate is invalid, break loop and continue to next loop
				else {
					break;
				}
			} catch (IndexOutOfBoundsException n) {
				//If exception is caught, loop must end
				i = 8;
				break;
			} catch (NullPointerException nu) {
				//Exception catch for safety sake
				i = 8;
				break;
			}
		}
		
		//For loop for rank 8 direction 
		for (int i = 1; i < 8; i++) {
			//Check for passed king
			if (i > king) {
				break;
			}
			//Duplicate dummyBoard for each loop check
			this.duplicateBoard(board, dummyBoard);
			
			try {
				//If coordinate is invalid, break loop
				if (dummyBoard[horz][vert].validCoordinate(dummyBoard, horz, vert - i)) {
					//If coordinate is valid, move piece to coordinate and begin calculation
					currentValue = this.calculateSingleMove(dummyBoard, piece, pieceList, horz, vert - i);
					//If newly calculated move is higher than current best, adjust best coordinates and value
					if (currentValue > bestValue) {
						bestValue = currentValue;
						//reset current value for next iteration
						currentValue = 0;
						bestHorz = horz;
						bestVert = vert - i;
					}
					//Check for identical values
					else if (currentValue == bestValue) {
						//Other doubles to hold values
						double current, best;
						this.duplicateBoard(board, dummyBoard);
						//Move selected piece back to best coordinate for testing
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horz + i, vert + i);
						current = dummyBoard[horz][vert - i].threatenedSquares(dummyBoard) + dummyBoard[horz][vert - i].threatenedPieces(dummyBoard);
						
						//Duplicate pieceList board again for fresh counts
						this.duplicateBoard(board, dummyBoard);
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, bestHorz, bestVert);
						best = dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard) + dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard);
						//If current iteration has higher score, set MoveList parameters to current move
						if (current > best) {
							bestValue = currentValue;
							bestHorz = horz;
							bestVert = vert - i;
						}
					}
					else {
						//Reset value for next iteration
						currentValue = 0;
					}
				}
				//If coordinate is invalid, break loop and continue to next loop
				else {
					break;
				}
			} catch (IndexOutOfBoundsException n) {
				//If exception is caught, loop must end
				break;
			} catch (NullPointerException nu) {
				//Exception catch for safety sake
				break;
			}
		}
		
		//For loop for A direction 
		//For loop to check H1 direction
		for (int i = 1; i < 8; i++) {
			//Check for passed king
			if (i > king) {
				break;
			}
			//Duplicate dummyBoard for each loop check
			this.duplicateBoard(board, dummyBoard);
			
			try {
				//If coordinate is invalid, break loop
				if (dummyBoard[horz][vert].validCoordinate(dummyBoard, horz - i, vert)) {
					//If coordinate is valid, move piece to coordinate and begin calculation
					currentValue = this.calculateSingleMove(dummyBoard, piece, pieceList, horz - i, vert);
					//If newly calculated move is higher than current best, adjust best coordinates and value
					if (currentValue > bestValue) {
						bestValue = currentValue;
						//reset current value for next iteration
						currentValue = 0;
						bestHorz = horz - i;
						bestVert = vert;
					}
					//Check for identical values
					else if (currentValue == bestValue) {
						//Other doubles to hold values
						double current, best;
						this.duplicateBoard(board, dummyBoard);
						//Move selected piece back to best coordinate for testing
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horz - i, vert);
						current = dummyBoard[horz - i][vert].threatenedSquares(dummyBoard) + dummyBoard[horz - i][vert].threatenedPieces(dummyBoard);
						
						//Duplicate pieceList board again for fresh counts
						this.duplicateBoard(board, dummyBoard);
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, bestHorz, bestVert);
						best = dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard) + dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard);
						//If current iteration has higher score, set MoveList parameters to current move
						if (current > best) {
							bestValue = currentValue;
							bestHorz = horz - i;
							bestVert = vert;
						}
					}
					else {
						//Reset value for next iteration
						currentValue = 0;
					}
				}
				//If coordinate is invalid, break loop and continue to next loop
				else {
					break;
				}
			} catch (IndexOutOfBoundsException n) {
				//If exception is caught, loop must end
				break;
			} catch (NullPointerException nu) {
				//Exception catch for safety sake
				break;
			}
		}
		
		//For loop to check H direction
		for (int i = 1; i < 8; i++) {
			//Check for passed king
			if (i > king) {
				break;
			}
			//Duplicate dummyBoard for each loop check
			this.duplicateBoard(board, dummyBoard);
			
			try {
				//If coordinate is invalid, break loop
				if (dummyBoard[horz][vert].validCoordinate(dummyBoard, horz + i, vert)) {
					//If coordinate is valid, move piece to coordinate and begin calculation
					currentValue = this.calculateSingleMove(dummyBoard, piece, pieceList, horz + i, vert);
					//If newly calculated move is higher than current best, adjust best coordinates and value
					if (currentValue > bestValue) {
						bestValue = currentValue;
						//reset current value for next iteration
						currentValue = 0;
						bestHorz = horz + i;
						bestVert = vert;
					}
					//Check for identical values
					else if (currentValue == bestValue) {
						//Other doubles to hold values
						double current, best;
						this.duplicateBoard(board, dummyBoard);
						//Move selected piece back to best coordinate for testing
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, horz + i, vert);
						current = dummyBoard[horz + i][vert].threatenedSquares(dummyBoard) + dummyBoard[horz + i][vert].threatenedPieces(dummyBoard);
						
						//Duplicate pieceList board again for fresh counts
						this.duplicateBoard(board, dummyBoard);
						dummyBoard[piece.getHorz()][piece.getVert()].move(dummyBoard, bestHorz, bestVert);
						best = dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard) + dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard);
						//If current iteration has higher score, set MoveList parameters to current move
						if (current > best) {
							bestValue = currentValue;
							bestHorz = horz + i;
							bestVert = vert;
						}
					}
					else {
						//Reset value for next iteration
						currentValue = 0;
					}
				}
				//If coordinate is invalid, break loop and continue to next loop
				else {
					break;
				}
			} catch (IndexOutOfBoundsException n) {
				//If exception is caught, loop must end
				break;
			} catch (NullPointerException nu) {
				//Exception catch for safety sake
				break;
			}
		}
		
		//With all diagonals calculated, return MoveList object with best coordinates and value
		return new MoveList(piece.getHorz(), piece.getVert(), bestHorz, bestVert, bestValue);
		
	}
	
	//DESCRIPTION:		Method calculates best possible coordinate for passed King
	//PRE-CONDITION:	Passed piece must be a King
	//POST-CONDITION:	Passed board and King must be instantiated
	public MoveList kingPossibleMoves(Pieces[][] board, Pieces king, ArrayList<MoveList> pieceList) {
		
		//Ints for measuring best and current coordinates
		int bestHorz, bestVert, currentHorz, currentVert, lcv;
		double bestValue, currentValue;
		
		bestHorz = bestVert = currentHorz = currentVert = lcv = 0;
		bestValue = currentValue = 0;
		
		//If passed piece is not King, return default MoveList
		if (!(king instanceof King)) {
			return new MoveList();
		}
		
		//Duplicate board for calculation
		Pieces[][] dummyBoard = new Pieces[8][8];
		this.duplicateBoard(board, dummyBoard);
		
		//Check all coordinates for King
		while (lcv < 8) {
			
			//Switch statement to change coordinates each iteration
			switch(lcv) {
			case 0 :
				currentHorz = king.getHorz() + 1;
				currentVert = king.getVert();
				break;
			case 1:
				currentHorz = king.getHorz() - 1;
				currentVert = king.getVert();
				break;
			case 2:
				currentHorz = king.getHorz();
				currentVert = king.getVert() + 1;
				break;
			case 3:
				currentHorz = king.getHorz();
				currentVert = king.getVert() - 1;
				break;
			//Diagonal cases
			case 4:
				currentHorz = king.getHorz() - 1;
				currentVert = king.getVert() - 1;
				break;
			case 5:
				currentHorz = king.getHorz() - 1;
				currentVert = king.getVert() + 1;
				break;
			case 6:
				currentHorz = king.getHorz() + 1;
				currentVert = king.getVert() + 1;
				break;
			case 7:
				currentHorz = king.getVert() + 1;
				currentVert = king.getVert() - 1;
				break;
			default:
				break;
			}
			//Try catch block for nullPointers or out of bounds 
			try {
				if (king.validCoordinate(dummyBoard, currentHorz, currentVert)) {
					currentValue += this.calculateSingleMove(dummyBoard, king, pieceList, currentHorz, currentVert);
					this.possibleMoves.add(new MoveList(king.getHorz(), king.getVert(), currentHorz, currentVert, currentValue));
					lcv++;
				}
				else {
					lcv++;
				}
			//If either exception is caught, proceed with next section of while loop
			} catch (IndexOutOfBoundsException in) {
				lcv++;
			} catch (NullPointerException n) {
				lcv++;
			}
		} //End while loop
		
		//Return MoveList with position and best calculated position
		return new MoveList(king.getHorz(), king.getVert(), bestHorz, bestVert, bestValue);
	}
	
	//DESCRIPTION:		Method calculates all possible coordinates Queen can go to, returning MoveList with move coordinate and value
	//PRE-CONDITION:	All passed parameters must be initialized and within bounds; ArrayList and passed Queen MUST be same color
	//POST-CONDITION:	MoveList containing highest score value is returned with coordinates assigned
	public MoveList queenHighestMove(Pieces[][] board, Pieces queen, ArrayList<MoveList> pieceList) {
		//Try catch block to validate proper pieceList ArrayList with proper color; if not proper color, return default MoveList object
		try {
			if (queen.getColor() != board[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor()) {
				return new MoveList();
			}
		} catch (NullPointerException n) {
			//If null pointer is caught, return default object to prevent crashing
			return new MoveList();
		} catch (IndexOutOfBoundsException i) {
			return new MoveList();
		}
		
		//Two ArrayLists taking return object from helper methods
		MoveList diag = this.diagonalHighestMove(board, queen, pieceList);
		MoveList linear = this.linearHighestMove(board, queen, pieceList);
		//Return MoveList with higher moveValue
		if (diag.getMoveValue() > linear.getMoveValue()) {
			return new MoveList(diag);
		}
		else if (diag.getMoveValue() == linear.getMoveValue()) {
			//uhhhhh return diag for now
			return new MoveList(diag);
		}
		else {
			return new MoveList(linear);
		}
	}
	
	//DESCRIPTION:		Method calculates best coordinate for Bishop to go to, returning MoveList object with all coordinates and values
	//PRE-CONDITION:	All passed parameters must be initialized; Passed ArrayList and piece must be using same color
	//POST-CONDITION:	MoveList object with best coordinates and highest value is returned
	public MoveList bishopHighestMove(Pieces[][] board, Pieces bishop, ArrayList<MoveList> pieceList) {
		//Try catch block to validate proper pieceList ArrayList with proper color
		try {
			//If color is not same, return default MoveList object
			if (bishop.getColor() != board[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor()) {
				return new MoveList();
			}
		} catch (NullPointerException n) {
			return new MoveList();
		} catch (IndexOutOfBoundsException i) {
			return new MoveList();
		}
		
		//If try/catch block is passed, calculate and return best MoveList
		return this.diagonalHighestMove(board, bishop, pieceList);
	}
	
	//DESCRIPTION:		Method calculates best move for passed Rook to do; returns MoveList object with coordinate and move value
	//PRE-CONDIITON:	All passed parameters and calling object must be fully instantiated
	//POST-CONDITION:	MoveList object with best rook move is returned
	public MoveList rookHighestMove(Pieces[][] board, Pieces rook, ArrayList<MoveList> pieceList) {
		
		//Try catch block to validate proper pieceList ArrayList with proper color
		try {
			//If color is not same, return default MoveList object
			if (rook.getColor() != board[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor()) {
				return new MoveList();
			}
		} catch (NullPointerException n) {
			return new MoveList();
		} catch (IndexOutOfBoundsException i) {
			return new MoveList();
		}
		
		//If try/catch block is passed, calculate and return best MoveList
		return this.linearHighestMove(board, rook, pieceList);
	}
	
	//DESCRIPTION:		Method calculates best move for passed Knight; returns MoveList object with coordinate and move value
	//PRE-CONDITION:	All passed parameters must be fully instantiated 
	//POST-CONDITION:	MoveList object with best Knight move is returned
	public MoveList knightHighestMove(Pieces[][] board, Pieces knight, ArrayList<MoveList> pieceList) {
		
		//If passed piece is not a knight, return default object
		if (!(knight instanceof Knight)) {
			return new MoveList();
		}
		
		//Try catch block in case null pointer is caught
		try {
			//If colors are not the same, return default object
			if (knight.getColor() != board[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor()) {
				return new MoveList();
			}
		//If either exception is caught, return default MoveList object
		} catch (IndexOutOfBoundsException ind) {
			return new MoveList();
		} catch (NullPointerException n) {
			return new MoveList();
		}
		
		//Variables for easy data storage and loop control
		int bestHorz, bestVert, currentHorz, currentVert, lcv;
		bestHorz = bestVert = currentHorz = currentVert = 0;
		
		double currentValue, bestValue;
		currentValue = bestValue = 0;

		//pieceList board for loop calculation
		Pieces[][] dummyBoard = new Pieces[8][8];
		
		//Begin calculation with each loop; use lcv to hand exceptions in while loop
		lcv = 0;
		
		//Must check 8 moves; lcv from 0 to 7
		while (lcv < 8) {
			//Duplicate board at start of each loop
			this.duplicateBoard(board, dummyBoard);
			
			//Switch statement to switch checked coordinates for the knight
			switch (lcv) {
			case 0:
				currentHorz = knight.getHorz() - 1;
				currentVert = knight.getVert() - 2;
				break;
			case 1:
				currentHorz = knight.getHorz() - 1;
				currentVert = knight.getVert() + 2;
				break;
			case 2:
				currentHorz = knight.getHorz() + 1;
				currentVert = knight.getVert() - 2;
				break;
			case 3:
				currentHorz = knight.getHorz() + 1;
				currentVert = knight.getVert() + 2;
				break;
			case 4:
				currentHorz = knight.getHorz() - 2;
				currentVert = knight.getVert() - 1;
				break;
			case 5:
				currentHorz = knight.getHorz() - 2;
				currentVert = knight.getVert() + 1;
				break;
			case 6:
				currentHorz = knight.getHorz() + 2;
				currentVert = knight.getVert() - 1;
				break;
			case 7:
				currentHorz = knight.getHorz() + 2;
				currentVert = knight.getVert() + 1;
				break;
			default:
				break;
			}
			
			//Try catch block to catch all exceptions; if any caught, proceed to next part of loop
			try {
					//Validate current coordinate before calculation 
					if (knight.validCoordinate(dummyBoard, currentHorz, currentVert)){
						//If coordinate is valid, calculate move and store in currentValue
						currentValue = this.calculateSingleMove(dummyBoard, knight, pieceList, currentHorz, currentVert);
						//Check if values have changed; if changed, change coordinates and value
						if (currentValue > bestValue) {
							bestValue = currentValue;
							bestHorz = currentHorz;
							bestVert = currentVert;
							lcv++;
						}
						else if (currentValue == bestValue) {
							double current, best;
							current = best = 0;
							//Check both current and tested coordinates for move values
							this.duplicateBoard(board, dummyBoard);
							//Move Knight before each calculation
							//dummyBoard[knight.getHorz()][knight.getVert()].move(dummyBoard, knight.getHorz() - 1, knight.getVert() - 2);
							current = dummyBoard[currentHorz][currentVert].threatenedPieces(dummyBoard) 
									  + dummyBoard[currentHorz][currentVert].threatenedSquares(dummyBoard);
							//Redup board for moving and check best coordinate
							this.duplicateBoard(board, dummyBoard);
							best = dummyBoard[bestHorz][bestVert].threatenedPieces(dummyBoard) 
								    + dummyBoard[bestHorz][bestVert].threatenedSquares(dummyBoard);
							//Reupdate; if values are still equal, keep best
							if (current > best) {
								//Values are already equal, so change coordinates
								bestHorz = currentHorz;
								bestVert = currentVert;
							}
							lcv++;	
						}
						else {
							lcv++;
						}
					}
					//If coordinate is invalid; increment lcv to continue loop
					else {
						lcv++;
				}
				
			} catch (IndexOutOfBoundsException ind) {
				//Increment lcv for next loop
				lcv++;
			} catch (NullPointerException n) {
				lcv++;
			} //End try/catch block
		}//End while loop 
		//With all possibilities calculated, return MoveList using stored values
		return new MoveList(knight.getHorz(), knight.getVert(), bestHorz, bestVert, bestValue);
	}
	
	//DESCRIPTION:		Method calculates best move for passed Pawn; returns MoveList object with best coordinates and move value
	//PRE-CONDITION:	All passed parameters must be fully initialized and instantiated
	//POST-CONDITION:	MoveList object with best Pawn move is returned
	public MoveList pawnHighestMove(Pieces[][] board, Pieces pawn, ArrayList<MoveList> pieceList) {
		
		//If passed piece is not a pawn, return default MoveList
		if (!(pawn instanceof Pawn)) {
			return new MoveList();
		}
		
		//Try catch block for out of bounds or null pointers
		try {
			//Check for right colors; if colors don't match, return default object
			if (pawn.getColor() != board[pieceList.get(0).getPositionHorz()][pieceList.get(0).getPositionVert()].getColor()) {
				return new MoveList();
			}
		} catch (NullPointerException e) {
			return new MoveList();
		} catch (IndexOutOfBoundsException i) {
			return new MoveList();
		}
		
		//variables for storing best move coordinates and values
		//lcv to control while loop to check moves
		int bestHorz, bestVert, lcv;
		bestHorz = bestVert = lcv = 0;
		
		double bestValue, currentValue;
		bestValue = -100;
		
		//pieceList board for calculations
		Pieces[][] dummyBoard = new Pieces[8][8];
		
		int vert;
		//Switch statement for different colors 
		switch(pawn.getColor()) {
		
		//White pawn case
		case 'w':
			vert = 0 - pawn.getVert();
			break;
		case 'b':
			vert = pawn.getVert();
			break;
		default:
			vert = 0;
			break;
		}
			//While loop, using lcv as control
			while (lcv < 6) {
				
				//Duplicate board before each loop calculation
				this.duplicateBoard(board, dummyBoard);
				try {
					
					//Single move check 
					if (lcv == 0) {
						//If coordinate is valid, continue to calculation step; else, increment lcv
						if (pawn.validCoordinate(dummyBoard, pawn.getHorz(), Math.abs(vert + 1))) {
							//Coordinate is valid, begin calculation
							currentValue = this.calculateSingleMove(dummyBoard, pawn, pieceList, pawn.getHorz(), Math.abs(vert + 1));
							//If calculated move is better than current best, replace current best and best coordinates
							if (currentValue > bestValue) {
								bestValue = currentValue;
								bestHorz = pawn.getHorz();
								bestVert = Math.abs(vert + 1);
								lcv++;
							}
							else {
								lcv++;
							}
						} //End calculation section
						else {
							lcv++;
						} //End single move check
					}
					//Double Move Check
					else if (lcv == 1) {
						//Check if coordinate is valid
						if (pawn.validCoordinate(dummyBoard, pawn.getHorz(), Math.abs(vert + 2))) {
							//Calculate move if move is valid
							currentValue = this.calculateSingleMove(dummyBoard, pawn, pieceList, pawn.getHorz(), Math.abs(vert + 2));
							//If calculated move is better, set coordinates and move value
							if (currentValue > bestValue) {
								bestValue = currentValue;
								bestHorz = pawn.getHorz();
								bestVert = Math.abs(vert + 1);
								lcv++;
							}
							else {
								lcv++;
							}
						}
						else {
							lcv++;
						}
					}
					//Check for left file take
					else if (lcv == 2) {
						//Check for valid coordinate
						if (pawn.validCoordinate(dummyBoard, pawn.getHorz() - 1, Math.abs(vert + 1))) {
							currentValue = this.calculateSingleMove(dummyBoard, pawn, pieceList, pawn.getHorz() - 1,Math.abs(vert + 1));
							//Change values if left file take is better move
							if (currentValue > bestValue) {
								bestValue = currentValue;
								bestHorz = pawn.getHorz() - 1;
								bestVert = Math.abs(vert + 1);
								lcv++;
							}
							else {
								lcv++;
							}
						}
						else {
							lcv++;
						}
					}
					//Check for right file take
					else if (lcv == 3) {
						//Check if coordinate is valid; if valid, continue with calculation
						if (pawn.validCoordinate(dummyBoard, pawn.getHorz() + 1, Math.abs(vert + 1))) {
							currentValue = this.calculateSingleMove(dummyBoard, pawn, pieceList, pawn.getHorz() + 1, Math.abs(vert + 1));
							//If new value has better value, adjust coordinates
							if (currentValue > bestValue) {
								bestValue = currentValue;
								bestHorz = pawn.getHorz() + 1;
								bestVert = Math.abs(vert + 1);
								lcv++;
							}
							else {
								lcv++;
							}
						}
						else {
							lcv++;
						}
					}
					//Check for en passant on left
					else if (lcv == 4) {
						//If both pawns do not have can pass, break and increment lcv
						if (!(pawn.getCanPass()) && (!(dummyBoard[pawn.getHorz() - 1][pawn.getVert()].getCanPass()))) {
							lcv++;
						}
						else {
							//If both have canPass marked, en passant can be performed; calculate based on that
							if (pawn.validCoordinate(dummyBoard, pawn.getHorz() - 1, Math.abs(vert + 1))) {
								//Begin move calculation
								currentValue = this.calculateSingleMove(dummyBoard, pawn, pieceList, pawn.getHorz() - 1, Math.abs(vert + 1));
								if (currentValue > bestValue) {
									bestValue = currentValue;
									bestHorz = pawn.getHorz() - 1;
									bestVert = Math.abs(vert + 1);
									lcv++;
								}
								else {
									lcv++;
								}
							}
							else {
								lcv++;
							}
						}
					}
					//Right file en passant 
					else if (lcv == 5) {
						//Check if passed pawn has ability to pass; if false, increment and continue
						if (!(pawn.getCanPass())) {
							lcv++;
						}
						else {
							//Check for valid coordinate and begin calculation
							if (pawn.validCoordinate(dummyBoard, pawn.getHorz() + 1, Math.abs(vert + 1))) {
								//If coordinate is valid, continue to calculation
								currentValue = this.calculateSingleMove(dummyBoard, pawn, pieceList, pawn.getHorz() + 1, Math.abs(vert + 1));
								if (currentValue > bestValue) {
									bestValue = currentValue;
									bestHorz = pawn.getHorz() + 1;
									bestVert = Math.abs(vert + 1);
									lcv++;
								}
								else {
									lcv++;
								}
							}
							else {
								lcv++;
							}
						}
					}
					//left file en passant
				  //If either exception is caught, increment lcv to continue calculation
				} catch (IndexOutOfBoundsException i) {
					lcv++;
				} catch (NullPointerException n) {
					lcv++;
				}
			 //End while loop
	
			}
		
		//Once switch is executed, return MoveList object with best coordinates
		return new MoveList(pawn.getHorz(), pawn.getVert(), bestHorz, bestVert, bestValue);
		
		
	}
	
	//DESCRIPTION:		Bacchus moves opening piece based on passed int; moves piece on both testBoard and passed board
	//PRE-CONDITION:	Passed board must be valid; Method is only called when Bacchus is playing white pieces 
	//POST-CONDITION:	Opening piece is moved, and according ArrayLists in Bacchus are adjusted
	public void opening(Pieces[][] board) {
		//Int determines which opening to use in switch statement
		int random = Math.abs((int)((Math.random() * 10) - 7));
		//While loop to control random going between 1 and 3
		while (random > 4) {
			random = Math.abs((int)((Math.random() * 10) - 7));
		}
			//Switch statement for which opening to use
			switch(random) {
			//D4/D5 opening
			case 0:
				switch (this.bacchusColor) {
				case 'w':
					board[3][6].doubleMove(board);
					break;
				case 'b':
					board[3][1].doubleMove(board);
					break;
				}
				break;
			//E4 opening
			case 1:
				switch(this.bacchusColor) {
				case 'w':
					board[4][6].doubleMove(board);
					break;
				case 'b':
					board[4][1].doubleMove(board);
					break;
				}
				break;
			//C4 opening
			case 2:
				switch(this.bacchusColor) {
				case 'w':
					board[2][6].doubleMove(board);
					break;
				case 'b':
					board[2][1].doubleMove(board);
					break;
				}
				break;
			//Queen's Indian attack B3
			case 3:
				switch(this.bacchusColor) {
				case 'w':	
					board[1][6].move(board, 1, 5);
					break;
				case 'b':
					board[1][1].move(board, 1, 2);
					break;
				}
				break;
			//King's Indian Attack G3
			case 4:
				switch(this.bacchusColor) {
				case 'w':
					board[6][6].move(board, 6, 5);
					break;
				case 'b':
					board[6][1].move(board, 6, 2);
					break;
				}
				break;
			}
			
		}
	
	
	//DESCRIPTION:		Checks if passed color's King can be checked next turn
	//PRE-CONDITION:	Bacchus must be fully initialized
	//POST-CONDITION:	Returns true if passed color King can be checked next turn; else false
	public boolean canKingBeChecked(char color){
		
		boolean canBeChecked = false;
		return canBeChecked;
	}	
	
	//DESCRIPTION:		Method checks if passed MoveList object has high enough value to be added to calculatedMoves
	//PRE-CONDITION:	Bacchus must be fully initialized
	//POST-CONDITION:	If MoveList has high enough value, is added to calculatedMoves; else, nothing happens
	public void addCalculatedMoves(MoveList move) {
		//Int tracks index of lowest value move 
		double lowest = move.getMoveValue();
		int index = 0;
		//For loop with try catch block to catch any null shenanigans
		try {
			//If calculatedMoves has size < 3, add move by default
			if (this.calculatedMoves.size() < 3) {
				this.calculatedMoves.add(move);
			}
			else {
				
				for (int i = 0; i < 3; i++) {
					if (this.calculatedMoves.get(i).getMoveValue() < lowest) {
						lowest = this.calculatedMoves.get(i).getMoveValue();
						index = i;
					}
				}
			}
		} catch (NullPointerException n) {
			//If null pointer is caught, add passed move to calculated moves
			this.calculatedMoves.add(move);
		} catch (IndexOutOfBoundsException ind) {
			this.calculatedMoves.add(move);
		}
	
		//If lowest is not same as passed MoveList moveValue, remove MoveList item at index and add passed move
		if (lowest != move.getMoveValue()) {
			//Remove move at index and add passed move
			this.calculatedMoves.remove(index);
			this.calculatedMoves.add(move);
		}
		else {
			return;
		}
	}
	
	/******* ARRAYLIST AND ARRAY METHODS *************/
	
	//DESCRIPTION:		Method populates pieces ArrayList with colored Bacchus pieces
	//PRE-CONDITION:	Passed board and pieces must be fully instantiated
	//POST-CONDITION:	pieces is populated with MoveList object with coordinate of each piece
	public void populatePieces(Pieces[][] board, ArrayList<MoveList> pieceList, char color) {
		//Search passed board for same color pieces
		for (int horz = 0; horz < 8; horz++) {
			for (int vert = 0; vert < 8; vert++) {
				//If color matches Bacchus, add to pieces
				if (board[horz][vert].getColor() == color) {
					this.addPieces(pieceList, board[horz][vert]);
				}
			}
		}
	}
	
	//DESCRIPTION:		Method populates both pieces arrayLists
	//PRE-CONDITION:	Passed board must be valid
	//POST-CONDITION:	Both Bacchus pieces ArrayLists are populated/updated
	public void updatePieces(Pieces[][] board) {
		//Reset both ArrayLists before updating
		this.pieces = new ArrayList<MoveList>();
		this.enemyPieces = new ArrayList<MoveList>();
		this.populatePieces(board, this.pieces, this.bacchusColor);
		this.populatePieces(board, this.enemyPieces, this.playerColor);
	}
	
	//DESCRIPTION:		Adds passed piece to pieces ArrayList, also checks for duplicate pieces 	
	//PRE-CONDITION:	Passed piece must be fully instantiated and pieces must be initialized
	//POST-CONDITION:	Passed piece object is added to pieces ArrayList, with duplicates removed
	public void addPieces(ArrayList<MoveList> pieceList, Pieces piece) {
		//Check if piece is not added; if not, add to pieces ArrayList; else do nothing
		try {
			if (this.isPieceAdded(pieceList, piece.getHorz(), piece.getVert())) {
				//If added, do nothing
			} else {
				pieceList.add(new MoveList(piece.getHorz(), piece.getVert()));
			}
		} catch (NullPointerException e) {
			//If exception is caught, add piece to passed array
			pieceList.add(new MoveList(piece.getHorz(), piece.getVert()));
		} catch (IndexOutOfBoundsException i) {
			//If out of bounds is caught, add piece to array
			pieceList.add(new MoveList(piece.getHorz(), piece.getVert()));
		}
	}
	
	//DESCRIPTION:		Checks for duplicate piece coordinates in pieces; returns true if passed int coordinates are not duplicates	
	//PRE-CONDITION:	Passed coordinates must be 0 - 7 and pieces must be initialized
	//POST-CONDITION:	Returns true if passed coordinates have not been added yet
	public boolean isPieceAdded(ArrayList<MoveList> pieceList, int positionHorz, int positionVert) {
		int size;
		if(pieceList.size() == 0) {
			size = 1;
		} else {
			size = pieceList.size();
		}
		//For loop to check pieces ArrayList
		for (int i = 0; i < size; i++) {
			//If any MoveList coordinates match passed coordinates, return true
			if ((pieceList.get(i).getPositionHorz() == positionHorz) 
				&& (pieceList.get(i).getPositionVert() == positionVert)){
					return true;
				}
			else {
				continue;
			}
		}
		
		//If not duplicates are found in for loop search, return false
		return false;
	}
	
	//DESCRIPTION:		Duplicates passed board array for Bacchus to use as simulation
	//PRE-CONDITION:	Passed board must be fully initialized with pieces
	//POST-CONDITION:	board array is copied onto passed array
	public void duplicateBoard(Pieces[][] original, Pieces[][] test) {
		//Nested for loop to duplicate
		
			for (int horz = 0; horz < 8; horz++) {
				for (int vert = 0; vert < 8; vert++) {
					//Shit ton of if statements to determine piece type
					if (original[horz][vert] instanceof Pawn) {
						test[horz][vert] = new Pawn(original[horz][vert]);
					}
					else if (original[horz][vert] instanceof Knight) {
						test[horz][vert] = new Knight(original[horz][vert]);
					}
					else if (original[horz][vert] instanceof Bishop) {
						test[horz][vert] = new Bishop(original[horz][vert]);
					}
					else if (original[horz][vert] instanceof Rook) {
						test[horz][vert] = new Rook(original[horz][vert]);
					}
					else if (original[horz][vert] instanceof Queen) {
						test[horz][vert] = new Queen(original[horz][vert]);
					}
					else if (original[horz][vert] instanceof King) {
						test[horz][vert] = new King(original[horz][vert]);
					}
					//If index is not a piece, clone object as Squares object
					else if (original[horz][vert] instanceof Squares){
						test[horz][vert] = new Squares(original[horz][vert]);
					}
				}
			}
	}
	
	//DESCRIPTION:		Duplicates passed ArrayList to other passed ArrayList for MoveList items
	//PRE-CONDITION:	Both ArrayLists must be initialized, with one list having valid MoveList items
	//POST-CONDITION:	First passed ArrayList is duplicated onto second passed ArrayList
	public void duplicatePieces(ArrayList<MoveList> original, ArrayList<MoveList> copy) {
		//For loop to copy each MoveList using copy constructor 
		for (int i = 0; i < original.size(); i++) {
			copy.add(new MoveList(original.get(i)));
		}
	}
	
	
	
	/*********** THEORY METHODS **************/
	//DESCRIPTION:		Populates knownGames array using games in openings file
	//PRE-CONDITION:	File must exist and calling object must be fully instantiated
	//POST-CONDITION:	knownGames is populated with information from openings text file
	public void populateGames() throws Exception{
		
		//If file exists, begin reading and populating
		if (openings.exists()) {
			
			reader = new Scanner(openings);
			
			//pieceList String and String[] for reading and splitting
			String pieceList = "";
			String[] pieceListSplit;
			
			//For loop to store each game into knownGames
			for(int i = 0; i < this.gameNums; i++) {
				
				//Read line for pieceList to split
				pieceList = reader.nextLine();
				pieceListSplit = pieceList.split(",");
				
				try {
					//Nested for loop to record each split string in pieceList
					for (int n = 0; n < pieceListSplit.length; n++) {
						
						String[] secondpieceList;
						
						secondpieceList = pieceListSplit[n].split(" ");
						
						//First String is white's move, second is black's move
						this.knownGames[i][n][0] = secondpieceList[0];
						this.knownGames[i][n][1] = secondpieceList[1];
					}
				} catch (IndexOutOfBoundsException index) {
					
				}
			}
		}
	}
	
	
	//DESCRIPTION:		Searches knownGames array for which knownGames are still valid for match being played, updates gameIndex
	//PRE-CONDITION:	knownGames and gameIndex array must be fully initialized, 
					    //and passed moveList must be seperated by commas (E4,E5,NF3,NC6,...)
	//POST-CONDITION:	gameIndex is updated
	public void updateGameCheck(String moveList) {
	
	}
		
	
	
	/********* HELPER METHODS ***********/
	//DESCRIPTION:		Method determines if moving passed piece to passed coordinate is safe; returns false if piece would be threatened
	//PRE-CONDITION:	Passed board must be fully initialized with valid coordinates passed into method
	//POST-CONDITION:	Returns false if coordinate is unsafe; else, true
	public boolean isMoveSafe(Pieces[][] original, Pieces piece, int horzCoordinate, int vertCoordinate) {
		Pieces[][] dummyBoard = new Pieces[8][8];
		//Duplicate board for testing 
		this.duplicateBoard(original, dummyBoard);
		
		//Check if coordinate is valid for passed piece
		if (dummyBoard[piece.getHorz()][piece.getVert()].validCoordinate(dummyBoard, horzCoordinate, vertCoordinate)) {
			//If coordinate is threatened by enemy piece and passed piece would be left hanging, return false
				switch(piece.getColor()) {
				case 'w':
					//If passed piece is white, check if square isThreatenedByBlack; if true, return false as square is not safe
					if ((dummyBoard[horzCoordinate][vertCoordinate].getIsThreatenedByBlack()) &&
							(!(dummyBoard[horzCoordinate][vertCoordinate].getIsThreatenedByWhite()))) {
						return false;
					}
					else {
						return true;
					}
					
				case 'b': 
					if ((dummyBoard[horzCoordinate][vertCoordinate].getIsThreatenedByWhite()) &&
							(!(dummyBoard[horzCoordinate][vertCoordinate].getIsThreatenedByBlack()))) {
						return false;
					}
					else {
						return true;
					}
					
				}//End switch statement
			}
			//If coordinate is not valid, return false
			else {
				return false;
			}
		return false;
		}
	}
	
	
	
	

