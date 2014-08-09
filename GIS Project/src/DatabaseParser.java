import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is used to parse the database file
 * @author Alazar
 *
 */
public class DatabaseParser {

	private RandomAccessFile database;
	private long offset;
	private String fileName;
	
	
	/**
	 * Constructor 
	 * @param db
	 */
	public DatabaseParser(String db) {
		try {
			database = new RandomAccessFile(db, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		offset = 0;
		fileName = db;
	}
	
	/**
	 * This function gets a line from the database and returns it, also saves the offset
	 * before reading the line
	 * @return
	 */
	public String getData() {
		String data = null;
		try {
			offset = database.getFilePointer();
			data = database.readLine();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * This function gets the current offset
	 * @return
	 */
	public long getOffset() {
		return offset;
	}
	
	/**
	 * This function is used the close the random access file
	 */
	public void close() {
		try {
			database.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This function returns the name of the database file
	 * @return
	 */
	public String getDBName() {
		return fileName;
	}
}
