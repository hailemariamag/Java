package com.interview.marsrover;

/**
 * This Class is used to model a Rover
 * @author Alazar
 *
 */
public class Rover {
	private Position position;
	private Direction direction;
	
	/**
	 * Constructor
	 * @param position
	 * @param direction
	 */
	public Rover(Position position, Direction direction) {
		this.position = position;
		this.direction = direction;
	}
	
	/**
	 * Getters
	 */
	public Position getPosition() {
		return position;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Function to turn the rover left and update its facing direction
	 */
	public void turnLeft() {
		this.direction = this.direction.getLeft();
	}
	
	/**
	 * Function to turn the rover right and update its direction
	 */
	public void turnRight() {
		this.direction = this.direction.getRight();
	}
	
	/**
	 * This method advances the rover forward one unit forward in the direction
	 * it's currently facing and updates it's position
	 */
	public void moveForward() {
		switch(getDirection()) {
		case N:
			this.position.setLatitude(position.getLatitude() + 1);
			break;
		case E:
			this.position.setLongitude(position.getLongitude() + 1);
			break;
		case S:
			this.position.setLatitude(position.getLatitude() - 1);
			break;
		case W:
			this.position.setLongitude(position.getLongitude() - 1);
			break;
		}
	}
	
	/**
	 * This method takes a Movement object and calls the appropriate function to carry
	 * out the action
	 * @param movement
	 */
	public void moveOrTurn(Movement movement) {
		switch (movement) {
		case L:
			turnLeft();
			break;
		case R:
			turnRight();
			break;
		case M:
			moveForward();
			break;
		default:
			break;
		}
	}
	
	/**
	 * This method returns a string containing the latitude, longitude, and direction of rover
	 */
	public String toString() {
		return position.getLongitude() + " " + position.getLatitude() + " " + direction.toString();
	}
}
