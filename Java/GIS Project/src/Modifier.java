/**
 * This enum is used to represent modifiers that can be present in a command
 * @author Alazar
 *
 */
public enum Modifier {LIST("-l"), COUNT("-c"),QUAD("quad"), HASH("hash"), POOL("pool"), NONE("none");

	private String modifier;
	
	/**
	 * Private constructor
	 * @param mod
	 */
	private Modifier(String mod) {
		modifier= mod;
	}
	
	/**
	 * returns the string value representing the enum
	 * @return
	 */
	public String getMOD() {
		return modifier;
	}
};
