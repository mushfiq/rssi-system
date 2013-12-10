package gui;

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
import algorithm.PositionLocalizationAlgorithm;
import main.Application;
import utilities.Utilities;


public class ParametersPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final int PANEL_WIDTH = 200;
	private static final int PANEL_HEIGHT = 620;
	private static final int ROTATE_AMOUNT = 45; // degrees
	private static final String ROTATE_CLOCKWISE_IMAGE_PATH = "images/clockwise.png";
	private static final String ROTATE_COUNTER_CLOCKWISE_IMAGE_PATH = "images/counterClockwise.png";
	private static final String START_ICON_IMAGE_PATH = "images/start.png";
	private static final String STOP_ICON_IMAGE_PATH = "images/stop.png";
	private JButton uploadButton, saveButton, cancelButton;
	private JLabel ratioLabel;
	private JLabel animationLabel;
	private JSpinner ratioSpinner;
	private AddMapDialog addMapDialog;
	private JButton rotateReceiverClockwiseButton;
	private JButton rotateReceiverCounterClockwiseButton;
	private StartStopButton startStopButton;
	private JComboBox<PositionLocalizationAlgorithmType> algorithmComboBox;
	/** The logger. */
	private Logger logger;
	private AddMapDialogMode openningMode;
	
	public ParametersPanel(AddMapDialog parentDialog, AddMapDialogMode openningMode) {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
		this.openningMode = openningMode;
		this.addMapDialog = parentDialog;
		initialize();
		addListenersToComponents();
	}

	private void initialize() {
		
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setLayout(new GridBagLayout());
		setBackground(new Color(230, 230, 230));
		
		// Add 'Counter clockwise button'
		ImageIcon counterClockwiseIcon = new ImageIcon(Utilities.loadImage(ROTATE_COUNTER_CLOCKWISE_IMAGE_PATH));
		rotateReceiverCounterClockwiseButton = new JButton(counterClockwiseIcon);
		rotateReceiverCounterClockwiseButton.addActionListener(new RotateButtonsListener());
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.gridx = 0;
		gbc4.gridy = 3;
		gbc4.gridwidth = 1;
		gbc4.gridheight = 1;
		gbc4.weightx = 1;
		gbc4.weighty = 2;
		gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
		
		this.add(rotateReceiverCounterClockwiseButton, gbc4);
		
		// Add 'Clockwise button'
		ImageIcon clockwiseIcon = new ImageIcon(Utilities.loadImage(ROTATE_CLOCKWISE_IMAGE_PATH));
		rotateReceiverClockwiseButton = new JButton(clockwiseIcon);
		rotateReceiverClockwiseButton.addActionListener(new RotateButtonsListener());
		GridBagConstraints gbc5 = new GridBagConstraints();
		gbc5.gridx = 1;
		gbc5.gridy = 3;
		gbc5.gridwidth = 1;
		gbc5.gridheight = 1;
		gbc5.weightx = 1;
		gbc5.weighty = 2;
		gbc5.anchor = GridBagConstraints.BASELINE_TRAILING;
		
		this.add(rotateReceiverClockwiseButton, gbc5);
		
		
		// add start/stop button
		startStopButton = new StartStopButton();
		GridBagConstraints gbc6 = new GridBagConstraints();
		gbc6.gridx = 0;
		gbc6.gridy = 5;
		gbc6.gridwidth = 1;
		gbc6.gridheight = 1;
		gbc6.weightx = 1;
		gbc6.weighty = 2;
		gbc6.anchor = GridBagConstraints.BASELINE_LEADING;
		
		this.add(startStopButton, gbc6);
		startStopButton.setVisible( (openningMode == AddMapDialogMode.ADD) ? false : true );
		
		// add algorithm combo box
		algorithmComboBox = new JComboBox<PositionLocalizationAlgorithmType>();
		algorithmComboBox.addItem(PositionLocalizationAlgorithmType.PROBABILITY_BASED);
		algorithmComboBox.addItem(PositionLocalizationAlgorithmType.PROXIMITY);
		algorithmComboBox.addActionListener(new AlgorithmComboBoxActionListener());
		GridBagConstraints gbc7 = new GridBagConstraints();
		gbc7.gridx = 0;
		gbc7.gridy = 4;
		gbc7.gridwidth = 2;
		gbc7.gridheight = 1;
		gbc7.weightx = 1;
		gbc7.weighty = 2;
		gbc7.anchor = GridBagConstraints.BASELINE_LEADING;
		
		this.add(algorithmComboBox, gbc7);
		algorithmComboBox.setVisible( (openningMode == AddMapDialogMode.EDIT) ? true : false);
		
		// Add 'Upload' button
		uploadButton = new JButton("Upload");
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 9;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.weightx = 1;
		gbc1.weighty = 2;
		gbc1.anchor = GridBagConstraints.LAST_LINE_START;
		
		this.add(uploadButton, gbc1);
		
		// Add 'Save' button
		saveButton = new JButton("Save");
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 10;
		gbc2.gridwidth = 1;
		gbc2.gridheight = 1;
		gbc2.weightx = 1;
		gbc2.weighty = 1;
		gbc2.anchor = GridBagConstraints.LINE_START;
		
		this.add(saveButton, gbc2);
		
		// Add 'Cancel' button
		cancelButton = new JButton("Cancel");
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 1;
		gbc3.gridy = 10;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1;
		gbc3.weighty = 1;
		gbc3.anchor = GridBagConstraints.LINE_END;
			
		this.add(cancelButton, gbc3);
	}
	
	private void addListenersToComponents() {
		
		this.uploadButton.addActionListener(new UploadButtonListener(addMapDialog));
		this.saveButton.addActionListener(new SaveButtonListener(addMapDialog));
		this.cancelButton.addActionListener(new CancelButtonListener(addMapDialog));
	}
	
	
	private class UploadButtonListener implements ActionListener {
		
		private JDialog observable;
		
		public UploadButtonListener(JDialog observable) {
			
			this.observable = observable;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//Create a file chooser
			final JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new ImageFilter());
			
			int returnVal = fc.showOpenDialog(observable);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            //This is where a real application would open the file.
	            
	            addMapDialog.setPreviewImage(file);
	            addMapDialog.addCoordinateZeroView();
	            
	        } else {
	            
	        	logger.severe("Couldn't open the image.");
	         
	        } 
			
		}
			
	}
	
	private class SaveButtonListener implements ActionListener {
		
		private JDialog observable;
		
		public SaveButtonListener(JDialog observable) {
			
			this.observable = observable;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
			
	}
	
	private class CancelButtonListener implements ActionListener {

		private JDialog observable;
		
		public CancelButtonListener(JDialog observable) {
			
			this.observable = observable;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			observable.dispose(); // close the dialog
		}
			
	}
	

	private class RotateButtonsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource().equals(rotateReceiverClockwiseButton)) {
				
				// TODO: handle clockwise rotation
				addMapDialog.getMapPreviewPanel().rotateReceiverViewInFocus(-ROTATE_AMOUNT);
			} else { // counter clockwise
				
				// TODO: handle counter clockwise rotation
				addMapDialog.getMapPreviewPanel().rotateReceiverViewInFocus(ROTATE_AMOUNT);
			}
			
		}
		
	}
	
	private class StartStopButton extends JButton implements ActionListener {
		
		private static final long serialVersionUID = 1L;
		private StartStopButtonState state;
		private ImageIcon startIcon = new ImageIcon(Utilities.loadImage(START_ICON_IMAGE_PATH));
		private ImageIcon stopIcon  = new ImageIcon(Utilities.loadImage(STOP_ICON_IMAGE_PATH));
		
		public StartStopButton() {
			this.state = StartStopButtonState.STARTED;
			this.setIcon(startIcon);
			this.addActionListener(this);
		}

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
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			toggle();	
		}	
	}
	
	
	private class AlgorithmComboBoxActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JComboBox<PositionLocalizationAlgorithmType> comboBox = ((JComboBox<PositionLocalizationAlgorithmType>) e.getSource());
			ComboBoxModel<PositionLocalizationAlgorithmType> model = comboBox.getModel();
		    int index = comboBox.getSelectedIndex();
		    PositionLocalizationAlgorithmType selection = model.getElementAt(index);
			
		    Application.getApplication().setAlgorithm(createAlgorithmInstance(selection));
		}
		
	}
	
	
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


}
