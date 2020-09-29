package Bacchus;
import java.util.*;

/* UML Class Diagram
--------------------
* Pieces *
--------------------
* - piece : char
* - color : char
* - horz : int
* - vert : int
* - isTaken : boolean
--------------------
* + Pieces()
* + Pieces(piece: char, color : char, horz : int, vert : int,
* 			isTaken : boolean)
* 
* + getPiece() : char
* + getColor() : char
* + getHorz() : int
* + getVert() : int
* + getIsTaken() : boolean
* 
* + setPiece(piece : char) : void
* + setColor(color : char) : void
* + setHorz(horz : int) : void
* + setVert(vert : int) : void
* + setIsTaken(isTaken : boolean) : void
* + setAll(piece : char, color : char, horz : int, vert : int,
* 			isTaken : boolean) : void
* 
* + threats(board : Pieces[][]) : int
*/

public class Pieces
{
	protected char piece;
	protected char color;
	protected int horz;
	protected int vert;
	protected int threats;
	protected double value;
	protected boolean isThreatenedByWhite;
	protected boolean isThreatenedByBlack;
	protected boolean isHanging;
	protected boolean isPinnedVert;
	protected boolean isPinnedHorz;
	protected boolean isPinnedDiagonal;
	protected boolean isPinning;
	protected boolean isChecked;
	protected boolean canPass;
	
	//ArrayList for storing pieces that can be attacked by calling piece
	ArrayList<Pieces> possibleAttacks;
	
	
	/************** CONSTRUCTORS **************/
	
	//DESCRIPTION:		Constructs Pieces object with default instance variable values
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Pieces object is constructed with default values
	Pieces() {
		piece = 'a';
		color = ' ';
		horz = 0;
		vert = 0;
		value = 0;
		threats = 0;
		isThreatenedByWhite = false;
		isThreatenedByBlack = false;
		isHanging = false;
		isPinnedVert = false;
		isPinnedHorz = false;
		isPinnedDiagonal = false;
		isPinning = false;
		isChecked = false;
		canPass = false;
		possibleAttacks = new ArrayList<Pieces>();
	}
	
	//DESCRIPTION:		Constructs Pieces object with passed values
	//PRE-CONDITION:	Passed values must be valid
	//POST-CONDITION:	Pieces object is created with passed values
	Pieces(char piece, char color, int horz, int vert) {
		this.piece = piece;
		this.color = color;
		this.horz = horz;
		this.vert = vert;
		value = 0;
		threats = 0;
		isThreatenedByWhite = false;
		isThreatenedByBlack = false;
		isHanging = false;
		isPinnedVert = false;
		isPinnedHorz = false;
		isPinnedDiagonal = false;
		isPinning = false;
		isChecked = false;
		canPass = false;
		possibleAttacks = new ArrayList<Pieces>();
	}
	
	//DESCRIPTION:		Constructs copy of Pieces object
	//PRE-CONDITION:	Original object must be valid
	//POST-CONDITION:	New pawn object is created
	Pieces(Pieces original) {
		this.setAll(original.getPiece(), original.getColor(), original.getHorz(),
					original.getVert(), original.getValue(),
					original.getThreats(), original.getIsThreatenedByWhite(), original.getIsThreatenedByBlack(),
					original.getIsHanging(), original.getIsPinnedVert(), 
					original.getIsPinnedHorz(), original.getIsPinnedDiagonal(),
					original.getIsPinning(), original.getIsChecked(), original.getCanPass());
	}
	
	
	/************** ACCESSORS *******************/
	
	//DESCRIPTION:		Returns piece instance variable of calling object
	//PRE-CONDITION:	Calling object must have valid piece char
	//POST-CONDITION:	piece instance variable is returned
	public char getPiece()
	{
		return piece;
	}
	
	//DESCRIPTION:		Returns color instance variable of calling object
	//PRE-CONDITION:	Calling object must have valid color char
	//POST-CONDITION:	color instance variable is returned
	public char getColor()
	{
		return color;
	}
	
	//DESCRIPTION:		Returns horz instance variable of calling object
	//PRE-CONDITION:	Calling object must have valid int horz value
	//POST-CONDITION:	horz int value is returned
	public int getHorz()
	{
		return horz;
	}
	
