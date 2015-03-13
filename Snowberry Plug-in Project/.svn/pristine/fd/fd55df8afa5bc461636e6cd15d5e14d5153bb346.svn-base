package fatcat.snowberry.diagram;

import fatcat.gui.GraphicsX;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedList;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;

import fatcat.gui.snail.SnailShell;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.gui.Arrow;
import fatcat.snowberry.gui.Button;
import fatcat.snowberry.gui.DiagramPanelTitle;
import fatcat.snowberry.gui.SwitchButton;
import fatcat.snowberry.search.IConnection;
import fatcat.snowberry.search.IResultListener;
import fatcat.snowberry.search.SearchCore;
import fatcat.snowberry.tag.ITypeModel;


public class DiagramToolBar extends Container {
	
	private final ClassDiagram diagram;

	public DiagramToolBar(DiagramPanelTitle owner, ClassDiagram cd) {
		super(owner);
		diagram = cd;
		initButtons();
	}
	
	public ClassDiagram getDiagram() {
		return diagram;
	}
	
	//// buttons ////
	
	private SwitchButton btnShowBubble;
	private SwitchButton btnShowHierarchy;
	private SwitchButton btnShowInFamily;
	private Button btnAnyConnection;
	private SwitchButton btnShowInfo;
	
	public static final BufferedImage ICON_SHOW_BUBBLE = GraphicsX.createImage("/icons/label.png");
	public static final BufferedImage ICON_SHOW_SUPER = GraphicsX.createImage("/icons/up.png");
	public static final BufferedImage ICON_SHOW_SUB = GraphicsX.createImage("/icons/down.png");
	public static final BufferedImage ICON_SHOW_INFO = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/info_obj.gif");
	public static final BufferedImage ICON_SHOW_TYPES = GraphicsX.createImage("/icons/connect.png");
	
