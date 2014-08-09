import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is used to read commands, modifiers, and args from the input file and return them as enumerated
 * type of Command, Modifier, or arrays containing data arguments
 * @author Alazar
 *
 */
public class CommandBuilder {

	private Command command;
	private RandomAccessFile inputFile;
	private String commandLine;
	boolean modPresent;

	/**
	 * Constructor gives the input file name of the command file and creates 
	 * a random access file reader object
	 * @param fileName
	 */
	public CommandBuilder(String script) {
		try {
			inputFile = new RandomAccessFile(script, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modPresent = false;
	}

	/**
	 * Reads the file and returns a Command object loaded with the appropriate command type,
	 * modifier, and arguments
	 * @return
	 */
	public Command getCommand() {
		commandLine = null;
		modPresent = false;
		while(commandLine == null || commandLine.isEmpty() || commandLine.charAt(0) == ';') {
			try {
				commandLine = inputFile.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String [] args = commandLine.split("\t");
		CommandType commandType = CommandType.INVALID;
		for(CommandType cmd  : CommandType.values()) {
			if(args[0].equals(cmd.getCMD()))
				commandType = cmd;
		}
		Modifier modifier = Modifier.NONE;
		if(commandType != CommandType.QUIT)
			for(Modifier mod : Modifier.values()) {
				if(args[1].equals(mod.getMOD())) {
					modPresent = true;
					modifier = mod;
				}
			}
		String [] properArgs;
		if(modPresent && args.length == 2) {
			command = new Command(commandType, modifier, null);
			return command;
		}
		if(modPresent) {
			properArgs = new String[args.length - 2];
			for(int x = 2; x < args.length; x++)
				properArgs[x-2] = args[x];
		}
		else {
			properArgs = new String[args.length - 1];
			for(int x = 1; x < args.length; x++)
				properArgs[x-1] = args[x];
		}

		command = new Command(commandType, modifier, properArgs);
		return command;
	}

	/**
	 * This function is used to close the random access file
	 */
	public void close() {
		try {
			inputFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
