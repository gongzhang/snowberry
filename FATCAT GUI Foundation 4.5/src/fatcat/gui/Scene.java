/**
 * @(#)Scene.java	5.0.0 2010-1-5
 *
 * FATCAT团队，北京航空航天大学，2009。版权所有，保留所有权利。
 * FATCAT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * (http://www.cnblogs.com/fatcat)
 */
package fatcat.gui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * FATCAT UI Framework中的顶层容器。
 * <p>
 * 在一个UI框架中，同一时刻只能显示一个<code>Scene</code>实例。
 * </p>
 * 
 * @author 张弓
 * @version 4.4 for SWT
 */
public abstract class Scene extends Container implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	private final GUIFramework fw;
	
	/**
	 * 创建一个属于框架<code>fw</code>的场景。
	 * <p>
	 * 注意，实例化的<code>Scene</code>只能由<code>fw</code>调用{@link UIFramework#show(Scene)}方法显示。
	 * </p>
	 * 
	 * @param fw 当前<code>Scene</code>所依附的框架实例
	 */
	public Scene(GUIFramework fw) {
		super();
		this.fw = fw;
	}
	
	/**
	 * 取得当前<code>Scene</code>所依附的框架实例。
	 * 
	 * @return <code>UIFramework</code>实例
	 */
	public final GUIFramework getFramework() {
		return fw;
	}
	
	/**
	 * 取得当前所处框架的宽度。
	 * 
	 * @return 宽度
	 */
	public final int getScreenWidth() {
		return fw.getWidth();
	}
	
	/**
	 * 取得当前所处框架的高度。
	 * 
	 * @return 高度
	 */
	public final int getScreenHeight() {
		return fw.getHeight();
	}
	
	@Override
	public void update(double dt) {
		updateScene(dt);
		super.update(dt);
	}
	
	@Override
	public void repaint(GraphicsX g2) {
		repaintScene(g2);
		super.repaint(g2);
	}
	
	public void show() {
//		fw.canvas.removeKeyListener(fw.scene);
//		fw.canvas.removeMouseListener(fw.scene);
//		fw.canvas.removeMouseMotionListener(fw.scene);
//		fw.canvas.removeMouseWheelListener(fw.scene);
		fw.scene.hidden();
		fw.scene = this;
		shown();
//		fw.canvas.addKeyListener(this);
//		fw.canvas.addMouseListener(this);
//		fw.canvas.addMouseMotionListener(this);
//		fw.canvas.addMouseWheelListener(this);
	}
	
	/**
	 * 场景更新的回调函数。
	 * <p>
	 * 此方法不应被用户调用。
	 * </p>
	 * 
	 * @param dt 两次调用之间的时间间隔，单位为毫秒。
	 */
	abstract protected void updateScene(double dt);
	
	/**
	 * 场景重绘的回调函数。
	 * <p>
	 * 此方法不应被用户调用。
	 * </p>
	 * 
	 * @param gx 绘图句柄
	 */
	abstract protected void repaintScene(GraphicsX gx);
	
	protected void hidden() {
	}
	
	protected void shown() {
	}
	
	protected void resized(int w, int h) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
	}

}

class EmptyScene extends Scene {
	
	public EmptyScene(GUIFramework fw) {
		super(fw);
	}
	
	@Override
	protected void repaintScene(GraphicsX g2) {
		g2.setColor(new Color(0xffffff));
		g2.fillRect(0, 0, getScreenWidth(), getScreenHeight());
	}
	
	@Override
	protected void updateScene(double dt) {
		
	}
	
}
