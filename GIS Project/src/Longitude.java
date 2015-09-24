/**
 * This class is used to represent longitude values in seconds 
 * @author Alazar
 *
 */
public class Longitude {

	private long seconds;
	
	/**
	 * Constructor: Constructs the longitude object and stores the coordinate in seconds
	 * @param longt
	 */
	public Longitude(String longt) {
		if(longt != null || !longt.isEmpty()) {
			long longitude = Long.parseLong(longt.substring(0, longt.length() -1));
			seconds = longitude % 100;
			longitude /= 100;
			seconds += (longitude % 100) * 60;
			longitude /= 100;
			seconds += (longitude % 100) * 3600;
			if(longt.substring(longt.length() - 1).equals("W"))
				seconds *= -1;
		}
	}
	
	
	/**
	 * Returns the coordinate in seconds
	 * @return
	 */
	public long getSeconds() {
		return seconds;
	}
}
