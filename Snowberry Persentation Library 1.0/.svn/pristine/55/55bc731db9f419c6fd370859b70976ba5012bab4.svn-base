package fatcat.snowberry.gui;

import java.awt.Color;
import java.awt.Font;
import fatcat.gui.GraphicsX;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ContainerListener;
import fatcat.snowberry.gui.util.Dragger;


public class DiagramPanelTitle extends Container {
	
	private int space = 18;
	private final Dragger dragger;
	private Image image = null;
	private Font font;
	private Color textColor;

	public DiagramPanelTitle(DiagramPanel owner) {
		super(owner);
		setClip(true);
		enableBuffer();
		setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		setTextColor(Color.black);
		setLocation(1, 1);
		addMouseListener(dragger = new Dragger(owner));
		addContainerListener(new ContainerListener() {
			
			@Override
			public void childRemoved(Container parent, Component c) {
				c.removeMouseListener(dragger);
			}
			
			@Override
			public void childAdded(Container parent, Component c) {
				c.addMouseListener(dragger);
			}
			
		});
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		requestRepaint();
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
		requestRepaint();
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
		requestRepaint();
	}
	
	public final Dragger getDragger() {
		return dragger;
	}
	
	public final int getSpace() {
		return space;
	}
	
	public final void setSpace(int space) {
		this.space = space;
		requestRepaint();
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 46;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return getOwner().getWidth() - 2;
	}
	
	//// paint ////
	
	public static final int COLOR_WHITE = 0; // DO NOT change the values !
	public static final int COLOR_GRAY = 1;
	public static final int COLOR_RED = 2;
	public static final int COLOR_YELLOW = 3;
	public static final int COLOR_GREEN = 4;
	public static final int COLOR_BLUE = 5;
	public static final int COLOR_PURPLE = 6;
	
	public static final BufferedImage[] IMG_DIAGRAM_PANEL_TITLES;
	
	static {
		IMG_DIAGRAM_PANEL_TITLES = new BufferedImage[7];
		IMG_DIAGRAM_PANEL_TITLES[0] = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Title.White.png");
		IMG_DIAGRAM_PANEL_TITLES[1] = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Title.Gray.png");
		IMG_DIAGRAM_PANEL_TITLES[2] = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Title.Red.png");
		IMG_DIAGRAM_PANEL_TITLES[3] = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Title.Yellow.png");
		IMG_DIAGRAM_PANEL_TITLES[4] = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Title.Green.png");
		IMG_DIAGRAM_PANEL_TITLES[5] = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Title.Blue.png");
		IMG_DIAGRAM_PANEL_TITLES[6] = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Title.Purple.png");
	}

	private int color = COLOR_WHITE;
	
	public final int getColor() {
		return color;
	}
	
	public final void setColor(int color) {
		this.color = color;
		requestRepaint();
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.drawImage(IMG_DIAGRAM_PANEL_TITLES[color], 0, 0, 6, 46, 0, 0, 6, 46, null);
		final int tmp = getRight() - space;
		g2.drawImage(IMG_DIAGRAM_PANEL_TITLES[color], tmp, 0, tmp + space - 1, 46, 55, 0, 64, 46, null);
		g2.drawImage(IMG_DIAGRAM_PANEL_TITLES[color], tmp - 38, 0, tmp, 46, 17, 0, 55, 46, null);
		g2.drawImage(IMG_DIAGRAM_PANEL_TITLES[color], 6, 0, tmp - 38, 46, 6, 0, 17, 46, null);
		
		g2.setColor(textColor);
		g2.setFont(font);
		
		g2.pushClip(new Rectangle(0, 0, tmp - 24, 22));
		
		// image and text
		if (image != null) {
			g2.drawImage(image, 3, 3, null);
			g2.drawString(getText(), 22, 3 + font.getSize());
		} else {
			g2.drawString(getText(), 6, 5 + font.getSize());
		}
		
		g2.popClip();
	}

}
