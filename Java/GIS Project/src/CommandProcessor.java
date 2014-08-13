import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * Class: CommandProcessor
 * Description: This class is used to process the commands from the commands.txt
 * @author Alazaqr Hailemariam
 */
public class CommandProcessor {
	/*
	private BufferedReader gisReader, comReader;
	private BufferedWriter outWriter;
	private int offset, byteCount, comCount;
	private String comString, gisString;
	private String gisFileName;
	 */

	private BufferPool bufferPool;
	private CoordIndex coordIndex;
	private NameIndex nameIndex;
	private DatabaseParser databaseParser;
	private DatabaseBuilder databaseBuilder;
	private GISRecord gisRecord;
	private int commandCount;
	private int probeCount;
	private String lineBreaker = "--------------------------------------------------------------------------------\n";
	private String out;
	private boolean noDBHeader;

	/**
	 * Function: CommandProcessor
	 * Description: This function is the constructor for the CommandProcessor class
	 * @param gisFile
	 * @param comFile
	 */
	public CommandProcessor(DatabaseBuilder dbBuilder, DatabaseParser dbParser, BufferPool bp, NameIndex ni, CoordIndex ci) {
		bufferPool = bp;
		nameIndex = ni;
		coordIndex = ci;
		databaseParser = dbParser;
		databaseBuilder = dbBuilder;
		commandCount = 0;
		noDBHeader = true;
		out = "";
	}

	/**
	 * This function processes takes a commmand as an input and directs the it to the
	 * appropriate command processing function
	 * @param command
	 */
	public void processCommand(Command command) {
		switch(command.getCommand()) {
		case WORLD:
			processWorld(command);
			break;
		case IMPORT:
			importData(command);
			break;
		case WHAT_IS_AT:
			whatIsAt(command);
			break;
		case WHAT_IS:
			whatIs(command);
			break;
		case WHAT_IS_IN:
			whatIsIn(command);
			break;
		case DEBUG:
			debug(command);
			break;
		case QUIT:
			quit(command);
			break;
		case INVALID:
			break;
		}
	}

	/**
	 * This function processes the world command
	 * @param command
	 */
	private void processWorld(Command command) {
		out = "\nWorld Boundaries are set to:\n";
		//Initialize the coordIndex with the world coordinates
		coordIndex.defineWorld(new Longitude(command.getArgs()[0]).getSeconds(), 
				new Longitude(command.getArgs()[1]).getSeconds(), 
				new Latitude(command.getArgs()[2]).getSeconds(), 
				new Latitude(command.getArgs()[3]).getSeconds());

		String [] bounds = coordIndex.getBoundaries();
		out += "\t\t" + bounds[3] + "\n\t" + bounds[0] +"\t\t" + bounds[1];
		out += "\n\t\t" + bounds[2] + "\n" + lineBreaker;
	}

	/**
	 * This function processes the import command
	 */
	private void importData(Command command) {
		commandCount++;
		String fileName = command.getArgs()[0];
		databaseParser = new DatabaseParser(fileName);
		out = ";\n;Import Data:\nCommand " + Integer.toString(commandCount) + ":\t";
		out += "import\t\t" + fileName + "\n\n" + "Imported Features by name:\t";
		int importCount = 0;
		String dbData = databaseParser.getData();
		//If first import write field names
		if(noDBHeader) {
			databaseBuilder.writeData(dbData+"\n");
			noDBHeader = false;
		}
		bufferPool.addFile(fileName);
		dbData = databaseParser.getData();
		RawRecord rawRecord;
		while(!(dbData == null))
			if(!dbData.isEmpty() && dbData.charAt(0) != ';') {
				rawRecord = new RawRecord(dbData, databaseParser.getOffset());
				//If record is in the map, then add it to the hash as well
				if(coordIndex.insert(rawRecord.getCoordinates())) {
					importCount++;
					databaseBuilder.writeData(dbData+"\n");
					HashItem item = new HashItem(rawRecord.getNameAndState(), rawRecord.getOffset());
					nameIndex.addItem(item);
					if(probeCount < nameIndex.getProbeCount())
						probeCount = nameIndex.getProbeCount();
				}
				dbData = databaseParser.getData();
			}
		databaseParser.close();
		out += Integer.toString(importCount) + "\nLongest probe sequence:\t";
		out += Integer.toString(probeCount) + "\nImported Locations:\t";
		out += Integer.toString(importCount) + "\n" + lineBreaker;
	}

	/**
	 * This function processes the what_is_at command
	 * @param command
	 */
	private void whatIsAt(Command command) {
		commandCount++;
		String longitude = null, latitude = null;
		latitude = command.getArgs()[0];
		longitude = command.getArgs()[1];
		out = ";\n;Location Search:\nCommand " + Integer.toString(commandCount) + 
				":\twhat_is_at\t\t" + latitude +" "+ longitude;
		//Create a point object from the command args and search inside the coordIndex
		Point point = new Point(new Longitude(longitude).getSeconds(),
				new Latitude(latitude).getSeconds());
		point = coordIndex.find(point);
		//Check if the point exists on the map
		if(point != null && !point.getOffset().isEmpty()) {
			out += "\n\n\tThe following features were found at\t" + longitude + "\t" + latitude + "\n";
			for(long offset : point.getOffset()) {
				gisRecord = bufferPool.findRecord(offset);
				String [] args = gisRecord.getArgs();
				out += Long.toString(gisRecord.getOffset()) + ":\t" + args[Field.FEATURENAME.index()];
				out += "\t" + args[Field.COUNTYNAME.index()] + ", " + args[Field.STATE.index()] + "\n";
			}
		}
		else
			out += "\n\n\t No features were found at " + longitude + " " + latitude + "\n";
		out += lineBreaker;
	}

