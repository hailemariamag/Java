import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * This class is the entry point to the GIS program, it takes the i/o file arguments
 * @author Alazar
 *
 */
public class GISController {

	private CommandProcessor commandProcessor;
	private CoordIndex coordIndex;
	private BufferPool bufferPool;
	private NameIndex nameIndex;
	private CommandBuilder commandBuilder;
	private DatabaseParser databaseParser;
	private DatabaseBuilder databaseBuilder;
	private BufferedWriter logWriter;
	private String db;
	private String script;
	private String log;

	/**
	 * Constructor: Constructs the GISController object and initializes its member objects
	 * @param scriptFile
	 * @param logFile
	 * @param dbFile
	 */
	public GISController(String dbFile, String scriptFile, String logFile) {
		commandBuilder = new CommandBuilder(scriptFile);
		//databaseBuilder = new DatabaseBuilder(dbFile);
		coordIndex = new CoordIndex();
		nameIndex = new NameIndex();
		bufferPool = new BufferPool();
		databaseBuilder = new DatabaseBuilder(dbFile);
		commandProcessor = new CommandProcessor(databaseBuilder, databaseParser, bufferPool, nameIndex, coordIndex);
		try {
			logWriter = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log = logFile;
		script = scriptFile;
		db = dbFile;
	}

	/**
	 * This function is used to run the GISSystem and execute commands
	 */
	public void run() {
		try {
			Date date = new Date();
			logWriter.write("GIS Program by Alazar Hailemariam\n\ndbFile:\t\t"+ db + "\nscript:t\t");
			logWriter.write(script + "\nlog:\t\t" + log + "\nStart time: " + date.toString() + "\n");
			logWriter.write("Quadtree children are printed in the order SW  SE  NE  NW\n");
			logWriter.write("--------------------------------------------------------------------------------\n");
			//Command object to load commands into from the commmandbuilder
			Command command;
			do {
				command = commandBuilder.getCommand();
				commandProcessor.processCommand(command);
				logWriter.write(commandProcessor.toString());
				System.out.print(commandProcessor);				
			} while(command.getCommand() != CommandType.QUIT);	//Keep running while the command is not quit
			date = new Date();
			logWriter.write("End Time: " + date.toString());
			logWriter.close();
			commandBuilder.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
