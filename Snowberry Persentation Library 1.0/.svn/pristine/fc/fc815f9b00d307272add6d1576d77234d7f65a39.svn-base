package fatcat.snowberry.gui.util;

import java.awt.event.MouseWheelEvent;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.event.MouseListener;


public class Resizer implements MouseListener {
	
	private final Component dstComponent;
	private int old_x, old_y;
	private int old_dst_x, old_dst_y;

	public Resizer(Component dstComponent) {
		this.dstComponent = dstComponent;
	}
	
	public Component getDstComponent() {
		return dstComponent;
	}

	@Override
	public void mouseClicked(Component c, java.awt.event.MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(Component c, java.awt.event.MouseEvent e, int x, int y) {
		dstComponent.setSize(
				old_dst_x + (e.getX() - old_x),
				old_dst_y + (e.getY() - old_y)
			);
	}

	@Override
	public void mouseEntered(Component c) {
		
	}

	@Override
	public void mouseExited(Component c) {
		
	}

	@Override
	public void mouseMoved(Component c, java.awt.event.MouseEvent e, int x, int y) {
		
	}

	@Override
	public void mousePressed(Component c, java.awt.event.MouseEvent e, int x, int y) {
		old_x = e.getX();
		old_y = e.getY();
		old_dst_x = dstComponent.getWidth();
		old_dst_y = dstComponent.getHeight();
	}

	@Override
	public void mouseReleased(Component c, java.awt.event.MouseEvent e, int x, int y) {
		
	}

	@Override
	public void mouseWheelMoved(Component c, MouseWheelEvent e) {
		
	}
	
}
