/**
 * Main class used to run the GIS System
 * @author Alazar
 *
 */
public class GIS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 3) {
			GISController gisc = new GISController(args[0], args[1], args[2]);
			gisc.run();
		}
		else
			System.out.println("Cannot start, incorrect arguments.");
			

	}

}
