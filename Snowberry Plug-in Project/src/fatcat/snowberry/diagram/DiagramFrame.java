package fatcat.snowberry.diagram;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import fatcat.gui.GraphicsX;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Menu;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.Frame;

import fatcat.gui.snail.SnailShell;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.dp.DPNavigator;
import fatcat.snowberry.diagram.menus.DiagramFrameMenuFactory;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.debug.views.DesignPatternView;
import fatcat.snowberry.gui.LegendPanel;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.ITypeModelListener;
import fatcat.snowberry.tag.TagCore;

public class DiagramFrame extends Frame implements ITypeModelListener {
	
	private final IProjectModel project;
	private final IFile file;
	private final HashMap<ITypeModel, ClassDiagram> diagramMap;
	private final DiagramEditor2 editor;
	private final VirtualContainer areaContainer; // 在arrowContainer下层
	private final VirtualContainer arrowContainer; // 在ClassDiagram下层
	private final VirtualContainer diagramContainer;
	private final VirtualContainer relationContainer; // 在ClassDiagram上层
	private final LegendPanel legendPanel; // 在最上层
	private final DPNavigator navigator; // 在最最上面
	private final VirtualContainer topContainer;

	public DiagramFrame(final SnailShell shell, IFile file, DiagramEditor2 editor) {
		super(shell);
		this.file = file;
		this.editor = editor;
		
		areaContainer = new VirtualContainer(this);
		arrowContainer = new VirtualContainer(this);
		diagramContainer = new VirtualContainer(this); // 都不要改变Owner！！！
		relationContainer = new VirtualContainer(this);
		legendPanel = new LegendPanel(this);
		legendPanel.setVisible(false);
		navigator = new DPNavigator(this); // 都不要改变Owner！！！
		navigator.setVisible(false);
		topContainer = new VirtualContainer(this);
		
		diagramMap = new HashMap<ITypeModel, ClassDiagram>();
		project = TagCore.getProjectModel(file);
		project.addTypeModelListener(this);
		ITypeModel[] typeModels = project.getTypeModels(file);
		for (ITypeModel typeModel : typeModels) {
			this.typeModelAdded(typeModel); // 手工触发
		}
		shell.syncExec(new Runnable() {
			@Override
			public void run() {
				resetDiagramLayout();
			}
		});
	}
	
	public IProjectModel getProject() {
		return project;
	}
	
	@Override
	public void refreshLayout(Container c) {
		if (isInitialized()) {
			legendPanel.setSize();
			legendPanel.setLocation(12, getHeight() - legendPanel.getHeight() - 12);
			navigator.setWidth(getWidth());
		}
	}
	
	public Container getTopContainer() {
		return topContainer;
	}
	
	public LegendPanel getLegendPanel() {
		return legendPanel;
	}
	
	public Container getDiagramContainer() {
		return diagramContainer;
	}
	
	public Container getRelationContainer() {
		return relationContainer;
	}
	
	public Container getArrowContainer() {
		return arrowContainer;
	}
	
	public Container getAreaContainer() {
		return areaContainer;
	}
	
	public DPNavigator getNavigator() {
		return navigator;
	}
	
	public final DiagramEditor2 getEditor() {
		return editor;
	}
	
	private GradientPaint gradientPaint = null; 
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		gradientPaint = new GradientPaint(0, 0, new Color(0xececed), 0, height, new Color(0xfdfdfd));
	}
	
	public static final BufferedImage IMG_BG = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramFrame.Background.png"); //$NON-NLS-1$
	public static final BufferedImage IMG_CORNER = GraphicsX.createImage("/fatcat/snowberry/gui/res/DiagramFrame.Corner.png"); //$NON-NLS-1$
	public static final BufferedImage IMG_FATCAT = GraphicsX.createImage("/fatcat/snowberry/gui/res/FATCAT.png"); //$NON-NLS-1$

	public static final Font FONT_INFO = new Font(Font.DIALOG, Font.PLAIN, 11); //$NON-NLS-1$
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
//		g2.setColor(Color.white);
		
		g2.setPaint(gradientPaint);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		// 使用图片绘制背景会导致性能低下
