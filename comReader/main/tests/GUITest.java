package tests;

import main.Application;


/**
 * Only used for testing the graphical user interface, without any reading and/or 
 * writing of the data.
 */
public class GUITest {

	
	/**
	 * Instantiates a new GUI test.
	 */
	private GUITest() {
		
	}
	
	public static void main(String[] args) {
		Application.getApplication();
		Application.getApplication().initializeGUI();
	}

}
