/**
 * Author: Alazar Hailemariam
 * CS-3114 Project 3: PR Quadtree Generic
 * Last Modified: 3/8/12
 */
//    On my honor:
//   
//    - I have not discussed the Java language code in my program with
//      anyone other than my instructor or the teaching assistants
//      assigned to this course.
//   
//    - I have not used Java language code obtained from another student,
//      or any other unauthorized source, either modified or unmodified. 
//   
//    - If any Java language code or documentation used in my program
//      was obtained from another source, such as a text book or course
//      notes, that has been clearly noted with a proper citation in
//      the comments of my program.
//   
//    - I have not designed this program in such a way as to defeat or
//      interfere with the normal operation of the Curator System.
//
//    Alazar Hailemariam


// The test harness will belong to the following package; the quadtree 
// implementation must belong to it as well.  In addition, the quadtree 
// implementation must specify package access for the node types and tree 
// members so that the test harness may have access to it. 
//
//package Minor.P3.DS; 

import java.util.Vector;

public class prQuadtree< T extends Compare2D<? super T> > { 
	public abstract class prQuadNode {

	} 
	public class prQuadLeaf extends prQuadNode { 
		public prQuadLeaf(T elem) {
			if(Elements == null)
				Elements = new Vector<T>();
			Elements.add(elem);
		}
		Vector<T> Elements;
	} 
	public class prQuadInternal extends prQuadNode { 

		public prQuadInternal(){

		}
		prQuadNode NE, NW, SW, SE;
	} 

	prQuadNode root; 
	long xMin, xMax, yMin, yMax; 
	boolean operationFlag;
	private static final int bucketSize = 4;

	// Initialize quadtree to empty state, representing the specified region. 
	public prQuadtree(long xMin, long xMax, long yMin, long yMax) { 
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	} 

	// Pre:   elem != null 
	// Post:  If elem lies within the tree's region, and elem is not already  
	//        present in the tree, elem has been inserted into the tree. 
	// Return true iff elem is inserted into the tree.  
	public boolean insert(T elem) {
		//Case 1: Element is null
		if(elem == null)
			return false;
		//Case 2: Element is out of bounds of 
		else if(!elem.inBox(xMin, xMax, yMin, yMax))
			return false;
		//Case 3: Element is not null and within bounds
		else {
			root = insertElements(root, elem, xMin, xMax, yMin, yMax);
			return operationFlag; 
		}
	}

	// Pre:  elem != null 
	// Post: If elem lies in the tree's region, and a matching Elements occurs 
	//       in the tree, then that Elements has been removed. 
	// Returns true iff a matching Elements has been removed from the tree. 
	public boolean delete(T elem) { 
		//Case 1: Elements is null
		if(elem == null)
			return false;
		//Case 2: Elements is out of bounds of 
		else if(!elem.inBox(xMin, xMax, yMin, yMax))
			return false;
		//Case 3: Elements is in bounds
		operationFlag = false;
		root = deleteElements(root, elem, xMin, xMax, yMin, yMax);

		return operationFlag;

	} 
	// Pre:  elem != null 
	// Returns reference to an Elements x within the tree such that  
	// elem.equals(x)is true, provided such a matching Elements occurs within 
	// the tree; returns null otherwise. 
	public T find(T elem) { 
		//Case 1: Elements is null
		if(elem == null)
			return null;
		//Case 2: Elements is out of bounds
		else if(!elem.inBox(xMin, yMax, yMin, yMax))
			return null;
		return findElements(root, elem, xMin, xMax, yMin, yMax);
	} 

	// Pre: xLo, xHi, yLo and yHi define a rectangular region
	// Returns a collection of (references to) all Elementss x such that x is
	//in the tree and x lies at coordinates within the defined rectangular
	// region, including the boundary of the region.
	public Vector<T> find(long xLo, long xHi, long yLo, long yHi) {
		Vector<T> pool = new Vector<T>();
		findVector(root, pool, xLo, xHi, yLo, yHi, xMin, xMax, yMin, yMax);
		return pool;
	}

