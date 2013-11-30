package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

public class ParametersPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final int PANEL_WIDTH = 200;
	private static final int PANEL_HEIGHT = 550;
	private JButton uploadButton, saveButton, cancelButton;
	private JLabel ratioLabel;
	private JLabel animationLabel;
	private JSpinner ratioSpinner;
	private AddMapDialog addMapDialog;
	
	public ParametersPanel(AddMapDialog parentDialog) {
		
		this.addMapDialog = parentDialog;
		initialize();
		addListenersToComponents();
	}

	private void initialize() {
		
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setLayout(new GridBagLayout());
		
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
	            
	        } else {
	            
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
	
	

}
