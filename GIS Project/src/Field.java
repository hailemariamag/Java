/**
 * This enum is used to specify index positions in GISRecords when they are split
 * into argument strings
 * @author Alazar
 *
 */
public enum Field {
	FEATUREID(0, "Feature ID"), FEATURENAME(1, "Feature Name"), FEATURECLASS(2, "Feature Cat"),
	STATE(3, "State"), COUNTYNAME(5, "County"), PRIMARYLATDMS(7, "Latitude"), PRIMARYLONGDMS(8, "Longitude"),
	SOURCELATDMS(11, "Src Lat"), SOURCELONGDMS(12, "Src Long"), ELEVATIONFEET(16, "Elev in ft"),
	MAPNAME(17, "USGS Quad"), DATECREATED(18, "Date created"), DATEEDITED(19, "Date mod");
	//STATENUMERIC(4, "State No"),COUNTYNUMERIC(6, "County No"), ELEVATIONMETERS(13, "Elevation in m"), SOURCELATDEC(11, "Source Dec Lat), SOURCELONGDEC(12),
	
	private int index;
	private String fieldName;
	
	/**
	 * Private Constructor for enum 
	 * @param value
	 */
	private Field(int index, String id) {
		this.index = index;
		fieldName = id;
	}
	
	/**
	 * Returns the index of the enum
	 * @return
	 */
	public int index() {
		return index;
	}
	
	/**
	 * Returns the fieldName of the enum
	 * @return
	 */
	public String fieldName() {
		return fieldName;
	}
};
