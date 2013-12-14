package gui.enumeration;

public enum PositionLocalizationAlgorithmType {

	PROBABILITY_BASED("Probability based"), PROXIMITY ("Proximity");
	
	
	private String name;
	
	PositionLocalizationAlgorithmType(String name) {
	    this.name = name;
	}
	
	@Override
	public String toString() {
	   
	    return name;
	}
}
