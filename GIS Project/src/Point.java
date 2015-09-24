import java.util.Vector;


public class Point implements Compare2D<Point> {

	private long xcoord;
	private long ycoord;
	private Vector<Long> offsetBucket;

	public Point() {
		xcoord = 0;
		ycoord = 0;
	}
	
	// Constructor: returns a point object
	public Point(long x, long y) {
		xcoord = x;
		ycoord = y;
		offsetBucket = new Vector<Long>();
	}
	
	// Constructor: returns a point object
	public Point(long x, long y, long offset) {
		xcoord = x;
		ycoord = y;
		offsetBucket = new Vector<Long>();
		offsetBucket.add(offset);
	}
	
	// Returns the offset of the user data object
	public Vector<Long> getOffset() {
		return offsetBucket;
	}

	// Returns the x-coordinate field of the user data object. 
	public long getX() {
		return xcoord;
	}

	// Returns the y-coordinate field of the user data object. 
	public long getY() {
		return ycoord;
	}
	
	// Adds additional offsets to point 
	public boolean addOffset(long offset) {
		if(offsetBucket.contains(offset))
			return false;
		offsetBucket.add(offset);
		return true;
	}

	// Returns indicator of the direction to the user data object from the 
	// location (X, Y) specified by the parameters.
	// The indicators are defined in the enumeration Direction, and are used
	// as follows:
	//
	//    NE:  vector from (X, Y) to user data object has a direction in the 
	//         range [0, 90) degrees (relative to the positive horizontal axis
	//    NW:  same as above, but direction is in the range [90, 180) 
	//    SW:  same as above, but direction is in the range [180, 270)
	//    SE:  same as above, but direction is in the range [270, 360)  
	//    NOQUADRANT:  location of user object is equal to (X, Y)
	//
	public Direction directionFrom(long X, long Y) { 
		if(xcoord == X ) {
			if(ycoord == Y)
				return Direction.NOQUADRANT;
			else if(ycoord > Y)
				return Direction.NW;
			else
				return Direction.SE;
		}
		else if(xcoord < X) {
    		if(ycoord > Y)
    			return Direction.NW;
    		else
    			return Direction.SW; 
		}
		else {
			if(ycoord > Y)
				return Direction.NE;
			else
				return Direction.SE;
		}
	}

	// Returns indicator of which quadrant of the rectangle specified by the 
	// parameters that user data object lies in. 
	// The indicators are defined in the enumeration Direction, and are used 
	// as follows, relative to the center of the rectangle: 
	// 
	//    NE:  user data object lies in NE quadrant, including non-negative 
	//         x-axis, but not the positive y-axis       
	//    NW:  user data object lies in the NW quadrant, including the positive 
	//         y-axis, but not the negative x-axis 
	//    SW:  user data object lies in the SW quadrant, including the negative 
	//         x-axis, but not the negative y-axis 
	//    SE:  user data object lies in the SE quadrant, including the negative 
	//         y-axis, but not the positive x-axis 
	//    NOQUADRANT:  user data object lies outside the specified rectangle 
	// 	
	public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi) { 
		if(xcoord > xHi || xcoord < xLo || ycoord > yHi || ycoord < yLo)
			return Direction.NOQUADRANT;
		
		//Find Midpoints of Quadrant
		double xMidpoint = xLo + ((xHi - xLo)/2);
    	double yMidpoint = yLo + ((yHi - yLo)/2);
    	
    	//If element is on the Y axis
    	if(xcoord == xMidpoint) {
    		if(ycoord == yMidpoint)
    			return Direction.NE;
    		if(ycoord > yMidpoint)
    			return Direction.NW;
    		else
    			return Direction.SE;
    	}
    	//If element is to the left of the Y axis
    	else if(xcoord < xMidpoint) {
    		if(ycoord > yMidpoint)
    			return Direction.NW;
    		else
    			return Direction.SW;    		
    	}
    	//If element is to the right of the Y axis
    	else {
    		if(ycoord < yMidpoint)
    			return Direction.SE;
    		return Direction.NE;
    	}
	}

	/**
	 * Function: 	inBox(double xLo, double xHi, double yLo, double yHi)
	 * Description: This function checks if an element is present within the given
	 * 				box bounded by the arguments
	 * Arguments: 	Coordinates of the region to search
	 * Returns:		Boolean indicating whether the element is found within the quadrant
	 */
	public boolean inBox(double xLo, double xHi, double yLo, double yHi) {
		return (xcoord >= xLo && xcoord <= xHi && ycoord >= yLo && ycoord <= yHi);
	}

	public String toString() {
		String out;
		out = "[(" + xcoord + ", " + ycoord + ")";
		for(long offset : offsetBucket)
			out += ", " + Long.toString(offset);
		out += "]";
		return out;
	}

	public boolean equals(Object o) { 
		//Case 1: o is null
		if(o == null)
			return false;
		//Case 2: o and this are not the same types of objects
		if(!this.getClass().getName().equals(o.getClass().getName()))
			return false;
		//Case 3: Check for coordinate equality between this and o after casting o into a point
		Point point = (Point) o;
		return (point.getX() == this.getX() && point.getY() == this.getY());
	}
}
