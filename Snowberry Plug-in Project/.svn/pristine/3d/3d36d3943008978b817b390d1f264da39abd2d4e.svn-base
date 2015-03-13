package fatcat.snowberry.diagram;

import java.awt.event.MouseEvent;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.gui.ScrollPanel;
import fatcat.snowberry.gui.StandardContent;
import fatcat.snowberry.gui.XPanel;
import fatcat.snowberry.tag.IMemberModel;


public class CompositeItemViewer extends XPanel {
	
	private final ScrollPanel scrollPanel;
	private final StandardContent content;
	private final CategoryPanel categoryPanel;
	private final CompositeItem item;

	public CompositeItemViewer(Container owner, final CompositeItem item) {
		super(owner);
		this.item = item;
		setBarWidth(item.getWidth() - 14);
		final CompositeItemGhost hover = new CompositeItemGhost(this, item);
		hover.setBounds(
			-item.getWidth() - 11,
			getHeight() / 2 - 10,
			item.getWidth(),
			item.getHeight()
		);
		hover.addMouseListener(new MouseAdapter() {
			
			int x = 0;
			
			@Override
			public void mouseMoved(Component c, MouseEvent e, int x, int y) {
				if (this.x == 0) {
					this.x = x;
				} else if (this.x >= x) {
					item.hideViewer();
				} else {
					final float alpha = (x - this.x) / 30.0f;
					setAlpha(alpha > 1.0f ? 1.0f : alpha);
				}
			}
			
		});
		final ComponentAdapter remover1 = new ComponentAdapter() {
			@Override
			public void componentResized(Component c) {
				item.hideViewer();
			}
		};
		item.addComponentListener(remover1);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentRemoved(Component c) {
				item.removeComponentListener(remover1);
			}
		});
	
		// content
		scrollPanel = new ScrollPanel(this);
		content = new StandardContent(scrollPanel.getClientContainer());
		Category category = item.getCategory();
		categoryPanel = new CategoryPanel(content, category.name, category.color);
		IMemberModel[] models = item.getModels();
		for (IMemberModel model : models) {
			MemberItem.createMemberItem(categoryPanel, model).disableBuffer();
		}
		
	}
	
	public CompositeItem getCompositeItem() {
		return item;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return getOwner().getWidth();
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 120;
	}
	
	@Override
	public void refreshLayout(Container c) {
		scrollPanel.setLocation(0, 0);
		scrollPanel.setSize(c);
	}

}
