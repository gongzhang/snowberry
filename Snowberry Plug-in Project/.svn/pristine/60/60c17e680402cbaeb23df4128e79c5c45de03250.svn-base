package fatcat.snowberry.diagram.dp;

import java.awt.Color;
import fatcat.gui.GraphicsX;
import java.awt.Polygon;

import fatcat.gui.snail.Container;
import fatcat.snowberry.diagram.ClassDiagram;


public class GeneralizationArrow extends DependencyArrow {

	public GeneralizationArrow(Container owner, ClassDiagram src, ClassDiagram dst) {
		super(owner, src, dst);
	}
	
	private final static Polygon polygon;
	
	static {
		int[] x = new int[] {0, -10, -10};
		int[] y = new int[] {0, -8, 8};
		polygon = new Polygon(x, y, 3);
	}
	
	@Override
	protected void repaintArrow(GraphicsX g2) {
		g2.setColor(Color.white);
		g2.fill(polygon);
		g2.setColor(getColor());
		g2.draw(polygon);
	}	

}
