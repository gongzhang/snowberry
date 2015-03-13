package fatcat.snowberry.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import fatcat.gui.GraphicsX;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ComponentAdapter;


public class Arrow extends Component {
	
	private Stroke stroke;
	private float LineWidth;
	private final Component src, dst;
	private Color color;
	
	private final ComponentAdapter SPY = new ComponentAdapter() {
		
		public void componentMoved(Component c) {
			refreshLocation();
		}
		
		public void componentResized(Component c) {
			refreshLocation();
		}
	};

	public Arrow(Container owner, Component src, Component dst) {
		super(owner);
		setLocation(-1, -1);
		setSize(0, 0);
		setLineWidth(1.0f);
		setColor(Color.black);
		this.src = src;
		this.dst = dst;
		this.src.addComponentListener(SPY);
		this.dst.addComponentListener(SPY);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentRemoved(Component c) {
				Arrow.this.src.removeComponentListener(SPY);
				Arrow.this.dst.removeComponentListener(SPY);
			}
		});
		refreshLocation();
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	protected int src_x, src_y, dst_x, dst_y;
	
	protected void refreshLocation() {
		src_x = src.getAbsLeft() - getOwner().getAbsLeft() + src.getWidth() / 2;
		src_y = src.getAbsTop() - getOwner().getAbsTop() + src.getHeight() / 2;
		dst_x = dst.getAbsLeft() - getOwner().getAbsLeft() + dst.getWidth() / 2;
		dst_y = dst.getAbsTop() - getOwner().getAbsTop() + dst.getHeight() / 2;
		
		if (src_x == dst_x) {
			if (src_y >= dst_y) {
				src_y -= src.getHeight() / 2;
				dst_y += dst.getHeight() / 2;
			} else {
				src_y += src.getHeight() / 2;
				dst_y -= dst.getHeight() / 2;
			}
		} else {
			final float k = (dst_y - src_y) / (float) (dst_x - src_x);
			final float b = dst_y - k * dst_x;

			if (Math.abs(k) < src.getHeight() / (float) src.getWidth()) {
				if (src_x >= dst_x) {
					src_x -= src.getWidth() / 2;
				} else {
					src_x += src.getWidth() / 2;
				}
				src_y = (int) (k * src_x + b);
			} else {
				if (src_y >= dst_y) {
					src_y -= src.getHeight() / 2;
				} else {
					src_y += src.getHeight() / 2;
				}
				src_x = (int) ((src_y - b) / k);
			}
			
			if (Math.abs(k) < dst.getHeight() / (float) dst.getWidth()) {
				if (src_x >= dst_x) {
					dst_x += dst.getWidth() / 2;
				} else {
					dst_x -= dst.getWidth() / 2;
				}
				dst_y = (int) (k * dst_x + b);
			} else {
				if (src_y >= dst_y) {
					dst_y += dst.getHeight() / 2;
				} else {
					dst_y -= dst.getHeight() / 2;
				}
				dst_x = (int) ((dst_y - b) / k);
			}
			
		}
	}
	
	public Component getDst() {
		return dst;
	}
	
	public Component getSrc() {
		return src;
	}
	
	public float getLineWidth() {
		return LineWidth;
	}
	
	public void setLineWidth(float lineWidth) {
		LineWidth = lineWidth;
		stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}
	
	private static final Polygon polygon;
	
	static {
		int[] xs = new int[] {0, -10, -10};
		int[] ys = new int[] {-1, 6, -6};
		polygon = new Polygon(xs, ys, 3);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setStroke(stroke);
		g2.setColor(color);
		g2.drawLine(src_x, src_y, dst_x, dst_y);
		
		AffineTransform saveAT = g2.getTransform();
		AffineTransform transform = AffineTransform.getTranslateInstance(dst_x, dst_y);
		transform.rotate(dst_x - src_x, dst_y - src_y);
		g2.setTransform(transform);
		
		repaintArrow(g2);
		
		g2.setTransform(saveAT);
	}
	
	protected void repaintArrow(GraphicsX g2) {
		g2.draw(polygon);
	}
	
	@Override
	public int preferredWidth(Component c) {
		return 0;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 0;
	}
	
	public Stroke getStroke() {
		return stroke;
	}

}
