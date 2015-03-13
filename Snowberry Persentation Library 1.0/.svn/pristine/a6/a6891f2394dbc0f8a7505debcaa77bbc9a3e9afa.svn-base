package fatcat.snowberry.gui;

import fatcat.gui.GraphicsX;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.util.Image3x3;


abstract public class AbstractButton extends Component {
	
	public static final int NORMAL = 0;
	public static final int UP = 1;
	public static final int DOWN = 2;
	private int state = 0;

	public AbstractButton(Container owner) {
		super(owner);
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return 20;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 20;
	}
	
	//// paint ////
	
	public static final Image3x3 IMG_BUTTON_DOWN = new Image3x3("/fatcat/snowberry/gui/res/Button.Down.png", 6, 19, 6, 19);
	public static final Image3x3 IMG_BUTTON_UP = new Image3x3("/fatcat/snowberry/gui/res/Button.Up.png", 6, 19, 6, 19);

	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.translate(-3, -3);
		switch (state) {
		case UP:
			g2.paint3x3(IMG_BUTTON_UP, getWidth() + 6, getHeight() + 6);
			break;
		case DOWN:
			g2.paint3x3(IMG_BUTTON_DOWN, getWidth() + 6, getHeight() + 6);
			break;
		}
		if (state == DOWN) {
			g2.translate(6, 5);
			repaintButton(g2);
			g2.translate(-3, -2);
		} else {
			g2.translate(5, 5);
			repaintButton(g2);
			g2.translate(-2, -2);
		}
	}
	
	abstract protected void repaintButton(GraphicsX p);
	
}
