package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import components.Receiver;

public class ReceiversPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int PANEL_WIDTH = 950;
	private static final int PANEL_HEIGHT = 40;
	private List<ReceiverButton> receiverButtons;
	private AddMapDialog addMapDialog;
	private List<Receiver> receivers;
	
	public ReceiversPanel(AddMapDialog addMapDialog, List<Receiver> allReceivers) {
		
		this.addMapDialog = addMapDialog;
		this.receivers = allReceivers;
		this.receiverButtons = new ArrayList<ReceiverButton>();
		initialize();
	}
	
	private void initialize() {
		
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(new Color(230, 230, 230));
		
		// TODO: display buttons for all receivers and set their properties - added/not added
		for (Receiver receiver : receivers) {
			ReceiverButton button = new ReceiverButton(receiver, addMapDialog);
			receiverButtons.add(button);
			this.add(button);
		}
		
		
	}

}
