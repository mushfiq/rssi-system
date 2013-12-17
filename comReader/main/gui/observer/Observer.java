/*
 * 
 * 
 */
package gui.observer;

/**
 * Interface Observer. Part of observer pattern. <br><br>
 * 
 * <code>Observer</code>s monitor the behavior of <code>Observable</code>s. <code>Observer</code>s must implement only one method, <code>update</code>,
 * that is called by <code>Observerable</code> when a change occurs.
 * 
 * @author Danilo
 * @see Observable
 */
public interface Observer {

	/**
	 * Invoked by <code>Obserbable</code> when a change occurs.
	 *
	 * @param observable <code>Observable</code> object
	 * @see Observable
	 */
	abstract void update(Observable observable);
}
