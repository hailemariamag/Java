package com.interview.marsrover;

import java.net.URI;
import java.util.List;

/**
 * This class models the entire mars rover navigation process
 * @author Alazar
 *
 */
public class MarsRoverContainer {
	private Rover rover;
	private RectangularPlateau rp;
	InputProcessor ip;
	List<Command> commands;
	
	/**
	 * Constructor
	 * @param fileName
	 */
	public MarsRoverContainer(URI fileName) {
		ip = new FlatFileInputProcessor(fileName);
		rp = (RectangularPlateau) ip.getMap();
		commands = ip.getCommands();
	}
	
	/**
	 * This method starts the navigation
	 *@return returns a string containing final rover position and direction
	 *for each rover on the rectangular map
	 */
	public String execute() {
		String roverPositions  = "";
		for (Command command : commands) {
			rover = new Rover(command.getStartPosition(), command.getStartDirection());
			for (Movement movement : command.getMovements()) {
				switch (movement) {
				case L:
					rover.turnLeft();
					break;
				case R:
					rover.turnRight();
					break;
				case M:
					if (rp.canMove(rover.getPosition(), rover.getDirection())) {
						rover.moveForward();
					}
				}
			}
			roverPositions = roverPositions + rover.getPosition().getLongitude()
							+ " " + rover.getPosition().getLatitude()
							+ " " + rover.getDirection().toString() + "\n";
		}
		return roverPositions;
	}
}
