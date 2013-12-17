/*
 * 
 * 
 */
package gui.enumeration;

/**
 * Enumeration for <code>StartStopButton</code> in <code>gui.ParametersPanel</code>.
 * 
 * @author Danilo
 * @see gui.ParametersPanel
 */
public enum StartStopButtonState {

	/** This denotes the state when button is clicked and readings and writings have started. */
	STARTED,
	/**
	 * This is the initial state. It denotes the state after the window is opened and readings and writings haven't yet
	 * started.
	 */
	STOPPED;
}
