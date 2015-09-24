/**
 * This class is used to store GISRecords
 * @author Alazar
 *
 */
public class GISRecord {
	private String gisLine;
	private String [] args;
	private long offset;
	private Longitude longitude;
	private Latitude latitude;
	private Point gisPoint;

	/**
	 * Contructor: Creates a gis record based on the input arguments
	 * @param record
	 * @param offset
	 */
	public GISRecord(String record, long offset) {
		this.offset = offset;
		if(record != null) {
			gisLine = record;
			args = gisLine.split("\\|");
		}
		longitude = new Longitude(args[Field.PRIMARYLONGDMS.index()]);
		latitude = new Latitude(args[Field.PRIMARYLATDMS.index()]);
		gisPoint = new Point(longitude.getSeconds(), latitude.getSeconds(), offset);
	}
	
	/**
	 * Getter function for offset of the GISRecord object
	 * @return
	 */
	public long getOffset() {
		return offset;
	}
	
	/**
	 * Returns a point object with set location 
	 * @return
	 */
	public Point getCoordinates() {
		return gisPoint;
	}
	
	/**
	 * Returns whether the GISRecord object exists within the world defined by the 
	 * dimensions of the arguments
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @return
	 */
	public boolean existsInWorld(long xMin, long xMax, long yMin, long yMax) {
		return gisPoint.inBox(xMin, xMax, yMin, yMax);
	}
	
	/**
	 * Getter function for GISRecord arguments
	 * @return
	 */
	public String[] getArgs() {
		return args;
	}
	
	/**
	 * This function is used to print the contents of a GISRecord object
	 */
	public String toString() {
		//String.format("%-13s%s" , f.fieldName(), ": " + args[f.index()]  + "\n" );
		String out = String.format("%-10s", Long.toString(offset) + ":");
		for(String arg : args) {
			out += arg + "|";
		}
		out += "\n";
		return out;
	}
}
