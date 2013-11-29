package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 700;
	private static final int SCROLL_PANE_WIDTH  = 650; // 20px more than the content so that it will not display scroll bar by default
	private static final int SCROLL_PANE_HEIGHT = 520; // 20px more than the content so that it will not display scroll bar by default
	private MenuBar menuBar;
	private ButtonsPanel buttonsPanel;
	private MapsPanel mapsPanel;
	private StatusPanel statusPanel;
	
	public MainFrame() {
		
		initialize();
		addListenersForComponents();
	}

	private void initialize() {
		
		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setLayout(new GridBagLayout());
		
		// add menu bar
		menuBar = new MenuBar();
		this.setJMenuBar(menuBar);
		
		// add buttons panel
		buttonsPanel = new ButtonsPanel();
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1;
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		
		this.add(buttonsPanel, gbc1);
		
		// add maps panel
		mapsPanel = new MapsPanel();
		JScrollPane mapsScrollPane = new JScrollPane(mapsPanel);
		mapsScrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
		mapsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mapsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.weightx = 1;
		gbc2.anchor = GridBagConstraints.LINE_START;
		this.add(mapsScrollPane, gbc2);
		
		
		// add status panel
		statusPanel = new StatusPanel();
		
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 2;
		gbc3.weightx = 1;
		gbc3.fill = GridBagConstraints.HORIZONTAL;
		
		this.add(statusPanel, gbc3);
		
		this.pack();
		setLocationRelativeTo(null); // center the window on the screens
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLookAndFeel();
		setVisible(true);
		
	}
	
	private void addListenersForComponents() {
		
		
		
		// TODO: add other listeners
	}
	
	
	
	private void setLookAndFeel() {
		
		try {
	        LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
	        for (LookAndFeelInfo lookAndFeelInfo : infos) {
	        	
	    		if (lookAndFeelInfo.getName() == "Nimbus" ) {
	                UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
	                SwingUtilities.updateComponentTreeUI(this);
	            }
	        }
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       e.printStackTrace();
	    }
	    catch (ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
	    catch (InstantiationException e) {
	    	e.printStackTrace();
	    }
	    catch (IllegalAccessException e) {
	    	e.printStackTrace();
	    }
	}
		

}
