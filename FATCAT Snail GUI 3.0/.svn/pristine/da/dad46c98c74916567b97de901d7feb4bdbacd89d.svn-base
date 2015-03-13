package fatcat.gui.snail.test;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.Frame;
import fatcat.gui.snail.SnailShell;
import fatcat.gui.snail.event.MouseListener;
import fatcat.gui.util.Image3x3;


public class SampleFrame extends Frame {

	public SampleFrame(final SnailShell shell) {
		super(shell);
		
		enableIncrementalPaint();
		
		
		Component c1 = new TestComponent(this);
		c1.setLocation(30, 30);
		c1.setSize(100, 100);
		
		Component c2 = new TestComponent(this);
		c2.setLocation(30, 180);
		
		final Dragger dragger = new Dragger(this, c1);
		dragger.setBounds(c2);
		
		Container c3 = new Container(this) {
			
			Image3x3 res = new Image3x3("/fatcat/gui/snail/test/Panel.png", 9, 55, 9, 55);
			
			@Override
			public void repaintComponent(GraphicsX g2, Component c) {
				g2.paint3x3(res, c.getWidth(), c.getHeight());
			}
			
		};
		c3.setLocation(120, 30);
		
		Component c4 = new Component(c3);
		c4.setLocation(c1);
		c4.setSkin(c3);
		
		Component c5 = new Component(c3);
		c5.setLocation(c2);
		
		Container c6 = new Container(c3);
		c6.setSkin(c3);
		c6.setLocation(c3);
		c6.setRightSpace(0);
		c6.setBottomSpace(0);
		
		final Dragger dragger2 = new Dragger(this, c3) {
			@Override
			public void mousePressed(Component c, MouseEvent e, int x, int y) {
				super.mousePressed(c, e, x, y);
			}
		};
		dragger2.setBounds(c6);
		
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		super.repaintComponent(g2, c);
		System.out.print(1);
	}

}

class TestComponent extends Component {

	public TestComponent(Container owner) {
		super(owner);
		enableIncrementalPaint(false);
	}
	
}

class Dragger extends Component implements MouseListener {
	
	private final Component dstComponent;
	private int old_x, old_y;
	private int old_dst_x, old_dst_y;

	protected Dragger(Container owner, Component dstComponent) {
		super(owner);
		super.setFocusable(false);
		this.dstComponent = dstComponent;
		addMouseListener(this);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		
	}
	
	public Component getDstComponent() {
		return dstComponent;
	}

	@Override
	public void mouseClicked(Component c, java.awt.event.MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(Component c, java.awt.event.MouseEvent e, int x, int y) {
		dstComponent.setLocation(
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
		old_dst_x = dstComponent.getLeft();
		old_dst_y = dstComponent.getTop();
	}

	@Override
	public void mouseReleased(Component c, java.awt.event.MouseEvent e, int x, int y) {
		
	}

	@Override
	public void mouseWheelMoved(Component c, MouseWheelEvent e) {
		
	}
	
}