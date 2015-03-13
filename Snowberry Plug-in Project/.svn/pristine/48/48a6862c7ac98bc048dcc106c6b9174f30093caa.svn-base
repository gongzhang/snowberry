package fatcat.snowberry.views.internal;

import java.awt.Image;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;

public class Icon extends Component {

	private final Image image;
	
	public Icon(Container owner, Image image) {
		super(owner);
		this.image = image;
		setSize();
	}
	
	@Override
	public int preferredWidth(Component c) {
		return image == null ? 0 : image.getWidth(null);
	}
	
	@Override
	public int preferredHeight(Component c) {
		return image == null ? 0 : image.getHeight(null);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.drawImage(image, 0, 0);
	}
	
}
