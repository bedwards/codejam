import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ShowCircle extends Frame {
	
	public static void main(String[] args) {
		ShowCircle frame = new ShowCircle();
		frame.addWindowListener(new ExitProgram());
		frame.setSize(400,400);
		frame.setVisible(true);
	}
	
	static class ExitProgram extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
		
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Rectangle b = this.getBounds();
		g2.setColor(Color.GREEN);
		g2.fill(b);
		g2.setColor(Color.RED);
		g2.fillOval(100,100,200,200);
	}

	
}