	/**
	 * Check
	 * This function process the what_is command
	 * @param command
	 */
	private void whatIs(Command command) {
		commandCount++;
		String [] args = command.getArgs();
		Vector<HashItem> results = new Vector<HashItem>();
		results = nameIndex.findByName(args[0] + args[1]);
		out = ";\n;Name Search\nCommand " + Integer.toString(commandCount) + ":\twhat_is\t\t";
		out += args[0] + ", " + args[1];
		if(results == null || results.isEmpty()) 
			out += "\n\n\tNo features were found for " + args[0] + ", " + args[1];
		else {
			out += "\n\n\tThe following features were found for " + args[0] + ", " + args[1] + "\n";
			for(HashItem item : results) {
				gisRecord = bufferPool.findRecord(item.getOffset());
				args = gisRecord.getArgs();
				out += Long.toString(gisRecord.getOffset()) + ":\t" + args[Field.COUNTYNAME.index()];
				out += "\t" + args[Field.PRIMARYLONGDMS.index()] + "\t" + args[Field.PRIMARYLATDMS.index()] + "\n";
			}
		}
		out += lineBreaker;
	}

	/**Check
	 * This function processes the what_is_in command
	 * @param command
	 */
	private void whatIsIn(Command command) {
		commandCount++;
		String [] args = command.getArgs();
		out = ";\n;Region Search\nCommand " + Integer.toString(commandCount) + ":\twhat_is_in";
		if(command.getModifier() == Modifier.NONE)
			out += "\t\t" + args[1] + " " + args[0] + "\t\t";
		else
			out += "\t" + command.getModifier().getMOD() + "\t\t" + args[1] + " " + args[0] + "\t";
		out += args[3] + "\t" + args[2] + "\n\n";
		Vector<Point> result;
		result = coordIndex.findInRegion(new Longitude(args[1]).getSeconds(), new Latitude(args[0]).getSeconds(), 
				Long.parseLong(args[3]), Long.parseLong(args[2]));
		//Check the modifier in the command object
		switch(command.getModifier()) {
		//Case 1: If its a count modifier
		case COUNT:
			int counter = 0;
			for(Point p : result)
				counter += p.getOffset().size();
			out += Integer.toString(counter) + " features were found in (" + args[1];
			out += " +/- " + args[3] + ", " + args[0] + " +/- " + args[2] + ")\n";
			return;
		//Case 2: If its a list modifier
		case LIST:
			if(!result.isEmpty()) {
				int counter1 = 0;
				for(Point p : result) {
					for(long offset : p.getOffset()) {
						gisRecord = bufferPool.findRecord(offset);
						args = gisRecord.getArgs();
						counter1++;
						for(Field f : Field.values()) {
							if(f.index() < args.length && args[f.index()] != null && !args[f.index()].isEmpty())
								out += String.format("%-13s%s" , f.fieldName(), ": " + args[f.index()]  + "\n" );
						}
						out += "\n";
					}
				}
				out += "\tThe above " + counter1 + " features were found in (" + args[1];
				out += " +/- " + args[3] + ", " + args[0] + "+/- " + args[2] + ")\n";
			}
			else {
				out += "No features were found in (" + args[1]+" +/- " + args[3] + ", " + args[0] + " +/- " + args[2] + ")\n";
			}
			out += lineBreaker;
			return;
		//Case 3: If no modifier is present
		case NONE:
			if(!result.isEmpty()) {
				int counter2 = 0;
				for(Point p : result) {
					for(long offset : p.getOffset()) {
						counter2++;
						gisRecord = bufferPool.findRecord(offset);
						args = gisRecord.getArgs();
						out += Long.toString(gisRecord.getOffset()) + ":\t" + args[Field.FEATURENAME.index()];
						out += "\t" + args[Field.STATE.index()] + "\t" + args[Field.PRIMARYLATDMS.index()];
						out += "\t" + args[Field.PRIMARYLONGDMS.index()] + "\n";
					}
				}
				out += "\tThe above " + Integer.toString(counter2) + " features were found in (" + args[1];
				out += " +/- " + args[3] + ", " + args[0] + " +/- " + args[2] + ")\n";
			}
			else 
				out += "No features were found in (" + args[1]+" +/- " + args[3] + ", " + args[0] + "+/-" + args[2] + ")\n";
			out += lineBreaker;
			return;
		}		
	}

	/**Check
	 * This function is used to process the debug command
	 * @param command
	 */
	private void debug(Command command) {
		commandCount++;
		out = ";\n;Debugging\nCommand " + Integer.toString(commandCount) + ":\tdebug\t\t";
		switch(command.getModifier()) {
		case QUAD:
			out += command.getModifier().getMOD() +"\n\n";
			out += coordIndex.toString() + "\n" + lineBreaker;
			return;
		case HASH:
			out += command.getModifier().getMOD() +"\n\n";
			out += nameIndex.toString() + lineBreaker;
			return;
		case POOL:
			out += command.getModifier().getMOD() +"\n\n";
			out += bufferPool.toString() + lineBreaker;
			return;
		}
	}

	/**Check
	 * This function is used to exit from the command processor
	 * @param command
	 */
	private void quit(Command command) {
		commandCount++;
		out = ";\n;Exit\nCommand " + Integer.toString(commandCount) + ":\t" + command.getCommand().getCMD();
		out += "\n\nTerminating execution of commands.\n" + lineBreaker;
		//Close dbparser, bufferpool, and dbbuilder
		databaseParser.close();
		bufferPool.close();
		databaseBuilder.close();
	}


	/**
	 * This function is used to print the output of the last command processed
	 */
	public String toString() {
		return out;
	}


}
