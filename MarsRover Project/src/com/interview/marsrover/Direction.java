package com.interview.marsrover;

/**
 * Enum used to represent Direction
 * @author Alazar
 *
 */
public enum Direction {
	N, E, S, W;
	
	/**
	 * This function returns the direction Left to the current direction
	 * @return
	 */
	public Direction getLeft() {
		switch (this) {
		case N:
			return W;
		case E:
			return N;
		case W:
			return S;
		case S:
			return E;
		default:
			return null;
		}
	}

	/**
	 * This function returns the direction right to the current direction
	 * @return
	 */
	public Direction getRight() {
		switch (this) {
		case N:
			return E;
		case E:
			return S;
		case W:
			return N;
		case S:
			return W;
		default:
			return null;
		}
	}
}
