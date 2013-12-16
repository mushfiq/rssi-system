/*
 * 
 * 
 */
package gui.observer;

/**
 * The Interface Observable. Part of observer pattern. <br>
 * <br>
 * 
 * Observables are objects whose behavior is being monitored by observers. Observers are notified when observables make
 * some change. Initially, <code>Observable</code>s add <code>Observer</code>s to a <code>List</code> of
 * <code>Observer</code>s. When a change occurs, <code>Observable</code> iterates over the <code>List</code> of
 * <code>Observer</code>s and calls their <code>update</code> method.
 * 
 * @author Danilo
 * @see Observer
 */
public interface Observable {

	/**
	 * Registers the observer.
	 * 
	 * @param observer
	 *            Object of a class that implements <code>Observer</code> interface.
	 * @see Observer
	 */
	void registerObserver(Observer observer);

	/**
	 * Unregisters an observer.
	 * 
	 * @param observer
	 *            <code>Observer</code> object to be unregistered.
	 * @see Observer
	 */
	void unregisterObserver(Observer observer);

	/**
	 * Notifies observers about a certain change.
	 * 
	 * @param observable
	 *            the observable
	 * @see Observer
	 */
	void notifyObservers(Observable observable);
}
