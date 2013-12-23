package gui;

import gui.enumeration.AddMapDialogMode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import components.Receiver;

/**
 * This class extends <code>JPanel</code> and contains <code>ReceiverButton</code>s.
 * 
 * @see ReceiverButton
 * @author Danilo
 */
public class ReceiversPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant PANEL_WIDTH. */
	private static final int PANEL_WIDTH = 950;

	/** The Constant PANEL_HEIGHT. */
	private static final int PANEL_HEIGHT = 40;

	private static final int LIGHTER_GRAY_COULOUR = 230;

	/** The receiver buttons. */
	private List<ReceiverButton> receiverButtons;

	/** The add map dialog. */
	private AddMapDialog addMapDialog;

	/** The all receivers. */
	private List<Receiver> allReceivers;

	/** The receivers on map. */
	private List<Receiver> receiversOnMap;

	/**
	 * Instantiates a new receivers panel.
	 * 
	 * @param addMapDialog
	 *            <code>AddMapDialog</code> parent component
	 * @param allReceivers
	 *            <code>List</code> of all <code>Receiver</code>s in the data source
	 * @param receiversOnMap
	 *            <code>List</code> of all <code>Receiver</code>s that are on the map
	 */
	public ReceiversPanel(AddMapDialog addMapDialog, List<Receiver> allReceivers, List<Receiver> receiversOnMap) {

		if (addMapDialog.getOpeningMode() == AddMapDialogMode.ADD) {
			this.receiversOnMap = new ArrayList<Receiver>();
		} else {
			this.receiversOnMap = receiversOnMap;
		}
		
		this.addMapDialog = addMapDialog;
		this.allReceivers = allReceivers;
		
		this.receiverButtons = new ArrayList<ReceiverButton>();
		initialize();
	}

	/**
	 * Initializes <code>ReceiversPanel</code>. It sets the appropriate <code>ReceiverButtonState</code> for every
	 * <code>ReceiverButton</code>, according to its presence on the map.
	 */
	private void initialize() {

		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(new Color(LIGHTER_GRAY_COULOUR, LIGHTER_GRAY_COULOUR, LIGHTER_GRAY_COULOUR));

		for (Receiver receiver : allReceivers) {
			if (isReceiverOnMap(receiver)) {
				receiver.setOnMap(true);
			}
		}

		for (Receiver receiver : allReceivers) {
			ReceiverButton button = new ReceiverButton(receiver, addMapDialog);
			receiverButtons.add(button);
			this.add(button);
		}
	}

	/**
	 * Checks if <code>Receiver</code> is on map. 
	 * 
	 * @param receiver
	 *            the receiver
	 * @return true, if successful
	 */
	private boolean isReceiverOnMap(Receiver receiver) {

		for (Receiver aReceiver : receiversOnMap) {
			if (aReceiver.getID() == receiver.getID()) {
				return true;
			}
		}
		return false;
	}

}
