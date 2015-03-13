package fatcat.snowberry.gui;

import java.awt.Color;
import fatcat.gui.GraphicsX;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;


public class DiagramContent extends Container {
	
	public final ScrollPanel scrollPanel;

	public DiagramContent(Container owner) {
		super(owner);
		setClip(true);
		scrollPanel = new ScrollPanel(this);
	}
	
	public final Container getClientContainer() {
		return scrollPanel.getClientContainer();
	}
	
	@Override
	public void refreshLayout(Container c) {
		scrollPanel.setSize(getWidth(), getHeight() - 1);
	}
	
	@Override
	public int preferredWidth(Component c) {
		return getOwner().getWidth() - 2;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return getOwner().getHeight() - 70;
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(new Color(0xc9c9c9));
		g2.drawLine(0, getHeight() - 1, getRight(), getHeight() - 1);
	}

}
