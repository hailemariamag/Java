package com.interview.marsrover;

import java.net.URI;
import java.net.URISyntaxException;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Please include full input file path");
		} else if (args.length > 1) {
			System.out.println("Too many arguments given");
		} else {	
			try {
				URI filePath = null;
				if (System.getProperty("os.name").toLowerCase().contains("windows")) {
					filePath = new URI("file:/" + args[0]);
				} else {
					filePath = new URI("file:///" + args[0]);
				}
				MarsRoverContainer mrc = new MarsRoverContainer(filePath);
				String roverPositions = mrc.execute();
				System.out.println(roverPositions);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				System.out.println("Cannot find file.");
			}
		}
	}
}
