package fatcat.snowberry.gui;

import java.awt.Color;
import fatcat.gui.GraphicsX;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;


public class LegendItem extends Label {
	
	private Color iconColor;
	
	public LegendItem(Container owner, Color iconColor, String text) {
		super(owner, text);
		setIconColor(iconColor);
	}

	public LegendItem(Container owner, String text) {
		this(owner, Color.black, text);
	}
	
	public Color getIconColor() {
		return iconColor;
	}
	
	public void setIconColor(Color iconColor) {
		this.iconColor = iconColor;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return super.preferredWidth(c) + 20;
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(iconColor);
		g2.fillRoundRect(0, getHeight() / 2 - 8, 16, 16, 12, 12);
		g2.translate(20, 0);
		super.repaintComponent(g2, c);
		g2.translate(-20, 0);
	}

}
