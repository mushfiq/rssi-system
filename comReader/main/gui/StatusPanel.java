package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int PANEL_WIDTH = 950;
	private static final int PANEL_HEIGHT = 20;
	private JLabel statusLabel;
	private String message;
	
	public StatusPanel() {
		
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(new Color(230, 230, 230));
		statusLabel = new JLabel("");
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		message = "";
		add(statusLabel);
		
	}

	public void setMessage(String message){
		this.message = message;
		updateMessage();
	}
	
	private void updateMessage(){
		if( message != null) {
			statusLabel.setText(message);
		}
		
	}
}
