package fatcat.gui.snail.event;

import fatcat.gui.snail.Component;


public interface FocusListener {
	
	public void gotFocus(Component c);
	
	public void lostFocus(Component c);

}
