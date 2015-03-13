package fatcat.snowberry.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import fatcat.gui.GraphicsX;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.ContainerListener;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.gui.util.Iterator;
import fatcat.snowberry.gui.util.QInterpolator;


public class CategoryPanel extends Container {
	
	public static final BufferedImage IMG_UNFOLDING = GraphicsX.createImage("/fatcat/snowberry/gui/res/CategoryPanel.Unfolding.png");
	public static final BufferedImage IMG_FOLDING = GraphicsX.createImage("/fatcat/snowberry/gui/res/CategoryPanel.Folding.png");
	
	public static final int COLOR_WHITE = 0; // DO NOT change the values !
	public static final int COLOR_GRAY = 1;
	public static final int COLOR_RED = 2;
	public static final int COLOR_YELLOW = 3;
	public static final int COLOR_GREEN = 4;
	public static final int COLOR_BLUE = 5;
	public static final int COLOR_PURPLE = 6;
	
	private int color;
	private Font font;
	private boolean expanded;
	
//	private final LinkedList<StandardItem> items;

	public CategoryPanel(final StandardContent owner, String text, int color) {
		super(owner);
//		items = new LinkedList<StandardItem>();
		setClip(true);
		setText(text);
		setColor(color);
		setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
		expanded = true;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(Component c, MouseEvent e, int x, int y) {
				if (expanded) collapse();
				else expand();
			}
		});
		addContainerListener(new ContainerListener() {
			@Override
			public void childRemoved(Container parent, Component c) {
//				if (c instanceof StandardItem) {
//					items.remove((StandardItem) c);
//				}
				setSize();
			}
			
			@Override
			public void childAdded(Container parent, Component c) {
//				if (c instanceof StandardItem) {
//					items.add((StandardItem) c);
//				}
				setSize();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(Component c) {
				owner.doLayout();
			}
		});
	}
	
	public int getColor() {
		return color;
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	public final void expand() {
		if (!expanded) {
			expanded = true;
			animation = new QInterpolator(getHeight(), getPreferredHeight(), 0.3);
		}
	}
	
	public final void collapse() {
		if (expanded) {
			expanded = false;
			animation = new QInterpolator(getHeight(), 15, 0.3);
		}
	}
	
	public boolean isCollapse() {
		return !expanded;
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		setColor(color);
	}
	
	private GradientPaint gradient;
	
	public void setColor(int color) {
		this.color = color;
		Color awt_color;
		switch (color) {
		case COLOR_RED:
			awt_color = new Color(0xf40e4a);
			break;
		case COLOR_YELLOW:
			awt_color = new Color(0xefba04);
			break;
		case COLOR_GREEN:
			awt_color = new Color(0x31ae10);
			break;
		case COLOR_BLUE:
			awt_color = new Color(0x0967c7);
			break;
		case COLOR_PURPLE:
			awt_color = new Color(0x9903d7);
			break;
		default:
			awt_color = new Color(0x8f8f8f);
			break;
		}
		gradient = new GradientPaint(new Point(0, 0), awt_color, new Point(getWidth() - 1, 0), Color.white);
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return expanded ? (preferred_hieght < 15 ? 15 : preferred_hieght) : 15;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return getOwner().getWidth();
	}
	
	private int preferred_hieght = 15;
	
	@Override
	public void refreshLayout(Container container) {
		int top = 15;
		Iterator iterator = iterator();
		for (; iterator.hasNext();) {
			final Component c = (Component) iterator.next();
			c.setRight(getWidth());
			c.setHeight(c.getPreferredHeight());
			c.setLocation(0, top);
			top += c.getHeight();
		}
		preferred_hieght = top;
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setPaint(gradient);
		g2.fillRect(0, 0, getWidth(), 15);
		g2.setColor(Color.white);
		g2.setFont(font);
		g2.drawString(getText(), 15, 1 + font.getSize());
		g2.drawImage(expanded ? IMG_FOLDING : IMG_UNFOLDING, 2, 2, null);
	}
	
	private QInterpolator animation = null;
	
	@Override
	protected void update(double dt) {
		super.update(dt);
		if (animation != null) {
			if (!animation.hasDone()) {
				setHeight((int)animation.update(dt));
			} else {
				setHeight((int)animation.endValue);
				animation = null;
			}
		}
	}
	
	public final void clearSelection() {
		Component[] cs = getComponents();
		for (Component c : cs) {
			if (c instanceof StandardItem) {
				((StandardItem) c).setSelected(false);
			}
		}
//		for (StandardItem item : items) {
//			 item.setSelected(false);
//		}
	}
	
	public final StandardItem[] getItems() {
		Component[] cs = getComponents();
		LinkedList<StandardItem> rst = new LinkedList<StandardItem>();
		for (Component c : cs) {
			if (c instanceof StandardItem) {
				rst.add((StandardItem) c);
			}
		}
		return rst.toArray(new StandardItem[0]);
	}
	
	public final StandardItem[] getSelection() {
		Component[] cs = getComponents();
		LinkedList<StandardItem> rst = new LinkedList<StandardItem>();
		for (Component c : cs) {
			if ((c instanceof StandardItem) && ((StandardItem) c).isSelected()) {
				rst.add((StandardItem) c);
			}
		}
		return rst.toArray(new StandardItem[0]);
	}

}
