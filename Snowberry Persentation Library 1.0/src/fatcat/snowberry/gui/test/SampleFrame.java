package fatcat.snowberry.gui.test;

import java.awt.Color;
import java.awt.event.MouseEvent;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Frame;
import fatcat.gui.snail.SnailShell;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.gui.DiagramPanel;
import fatcat.snowberry.gui.DiagramPanelTitle;
import fatcat.snowberry.gui.MultilineLabel;
import fatcat.snowberry.gui.StandardContent;
import fatcat.snowberry.gui.StandardItem;


public class SampleFrame extends Frame {
	
	final DiagramPanel dp1, dp2;

	public SampleFrame(SnailShell shell) {
		super(shell);
		
		dp1 = new DiagramPanel(this);
		dp1.setLocation(100, 100);
		dp1.title.setColor(DiagramPanelTitle.COLOR_BLUE);
		dp1.title.setSpace(64);
		dp1.title.setText("你耦合啊~~");
		
		dp2 = new DiagramPanel(this);
		dp2.setLocation(400, 100);
		dp2.title.setColor(DiagramPanelTitle.COLOR_PURPLE);
		dp2.title.setSpace(64);
		dp2.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(Component c) {
				System.out.println(c.getWidth() + ", " + c.getHeight());
			}
		});
		
		
		final StandardContent sc = new StandardContent(dp2.getClientContainer()) {
			@Override
			protected void selectionChanged() {
				StandardItem[] ss = getSelection();
				for (StandardItem s : ss) {
					System.out.println(s.getOwner().indexOf(s));
				}
			}
		};
		CategoryPanel cp1 = new CategoryPanel(sc, "Test", CategoryPanel.COLOR_RED);
		new StandardItem(cp1);
		new StandardItem(cp1);
		new StandardItem(cp1);
		new StandardItem(cp1);
		CategoryPanel cp2 = new CategoryPanel(sc, "Cool", CategoryPanel.COLOR_BLUE);
		new StandardItem(cp2);
		new StandardItem(cp2);
		new StandardItem(cp2);
		new StandardItem(cp2);
		new StandardItem(cp2);
		final CategoryPanel cp3 = new CategoryPanel(sc, "ZG", CategoryPanel.COLOR_GRAY);
		new StandardItem(cp3);
		new StandardItem(cp3);
		new StandardItem(cp3);
		new StandardItem(cp3);
		
		dp2.title.setImage(GraphicsX.createImage("/fatcat/snowberry/gui/res/sample.gif"));
		
//		LengendPanel panel = new LengendPanel(this);
//		LengendItem item1 = new LengendItem(panel, "zhang");
//		LengendItem item2 = new LengendItem(panel, "zhang");
//		LengendItem item3 = new LengendItem(panel, "zhang");
//		LengendItem item4 = new LengendItem(panel, "zhangsdfsdf");
//		panel.setSize();
		
//		InfoPanel panel = new InfoPanel(this);
//		panel.setLocation(300, 100);
		
//		SwitchButton button = new SwitchButton(dp1);
		
		MultilineLabel label = new MultilineLabel(this);
		label.setWidth(50);
		label.setLineSpace(15);
		label.setText("12345678张弓收到的所发生地方90ab\ncd\nef\nghijklmnopqrstuvwxyz");
		label.setWidth(50);
		label.setHeight(label.getPreferredHeight());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (dp1.getLeft() <= e.getX() && dp1.getRight() >= e.getX() &&
			dp1.getTop() <= e.getY() && dp1.getBottom() >= e.getY()) {
			dp1.darken();
		} else {
			dp1.lighten();
		}
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

}
