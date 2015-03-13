package fatcat.snowberry.gui;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.MouseAdapter;


public class SwitchButton extends AbstractButton {
	
	private BufferedImage image = null;
	private boolean selected = false;

	public SwitchButton(Container owner) {
		super(owner);
		setClip(true);
		enableBuffer();
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(Component c) {
				if (!selected) setState(UP);
			}
			
			@Override
			public void mouseExited(Component c) {
				if (!selected) setState(NORMAL);
				else setState(DOWN);
			}
			
			@Override
			public void mousePressed(Component c, MouseEvent e, int x, int y) {
				setState(DOWN);
			}
			
			@Override
			public void mouseReleased(Component c, MouseEvent e, int x, int y) {
				if (!selected) {
					selected = true;
					setState(DOWN);
				} else {
					selected = false;
					setState(UP);
				}
			}
			
		});
	}
	
	@Override
	public void setState(int state) {
		super.setState(state);
		requestRepaint();
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		requestRepaint();
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
		requestRepaint();
	}

	@Override
	protected void repaintButton(GraphicsX p) {
		if (image != null) p.drawImage(image, 0, 0, null);
	}

}
