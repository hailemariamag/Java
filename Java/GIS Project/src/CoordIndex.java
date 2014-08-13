import java.util.Vector;

/**
 * This class is used to make the coordinate index and wraps around the prQuadtree class
 * @author Alazar
 *
 */
public class CoordIndex {

	private prQuadtree<Point> map;
	private String [] boundaries;
	
	/**
	 * Constructor does nothing
	 */
	public CoordIndex() {
		boundaries = null;
		map = null;
	}
	
	/**
	 * Function used to initialize prQuadtree with the given coordinates
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 */
	public void defineWorld(long xMin, long xMax, long yMin, long yMax) {
		map = new prQuadtree<Point>(xMin, xMax, yMin, yMax);
		boundaries = new String [] {Long.toString(xMin),Long.toString(xMax), Long.toString(yMin), Long.toString(yMax)};
	}
	
	/**
	 * Function used to find a point in the map and return it
	 * @param point
	 * @return
	 */
	public Point find(Point point) {
		return map.find(point);
	}
	
	/**
	 * Function used to insert a point in the map and return a boolean
	 * indicating success
	 * @param point
	 * @return
	 */
	public boolean insert(Point point) {
		return map.insert(point);
	}
	
	/**
	 * Function used to find the collection of points in a region determined by the 
	 * arguments
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @return
	 */
	public Vector<Point> findInRegion(long midX, long midY, long halfX, long halfY) {
		return map.find(midX-halfX, midX + halfX, midY - halfY, midY + halfY);
	}
	
	/**
	 * This function returns the boundaries of the coordIndex in an array
	 * @return
	 */
	public String [] getBoundaries() {
		return boundaries;
	}
	
	/**
	 * Function used to print contents of prQuadtree
	 */
	public String toString() {
		return map.toString();
	}
}
