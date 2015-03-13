package fatcat.snowberry.diagram.dp;

import fatcat.gui.GraphicsX;
import java.awt.Polygon;

import fatcat.gui.snail.Container;
import fatcat.snowberry.diagram.ClassDiagram;


public class AssociationArrow extends DependencyArrow {

	public AssociationArrow(Container owner, ClassDiagram src, ClassDiagram dst) {
		super(owner, src, dst);
	}

	private final static Polygon polygon;
	
	static {
		int[] x = new int[] {0, -12, -12};
		int[] y = new int[] {0, 5, -5};
		polygon = new Polygon(x, y, 3);
	}
	
	@Override
	protected void repaintArrow(GraphicsX g2) {
		g2.fill(polygon);
	}	
}