	/**
	 * Function: insertElements(...)
	 * Description: Helper function for insert(T elem)
	 * @param curQNode - Current node being examined for the element to be inserted in
	 * @param elem - The element to be inserted
	 * @param xMin - minimum x boundary
	 * @param xMax - maximum x boundary
	 * @param yMin - minimum y boundary
	 * @param yMax - maximum y boundar
	 * @return - returns prQuadTree with inserted element if it is within the boundary and does not already exist
	 *           in the tree or prQuadtree unchanged if the element already exists in the prQuadtree
	 */
	private prQuadNode insertElements(prQuadNode curQNode, T elem, double xMin, double xMax, double yMin, double yMax) {
		//Case 1: If qNode is null, create qNode and insert Elements
		if(curQNode == null) {
			curQNode = new prQuadLeaf(elem);
			operationFlag = true;
			return curQNode;
		}
		//Case 2: current qNode is a qInternal
		else if(curQNode.getClass().getName().equals("prQuadtree$prQuadInternal")) {			
			//Determine which direction the Elements should go, find midpoints, cast node to internal node
			Direction elemQuadrant = elem.inQuadrant(xMin, xMax, yMin, yMax);			
			double xMid = xMin + ((xMax - xMin)/2);
			double yMid = yMin + ((yMax - yMin)/2);	    	
			prQuadInternal internalNode = (prQuadInternal) curQNode;

			switch(elemQuadrant) {
			case NE:
				internalNode.NE = insertElements(internalNode.NE, elem, xMid, xMax, yMid, yMax);
				break;
			case NW:
				internalNode.NW = insertElements(internalNode.NW, elem, xMin, xMid, yMid, yMax);
				break;
			case SE:
				internalNode.SE = insertElements(internalNode.SE, elem, xMid, xMax, yMin, yMid);
				break;
			case SW:
				internalNode.SW = insertElements(internalNode.SW, elem, xMin, xMid, yMin, yMid);
				break;
			}
			return internalNode;
		}
		//Case 3: current qNode is a leaf
		else {
			double xMid = xMin + ((xMax - xMin)/2);
			double yMid = yMin + ((yMax - yMin)/2);
			prQuadLeaf leafNode = (prQuadLeaf) curQNode;
			//Item already exists at same location, therefore add offset
			for(T element : leafNode.Elements) {
				if(element.directionFrom(elem.getX(), elem.getY()) == Direction.NOQUADRANT) {
					//Add the new offset if it does not already exists in the element being examined
					operationFlag = element.addOffset(elem.getOffset().firstElement());
					return leafNode;
				}
			}
			//If bucket is already full
			if(leafNode.Elements.size() == bucketSize) {
				//Item already exists at same location, therefore add offset
				for(T element : leafNode.Elements) {
					if(element.directionFrom(elem.getX(), elem.getY()) == Direction.NOQUADRANT) {
						//Add the new offset if it does not already exists in the element being examined
						operationFlag = element.addOffset(elem.getOffset().firstElement());
						return leafNode;
					}
				}
				prQuadInternal internalNode = new prQuadInternal();
				//Move old leaf to new location
				//Direction leafQuadrant = leafNode.Elements.firstElement().inQuadrant(xMin, xMax, yMin, yMax);
				for(T element : leafNode.Elements) {
					Direction leafQuadrant = element.inQuadrant(xMin, xMax, yMin, yMax);
					switch(leafQuadrant) {
					case NE:
						internalNode.NE = insertElements(internalNode.NE, element, xMid, xMax, yMid, yMax);//leafNode;
						break;
					case NW:
						internalNode.NW = insertElements(internalNode.NW, element, xMin, xMid, yMid, yMax);//leafNode;
						break;
					case SE:
						internalNode.SE = insertElements(internalNode.SE, element, xMid, xMax, yMin, yMid);//leafNode;
						break;
					case SW:
						internalNode.SW = insertElements(internalNode.SW, element, xMin, xMid, yMin, yMid);//leafNode;
						break;
					}
				}
				//Insert new leaf
				Direction elemQuadrant = elem.inQuadrant(xMin, xMax, yMin, yMax);
				switch(elemQuadrant) {
				case NE:
					internalNode.NE = insertElements(internalNode.NE, elem, xMid, xMax, yMid, yMax);
					break;
				case NW:
					internalNode.NW = insertElements(internalNode.NW, elem, xMin, xMid, yMid, yMax);
					break;
				case SE:
					internalNode.SE = insertElements(internalNode.SE, elem, xMid, xMax, yMin, yMid);
					break;
				case SW:
					internalNode.SW = insertElements(internalNode.SW, elem, xMin, xMid, yMin, yMid);
					break;
				}
				return internalNode;	
			}
			//If bucket is not full, add element to bucket
			else {
				leafNode.Elements.add(elem);
				return leafNode;
			}
		}
	}


