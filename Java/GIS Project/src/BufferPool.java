import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * This class is used as a buffer pool when of recently used GISRecord objects to minimize disk access
 * @author Alazar
 *
 */
public class BufferPool {
	
	private int maxPoolSize = 20;
	private LinkedList<GISRecord> bufferPool;
	private LinkedList<RandomAccessFile> fileList;

	/**
	 * Constructor: creates an empty BufferPool linked list and initializes the random access file
	 * @param fileName
	 */
	public BufferPool() {
		bufferPool = new LinkedList<GISRecord>();
	}
	
	/**
	 * This function is used to add other files to the file list
	 * @param fileName
	 */
	public void addFile(String fileName) {
		try {
			if(fileList == null)
				fileList = new LinkedList<RandomAccessFile>();
			fileList.add(new RandomAccessFile(fileName, "r"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This function find a GISRecord based on the offset, it first searches for it in the BufferPool,
	 * if its not found there it reads it from the input file and inserts it in the beginning of the 
	 * BufferPool
	 * @param offset
	 * @return
	 */
	public GISRecord findRecord(long offset) {
		GISRecord record;
		//Case 1: BufferPool is not empty therefore search for the GISRecord
		//		  and if found add move it to first in the LinkedList
		if(!bufferPool.isEmpty()) {
			for(GISRecord gisRecord : bufferPool) {
				if(gisRecord.getOffset() == offset) {
					bufferPool.remove(gisRecord);
					bufferPool.addFirst(gisRecord);
					return gisRecord;
				}
			}
		}
		//Case 2: BufferPool is either empty or does not contain the GISRecord therefore
		//		  get the record from file and put it in the first position of BufferPool
		String recordString = null;
		try {
			for(RandomAccessFile file : fileList) {
				file.seek(offset-2);
				String a = file.readLine();
				//Make sure offset points to a new line, can be middle of line if parsing incorrect database
				if(a != null && a.length() < 2){
					file.seek(offset);				
					recordString = file.readLine();
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		record = new GISRecord(recordString, offset);
		if(bufferPool.size() < maxPoolSize) {
			bufferPool.addFirst(record);
		}
		else {
			bufferPool.removeLast();
			bufferPool.addFirst(record);
		}
		return record;
	}
	
	/**
	 * This function is used to print the contents of a BufferPool object
	 */
	public String toString() {
		String out;
		//Case 1: BufferPool is empty
		if(bufferPool.size() == 0)
			out = "BufferPool does not contain any GIS-Records\n";
		//Case 2: BufferPool contains gisRecords therefore call the toString method on each
		//		  gisRecord
		else {
			out = "BufferPool contains the following GIS-Records\nMRU\n";
			for(GISRecord gisRecord : bufferPool)
				out += gisRecord.toString();
			out += "LRU\n";
		}
		return out;
	}
	
	/**
	 * This function closes the RandomAccessFile
	 */
	public void close() {
		try {
			for(RandomAccessFile file : fileList)
				file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
