package fatcat.snowberry.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;

public class MultilineLabel extends Label {
	
	private final LinkedList<String> strings;
	private int lineSpace;

	public MultilineLabel(Container owner) {
		super(owner);
		strings = new LinkedList<String>();
		setLineSpace(2);
		setAutoSize(false);
	}
	
	public int getLineSpace() {
		return lineSpace;
	}
	
	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		strings.clear();
		final Graphics2D g2 = getShell().createGraphics();
		final FontRenderContext context = g2.getFontRenderContext();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < getText().length(); i++) {
			char ch = getText().charAt(i);
			if (ch == '\n') {
				strings.add(buffer.toString());
				buffer = new StringBuffer();
			} else {
				buffer.append(ch);
				Rectangle2D rect = getFont().getStringBounds(buffer.toString(), context);
				if (rect.getWidth() > getWidth()) {
					if (buffer.length() > 0) {
						buffer.deleteCharAt(buffer.length() - 1);
					}
					strings.add(buffer.toString());
					buffer = new StringBuffer();
				}
			}
		}
		if (buffer.length() > 0) {
			strings.add(buffer.toString());
		}
	}
	
	@Override
	public int preferredHeight(Component c) {
		if (strings != null) {
			return (getFont().getSize() + lineSpace) * strings.size();
		} else {
			return super.preferredHeight(c);
		}
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(new Color(getColor()));
		g2.setFont(getFont());
		int top = 0;
		for (String string : strings) {
			g2.drawString(string, 0, top);
			top += getFont().getSize() + lineSpace;
		}
	}

}
