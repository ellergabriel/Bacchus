import java.util.Scanner;

public class Chess {
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		/********** INITIALIZATION *************/
		
		//Scanner for capturing user input
		Scanner keyboard = new Scanner(System.in);
		
		//Board array for game loop
		Pieces[][] board = new Pieces[8][8];
		
		//Variable for tracking number of turns
		int turnCount = 0;
		
		//variable for storing player color, dummy string for parsing
		String dummyColor;
		char playerColor;
		
		//Variables for storing player pieces and coordinates as String when input
		String playerPiece;
		String playerCoordinate;
		
		//Variables for storing player piece selection and coordinate selection
		int horzSelect;
		int vertSelect;
		int horzCoordinate;
		int vertCoordinate;
		
		//Booleans for checking if turn is completed
		boolean turnComplete = false;
		
		//Place pieces on board
		Pawn.placePawn(board);
		Knight.placeKnight(board);
		Bishop.placeBishop(board);
		Rook.placeRook(board);
		Queen.placeQueen(board);
		King.placeKing(board);
		
		//Fill all empty squares with Square objects
		Squares.fillSquares(board);
		
		
		/********* PRE MATCH SETUP *****************/
		
		//Prompt player to pick side
		System.out.printf("Which side would you like to play?%n%c%n%c%n", 'w', 'b');
		dummyColor = keyboard.nextLine().toLowerCase();
		playerColor = dummyColor.charAt(0);
		
		//Error check to make sure color is correct; while loop to check error
		while ((playerColor != 'w') && (playerColor != 'b')) {
			System.out.printf("ERROR: Not a valid color. Try again.%n");
			dummyColor = keyboard.nextLine().toLowerCase();
			playerColor = dummyColor.charAt(0);
		} //End while loop 
		
		
		//Print board based on color
		switch(playerColor) {
		
		case 'w':
			Pieces.printBoardWhite(board);
			Bacchus blackComputer = new Bacchus(board, 'b');
			break; //end White case
			
		case 'b':
			Pieces.printBoardBlack(board);
			Bacchus whiteComputer = new Bacchus(board, 'w');
			break; //end Black case
		} //End switch 
		
		
		
