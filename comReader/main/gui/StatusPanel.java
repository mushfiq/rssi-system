/*
 * 
 * 
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class extends <code>JPanel</code> and is used to show messages in <code>AddMapDialog</code> window.
 * 
 * @author Danilo
 * @see AddMapDialog
 */
public class StatusPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant PANEL_WIDTH. */
	private static final int PANEL_WIDTH = 950;

	/** The Constant PANEL_HEIGHT. */
	private static final int PANEL_HEIGHT = 20;
	
	/** The Constant GRAY_COLOUR. */
	private static final int GRAY_COLOUR = 230;

	/** The status label. */
	private JLabel statusLabel;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new <code>StatusPanel</code>.
	 */
	public StatusPanel() {

		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(new Color(GRAY_COLOUR, GRAY_COLOUR, GRAY_COLOUR));
		statusLabel = new JLabel("");
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		message = "";
		add(statusLabel);
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            <code>String</code> new message
	 */
	public void setMessage(String message) {
		this.message = message;
		updateMessage();
	}

	/**
	 * Updates message.
	 */
	private void updateMessage() {
		if (message != null) {
			statusLabel.setText(message);
		}

	}
}
