/*
 * 
 * 
 */
package gui.enumeration;

/**
 * Enumeration for <code>PositionLocalizationAlgorithm</code>.
 * 
 * @author Danilo
 * @see algorithm.PositionLocalizationAlgorithm
 */
public enum PositionLocalizationAlgorithmType {

	/** This denotes probability based algorithm. */
	PROBABILITY_BASED("Probability based"),
	/** This denotes proximity algorithm. */
	PROXIMITY("Proximity");

	/** Name of algorithm, when needed to be shown as a string. */
	private String name;

	/**
	 * Instantiates a new position localization algorithm type.
	 * 
	 * @param name
	 *            <code>String</code> name of algorithm
	 */
	PositionLocalizationAlgorithmType(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
