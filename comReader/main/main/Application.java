package main;

import java.io.File;

/**
 * The Class Application.
 */
public final class Application {

	/** The application. */
	private static Application application;
	
	/** The configuration file. */
	private File configurationFile;


	/**
	 * Instantiates a new application.
	 */
	private Application() {
		
		readConfigurationFile();
		initializeGUI();
	}
	
	/**
	 * Initialize gui.
	 */
	private void initializeGUI() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Read configuration file.
	 */
	private void readConfigurationFile() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets the application.
	 *
	 * @return the application
	 */
	public static Application getApplication() {
		
		if (application == null) {
			application = new Application();
		}
		
		return application;
	}
	
	
}
