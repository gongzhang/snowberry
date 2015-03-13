package fatcat.snowberry.gui;

import java.awt.Font;
import fatcat.gui.GraphicsX;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.gui.util.Image3x3;


public class RealLabel extends Label {
	
	public static final Font FONT_NORMAL = new Font(Font.DIALOG, Font.PLAIN, 12);
	public static final Font FONT_BOLD = new Font(Font.DIALOG, Font.BOLD, 12);

	public RealLabel(Container owner) {
		super(owner);
		setClip(true);
		enableBuffer();
		init();
	}
	
	public RealLabel(Container owner, String text) {
		super(owner, text);
		setClip(true);
		enableBuffer();
		init();
	}
	
	private void init() {
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(Component c) {
				setFont(FONT_BOLD);
				requestRepaint();
			}
			
			@Override
			public void mouseExited(Component c) {
				setFont(FONT_NORMAL);
				requestRepaint();
			}
			
		});
	}
	
	@Override
	public int preferredWidth(Component c) {
		return super.preferredWidth(c) + 12;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return super.preferredHeight(c) + 2;
	}
	
	public static final Image3x3 IMG_TAG_BG = new Image3x3("/fatcat/snowberry/gui/res/Tag.Background.png", 7, 58, 7, 19);
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.paint3x3(IMG_TAG_BG, c.getWidth(), c.getHeight());
		g2.translate(8, 1);
		super.repaintComponent(g2, c);
		g2.translate(-8, -1);
	}

}
