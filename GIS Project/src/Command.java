/**
 * This class is used to integrate script commands, modifiers, and arguments into a single
 * class whose instance represents an object which contains all three fields of a command
 * @author Alazar
 *
 */
public class Command {

	private CommandType commandType;
	private Modifier modifier;
	private String [] args;
	
	/**
	 * Constructor to build the command
	 * @param ct
	 * @param mod
	 * @param args
	 */
	public Command(CommandType ct, Modifier mod, String []args) {
		commandType = ct;
		modifier = mod;
		this.args = args;
	}
	
	/**
	 * This function returns the current type of command
	 * @return
	 */
	public CommandType getCommand() {
		return commandType;
	}
	
	/**
	 * This function returns any modifiers present 
	 * @return
	 */
	public Modifier getModifier() {
		return modifier;
	}
	
	/**
	 * This function returns arguments of a command
	 * @return
	 */
	public String [] getArgs() {
		return args;
	}
}
