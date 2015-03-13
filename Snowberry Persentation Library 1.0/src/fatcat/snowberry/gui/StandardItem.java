package fatcat.snowberry.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import fatcat.gui.GraphicsX;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.gui.util.Image3x3;


public class StandardItem extends Component {
	
	public static final Image3x3 IMG_STD_ITEM_BG1 = new Image3x3("/fatcat/snowberry/gui/res/StandardItem.Background.1.png", 2, 18, 2, 18);
	public static final Image3x3 IMG_STD_ITEM_BG2 = new Image3x3("/fatcat/snowberry/gui/res/StandardItem.Background.2.png", 2, 18, 2, 18);
	
	private boolean selected;
	private boolean mouse_on;
	private static final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
	
	private BufferedImage image;
	private Font font;
	private String suffixText = "Suffix Text";
	
	public StandardItem(CategoryPanel owner) {
		super(owner);
		setClip(true);
		enableBuffer();
		standardContent = (StandardContent) owner.getOwner();
		selected = false;
		mouse_on = false;
		setFont(DEFAULT_FONT);
		setText(getText());
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(Component c) {
				mouse_on = true;
				requestRepaint();
			}
			
			@Override
			public void mouseExited(Component c) {
				mouse_on = false;
				requestRepaint();
			}
			
			private void selectOne(Component c) {
				StandardItem[] items = ((CategoryPanel) getOwner()).getItems();
				for (StandardItem item : items) {
					if (item != c) item.setSelected(false);
					else item.setSelected(true);
				}
			}
			
			@Override
			public void mousePressed(Component c, MouseEvent e, int x, int y) {
				if (isSelected() && e.getButton() != MouseEvent.BUTTON1) return;
				CategoryPanel[] categories = getStandardContent().getCategories();
				for (CategoryPanel category : categories) {
					if (category != getOwner()) category.clearSelection();
				}
				
				if (!e.isControlDown() && !e.isShiftDown()) {
					
					selectOne(c);
					
				} else if (e.isShiftDown()) {
					
					StandardItem[] items = ((CategoryPanel) getOwner()).getSelection();
					if (items.length == 0) {
						selectOne(c);
					} else {
						int i = getOwner().indexOf(items[0]);
						int j = getOwner().indexOf(c);
						if (i > j) {
							x = j;
							j = i;
							i = x;
						}
						if (i < 0) return;
						items = ((CategoryPanel) getOwner()).getItems();
						for (x = i; x <= j; x++) {
							items[x].setSelected(true);
						}
					}
					
				} else {
					
					if (isSelected()) setSelected(false);
					else setSelected(true);
					
				}
				getStandardContent().selectionChanged();
			}
			
		});
	}
	
	public String getSuffixText() {
		return suffixText;
	}
	
	public void setSuffixText(String suffixText) {
		this.suffixText = suffixText;
		requestRepaint();
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
		requestRepaint();
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
		requestRepaint();
	}
	
	private final StandardContent standardContent;
	
	public final StandardContent getStandardContent() {
		return standardContent;
	}
	
	public final boolean isSelected() {
		return selected;
	}
	
	protected void setSelected(boolean selected) {
		this.selected = selected;
		requestRepaint();
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 20;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return getOwner().getWidth();
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		Graphics2D g2 = getShell().createGraphics();
		if (font != null) g2.setFont(font);
		text_width = g2.getFontMetrics().stringWidth(text);
		requestRepaint();
	}
	
	private int text_width;
	private final static Color suffixTextColor = new Color(0xcd9648);
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.translate(20, 0);
		if (selected) g2.paint3x3(IMG_STD_ITEM_BG2, getWidth() - 21, getHeight());
		else if (mouse_on) g2.paint3x3(IMG_STD_ITEM_BG1, getWidth() - 21, getHeight());
		
		g2.pushClip(new Rectangle(0, 0, getWidth() - 22, getHeight()));
		if (image != null) {
			g2.drawImage(image, 2, 2, null);
		}
		g2.setColor(Color.black);
		g2.setFont(font);
		g2.drawString(getText(), 20, font.getSize() + 2);
		g2.setColor(selected ? Color.black : suffixTextColor);
		g2.drawString(suffixText, 20 + text_width, font.getSize() + 2);
		g2.popClip();
		
		g2.translate(-20, 0);
	}

}
