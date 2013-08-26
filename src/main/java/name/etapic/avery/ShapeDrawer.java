package name.etapic.avery;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class ShapeDrawer extends Frame {

	private static final long serialVersionUID = 4719982395394873055L;

	public static void main(String[] args) {
		Frame frame = new ShapeDrawer();
		frame.addWindowListener(new ExitOnWindowClose());
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

	static class ExitOnWindowClose extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(Color.GREEN);
		Rectangle bounds = this.getBounds();
		graphics2D.fill(bounds);
		graphics2D.setColor(Color.RED);
		graphics2D.fillOval(100, 100, 200, 200);
	}
}
