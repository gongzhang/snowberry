package fatcat.snowberry.diagram.dp;

import java.awt.Color;
import fatcat.gui.GraphicsX;
import java.awt.Polygon;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.snowberry.diagram.ClassDiagram;


public class AggregationArrow extends DependencyArrow {

	public AggregationArrow(Container owner, ClassDiagram src, ClassDiagram dst) {
		super(owner, src, dst);
	}
	
	@Override
	protected void refreshLocation() {
		src_x = getSrc().getLeft() + getSrc().getWidth() / 2;
		if (getSrc().getLeft() + getSrc().getWidth() / 2 > getDst().getLeft() + getDst().getWidth() / 2) {
			dst_x = getDst().getRight();
		} else {
			dst_x = getDst().getLeft();
		}
		
		dst_y = getDst().getTop() + getDst().getHeight() / 2;
		if (getSrc().getTop() + getSrc().getHeight() / 2 > getDst().getTop() + getDst().getHeight() / 2) {
			src_y = getSrc().getTop();
		} else {
			src_y = getSrc().getBottom();
		}
	}
	
	private final static Polygon polygon;
	
	static {
		int[] x = new int[] {0, -10, -20, -10};
		int[] y = new int[] {0, 5, 0, -5};
		polygon = new Polygon(x, y, 4);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(getColor());
		g2.setStroke(getStroke());
		g2.drawLine(src_x, src_y, src_x, dst_y);
		g2.drawLine(src_x, dst_y, dst_x, dst_y);
		g2.translate(dst_x + (src_x > dst_x ? 20 : 0), dst_y);
		repaintArrow(g2);
		g2.translate(-dst_x - (src_x > dst_x ? 20 : 0), -dst_y);
	}
	
	@Override
	protected void repaintArrow(GraphicsX g2) {
		g2.setColor(Color.white);
		g2.fill(polygon);
		g2.setColor(getColor());
		g2.draw(polygon);
	}
	
	@Override
	protected void repaintLabels(GraphicsX g2) {
		g2.setColor(getColor());
		g2.setFont(getFont());
		g2.drawString(getComment(), src_x, dst_y - 1);
	}

}
