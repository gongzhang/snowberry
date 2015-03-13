package fatcat.snowberry.gui;

import fatcat.gui.GraphicsX;
import java.awt.image.BufferedImage;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.util.Image3x3;


public class Bubble extends Container {

	public Bubble(Container owner) {
		super(owner);
	}
	
	@Override
	public int preferredWidth(Component c) {
		return 64;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 64;
	}
	
	public static final Image3x3 IMG_BUBBLE_BG = new Image3x3("/fatcat/snowberry/gui/res/Bubble.Background.png", 6, 86, 11, 86);
	public static final BufferedImage IMG_BUBBLE_ARROW = GraphicsX.createImage("/fatcat/snowberry/gui/res/Bubble.Arrow.png");
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.paint3x3(IMG_BUBBLE_BG, c.getWidth(), c.getHeight());
		g2.drawImage(IMG_BUBBLE_ARROW, getWidth() - 5, getHeight() / 2 - 8, null);
	}

}
