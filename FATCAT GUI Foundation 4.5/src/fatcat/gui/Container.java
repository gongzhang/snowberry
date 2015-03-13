/**
 * @(#)Container.java	4.12 2009-9-2
 *
 * FATCAT团队，北京航空航天大学，2009。版权所有，保留所有权利。
 * FATCAT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * (http://www.cnblogs.com/fatcat)
 */
package fatcat.gui;


import java.util.Arrays;
import java.util.Comparator;

import fatcat.gui.util.Iterator;
import fatcat.gui.util.LinkedList;


/**
 * FATCAT J2ME UI Framework框架中的通用容器。
 * @author 张弓
 * @version 4.12
 */
public class Container extends Component {

	private final LinkedList components;

	/**
	 * 创建一个容器对象。
	 */
	public Container() {
		components = new LinkedList();
	}

	/**
	 * 向容器中追加一个组件。
	 * @param c 要添加的组件
	 * @return 追加后的子组件个数
	 */
	public final int append(final Component c) {
		if (c.getContainer() != null)
			throw new IllegalArgumentException("the component already has a container");
		c.setContainer(this);
		return components.append(c);
	}

	/**
	 * 取得组件<code>c</code>在容器中的索引。
	 * @param c 要查询的组件
	 * @return <code>c</code>在容器中的索引
	 */
	public final int indexOf(final Component c) {
		return components.indexOf(c);
	}

	/**
	 * 在组件<code>before</code>之前插入新组件<code>data</code>。
	 * @param data 要插入的组件
	 * @param before 容器中已存在的组件
	 * @return 是否成功插入
	 */
	public final boolean insert(final Component data, final Component before) {
		return components.insert(data, before);
	}

	/**
	 * 删除容器中的组件<code>data</code>。
	 * @param c 要删除的组件
	 * @return 是否成功删除
	 */
	public final void remove(final Component c) {
		int index = indexOf(c);
		if (index == -1)
			throw new IllegalArgumentException("the component is not in this container");
		c.setContainer(null);
		components.remove(index);
	}

	/**
	 * 删除容器中的指定组件。
	 * @param index 要删除组件的索引。
	 * @return 是否成功删除。
	 */
	public final void remove(int index) {
		remove(get(index));
	}
	
	public final void removeAll() {
		components.removeAll();
	}

	/**
	 * 将指定组件移至容器顶层。
	 * @param c 要移动的组件
	 */
	public final void bringToTop(final Component c) {
		if (components.remove(c)) {
			components.append(c);
		}
	}

	/**
	 * 根据索引取得容器中的组件。
	 * @param index 组件索引
	 * @return 索引所表示的组件
	 */
	public final Component get(int index) {
		return (Component) components.get(index);
	}

	/**
	 * 取得此容器的迭代器。
	 * @return 此容器的迭代器
	 */
	public final Iterator iterator() {
		return components.iterator();
	}

	/**
	 * 取得容器中子组件的个数。
	 * @return 子组件的个数
	 */
	public final int size() {
		return components.size();
	}
	
	public void sortChildren(Comparator<Component> comparator) {
		Component[] cs = new Component[size()];
		Iterator it = iterator();
		for (int i = 0; it.hasNext(); i++) {
			cs[i] = (Component) it.next(); 
		}
		Arrays.sort(cs, comparator);
		components.removeAll();
		for (int i = 0; i < cs.length; i++) {
			components.append(cs[i]);
		}
	}

	public void repaint(GraphicsX gx) {
		for (Iterator it = components.iterator(); it.hasNext();) {
			Component c = (Component) it.next();
			if (c.isVisible()) {
				c.repaint(gx);
			}
		}
	}

	public void update(double dt) {
		for (Iterator it = components.iterator(); it.hasNext();) {
			Component c = (Component) it.next();
			if (c.isEnable()) {
				c.update(dt);
			}
		}
	}

	
}
