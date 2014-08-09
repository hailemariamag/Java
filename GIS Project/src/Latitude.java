/**
 * Class to store latitude coordinates in seconds
 * @author Alazar
 *
 */
public class Latitude {
	
	private long seconds;
	
	/**
	 * Constructor: constructs the latitude object and stores the coordinate in seconds
	 * @param lat
	 */
	public Latitude(String lat) {
		if(lat != null && !lat.isEmpty()) {
			long latitude = Long.parseLong(lat.substring(0, lat.length() - 1));
			seconds = latitude % 100;
			latitude /= 100;
			seconds += (latitude % 100) * 60;
			latitude /= 100;
			seconds += (latitude % 100) * 3600;
			if(lat.substring(lat.length() - 1).equals("S"))
				seconds *= -1;
		}
	}
	
	/**
	 * This function returns the coordinates of the object in seconds
	 * @return
	 */
	public long getSeconds() {
		return seconds;
	}

}
