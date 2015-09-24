import java.util.List;
import java.util.Vector;

/**
 * Class definition for a hash table 
 * @author Alazar Hailemariam
 *
 */
public class HashTable {

	private HashItem [] table;
	private int [] sizeCollection = {1019, 2027, 4079, 8123, 16267, 32503, 65011, 130027, 260111, 520279, 1040387, 2080763, 4161539, 8323151, 16646323};
	private int curTableSize;
	private int itemCount;
	private int probeCount;

	/**
	 * Constructor: Sets current table size and item count to 0 and creates 
	 * the hashitem array with the first size
	 */
	public HashTable() {
		curTableSize = 0;
		itemCount = 0;
		table = new HashItem[1019];
		probeCount = 0;
	}

	/**
	 * Adds the item to the table it its not already there and resizes the table if
	 * necessary
	 * @param item
	 */
	public void add(HashItem item) {
		if(needsResize())
			resize();
		probeCount =  addItem(item);
	}

	/**
	 * Returns the number of probe counts needed by the last add operation,
	 * if the add function was not called, it returns 0
	 * @return
	 */
	public int getProbeCount() {
		return probeCount;
	}

	/**
	 * Helper function for adding items to hash table and return the number of probes needed
	 * to insert the item into the hash table, also used for resizing the table
	 * @param item
	 * @param table
	 * @return
	 */
	private int addItem(HashItem item) {
		int position = elfHash(item.getName()+item.getState());
		position = position % table.length;
		int probeCount = 0;
		//Case 1: position is unoccupied in hash table
		if(table[position] == null) {
			table[position] = item;
			itemCount++;
			return probeCount;
		}
		//Case 2: needs probing to insert
		else {
			int n = 1;
			while(true) {
				probeCount++;
				n = (n*n + n) / 2;
				position = (n + position) % table.length;
				if(position < 0)
					position *= -1;
				if(table[position] == null) {
					table[position] = item;
					itemCount++;
					return probeCount;
				}
				n++;
			}
		}
	}

	/**
	 * Returns a vector containing the items that match the name of the search item
	 * @param item
	 * @return
	 */
	public Vector<HashItem> find(String item) {
		Vector <HashItem> result;
		int position = elfHash(item);
		position = position % table.length;
		//Case 1: item is not in table
		if(table[position] == null)
			return null;
		//Case 2: item is found therefore get all matching position by probing
		else {
			result = new Vector<HashItem>();
			if(table[position].getNameAndState().equals(item))
				result.add(table[position]);
			int n = 1;
			while(true) {
				n = (n*n + n) / 2;
				position = (n + position) % table.length;
				if(position < 0)
					position *= -1;
				if(table[position] == null) 
					return result;
				if(table[position].getNameAndState().equals(item))
					result.add(table[position]);
				n++;
			}
		}
	}

	/**
	 * This function is used to print contents of the hash table
	 */
	public String toString() {
		String out = null;
		out = "Format of display is\nSlot number: data record\nCurrent table size is " + Integer.toString(sizeCollection[curTableSize]);
		out += "\nNumber of elements in table is " + Integer.toString(itemCount) + "\n";
		for(int x = 0; x < table.length; x++)
			if(table[x] != null)
				out += "Slot: " + Integer.toString(x) +  String.format("%-70s\t%s", "  \tName: " + table[x].getName() + ", "
						+ table[x].getState(), "Offset: " + Long.toString(table[x].getOffset()) + "\n");
		return out;
	}

	/**
	 * Checks if the table is 70% full
	 * @return
	 */
	private boolean needsResize() {
		return (itemCount >= (table.length * 7)/10);
	}

	/**
	 * Resizes the table to the next bigger size
	 */
	private void resize() {
		curTableSize++;
		itemCount = 0;
		HashItem [] temp = table;
		table = new HashItem[sizeCollection[curTableSize]];
		for(HashItem item : temp) {
			if(item != null)
				addItem(item);
		}
	}

	/**
	 * Function to calculate hash value
	 * @param toHash
	 * @return
	 */
	private static int elfHash(String toHash) {
		int hashValue = 0;
		for (int Pos = 0; Pos < toHash.length(); Pos++) { // use all elements
			hashValue = (hashValue << 4) + toHash.charAt(Pos); // shift/mix
			int hiBits = hashValue & 0xF0000000; // get high nybble
			if (hiBits != 0)
				hashValue ^= hiBits >> 24; // xor high nybble with second nybble
			hashValue &= ~hiBits; // clear high nybble
		}
		return hashValue;
	}	
}
