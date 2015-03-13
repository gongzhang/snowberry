package fatcat.snowberry.gui.util;


final public class QInterpolator {
	
	public final double startValue, endValue, time;
	private double curValue, timer;
	
	public QInterpolator(double startValue, double endValue, double time) {
		this.startValue = startValue;
		this.endValue = endValue;
		this.time = time;
		curValue = startValue;
		timer = 0.0;
	}
	
	public boolean hasDone() {
		return timer >= time;
	}
	
	public double update(double dt) {
		if (timer < time) {
			curValue = qInterpolate(timer / time) * (endValue - startValue) + startValue;
			timer += dt;
		}
		return curValue;
	}
	
	public static final double qInterpolate(double lambda) {
		return lambda * (2.0 - lambda);
	}

}
