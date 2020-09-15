
public class Squares extends Pieces{
	
	//Booleans for checking if square is threatened/unsafe
	private boolean isThreatened;
	
	//Default constructor
	public Squares() {
		super();
		value = 0;
		piece = 'x';
		//color = 's';
		isThreatened = false;
	}
	
	//Full constructor
	public Squares(boolean isThreatened) {
		this();
		this.isThreatened = isThreatened;
	}

	//Copy constructor
	public Squares(Pieces original) {
		super(original);
		if (original instanceof Squares) {
			this.isThreatenedByWhite = original.getIsThreatenedByWhite();
			this.isThreatenedByBlack = original.getIsThreatenedByBlack();
			this.threats = original.getThreats();
		}
	}
	
	
	/******************** ACCESSORS ******************/
	
	//DESCRIPTION:		Returns boolean value of isThreatened
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	Boolean value is returned
	public boolean getIsThreatened() {
		return isThreatened;
	}
	
	
	/************** MUTATORS **************/
	
	//DESCRIPTION:		Sets boolean value of isThreatened
	//PRE-CONDITION:	Passed value must be a boolean
	//POST-CONDITION:	isThreatened boolean of calling object is set to passed value
	public void setIsThreatened(boolean isThreatened) {
		this.isThreatened = isThreatened;
	}
	
	
	//DESCRIPTION:		Overrides super class method; marks isThreatened as true instead
	//PRE-CONDITION:	Passed value must be boolean
	//POST-CONDITION:	isThreatened is set to true if false is passed into method
	@Override
	public void setIsHanging(boolean isHanging) {
		//If Square is not hanging, Square is threatened; If not hanging, square is not threatened
		this.isThreatened = !(isHanging);
	}
	
	/******** STATIC/HELPER METHODS **************/
	
	//DESCRIPTION:		Populates empty spaces on board with Squares object for information storage
	//PRE-CONDITION:	Board must be initialized
	//POST-CONDITION:	Empty board coordinates are populated with Squares object
	public static void fillSquares(Pieces[][] board) {
		
		//Nested for loop for each row and col
		for (int idxRow = 0; idxRow < 8; idxRow++) {
			for (int idxCol = 0; idxCol < 8; idxCol++) 
			{
				
				//Check if space is empty or has Squares object; replace objects if Square
				if (board[idxRow][idxCol] == null) {
					board[idxRow][idxCol] = new Squares();
					board[idxRow][idxCol].setHorz(idxRow);
					board[idxRow][idxCol].setVert(idxCol);
				}
				//Check if coordinate is already Squares object; clones object if true
				else if (board[idxRow][idxCol] instanceof Squares) {
					board[idxRow][idxCol] = new Squares(board[idxRow][idxCol]);
					board[idxRow][idxCol].setIsHanging(false);
				}
				//coordinate is occupied by valid piece; continue to next loop
				else {
					continue;
				}
			}
		}
	}
	
	
	
	//DESCRIPTION:		Prints information about square
	//PRE-CONDITION:	Square object must be initialized
	//POST-CONDITION:	Returns String about calling Squares object
	public String toString() {
		//Switch statement for coordinate translation
		int newVert;
		newVert = 8 - this.vert;
		
		switch(this.horz)
		{
			case 0:
				return String.format("%C%d", 'a', newVert);
			case 1:
				return String.format("%C%d", 'b', newVert);
			case 2:
				return String.format("%C%d", 'c', newVert);
			case 3:
				return String.format("%C%d", 'd', newVert);
			case 4:
				return String.format("%C%d", 'e', newVert);
			case 5:
				return String.format("%C%d", 'f', newVert);
			case 6:
				return String.format("%C%d", 'g', newVert);
			case 7:
				return String.format("%C%d", 'h', newVert);
		}
		
		return null;
	}
	
	
	
	
	
}
