package fatcat.snowberry.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import fatcat.gui.GraphicsX;
import java.awt.image.BufferedImage;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.util.Image3x3;


public class XPanel extends Container {
	
	private int barWidth;
	private float alpha;

	public XPanel(Container owner) {
		super(owner);
		setBarWidth(128);
		setAlpha(0.0f);
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}
	
	public int getBarWidth() {
		return barWidth;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return 128;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 128;
	}
	
	//// paint ////
	
	public static final Image3x3 IMG_XPANEL_BG = new Image3x3("/fatcat/snowberry/gui/res/XPanel.Background.png", 12, 163, 12, 92);
	public static final BufferedImage IMG_XPANEL_BAR = GraphicsX.createImage("/fatcat/snowberry/gui/res/XPanel.Bar.png");
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.translate(-12, -12);
		g2.paint3x3(IMG_XPANEL_BG, getWidth() + 24, getHeight() + 24);
		g2.translate(12, 12);
		g2.translate(-4, getHeight() / 2 - 28);
		g2.drawImage(IMG_XPANEL_BAR, -8, 0, 0, 56, 189, 0, 197, 56, null);
		g2.drawImage(IMG_XPANEL_BAR, -barWidth, 0, -8, 56, 10, 0, 189, 56, null);
		g2.drawImage(IMG_XPANEL_BAR, -barWidth - 10, 0, -barWidth, 56, 0, 0, 10, 56, null);
		g2.translate(4, -getHeight() / 2 + 28);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	protected void repaint(GraphicsX g2) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		super.repaint(g2);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

}
