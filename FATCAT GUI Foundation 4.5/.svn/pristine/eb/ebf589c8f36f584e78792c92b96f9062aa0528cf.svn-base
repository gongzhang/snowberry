/**
 * @(#)Component.java	4.12 2009-9-2
 *
 * FATCAT团队，北京航空航天大学，2009。版权所有，保留所有权利。
 * FATCAT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * (http://www.cnblogs.com/fatcat)
 */
package fatcat.gui;


/**
 * FATCAT J2ME UI Framework框架中的原子组件。
 * <p>
 * 每个组件都可以访问当前屏幕并进行绘图操作，并拥有自己的逻辑代码。
 * @author 张弓
 * @version 4.12
 */
abstract public class Component {

	private boolean enable;
	private boolean visible;
	private Container owner;

	/**
	 * 对组件进行初始化。
	 */
	protected Component() {
		enable = true;
		visible = true;
		owner = null;
	}
	
	public Container getContainer() {
		return owner;
	}
	
	void setContainer(Container owner) {
		this.owner = owner;
	}

	/**
	 * 设定组件是否有效。
	 * 只有当组件有效时，框架才会调用<code>update(long dt)</code>方法更新组件。
	 * @param enable 组件是否有效
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * 取得组件是否有效。
	 * @return 组件是否有效
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * 设定组件是否可见。
	 * 只有当组件可见时，系统才会调用<code>repaint(Graphics g)</code>方法绘制组件。
	 * @param visible 组件是否可见
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * 取得组件是否可见。
	 * @return 组件是否可见
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * 框架将不断调用此方法进行屏幕重绘。重绘频率即为FPS(Frame per Second)，与手机性能与绘图复杂程度有关。
	 * 可以通过<code>UIFramework.setMaxFPS(int fps)</code>方法限制最大FPS。
	 * @param gc 当前屏幕缓冲器的绘图句柄
	 */
	abstract protected void repaint(GraphicsX gx);

	/**
	 * 框架将不断调用此方法进行组件数据更新工作。参数<code>dt</code>通常用于时间控制。
	 * @param dt 两次调用之间的时间间隔，单位为秒
	 */
	abstract protected void update(double dt);

}
