package fatcat.gui.snail;

import fatcat.gui.GraphicsX;


public interface ISkin {
	
	public int preferredHeight(Component c);

	public int preferredWidth(Component c);
	
	public void repaintComponent(GraphicsX g2, Component c);

}
