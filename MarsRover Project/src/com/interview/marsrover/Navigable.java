package com.interview.marsrover;

/**
 * This interface defines a navigable to be used to navigable surfaces
 * @author Alazar
 *
 */
public interface Navigable {
	public boolean canMove(Position position, Direction direction);
}
