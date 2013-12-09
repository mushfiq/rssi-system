package data;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadingFilter.
 */
public class ReadingFilter {

	/** The data. */
	private Reading data;
	
	/**
	 * Instantiates a new reading filter.
	 */
	public ReadingFilter() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Instantiates a new reading filter.
	 *
	 * @param data the data
	 */
	public ReadingFilter(Reading data) {
		super();
		if (data == null) {
			this.data = new Reading();
		} else {
			this.data = data;
		}
		
	}
	
	/**
	 * Removes the inappropriate values.
	 */
	public void removeInappropriateValues() {
		
	}
	
	/**
	 * Calculate average.
	 *
	 * @return the int
	 */
	public int calculateAverage() {
		
		return 0;
	}
	
	public Reading getData() {
		return data;
	}

	public void setData(Reading data) {
		this.data = data;
	}

	
}
