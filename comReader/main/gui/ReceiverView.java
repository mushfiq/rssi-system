package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import utilities.Utilities;

public class ReceiverView extends JComponent {

	private static final long serialVersionUID = 1L;
	private int receiverId;
	private int x;
	private int y;
	private BufferedImage image;
	private static final int RECEIVER_ITEM_WIDTH  = 30;
	private static final int RECEIVER_ITEM_HEIGHT = 30;
	private ReceiversPanel parent;
	 
	
	public ReceiverView(int receiverId, ReceiversPanel parent) {
		
		this.receiverId = receiverId;
		this.parent = parent;
		initialize();
	}
	
	private void initialize() {

		setSize(RECEIVER_ITEM_WIDTH, RECEIVER_ITEM_HEIGHT);
		setPreferredSize(new Dimension(RECEIVER_ITEM_WIDTH, RECEIVER_ITEM_HEIGHT));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new ReceiverViewMouseListener(this));
		setDoubleBuffered(true);
		
		BufferedImage myPicture = null;
		try {
			String path = this.getClass().getResource("images/receiverView.png").getPath();
			myPicture = ImageIO.read(new File(path));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		image = Utilities.scaleImageToFitContainer(myPicture, ReceiverView.RECEIVER_ITEM_WIDTH, ReceiverView.RECEIVER_ITEM_HEIGHT);
		setOpaque(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(this.image, 0, 0, this);
		g.drawString("" + receiverId, 10, 15);
		
	}

	
	private class ReceiverViewMouseListener implements MouseListener {

		private ReceiverView receiverView;
		
		public ReceiverViewMouseListener(ReceiverView receiverView) {
			this.receiverView = receiverView;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}		
	}

	
}
