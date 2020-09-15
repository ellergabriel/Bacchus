
public class MoveList {
	
	protected double moveValue;
	protected int positionHorz;
	protected int positionVert;
	protected int moveHorz;
	protected int moveVert;
	protected MoveList next;
	
	
	/******** CONSTRUCORS ***************/
	//DESCRIPTION:		Creates a default MoveList object
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	Default MoveList object is created
	public MoveList() {
		this.next = null;
		moveValue = moveHorz = moveVert = positionHorz = positionVert = 0;
	}
	
	//DESCRIPTION:		Creates MoveList object with passed position coordinates 
	//PRE-CONDITION:	Passed values must be valid 0 - 7
	//POST-CONDITION:	New MoveList object is created with positional coordinate stored
	public MoveList(int positionHorz, int positionVert) {
		this.positionHorz = positionHorz;
		this.positionVert = positionVert;
		this.moveHorz = this.moveVert = 0;
		this.moveValue = 0;
	}
	
	//DESCRIPTION:		Creates MoveList object with positional and movement coordinates passed		
	//PRE-CONDITION:	All passed values must be 0 - 7
	//POST-CONDITION:	New MoveList object is instantiated with passed values
	public MoveList(int positionHorz, int positionVert, int moveHorz, int moveVert) {
		this.positionHorz = positionHorz;
		this.positionVert = positionVert;
		this.moveHorz = moveHorz;
		this.moveVert = moveVert;
	}
	
	//DESCRIPTION:		Creates MoveList object with only move coordinates
	//PRE-CONDITION:	Passed coordinates must be 0 - 7
	//POST-CONDITION:	New MoveList object is constructed with passed values set to moveHorz and moveVert
	public MoveList(int moveHorz, int moveVert, double moveValue) {
		this.moveHorz = moveHorz;
		this.moveVert = moveVert;
		this.moveValue = moveValue;
	}
	
	//CONDITION:		Creates MoveList object with all instance variables initialized
	//PRE-CONDITION:	All passed values must be valid ints and single double
	//POST-CONDITION:	New MoveList object is created with passed value
	public MoveList(int positionHorz, int positionVert, int moveHorz, int moveVert, double moveValue) {
		this.positionHorz = positionHorz;
		this.positionVert = positionVert;
		this.moveHorz = moveHorz;
		this.moveVert = moveVert;
		this.moveValue = moveValue;
	}
	
	//CONDITION:		Creates MoveList object with all instance variables initialized
		//PRE-CONDITION:	All passed values must be valid ints and single double
		//POST-CONDITION:	New MoveList object is created with passed value
		public MoveList(int positionHorz, int positionVert, int moveHorz, int moveVert, double moveValue, MoveList next) {
			this.positionHorz = positionHorz;
			this.positionVert = positionVert;
			this.moveHorz = moveHorz;
			this.moveVert = moveVert;
			this.moveValue = moveValue;
			this.next = next;
		}
	
	//DESCRIPTION:		Creates MoveList object with same values as passed MoveList object			
	//PRE-CONDITION:	Passed object must be fully instantiated
	//POST-CONDITION:	New MoveList is cloned using passed object
	public MoveList(MoveList original) {
		this.setAll(original.getMoveValue(), original.getPositionHorz(), original.getPositionVert(), 
					original.getMoveHorz(), original.getMoveVert(), original.getNext());
	}
	
	
	/********* ACCESSORS **********/
	//DESCRIPTION:		Returns moveValue instance variable of calling object
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	moveValue is returned
	public double getMoveValue() {
		return moveValue;
	}
	
	//DESCRIPTION:		Returns positionHorz instance variable of calling object
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	positionHorz is returned
	public int getPositionHorz() {
		return positionHorz;
	}
	
	//DESCRIPTION:		Returns positionVert instance variable of calling object		
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	positionVert is returned
	public int getPositionVert() {
		return positionVert;
	}
	
	//DESCRIPTION:		Returns moveHorz instance variable of calling object		
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	moveHorz is returned
	public int getMoveHorz() {
		return moveHorz;
	}
	
	//DESCRIPTION:		Returns moveVert instance variable of calling object		
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	moveVert is returned
	public int getMoveVert() {
		return moveVert;
	}
	
	//DESCRIPTION:		Returns next instance variable, containing memory address of next MoveList object in LinkedList
	//PRE-CONDITION:	next must have valid memory address stored
	//POST-CONDITION:	Memory address of next MoveList object is returned
	public MoveList getNext() {
		return this.next;
	}
	
	//DESCRIPTION:		Returns true if all values stored in calling object are 0; else, false
	//PRE-CONDITION:	Calling object must have all variables initialized
	//POST-CONDITION:	Returns true if all values are 0 (object is default); else returns false
	public boolean isDefault() {
		return ((this.positionHorz == 0) && (this.positionVert == 0) && (this.moveHorz == 0)
				&& (this.moveVert == 0) && (this.moveValue == 0));
	}
	
	/******* MUTATORS ***********/
	//DESCRIPTION:		Sets moveValue to passed int	
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	moveValue is set to passed int
	public void setMoveValue(double moveValue) {
		this.moveValue = moveValue;
	}
	
	
	//DESCRIPTION:		Sets positionHorz to passed int		
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	positionHorz is set to passed int
	public void setPositionHorz(int positionHorz) {
		this.positionHorz = positionHorz;
	}
	
	
	//DESCRIPTION:		Sets positionVert to passed int		
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	positionVert is set to passed int
	public void setPositionVert(int positionVert) {
		this.positionVert = positionVert;
	}
	
	
	//DESCRIPTION:		Sets moveHorz to passed int		
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	moveHorz is set to passed int
	public void setMoveHorz(int moveHorz) {
		this.moveHorz = moveHorz;
	}
	
	//DESCRIPTION:		Sets moveVert to passed int		
	//PRE-CONDITION:	Calling object must be instantiated
	//POST-CONDITION:	moveVert is set to passed int
	public void setMoveVert(int moveVert) {
		this.moveVert = moveVert;
	}
	
	//DESCRIPTION:		Sets next memory address to passed MoveList object
	//PRE-CONDITION:	Passed MoveList object must be instantiated
	//POST-CONDITION:	next memory address is set to passed MoveList object
	public void setNext(MoveList next) {
		this.next = next;
	}
	
	//DESCRIPTION:		Sets all instance variable of calling object		
	//PRE-CONDITION:	Calling object must be fully instantiated
	//POST-CONDITION:	All instance variables are set to passed int values
	public void setAll(double moveValue, int positionHorz, int positionVert, int moveHorz, int moveVert, MoveList next) {
		this.moveValue = moveValue;
		this.positionHorz = positionHorz;
		this.positionVert = positionVert;
		this.moveHorz = moveHorz;
		this.moveVert = moveVert;
		this.next = next;
	}
	
	
	
	
	/****** OTHER *********/
	//DESCRIPTION:		Overrides toString method in Object class	
	//PRE-CONDITION:	Calling object must be fully initialized
	//POST-CONDITION:	Object is printed to console
	public String toString() {
		return String.format("%d %d %.2f", this.moveHorz, this.moveVert, this.moveValue);
	}
}
	
	
	
	
	
	

