package fatcat.snowberry.gui;

import java.awt.AlphaComposite;
import fatcat.gui.GraphicsX;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.ContainerListener;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.gui.util.QInterpolator;


public class ScrollPanel extends Container implements MouseWheelListener {
	
	private final Container clientContainer;
	private final ScrollBar scrollBar;

	public ScrollPanel(Container owner) {
		super(owner);
		setClip(true);
		clientContainer = new Container(this) {
			
			@Override
			protected boolean isLegalTop(int top) {
				return top + getHeight() >= ScrollPanel.this.getHeight() && top <= 0;
			}
			
			@Override
			public void repaintComponent(GraphicsX g2, Component c) {
			}
			
			@Override
			public int preferredWidth(Component c) {
				return ScrollPanel.this.getWidth();
			}
			
		};
		clientContainer.addComponentListener(new ComponentAdapter() {
			
			private int old_height = 0;
			
			@Override
			public void componentResized(Component c) {
				fireScrollChanged();
				if (old_height != c.getHeight()) {
					old_height = c.getHeight();
					int d = ScrollPanel.this.getHeight() - clientContainer.getHeight();
					if (d >= 0) clientContainer.setTop(0);
					else if (clientContainer.getBottomSpace() > 0) clientContainer.setTop(d);
				}
				
				
			}
			
			@Override
			public void componentMoved(Component c) {
				fireScrollChanged();
			}
			
		});
		clientContainer.setSkin(null);
		scrollBar = new ScrollBar(this);
		
		scrollBar.setLeft(getWidth() - 10);
		scrollBar.setTop(5);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(Component c) {
				int d = c.getHeight() - clientContainer.getHeight();
				if (d >= 0) clientContainer.setTop(0);
				else if (clientContainer.getBottomSpace() > 0) clientContainer.setTop(d);
			}
		});
		
		new MouseWheelBinding(this, this);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		fireScrollChanged();
	}
	
	public Container getClientContainer() {
		return clientContainer;
	}
	
	@Override
	public void refreshLayout(Container c) {
		if (clientContainer != null && scrollBar != null) {
			clientContainer.setWidth(c);
			scrollBar.setLeft(getWidth() - 10);
			scrollBar.setHeight(getHeight() - 10);
		}
	}
	
	private void fireScrollChanged() {
		if (clientContainer != null) {
			scrollChanged(getHeight(), clientContainer.getHeight(), -clientContainer.getTop());
		}
	}
	
	protected void scrollChanged(int slider_length, int length, int current) {
		scrollBar.show();
		scrollBar.current = current;
		scrollBar.slider_length = slider_length;
		scrollBar.length = length;
	}
	
	public static final BufferedImage IMG_DIAGRAM_CONTENT_MASK = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Content.Mask.png");
	
	private boolean showGradient = true;
	
	public void setGradientVisible(boolean value) {
		showGradient = value;
	}
	
	@Override
	protected void repaint(GraphicsX g2) {
		super.repaint(g2);
		if (showGradient) {
			g2.drawImage(IMG_DIAGRAM_CONTENT_MASK, getLeft(), getTop(), getRight(), getTop() + 4, 0, 0, 64, 4, null);
			g2.drawImage(IMG_DIAGRAM_CONTENT_MASK, getLeft(), getBottom(), getRight(), getBottom() - 4, 0, 0, 64, 4, null);
		}
	}
	
	private QInterpolator animation = null;
	private int dst_top = 0;

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (clientContainer.getHeight() <= getHeight()) return;
		dst_top += e.getWheelRotation() * 64;
		if (dst_top < 0) dst_top = 0;
		else if (dst_top > clientContainer.getHeight() - getHeight()) dst_top = clientContainer.getHeight() - getHeight();
		animation = new QInterpolator(
			clientContainer.getTop(),
			-dst_top,
			0.5
		);
	}
	
	@Override
	protected void update(double dt) {
		super.update(dt);
		if (animation != null) {
			if (!animation.hasDone()) {
				clientContainer.setTop((int)(animation.update(dt)));
			} else {
				clientContainer.setTop((int)(animation.endValue));
				animation = null;
			}
		}
	}
	
}

class MouseWheelBinding {
	