		//Begin gameplay loop
		while (true) {
			
			//Switch statement for different starting cases
			switch(playerColor) {
			
			
			/********** PLAYER CHOOSES WHITE *********************/
			case 'w':
				
				//Do while loop for player turn
				do {
				/******* CHECKED KING CODE BLOCK *********/
				
				//If player is checked, must move king
				if (Pieces.isPlayerChecked(board, playerColor)) {
					while (Pieces.isPlayerChecked(board, playerColor)) {
						System.out.println("Check");
						System.out.printf("Select a piece to move:");
						playerPiece = keyboard.nextLine().toLowerCase();
						
						//Checks for valid user input
						while (!(Pieces.isPieceChoiceValid(board, playerPiece, playerColor))) {
							//Error message; prompt another selection
							System.out.printf("%nERROR: Invalid piece selection coordinate. Try again: ");
							playerPiece = keyboard.nextLine().toLowerCase();
						} //End while
						
						
						//Parse player piece selection into int coordinates
						horzSelect = Pieces.parseHorz(playerPiece.charAt(0));
						vertSelect = Pieces.parseVert(playerPiece.charAt(1)); 
						
						//Checks if player picked a non empty square
						while (Pieces.isEmpty(board, horzSelect, vertSelect)) {
							System.out.printf("%nERROR: Square is empty. Try again.");
							playerPiece = keyboard.nextLine().toLowerCase();
							
							//Parse player piece selection into int coordinates
							horzSelect = Pieces.parseHorz(playerPiece.charAt(0));
							vertSelect = Pieces.parseVert(playerPiece.charAt(1)); 
							
							//While loop to ensure player choice is valid and non empty
							while (!(Pieces.isPieceChoiceValid(board, playerPiece, playerColor))) {
								System.out.printf("%nERROR: Invalid piece selection. Try again: ");
								playerPiece = keyboard.nextLine().toLowerCase();
							}
						}
						
						//Player selects where to move piece
						//Prompt user to select move coordinate
						System.out.printf("%nSelect where piece will move: ");
						playerCoordinate = keyboard.nextLine().toLowerCase();
						
						
						//Error check for valid coordinate input
						while (!(Pieces.isPieceChoiceValid(board, playerCoordinate, playerColor))) {
							//Error message; prompt input
							System.out.printf("%nERROR: Invalid coordinate. Try again: ");
							playerCoordinate = keyboard.nextLine().toLowerCase();
						} //End while
						
						//Error check for identical coordinates
						while (playerPiece.equals(playerCoordinate)) {
							System.out.printf("%nERROR: Invalid coordinate. Try again: ");
							playerCoordinate = keyboard.nextLine().toLowerCase();
						}
						
						//Parse player coordinate into ints
						horzCoordinate = Pieces.parseHorz(playerCoordinate.charAt(0));
						vertCoordinate = Pieces.parseVert(playerCoordinate.charAt(1));
						
						//Check if passed coordinate is valid; Move selected piece if valid
						if (board[horzSelect][vertSelect].validCoordinate(board, horzCoordinate, vertCoordinate)) {
							//Move piece to coordinate
							board[horzSelect][vertSelect].move(board, horzCoordinate, vertCoordinate);
							//Pieces.placePiece(board, board[horzSelect][vertSelect], horzCoordinate, vertCoordinate);
						}
						
						//Check if King is still checked after move has been made
						King.checkUpdate(board, playerColor);
						
						//If king is still checked after move, prompt reentry by user
						if (Pieces.isPlayerChecked(board, playerColor)) {
							//Return moved piece to previous square
							Pieces.placePiece(board, board[horzCoordinate][vertCoordinate], horzSelect, vertSelect);
							System.out.printf("%nERROR: Still checked. Try again.");
						}
						//King is no longer checked; complete turn 
						else {
							turnComplete = true;
						}
					}
				}
				
				/*********** REGULAR MOVE CODE BLOCK *************/
				//If player is not checked, proceed with normal code block
				else {
				
					//Prompt player to select piece
					System.out.printf("Select a piece to move: ");
					playerPiece = keyboard.nextLine().toLowerCase();
					
					while (!(Pieces.isPieceChoiceValid(board, playerPiece, playerColor))) {
						//Error message; prompt another selection
						System.out.printf("%nERROR: Invalid piece selection coordinate. Try again: ");
						playerPiece = keyboard.nextLine().toLowerCase();
					} //End while
					
					
					//Parse player piece selection into int coordinates
					horzSelect = Pieces.parseHorz(playerPiece.charAt(0));
					vertSelect = Pieces.parseVert(playerPiece.charAt(1)); 
					
					//Checks if player picked an empty square
					while (Pieces.isEmpty(board, horzSelect, vertSelect)) {
						System.out.printf("%nERROR: Square is empty. Try again.");
						playerPiece = keyboard.nextLine().toLowerCase();
						
						//Parse player piece selection into int coordinates
						horzSelect = Pieces.parseHorz(playerPiece.charAt(0));
						vertSelect = Pieces.parseVert(playerPiece.charAt(1)); 
						
						//While loop to ensure player choice is valid and non empty
						while (!(Pieces.isPieceChoiceValid(board, playerPiece, playerColor))) {
							System.out.printf("%nERROR: Invalid piece selection coordinate. Try again: ");
							playerPiece = keyboard.nextLine().toLowerCase();
						}
					}
					
					
					//Player selects where to move piece
					//Prompt user to select move coordinate
					System.out.printf("%nSelect where piece will move: ");
					playerCoordinate = keyboard.nextLine().toLowerCase();
					
					
					//Error check for valid coordinate input
					while (!(Pieces.isPieceChoiceValid(board, playerCoordinate, playerColor))) {
						//Error message; prompt input
						System.out.printf("%nERROR: Invalid coordinate. Try again: ");
						playerCoordinate = keyboard.nextLine().toLowerCase();
					} //End while
					
					//Error check for identical coordinates
					while (playerPiece.equals(playerCoordinate)) {
						System.out.printf("%nERROR: Invalid coordinate. Try again: ");
						playerCoordinate = keyboard.nextLine().toLowerCase();
					} //End while
					
					//Parse player coordinate into ints
					horzCoordinate = Pieces.parseHorz(playerCoordinate.charAt(0));
					vertCoordinate = Pieces.parseVert(playerCoordinate.charAt(1));
					
					/*********** CASTLING CHECK *************/
					//Check if player select King piece
					if ((board[horzSelect][vertSelect] instanceof King) && (Math.abs(horzCoordinate - horzSelect) > 1)) {
					//Check if coordinate is +1; if true, check if King can castle; if not, print error message
						 //Check if King can castle; if so, move rook and king accordingly
						if (Chess.canCastle(board[horzSelect][vertSelect], board, horzCoordinate)) {
							Chess.castle(board, board[4][vertSelect], horzCoordinate);
							turnComplete = true;
						}
					}
					
					//Check if selected piece can move to playerCoordinate
					else if (board[horzSelect][vertSelect].validCoordinate(board, horzCoordinate, vertCoordinate)) {
						//Move piece to coordinate
						board[horzSelect][vertSelect].move(board, horzCoordinate, vertCoordinate);
						//Pieces.placePiece(board, board[horzSelect][vertSelect], horzCoordinate, vertCoordinate);
						//End turn; ends do While loop
						turnComplete = true;
					}
					//If coordinate is not valid, restart loop after error message
					else {
						//ERROR message
						System.out.printf("%nERROR: Invalid coordinate for piece. Try again.\n");
						turnComplete = false;
					}
				}
			} while (!(turnComplete)); //Continues until player completes turn
			
			//Post turn checks after White move
			//Refill board with threatened Squares object
			Squares.fillSquares(board);
			Chess.threatenUpdate(board);
			
			/*********** BLACK COMPUTER TURN *******************/
			//Increment turn count
			turnCount++;
			
			/*
			//KING CHECK TEST
			//Move rook to A2 for check test
			Pieces.placePiece(board, board[0][0], 0, 6);
			//Check each piece for threatened squares and threatened pieces
			for (int horz = 0; horz < 8; horz++) {
				for (int vert = 0; vert < 8; vert++) {
					board[horz][vert].threatenedSquares(board);
					board[horz][vert].threatenedPieces(board);
				}
			}
			*/
			//Computer/Black turn
			
			Pieces.printBoardWhite(board);
			
			
			//End white case
			break;
			/*********** PLAYER CHOOSES BLACK ****************/
			case 'b':
				//Do while loop for player turn
				do {
				
					/******* CHECKED KING CODE BLOCK *********/
					
					//If player is checked, must move king
					if (Pieces.isPlayerChecked(board, playerColor)) {
						while (Pieces.isPlayerChecked(board, playerColor)) {
							System.out.println("Check");
							System.out.printf("Select a piece to move:");
							playerPiece = keyboard.nextLine().toLowerCase();
							
							//Checks for valid user input
							while (!(Pieces.isPieceChoiceValid(board, playerPiece, playerColor))) {
								//Error message; prompt another selection
								System.out.printf("%nERROR: Invalid piece selection coordinate. Try again: ");
								playerPiece = keyboard.nextLine().toLowerCase();
							} //End while
							
							
							//Parse player piece selection into int coordinates
							horzSelect = Pieces.parseHorz(playerPiece.charAt(0));
							vertSelect = Pieces.parseVert(playerPiece.charAt(1)); 
							
							//Checks if player picked a non empty square
							while (Pieces.isEmpty(board, horzSelect, vertSelect)) {
								System.out.printf("%nERROR: Square is empty. Try again.");
								playerPiece = keyboard.nextLine().toLowerCase();
								
								//Parse player piece selection into int coordinates
								horzSelect = Pieces.parseHorz(playerPiece.charAt(0));
								vertSelect = Pieces.parseVert(playerPiece.charAt(1)); 
								
								//While loop to ensure player choice is valid and non empty
								while (!(Pieces.isPieceChoiceValid(board, playerPiece, playerColor))) {
									System.out.printf("%nERROR: Invalid piece selection. Try again: ");
									playerPiece = keyboard.nextLine().toLowerCase();
								}
							}
							
							//Player selects where to move piece
							//Prompt user to select move coordinate
							System.out.printf("%nSelect where piece will move: ");
							playerCoordinate = keyboard.nextLine().toLowerCase();
							
							
							//Error check for valid coordinate input
							while (!(Pieces.isPieceChoiceValid(board, playerCoordinate, playerColor))) {
								//Error message; prompt input
								System.out.printf("%nERROR: Invalid coordinate. Try again: ");
								playerCoordinate = keyboard.nextLine().toLowerCase();
							} //End while
							
							//Error check for identical coordinates
							while (playerPiece.equals(playerCoordinate)) {
								System.out.printf("%nERROR: Invalid coordinate. Try again: ");
								playerCoordinate = keyboard.nextLine().toLowerCase();
							} //End while
							
							//Parse player coordinate into ints
							horzCoordinate = Pieces.parseHorz(playerCoordinate.charAt(0));
							vertCoordinate = Pieces.parseVert(playerCoordinate.charAt(1));
							
							//Check if passed coordinate is valid; Move selected piece if valid
							if (board[horzSelect][vertSelect].validCoordinate(board, horzCoordinate, vertCoordinate)) {
								//Move piece to coordinate
								board[horzSelect][vertSelect].move(board, horzCoordinate, vertCoordinate);
								//Pieces.placePiece(board, board[horzSelect][vertSelect], horzCoordinate, vertCoordinate);
							}
							
							//Check if King is still checked after move has been made\
							King.checkUpdate(board, playerColor);
							
							//If king is still checked after move, prompt reentry by user
							if (Pieces.isPlayerChecked(board, playerColor)) {
								//Return moved piece to previous square
								Pieces.placePiece(board, board[horzCoordinate][vertCoordinate], horzSelect, vertSelect);
								System.out.printf("%nERROR: Still checked. Try again.");
							}
							//King is no longer checked; complete turn 
							else {
								Pieces.printBoardBlack(board);
								turnComplete = true;
							}
						}
					}
				
				
				else {
					//Prompt player to select piece
					System.out.printf("Select a piece to move: ");
					playerPiece = keyboard.nextLine().toLowerCase();
					
					while (!(Pieces.isPieceChoiceValid(board, playerPiece, playerColor))) {
						//Error message; prompt another selection
						System.out.printf("%nERROR: Invalid piece selection. Try again: ");
						playerPiece = keyboard.nextLine().toLowerCase();
					} //End while
					
					
					//Parse player piece selection into int coordinates
					horzSelect = Pieces.parseHorz(playerPiece.charAt(0));
					vertSelect = Pieces.parseVert(playerPiece.charAt(1)); 
					
					//Checks if player picked a non empty square
					while (Pieces.isEmpty(board, horzSelect, vertSelect)) {
						System.out.printf("%nERROR: Square is empty. Try again.");
						playerPiece = keyboard.nextLine().toLowerCase();
						
						//Parse player piece selection into int coordinates
						horzSelect = Pieces.parseHorz(playerPiece.charAt(0));
						vertSelect = Pieces.parseVert(playerPiece.charAt(1)); 
						
						//While loop to ensure player choice is valid and non empty
						while (!(Pieces.isPieceChoiceValid(board, playerPiece, playerColor))) {
							System.out.printf("%nERROR: Invalid piece selection coordinate. Try again: ");
							playerPiece = keyboard.nextLine().toLowerCase();
						}
					}
					
					//Player selects where to move piece
					//Prompt user to select move coordinate
					System.out.printf("%nSelect where piece will move: ");
					playerCoordinate = keyboard.nextLine().toLowerCase();
					
					
					//Error check for valid coordinate input
					while (!(Pieces.isPieceChoiceValid(board, playerCoordinate, playerColor))) {
						//Error message; prompt input
						System.out.printf("%nERROR: Invalid coordinate. Try again: ");
						playerCoordinate = keyboard.nextLine().toLowerCase();
					} //End while
					
					//Error check for identical coordinates
					while (playerPiece.equals(playerCoordinate)) {
						System.out.printf("%nERROR: Invalid coordinate. Try again: ");
						playerCoordinate = keyboard.nextLine().toLowerCase();
					} //End while
					
					//Parse player coordinate into ints
					horzCoordinate = Pieces.parseHorz(playerCoordinate.charAt(0));
					vertCoordinate = Pieces.parseVert(playerCoordinate.charAt(1));
					
					/*********** CASTLING CHECK *************/
					//Check if player select King piece
					if ((board[horzSelect][vertSelect] instanceof King) && (Math.abs(horzCoordinate - horzSelect) > 1)) {
					//Check if coordinate is +1; if true, check if King can castle; if not, print error message
						 //Check if King can castle; if so, move rook and king accordingly
						if (Chess.canCastle(board[horzSelect][vertSelect], board, horzCoordinate)) {
							Chess.castle(board, board[4][vertSelect], horzCoordinate);
							turnComplete = true;
						}
					}
					
					//Check if selected piece can move to playerCoordinate
					else if (board[horzSelect][vertSelect].validCoordinate(board, horzCoordinate, vertCoordinate)) {
						//Move piece to coordinate
						board[horzSelect][vertSelect].move(board, horzCoordinate, vertCoordinate);
						//Pieces.placePiece(board, board[horzSelect][vertSelect], horzCoordinate, vertCoordinate);
						//End turn; ends do While loop
						turnComplete = true;
					}
					//If coordinate is not valid, restart loop after error message
					else {
						//ERROR message
						System.out.printf("%nERROR: Invalid coordinate for piece. Try again.\n");
						turnComplete = false;
					}
				}//End else statement
			
			} while (!(turnComplete)); //Continues until player completes turn
			
			//Post turn checks after White move
			//Refill board with threatened Squares object
			Squares.fillSquares(board);
			
			//Check each piece for threatened squares and threatened pieces
			Chess.threatenUpdate(board);
			
			/************* WHITE COMPUTER TURN ************/
			//Increment turn count
			turnCount++;
			
			//Computer/Black turn
			Pieces.printBoardBlack(board);
			break;
			}
		}
		
	}
		
		//DESCRIPTION:		Static function checks if King can castle; returns true if King can; else false
		//PRE-CONDITION:	Board array must be fully intiliazed
		//POST-CONDITION:	Returns true if king can castle, else false
		public static boolean canCastle(Pieces piece, Pieces[][] board, int horz) {
			//Check if King is checked; if checked, cannot castle
			if(piece.getIsChecked()) {
				return false;
			}
			
			if (piece instanceof King) {
				//Check if piece is in correct file (E file)
				if (piece.getHorz() != 4) {
					return false;
				}
				else {
					//Switch statement for checking vert of both color kings
					switch(piece.getColor()) {
					
					//White king case
					case 'w': 
						//Check vert; return false if not 7
						if (piece.getVert() != 7) {
							return false;
						}
						break;
					
					//Black king case
					case 'b':
						//if Vert is not 0, return false
						if (piece.getVert() != 0) {
							return false;
						}
						break;
					} //End switch 
				}
				
				//Check if passed coordinate is valid castle coordinate; 2 or 6
				if ((horz != 2) && (horz != 6)) {
					return false;
				}
				
				//If coordinate is valid, check if rook is on correct side
				switch(horz) {
				
				//Queen side case
				case 2:
					//Check if white has queen side rook
					if(piece.getColor() == 'w') {
						
						//Check if C1 is threatened by other square; if threatened, return false 
						if (board[2][7].getIsThreatenedByBlack()) {
							return false;
						}
						
						//Check for rook on A1
						if ((board[0][7] instanceof Rook) && (!(Pieces.isDifferentColor(piece, board[0][7]))
								&& (Pieces.isEmpty(board, 1, 7)) && (Pieces.isEmpty(board, 2, 7)) 
								&& (Pieces.isEmpty(board, 3, 7)))) {
							return true;
						}
					}
					
					//Check if black king has queen side rook
					else {
						
						//Check if C8 is threatened; if threatened, return false
						if (board[2][0].getIsThreatenedByWhite()) {
							return false;
						}
						
						//Check for A8 rook
						if ((board[0][0] instanceof Rook) && (!(Pieces.isDifferentColor(piece, board[0][0]))
								&& (Pieces.isEmpty(board, 1, 0))&& (Pieces.isEmpty(board, 2,  0)) 
								&& (Pieces.isEmpty(board, 3, 0)))) {
							return true;
						}
					}
					//End queen side case
					break;
				case 6:
					//Check if White has king side rook
					if (piece.getColor() == 'w') {
						
						//Check if G1 is threatened; if threatened, return false
						if (board[6][7].getIsThreatenedByBlack()) {
							return false;
						}
						
						//Check for rook on H1
						if ((board[7][7] instanceof Rook) && (!(Pieces.isDifferentColor(piece, board[7][7])))
								&& (Pieces.isEmpty(board, 5, 7)) && (Pieces.isEmpty(board, 6, 7))) {
							return true;
						}
					}
					
					//Black king check
					else {
						
						//Check if G8 is threatened
						if (board[6][0].getIsThreatenedByWhite()) {
							return false;
						}
						
						//Check for rook on H8
						if ((board[7][0] instanceof Rook) && (!(Pieces.isDifferentColor(piece, board[7][0])) 
								&& (Pieces.isEmpty(board, 5, 0)) && (Pieces.isEmpty(board, 6, 0)))) {
							return true;
						}
					}
					
				}
			}
			//If piece is not king, return false
			else {
				return false;
			}
			
			//Failsafe for return
			return false;
		}
		
		
		//DESCRIPTION:		Static function castles King piece
		//PRE-CONDITION:	Passed coordinates must be valid
		//POST-CONDITION:	King and appropiate rook are castled
		public static void castle(Pieces[][] board, Pieces king, int horz) {
			
			//Switch statement for castling on either side
			
			switch(horz) {
			
			//King side castle
			case 6:
				
				//Place King onto G file
				Pieces.placePiece(board, board[4][king.getVert()], horz, king.getVert());
				//Place Rook onto F file
				Pieces.placePiece(board, board[7][king.getVert()], horz - 1, king.getVert());
				break; 
				//End King side castle case
			
			//Queen side castle
			case 2:
				
				//Place King onto C file 
				Pieces.placePiece(board, board[4][king.getVert()], horz, king.getVert());
				//Place Rook onto D file
				Pieces.placePiece(board, board[0][king.getVert()], horz + 1, king.getVert());
				break;
				//End Queen side castle case
			} 
		}

		
		//DESCRIPTION:		Updates Squares objects to determine which squares are threatened
		//PRE-CONDITION:	Board must be fully initialized
		//POST-CONDITION:	Each square object is updated if threatened
		public static void threatenUpdate(Pieces[][] board) {
			
			//Boolean controls while loop to allow exception handling
			boolean isChecking = true;
			
			//Nested for loop to iterate through all squares
			while(isChecking) {
				for (int horz = 0; horz < 8; horz++) {
					for (int vert = 0; vert < 8; vert++) {
	
						 try {
							 //If object is Squares, continue to next iteration
							 if(board[horz][vert] instanceof Squares) {
								 continue;
							 }
							 
							 board[horz][vert].threatenedSquares(board);
							 board[horz][vert].threatenedPieces(board);
							 
							 //Catch end of for loop 
							 if((horz == 7) && (vert == 7)) {
								 isChecking = false;
							 }
						 } catch (IndexOutOfBoundsException i) {
							 //If index out of bounds exception is caught, continue to next iteration of loop
							 continue;
						 }
					}
				}
				isChecking = false;
			}
		}
		
		
		//DESCRIPTION:		Resets threats instance variable of each piece for next turn
		//PRE-CONDITION:	board must be fully initialized
		//POST-CONDITION:	All pieces have threats reset to 0
		public static void resetThreats(Pieces[][] board) {
			//Double for loop to reset each square
			for(int horz = 0; horz < 8; horz++) {
				for(int vert = 0; vert < 8; vert++) {
					
					if(board[horz][vert] instanceof Squares) {
						continue;
					}
					else {
					
						board[horz][vert].setThreats(0);
					}
				}
			}
		}
	
		
		
		
	}


