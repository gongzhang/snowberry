package fatcat.snowberry.diagram;

import java.awt.Color;

import fatcat.gui.snail.Container;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.util.HashColor;


public class AuthorPolygon extends PolygonArea<ClassDiagram> {
	
	private double timer = 0.0;

	public AuthorPolygon(Container owner, ClassDiagram area, String author) {
		super(owner, area);
		if (author == null) {
			author = International.Anonymous;
			setColor(Color.gray);
		} else {
			setColor(HashColor.getColor(author));
		}
		setAlpha(0.0f);
	}
	
	@Override
	protected void update(double dt) {
		super.update(dt);
		timer += dt;
		if (timer < 2.0) {
			setAlpha((float) (timer / 2.0 * 0.5));
		}
	}

}
