package com.interview.marsrover;

import java.util.List;

/**
 * This class is used to model input commands indicating 
 * rover position, direction, and movement actions
 * @author Alazar
 *
 */
public class Command {
	Position startPosition;
	Direction startDirection;
	List<Movement> movements;
	
	/**
	 * Constructor
	 * @param startPosition
	 * @param startDirection
	 * @param movements
	 */
	public Command(Position startPosition, Direction startDirection, List<Movement> movements) {
		this.startPosition = startPosition;
		this.startDirection = startDirection;
		this.movements = movements;
	}
	
	/**
	 * Getters
	 * @return
	 */
	public Position getStartPosition() {
		return startPosition;
	}
	
	public Direction getStartDirection() {
		return startDirection;
	}
	
	public List<Movement> getMovements() {
		return movements;
	}
}
