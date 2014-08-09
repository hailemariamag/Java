/**
 * This enum is used to model all the valid commands expected from the input script file
 * @author Alazar
 *
 */
public enum CommandType {WORLD("world"), IMPORT("import"), WHAT_IS_AT("what_is_at"), 
	WHAT_IS("what_is"), WHAT_IS_IN("what_is_in"), DEBUG("debug"), QUIT("quit"), INVALID("invalid");

	private String command;
	
	/**
	 * Private constructor
	 * @param cmd
	 */
	private CommandType(String cmd) {
		command = cmd;
	}
	
	/**
	 * Returns the string version of the command
	 * @return
	 */
	public String getCMD() {
		return command;
	}
};
