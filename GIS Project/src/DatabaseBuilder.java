import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class DatabaseBuilder {

	RandomAccessFile database;
	
	public DatabaseBuilder(String dbName) {
		try {
			database = new RandomAccessFile(dbName, "rw");
			database.setLength(0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeData(String data) {
		try {
			database.writeBytes(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			database.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
