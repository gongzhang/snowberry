package fatcat.snowberry.diagram.util;


public class JarvisPointFactory {

	private JarvisPoint[] points = null;
	private int newIndex;
	private int firstIndex = 0;

	public JarvisPoint[] getPoints() {
		return points;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public static JarvisPointFactory getInstance() {
		return new JarvisPointFactory();
	}

	public static JarvisPointFactory getInstance(int count) {
		return new JarvisPointFactory(count);
	}
	
	public static JarvisPointFactory getInstance(int[] x, int[] y) {
		return new JarvisPointFactory(x, y);
	}

	private JarvisPointFactory() {
		this(10);
	}

	private JarvisPointFactory(int count) {
		points = new JarvisPoint[count];
		for (int i = 0; i < count; i++) {
			points[i] = new JarvisPoint();
			newIndex = i;
			validatePoints();
		}
		firstIndex = getFirstPoint();
	}

	public JarvisPointFactory(int[] x, int[] y) {
		points = new JarvisPoint[y.length];
		for (int i = 0; i < y.length; i++) {
			points[i] = new JarvisPoint(x[i], y[i]);
		}
		firstIndex = getFirstPoint();
	}

	private void validatePoints() {
		for(int i = 0; i < newIndex; i++) {
				if(points[i].equals(points[newIndex])) {
					points[newIndex] = new JarvisPoint();
					validatePoints();
				}
			}
	}

	public int getFirstPoint() {
		int minIndex = 0;
		for (int i = 1; i < points.length; i++) {
			if (points[i].getY() < points[minIndex].getY()) {
				minIndex = i;
			} else if ((points[i].getY() == points[minIndex].getY())
					&& (points[i].getX() < points[minIndex].getX())) {
				minIndex = i;
			}
		}
		return minIndex;
	}

}