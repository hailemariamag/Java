package com.interview.marsrover;

/**
 * This class is used to model a 2d rectangular surface and implements
 * the navigable interface
 * @author Alazar
 *
 */
public class RectangularPlateau implements Navigable {
	private Position topLeft;
	private Position topRight;
	private Position bottomLeft;
	private Position bottomRight;
	
	/**
	 * Constructor
	 * @param topLeft
	 * @param topRight
	 * @param bottomLeft
	 * @param bottomRight
	 */
	public RectangularPlateau(Position bottomLeft, Position topRight) {
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;
		this.topLeft = new Position(bottomLeft.getLongitude(), topRight.getLatitude());
		this.bottomRight = new Position(topRight.getLatitude(), bottomLeft.getLongitude());
	}
	
	/**
	 * This method returns true if the next cell in a specific direction from the current cell is navigable
	 * false otherwise, also checks whether another rover is present in intended position
	 */
	public boolean canMove(Position position, Direction direction) {
		Position goalPosition;
		switch (direction) {
		case N:
			if (topLeft.getLatitude() > position.getLatitude()) {
				goalPosition = new Position(position.getLongitude(), position.getLatitude() + 1);
				break;
			} else {
				return false;
			}
		case E:
			if (topRight.getLongitude() > position.getLongitude()) {
				goalPosition = new Position(position.getLongitude() + 1, position.getLatitude());
				break;
			} else {
				return false;
			}
		case S:
			if (bottomLeft.getLatitude() < position.getLatitude()) {
				goalPosition = new Position(position.getLongitude(), position.getLatitude() - 1);
				break;
			} else {
				return false;
			}
		case W:
			if (bottomLeft.getLongitude() < position.getLongitude()) {
				goalPosition = new Position(position.getLongitude() - 1, position.getLatitude());
				break;
			} else {
				return false;
			}
		default:
			return false;
		}
		return true;
	}
	
	/**
	 * This method checks whether a position lies on/in the boundaries
	 * defined by the rectangular plateau
	 * @param position
	 * @return
	 */
	public boolean withinBoundaries(Position position) {
		if (position.getLatitude() >= bottomLeft.getLatitude() && position.getLongitude() >= bottomLeft.getLongitude()) {
			if (position.getLatitude() <= topRight.getLatitude() && position.getLongitude() <= topRight.getLongitude()) {
				return true;
			}
		}
		return false;
	}
}
