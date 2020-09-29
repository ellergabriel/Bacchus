package Bacchus;

public class LinkedList {
	private MoveList head;
	private MoveList tail;
	
	//DESCRIPTION:		Constructs default LinkedList object using MoveList object nodes
	//PRE-CONDITION:	N/A
	//POST-CONDITION:	LinkedList is constructed
	public LinkedList() {
		head = null;
		tail = null;
	}
	
	//DESCRIPTION:		Constructs LinkedList using passed MoveList node as head
	//PRE-CONDITION:	Passed node must be instantiated MoveList object
	//POST-CONDITION:	LinkedList is created with passed node as head
	public LinkedList(MoveList head) {
		this.head = head;
		this.tail = head;
	}
	
	/******** ACCESSORS *********/
	//DESCRIPTION:		Returns next object in LinkedList
	//PRE-CONDITION:	LinkedList must be instantiated
	//POST-CONDITION:	Returns memory address of next node in list
	public double getTotalValue() {
		double value = 0;
		//Dummy node to keep track os list
		MoveList dummy = this.head;
		//Try catch block to continue grabbing moveValues from each node
			for (;;) {
			try {
				//Begin with head node
				value += dummy.getMoveValue();
				//Move dummy to next node stored in current node
				dummy = dummy.getNext();
				//Next node will be enemy move; subtract value of move from total value
				value -= dummy.getMoveValue();
				//Move dummy to next node for next iteration
				dummy = dummy.getNext();
			} catch (NullPointerException n) {
				//If null pointer is caught, break loop
				break;
			}
		}
		return value;
	}
	
	/******* MUTATORS *********/
	//DESCRIPTION:		Adds new MoveList object to LinkedList
	//PRE-CONDITION:	LinkedList must be instantiated with valid head
	//POST-CONDITION:	New node is added, with head and tail adjusted as needed
	public void addNode(MoveList node) {
		//Check for empty list; if empty, head and tail points to passed node
		if (this.head == null) {
			this.head = node;
			this.tail = head;
		}
		//Check for single item list; change tail to passed node
		else if ((this.head != null) && (this.tail == null)) {
			this.head.setNext(node);
			this.tail = node;
		}
		//If list has two items, append passed node to tail and set passed node as the tail
		else {
			//Connect tail to passed node
			this.tail.setNext(node);
			//Set passed node to tail
			this.tail = node;
		}
	}
}
	