	//DESCRIPTION:		Returns vert instance variable of calling object
	//PRE-CONDITION:	Calling object must have valid int vert value
	//POST-CONDITION:	vert int value is returned
	public int getVert()
	{
		return vert;
	}
	
	//DESCRIPTION:		Returns value instance variable of calling piece
	//PRE-CONDITION:	Piece must be initialized
	//POST-CONDITION:	value is returned
	public double getValue() {
		return value;
	}
	
	//DESCRIPTION:		Returns threats instance variable
	//PRE-CONDITION:	Calling piece must be instantiated
	//POST-CONDITION:	Value of threats is returned
	public int getThreats() {
		return threats;
	}
	
	//DESCRIPTION:		Returns isTaken instance variable of calling object
	//PRE-CONDITION:	Calling object must have valid isTaken boolean
	//POST-CONDITION:	isTaken boolean is returned
	public boolean getIsThreatenedByWhite(){
		return isThreatenedByWhite;
	}
	
	//DESCRIPTION:		Returns isThreatenedByBlack variable of calling object
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	Boolean value of isThreatenedByBlack is returned
	public boolean getIsThreatenedByBlack() {
		return isThreatenedByBlack;
	}
	
	//DESCRIPTION:		Returns isHanging instance variable of calling object
	//PRE-CONDITION:	Calling object must be fully instantiated
	//POST-CONDITION:	isHanging boolean is returned
	public boolean getIsHanging() {
		return isHanging;
	}
	
	//DESCRIPTION:		Returns isPinnedLinear boolean of calling object
	//PRE-CONDITION:	Calling object must have valid isPinned boolean
	//POST-CONDITION:	isPinnedLinear is returned
	public boolean getIsPinnedVert() {
		return isPinnedVert;
	}
	
	//DESCRIPTION:		Returns isPinnedHorz boolean of calling object
	//PRE-CONDITION:	Calling object must be fully initialized
	//POST-CONDITION:	isPinnedHorz is returned
	public boolean getIsPinnedHorz() {
		return isPinnedHorz;
	}
	
	//DESCRIPTION:		Returns isPinnedDiagonal boolean of calling object
	//PRE-CONDITION:	Calling object must have valid isPinnedDiagonal boolean
	//POST-CONDITION:	isPinnedDiagonal is returned
	public boolean getIsPinnedDiagonal() {
		return isPinnedDiagonal;
	}
	
	//DESCRIPTION:		Returns isPinning boolean of calling object
	//PRE-CONDITION:	Calling object must be instantiated 
	//POST-CONDITION:	isPinning is returned
	public boolean getIsPinning() {
		return this.isPinning;
	}
	
	//DESCRIPTION:		Returns isChecked boolean of calling object
	//PRE-CONDITION:	Calling object must have valid isChecked boolean
	//POST-CONDITION:	isChecked is returned
	public boolean getIsChecked() {
		return false;
	}
	
	//DESCRIPTION:		Returns canPass boolean of calling object
	//PRE-CONDITION:	Calling object must have valid canPass value
	//POST-CONDITION:	canPass boolean is returned
	public boolean getCanPass() {
		return false;
	}
	
	//DESCRIPTION:		Returns memory address of calling piece's possibleAttack ArrayList
	//PRE-CONDITION:	Calling piece must have possibleAttack initiliazed
	//POST-CONDITION:	possibleAttack memory address is returned
	public ArrayList<Pieces> getPossibleAttacks(){
		return this.possibleAttacks;
	}
	
	//DESCRIPTION:		Returns piece stored at passed index of possibleAttacks ArrayList
	//PRE-CONDITION:	possibleAttacks must be initialized
	//POST-CONDITION:	Memory address of Pieces object at index of possibleAttacks is returned
	public Pieces getPossibleAttackedPiece(int index) {
		//Try catch block to catch any null pointers
		try {
			return this.possibleAttacks.get(index);
		} catch (NullPointerException n) {
			return null;
		} catch (IndexOutOfBoundsException i) {
			return null;
		}
		
	}
	
	/*************** MUTATORS ****************/
	
