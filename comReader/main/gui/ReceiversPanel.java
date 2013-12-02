package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ReceiversPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int PANEL_WIDTH = 950;
	private static final int PANEL_HEIGHT = 40;
	private List<ReceiverView> receivers;
	private AddMapDialog addMapDialog;
	
	public ReceiversPanel(AddMapDialog addMapDialog) {
		
		this.addMapDialog = addMapDialog;
		initialize();
		receivers = new ArrayList<ReceiverView>();
		
		// XXX: hardcoding receivers for testing purposes
		
		ReceiverView receiver1 = new ReceiverView(1, this);
		ReceiverView receiver2 = new ReceiverView(2, this);
		ReceiverView receiver3 = new ReceiverView(3, this);
		ReceiverView receiver4 = new ReceiverView(4, this);
		ReceiverView receiver5 = new ReceiverView(5, this);
		
		this.addReceiver(receiver1);
		this.addReceiver(receiver2);
		this.addReceiver(receiver3);
		this.addReceiver(receiver4);
		this.addReceiver(receiver5);
		
		// XXX: additional code that handles moving of components
		
//		ComponentMover cm = new ComponentMover();
//		cm.registerComponent(receiver1, receiver2, receiver3, receiver4, receiver5);
		
		// end of additional code that handles moving of components
		
		refreshPanel();
		// end of bogus code
	}
	
	public ReceiversPanel(List<ReceiverView> receivers) {
		
		if (receivers == null) {
			this.receivers = new ArrayList<ReceiverView>();
		} else {
			this.receivers = receivers;
		}
		
		initialize();
	}
	
	private void initialize() {
		
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(new Color(230, 230, 230));
	}
	
	private void clearReceivers() {
		
		receivers.clear();
	}
	
	private void removeReceiver(ReceiverView receiver) {
		
		if (this.receivers.contains(receiver)) {
			this.receivers.remove(receiver);
		}
	}
	
	private void addReceiver(ReceiverView receiver) {
		
		if(!this.receivers.contains(receiver)) {
			this.receivers.add(receiver);
		}
		
		refreshPanel();
	}
	
	private void refreshPanel() {
		// TODO: redraw receivers on the panel
		
		for (ReceiverView receiver : this.receivers) {
			this.add(receiver); // add receiver to the panel
		}
		
	}
	

}