	/**
	 * Function: deleteElements(...)
	 * Description: Helper function for delete(T elem) function
	 * @param curQNode - Current node being searched
	 * @param elem - Element to be deleted if found
	 * @param xMin - minimum x boundary
	 * @param xMax - maximum x boundary
	 * @param yMin - minimum y boundary
	 * @param yMax - maximum y boundary
	 * @return - Returns prQuadtree with deleted element if the element is found or unchanged prQuadtree if it's not found
	 */
	private prQuadNode deleteElements(prQuadNode curQNode, T elem, double xMin, double xMax, double yMin, double yMax) {
		//Case 1:  If qNode is null, nothing to delete
		if(curQNode == null) 
			return curQNode;
		//Case 2: If qNode is a qInternal
		else if(curQNode.getClass().getName().equals("prQuadtree$prQuadInternal")) {
			Direction elemQuadrant = elem.inQuadrant(xMin, xMax, yMin, yMax);			
			double xMid = xMin + ((xMax - xMin)/2);
			double yMid = yMin + ((yMax - yMin)/2);	    	
			prQuadInternal internalNode = (prQuadInternal) curQNode;

			switch(elemQuadrant) {
			case NE:
				internalNode.NE = deleteElements(internalNode.NE, elem, xMid, xMax, yMid, yMax);
				break;
			case NW:
				internalNode.NW = deleteElements(internalNode.NW, elem, xMin, xMid, yMid, yMax);
				break;
			case SE:
				internalNode.SE = deleteElements(internalNode.SE, elem, xMid, xMax, yMin, yMid);
				break;
			case SW:
				internalNode.SW = deleteElements(internalNode.SW, elem, xMin, xMid, yMin, yMid);
				break;
			}
			return checkStructure(internalNode);		
		}
		//Case 3: If qNode is a leaf
		else {
			prQuadLeaf leafNode = (prQuadLeaf) curQNode;
			//Check if Elements is equal to the Elements in the leaf
			if(leafNode.Elements.firstElement().directionFrom(elem.getX(), elem.getY()) == Direction.NOQUADRANT) {
				curQNode = null;
				operationFlag = true;
				return curQNode;
			}
			return curQNode;
		}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
	}