	//DESCRIPTION:		Sets piece instance variable of calling object
	//PRE-CONDITION:	Calling object must be initialized
	//POST-CONDITION:	piece isntance variable is set to passed char
	public void setPiece(char piece)
	{
		this.piece = piece;
	}
	
	//DESCRIPTION:		Sets color instance variable of calling object
	//PRE-CONDITION:	Calling object must be initialized
	//POST-CONDITION:	color instance variable is set to passed char
	public void setColor(char color)
	{
		this.color = color;
	}
	
	//DESCRIPTION:		Sets horz instance variableof calling object
	//PRE-CONDITION:	Calling object must be intitialized
	//POST-CONDITION:	horz instance variable is set to passed int
	public void setHorz(int horz)
	{
		this.horz = horz;
	}
	
	//DESCRIPTION:		Sets vert instance variable of calling object
	//PRE-CONDITION:	Calling object must be initialized
	//POST-CONDITION:	vert instance variable is set to passed int
	public void setVert(int vert)
	{
		this.vert = vert;
	}
	
	//DESCRIPTION:		Sets value instance variable of calling object
	//PRE-CONDITION:	Calling object must be fully intialized
	//POST-CONDITION:	value instance variable is set to passed double
	public void setValue(double value) {
		this.value = value;
	}
	
	//DESCRIPTION:		Sets value of threats instance variable
	//PRE-CONDITION:	Caling object must be fully intsantiated
	//POST-CONDITION:	Value of threats variable is set to passed int
	public void setThreats(int threats) {
		this.threats = threats;
	}
	
	//DESCRIPTION:		Sets isTaken instance variable of calling object
	//PRE-CONDITION:	Calling object must be initialized
	//POST-CONDITION:	isTaken instance variable is set to passed boolean
	public void setIsThreatenedByWhite(boolean isThreatenedByWhite){
		this.isThreatenedByWhite = isThreatenedByWhite;
	}
	
	//DESCRIPTION:		Sets isThreatenedByBlack variable of calling object
	//PRE-CONDITION:	Calling object must be initialized
	//POST-CONDITION:	isThreatenedByBlack is set to passed boolean
	public void setIsThreatenedByBlack(boolean isThreatenedByBlack) {
		this.isThreatenedByBlack = isThreatenedByBlack;
	}
	
	//DESCRIPTION:		Sets isHanging variable of calling object
	//PRE-CONDITION:	Calling object must be initialized and boolean must be passed
	//POST-CONDITION:	isHanging instance variable is set to passed boolean
	public void setIsHanging(boolean isHanging) {
		this.isHanging = isHanging;
	}
	
	//DESCRIPTION:		Sets isPinned instance variable of calling object
	//PRE-CONDITION:	Calling object must be initialized and boolean must be passed
	//POST-CONDITION:	isPinned is set to passed boolean
	public void setIsPinnedVert(boolean isPinnedVert) {
		this.isPinnedVert = isPinnedVert;
	}
	
	//DESCRIPTION:		Sets isPinnedHorz boolean of calling object
	//PRE-CONDITION:	Calling object must be intiialized and boolean must be passed
	//POST-CONDITION:	isPinnedHorz boolean is set to passed boolean
	public void setIsPinnedHorz(boolean isPinnedHorz) {
		this.isPinnedHorz = isPinnedHorz;
	}
	
	//DESCRIPTION:		Sets isPinnedDiagonal boolean of calling object
	//PRE-CONDITION:	Calling object must be initialized and boolean must be passed
	//POST-CONDITION:	isPinnedDiagonal is set to passed boolean
	public void setIsPinnedDiagonal(boolean isPinnedDiagonal) {
		this.isPinnedDiagonal = isPinnedDiagonal;
	}
	
	//DESCRIPTION:		Sets isPinning to passed boolean
	//PRE-CONDITION:	Calling object must be non null
	//POST-CONDITION:	isPinning is set to passed boolean
	public void setIsPinning(boolean isPinning) {
		this.isPinning = isPinning;
	}
	
	//DESCRIPTION:		Sets isChecked boolean of calling object
	//PRE-CONDITION:	Calling object must be initialized and boolean must be passed
	//POST-CONDITION:	isChecked is set to passed boolean
	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	//DESCRIPTION:		Sets canPass boolean of calling object
	//PRE-CONDITION:	Boolean must be passed and calling object must be instantiated
	//POST-CONDITION:	canPass boolean is set to passed value
	public void setCanPass(boolean canPass) {
		this.canPass = canPass;
	}
	
