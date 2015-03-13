package fatcat.snowberry.diagram.util;

public class JarvisPoint {
	
	private int x;
	private int y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private static double PADDING = 20;
	private static double POINT_RANGE = (800 - PADDING * 2);

	public JarvisPoint() {
		this.x = (int) ((Math.random() * POINT_RANGE) + PADDING);
		this.y = (int) ((Math.random() * POINT_RANGE) + PADDING);
	}
	
	public JarvisPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		JarvisPoint other = (JarvisPoint) obj;
		if ((x == other.x) && (y == other.y))
			return true;
		
		return false;
	}
	
	
}