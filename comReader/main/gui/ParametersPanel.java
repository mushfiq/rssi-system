/*
 * 
 * 
 */
package gui;

import gui.enumeration.AddMapDialogMode;
import gui.enumeration.PositionLocalizationAlgorithmType;
import gui.enumeration.StartStopButtonState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;

import main.Application;
import utilities.Utilities;
import algorithm.PositionLocalizationAlgorithm;


/**
 * Contains buttons, labels, combo boxes, spinners and text fields used to set <code>RoomMap</code> properties.
 * 
 * @author Danilo
 * @see RoomMap
 */
public class ParametersPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant PANEL_WIDTH. */
	private static final int PANEL_WIDTH = 200;

	/** The Constant PANEL_HEIGHT. */
	private static final int PANEL_HEIGHT = 620;

	/** The Constant ROTATE_AMOUNT. */
	private static final int ROTATE_AMOUNT = 45; // degrees

	/** The Constant ROTATE_CLOCKWISE_IMAGE_PATH. */
	private static final String ROTATE_CLOCKWISE_IMAGE_PATH = "images/clockwise.png";

	/** The Constant ROTATE_COUNTER_CLOCKWISE_IMAGE_PATH. */
	private static final String ROTATE_COUNTER_CLOCKWISE_IMAGE_PATH = "images/counterClockwise.png";

	/** The Constant START_ICON_IMAGE_PATH. */
	private static final String START_ICON_IMAGE_PATH = "images/start.png";

	/** The Constant STOP_ICON_IMAGE_PATH. */
	private static final String STOP_ICON_IMAGE_PATH = "images/stop.png";

	/** The Constant ROOM_WIDTH_IN_METERS_MINIMUM. */
	private static final double ROOM_WIDTH_IN_METERS_MINIMUM = 1.0; // 1 meter

	/** The Constant ROOM_WIDTH_IN_METERS_MAXIMUM. */
	private static final double ROOM_WIDTH_IN_METERS_MAXIMUM = 100.0; // 100 meters

	/** The Constant ROOM_HEIGHT_IN_METERS_MINIMUM. */
	private static final double ROOM_HEIGHT_IN_METERS_MINIMUM = 1.0; // 1 meter

	/** The Constant ROOM_HEIGHT_IN_METERS_MAXIMUM. */
	private static final double ROOM_HEIGHT_IN_METERS_MAXIMUM = 100.0; // 100 meters
	
	/** The Constant MINIMUM_ROOM_WIDTH_IN_METERS. */
	private static final double MINIMUM_ROOM_WIDTH_IN_METERS = 1.0;
	
	/** The Constant MINIMUM_ROOM_HEIGHT_IN_METERS. */
	private static final double MINIMUM_ROOM_HEIGHT_IN_METERS = 1.0;

	/** The Constant SPINNER_STEP. */
	private static final double SPINNER_STEP = 0.1; // 10cm

	/** The Constant GRAY_COULOUR. */
	private static final int GRAY_COULOUR = 230;

	/** Button for choosing an image that will be used as a map. */
	private JButton chooseImageButton;

	/** The save button. */
	private JButton saveButton;

	/** The cancel button. */
	private JButton cancelButton;

	/** The start stop button. */
	private StartStopButton startStopButton;

	/** The add map dialog. */
	private AddMapDialog addMapDialog;

	/** The rotate receiver clockwise button. */
	private JButton rotateReceiverClockwiseButton;

	/** The rotate receiver counter clockwise button. */
	private JButton rotateReceiverCounterClockwiseButton;

	/** The algorithm combo box. */
	private JComboBox<PositionLocalizationAlgorithmType> algorithmComboBox;

	/** The logger. */
	private Logger logger;

	/** The opening mode. */
	private AddMapDialogMode openingMode;

	/** The room width in meters spinner. */
	private JSpinner roomWidthInMetersSpinner;

	/** The room height in meters spinner. */
	private JSpinner roomHeightInMetersSpinner;

	/** The title text field. */
	private JTextField titleTextField;

	/**
	 * Instantiates a new <code>ParametersPanel</code>.
	 * 
	 * @param parentDialog
	 *            <code>AddMapDialog</code> parent component
	 */
	public ParametersPanel(AddMapDialog parentDialog) {

		logger = Utilities.initializeLogger(this.getClass().getName());
		this.openingMode = parentDialog.getOpeningMode();
		this.addMapDialog = parentDialog;
		initialize();
		addListenersToComponents();
	}

	/**
	 * Initializes <code>ParametersPanel</code>. Depending on the value of <code>AddMapDialogMode</code> field in
	 * <code>parentDialog</code> object, some components may not be shown. For example, if the
	 * <code>AddMapDialogMode</code> is set to <code>AddMapDialogMode.ADD</code>, there will not be a button for
	 * starting/stopping readings and writings.
	 * 
	 */
	private void initialize() {

		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setLayout(new GridBagLayout());
		setBackground(new Color(GRAY_COULOUR, GRAY_COULOUR, GRAY_COULOUR));

		// Receiver view rotation label
		JLabel rotationlabel = new JLabel("Rotate receiver:");
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.gridwidth = 2;
		gbc1.gridheight = 1;
		gbc1.weightx = 1;
		gbc1.weighty = 2;
		gbc1.anchor = GridBagConstraints.CENTER;

		this.add(rotationlabel, gbc1);

		// Add 'Counter clockwise button'
		ImageIcon counterClockwiseIcon = new ImageIcon(Utilities.loadImage(ROTATE_COUNTER_CLOCKWISE_IMAGE_PATH));
		rotateReceiverCounterClockwiseButton = new JButton(counterClockwiseIcon);
		rotateReceiverCounterClockwiseButton.addActionListener(new RotateButtonsListener());
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 2;
		gbc2.gridheight = 1;
		gbc2.weightx = 1;
		gbc2.weighty = 2;
		gbc2.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(rotateReceiverCounterClockwiseButton, gbc2);

		// Add 'Clockwise button'
		ImageIcon clockwiseIcon = new ImageIcon(Utilities.loadImage(ROTATE_CLOCKWISE_IMAGE_PATH));
		rotateReceiverClockwiseButton = new JButton(clockwiseIcon);
		rotateReceiverClockwiseButton.addActionListener(new RotateButtonsListener());
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 1;
		gbc3.gridy = 1;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1;
		gbc3.weighty = 2;
		gbc3.anchor = GridBagConstraints.FIRST_LINE_END;

		this.add(rotateReceiverClockwiseButton, gbc3);

		// Add map title label
		JLabel mapTitleLabel = new JLabel("Title:");
		GridBagConstraints gbc15 = new GridBagConstraints();
		gbc15.gridx = 0;
		gbc15.gridy = 2;
		gbc15.gridwidth = 2;
		gbc15.gridheight = 1;
		gbc15.weightx = 1;
		gbc15.weighty = 2;
		gbc15.anchor = GridBagConstraints.LINE_START;

		this.add(mapTitleLabel, gbc15);

		// Add map title text field
		titleTextField = new JTextField("", 40);
		GridBagConstraints gbc16 = new GridBagConstraints();
		gbc16.gridx = 0;
		gbc16.gridy = 3;
		gbc16.gridwidth = 2;
		gbc16.gridheight = 1;
		gbc16.weightx = 3;
		gbc16.weighty = 2;
		gbc16.fill = GridBagConstraints.HORIZONTAL;
		gbc16.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(titleTextField, gbc16);

		// Add algorithm label
		JLabel algorithmlabel = new JLabel("Algorithm:");
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.gridx = 0;
		gbc4.gridy = 4;
		gbc4.gridwidth = 1;
		gbc4.gridheight = 1;
		gbc4.weightx = 1;
		gbc4.weighty = 2;
		gbc4.anchor = GridBagConstraints.LAST_LINE_START;

		this.add(algorithmlabel, gbc4);

		// add algorithm combo box
		algorithmComboBox = new JComboBox<PositionLocalizationAlgorithmType>();
		algorithmComboBox.addItem(PositionLocalizationAlgorithmType.PROBABILITY_BASED);
		algorithmComboBox.addItem(PositionLocalizationAlgorithmType.PROXIMITY);
		GridBagConstraints gbc5 = new GridBagConstraints();
		gbc5.gridx = 0;
		gbc5.gridy = 5;
		gbc5.gridwidth = 2;
		gbc5.gridheight = 1;
		gbc5.weightx = 1;
		gbc5.weighty = 2;
		gbc5.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(algorithmComboBox, gbc5);
		algorithmComboBox.setVisible((openingMode == AddMapDialogMode.EDIT) ? true : false);

		// add room height label
		JLabel roomHeightLabel = new JLabel("Room height (m):");
		GridBagConstraints gbc6 = new GridBagConstraints();
		gbc6.gridx = 0;
		gbc6.gridy = 6;
		gbc6.gridwidth = 1;
		gbc6.gridheight = 1;
		gbc6.weightx = 1;
		gbc6.weighty = 2;
		gbc6.anchor = GridBagConstraints.CENTER;

		this.add(roomHeightLabel, gbc6);

		// add room height spinner
		SpinnerModel roomHeightSpinnerModel = new SpinnerNumberModel(ROOM_HEIGHT_IN_METERS_MINIMUM,
																		ROOM_HEIGHT_IN_METERS_MINIMUM,
																		ROOM_HEIGHT_IN_METERS_MAXIMUM, SPINNER_STEP);
		roomHeightInMetersSpinner = new JSpinner(roomHeightSpinnerModel);
		((DefaultFormatter) ((JSpinner.DefaultEditor) roomHeightInMetersSpinner.getEditor()).getTextField()
				.getFormatter()).setAllowsInvalid(false);
		GridBagConstraints gbc7 = new GridBagConstraints();
		gbc7.gridx = 1;
		gbc7.gridy = 6;
		gbc7.gridwidth = 1;
		gbc7.gridheight = 1;
		gbc7.weightx = 1;
		gbc7.weighty = 2;
		gbc7.anchor = GridBagConstraints.LINE_START;

		this.add(roomHeightInMetersSpinner, gbc7);

		// add room width label
		JLabel roomWidthLabel = new JLabel("Room width (m):");
		GridBagConstraints gbc8 = new GridBagConstraints();
		gbc8.gridx = 0;
		gbc8.gridy = 7;
		gbc8.gridwidth = 1;
		gbc8.gridheight = 1;
		gbc8.weightx = 1;
		gbc8.weighty = 2;
		gbc8.anchor = GridBagConstraints.PAGE_START;

		this.add(roomWidthLabel, gbc8);

		// add room width spinner
		SpinnerModel roomWidthSpinnerModel = new SpinnerNumberModel(ROOM_WIDTH_IN_METERS_MINIMUM,
																	ROOM_WIDTH_IN_METERS_MINIMUM,
																	ROOM_WIDTH_IN_METERS_MAXIMUM, SPINNER_STEP);
		roomWidthInMetersSpinner = new JSpinner(roomWidthSpinnerModel);
		((DefaultFormatter) ((JSpinner.DefaultEditor) roomWidthInMetersSpinner.getEditor()).getTextField()
				.getFormatter()).setAllowsInvalid(false);
		GridBagConstraints gbc9 = new GridBagConstraints();
		gbc9.gridx = 1;
		gbc9.gridy = 7;
		gbc9.gridwidth = 1;
		gbc9.gridheight = 1;
		gbc9.weightx = 1;
		gbc9.weighty = 2;
		gbc9.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(roomWidthInMetersSpinner, gbc9);

		// add start/stop label
		JLabel startStopLabel = new JLabel("Start/Stop readings:");
		GridBagConstraints gbc10 = new GridBagConstraints();
		gbc10.gridx = 0;
		gbc10.gridy = 8;
		gbc10.gridwidth = 2;
		gbc10.gridheight = 1;
		gbc10.weightx = 1;
		gbc10.weighty = 2;
		gbc10.anchor = GridBagConstraints.LINE_START;

		this.add(startStopLabel, gbc10);

		// add start/stop button
		startStopButton = new StartStopButton();
		GridBagConstraints gbc11 = new GridBagConstraints();
		gbc11.gridx = 0;
		gbc11.gridy = 9;
		gbc11.gridwidth = 1;
		gbc11.gridheight = 1;
		gbc11.weightx = 1;
		gbc11.weighty = 2;
		gbc11.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(startStopButton, gbc11);
		startStopButton.setVisible((openingMode == AddMapDialogMode.ADD) ? false : true);

		// Add 'Choose image' button
		chooseImageButton = new JButton("Choose image");
		GridBagConstraints gbc12 = new GridBagConstraints();
		gbc12.gridx = 0;
		gbc12.gridy = 10;
		gbc12.gridwidth = 2;
		gbc12.gridheight = 1;
		gbc12.weightx = 1;
		gbc12.weighty = 2;
		gbc12.anchor = GridBagConstraints.LINE_START;

		this.add(chooseImageButton, gbc12);

		// Add 'Save' button
		saveButton = new JButton("Save");
		GridBagConstraints gbc13 = new GridBagConstraints();
		gbc13.gridx = 0;
		gbc13.gridy = 11;
		gbc13.gridwidth = 1;
		gbc13.gridheight = 1;
		gbc13.weightx = 1;
		gbc13.weighty = 1;
		gbc13.anchor = GridBagConstraints.LINE_START;

		this.add(saveButton, gbc13);

		// Add 'Cancel' button
		cancelButton = new JButton("Cancel");
		GridBagConstraints gbc14 = new GridBagConstraints();
		gbc14.gridx = 1;
		gbc14.gridy = 11;
		gbc14.gridwidth = 1;
		gbc14.gridheight = 1;
		gbc14.weightx = 1;
		gbc14.weighty = 1;
		gbc14.anchor = GridBagConstraints.LINE_END;

		this.add(cancelButton, gbc14);
	}

	/**
	 * Adds the listeners to components.
	 */
	private void addListenersToComponents() {

		this.chooseImageButton.addActionListener(new UploadButtonListener(addMapDialog));
		this.saveButton.addActionListener(new SaveButtonListener());
		this.cancelButton.addActionListener(new CancelButtonListener(addMapDialog));
	}

	/**
	 * The listener interface for receiving uploadButton events. The class that is interested in processing a
	 * uploadButton event implements this interface, and the object created with that class is registered with a
	 * component using the component's <code>addUploadButtonListener</code> method. When the uploadButton event occurs,
	 * that object's appropriate method is invoked.
	 * 
	 * @see UploadButtonEvent
	 */
	private class UploadButtonListener implements ActionListener {

		/** The observable. */
		private JDialog observable;

		/**
		 * Instantiates a new upload button listener.
		 * 
		 * @param observable
		 *            observable
		 */
		public UploadButtonListener(JDialog observable) {

			this.observable = observable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			// Create a file chooser
			final JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new ImageFilter());

			int returnVal = fc.showOpenDialog(observable);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				// This is where a real application would open the file.
				addMapDialog.setPreviewImage(file);
				addMapDialog.addCoordinateZeroView();
			} else {
				logger.severe("Couldn't open the image.");
			}
		}
	}

	/**
	 * The listener interface for receiving saveButton events. The class that is interested in processing a saveButton
	 * event implements this interface, and the object created with that class is registered with a component using the
	 * component's <code>addSaveButtonListener</code> method. When the saveButton event occurs, that object's
	 * appropriate method is invoked.
	 * 
	 * @see SaveButtonEvent
	 */
	private class SaveButtonListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			 * TODO Save map with all its details in case of successful validation, otherwise display an error message
			 * to the user
			 */
			
			addMapDialog.saveMap();
			
		}
	}

	/**
	 * The listener interface for receiving cancelButton events. The class that is interested in processing a
	 * cancelButton event implements this interface, and the object created with that class is registered with a
	 * component using the component's <code>addCancelButtonListener</code> method. When the cancelButton event occurs,
	 * that object's appropriate method is invoked.
	 * 
	 * @see CancelButtonEvent
	 */
	private class CancelButtonListener implements ActionListener {

		/** The observable. */
		private JDialog observable;

		/**
		 * Instantiates a new cancel button listener.
		 * 
		 * @param observable
		 *            the observable
		 */
		public CancelButtonListener(JDialog observable) {

			this.observable = observable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			observable.dispose(); // close the dialog
		}

	}

	/**
	 * The listener interface for receiving rotateButtons events. The class that is interested in processing a
	 * rotateButtons event implements this interface, and the object created with that class is registered with a
	 * component using the component's <code>addRotateButtonsListener</code> method. When the rotateButtons event
	 * occurs, that object's appropriate method is invoked.
	 * 
	 * @see RotateButtonsEvent
	 */
	private class RotateButtonsListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(rotateReceiverClockwiseButton)) { // clockwise rotation
				addMapDialog.getMapPreviewPanel().rotateReceiverViewInFocus(-ROTATE_AMOUNT);
			} else { // counter clockwise
				addMapDialog.getMapPreviewPanel().rotateReceiverViewInFocus(ROTATE_AMOUNT);
			}
		}
	}

	/**
	 * This class is used for starting and stopping readings using current <code>RoomMap</code> object's properties.
	 */
	private class StartStopButton extends JButton implements ActionListener {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The state. */
		private StartStopButtonState state;

		/** The start icon. */
		private ImageIcon startIcon = new ImageIcon(Utilities.loadImage(START_ICON_IMAGE_PATH));

		/** The stop icon. */
		private ImageIcon stopIcon = new ImageIcon(Utilities.loadImage(STOP_ICON_IMAGE_PATH));

		/**
		 * Instantiates a new <code>StartStopButton</code>.
		 */
		public StartStopButton() {
			this.state = StartStopButtonState.STOPPED;
			this.setIcon(startIcon);
			this.addActionListener(this);
		}

		/**
		 * Toggles the button's state. Since we are using one button for two actions, we toggle
		 * between the two states.
		 * 
		 * @see StartStopButtonState
		 */
		private void toggle() {

			if (state == StartStopButtonState.STARTED) {
				state = StartStopButtonState.STOPPED;
				this.setIcon(startIcon);
				this.repaint();
				Application.getApplication().stopReadingsAndWritings();
				// TODO stop readings and writings
			} else { // state was StartStopButtonState.STOPPED
				state = StartStopButtonState.STARTED;
				this.setIcon(stopIcon);
				this.repaint();
				// TODO start readings and writings
				Application.getApplication().startReadingsAndWritings();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			toggle();
		}
	}

	/*
	 * XXX listener works fine, but it is most likely not going to be used this way. Code is left just in case. Should
	 * be removed later if found unnecessary.
	 */
	/**
	 * The listener interface for receiving algorithmComboBoxAction events. The class that is interested in processing a
	 * algorithmComboBoxAction event implements this interface, and the object created with that class is registered
	 * with a component using the component's <code>addAlgorithmComboBoxActionListener</code> method. When the
	 * algorithmComboBoxAction event occurs, that object's appropriate method is invoked.
	 * 
	 * @see AlgorithmComboBoxActionEvent
	 */
	private class AlgorithmComboBoxActionListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() instanceof JComboBox<?>) {
				@SuppressWarnings("unchecked")
				JComboBox<PositionLocalizationAlgorithmType> comboBox = ((JComboBox<PositionLocalizationAlgorithmType>) e
						.getSource());
				ComboBoxModel<PositionLocalizationAlgorithmType> model = comboBox.getModel();
				int index = comboBox.getSelectedIndex();
				PositionLocalizationAlgorithmType selection = model.getElementAt(index);

				Application.getApplication().setAlgorithm(createAlgorithmInstance(selection));
			}
		}

	}

	/**
	 * Creates the algorithm instance.
	 * 
	 * @param type
	 *            the type
	 * @return the position localization algorithm
	 */
	private PositionLocalizationAlgorithm createAlgorithmInstance(PositionLocalizationAlgorithmType type) {

		switch (type) {

		case PROBABILITY_BASED:
			// TODO create probability based algorithm instance and return it
			return null;

		case PROXIMITY:
			// TODO create proximity based algorithm instance and return it
			return null;

		default:
			return null;
		}
	}

	/**
	 * Gets the room width in meters.
	 * 
	 * @return <code>double</code> room width in meters
	 */
	public double getRoomWidthInMeters() {

		return Double.parseDouble(roomWidthInMetersSpinner.getValue().toString());
	}

	/**
	 * Gets the room height in meters.
	 * 
	 * @return <code>double</code> room height in meters
	 */
	public double getRoomHeightInMeters() {
		return Double.parseDouble(roomHeightInMetersSpinner.getValue().toString());
	}

	/**
	 * Sets the room width and height in meters spinners.
	 * 
	 * @param width
	 *            <code>double</code> width
	 * @param height
	 *            <code>double</code> height
	 */
	public void setRoomWidthAndHeightInMetersSpinners(double width, double height) {

		if ( (width < MINIMUM_ROOM_WIDTH_IN_METERS) || (height < MINIMUM_ROOM_HEIGHT_IN_METERS)) {
			
			this.roomWidthInMetersSpinner.setValue(MINIMUM_ROOM_WIDTH_IN_METERS);
			this.roomHeightInMetersSpinner.setValue(MINIMUM_ROOM_HEIGHT_IN_METERS);
		} else {
			
			this.roomWidthInMetersSpinner.setValue(width);
			this.roomHeightInMetersSpinner.setValue(height);
		}
	}

	/**
	 * Sets the room title.
	 * 
	 * @param title
	 *            <code>String</code> new room title
	 */
	public void setRoomTitle(String title) {
		titleTextField.setText(title);
	}

	/**
	 * Gets the room title.
	 * 
	 * @return <code>String</code> room title
	 */
	public String getRoomTitle() {
		return titleTextField.getText();
	}
}
