package gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import utilities.Utilities;
import components.Receiver;

/**
 * The Class ReceiverButton.
 */
public class ReceiverButton extends JButton {

	/** The logger. */
	@SuppressWarnings("unused") // may be needed in the future
	private Logger logger;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The add receiver to map image. */
	private static Image addReceiverToMapImage;
	
	/** The remove receiver from map image. */
	private static Image removeReceiverFromMapImage;
	
	/** The receiver. */
	private Receiver receiver;
	
	/** The state. */
	private ReceiverButtonState state;
	
	/** The Constant PLUS_IMAGE. */
	private static final String PLUS_IMAGE = "images/plus.png";
	
	/** The Constant MINUS_IMAGE. */
	private static final String MINUS_IMAGE = "images/minus.png";
	
	/** The add map dialog. */
	private AddMapDialog addMapDialog;
	
	/**
	 * Instantiates a new receiver button.
	 *
	 * @param receiver Receiver model
	 * @param parent AddMapDialog instance
	 */
	public ReceiverButton(Receiver receiver, AddMapDialog parent) {

		this.receiver = receiver;
		this.addMapDialog = parent;
		initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		logger = Utilities.initializeLogger(this.getClass().getName());
		ReceiverButtonState buttonState = (receiver.isOnMap()) ? ReceiverButtonState.REMOVE
				: ReceiverButtonState.ADD;
		addActionListener(new ReceiverButtonListener());
		addReceiverToMapImage 	   = Utilities.loadImage(PLUS_IMAGE);
		removeReceiverFromMapImage = Utilities.loadImage(MINUS_IMAGE);
		setState(buttonState);
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public ReceiverButtonState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(ReceiverButtonState state) {
		this.state = state;
		refreshIcon();
	}

	/**
	 * Refresh icon.
	 */
	private void refreshIcon() {

		if (state == ReceiverButtonState.ADD) {
			
			this.setText(receiver.getID() + "");
			this.setIcon(new ImageIcon(addReceiverToMapImage));
		} else {
			
			this.setText(receiver.getID() + "");
			this.setIcon(new ImageIcon(removeReceiverFromMapImage));
		}
	}

	/**
	 * Toggles between the two states. It can be in state 'ADD' or state 'REMOVE'.
	 * In state 'ADD', receiver is not on the map and button has '+' sign on it.
	 * In state 'REMOVE', receiver is on the map and button has '-' sign on it.
	 */
	private void toggle() {
		
		if (this.state == ReceiverButtonState.ADD) {
			
			if (this.addMapDialog.getMapPreviewPanel().getBackgroundImage() == null) {
				return;
			}
			addMapDialog.addReceiverToMap(receiver);
			
		} else { // it is ReceiverButtonState.REMOVE
			
			addMapDialog.removeReceiverFromMap(receiver);
		}
		
		ReceiverButtonState newState = (this.state == ReceiverButtonState.ADD) ? ReceiverButtonState.REMOVE
				: ReceiverButtonState.ADD;
		this.setState(newState);
		refreshIcon();
		
	}
	
	/**
	 * The listener interface for receiving receiverButton events.
	 * The class that is interested in processing a receiverButton
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addReceiverButtonListener</code> method. When
	 * the receiverButton event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ReceiverButtonEvent
	 */
	private class ReceiverButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (!(e.getSource() instanceof ReceiverButton)) { // not a button
				return;
			}
			ReceiverButton button = (ReceiverButton) e.getSource();
			button.toggle();
		}
	}

}
