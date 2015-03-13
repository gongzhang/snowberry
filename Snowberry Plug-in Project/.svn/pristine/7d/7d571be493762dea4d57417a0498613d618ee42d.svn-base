package fatcat.snowberry.diagram;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.snowberry.gui.Bubble;
import fatcat.snowberry.tag.IMemberModel;

public class LabelBubble extends Bubble {
	
	private final IMemberModel model;

	public LabelBubble(Container owner, IMemberModel model) {
		super(owner);
		this.model = model;
		Label[] labels = Label.parseLabels(model);
		for (Label label : labels) {
			new LabelComponent(this, label, model);
		}
		
	}
	
	@Override
	public int preferredHeight(Component c) {
		return preferred_height;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return preferred_width;
	}
	
	private int preferred_height;
	private int preferred_width;
	private final int border_width = 10;
	
	@Override
	public void refreshLayout(Container co) {
		Component[] cs = co.getComponents();
		preferred_height = border_width;
		preferred_width = 0;
		for (Component c : cs) {
			c.setLocation(border_width, preferred_height);
			preferred_height += c.getHeight() + border_width;
			if (preferred_width < c.getWidth() + 2 * border_width) {
				preferred_width = c.getWidth() + 2 * border_width;
			}
		}
		setSize();
	}
	
	public IMemberModel getModel() {
		return model;
	}

}
