package fatcat.snowberry.gui;

import java.awt.AlphaComposite;
import java.awt.Point;
import java.awt.image.BufferedImage;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.util.Image3x3;
import fatcat.snowberry.gui.util.PointQInterpolator;


public class DiagramPanel extends Container {
	
	public final DiagramPanelTitle title;
	public final DiagramContent content;
	public final Component resizer;
	
	private PointQInterpolator locationInterpolator = null;
	private PointQInterpolator sizeInterpolator = null;
	
	public DiagramPanel(Container owner) {
		super(owner);
		title = new DiagramPanelTitle(this);
		content = new DiagramContent(this);
		resizer = new Resizer(this);
	}
	
	public boolean isMoving() {
		return locationInterpolator != null;
	}
	
	public boolean isResizing() {
		return sizeInterpolator != null;
	}
	
	public void moveTo(int left, int top) {
		locationInterpolator = new PointQInterpolator(getLeft(), getTop(), left, top, 0.35);
	}
	
	public void resizeTo(int width, int height) {
		sizeInterpolator = new PointQInterpolator(getWidth(), getHeight(), width, height, 0.35);
	}
	
	public final Container getClientContainer() {
		return content.getClientContainer();
	}
	
	//// layout ////
	
	@Override
	public int preferredWidth(Component c) {
		return 220;
	}
		
	@Override
	public int preferredHeight(Component c) {
		return 300;
	}
	
	@Override
	public void refreshLayout(Container c) {
		title.setLocation(1, 1);
		title.setSize();
		content.setLocation(1, title.getBottom());
		content.setSize();
		resizer.setLocation(getWidth() - 16, getHeight() - 16);
	}
	
	//// paint ////
	
	@Override
	protected void update(double dt) {
		super.update(dt);
		
		// animation
		if (locationInterpolator != null) {
			Point p;
			if (locationInterpolator.hasDone()) {
				p = locationInterpolator.endValue;
				locationInterpolator = null;
			} else {
				p = locationInterpolator.update(dt);
			}
			setLocation(p.x, p.y);
		}
		if (sizeInterpolator != null) {
			Point p;
			if (sizeInterpolator.hasDone()) {
				p = sizeInterpolator.endValue;
				sizeInterpolator = null;
			} else {
				p = sizeInterpolator.update(dt);
			}
			setSize(p.x, p.y);
		}
		
		// paint
		switch (img_state) {
		case IMG_STATE_1TO2:
			img_lambda += dt * 4.0;
			if (img_lambda > 1.0f) {
				img_lambda = 1.0f;
				img_state = IMG_STATE_2;
			}
			break;
		case IMG_STATE_2TO1:
			img_lambda -= dt * 4.0;
			if (img_lambda < 0.0f) {
				img_lambda = 0.0f;
				img_state = IMG_STATE_1;
			}
			break;
		}

	}

	public static final Image3x3 IMG_DIAGRAM_PANEL_BG1 = new Image3x3("/fatcat/snowberry/gui/res/DiagramPanel.Background.1.png", 15, 111, 16, 110);
	public static final Image3x3 IMG_DIAGRAM_PANEL_BG2 = new Image3x3("/fatcat/snowberry/gui/res/DiagramPanel.Background.2.png", 29, 99, 29, 98);
	
	public final void darken() {
		img_state = IMG_STATE_1TO2;
	}
	
	public final void lighten() {
		img_state = IMG_STATE_2TO1;
	}
	
	private int img_state = IMG_STATE_1;
	private static final int IMG_STATE_1 = 0;
	private static final int IMG_STATE_1TO2 = 1;
	private static final int IMG_STATE_2 = 2;
	private static final int IMG_STATE_2TO1 = 3;
	private float img_lambda = 0.0f;
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - img_lambda));
		paintBG1(g2);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, img_lambda));
		paintBG2(g2);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
	}
	
	private void paintBG1(GraphicsX g2) {
		g2.translate(-7, -8);
		g2.paint3x3(IMG_DIAGRAM_PANEL_BG1, getWidth() + 15, getHeight() + 16);
		g2.translate(7, 8);
	}
	
	private void paintBG2(GraphicsX g2) {
		g2.translate(-21, -21);
		g2.paint3x3(IMG_DIAGRAM_PANEL_BG2, getWidth() + 42, getHeight() + 42);
		g2.translate(21, 21);
	}

}

class Resizer extends Component {
	
	public static final BufferedImage IMG_DIAGRAM_CORNER = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramPanel.Corner.png");

	public Resizer(Container owner) {
		super(owner);
		addMouseListener(new fatcat.snowberry.gui.util.Resizer(owner));
	}
	
	@Override
	public int preferredWidth(Component c) {
		return 16;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 16;
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.drawImage(IMG_DIAGRAM_CORNER, getWidth() - 16, getHeight() - 16, null);
	}
	
}
