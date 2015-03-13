package fatcat.snowberry.diagram;

import java.awt.Color;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.snowberry.gui.Arrow;


public class AssociationArrow extends Arrow {
	
	private final ComponentAdapter REMOVER = new ComponentAdapter() {
		public void componentRemoved(fatcat.gui.snail.Component c) {
			if (!isRemoved()) remove();
			getSrc().removeComponentListener(REMOVER);
			getDst().removeComponentListener(REMOVER);
			((MemberItem<?>) getSrc()).getDiagram().removeComponentListener(REMOVER);
		}
	};
	
	protected void refreshLocation() {
		super.refreshLocation();
		src_x = getSrc().getAbsLeft() - getAbsLeft() + 8;
		src_y = getSrc().getAbsTop() - getAbsTop() + 8;
	};
	
	public AssociationArrow(Container owner, MemberItem<?> src, Component dst) {
		super(owner, src, dst);
		setLineWidth(1.0f);
		setColor(Color.blue);
		src.addComponentListener(REMOVER);
		dst.addComponentListener(REMOVER);
		src.getDiagram().addComponentListener(REMOVER);
	}
	
	private int src_abs_y;
	
	@Override
	protected void update(double dt) {
		super.update(dt);
		
		// update the src location
		src_abs_y = getSrc().getAbsTop();
		
		// test clip
		Container c = getSrc().getOwner();
		while (!(c instanceof VirtualContainer)) {
			if (src_abs_y + 8 < c.getAbsTop() || src_abs_y + 8 > c.getAbsBottom()) {
				setVisible(false);
				return;
			}
			c = c.getOwner();
		}
		
		setVisible(true);
		refreshLocation();
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		super.repaintComponent(g2, c);
		g2.fillOval(src_x - 2, src_y - 2, 5, 5);
	}
	
	@Override
	protected void repaintArrow(GraphicsX g2) {
		g2.drawLine(-8, 4, 0, -1);
		g2.drawLine(-8, -4, 0, -1);
	}
	
}