	private void initButtons() {
		btnShowBubble = new SwitchButton(this);
		btnShowBubble.setImage(ICON_SHOW_BUBBLE);
		btnShowBubble.addMouseListener(new MouseAdapter() {
			
			private LabelBubble bubble = null;
			
			@Override
			public void mouseReleased(final Component c, MouseEvent e, int x, int y) {
				if (btnShowBubble.isSelected()) {
					final ITypeModel model = diagram.getModel();
					if (Label.parseLabels(model).length != 0) {
						bubble = new LabelBubble(diagram, model);
						getShell().syncExec(new Runnable() {
							@Override
							public void run() {
								bubble.setLocation(
									c.getAbsLeft() - diagram.getAbsLeft() - bubble.getWidth() - 4,
									c.getAbsTop() - diagram.getAbsTop() + (getHeight() - bubble.getHeight()) / 2
								);
							}
						});
						
					} else {
						btnShowBubble.setState(SwitchButton.UP);
						btnShowBubble.setSelected(false);
					}
				} else {
					if (bubble != null) {
						bubble.remove();
						bubble = null;
					}
				}
			}
			
		});
		
		btnShowHierarchy = new SwitchButton(this);
		btnShowHierarchy.setImage(ICON_SHOW_SUPER);
		btnShowHierarchy.addMouseListener(new MouseAdapter() {
			
			LinkedList<ClassDiagram> superDiagrams = new LinkedList<ClassDiagram>();
			LinkedList<HierarchyArrow> arrows = new LinkedList<HierarchyArrow>();

			@Override
			public void mouseReleased(Component c, MouseEvent e, int x, int y) {
				if (btnShowHierarchy.isSelected()) {
					final ITypeModel superTypeModel = diagram.getModel().getSuperType();
					if (superTypeModel != null) {
						open(superTypeModel).moveTo(diagram.getLeft(), diagram.getTop() - 100);
					}
					final ITypeModel[] interfaceModels = diagram.getModel().getInterfaces();
					int top = diagram.getTop();
					int left = diagram.getRight() + 30;
					for (ITypeModel interfaze : interfaceModels) {
						open(interfaze).moveTo(left, top);
						top += 100;
					}
					if (superTypeModel == null && interfaceModels.length == 0) {
						btnShowHierarchy.setState(SwitchButton.UP);
						btnShowHierarchy.setSelected(false);
					}
				} else {
					while (superDiagrams.size() > 0) {
						ClassDiagram cd = superDiagrams.removeLast();
						if (cd instanceof ClosableDiagram && !cd.isRemoved()) {
							((ClosableDiagram) cd).close();
						}
					}
					while (arrows.size() > 0) {
						Arrow a = arrows.removeLast();
						if (!a.isRemoved()) a.remove();
					}
				}
			}
			
			private ClassDiagram open(ITypeModel model) {
				ClassDiagram superTypeDiagram = diagram.getDiagramFrame().openDiagram(model, false);
				superTypeDiagram.setLocation(getDiagram());
				superTypeDiagram.setState(ClassDiagram.STATE_MINIMIZED, true);
				superDiagrams.add(superTypeDiagram);
				Container arrow_container = diagram.getDiagramFrame().getArrowContainer();
				arrows.add(new HierarchyArrow(arrow_container, diagram, superTypeDiagram));
				return superTypeDiagram;
			}
			
		});
	
		btnShowInFamily = new SwitchButton(this);
		btnShowInFamily.setImage(ICON_SHOW_SUB);
		btnShowInFamily.addMouseListener(new MouseAdapter() {
			
			LinkedList<ClassDiagram> superDiagrams = new LinkedList<ClassDiagram>();
			LinkedList<HierarchyArrow> arrows = new LinkedList<HierarchyArrow>();

			@Override
			public void mouseReleased(Component c, MouseEvent e, int x, int y) {
				if (btnShowInFamily.isSelected()) {
					diagram.setState(ClassDiagram.STATE_MINIMIZED, true);
					final ITypeModel[] subClasses = diagram.getModel().getKnownSubTypes();
					int top = diagram.getTop() + 100;
					int left = diagram.getLeft() - (subClasses.length) * 90 + 90;
					for (ITypeModel sub : subClasses) {
						open(sub).moveTo(left, top);
						left += 180;
					}
					
					if (subClasses.length == 0) {
						btnShowInFamily.setState(SwitchButton.UP);
						btnShowInFamily.setSelected(false);
					}
				} else {
					while (superDiagrams.size() > 0) {
						ClassDiagram cd = superDiagrams.removeLast();
						if (cd instanceof ClosableDiagram && !cd.isRemoved()) {
							((ClosableDiagram) cd).close();
						}
					}
					while (arrows.size() > 0) {
						Arrow a = arrows.removeLast();
						if (!a.isRemoved()) a.remove();
					}
				}
			}
			
			private ClassDiagram open(ITypeModel model) {
				ClassDiagram superTypeDiagram = diagram.getDiagramFrame().openDiagram(model, false);
				superTypeDiagram.setLocation(getDiagram());
				superTypeDiagram.setState(ClassDiagram.STATE_MINIMIZED, true);
				superDiagrams.add(superTypeDiagram);
				Container arrow_container = diagram.getDiagramFrame().getArrowContainer();
				arrows.add(new HierarchyArrow(arrow_container, superTypeDiagram, diagram));
				return superTypeDiagram;
			}
			
		});

		btnAnyConnection = new Button(this);
		btnAnyConnection.setImage(ICON_SHOW_TYPES);
		btnAnyConnection.addMouseListener(new MouseAdapter() {
			
			private boolean busy = false;
			
			@Override
			public void mouseReleased(Component c, MouseEvent e, int x, int y) {
				if (busy) return;
				
				getDiagram().getDiagramFrame().getArrowContainer().removeAll();
				
				if (btnShowBubble.isSelected()) cancelButton(btnShowBubble, e);
				if (btnShowHierarchy.isSelected()) cancelButton(btnShowHierarchy, e);
				if (btnShowInFamily.isSelected()) cancelButton(btnShowInFamily, e);
				if (btnShowInfo.isSelected()) cancelButton(btnShowInfo, e);
				final DiagramFrame frame = diagram.getDiagramFrame();
				final SnailShell shell = frame.getShell();
				final int cx = frame.getWidth() / 2 - 75;
				final int cy = frame.getHeight() / 2 - 35;
				frame.closeDiagramsExcept(diagram.getModel());
				frame.resetDiagramLayout(diagram);
				diagram.setState(ClassDiagram.STATE_MINIMIZED, true);
				diagram.moveTo(cx, cy);
				busy = true;
				
				SearchCore.anyConnection2(new IResultListener<IConnection>() {
					
//					private final double min_r = Math.min(frame.getWidth(), frame.getHeight()) / 2.0 - 35 - 20;
					private final double max_r = Math.sqrt(frame.getWidth() * frame.getWidth() + frame.getHeight() * frame.getHeight()) / 2.0;
					private final double radium_w = frame.getWidth() / 2;
					private final double radium_h = frame.getHeight() / 2;
					private int result_count = 0;
					private HashSet<IConnection> rst = new HashSet<IConnection>();
					
					public boolean gotResult(final IConnection result) {
						rst.add(result);
						result_count++;
						if (result_count > 5) return true;
						return false;
					}
					
					@Override
					public void done(boolean stoppedManually) {
						busy = false;
						shell.syncExec(new Runnable() {
							@Override
							public void run() {
								if (rst.size() == 0) return;
								double theta = 0.0;
								final double theta_delta = 2.0 * Math.PI / rst.size();
								double min_lambda = 10.0, max_lambda = 0.0;
								for (IConnection connection : rst) {
									if (connection.getLambda() < min_lambda) {
										min_lambda = connection.getLambda();
									}
									if (connection.getLambda() > max_lambda) {
										max_lambda = connection.getLambda();
									}
								}
								if (max_lambda != min_lambda) {
									for (IConnection connection : rst) {
										connection.setLambda((connection.getLambda() - min_lambda) / (max_lambda - min_lambda));
									}
								} else {
									for (IConnection connection : rst) {
										connection.setLambda(0.5);
									}
								}
								
								for (IConnection connection : rst) {
									final ClassDiagram cd = frame.openDiagram(connection.getResult(), false);
									if (cd != null) {
										final double lambda = (1.0f - connection.getLambda() / 3) * 0.8;
										final double x = Math.sin(theta) * lambda;
										final double y = -Math.cos(theta) * lambda;
										theta += theta_delta;
										cd.setState(ClassDiagram.STATE_MINIMIZED, false);
										cd.setLocation(cx + (int) (x * max_r), cy + (int) (y * max_r));
										cd.moveTo(cx + (int) (x * radium_w), cy + (int) (y * radium_h));
										connection.createLine(frame.getArrowContainer(), cd, diagram);
									}
								}
								
								rst.clear();
							}
						});
						
						
					}
					
				}, diagram.getModel());
			}
			
			private void cancelButton(SwitchButton btn, MouseEvent e) {
				btn.fireMouseReleased(e);
				btn.setState(SwitchButton.NORMAL);
			}
			
		});
		
		btnShowInfo = new SwitchButton(this);
		btnShowInfo.setImage(ICON_SHOW_INFO);
		btnShowInfo.addMouseListener(new MouseAdapter() {
			
			private InfoBox box = null;
			
			@Override
			public void mouseReleased(Component c, final MouseEvent e, int x, int y) {
				if (btnShowInfo.isSelected()) {
					box = new InfoBox(diagram);
					box.setLocation(diagram.getWidth() + 8, 0);
					box.addComponentListener(new ComponentAdapter() {
						@Override
						public void componentRemoved(Component c) {
							if (btnShowInfo.isSelected()) {
								btnShowInfo.fireMouseReleased(e);
								btnShowInfo.setState(SwitchButton.NORMAL);
							}
						}
					});
				} else {
					if (box != null && !box.isRemoved()) {
						box.remove();
						box = null;
					}
				}
			}
		});
	}
	
	//// layout ////
	@Override
	public void refreshLayout(Container c) {
		btnShowBubble.setLeft(2);
		btnShowHierarchy.setLeft(btnShowBubble.getRight() + 2);
		btnShowInFamily.setLeft(btnShowHierarchy.getRight() + 2);
		btnAnyConnection.setLeft(btnShowInFamily.getRight() + 2);
		
		btnShowInfo.setLeft(c.getWidth() - 2 - btnShowInfo.getWidth());
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
	}

}
