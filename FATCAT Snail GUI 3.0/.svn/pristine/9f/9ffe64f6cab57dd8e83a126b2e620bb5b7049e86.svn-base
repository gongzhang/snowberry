package fatcat.gui.snail;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.ComponentListener;
import fatcat.gui.snail.event.FocusListener;
import fatcat.gui.snail.event.KeyListener;
import fatcat.gui.snail.event.MouseListener;

public class Component extends ComponentArea implements ISkin {

	private boolean focusable, initialized;
	private ISkin skin;
	private String text;
	private boolean clip;
	private final Frame frame;
	private final static ComponentListener COMPONENT_LISTENER = new ComponentAdapter() {

		@Override
		public void componentInitialized(Component c) {
			c.initialized = true;
		}

		public void componentRemoved(Component c) {
			c.removeComponentListener(COMPONENT_LISTENER);
		}
	};

	Component(SnailShell shell) {
		super();
		initialized = false;
		frame = _getFrame();
		clip = false;
		focusable = true;
		skin = this;
		text = this.getClass().getSimpleName();
		setSize();
		addComponentListener(COMPONENT_LISTENER);
		shell.syncExec(new Runnable() {

			@Override
			public void run() {
				fireComponentInitialized();
			}
		});
	}

	public Component(final Container owner) {
		super(owner);
		initialized = false;
		owner.components.append(this);
		owner.checkThreadSafety();
		frame = _getFrame();
		clip = false;
		focusable = true;
		skin = this;
		text = this.getClass().getSimpleName();
		setSize();
		addComponentListener(COMPONENT_LISTENER);
		getShell().syncExec(new Runnable() {

			@Override
			public void run() {
				fireComponentInitialized();
				owner.fireChildAdded(Component.this);
			}
		});
	}

	public final boolean isInitialized() {
		return initialized;
	}

	@Override
	protected void repaint(GraphicsX g2) {

		if (clip) {
			frame.pushClip(g2, this);
		}
		final int left = getLeft();
		final int top = getTop();
		g2.translate(left, top);

		// // Incremental Painting ////
		if (incrementalPaint) {
			if (increpaint_flag) {
				repaintComponent(g2);
				increpaint_flag = false;
			} else {
				repaintChildren(g2);
			}
		} else {
			repaintComponent(g2);
		}

		g2.translate(-left, -top);
		if (clip) {
			frame.popClip(g2);
		}
	}

	void repaintChildren(GraphicsX gx) {}

	public final boolean isCurrentlyFocusable() {
		return isFocusable() && isVisible() && !isRemoved();
	}

	public final boolean isFocusable() {
		return focusable;
	}

	public final void setFocusable(boolean focusable) {
		this.focusable = focusable;
	}

	public void bringToTop() {
		getOwner().components.bringToTop(this);
		requestRepaint();
	}

	@Override
	public boolean isEnable() {
		if (this instanceof Frame)
			return super.isEnable();
		else
			return super.isEnable() && getOwner().isEnable();
	}

	@Override
	public boolean isVisible() {
		if (this instanceof Frame)
			return super.isVisible();
		else
			return super.isVisible() && getOwner().isVisible();
	}

	protected void repaintComponent(GraphicsX g2) {
		if (enableBuffer) {
			if (!repaint_flag && buf_w == getWidth() && buf_h == getHeight()) {
				g2.drawImage(buffer, 0, 0, buf_w, buf_h, 0, 0, buf_w, buf_h, null);
			} else {
				skin.repaintComponent(g2, this);
				final int w = getWidth();
				final int h = getHeight();
				final int l = getAbsLeft();
				final int t = getAbsTop();
				if (buffer == null || buffer.getWidth() < w || buffer.getHeight() < h) {
					if (buf_g != null)
						buf_g.dispose();
					buffer = (BufferedImage) getShell().getFramework().getAWTCanvas().createImage(Math.max(1, w),
							Math.max(1, h));
					buf_g = buffer.createGraphics();
				}
				buf_g.drawImage(getShell().getFramework().getCurrentBuffer(), 0, 0, w, h, l, t, l + w, t + h, null);
				repaint_flag = false;
				Rectangle rectangle = g2.getClipBounds();
				buf_w = Math.min(w, rectangle.width);
				buf_h = Math.min(h, rectangle.height);
			}
		} else {
			skin.repaintComponent(g2, this);
		}
	}

