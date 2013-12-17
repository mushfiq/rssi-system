/*
 * 
 * 
 */
package gui.enumeration;

import gui.ReceiverButton;

/**
 * Enumeration for <code>ReceiverButton</code>.
 * 
 * @see ReceiverButton
 * @author Danilo
 */
public enum ReceiverButtonState {

	/** <code>ADD</code> state. In this state, receiver is not on the map.*/
	ADD("Add state"),
	/** <code>REMOVE</code> state. In this state, receiver is on the map. */
	REMOVE("Remove state");

	/** Name of <code>ReceiverButtonState</code> as a <code>String</code>. */
	private final String text;

	/**
	 * Instantiates a new <code>ReceiverButtonState</code>.
	 * 
	 * @param name
	 *            <code>String</code> name
	 */
	ReceiverButtonState(String name) {
		this.text = name;
	}

	/**
	 * To string.
	 * 
	 * @param o
	 *            the o
	 * @return the string
	 */
	public String toString(Object... o) {
		return this.text;
	}
}
