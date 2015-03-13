package fatcat.snowberry.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import fatcat.gui.GraphicsX;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;


public class Label extends Component {
	
	private Font font;
	private int color = 0x0;
	private boolean autoSize = true;

	public Label(Container owner) {
		super(owner);
		font = new Font(Font.DIALOG, Font.PLAIN, 12);
	}
	
	public Label(Container owner, String text) {
		this(owner);
		setText(text);
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
		if (autoSize) setSize();
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		if (autoSize) setSize();
	}
	
	@Override
	public int preferredWidth(Component c) {
		Graphics2D g2 = getShell().createGraphics();
		if (font != null) g2.setFont(font);
		return g2.getFontMetrics().stringWidth(getText());
	}
	
	@Override
	public int preferredHeight(Component c) {
		Graphics2D g2 = getShell().createGraphics();
		if (font != null) g2.setFont(font);
		return g2.getFontMetrics().getHeight();
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(new Color(color));
		g2.setFont(font);
		g2.drawString(getText(), 0, font.getSize());
	}

	public void setAutoSize(boolean autoSize) {
		this.autoSize = autoSize;
	}

	public boolean isAutoSize() {
		return autoSize;
	}

}