	@Override
	protected void update(double dt) {}

	public Frame getFrame() {
		return frame;
	}

	public final boolean isClip() {
		return clip;
	}

	public void setClip(boolean clip) {
		if (enableBuffer && (!clip)) {
			throw new UnsupportedOperationException("cannot execute \"setClip(false)\" when buffer is enable");
		}
		this.clip = clip;
	}

	public void remove() {
		getOwner().components.remove(this);
		getOwner().fireChildRemoved(this);
		fireComponentRemoved();
	}

	public boolean isRemoved() {
		return getOwner().indexOf(this) == -1;
	}

	public void setText(String text) {
		this.text = text == null ? "" : text;
		requestRepaint();
	}

	public String getText() {
		return text;
	}

	@Override
	public final int getPreferredHeight() {
		return skin.preferredHeight(this);
	}

	@Override
	public final int getPreferredWidth() {
		return skin.preferredWidth(this);
	}

	public final ISkin getSkin() {
		return skin;
	}

	public final void setSkin(ISkin skin) {
		this.skin = skin == null ? this : skin;
		requestRepaint();
	}

	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(new Color(0xffffff));
		g2.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		if (isFocus()) {
			g2.setColor(new Color(0xff0000));
		} else {
			g2.setColor(new Color(0xffcc00));
		}
		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setFont(new Font("Dialog", Font.PLAIN, 12));
		FontMetrics fontMetrics = g2.getFontMetrics();
		Rectangle2D rect = fontMetrics.getStringBounds(text, g2);
		g2.drawString(text, (int) (getWidth() - rect.getWidth()) / 2, (int) (getHeight() - rect.getHeight()) / 2
				+ (int) rect.getHeight());
	}

	@Override
	public int preferredHeight(Component c) {
		Graphics2D g2 = getShell().createGraphics();
		FontMetrics fm = g2.getFontMetrics();
		return fm.getHeight();
	}

	@Override
	public int preferredWidth(Component c) {
		Graphics2D g2 = getShell().createGraphics();
		FontMetrics fm = g2.getFontMetrics();
		return fm.stringWidth(text);
	}

	Frame _getFrame() {
		return getOwner()._getFrame();
	}

	public int getScreenWidth() {
		return frame.shell.getFramework().getWidth();
	}

	public int getScreenHeight() {
		return frame.shell.getFramework().getHeight();
	}

	public SnailShell getShell() {
		return getFrame().shell;
	}

	// component event

	private final LinkedList<ComponentListener> compoenntListeners = new LinkedList<ComponentListener>();

	public final void addComponentListener(ComponentListener listener) {
		compoenntListeners.add(listener);
	}

	public final void removeComponentListener(ComponentListener listener) {
		compoenntListeners.remove(listener);
	}

	protected final void fireComponentInitialized() {
		ComponentListener[] copy_list = compoenntListeners.toArray(new ComponentListener[0]);
		for (ComponentListener listener : copy_list)
			listener.componentInitialized(this);
	}

	protected final void fireComponentMoved() {
		ComponentListener[] copy_list = compoenntListeners.toArray(new ComponentListener[0]);
		for (ComponentListener listener : copy_list)
			listener.componentMoved(this);
	}

	protected final void fireComponentResized() {
		ComponentListener[] copy_list = compoenntListeners.toArray(new ComponentListener[0]);
		for (ComponentListener listener : copy_list)
			listener.componentResized(this);
	}

	protected final void fireComponentRemoved() {
		ComponentListener[] copy_list = compoenntListeners.toArray(new ComponentListener[0]);
		for (ComponentListener listener : copy_list)
			listener.componentRemoved(this);
	}

	@Override
	public void setLocation(int left, int top) {
		if (getLeft() != left || getTop() != top) {
			if (isLegalLocation(left, top)) {
				super.setLocation(left, top);
				fireComponentMoved();
				requestRepaint(true);
			} else {
				if (isLegalLeft(left)) {
					super.setLocation(left, getTop());
					fireComponentMoved();
					requestRepaint(true);
				}
				if (isLegalTop(top)) {
					super.setLocation(getLeft(), top);
					fireComponentMoved();
					requestRepaint(true);
				}
			}
		}
	}

	@Override
	public void setSize(int width, int height) {
		if (getWidth() != width || getHeight() != height) {
			if (isLegalSize(width, height)) {
				super.setSize(width, height);
				fireComponentResized();
				requestRepaint();
				requestRepaint(true);
			} else {
				if (isLegalWidth(width)) {
					super.setSize(width, getHeight());
					fireComponentResized();
					requestRepaint();
					requestRepaint(true);
				}
				if (isLegalHeight(height)) {
					super.setSize(getWidth(), height);
					fireComponentResized();
					requestRepaint();
					requestRepaint(true);
				}
			}
		}
	}

	protected boolean isLegalWidth(int width) {
		return true;
	}

	protected boolean isLegalHeight(int height) {
		return true;
	}

	protected boolean isLegalLeft(int left) {
		return true;
	}

	protected boolean isLegalTop(int top) {
		return true;
	}

	protected boolean isLegalSize(int width, int height) {
		return isLegalWidth(width) && isLegalHeight(height);
	}

	protected boolean isLegalLocation(int left, int top) {
		return isLegalLeft(left) && isLegalTop(top);
	}

	// mouse event

	final LinkedList<MouseListener> mouseListeners = new LinkedList<MouseListener>();

	public final void addMouseListener(MouseListener listener) {
		mouseListeners.add(listener);
	}

	public final void removeMouseListener(MouseListener listener) {
		mouseListeners.remove(listener);
	}

	public final void fireMouseClicked(MouseEvent e) {
		MouseListener[] copy_list = mouseListeners.toArray(new MouseListener[0]);
		for (MouseListener listener : copy_list)
			listener.mouseClicked(this, e);
	}

	public final void fireMouseEnered() {
		MouseListener[] copy_list = mouseListeners.toArray(new MouseListener[0]);
		for (MouseListener listener : copy_list)
			listener.mouseEntered(this);
	}

	public final void fireMouseExited() {
		MouseListener[] copy_list = mouseListeners.toArray(new MouseListener[0]);
		for (MouseListener listener : copy_list)
			listener.mouseExited(this);
	}

	public final void fireMouseWheelMoved(MouseWheelEvent e) {
		MouseListener[] copy_list = mouseListeners.toArray(new MouseListener[0]);
		for (MouseListener listener : copy_list)
			listener.mouseWheelMoved(this, e);
	}

	public final void fireMousePressed(MouseEvent e) {
		MouseListener[] copy_list = mouseListeners.toArray(new MouseListener[0]);
		for (MouseListener listener : copy_list)
			listener.mousePressed(this, e, e.getX() - getAbsLeft(), e.getY() - getAbsTop());
	}

	public final void fireMouseReleased(MouseEvent e) {
		MouseListener[] copy_list = mouseListeners.toArray(new MouseListener[0]);
		for (MouseListener listener : copy_list)
			listener.mouseReleased(this, e, e.getX() - getAbsLeft(), e.getY() - getAbsTop());
	}

	public final void fireMouseDragged(MouseEvent e) {
		MouseListener[] copy_list = mouseListeners.toArray(new MouseListener[0]);
		for (MouseListener listener : copy_list)
			listener.mouseDragged(this, e, e.getX() - getAbsLeft(), e.getY() - getAbsTop());
	}

	public final void fireMouseMoved(MouseEvent e) {
		MouseListener[] copy_list = mouseListeners.toArray(new MouseListener[0]);
		for (MouseListener listener : copy_list)
			listener.mouseMoved(this, e, e.getX() - getAbsLeft(), e.getY() - getAbsTop());
	}

	// focus event

	final LinkedList<FocusListener> focusListeners = new LinkedList<FocusListener>();

	public final void addFocusListener(FocusListener listener) {
		focusListeners.add(listener);
	}

	public final void removeFocusListener(FocusListener listener) {
		focusListeners.remove(listener);
	}

	protected final void fireGotFocus() {
		FocusListener[] copy_list = focusListeners.toArray(new FocusListener[0]);
		for (FocusListener listener : copy_list)
			listener.gotFocus(this);
	}

	protected final void fireLostFocus() {
		FocusListener[] copy_list = focusListeners.toArray(new FocusListener[0]);
		for (FocusListener listener : copy_list)
			listener.lostFocus(this);
	}

	public final boolean isFocus() {
		return getFrame().getFocus() == this;
	}

	public final boolean setFocus() {
		getFrame().setFocus(this);
		requestRepaint();
		return isFocus();
	}

	// key event

	final LinkedList<KeyListener> keyListeners = new LinkedList<KeyListener>();

	public final void addKeyListener(KeyListener listener) {
		keyListeners.add(listener);
	}

	public final void removeKeyListener(KeyListener listener) {
		keyListeners.remove(listener);
	}

	public final void fireKeyPressed(KeyEvent e) {
		KeyListener[] copy_list = keyListeners.toArray(new KeyListener[0]);
		for (KeyListener listener : copy_list)
			listener.keyPressed(this, e);
	}

	public final void fireKeyReleased(KeyEvent e) {
		KeyListener[] copy_list = keyListeners.toArray(new KeyListener[0]);
		for (KeyListener listener : copy_list)
			listener.keyReleased(this, e);
	}

	public final void fireKeyTyped(KeyEvent e) {
		KeyListener[] copy_list = keyListeners.toArray(new KeyListener[0]);
		for (KeyListener listener : copy_list)
			listener.keyTyped(this, e);
	}

	// // Request Repaint ////

	public void requestRepaint() {
		if (enableBuffer)
			repaint_flag = true;
		if (incrementalPaint)
			requestIncrementalRepaint2(def_repaint_owner);
	}

	public void requestRepaint(boolean repaint_owner) {
		if (enableBuffer)
			repaint_flag = true;
		if (incrementalPaint)
			requestIncrementalRepaint2(repaint_owner);
	}

	// // Buffer Painting ////

	boolean enableBuffer = false;
	BufferedImage buffer = null;
	Graphics buf_g = null;
	boolean repaint_flag = true;
	int buf_w, buf_h;

	public void enableBuffer() {
		if (!isClip()) {
			throw new UnsupportedOperationException("cannot enable buffer when \"isClip()\" is false");
		}
		enableBuffer = true;
		requestRepaint();
	}

	public void disableBuffer() {
		enableBuffer = false;
		if (buf_g != null) {
			buf_g.dispose();
			buf_g = null;
		}
		buffer = null;
	}

	// // Incremental Painting ////

	boolean incrementalPaint = false;
	boolean increpaint_flag = true;
	boolean def_repaint_owner = true;

	public void enableIncrementalPaint(boolean repaint_owner) {
		if (!getOwner().incrementalPaint) {
			throw new UnsupportedOperationException(
					"cannot enable incremental paint because its owner doesn't enable this feature");
		}
		def_repaint_owner = repaint_owner;
		incrementalPaint = true;
		requestIncrementalRepaint2();
	}

	public void disabelIncrementalPaint() {
		incrementalPaint = false;
	}

	@Deprecated
	public final void requestIncrementalRepaint() {
		requestIncrementalRepaint(def_repaint_owner);
	}

	final void requestIncrementalRepaint2() {
		requestIncrementalRepaint2(def_repaint_owner);
	}

	@Deprecated
	public void requestIncrementalRepaint(boolean repaint_owner) {
		if (repaint_owner) {
			if (!getOwner().increpaint_flag) {
				getOwner().requestIncrementalRepaint();
			}
		} else {
			increpaint_flag = true;
		}
	}

	void requestIncrementalRepaint2(boolean repaint_owner) {
		if (repaint_owner) {
			if (!getOwner().increpaint_flag) {
				getOwner().requestIncrementalRepaint2();
			}
		} else {
			increpaint_flag = true;
		}
	}

}
