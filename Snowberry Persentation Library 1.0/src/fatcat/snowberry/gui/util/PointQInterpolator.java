package fatcat.snowberry.gui.util;

import java.awt.Point;


public final class PointQInterpolator {
	
	private final QInterpolator xInterpolator;
	private final QInterpolator yInterpolator;
	public final Point endValue;
	
	public PointQInterpolator(int x1, int y1, int x2, int y2, double time) {
		xInterpolator = new QInterpolator(x1, x2, time);
		yInterpolator = new QInterpolator(y1, y2, time);
		endValue = new Point(x2, y2);
	}
	
	public boolean hasDone() {
		return xInterpolator.hasDone();
	}
	
	public Point update(double dt) {
		if (!xInterpolator.hasDone())
			return new Point((int) xInterpolator.update(dt), (int) yInterpolator.update(dt));
		else
			return null;
	}

}
