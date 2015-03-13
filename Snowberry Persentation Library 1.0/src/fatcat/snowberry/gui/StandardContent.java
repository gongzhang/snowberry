package fatcat.snowberry.gui;

import fatcat.gui.GraphicsX;
import java.util.LinkedList;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.ContainerListener;


public class StandardContent extends Container {
	
	private final ComponentAdapter COMPONENT_ADAPTER = new ComponentAdapter() {
		
		@Override
		public void componentResized(Component c) {
			StandardContent.this.setSize(c);
		}
		
		@Override
		public void componentRemoved(Component c) {
			StandardContent.this.removeContainerListener(CONTAINER_LISTENER);
		}
	};
	
	private static final ContainerListener CONTAINER_LISTENER = new ContainerListener() {
		
		@Override
		public void childRemoved(Container parent, Component c) {
			if (c instanceof CategoryPanel) {
				((StandardContent) parent).categories.remove((CategoryPanel) c);
			}
		}
		
		@Override
		public void childAdded(Container parent, Component c) {
			if (c instanceof CategoryPanel) {
				((StandardContent) parent).categories.add((CategoryPanel) c);
			}
		}
	}; 
	
	private final Component space1, space2;
	
	private final LinkedList<CategoryPanel> categories;

	public StandardContent(final Container owner) {
		super(owner);
		categories = new LinkedList<CategoryPanel>();
		space1 = new Component(this);
		space1.setHeight(2);
		space1.setSkin(this);
		space2 = new Component(this);
		space2.setSkin(this);
		owner.addComponentListener(COMPONENT_ADAPTER);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentRemoved(Component c) {
				owner.removeComponentListener(COMPONENT_ADAPTER);
			}
		});
		this.addContainerListener(CONTAINER_LISTENER);
	}
	
	public final CategoryPanel[] getCategories() {
		return categories.toArray(new CategoryPanel[0]);
	}
	
	public final StandardItem[] getSelection() {
		StandardItem[] rst = new StandardItem[0];
		for (CategoryPanel category : categories) {
			rst = category.getSelection();
			if (rst.length != 0) return rst;
		}
		return rst;
	}
	
	public final void clearSelection() {
		for (CategoryPanel category : categories) {
			category.clearSelection();
		}
	}
	
	@Override
	public void refreshLayout(Container container) {
		space2.bringToTop();
		int top = 0;
		for (int i = 0; i < size(); i++) {
			Component c = get(i);
			c.setLocation(0, top);
			c.setWidth(getWidth());
			top += c.getHeight();
		}
		getOwner().setHeight(top == 0 ? 15 : top);
	}
	
	protected void selectionChanged() {
		
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
	}

}
