package fatcat.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.LinkedList;
import java.util.Map;

import javax.imageio.ImageIO;

import fatcat.gui.util.Image3x3;

public class GraphicsX extends Graphics2D {
	
	public static BufferedImage createImage(String res_url) {
		BufferedImage rst = null;
		try {
			rst = ImageIO.read(GraphicsX.class.getResourceAsStream(res_url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rst;
	}
	
	private final Graphics2D g2;
	private Object antialiasingHint, textAntialiasingHint;
	private final LinkedList<Rectangle> clips = new LinkedList<Rectangle>();
	
	public void setAlpha(float alpha) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}
	
	public GraphicsX(Graphics2D g2) {
		this.g2 = g2;
		g2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
		setTextAntialiasingHint(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
	}
	
	public void setAntialiasingHint(Object antialiasingHint) {
		this.antialiasingHint = antialiasingHint;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasingHint);
	}
	
	public void setTextAntialiasingHint(Object textAntialiasingHint) {
		this.textAntialiasingHint = textAntialiasingHint;
	}
	
	public final Graphics2D getGraphics2D() {
		return g2;
	}
	
	public void popClip() {
		g2.setClip(clips.removeLast());
	}
	
	public void pushClip(Rectangle rect) {
		Rectangle clip = g2.getClipBounds();
		clips.addLast(clip);
		final int new_left = java.lang.Math.max(clip.x, rect.x);
		final int new_top = java.lang.Math.max(clip.y, rect.y);
		g2.setClip(new_left, new_top, java.lang.Math.min(clip.x + clip.width, rect.x + rect.width) - new_left, java.lang.Math.min(clip.y + clip.height, rect.y + rect.height) - new_top);
	}
	
	public void paint3x3(final Image3x3 src, final int w, final int h) {
		
		final int x1 = src.x1;
		final int x2 = w - src.width + src.x2;
		final int y1 = src.y1;
		final int y2 = h - src.height + src.y2;
		
		// four corners
		g2.drawImage(src.image, 0, 0, x1, y1, 0, 0, src.x1, src.y1, null);
		g2.drawImage(src.image, x2, 0, w, y1, src.x2, 0, src.width, src.y1, null);
		g2.drawImage(src.image, 0, y2, x1, h, 0, src.y2, src.x1, src.height, null);
		g2.drawImage(src.image, x2, y2, w, h, src.x2, src.y2, src.width, src.height, null);
		
		// four borders
		g2.drawImage(src.image, x1, 0, x2, y1, src.x1, 0, src.x2, src.y1, null);
		g2.drawImage(src.image, x1, y2, x2, h, src.x1, src.y2, src.x2, src.height, null);
		g2.drawImage(src.image, 0, y1, x1, y2, 0, src.y1, src.x1, src.y2, null);
		g2.drawImage(src.image, x2, y1, w, y2, src.x2, src.y1, src.width, src.y2, null);
		
		// center
		g2.drawImage(src.image, x1, y1, x2, y2, src.x1, src.y1, src.x2, src.y2, null);
		
	}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		g2.addRenderingHints(hints);
	}

	@Override
	public void clip(Shape s) {
		g2.clip(s);
	}

	@Override
	public void draw(Shape s) {
		g2.draw(s);
	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		g2.drawGlyphVector(g, x, y);
	}
	
	public final boolean drawImage(Image img, AffineTransform xform) {
		return this.drawImage(img, xform, null);
	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return g2.drawImage(img, xform, obs);
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		g2.drawImage(img, op, x, y);
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		g2.drawRenderableImage(img, xform);
	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		g2.drawRenderedImage(img, xform);
	}

	@Override
	public void drawString(String str, int x, int y) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntialiasingHint);
		g2.drawString(str, x, y);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasingHint);
	}

	@Override
	public void drawString(String str, float x, float y) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntialiasingHint);
		g2.drawString(str, x, y);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasingHint);
	}
	
	public void drawString(String str, double x, double y) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntialiasingHint);
		g2.drawString(str, (float) x, (float) y);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasingHint);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntialiasingHint);
		g2.drawString(iterator, x, y);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasingHint);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntialiasingHint);
		g2.drawString(iterator, x, y);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasingHint);
	}

	@Override
	public void fill(Shape s) {
		g2.fill(s);
	}

	@Override
	public Color getBackground() {
		return g2.getBackground();
	}

	@Override
	public Composite getComposite() {
		return g2.getComposite();
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return g2.getDeviceConfiguration();
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return g2.getFontRenderContext();
	}

	@Override
	public Paint getPaint() {
		return g2.getPaint();
	}

	@Override
	public Object getRenderingHint(Key hintKey) {
		return g2.getRenderingHint(hintKey);
	}

	@Override
	public RenderingHints getRenderingHints() {
		return g2.getRenderingHints();
	}

	@Override
	public Stroke getStroke() {
		return g2.getStroke();
	}

	@Override
	public AffineTransform getTransform() {
		return g2.getTransform();
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return g2.hit(rect, s, onStroke);
	}

	@Override
	public void rotate(double theta) {
		g2.rotate(theta);
	}

	@Override
	public void rotate(double theta, double x, double y) {
		g2.rotate(theta, x, y);
	}

	@Override
	public void scale(double sx, double sy) {
		g2.scale(sx, sy);
	}

	@Override
	public void setBackground(Color color) {
		g2.setBackground(color);
	}

	@Override
	public void setComposite(Composite comp) {
		g2.setComposite(comp);
	}

	@Override
	public void setPaint(Paint paint) {
		g2.setPaint(paint);
	}

	@Override
	public void setRenderingHint(Key hintKey, Object hintValue) {
		g2.setRenderingHint(hintKey, hintValue);
	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		g2.setRenderingHints(hints);
	}

	@Override
	public void setStroke(Stroke s) {
		g2.setStroke(s);
	}

	@Override
	public void setTransform(AffineTransform Tx) {
		g2.setTransform(Tx);
	}

	@Override
	public void shear(double shx, double shy) {
		g2.shear(shx, shy);
	}

	@Override
	public void transform(AffineTransform Tx) {
		g2.transform(Tx);
	}

	@Override
	public void translate(int x, int y) {
		g2.translate(x, y);
	}

	@Override
	public void translate(double tx, double ty) {
		g2.translate(tx, ty);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		g2.clearRect(x, y, width, height);
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		g2.clipRect(x, y, width, height);
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		g2.copyArea(x, y, width, height, dx, dy);
	}

	@Override
	public Graphics create() {
		return g2.create();
	}

	@Override
	public void dispose() {
		g2.dispose();
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		g2.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return g2.drawImage(img, x, y, observer);
	}
	
	public final boolean drawImage(Image img, int x, int y) {
		return this.drawImage(img, x, y, null);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		return g2.drawImage(img, x, y, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		return g2.drawImage(img, x, y, width, height, observer);
	}
	
	public final boolean drawImage(Image img, int x, int y, int width, int height) {
		return this.drawImage(img, x, y, width, height, null);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		return g2.drawImage(img, x, y, width, height, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return g2.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
		return g2.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		g2.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		g2.drawOval(x, y, width, height);
	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		g2.drawPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		g2.drawPolyline(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g2.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		g2.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		g2.fillOval(x, y, width, height);
	}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		g2.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		g2.fillRect(x, y, width, height);
	}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public Shape getClip() {
		return g2.getClip();
	}

	@Override
	public Rectangle getClipBounds() {
		return g2.getClipBounds();
	}

	@Override
	public Color getColor() {
		return g2.getColor();
	}

	@Override
	public Font getFont() {
		return g2.getFont();
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return g2.getFontMetrics(f);
	}

	@Override
	public void setClip(Shape clip) {
		g2.setClip(clip);
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		g2.setClip(x, y, width, height);
	}

	@Override
	public void setColor(Color c) {
		g2.setColor(c);
	}

	@Override
	public void setFont(Font font) {
		g2.setFont(font);
	}

	@Override
	public void setPaintMode() {
		g2.setPaintMode();
	}

	@Override
	public void setXORMode(Color c1) {
		g2.setXORMode(c1);
	}

}