//		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		g2.drawImage(IMG_BG, 0, 0, getWidth(), getHeight(), null);
//		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	}
	
	@Override
	protected void repaintComponent(GraphicsX g2) {
		super.repaintComponent(g2);	// DONE ！发现空指针异常
		
		if (getWidth() > 800 && getHeight() > 500 && !legendPanel.isVisible()) {
			// 左下角
			g2.drawImage(IMG_CORNER, 0, getHeight() - 60, null);
			g2.setFont(FONT_INFO);
			g2.setColor(Color.gray);
			g2.drawString(International.CopyRight, 75, getHeight() - 20);
			g2.drawString(International.Link, 75, getHeight() - 6);
		}
		
		// 右下角
		g2.drawImage(IMG_FATCAT, getWidth() - IMG_FATCAT.getWidth() - 20, getHeight() - 45, null);
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		for (ITypeModel typeModel : diagramMap.keySet()) {
			ClassDiagram cd = diagramMap.get(typeModel);
			if (cd.getLeft() <= e.getX() && cd.getRight() >= e.getX() &&
				cd.getTop() <= e.getY() && cd.getBottom() >= e.getY()) {
				cd.darken();
			} else {
				cd.lighten();
			}
			
			// if diagram is out of screen, then move it back
			if (!cd.isMoving() && (cd.getLeft() < 0 || cd.getRight() > getWidth() ||
				cd.getTop() < 0 || cd.getBottom() > getHeight())) {
				int left = cd.getLeft(), top = cd.getTop();
				if (left < 0) left = 0;
				else if (cd.getRight() > getWidth()) left = getWidth() - cd.getWidth();
				if (top < 0) top = 0;
				else if (cd.getBottom() > getHeight()) top = getHeight() - cd.getHeight();
				cd.moveTo(left, top);
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		for (int i = diagramContainer.size() - 1; i >= 0; i--) {
			Component c = diagramContainer.get(i);
			if (c instanceof ClassDiagram) {
				ClassDiagram cd = (ClassDiagram) c;
				if (cd.getLeft() <= e.getX() && cd.getRight() >= e.getX() &&
					cd.getTop() <= e.getY() && cd.getBottom() >= e.getY()) {
					cd.bringToTop();
					if (DesignPatternView.getInstance() != null) {
						DesignPatternView.getInstance().showPatternOn(cd.getModel());
					}
					clickedDiagram = cd;
					return;
				}
			}
		}
		clickedDiagram = null;
	}
	
	@Override
	public void mouseReleased(final MouseEvent e) {
		// 触发右键菜单
		// 第一层同步是为了等待Snail组件接收这次鼠标事件，第二层同步是SWT线程同步
		if (e.getButton() == MouseEvent.BUTTON3) {
			getShell().syncExec(new Runnable() {
				@Override
				public void run() {
					editor.diagramComposite.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							// 创建并弹出菜单
							Menu menu = DiagramFrameMenuFactory.createContextMenu(editor, DiagramFrame.this, e);
							if (menu != null) {
								menu.setVisible(true);
							}
						}
					});
				}
			});
		}
	}
	
	@Deprecated
	private ClassDiagram clickedDiagram = null;
	
	@Deprecated
	public final ClassDiagram getClickedDiagram() {
		return clickedDiagram;
	}
	
	public void resetDiagramLayout() {
		int left = 30, top = 30 + (navigator.isVisible() ? navigator.getHeight() : 0);
		for (int i = diagramContainer.size() - 1; i >= 0; i--) {
			Component c = diagramContainer.get(i);
			if (c instanceof ClassDiagram) {
				ClassDiagram cd = (ClassDiagram) c;
				if (!cd.getModel().isPrimaryType() || (cd instanceof ClosableDiagram)) {
					cd.setState(ClassDiagram.STATE_MINIMIZED, true);
					cd.moveTo(left, top);
					top += cd.getPreferredHeight() + 30;
					if (top + 70 > getPreferredHeight()) {
						left += 180;
						top = 30 + (navigator.isVisible() ? navigator.getHeight() : 0);
					}
				} else {
					cd.setState(ClassDiagram.STATE_STANDARD, true);
					cd.moveTo((getWidth() - cd.getPreferredWidth()) / 2, (getHeight() - cd.getPreferredHeight()) / 2);
				}
			}
		}
	}
	
	public void resetDiagramLayout(ClassDiagram primary) {
		int left = 30, top = 30 + (navigator.isVisible() ? navigator.getHeight() : 0);
		for (int i = diagramContainer.size() - 1; i >= 0; i--) {
			Component c = diagramContainer.get(i);
			if (c instanceof ClassDiagram && c != primary) {
				ClassDiagram cd = (ClassDiagram) c;
				cd.setState(ClassDiagram.STATE_MINIMIZED, true);
				cd.moveTo(left, top);
				top += cd.getPreferredHeight() + 30;
				if (top + 70 > getPreferredHeight()) {
					left += 180;
					top = 30 + (navigator.isVisible() ? navigator.getHeight() : 0);
				}
			}
		}
		primary.setState(ClassDiagram.STATE_STANDARD, true);
		primary.moveTo((getWidth() - primary.getPreferredWidth()) / 2, (getHeight() - primary.getPreferredHeight()) / 2);
	}

	@Override
	public void typeModelAdded(final ITypeModel typeModel) {
		if (typeModel.getResource().equals(file)) {
			getShell().syncExec(new Runnable() {
				@Override
				public void run() {
					ClassDiagram cd = new ClassDiagram(DiagramFrame.this, typeModel);
					cd.moveTo(30, 30);
					diagramMap.put(typeModel, cd);
				}
			});
		}
	}

	@Override
	public void typeModelChanged(final ITypeModel typeModel) {
		if (typeModel.getResource().equals(file)) {
			getShell().syncExec(new Runnable() {
				@Override
				public void run() {
					ClassDiagram cd = diagramMap.get(typeModel);
					if (cd != null) {
						cd.modelChanged();
					}
				}
			});
		}
	}

	@Override
	public void typeModelRemoved(final ITypeModel typeModel) {
		if (typeModel.getResource().equals(file)) {
			getShell().syncExec(new Runnable() {
				@Override
				public void run() {
					ClassDiagram cd = diagramMap.get(typeModel);
					if (cd != null) {
						cd.remove();
						cd.dispose();
						diagramMap.remove(typeModel);
					}
				}
			});
		}
	}
	
	public void dispose() {
		for (ITypeModel typeModel : diagramMap.keySet()) {
			ClassDiagram cd = diagramMap.get(typeModel);
			cd.dispose();
		}
		project.removeTypeModelListener(this);
	}
	
	public ClassDiagram openDiagram(ITypeModel model) {
		return openDiagram(model, true);
	}
	
	public ClassDiagram openDiagram(ITypeModel model, boolean auto_location) {
		ClassDiagram cd = diagramMap.get(model);
		if (cd == null) {
			cd = new ClosableDiagram(DiagramFrame.this, model);
			diagramMap.put(model, cd);
			if (auto_location) cd.moveTo(30, 30);
		}
		return cd;
	}
	
	public boolean closeDiagram(ITypeModel model) {
		ClassDiagram cd = diagramMap.get(model);
		if (cd != null && cd instanceof ClosableDiagram) {
			cd.remove();
			cd.dispose();
			diagramMap.remove(model);
			return true;
		} else {
			return false;
		}
	}
	
	public void closeDiagrams() {
		ITypeModel[] models = diagramMap.keySet().toArray(new ITypeModel[0]);
		for (ITypeModel model : models) {
			closeDiagram(model);
		}
	}
	
	public void closeDiagramsExcept(ITypeModel model) {
		ITypeModel[] models = diagramMap.keySet().toArray(new ITypeModel[0]);
		for (ITypeModel m : models) {
			if (!m.equals(model)) closeDiagram(m);
		}
	}
	
	@Deprecated
	public void showDesignPattern(DesignPattern pattern, ITypeModel model) {
		navigator.show(pattern);
	}
	
	public void showDesignPattern(DesignPattern pattern) {
		navigator.show(pattern);
	}
	
}

class VirtualContainer extends Container {
	
	public VirtualContainer(Container owner) {
		super(owner);
		setClip(false);
	}

	@Override
	public int preferredWidth(Component c) {
		return 0;
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 0;
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
	}
	
};
