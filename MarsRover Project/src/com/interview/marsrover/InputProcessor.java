package com.interview.marsrover;

import java.util.List;

/**
 * This interface specifies the methods any type of input processor must implement,
 * the FlatFileInputProcessor implements this interface to read from an input file
 * @author Alazar
 *
 */
public interface InputProcessor {
	Navigable getMap();
	List<Command> getCommands();
}
