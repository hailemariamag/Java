/**
 * Class to model hash table items
 * @author Alazar
 *
 */
public class HashItem {
	private String nameState;
	private long offset;
	
	/**
	 * Constructor: takes the name and offset and creates the object
	 * @param name
	 * @param offset
	 */
	HashItem(String name, long offset) {
		nameState = name;
		this.offset = offset;
	}
	
	/**
	 * Returns the name of the item
	 * @return
	 */
	public String getName() {
		return nameState.substring(0, nameState.length() - 2);
	}
	
	/**
	 * Returns the state of the item
	 * @return
	 */
	public String getState() {
		return nameState.substring(nameState.length() -2, nameState.length());
	}
	
	/**
	 * Returns both the name+state concatenated string
	 * @return
	 */
	public String getNameAndState() {
		return nameState;
	}
	
	/**
	 * Returns the offset of the item
	 * @return
	 */
	public long getOffset() {
		return offset;
	}
	
	
}