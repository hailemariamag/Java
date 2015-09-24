package com.interview.marsrover;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is used convert input file into appropriate objects from the 
 * format given in the functional specification
 * @author Alazar
 *
 */
public class FlatFileInputProcessor implements InputProcessor {
	private Navigable map = null;
	private List<Command> commands;
	
	/**
	 * This method takes a URI of the file path and reads the map coordinates
	 * as well as gets rover start position and rover movement commands
	 * @param uri
	 */
	public FlatFileInputProcessor(URI uri) {
		commands = new ArrayList<Command>();
		try {
			List<String> fileLines = new ArrayList<String>();
			Command command;
			Position startPosition = null;
			Direction startDirection = null;
			Scanner s = new Scanner(Paths.get(uri).toFile());
			s.useDelimiter(System.getProperty("line.separator"));
			while (s.hasNext()){
			    fileLines.add(s.next());
			}
			s.close();
			initMap(fileLines.get(0));
			fileLines.remove(0);
			for (String line : fileLines) {
				String [] inputArgs = line.split(" ");
				if (inputArgs.length > 2) {
					int xPos = Integer.parseInt(inputArgs[0]);
					int yPos = Integer.parseInt(inputArgs[1]);
					startPosition = new Position(xPos, yPos);
					startDirection = Direction.valueOf(inputArgs[2]);
				} else {
					List<Movement> movements = new ArrayList<Movement>();
					inputArgs = line.split("");
					for (int index = 1; index < inputArgs.length; index++) {
						movements.add(Movement.valueOf(inputArgs[index]));
					}
					command = new Command(startPosition, startDirection, movements);
					commands.add(command);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Input file error.");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("Input file not formatted correctly, cannot get rover start position.");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Input file not formatted correctly, Invalid movement commands given.");
		}
	}
	
	/**
	 * This private method initializes the map after parsing dimensions from the given string
	 * @param dimensions
	 */
	private void initMap(String dimensions) {
		int xMax = Integer.parseInt(dimensions.split(" ")[0]);
		int yMax = Integer.parseInt(dimensions.split(" ")[1]);
		Position topRight = new Position(xMax, yMax);
		Position bottomLeft = new Position(0, 0);
		map = new RectangularPlateau(bottomLeft, topRight);
	}
	
	/**
	 * Get map
	 */
	public Navigable getMap() {
		return map;
	}
	
	/**
	 * Get commands
	 * @return
	 */
	public List<Command> getCommands() {
		return commands;
	}
}