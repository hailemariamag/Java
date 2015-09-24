import java.util.Vector;

/**
 * This class is a wrapper class for the HashTable class
 * @author Alazar
 *
 */
public class NameIndex {

	private HashTable hashTable;
	
	/**
	 * Constructor
	 */
	public NameIndex() {
		hashTable = new HashTable();
	}
	
	/**
	 * Adds the hashItem to the hash table and returns the number of 
	 * probes needed
	 * @param item
	 * @return
	 */
	public void addItem(HashItem item) {
		hashTable.add(item);
	}
	
	/**
	 * Returns a collection of hash items whose name match the argument string
	 * @param name
	 * @return
	 */
	public Vector<HashItem> findByName(String name) {
		return hashTable.find(name);
	}
	
	/**
	 * This function returns the probe count of the last addItem call or 0 if addItem was
	 * never called
	 * @return
	 */
	public int getProbeCount() {
		return hashTable.getProbeCount();
	}
	
	/**
	 * This function is used to print the hash table
	 */
	public String toString() {
		return hashTable.toString();
	}
}
