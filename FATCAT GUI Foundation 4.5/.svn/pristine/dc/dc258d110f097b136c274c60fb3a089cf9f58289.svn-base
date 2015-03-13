package fatcat.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import fatcat.gui.util.Iterator;

final public class GUIFramework {

	final GUICanvas canvas;
	Scene scene;
	final int max_fps;
	GXConfiguration config;
	
	public GUIFramework(final Container parent) {
		this(parent, 60, null);
	}
	
	public GUIFramework(final Container parent, GXConfiguration config) {
		this(parent, 60, config);
	}
	
	public GUIFramework(final Container parent, int max_fps) {
		this(parent, max_fps, null);
	}

	public GUIFramework(final Container parent, int max_fps, GXConfiguration config) {
		this.max_fps = max_fps;
		this.config = config;
		System.setProperty("sun.awt.noerasebackground", "true");
		scene = new EmptyScene(this);
		canvas = new GUICanvas(this);
		parent.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				if (isRunning()) scene.keyTyped(arg0);
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (isRunning()) scene.keyReleased(arg0);
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (isRunning()) scene.keyPressed(arg0);
			}
		});
		parent.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (isRunning()) scene.mouseReleased(arg0);
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (isRunning()) scene.mousePressed(arg0);
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				if (isRunning()) scene.mouseExited(arg0);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (isRunning()) scene.mouseEntered(arg0);
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isRunning()) scene.mouseClicked(arg0);
			}
		});
		parent.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				if (isRunning()) scene.mouseMoved(arg0);
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (isRunning()) scene.mouseDragged(arg0);
			}
		});
		parent.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if (isRunning()) scene.mouseWheelMoved(arg0);
			}
		});
		parent.setLayout(new BorderLayout());
		parent.add(canvas, BorderLayout.CENTER);
		scene.shown();
	}
	
	public int getFPS() {
		return canvas.thread.fps;
	}
	
	public boolean isRunning() {
		return canvas.isRunning();
	}
	
	public void restart() {
		canvas.restart();
	}
	
	public void stop() {
		canvas.stop();
	}
	
	public javax.swing.JPanel getAWTCanvas() {
		return canvas;
	}
	
	public int getWidth() {
		return canvas.getWidth();
	}
	
	public int getHeight() {
		return canvas.getHeight();
	}
	
	public void syncExec(Runnable runnable) {
		canvas.thread.runnables.append(runnable);
	}
	
	private static BufferedImage COMMON_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	
	public Graphics2D createGraphics() {
		return COMMON_IMAGE.createGraphics();
	}
	
	public Scene getCurrentScene() {
		return scene;
	}
	
	public BufferedImage getCurrentBuffer() {
		return canvas.buffer2;
	}
	
	public void setSuspendRepaint(boolean isSuspended) {
		canvas.thread.suspend_paint = isSuspended;
	}
	
	public boolean isSuspendRepaint() {
		return canvas.thread.suspend_paint;
	}
	
	public boolean isThreadSafe() {
		return canvas.thread.thread_safe;
	}

}

class GUIThread implements Runnable {

	private final GUICanvas canvas;
	long t0, t, dt;
	boolean stop_flag;
	int fps;
	final int max_fps;
	final fatcat.gui.util.LinkedList runnables;
	
	boolean suspend_paint;
	boolean thread_safe = false;

	GUIThread(GUICanvas canvas, int max_fps) {
		this.canvas = canvas;
		this.max_fps = max_fps;
		stop_flag = false;
		suspend_paint = false;
		runnables = new fatcat.gui.util.LinkedList();
	}

	@Override
	public void run() {
		t0 = System.currentTimeMillis();
		GraphicsX gx = null;
		try {
			while (!stop_flag) {
				t = System.currentTimeMillis();
				if ((dt = t - t0) * max_fps > 1000L) {
					fps = (int) (1000L / dt);
					
					thread_safe = true;
					
					update(dt / 1000.0);
					
					
					for (Iterator it = runnables.iterator(); it.hasNext();) {
						((Runnable) it.next()).run();
					}
					runnables.removeAll();
					
					thread_safe = false;
					
					if (canvas.buf2_g != null && !suspend_paint) {
						
						// 更新句柄
						if (gx == null || gx.getGraphics2D() != canvas.buf2_g) {
							gx = new GraphicsX(canvas.buf2_g);
							if (canvas.framework.config != null) {
								canvas.framework.config.initConfiguration(gx);
							}
						}

						repaint(gx);
						
						if (canvas.buf_g != null) {
							canvas.buf_g.drawImage(canvas.buffer2, 0, 0, null);
						}
						
						canvas.repaint();
					}
					
					Thread.yield();
					t0 = t;
				} else {
					Thread.sleep(1000L / max_fps - dt);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			stop_flag = true;
			thread_safe = false;
		}
	}
	
	void start() {
		stop_flag = false;
	}
	
	void stop() {
		stop_flag = true;
	}
	
	void update(double dt) {
		canvas.framework.scene.update(dt);
	}
	
	void repaint(GraphicsX g2) {
		canvas.framework.scene.repaint(g2);
	}

}

class GUICanvas extends javax.swing.JPanel implements ComponentListener {

	private static final long serialVersionUID = -7979360289037330914L;

	final GUIFramework framework;
	BufferedImage buffer, buffer2;
	Graphics2D buf_g, buf2_g;
	final GUIThread thread;

	GUICanvas(GUIFramework framework) {
		super();
		this.framework = framework;
		this.addComponentListener(this);
		thread = new GUIThread(this, framework.max_fps);
		new Thread(thread).start();
	}
	
	boolean isRunning() {
		return !thread.stop_flag;
	}
	
	void restart() {
		this.addComponentListener(this);
		resizeBuffer(getWidth(), getHeight());
		thread.start();
		new Thread(thread).start();
	}
	
	void stop() {
		thread.stop();
		this.removeComponentListener(this);
	}

	@Override
	public boolean isDoubleBuffered() {
		return true;
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (buffer != null && !thread.stop_flag) {
			g.drawImage(buffer, 0, 0, null);
		} else {
			super.paintComponent(g);
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}
	
	@Override
	public void componentResized(ComponentEvent e) {
		resizeBuffer(getWidth(), getHeight());
	}
	
	private void resizeBuffer(final int w, final int h) {
		buf_g = buf2_g = null;
		buffer = (BufferedImage) this.createImage(w > 0 ? w : 1, h > 0 ? h : 1); // TODO （低优先）调整大小时有闪烁问题
		buffer2 = (BufferedImage) this.createImage(w > 0 ? w : 1, h > 0 ? h : 1);
		buffer.setAccelerationPriority(0.0f);
		buf_g = buffer.createGraphics();
		buffer2.setAccelerationPriority(1.0f);
		framework.scene.resized(w, h);
		buf2_g = buffer2.createGraphics();
	}

	@Override
	public void componentShown(ComponentEvent e) {}

}
