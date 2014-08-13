/**
 * This class is used for importing data to the NameIndex and CoordIndex, this is used instead of a GISRecord
 * since all the two classes require is an offset, coordinates, and the name+state string
 * @author Alazar
 *
 */
public class RawRecord {
	String nameState;
	long offset;
	Point gisPoint;

	/**
	 * Constructor: constructs the raw record object and stores the gisPoint for the CoordIndex
	 * 
	 * @param record
	 * @param offset
	 */
	public RawRecord(String record, long offset) {
		String [] args = null;
		this.offset = offset;
		if(record != null) {
			args = record.split("\\|");
		}
		nameState = args[Field.FEATURENAME.index()] + args[Field.STATE.index()];
		Longitude longitude = new Longitude(args[Field.PRIMARYLONGDMS.index()]);
		Latitude latitude = new Latitude(args[Field.PRIMARYLATDMS.index()]);
		gisPoint = new Point(longitude.getSeconds(), latitude.getSeconds(), offset);
	}
	
	/**
	 * Returns the offset of the record
	 * @return
	 */
	public long getOffset() {
		return offset;
	}
	
	/**
	 * Returns the name+state string 
	 * @return
	 */
	public String getNameAndState() {
		return nameState;
	}
	
	/**
	 * Returns a point containing the coordinats and the offset
	 * @return
	 */
	public Point getCoordinates() {
		return gisPoint;
	}
}