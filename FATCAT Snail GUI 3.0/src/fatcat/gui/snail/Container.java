package fatcat.gui.snail;

import java.util.Comparator;
import java.util.LinkedList;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.ContainerListener;
import fatcat.gui.util.Iterator;

public class Container extends Component implements ILayout {

	final fatcat.gui.Container components;
	private ILayout layout;
	private final static ComponentAdapter COMPONENT_ADAPTER = new ComponentAdapter() {

		@Override
		public void componentInitialized(Component c) {
			((Container) c).doLayout();
		}

		@Override
		public void componentRemoved(Component c) {
			((Container) c).removeComponentListener(this);
			((Container) c).removeContainerListener(CONTAINER_LISTENER);
		}

		@Override
		public void componentResized(Component c) {
			((Container) c).doLayout();
		}
	};
	private final static ContainerListener CONTAINER_LISTENER = new ContainerListener() {

		@Override
		public void childRemoved(Container parent, Component c) {
			parent.doLayout();
		}

		@Override
		public void childAdded(Container parent, Component c) {
			parent.doLayout();
		}
	};

	Container(SnailShell shell) {
		super(shell);
		setFocusable(false);
		components = new fatcat.gui.Container();
		layout = this;
		addComponentListener(COMPONENT_ADAPTER);
		addContainerListener(CONTAINER_LISTENER);
	}

	public Container(Container owner) {
		super(owner);
		setFocusable(false);
		components = new fatcat.gui.Container();
		layout = this;
		addComponentListener(COMPONENT_ADAPTER);
		addContainerListener(CONTAINER_LISTENER);
	}

	void checkThreadSafety() {
		SnailShell shell = getShell();
		if (shell.check_thread_safety && getFrame().isShown() && !getShell().getFramework().isThreadSafe()) {
			try {
				throw new IllegalAccessException("SNAIL GUI Exception : illegal thread access");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void update(double dt) {
		super.update(dt);
		components.update(dt);
	}

	@Override
	protected void repaintComponent(GraphicsX g2) {
		super.repaintComponent(g2);
		components.repaint(g2);
	}

	final void repaintChildren(GraphicsX gx) {
		components.repaint(gx);
	}

	public final int indexOf(final Component c) {
		return components.indexOf(c);
	}

	public final void remove(int index) {
		get(index).remove();
	}

	public final void removeAll() {
		while (size() > 0) {
			get(0).remove();
		}
	}

	public final Component get(int index) {
		return (Component) components.get(index);
	}

	public final Component[] getComponents() {
		Component[] rst = new Component[size()];
		int i = 0;
		for (Iterator it = components.iterator(); it.hasNext(); i++) {
			rst[i] = (Component) it.next();
		}
		return rst;
	}

	public final Component[] getVisibleComponents() {
		LinkedList<Component> rst = new LinkedList<Component>();
		for (Iterator it = components.iterator(); it.hasNext();) {
			Component c = (Component) it.next();
			if (c.isVisible())
				rst.add(c);
		}
		return rst.toArray(new Component[0]);
	}

	public final Iterator iterator() {
		return components.iterator();
	}

	public final int size() {
		return components == null ? 0 : components.size();
	}

	@Override
	public int preferredWidth(Component c) {
		return 400;
	}

	@Override
	public int preferredHeight(Component c) {
		return 300;
	}

	public ILayout getLayout() {
		return layout;
	}

	public void setLayout(ILayout layout) {
		this.layout = layout == null ? this : layout;
		doLayout();
	}

	public final void doLayout() {
		layout.refreshLayout(this);
		requestRepaint();
	}

	@Override
	public void refreshLayout(Container c) {}

	// component event

	final LinkedList<ContainerListener> containerListeners = new LinkedList<ContainerListener>();

	public final void addContainerListener(ContainerListener listener) {
		containerListeners.add(listener);
	}

	public final void removeContainerListener(ContainerListener listener) {
		containerListeners.remove(listener);
	}

	protected final void fireChildAdded(Component c) {
		ContainerListener[] copy_list = containerListeners.toArray(new ContainerListener[0]);
		for (ContainerListener listener : copy_list)
			listener.childAdded(this, c);
	}

	protected final void fireChildRemoved(Component c) {
		ContainerListener[] copy_list = containerListeners.toArray(new ContainerListener[0]);
		for (ContainerListener listener : copy_list)
			listener.childRemoved(this, c);
	}

	// mouse event

	public final Component getComponentOn(int x, int y) {
		Component tmp = null;
		final boolean out_of_area = x < 0 || getWidth() <= x || y < 0 || getHeight() <= y;
		if (isClip() && out_of_area) {
			return null;
		}
		for (int i = size() - 1; i >= 0; i--) {
			tmp = get(i);
			if (!tmp.isVisible())
				continue;
			if (tmp instanceof Container) {
				tmp = ((Container) tmp).getComponentOn(x - tmp.getLeft(), y - tmp.getTop());
				if (tmp != null)
					return tmp;
			} else {
				if (tmp.getLeft() <= x && tmp.getRight() > x && tmp.getTop() <= y && tmp.getBottom() > y)
					return tmp;
			}
		}
		return out_of_area ? null : this;
	}

	public void sortChildren(final Comparator<Component> comparator) {
		components.sortChildren(new Comparator<fatcat.gui.Component>() {

			@Override
			public int compare(fatcat.gui.Component o1, fatcat.gui.Component o2) {
				return comparator.compare((Component) o1, (Component) o2);
			}
		});
		doLayout();
	}

	// //Incremental Painting ////

	public void disabelIncrementalPaint() {
		Component[] cs = getComponents();
		for (Component c : cs) {
			if (c.incrementalPaint) {
				throw new UnsupportedOperationException(
						"cannot disable incremental paint because a child has enabled this feature");
			}
		}
		super.disabelIncrementalPaint();
	}

	public void requestIncrementalRepaint2(boolean repaint_owner) {
		if (repaint_owner) {
			super.requestIncrementalRepaint2(repaint_owner);
		} else if (!increpaint_flag) {
			increpaint_flag = true;
			Component[] cs = getComponents();
			for (Component c : cs) {
				c.requestIncrementalRepaint2(false);
			}
		}
	}

}
