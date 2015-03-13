package fatcat.gui.snail;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

import fatcat.gui.GraphicsX;
import fatcat.gui.Scene;
import fatcat.gui.snail.event.FrameListener;

public class Frame extends Container {

	final SnailShell shell;
	final Scene scene;
	private Component mouseOnComponent = null, focusedComponent = null;

	public Frame(final SnailShell shell) {
		super(shell);
		this.shell = shell;
		setSize();
		scene = new Scene(shell.getFramework()) {

			@Override
			protected void updateScene(double dt) {
				Frame.this.update(dt);
			}

			@Override
			protected void repaintScene(GraphicsX g2) {
				Frame.this.repaint(g2);
			}

			@Override
			protected void shown() {
				Frame.this.fireFrameShown();
			};

			@Override
			protected void hidden() {
				Frame.this.fireFrameHidden();
			}

			@Override
			protected void resized(int w, int h) {
				Frame.this.setSize(w, h);
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.keyPressed(e);
						if (focusedComponent != null)
							focusedComponent.fireKeyPressed(e);
					}
				});

			}

			@Override
			public void keyReleased(final KeyEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.keyReleased(e);
						if (focusedComponent != null)
							focusedComponent.fireKeyReleased(e);
					}
				});

			}

			@Override
			public void keyTyped(final KeyEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.keyTyped(e);
						if (focusedComponent != null)
							focusedComponent.fireKeyTyped(e);
					}
				});

			}

			@Override
			public void mouseClicked(final MouseEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.mouseClicked(e);
						if (mouseOnComponent != null)
							mouseOnComponent.fireMouseClicked(e);
					}
				});

			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.mouseEntered(e);
						Component c = getComponentOn(e.getX() - getLeft(), e.getY() - getTop());
						setMouseOnComponent(c);
					}
				});

			}

			@Override
			public void mouseExited(final MouseEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.mouseExited(e);
						setMouseOnComponent(null);
					}
				});

			}

			@Override
			public void mousePressed(final MouseEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.mousePressed(e);

						Component c = getComponentOn(e.getX() - getLeft(), e.getY() - getTop());
						setMouseOnComponent(c);

						if (mouseOnComponent != null) {
							setFocus(mouseOnComponent);
							mouseOnComponent.fireMousePressed(e);
						}
					}
				});

			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.mouseReleased(e);
						if (mouseOnComponent != null)
							mouseOnComponent.fireMouseReleased(e);
					}
				});

			}

			@Override
			public void mouseDragged(final MouseEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.mouseDragged(e);
						if (mouseOnComponent != null)
							mouseOnComponent.fireMouseDragged(e);
					}
				});

			}

			@Override
			public void mouseMoved(final MouseEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.mouseMoved(e);
						Component c = getComponentOn(e.getX() - getLeft(), e.getY() - getTop());
						setMouseOnComponent(c);
						if (mouseOnComponent != null)
							mouseOnComponent.fireMouseMoved(e);
					}
				});
			}

			@Override
			public void mouseWheelMoved(final MouseWheelEvent e) {
				shell.syncExec(new Runnable() {

					@Override
					public void run() {
						Frame.this.mouseWheelMoved(e);
						if (mouseOnComponent != null)
							mouseOnComponent.fireMouseWheelMoved(e);
					}
				});

			}

		};
	}

	LinkedList<Rectangle> clips = new LinkedList<Rectangle>();

	@Override
	protected void repaint(GraphicsX g2) {
		g2.setClip(0, 0, getScreenWidth(), getScreenHeight());
		super.repaint(g2);
	}

	public final void show() {
		shell.syncExec(new Runnable() {

			@Override
			public void run() {
				shell.show(Frame.this);
			}
		});
	}

	public final Scene getScene() {
		return scene;
	}

	@Override
	public final int getAbsLeft() {
		return getLeft();
	}

	@Override
	public final int getAbsTop() {
		return getTop();
	}

	public final Frame getFrame() {
		return this;
	}

	@Override
	final Frame _getFrame() {
		return this;
	}

	void pushClip(Graphics2D g2, Rectangle rect) {
		Rectangle clip = g2.getClipBounds();
		clips.addLast(clip);
		final int new_left = java.lang.Math.max(clip.x, rect.x);
		final int new_top = java.lang.Math.max(clip.y, rect.y);
		g2.setClip(new_left, new_top, java.lang.Math.min(clip.x + clip.width, rect.x + rect.width) - new_left, java.lang.Math
				.min(clip.y + clip.height, rect.y + rect.height)
				- new_top);
	}

	void pushClip(Graphics2D g2, IArea area) {
		Rectangle clip = g2.getClipBounds();
		clips.addLast(clip);
		final int new_left = java.lang.Math.max(clip.x, area.getLeft());
		final int new_top = java.lang.Math.max(clip.y, area.getTop());
		g2.setClip(new_left, new_top, java.lang.Math.min(clip.x + clip.width, area.getLeft() + area.getWidth()) - new_left,
				java.lang.Math.min(clip.y + clip.height, area.getTop() + area.getHeight()) - new_top);
	}

	void popClip(Graphics2D g2) {
		Rectangle clip = clips.removeLast();
		g2.setClip(clip);
	}

	@Override
	public int preferredWidth(Component c) {
		return getScreenWidth();
	}

	@Override
	public int preferredHeight(Component c) {
		return getScreenHeight();
	}

	public int getScreenWidth() {
		return shell != null ? shell.getFramework().getWidth() : 0;
	}

	public int getScreenHeight() {
		return shell != null ? shell.getFramework().getHeight() : 0;
	}

	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(new Color(0x0));
		g2.drawLine(0, 0, 100, 100);
		super.repaintComponent(g2, c);
	}

	@Override
	public final SnailShell getShell() {
		return shell;
	}

	// frame event

	private final LinkedList<FrameListener> frameListeners = new LinkedList<FrameListener>();

	public final void addFrameListener(FrameListener listener) {
		frameListeners.add(listener);
	}

	public final void removeFrameListener(FrameListener listener) {
		frameListeners.remove(listener);
	}

	protected final void fireFrameShown() {
		FrameListener[] copy_list = frameListeners.toArray(new FrameListener[0]);
		for (FrameListener listener : copy_list)
			listener.frameShown(this);
	}

	protected final void fireFrameHidden() {
		FrameListener[] copy_list = frameListeners.toArray(new FrameListener[0]);
		for (FrameListener listener : copy_list)
			listener.frameHidden(this);
	}

	// mouse event

	void setMouseOnComponent(Component c) {
		if (mouseOnComponent != c) {
			if (mouseOnComponent != null)
				mouseOnComponent.fireMouseExited();
			mouseOnComponent = c;
			if (mouseOnComponent != null)
				mouseOnComponent.fireMouseEnered();
		}
	}

	// focus event

	public final Component getFocus() {
		return focusedComponent;
	}

	public final void setFocus(Component c) {
		if (c != null && !c.isCurrentlyFocusable())
			return;
		if (focusedComponent != c) {
			if (focusedComponent != null)
				focusedComponent.fireLostFocus();
			focusedComponent = c;
			if (focusedComponent != null)
				focusedComponent.fireGotFocus();
			focusChanged();
		}
	}

	@Override
	public boolean isRemoved() {
		return false;
	}

	public boolean isShown() {
		return getShell().getFramework().getCurrentScene() == scene;
	}

	protected void focusChanged() {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public void mouseWheelMoved(MouseWheelEvent e) {}

	protected final void keyPressed(KeyEvent e) {}

	protected final void keyReleased(KeyEvent e) {}

	protected final void keyTyped(KeyEvent e) {}

	// //Incremental Painting ////

	public void enableIncrementalPaint2(boolean repaint_owner) {
		enableIncrementalPaint();
	}

	public void enableIncrementalPaint() {
		def_repaint_owner = false;
		incrementalPaint = true;
		requestIncrementalRepaint2();
	}

	public void requestIncrementalRepaint2(boolean repaint_owner) {
		super.requestIncrementalRepaint2(false);
	}

}
