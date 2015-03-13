package fatcat.snowberry.gui;

import fatcat.gui.GraphicsX;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;


public class LegendPanel extends Container {

	public LegendPanel(Container owner) {
		super(owner);
	}
	
	@Override
	public void refreshLayout(Container co) {
		Component[] cs = co.getComponents();
		int top = 12;
		for (Component c : cs) {
			c.setLocation(12, top);
			top += c.getHeight() + 4;
		}
	}
	
	@Override
	public int preferredWidth(Component co) {
		if (size() == 0) return 24;
		Component[] cs = getComponents();
		int max = 0;
		for (Component c : cs) {
			if (c.getWidth() > max) max = c.getWidth();
		}
		return max + 24;
	}
	
	@Override
	public int preferredHeight(Component co) {
		if (size() == 0) return 24;
		Component[] cs = getComponents();
		int height = 0;
		for (Component c : cs) {
			height += c.getHeight() + 4;
		}
		return height + 20;
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.translate(-7, -8);
		g2.paint3x3(DiagramPanel.IMG_DIAGRAM_PANEL_BG1, getWidth() + 15, getHeight() + 16);
		g2.translate(7, 8);
	}

}