	//DESCRIPTION:		Adds new piece to possibleAttacks ArrayList
	//PRE-CONDITION:	Passed object must be of Pieces type
	//POST-CONDITION:	New Pieces object is added to calling piece possibleAttacks ArrayList
	public void addPossibleAttacks(Pieces piece) {
		this.possibleAttacks.add(piece);
	}
	
	//DESCRIPTION:		Sets all instance variables of calling object
	//PRE-CONDITION:	Passed values must all be valid
	//POST-CONDITION:	All instance variables of calling object are set
	public void setAll(char piece, char color, int horz, int vert, double value,
					   int threats, boolean isThreatenedByWhite, boolean isThreatenedByBlack, boolean isHanging,
					   boolean isPinnedVert, boolean isPinnedHorz,
					   boolean isPinnedDiagonal, boolean isPinning,
					   boolean isChecked, boolean canPass) { 
		this.piece = piece;
		this.color = color;
		this.horz = horz;
		this.vert = vert;
		this.value = value;
		this.threats = threats;
		this.isThreatenedByWhite = isThreatenedByWhite;
		this.isThreatenedByBlack = isThreatenedByBlack;
		this.isHanging = isHanging;
		this.isPinnedVert = isPinnedVert;
		this.isPinnedHorz = isPinnedHorz;
		this.isPinnedDiagonal = isPinnedDiagonal;
		this.isPinning = isPinning;
		this.isChecked = isChecked;
		this.canPass = canPass;
	}
	
	//DESCRIPTION:		Increments threats variable of calling object
	//PRE-CONDITION:	Calling piece must be fully instantiated
	//POST-CONDITION:	threats instance variable is incremented
	public void updateThreats() {
		this.threats++;
	}
	/************** ENGINE METHODS *****************/

	//DESCRIPTION:		Checks to see if piece can move; base class method for subclass overriding
	//PRE-CONDITION:	Calling object must be initialized
	//POST-CONDITION:	Returns true if piece can move; else false
	public boolean canMove(Pieces[][] board) {
		return false;
	}

	
	//DESCRIPTION:		Override method for canAttack boolean in subclass objects
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	White space is printed
	public boolean canAttack(Pieces[][] board, int horz, int vert) {
		return true;
	}
	
	
	//DESCRIPTION:		Override attack method for subclasses
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	White space is printed
	public boolean attack(Pieces[][] board, int horz, int vert) {
		return true;
	}
	
	//DESCRIPTION:		Override canPassant method for pawn class
	//PRE-CONDITION:	Yep
	//POST-CONDITION:	Nope
	public boolean canPassant(Pieces[][] board, int horz) {
		return false;
	}
	
	/******** PLACEHOLDER POLYMORPHS *************/
	
