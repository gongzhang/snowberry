package fatcat.snowberry.diagram;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import fatcat.gui.GraphicsX;
import java.awt.Polygon;
import java.util.LinkedList;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.IArea;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.snowberry.diagram.util.JarvisMarch;


public class PolygonArea<T extends Component> extends Component {
	
	private Polygon polygon;
	private BasicStroke stroke;
	private Color color, fill_color;
	private LinkedList<T> areas;
	private float alpha;
	
	private final ComponentAdapter AUTO_UPDATE = new ComponentAdapter() {
		
		public void componentRemoved(Component c) {
			c.removeComponentListener(AUTO_UPDATE);
			areaRemoved(c);
		}
		
	};

	public PolygonArea(Container owner, T area) {
		super(owner);
		setClip(false);
		setLineWidth(2.5f);
		setColor(Color.red);
		setFocusable(false);
		this.areas = new LinkedList<T>();
		setAlpha(1.0f);
		addArea(area);
	}
	
	public void addArea(T area) {
		areas.add(area);
		area.addComponentListener(AUTO_UPDATE);
		updateArea();
	}
	
	private void areaRemoved(Component area) {
		areas.remove(area);
		if (areas.size() > 0) {
			updateArea();
		} else {
			getShell().syncExec(new Runnable() {
				@Override
				public void run() {
					if (!isRemoved())
						remove();
				}
			});
		}
	}
	
	@Override
	protected void update(double dt) {
		if (isInitialized()) updateArea();
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		if (polygon != null) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2.setColor(fill_color);
			g2.fill(polygon);
			g2.setColor(color);
			g2.setStroke(stroke);
			g2.draw(polygon);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
		final int r = 255 - color.getRed();
		final int g = 255 - color.getGreen();
		final int b = 255 - color.getBlue();
		fill_color = new Color(255 - (r / 5), 255 - (g / 5), 255 - (b / 5));
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return 0;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 0;
	}
	
	public void updateArea() {
		int[] x = new int[areas.size() * 4];
		int[] y = new int[areas.size() * 4];
		int i = 0;
		for (IArea area : areas) {
			x[i] = area.getLeft() - 10;
			y[i] = area.getTop() - 10;
			x[i + 1] = area.getRight() + 10;
			y[i + 1] = y[i];
			x[i + 2] = x[i + 1];
			y[i + 2] = area.getBottom() + 10;
			x[i + 3] = x[i];
			y[i + 3] = y[i + 2];
			i += 4;
		}
		JarvisMarch jm = new JarvisMarch(x, y);
		jm.calculateHull();
		polygon = new Polygon(jm.getXs(), jm.getYs(), jm.size());
	}
	
	public void setLineWidth(float width) {
		stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] {15.0f, 15.0f}, 0.0f);
	}
	
	public float getLineWidth() {
		return stroke.getLineWidth();
	}
	
}
