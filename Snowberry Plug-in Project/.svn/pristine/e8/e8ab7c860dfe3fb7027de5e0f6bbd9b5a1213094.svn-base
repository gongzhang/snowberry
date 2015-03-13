package fatcat.snowberry.diagram.dp;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import fatcat.gui.GraphicsX;
import java.awt.event.MouseEvent;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;

import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.DiagramFrame;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.gui.InfoPanel;
import fatcat.snowberry.gui.Label;
import fatcat.snowberry.gui.XPanel;
import fatcat.snowberry.gui.util.QInterpolator;
import fatcat.snowberry.tag.IMemberModel;

public class DPOverview extends Container {
	
	private final DesignPattern pattern;
	private final Component mouseHandler;
	private final DPFrame dpFrame;
	
	private void clicked() {
		final DiagramFrame diagramFrame = getDPFrame().getDiagramFrame();
		diagramFrame.show();
		diagramFrame.showDesignPattern(pattern);
	}

	public DPOverview(DPFrame owner, final DesignPattern pattern) {
		super(owner);
		this.pattern = pattern;
		dpFrame = owner;
		
		final Container content = createContent();
		
		mouseHandler = new Component(this) {
			@Override
			public void repaintComponent(GraphicsX g2, Component c) {
			}
		};
		setSize();
		content.setSize(this);
		mouseHandler.setSize(this);
		mouseHandler.addMouseListener(new MouseAdapter() {
			
			DPComment comment = null;
			
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				clicked();
			}
			
			@Override
			public void mouseEntered(Component c) {
				animation = new QInterpolator(0.3, 1.0, 0.3);
				comment = new DPComment(c.getFrame(), pattern);
				comment.setLocation(8, getScreenHeight() - comment.getHeight() - 8);
			}
			
			@Override
			public void mouseExited(Component c) {
				animation = new QInterpolator(alpha, 0.3, 0.5);
				if (comment != null) {
					comment.remove();
					comment = null;
				}
			}
			
//			@Override
//			public void mouseMoved(Component c, MouseEvent e, int x, int y) {
//				if (comment != null) {
//					comment.setLocation(Math.min(e.getX() + 16, getScreenWidth() - comment.getWidth()), e.getY() + 16);
//				}
//			}
			
		});
	}
	
	private int max_width = 0;
	private static final int preferred_height = 105;
	private static final GradientPaint GRADIENT_PAINT = new GradientPaint(0, 0, Color.white, 0, preferred_height, new Color(0xe5f0ff));
	public static final Font FONT_TITLE = new Font(Font.DIALOG, Font.BOLD, 16);
	public static final Font FONT_MEMBER = new Font(Font.DIALOG, Font.ITALIC, 12);
	public static final Font FONT_INFO = new Font(Font.DIALOG, Font.PLAIN, 12);
	
	private Container createContent() {
		final Container content = new Container(this) {
			@Override
			public void repaintComponent(GraphicsX g2, Component c) {
				g2.setPaint(GRADIENT_PAINT);
				g2.fillRect(0, 0, c.getWidth(), c.getHeight());
			}
		};
		content.setClip(true);
		IMemberModel[] models = pattern.getModels();
		
		final Label title = new Label(content, String.format("%s (%d)", pattern.getPatternName(), models.length)) {
			@Override
			public void repaintComponent(GraphicsX g2, Component c) {
				int color = getColor();
				this.setColor(0xffffff);
				g2.translate(1, 1);
				super.repaintComponent(g2, c);
				g2.translate(-1, -1);
				this.setColor(color);
				super.repaintComponent(g2, c);
			}
		};
		title.setAutoSize(true);
		title.setFont(FONT_TITLE);
		title.setColor(new Color(0x4985d9).getRGB());
		title.setLocation(4, 4);
		if (title.getRight() > max_width) max_width = title.getRight();
		
		int i = 0, top = title.getBottom() + 4;
		for (IMemberModel model : models) {
			if (i == 3) break;
			final Label modelLabel = new Label(content, String.format(" - %s", model.getJavaElement().getElementName()));
			modelLabel.setAutoSize(true);
			modelLabel.setFont(FONT_MEMBER);
			modelLabel.setLocation(12, top);
			top += modelLabel.getHeight() + 1;
			if (modelLabel.getRight() > max_width) max_width = modelLabel.getRight();
			i++;
		}
		
		if (models.length - i > 0) {
			final Label modelLabel = new Label(content, String.format(International.MoreMembersPaticipate, models.length - i));
			modelLabel.setAutoSize(true);
			modelLabel.setFont(FONT_INFO);
			modelLabel.setLocation(12, top);
			top += modelLabel.getHeight() + 1;
			if (modelLabel.getRight() > max_width) max_width = modelLabel.getRight();
		}

		return content;
	}
	
	public DPFrame getDPFrame() {
		return dpFrame;
	}
	
	public DesignPattern getPattern() {
		return pattern;
	}
	
	private double alpha = 0.3;
	private QInterpolator animation = null;
	
	@Override
	protected void update(double dt) {
		super.update(dt);
		if (animation != null) {
			if (animation.hasDone()) {
				alpha = animation.endValue;
				animation = null;
			} else {
				alpha = animation.update(dt);
			}
		}
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.translate(-12, -12);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
		g2.paint3x3(XPanel.IMG_XPANEL_BG, getWidth() + 24, getHeight() + 24);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2.translate(12, 12);
	}
	
	@Override
	public int preferredHeight(Component c) {
		return preferred_height;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return max_width + 8;
	}

}

class DPComment extends InfoPanel {
	
	private final static Font DEF_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);

	public DPComment(Container owner, DesignPattern pattern) {
		super(owner);
		final Label text = new Label(this);
		text.setText(pattern.getSchema().getComment());
		text.setFont(DEF_FONT);
		text.setLocation(2, 2);
		setSize(text.getRight() + 2, text.getBottom() + 2);
	}
	
}