	MouseWheelBinding(final ScrollPanel host, final Container c) {
		c.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseWheelMoved(Component c, MouseWheelEvent e) {
				host.mouseWheelMoved(e);
			}
			
		});
		c.addContainerListener(new ContainerListener() {
			
			@Override
			public void childRemoved(Container parent, Component c) {
				
			}
			
			@Override
			public void childAdded(Container parent, Component c) {
				if (c instanceof Container) {
					new MouseWheelBinding(host, ((Container) c));
				} else {
					c.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseWheelMoved(Component c, MouseWheelEvent e) {
							host.mouseWheelMoved(e);
						}
						
					});
				}
			}
			
		});
	}
	
}

class ScrollBar extends Component {
	
	int slider_length;
	int length;
	int current;

	public ScrollBar(ScrollPanel owner) {
		super(owner);
		addMouseListener(new MouseAdapter() {
			
			private boolean pressed_on_slider;
			private int old_y, old_top;
			
			@Override
			public void mousePressed(Component c, MouseEvent e, int x, int y) {
				final int h = getHeight() * slider_length / length;
				final int top = getHeight() * current / length;
				if (y >= top && y <= top + h) {
					old_top = ((ScrollPanel) getOwner()).getClientContainer().getTop();
					old_y = y;
					pressed_on_slider = true;
				}
			}
			
			@Override
			public void mouseReleased(Component c, MouseEvent e, int x, int y) {
				pressed_on_slider = false;
			}
			
			@Override
			public void mouseDragged(Component c, MouseEvent e, int x, int y) {
				if (pressed_on_slider) {
					final Container co = ((ScrollPanel) getOwner()).getClientContainer();
					final int dy = (y - old_y) * length / getHeight();
					co.setTop(old_top - dy);
				}
			}
			
			@Override
			public void mouseEntered(Component c) {
				show();
			}
			
			@Override
			public void mouseExited(Component c) {
				hide();
			}
		});
	}
	
	@Override
	public int preferredWidth(Component c) {
		return 5;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return getOwner().getHeight() - 10;
	}
	
	//// paint ////
	
	public void show() {
		img_state = IMG_STATE_SHOWWING;
	}
	
	public void hide() {
		img_state = IMG_STATE_HIDDING;
	}
	
	private int img_state = IMG_STATE_HIDDEN;
	private static final int IMG_STATE_HIDDEN = 0;
	private static final int IMG_STATE_SHOWWING = 1;
	private static final int IMG_STATE_SHOWN = 2;
	private static final int IMG_STATE_HIDDING = 3;
	private float img_alpha = 0.0f;
	private double img_timer = 0.0;
	
	public static final BufferedImage IMG_SCROLLBAR_BG = GraphicsX.createImage("/fatcat/snowberry/gui/res/ScrollBar.Background.png");
	public static final BufferedImage IMG_SCROLLBAR_SLIDER = GraphicsX.createImage("/fatcat/snowberry/gui/res/ScrollBar.Slider.png");
	
	@Override
	protected void update(double dt) {
		super.update(dt);
		switch (img_state) {
		case IMG_STATE_SHOWWING:
			img_alpha += dt * 6.0;
			if (img_alpha >= 1.0f) {
				img_alpha = 1.0f;
				img_state = IMG_STATE_SHOWN;
				img_timer = 0.0;
			}
			break;
		case IMG_STATE_HIDDING:
			img_alpha -= dt * 3.0;
			if (img_alpha <= 0.0f) {
				img_alpha = 0.0f;
				img_state = IMG_STATE_HIDDEN;
			}
			break;
		case IMG_STATE_SHOWN:
			img_timer += dt;
			if (img_timer > 0.5) {
				img_state = IMG_STATE_HIDDING;
			}
		}
		if (slider_length >= length) setVisible(false);
		else setVisible(true);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, img_alpha));
		g2.drawImage(IMG_SCROLLBAR_BG, 0, 0, getWidth(), getHeight(), null);
		length = length == 0 ? 1 : length;
		final int h = getHeight() * slider_length / length;
		final int top = getHeight() * current / length;
		g2.drawImage(IMG_SCROLLBAR_SLIDER, -1, top - 2, 6, top, 0, 0, 8, 1, null);
		g2.drawImage(IMG_SCROLLBAR_SLIDER, -1, top, 6, top + h, 0, 2, 8, 37, null);
		g2.drawImage(IMG_SCROLLBAR_SLIDER, -1, top + h, 6, top + h + 2, 0, 38, 8, 39, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}
	
}
