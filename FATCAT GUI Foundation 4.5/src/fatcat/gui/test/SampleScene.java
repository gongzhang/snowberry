package fatcat.gui.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Random;

import fatcat.gui.Component;
import fatcat.gui.GUIFramework;
import fatcat.gui.GraphicsX;
import fatcat.gui.Scene;

public class SampleScene extends Scene {
	
	Font font = new Font("微软雅黑", Font.PLAIN, 14);
	
	public SampleScene(GUIFramework fw) {
		super(fw);
		
	}
	
	@Override
	protected void repaintScene(GraphicsX g2) {
		g2.setColor(new java.awt.Color(15, 155, 255));
		g2.fillRect(0, 0, getScreenWidth(), getScreenHeight());
		g2.setColor(java.awt.Color.white);
		g2.setFont(font);
		g2.drawString("lskdfjlskdjflskj测试！", 3, 30);
	}
	
	@Override
	protected void updateScene(double dt) {
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		this.append(new Box(this));
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		this.append(new Box(this));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < 10; i++)
			this.append(new Box(this));
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (r.nextDouble() < 0.4)
			this.append(new Dot(this, e.getX(), e.getY()));
	}
	
	Random r = new Random();
	
}

class Box extends Component {
	
	final SampleScene owner;
	double x, y;
	double dx, dy;
	
	int r, g, b;
	
	Box(SampleScene owner) {
		this.owner = owner;
		x = Math.abs(owner.r.nextInt()) % owner.getScreenWidth();
		y = Math.abs(owner.r.nextInt()) % owner.getScreenHeight();
		dx = owner.r.nextDouble() * 50.0 - 25.0;
		dy = owner.r.nextDouble() * 50.0 - 25.0;
		r = Math.abs(owner.r.nextInt()) % 255;
		g = Math.abs(owner.r.nextInt()) % 255;
		b = Math.abs(owner.r.nextInt()) % 255;
	}
	
	Box(SampleScene owner, int x, int y) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		dx = owner.r.nextDouble() * 300.0 - 150.0;
		dy = owner.r.nextDouble() * 300.0 - 150.0;
		r = Math.abs(owner.r.nextInt()) % 255;
		g = Math.abs(owner.r.nextInt()) % 255;
		b = Math.abs(owner.r.nextInt()) % 255;
	}
	
	@Override
	protected void repaint(GraphicsX g2) {
		g2.setColor(new Color(r, g, b));
		g2.fillRoundRect((int) x - 40, (int) y - 40, 80, 80, 8, 8);
	}
	
	@Override
	protected void update(double dt) {
		
		x += dt * dx;
		y += dt * dy;
		
		if (x < 0 || y < 0 || x > owner.getScreenWidth() || y > owner.getScreenHeight())
			owner.remove(this);
	}
	
}

class Dot extends Component {
	
	final SampleScene owner;
	double x, y;
	double dx, dy;
	
	int r, g, b;
	int ra = 15;
	
	Dot(SampleScene owner, int x, int y) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		dx = owner.r.nextDouble() * 300.0 - 150.0;
		dy = owner.r.nextDouble() * 300.0 - 150.0;
		r = Math.abs(owner.r.nextInt()) % 255;
		g = Math.abs(owner.r.nextInt()) % 255;
		b = Math.abs(owner.r.nextInt()) % 255;
	}
	
	@Override
	protected void repaint(GraphicsX g2) {
		g2.setColor(new Color(r, g, b));
		g2.fillOval((int) x - ra, (int) y - ra, ra * 2, ra * 2);
	}
	
	@Override
	protected void update(double dt) {
		
		ra -= dt * 100;
		x += dt * dx;
		y += dt * dy;
		
		if (ra <= 0 || x < 0 || y < 0 || x > owner.getScreenWidth() || y > owner.getScreenHeight())
			owner.remove(this);
	}
	
}