	//DESCRIPTION:		move method placeholder	
	//PRE-CONDITION:	n/a
	//POST-CONDITION:	n/a
	public boolean move(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	//DESCRIPTION:		Pawn placeholder	
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	N/A
	public boolean canDoubleMove(Pieces[][] board) {
		return false;
	}
	public boolean doubleMove(Pieces[][] board) {
		return false;
	}
	
	
	//DESCRIPTION:		Method used for Knight class orverriding	
	//PRE-CONDITION:	Whatevs
	//POST-CONDITION:	Yeah
	public boolean canLong(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	//DESCRIPTION:		Method used for Knight class overriding	
	//PRE-CONDITION:	Shazbot
	//POST-CONDITION:	Neck Deep sucks mate
	public boolean canTall(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	
	//DESCRIPTION:		Method used for overriding later
	//PRE-CONDITION:	lmao whatever
	//POST-CONDITION:	yeah it dont matter
	public boolean canDiagonal(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	//DESCRIPTION:		Overriding
	//PRE-CONDITION:	lmao
	//POST-CONDITION:	lmao
	public boolean canDiagonalPinned(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	//DESCRIPTION:		Same thing, overriding
	//PRE-CONDITION:	Whatevs
	//POST-CONDITION:	Eh
	public boolean canLinear(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	//DESCRIPTION:		Yep
	//PRE-CONDITION:	Same
	//POST-CONDITION:	Metoothnx
	public boolean canFork(Pieces[][] board) {
		return false;
	}
	
	//DESCRIPTION:		Again, same thing about overriding
	//PRE-CONDITION:	Yup
	//POST-CONDITION:	Go java
	public boolean validCoordinate(Pieces[][] board, int horz, int vert) {
		return false;
	}
	
	//DESCRIPTION:		Yep
	//PRE-CONDITION:	yep
	//POST-CONDITION:	yep
	public int threatenedSquaresLinear(Pieces[][] board) {
		return 0;
	}
	
	//DESCRIPTION:		Yep
	//PRE-CONDITION:	Yep
	//POST-CONDITION:	Yep
	public int threatenedSquaresDiagonal(Pieces[][] board) {
		return 0;
	}
	
	//DESCRIPTION:		Yep, override	
	//PRE-CONDITION:	yep
	//POST-CONDITION:	mhm
	public int threatenedSquares(Pieces[][] board) {
		return 0;
	}
	
	//DESCRIPTION:		same thing	
	//PRE-CONDITION:	sames
	//POST-CONDITION:	samesies
	public int threatenedPieces(Pieces[][] board) {
		return 0;
	}
	
	//DESCRIPTION:		Same
	//PRE-CONDITION:	same
	//POST-CONDITION:	same
	public int threatenedPiecesDiagonal(Pieces[][] board) {
		return 0;
	}
	
	//DESCRIPTION:		same	
	//PRE-CONDITION:	same
	//POST-CONDITION:	same
	public int threatenedPiecesLinear(Pieces[][] board) {
		return 0;
	}

	
	//DESCRIPTION:		Same
	//PRE-CONDITION:	Same
	//POST-CONDITION:	Same
	public void castle(Pieces[][] board, int horz) {
		
	}
	
	/********* HELPER METHODS **************/
	
	//DESCRIPTION:		Places calling object on to passed board array
	//PRE-CONDITION:	Passed array must be intialized and object must be intialized
	//POST-CONDITION:	Object is copied onto board with matching coordinates
	public void placeBoard(Pieces[][] board) {
		//Place object on board with instance variable coordinates
		board[this.horz][this.vert] = new Pieces(this);
	}
	
	
	//DESCRIPTION:		Prints piece color and type
	//PRE-CONDITION:	color and piece instance variables must have non-null char
	//POST-CONDITION:	color and piece are printed in cC pattern
	public String boardPrint() {
		return String.format("%c%C", this.color, this.piece);
	}
	
	//DESCRIPTION:		Prints coordinate of piece
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Coordinate of piece is printed to console in "charInt" form i.e A1, B2, C3
	public String toString()
	{
		
		int newVert;
		newVert = 8 - this.vert;
		
		switch(this.horz)
		{
			case 0:
				return String.format("%c%C %C%d", this.color, this.piece, 'a', newVert);
			case 1:
				return String.format("%c%C %C%d", this.color, this.piece, 'b', newVert);
			case 2:
				return String.format("%c%C %C%d", this.color, this.piece, 'c', newVert);
			case 3:
				return String.format("%c%C %C%d", this.color, this.piece, 'd', newVert);
			case 4:
				return String.format("%c%C %C%d", this.color, this.piece, 'e', newVert);
			case 5:
				return String.format("%c%C %C%d", this.color, this.piece, 'f', newVert);
			case 6:
				return String.format("%c%C %C%d", this.color, this.piece, 'g', newVert);
			case 7:
				return String.format("%c%C %C%d", this.color, this.piece, 'h', newVert);
		}
		return "null";
	}
	
	//DESCRIPTION:		Overrides parent equals method; compares two Pieces objects
	//PRE-CONDITION:	Passed object must be of Pieces types
	//POST-CONDITION:	Returns true if calling and passed piece have identical instance variables; else returns false
	public boolean equals(Object other) {
		//If other is not a Pieces object, return false
		if (!(other instanceof Pieces)) {
			return false;
		}
		else {
			//Return value is based on values within other
			Pieces dummy = (Pieces) other;
			
			return ((this.getHorz() == dummy.getHorz()) && (this.getVert() == dummy.getVert()) && (this.getColor() == dummy.getColor())
					&& (this.getValue() == dummy.getValue()) && (this.getPiece() == dummy.getPiece()));
		}
	}
	
	/******** STATIC METHODS **********/
	
	//DESCRIPTION:		Checks if King is checked based on color
	//PRE-CONDITION:	Passed color must be 'w' or 'b' with a fully intiialized board passed
	//POST-CONDITION:	Returns true if King belonging to passed color is checked; else false
	public static boolean isPlayerChecked(Pieces[][] board, char color) {
		
		//Error check for passed color
		if ((color != 'w') && (color != 'b')) {
			return false;
		}
		
		//Nested for loop to find King in board array
		for (int horz = 0; horz < 8; horz++) {
			for (int vert = 0; vert < 8; vert++) {
				//Check if piece is King of passed color
				if((board[horz][vert].getPiece() == 'K') && (board[horz][vert].getColor() == color)) {
					
					//Return boolean of King inCheck
					return board[horz][vert].getIsChecked();
				}
				//If coordinate is not King of same color, continue
				else {
					continue;
				}
			}
		}
		
		return false;
	}
	
	//DESCRIPTION:		Prints out board with piece chars represented
	//PRE-CONDITION:	Board must be initialized
	//POST-CONDITION:	Board is printed to console
	public static void printBoardWhite(Pieces[][] board) {
		
		//Nested for loop for each file and rank
		
			for (int n = 0; n < 8 ; n++) {

				System.out.printf("%s" , " _____");
				for (int i = 0; i < 7; i++) {
					System.out.printf("%s", "  _____");
				}
			
				System.out.println();
				
				for (int i = 7; i > -1; i--) {
					System.out.printf("%s", "|     |");
				}
				
				System.out.println();
				
				for(int i = 0; i < 8; i++) {
					
					//Checks if square is empty and not occupied by a squares object
					if (Pieces.isEmpty(board, i, n)) {
						System.out.printf("%s", "|     |");
					}
					else {
						
						//Switch statement to adjust board mirror
						switch(board[i][n].getColor()) {
						
						case 'b':
							System.out.printf("%s%s%s", "|  ", board[i][n].boardPrint(), " |");
							break;
						case 'w' :
							System.out.printf("%s%s%s", "|  ", board[i][n].boardPrint(), " |");
							break;
						
						case 's':
							if (board[i][n].getIsThreatenedByWhite()) {
								System.out.printf("%s%c%s", "|  ", 'x', "  |");
								break;
								
							}
						}
					}
					
					//If end of loop has been reached, print rank
					if (i == 7) {
						System.out.print(Math.abs(n - 8));
					}
				}
				
				System.out.println();
				
				for (int i = 0; i < 8; i++) {
					System.out.printf("%s", "|_____|");
				}
				
				System.out.println();
			}
			
			//After board has been printed, print A-H file name
			System.out.printf("   %C\t  %C\t %C\t%C      %C      %C      %C      %C", 
								'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
	}
	
	
	
	//DESCRIPTION:		Prints board from black perspective
	//PRE-CONDITION:	Board array must be fully initialized
	//POST-CONDITION:	Board is printed from black perspective
	public static void printBoardBlack(Pieces[][] board) {
		
			for (int n = 7; n > -1 ; n--) {

				System.out.printf("%s" , " _____");
				for (int i = 0; i < 7; i++) {
					System.out.printf("%s", "  _____");
				}
			
				System.out.println();
				
				for (int i = 7; i > -1; i--) {
					System.out.printf("%s", "|     |");
				}
				
				System.out.println();
				
				for(int i = 7; i > -1; i--) {
					
					//Checks if square is empty and not occupied by a squares object
					if (Pieces.isEmpty(board, i, n)) {
					
					System.out.printf("%s", "|     |");
					}
					else {
						
						//Switch statement to adjust board mirror
						switch(board[i][n].getColor()) {
						
						case 'b':
							//Adjust coordinate representation of queen and king
							System.out.printf("%s%s%s", "|  ", board[i][n].boardPrint(), " |");
							break;
						case 'w' :
							System.out.printf("%s%s%s", "|  ", board[i][n].boardPrint(), " |");
							break;
						
						case 's':
							if (board[i][n].getIsThreatenedByWhite()) {
								System.out.printf("%s%c%s", "|  ", 'x', "  |");
								break; 
							}
						}
					}
					
					//If i is 0, end of line is reached; print rank
					if (i == 0) {
						System.out.print(Math.abs(8 - n));
					}
				}
				
				System.out.println();
				
				for (int i = 0; i < 8; i++) {
					System.out.printf("%s", "|_____|");
				}
				
				System.out.println();
			}
			
			System.out.printf("   %C\t  %C\t %C\t%C      %C      %C      %C      %C", 
					'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a');
	}
	
	
	
	//DESCRIPTION:		Checks if user input for piece selection is valid	
	//PRE-CONDITION:	Passed String must be valid String
	//POST-CONDITION:	Returns true if coordinate is in charInt form; else false
	public static boolean isPieceChoiceValid(Pieces[][] board, String userChoice, char color) {
		
		//Check if user String is 2 characters long
		if (userChoice.length() != 2) {
			return false;
		}
		
		//Int user to store corresponding horz file values
		int file;
		
		//Check if userChoice has valid char at index 0
			
		switch(Character.toLowerCase(userChoice.charAt(0))) {
		
		//Valid file choices for userChoice, index 0
		case 'a' : 
			file = 0;
			break;
		case 'b' : 
			file = 1;
			break;
		case 'c' : 
			file = 2;
			break;
		case 'd' : 
			file = 3;
			break;
		case 'e' : 
			file = 4;
			break;
		case 'f' : 
			file = 5;
			break;
		case 'g' : 
			file = 6;
			break;
		case 'h' : 
			file = 7;
			break;
		
		//If all other cases are missed, input is invalid; return false
		default: return false;
		}	//Finish char check
		
		//Int to convert userChoice rank into int, uses backwards board order
		int rank;
		
		//Same switch statement check for index 1, int component
		switch(userChoice.charAt(1)) {
		
		//Valid rank choices for userChoice, index 1
		case '1' : 
			rank = 7;
			break;
		case '2' : 
			rank = 6;
			break;
		case '3' : 
			rank = 5;
			break;
		case '4' : 
			rank = 4;
			break;
		case '5' : 
			rank = 3;
			break;
		case '6' : 
			rank = 2;
			break;
		case '7' : 
			rank = 1;
			break;
		case '8' : 
			rank = 0;
			break;
		
		//If all other cases fail, return false
		default: return false;
		} //End switch statement
		
		//Check if coordinate is null or is wrong color
		if (!(Pieces.isEmpty(board, file, rank)) && (board[file][rank].getColor() != color)) {
			return true;
		}
		
		//If coordinate is correct color and not null, return true
		else {
			return true;
		}
	}
	
	//DESCRIPTION:		Checks if two pieces have the same color
	//PRE-CONDITION:	Both objects passed must be initialized
	//POST-CONDITION:	Returns true if pieces have different colors; else false
	public static boolean isDifferentColor(Pieces piece1, Pieces piece2)
	{
		if (piece2 == null) {
			return true;
		}
		
		//False if both objects have the same color
		if(piece1.getColor() == piece2.getColor())
		{
			return false;
		}
		//Both objects have different colors; returns true
		else
		{
			return true;
		}
	}
	
	
	//DESCRIPTION:		Checks if space on board is empty/null
	//PRE-CONDITION:	Coordinates passed in must be in range of arrays
	//POST-CONDITION:	Returns true if coordinate is null; else false
	public static boolean isEmpty(Pieces[][] board, int horz, int vert)
	{
		//Check for out of bounds index entries
		if ((horz < 0) || (horz > 7)) {
			return false;
		}
		
		else if ((vert < 0) || (vert > 7)) {
			return false;
		}
		
		//Checks if space is a Squares object
		else if (board[horz][vert] instanceof Squares) {
			return true;
		}
		
		//If coordinates are in bounds
		else if (board[horz][vert] == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//DESCRIPTION:		Static function which determines if passed coordinate has opposite color of passed piece
	//PRE-CONDITITON:	Passed board must be fully initialized and only 'w' or 'b' can be passed
	//POST-CONDITION:	Returns true if passed coordinate is enemy piece or empty; else returns false
	public static boolean isEnemyPiece(Pieces[][] board, Pieces piece, int horz, int vert) {
		//If passed coordinate is not empty and has different color, enemy piece occupies coordinate; return true
		if (!(Pieces.isEmpty(board, horz, vert)) && Pieces.isDifferentColor(piece, board[horz][vert])) {
			return true;
		}
		//If square is empty or occupied by friendly piece, return false
		else {
			return false;
		}
	}
	
	//DESCRIPTION:		Static function marks passed coordinate as threatened by passed color
	//PRE-CONDITION:	Passed parameters must be valid
	//POST-CONDITION:	Passed coordinate is marked as threatened based on passed color
	public static void markThreat(Pieces[][] board, Pieces piece, int horz, int vert) {
		if (piece.validCoordinate(board, horz, vert)) {
			//Switch statement to separate color
			switch(piece.getColor()) {
			case 'w':
				//Mark square as threatened by white, break
				board[horz][vert].setIsThreatenedByWhite(true);
				break;
			case 'b':
				//Mark square as threatened by black, break
				board[horz][vert].setIsThreatenedByBlack(true);
				break;
			}
		}
	}
	
	//DESCRIPTION:		Static function moving pieces to their coordinate
	//PRE-CONDITION:	Passed piece object and coordinates must be valid
	//POST-CONDITION:	Piece is moved to passed coordinate; old coordinate is nullified
	public static void placePiece(Pieces[][] board, Pieces piece, int horz, int vert) {
		//If passed square has > 0 threats made against it, decrement threats for passed piece
		int threat = 0;
		if (board[horz][vert].getThreats() > 0) {
			//Dummy variable to store value for use at end of method
			threat = board[horz][vert].getThreats() - 1;
		}
		
		//Adjust coordinate on board in case coordinate has enemy piece	
		board[horz][vert] = null;
		//Change past square to square object
		board[piece.getHorz()][piece.getVert()] = new Squares();
		board[piece.getHorz()][piece.getVert()].setHorz(piece.getHorz());
		board[piece.getHorz()][piece.getVert()].setValue(piece.getValue());
		//Switch statement to create corresponding piece type
		switch (piece.getPiece()) {
		
		case 'P':
			board[horz][vert] = new Pawn(piece);
			break;
		case 'N':
			board[horz][vert] = new Knight(piece);
			break;
		case 'B':
			board[horz][vert] = new Bishop(piece);
			break;
		case 'R':
			board[horz][vert] = new Rook(piece);
			break;
		case 'Q' :
			board[horz][vert] = new Queen(piece);
			break;
		case 'K':
			board[horz][vert] = new King(piece);
			break;
		} //End switch statement
		//Adjust new coordinates for calling Piece
		board[horz][vert].setHorz(horz);
		board[horz][vert].setVert(vert);
		//Adjust threat count for moved piece
		board[horz][vert].setThreats(threat);
	}

	
	//DESCRIPTION:		Static function which parses player horz coordinate and returns 
	//					corresponding int value
	//PRE-CONDITION:	Passed value must be char a - h
	//POST-CONDITION:	Returns int based on passed char
	public static int parseHorz(char horz) {
		
		//Switch statement for each char
		switch(horz) {
		case 'a': 	return 0;
		case 'b': 	return 1;
		case 'c':	return 2;
		case 'd':	return 3;
		case 'e':	return 4;
		case 'f':	return 5;
		case 'g':	return 6;
		case 'h':	return 7;
		default:	return 0;
		}
	}
	
	//DESCRIPTION:		Static function for parsing vert coordinate of passed char	
	//PRE-CONDITION:	Passed char must be 1-7
	//POST-CONDITION:	Returns int value of swapped vert coordinate
	public static int parseVert(char vert) {
		
		switch(vert) {
		case '1' :	return 7;
		case '2' :	return 6;
		case '3' :	return 5;
		case '4' :	return 4;
		case '5' :	return 3;
		case '6' : 	return 2;
		case '7' :	return 1;
		case '8' : 	return 0;
		default  :	return 0;
		}
	}
	
}



