package fatcat.snowberry.diagram.util;

import static java.lang.Math.abs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JarvisMarch {

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	private static int MAX_ANGLE = 4;
	private double currentMinAngle = 0;
	private List<JarvisPoint> hullPointList;
	private List<Integer> indexList;
	private JarvisPointFactory pf;
	private JarvisPoint[] ps;

	public JarvisPoint[] getPs() {
		return ps;
	}

	private int firstIndex;

	public int getFirstIndex() {
		return firstIndex;
	}

	public JarvisMarch() {
		this(10);
	}

	public JarvisMarch(int count) {
		pf = JarvisPointFactory.getInstance(count);
		initialize();
	}

	public JarvisMarch(int[] x, int[] y) {
		pf = JarvisPointFactory.getInstance(x, y);
		initialize();
	}

	private void initialize() {
		hullPointList = new LinkedList<JarvisPoint>();
		indexList = new LinkedList<Integer>();
		firstIndex = pf.getFirstIndex();
		ps = pf.getPoints();
		addToHull(firstIndex);
	}

	private void addToHull(int index) {
		indexList.add(index);
		hullPointList.add(ps[index]);
	}

	public void calculateHull() {
		for (int i = getNextIndex(firstIndex); i != firstIndex; i = getNextIndex(i)) {
			addToHull(i);
		}
		//showHullPoints();
	}
	
	public int[] getXs() {
		int[] rst = new int[hullPointList.size()];
		int i = 0;
		Iterator<JarvisPoint> itPoint = hullPointList.iterator();
		while (itPoint.hasNext()) {
			rst[i++] = itPoint.next().getX();
		}
		return rst;
	}
	
	public int[] getYs() {
		int[] rst = new int[hullPointList.size()];
		int i = 0;
		Iterator<JarvisPoint> itPoint = hullPointList.iterator();
		while (itPoint.hasNext()) {
			rst[i++] = itPoint.next().getY();
		}
		return rst;
	}
	
	public int size() {
		return hullPointList.size();
	}

//	private void showHullPoints() {
//		Iterator<Point> itPoint = hullPointList.iterator();
//		Iterator<Integer> itIndex = indexList.iterator();
//		Point p;
//		int i;
//		int index = 0;
//		while (itPoint.hasNext()) {
//			i = itIndex.next();
//			p = itPoint.next();
//			System.out.print(i + ":(" + p.getX() + "," + p.getY() + ")  ");
//			index++;
//			if (index % 10 == 0)
//				System.out.println();
//		}
//		System.out.println();
//	}

	public int getNextIndex(int currentIndex) {
		double minAngle = MAX_ANGLE;
		double pseudoAngle;
		int minIndex = 0;
		for (int i = 0; i < ps.length; i++) {
			if (i != currentIndex) {
				pseudoAngle = getPseudoAngle(ps[i].getX() - ps[currentIndex].getX(), 
											 ps[i].getY() - ps[currentIndex].getY());
				if (pseudoAngle == Double.NaN) continue;
				if (pseudoAngle >= currentMinAngle && pseudoAngle < minAngle) {
					minAngle = pseudoAngle;
					minIndex = i;
				} else if (pseudoAngle == minAngle){
						if((abs(ps[i].getX() - ps[currentIndex].getX()) > 
							abs(ps[minIndex].getX() - ps[currentIndex].getX()))
							|| (abs(ps[i].getY() - ps[currentIndex].getY()) > 
							abs(ps[minIndex].getY() - ps[currentIndex].getY()))){
							minIndex = i;
						}
				}
			}

		}
		currentMinAngle = minAngle;
		return minIndex;
	}

	public double getPseudoAngle(double dx, double dy) {
		if (dx > 0 && dy >= 0)
			return dy / (dx + dy);
		if (dx <= 0 && dy > 0)
			return 1 + (abs(dx) / (abs(dx) + dy));
		if (dx < 0 && dy <= 0)
			return 2 + (dy / (dx + dy));
		if (dx >= 0 && dy < 0)
			return 3 + (dx / (dx + abs(dy)));
		return Double.NaN;
	}

}