	/**
	 * Function: findElements(...)
	 * Description: Helper function for find(T elem)
	 * @param curQNode current node being chcked
	 * @param elem - element being searched for
	 * @param xMin - minimum x boundary
	 * @param xMax - maximum x boundary
	 * @param yMin - minimum y boundary
	 * @param yMax - maximum y boundary
	 * @return - returns the element if found or null if it's not found then recursively returns entire prQuadtree
	 */
	private T findElements(prQuadNode curQNode, T elem, double xMin, double xMax, double yMin, double yMax) {
		//Case 1:  If qNode is null, qTree is empty or Elements not present
		if(curQNode == null) 
			return null;
		//Case 2: If qNode is a qInternal
		else if(curQNode.getClass().getName().equals("prQuadtree$prQuadInternal")) {
			Direction elemQuadrant = elem.inQuadrant(xMin, xMax, yMin, yMax);			
			double xMid = xMin + ((xMax - xMin)/2);
			double yMid = yMin + ((yMax - yMin)/2);	    	
			prQuadInternal internalNode = (prQuadInternal) curQNode;

			switch(elemQuadrant) {
			case NE:
				return findElements(internalNode.NE, elem, xMid, xMax, yMid, yMax);
			case NW:
				return findElements(internalNode.NW, elem, xMin, xMid, yMid, yMax);
			case SE:
				return findElements(internalNode.SE, elem, xMid, xMax, yMin, yMid);
			case SW:
				return findElements(internalNode.SW, elem, xMin, xMid, yMin, yMid);
			default:
				System.out.println("Function:\tprivate T findElements(...)\nProblem:\tinQuadrant(..) function returning NOQUADRANT value");
				return null;
			}	
		}
		//Case 3: If qNode is a leaf
		else {
			prQuadLeaf leafNode = (prQuadLeaf) curQNode;
			//Check if Elements is equal to the Elements in the leaf
			for(T element : leafNode.Elements)
				if(element.directionFrom(elem.getX(), elem.getY()) == Direction.NOQUADRANT)
					return element;
			return null;
		}                            

	}

	/**
	 * Function:	findVector(prQuadNode curQNode, Vector<T> elem,...)
	 * Description:	This function finds all leaves that lie within the bounds of a given region
	 * Arguments:	Coordinates of the region, a vector to store elements that exist within the region
	 * 				and prQuadNode used to traverse the quad tree
	 * Returns:		Returns the vector containing the elements by reference
	 */
	private void findVector(prQuadNode curQNode, Vector<T> elem, double xLo, double xHi, double yLo, double yHi, double xMin, double xMax, double yMin, double yMax) {
		//Case 1: curQNode is null
		if(curQNode == null)
			return;
		//Case 2: curQNode is a qInternal
		else if(curQNode.getClass().getName().equals("prQuadtree$prQuadInternal")) {
			prQuadInternal internalNode = (prQuadInternal) curQNode;
			double xMid = xMin + ((xMax - xMin)/2);
			double yMid = yMin + ((yMax - yMin)/2);
			//Check NE region for shared area
			if(sharedArea(xMid, xMax, yMid, yMax, xLo, xHi, yLo, yHi))
				findVector(internalNode.NE, elem, xLo, xHi, yLo, yHi, xMid, xMax, yMid, yMax);
			//Check NW region for shared area
			if(sharedArea(xMin, xMid, yMid, yMax, xLo, xHi, yLo, yHi)) 
				findVector(internalNode.NW, elem, xLo, xHi, yLo, yHi, xMin, xMid, yMid, yMax);
			//Check SE region for shared area
			if(sharedArea(xMid, xMax, yMin, yMid, xLo, xHi, yLo, yHi)) 
				findVector(internalNode.SE, elem, xLo, xHi, yLo, yHi, xMid, xMax, yMin, yMid);
			//Check SW region for shared area
			if(sharedArea(xMin, xMid, yMin, yMid, xLo, xHi, yLo, yHi)) 
				findVector(internalNode.SW, elem, xLo, xHi, yLo, yHi, xMin, xMid, yMin, yMid);	    	
		}
		//Case 3: curQNode is a leaf
		else {
			prQuadLeaf leafNode = (prQuadLeaf) curQNode;
			for(T element: leafNode.Elements)
				if(element.inBox(xLo, xHi, yLo, yHi))
					elem.add(element);
			return;
		}
	}

