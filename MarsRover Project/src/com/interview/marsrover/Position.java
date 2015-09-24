package com.interview.marsrover;

/**
 * Class used to represent longitude latitude position on a 2d surface
 * @author Alazar
 *
 */
public class Position {
	private int longitude;
	private int latitude;
	
	/**
	 * Constructor
	 * @param longitude
	 * @param latitude
	 */
	public Position(int longitude, int latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	/**
	 * Getters and Setters
	 * @return
	 */
	public int getLongitude() {
		return longitude;
	}
	
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	
	public int getLatitude() {
		return latitude;
	}
	
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Check if two positions are equal by 
	 * @param position
	 * @return
	 */
	public boolean equals(Position position) {
		if(getLongitude() == position.getLongitude() && getLatitude() == position.getLatitude())
			return true;
		return false;
	}
}
