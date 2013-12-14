package gui;

public enum ReceiverButtonState {

	ADD("Add state"), REMOVE("Remove state");
	
	private final String text;

    ReceiverButtonState(String text) {
        this.text = text;
    }

    public String toString(Object... o) {
        return this.text;
    }
}
