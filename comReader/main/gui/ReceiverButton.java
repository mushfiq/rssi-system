package gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import utilities.Utilities;

import components.Receiver;

public class ReceiverButton extends JButton {

	/** The logger. */
	private Logger logger;
	private static final long serialVersionUID = 1L;
	private static Image addReceiverToMapImage;
	private static Image removeReceiverFromMapImage;
	private Receiver receiver;
	private ReceiverButtonState state;
	private ReceiverView receiverView;
	private static final String PLUS_IMAGE = "images/plus.png";
	private static final String MINUS_IMAGE = "images/minus.png";
	private AddMapDialog addMapDialog;

	public ReceiverButton(Receiver receiver, AddMapDialog parent) {

		this.receiver = receiver;
		this.addMapDialog = parent;
		initialize();
	}

	private void initialize() {
		logger = Utilities.initializeLogger(this.getClass().getName());
		ReceiverButtonState buttonState = (receiver.isOnMap()) ? ReceiverButtonState.REMOVE
				: ReceiverButtonState.ADD;
		addActionListener(new ReceiverButtonListener());
		addReceiverToMapImage 	   = Utilities.loadImage(PLUS_IMAGE);
		removeReceiverFromMapImage = Utilities.loadImage(MINUS_IMAGE);
		setState(buttonState);
	}

	public ReceiverButtonState getState() {
		return state;
	}

	public void setState(ReceiverButtonState state) {
		this.state = state;
		refreshIcon();
	}

	private void refreshIcon() {

		if (state == ReceiverButtonState.ADD) {
			
			this.setText(receiver.getID() + "");
			this.setIcon(new ImageIcon(addReceiverToMapImage));
		} else {
			
			this.setText(receiver.getID() + "");
			this.setIcon(new ImageIcon(removeReceiverFromMapImage));
		}
	}

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
