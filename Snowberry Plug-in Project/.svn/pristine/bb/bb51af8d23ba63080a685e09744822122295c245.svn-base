package fatcat.snowberry.diagram;

import java.awt.Color;
import fatcat.gui.GraphicsX;
import java.awt.event.MouseEvent;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.gui.Arrow;
import fatcat.snowberry.gui.Button;
import fatcat.snowberry.gui.SwitchButton;
import fatcat.snowberry.search.IConnection;

public class ConnectionLine extends Arrow {
	
	private final IConnection connection;
	private final SwitchButton button;
	
	private final ComponentAdapter REMOVER = new ComponentAdapter() {
		public void componentRemoved(fatcat.gui.snail.Component c) {
			if (!isRemoved()) remove();
			getSrc().removeComponentListener(REMOVER);
			getDst().removeComponentListener(REMOVER);
		}
	};

	public ConnectionLine(Container owner, ClassDiagram src, ClassDiagram dst, IConnection connection) {
		super(owner, src, dst);
		this.connection = connection;
		src.addComponentListener(REMOVER);
		dst.addComponentListener(REMOVER);
		
		// set property
		setLineWidth(Math.min((float) connection.getLambda(), 1.0f) * 2.5f);
		setColor(new Color(0x333333));
		
		button = new SwitchButton(owner) {
			@Override
			public void repaintComponent(GraphicsX g2, Component c) {
				if (getState() == Button.NORMAL) {
					setState(Button.UP);
				}
				super.repaintComponent(g2, c);
			}
		};
		button.setSize(8, 8);
		class ButtonMouseAdapter extends MouseAdapter {
			
			public ConnectionPanel panel = null;
			int dx = 8, dy = 8;
			
			@Override
			public void mouseReleased(Component c, MouseEvent e, int x, int y) {
				if (button.isSelected()) {
					DiagramFrame frame = (DiagramFrame) getOwner().getFrame();
					panel = new ConnectionPanel(frame.getTopContainer(), getConnection(), button) {
						@Override
						protected void mouseDragged(Component c, MouseEvent e, int x, int y) {
							dx = panel.getAbsLeft() - button.getAbsLeft();
							dy = panel.getAbsTop() - button.getAbsTop();
						}
					};
					panel.setLocation(c.getLeft() + 8, c.getTop() + 8);
				} else {
					if (panel != null) {
						panel.remove();
						panel = null;
					}
				}
			}
			
		};
		final ButtonMouseAdapter btnMouseAdapter = new ButtonMouseAdapter();
		button.addMouseListener(btnMouseAdapter);
		button.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(Component c) {
				if (btnMouseAdapter.panel != null) {
					btnMouseAdapter.panel.setLocation(c.getLeft() + btnMouseAdapter.dx, c.getTop() + btnMouseAdapter.dy);
				}
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentRemoved(Component c) {
				if (!button.isRemoved()) {
					button.remove();
					if (btnMouseAdapter.panel != null) {
						btnMouseAdapter.panel.remove();
						btnMouseAdapter.panel = null;
					}
				}
			}
		});
	}
	
	@Override
	public void refreshLocation() {
		super.refreshLocation();
		if (button != null)
			button.setLocation((int) (src_x * 1.8 + dst_x * 0.2) / 2 - 4, (int) (src_y * 1.8 + dst_y * 0.2) / 2 - 4);
	}
	
	public IConnection getConnection() {
		return connection;
	}
	
	@Override
	protected void repaintArrow(GraphicsX g2) {
	}

}
