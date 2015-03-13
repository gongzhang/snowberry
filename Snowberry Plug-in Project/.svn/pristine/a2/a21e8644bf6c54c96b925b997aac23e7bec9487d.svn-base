package fatcat.snowberry.diagram;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import fatcat.gui.GraphicsX;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Menu;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.SnailShell;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.diagram.menus.DiagramFrameMenuFactory;

/**
 * 提供选择功能的类图编辑器。
 * 
 * @author 张弓
 *
 */
public class SelectableDiagramFrame extends DiagramFrame {
	
	private final LinkedList<ClassDiagram> selectedDiagrams; 
	
	private final VirtualContainer selectionAreaContainer;
	private final VirtualContainer selectionBorderContainer;

	public SelectableDiagramFrame(SnailShell shell, IFile file, DiagramEditor2 editor) {
		super(shell, file, editor);
		selectedDiagrams = new LinkedList<ClassDiagram>();
		
		selectionAreaContainer = new VirtualContainer(this);
		selectionBorderContainer = new VirtualContainer(this);
		
		selectionArea = new SelectionArea(selectionAreaContainer);
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(Component c, MouseEvent e, int x, int y) {
				SelectableDiagramFrame.this.mousePressed(c, e, x, y);
			}
			
			@Override
			public void mouseReleased(Component c, MouseEvent e, int x, int y) {
				SelectableDiagramFrame.this.mouseReleased(c, e, x, y);
			}
			
			@Override
			public void mouseDragged(Component c, MouseEvent e, int x, int y) {
				SelectableDiagramFrame.this.mouseDragged(c, e, x, y);
			}
			
		});
	}
	
	public ClassDiagram[] getSelectedDiagrams() {
		return selectedDiagrams.toArray(new ClassDiagram[0]);
	}
	
	private int old_x, old_y;
	private final SelectionArea selectionArea;
	private final HashMap<ClassDiagram, Point> diagramMap = new HashMap<ClassDiagram, Point>();
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		selectionBorderContainer.removeAll();
		selectedDiagrams.clear();
	}
	
	private void mousePressed(Component c0, MouseEvent e, int x, int y) {
		old_x = x;
		old_y = y;
		selectionArea.polygon.reset();
		diagramMap.clear();
		selectedDiagrams.clear();
		Component[] cs = getDiagramContainer().getComponents();
		for (Component c : cs) {
			if (c instanceof ClassDiagram) {
				diagramMap.put((ClassDiagram) c, new Point(c.getLeft() + c.getWidth() / 2, c.getTop() + c.getHeight() / 2));
			}
		}
		// start
	}
	
	private void mouseReleased(Component c, final MouseEvent e, int x, int y) {
		// over
		if (selectionArea.polygon.npoints > 2) {
			// select operation
			getShell().syncExec(new Runnable() {
				@Override
				public void run() {
					getEditor().diagramComposite.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							// 创建并弹出菜单
							Menu menu = DiagramFrameMenuFactory.createSelectionMenu(getEditor(), SelectableDiagramFrame.this, e);
							if (menu != null) {
								menu.setVisible(true);
							}
						}
					});
				}
			});
		} else {
			// cancel select operation
		}
		selectionArea.polygon.reset();
//		selectionBorderContainer.removeAll();
//		selectedDiagrams.clear();
	}
	
	private void mouseDragged(Component c, MouseEvent e, int x, int y) {
		if ((old_x - x) * (old_x - x) + (old_y - y) * (old_y - y) > 500) {
			old_x = x;
			old_y = y;
			// new point
			selectionArea.polygon.addPoint(x, y);
			selectionBorderContainer.removeAll();
			selectedDiagrams.clear();
			for (ClassDiagram cd : diagramMap.keySet()) {
				if (selectionArea.polygon.contains(diagramMap.get(cd))) {
					SelectionBorder border = new SelectionBorder(selectionBorderContainer);
					border.setBounds(cd);
					selectedDiagrams.add(cd);
				}
			}
		}
	}

}

class SelectionBorder extends Component {
	
	final Color color = Color.yellow.brighter();
	final Stroke stroke = new BasicStroke(2.0f);

	public SelectionBorder(Container owner) {
		super(owner);
		setFocusable(false);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setStroke(stroke);
		g2.setColor(color);
		g2.drawRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
	}
	
}

class SelectionArea extends VirtualContainer {
	
	final Polygon polygon = new Polygon();
	final Stroke stroke = new BasicStroke(1.0f,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND);
	final Color color = new Color(0x44ccff);

	public SelectionArea(Container owner) {
		super(owner);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		if (polygon.npoints > 1) {
			g2.setStroke(stroke);
			g2.setColor(color);
			g2.draw(polygon);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
			g2.fill(polygon);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
	}
	
}
