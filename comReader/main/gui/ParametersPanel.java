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
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;

import main.Application;
import utilities.Utilities;
import algorithm.PositionLocalizationAlgorithm;

// TODO: Auto-generated Javadoc
/**
 * The Class ParametersPanel.
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

	/** The Constant SPINNER_STEP. */
	private static final double SPINNER_STEP = 0.1; // 10cm

	/** The cancel button. */
	private JButton uploadButton, saveButton, cancelButton;

	/** The add map dialog. */
	private AddMapDialog addMapDialog;

	/** The rotate receiver clockwise button. */
	private JButton rotateReceiverClockwiseButton;

	/** The rotate receiver counter clockwise button. */
	private JButton rotateReceiverCounterClockwiseButton;

	/** The start stop button. */
	private StartStopButton startStopButton;

	/** The algorithm combo box. */
	private JComboBox<PositionLocalizationAlgorithmType> algorithmComboBox;
	/** The logger. */
	private Logger logger;

	/** The openning mode. */
	private AddMapDialogMode openningMode;

	/** The room width in meters spinner. */
	private JSpinner roomWidthInMetersSpinner;

	/** The room height in meters spinner. */
	private JSpinner roomHeightInMetersSpinner;

	/**
	 * Instantiates a new parameters panel.
	 * 
	 * @param parentDialog
	 *            the parent dialog
	 * @param openningMode
	 *            the openning mode
	 */
	public ParametersPanel(AddMapDialog parentDialog, AddMapDialogMode openningMode) {

		logger = Utilities.initializeLogger(this.getClass().getName());
		this.openningMode = openningMode;
		this.addMapDialog = parentDialog;
		initialize();
		addListenersToComponents();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {

		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setLayout(new GridBagLayout());
		setBackground(new Color(230, 230, 230));

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

		// Add algorithm label
		JLabel algorithmlabel = new JLabel("Algorithm:");
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.gridx = 0;
		gbc4.gridy = 2;
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
		gbc5.gridy = 3;
		gbc5.gridwidth = 2;
		gbc5.gridheight = 1;
		gbc5.weightx = 1;
		gbc5.weighty = 2;
		gbc5.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(algorithmComboBox, gbc5);
		algorithmComboBox.setVisible((openningMode == AddMapDialogMode.EDIT) ? true : false);

		// add room height label
		JLabel roomHeightLabel = new JLabel("Room height (m):");
		GridBagConstraints gbc6 = new GridBagConstraints();
		gbc6.gridx = 0;
		gbc6.gridy = 4;
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
		gbc7.gridy = 4;
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
		gbc8.gridy = 5;
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
		gbc9.gridy = 5;
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
		gbc10.gridy = 6;
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
		gbc11.gridy = 7;
		gbc11.gridwidth = 1;
		gbc11.gridheight = 1;
		gbc11.weightx = 1;
		gbc11.weighty = 2;
		gbc11.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(startStopButton, gbc11);
		startStopButton.setVisible((openningMode == AddMapDialogMode.ADD) ? false : true);

		// Add 'Upload' button
		uploadButton = new JButton("Upload");
		GridBagConstraints gbc12 = new GridBagConstraints();
		gbc12.gridx = 0;
		gbc12.gridy = 8;
		gbc12.gridwidth = 2;
		gbc12.gridheight = 1;
		gbc12.weightx = 1;
		gbc12.weighty = 2;
		gbc12.anchor = GridBagConstraints.LINE_START;

		this.add(uploadButton, gbc12);

		// Add 'Save' button
		saveButton = new JButton("Save");
		GridBagConstraints gbc13 = new GridBagConstraints();
		gbc13.gridx = 0;
		gbc13.gridy = 9;
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
		gbc14.gridy = 9;
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

		this.uploadButton.addActionListener(new UploadButtonListener(addMapDialog));
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
		 *            the observable
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
	 * The Class StartStopButton.
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
		 * Instantiates a new start stop button.
		 */
		public StartStopButton() {
			this.state = StartStopButtonState.STARTED;
			this.setIcon(startIcon);
			this.addActionListener(this);
		}

		/**
		 * Toggle.
		 */
		private void toggle() {

			if (state == StartStopButtonState.STOPPED) {

				state = StartStopButtonState.STARTED;
				this.setIcon(startIcon);
				this.repaint();
			} else {

				state = StartStopButtonState.STOPPED;
				this.setIcon(stopIcon);
				this.repaint();
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
	 * XXX listener works fine, but it is most likely not going to be used this way. Code is left just in case needed.
	 * Should be removed later if found unnecessary.
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

	public double getRoomWidthInMeters() {

		return Double.parseDouble(roomWidthInMetersSpinner.getValue().toString());
	}
	
	public double getRoomHeightInMeters() {
		return Double.parseDouble(roomHeightInMetersSpinner.getValue().toString());
	}
}