	/**
	 * Function:	checkStructure(prQuadInternal curQNode)
	 * Description:	This function checks parent of the node that was just deleted to ensure it has atleast two
	 * 				elements present, if not eliminates the prQuadInternal node and returns the single element
	 * Arguments:	the prQuadInternal node to be checked
	 * Returns:		prQuadNode of either internal or leaf type
	 */
	private prQuadNode checkStructure(prQuadInternal curQNode) {
		Direction temp = Direction.NOQUADRANT;
		int leafCount = 0;
		if(curQNode.NE != null) {
			if(curQNode.NE.getClass().getName().equals("prQuadtree$prQuadInternal"))
				return curQNode;
			leafCount++;
			temp = Direction.NE; 
		}
		if(curQNode.NW != null) {
			if(curQNode.NW.getClass().getName().equals("prQuadtree$prQuadInternal"))
				return curQNode;
			leafCount++;
			temp = Direction.NW;
		}
		if(curQNode.SE != null) {
			if(curQNode.SE.getClass().getName().equals("prQuadtree$prQuadInternal"))
				return curQNode;
			leafCount++;
			temp = Direction.SE;
		}
		if(curQNode.SW != null) {
			if(curQNode.SW.getClass().getName().equals("prQuadtree$prQuadInternal"))
				return curQNode;
			leafCount++;
			temp = Direction.SW;
		}

		if(leafCount > 1)
			return curQNode;
		switch(temp) {
		case NE:
			return curQNode.NE;
		case NW:
			return curQNode.NW;
		case SE:
			return curQNode.SE;
		case SW:
			return curQNode.SW;
		case NOQUADRANT:
			curQNode = null;
			return null;
		default:
			return curQNode;
		}
	}

	/**
	 * Function:	sharedArea(double mapxLo, double mapxHi, ...)
	 * Description:	This function takes two region coordinates and checks if they overlap
	 * Arguments:	two sets of 4 values indicating the rectangular coordinates of the two regions
	 * Returns:		True if they overlap including edge contact, false otherwise
	 */
	private boolean sharedArea(double mapxLo, double mapxHi, double mapyLo, double mapyHi, double squarexLo, double squarexHi, double squareyLo, double squareyHi) {
		return(mapxLo <= squarexHi && mapyLo <= squareyHi && mapxHi >= squarexLo && mapyHi >= squareyLo);
	}

	/**
	 * Function to print tree, taken from code provided in project 3
	 * @param Tree
	 * @param ptsPerDataItem
	 * @return
	 */
	public String toString() {
		String out = null;
		if ( root == null )
			out = ( "  Empty tree.\n" );
		else
			out = printTreeHelper(root, "");
		return out;
	}

	/**
	 * Helper function to print tree, taken from code provided in project 3
	 * @param Out
	 * @param sRoot
	 * @param Padding
	 * @return
	 */
	public String printTreeHelper(prQuadNode sRoot, String Padding) {

		String out = "";
		String pad = "---";
		// Check for empty leaf
		if ( sRoot == null ) {
			out += ( " " + Padding + "*\n");
			return out;
		}
		// Check for and process SW and SE subtrees
		if ( sRoot.getClass().getName().equals("prQuadtree$prQuadInternal") ) {
			prQuadInternal p = (prQuadInternal) sRoot;
			out += printTreeHelper(p.SW, Padding + pad);
			out += printTreeHelper(p.SE, Padding + pad);
		}

		// Determine if at leaf or internal and display accordingly
		if ( sRoot.getClass().getName().equals("prQuadtree$prQuadLeaf") ) {
			prQuadLeaf p = (prQuadLeaf) sRoot;
			out += Padding;
			for (int pos = 0; pos < p.Elements.size(); pos++) {
				out += p.Elements.get(pos).toString() + " ";
			}
			out += "\n";
		}
		else if ( sRoot.getClass().getName().equals("prQuadtree$prQuadInternal") )
			out +=( Padding + "@\n" );
		else
			out += ( sRoot.getClass().getName() + "#\n");

		// Check for and process NE and NW subtrees
		if ( sRoot.getClass().getName().equals("prQuadtree$prQuadInternal") ) {
			prQuadInternal p = (prQuadInternal) sRoot;
			out += printTreeHelper(p.NE, Padding + pad);
			out += printTreeHelper(p.NW, Padding + pad);
		}
		return out;
	}

}
