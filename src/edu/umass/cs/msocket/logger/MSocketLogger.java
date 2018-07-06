package edu.umass.cs.msocket.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MSocketLogger 
{
	private static Logger LOGGER = null;
	static
	{
		LOGGER = Logger.getLogger(
				MSocketLogger.class.getName());
		ConsoleHandler ch = new ConsoleHandler();
		// Warnings+ log level messages should be written.
        ch.setLevel(Level.WARNING);
        LOGGER.addHandler(ch);
        LOGGER.setLevel(Level.WARNING);

        // TODO: Not sure what this is doing here. May be removing some default logger or something.
        Logger l0 = Logger.getLogger("");
        l0.removeHandler(l0.getHandlers()[0]);
	}
	
	public static Logger getLogger()
	{
		return LOGGER;
			
		// set the LogLevel to Severe, only severe Messages will be written
		/*LOGGER.setLevel(Level.SEVERE);
		LOGGER.severe("Info Log");
		LOGGER.warning("Info Log");
		LOGGER.info("Info Log");
		LOGGER.finest("Really not important");
		
		// set the LogLevel to Info, severe, warning and info will be written
		// finest is still not written
		LOGGER.setLevel(Level.INFO);
		LOGGER.severe("Info Log");
		LOGGER.warning("Info Log");
		LOGGER.info("Info Log");
		LOGGER.finest("Really not important");*/
	}
}