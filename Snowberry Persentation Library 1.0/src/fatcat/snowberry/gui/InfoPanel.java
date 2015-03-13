package fatcat.snowberry.gui;

import fatcat.gui.GraphicsX;
import java.awt.image.BufferedImage;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.util.Image3x3;
import fatcat.snowberry.gui.util.Resizer;


public class InfoPanel extends Container {
	
	private final Component resizer;

	public InfoPanel(Container owner) {
		super(owner);
		resizer = new Component(this) {
			@Override
			public void repaintComponent(GraphicsX g2, Component c) {
			}
			@Override
			public int preferredWidth(Component c) {
				return 16;
			}
			@Override
			public int preferredHeight(Component c) {
				return 16;
			}
		};
		resizer.addMouseListener(new Resizer(this));
	}
	
	@Override
	public void refreshLayout(Container c) {
		if (resizer != null) {
			resizer.setLocation(getWidth() - 16, getHeight() - 16);
		}
	}
	
	public static final Image3x3 IMG_INFOPANEL_BG = new Image3x3("/fatcat/snowberry/gui/res/InfoPanel.Background.png", 9, 42, 9, 29);
	public static final BufferedImage IMG_INFOPANEL_CORNER = GraphicsX.createImage("/fatcat/snowberry/gui/res/InfoPanel.Corner.png");
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.translate(-8, -8);
		g2.paint3x3(IMG_INFOPANEL_BG, getWidth() + 16, getHeight() + 16);
		g2.translate(8, 8);
		g2.drawImage(IMG_INFOPANEL_CORNER, getWidth() - 14, getHeight() - 14, null);
	}

}
