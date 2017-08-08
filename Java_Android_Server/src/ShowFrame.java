import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShowFrame extends JFrame{
	MyPanel mp = new MyPanel();
	BufferedImage bi = null;
	public ShowFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(mp);
		setSize(600, 600);
		setVisible(true);
	}
	class MyPanel extends JPanel{
		public MyPanel() {
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(bi != null)
				g.drawImage(bi, 0, 0, getWidth(), getHeight(), this);
		}
	}
	public void setBufferedImage(BufferedImage bi){
		this.bi = bi;
		repaint();
	}
	

}
