package fatcat.snowberry.diagram.menus;

import java.awt.event.MouseEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Frame;
import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.Category;
import fatcat.snowberry.diagram.ClassDiagram;
import fatcat.snowberry.diagram.CompositeItemGhost;
import fatcat.snowberry.diagram.ConnectionLine;
import fatcat.snowberry.diagram.DiagramEditor2;
import fatcat.snowberry.diagram.DiagramFrame;
import fatcat.snowberry.diagram.FieldItem;
import fatcat.snowberry.diagram.GroupItem;
import fatcat.snowberry.diagram.Label;
import fatcat.snowberry.diagram.LabelComponent;
import fatcat.snowberry.diagram.MemberItem;
import fatcat.snowberry.diagram.MethodItem;
import fatcat.snowberry.diagram.SelectableDiagramFrame;
import fatcat.snowberry.diagram.actions.Action;
import fatcat.snowberry.diagram.actions.AttachLabel;
import fatcat.snowberry.diagram.actions.Categorize;
import fatcat.snowberry.diagram.actions.Group;
import fatcat.snowberry.diagram.actions.NewCategorize;
import fatcat.snowberry.diagram.actions.RemoveAllLabels;
import fatcat.snowberry.diagram.actions.RemoveLabel;
import fatcat.snowberry.diagram.actions.SearchRelatedModelByLable;
import fatcat.snowberry.diagram.actions.Uncategorize;
import fatcat.snowberry.diagram.actions.Ungroup;
import fatcat.snowberry.diagram.actions.ViewCode;
import fatcat.snowberry.diagram.dialogs.AddLabelDialog;
import fatcat.snowberry.dp.dialogs.DPCreator;
import fatcat.snowberry.gui.DiagramPanelTitle;
import fatcat.snowberry.gui.StandardItem;
import fatcat.snowberry.search.IConnection;
import fatcat.snowberry.search.SearchCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.MultiFileTask;
import fatcat.snowberry.tag.SourceEditingException;

// TODO （低优先）菜单加上图标和快捷键
public class DiagramFrameMenuFactory {
	
	public static Menu createSelectionMenu(final DiagramEditor2 editor, final SelectableDiagramFrame frame, final MouseEvent event) {
		
		// 创建上下文菜单
		Menu popupMenu = new Menu(editor.getDiagramComposite().getShell(), SWT.POP_UP);
		final ClassDiagram[] diagrams = frame.getSelectedDiagrams();
		if (diagrams.length > 0) {
			final IMemberModel[] models = new IMemberModel[diagrams.length];
			for (int i = 0; i < diagrams.length; i++)
				models[i] = diagrams[i].getModel(); 
			
			addItem(popupMenu, International.ReuseDesignPattern, new Action() {
				@Override
				public void fireAction() {
					Shell shell = SnowberryCore.getDefaultShell();
					DPCreator creator = new DPCreator(shell);
					creator.open(models);
				}
			});
			
			new MenuItem(popupMenu, SWT.SEPARATOR);
			
			addItem(popupMenu, International.AttachLabel, new Action() {
				@Override
				public void fireAction() {
					Shell shell = SnowberryCore.getDefaultShell();
					AddLabelDialog dialog = new AddLabelDialog(shell);
					Label l = dialog.open();
					if (l != null) {
						final ITag tag = l.toTag();
						MultiFileTask task = new MultiFileTask();
						for (IMemberModel model : models) {
							task.addTag(model, tag);
						}
						try {
							task.execute();
						} catch (SourceEditingException e) {
							SnowberryCore.showErrMsgBox(International.CannotAttachLabel, e.getMessage());
						}
					}
				}
			});
			
			if (diagrams.length > 1) {
				addItem(popupMenu, International.FindRelation, new Action() {
					
					@Override
					public void fireAction() {
						for (IMemberModel m1 : models) {
							for (IMemberModel m2 : models) {
								if (!m1.equals(m2) && (m1 instanceof ITypeModel) && (m2 instanceof ITypeModel)) {
									createConnection((ITypeModel) m1, (ITypeModel) m2);
								}
							}
						}
					}
					
					private void createConnection(ITypeModel m1, ITypeModel m2) {
						final IConnection connection = SearchCore.createConnection(m1, m2);
						if (connection != null) {
							connection.setLambda(0.5);
							ConnectionLine line = connection.createLine(editor.getDiagramFrame().getArrowContainer(), editor.getDiagramFrame().openDiagram(m2), editor.getDiagramFrame().openDiagram(m1));
							line.refreshLocation();
						}
					}
					
				});
			}
			
		}
		return popupMenu;
	}
	
	public static Menu createContextMenu(final DiagramEditor2 editor, final DiagramFrame frame, final MouseEvent event) {
		
		// 创建上下文菜单
		Menu popupMenu = new Menu(editor.getDiagramComposite().getShell(), SWT.POP_UP);
		
		// 鼠标点在何种组件上
		final Component mouseOn = frame.getComponentOn(event.getX(), event.getY());
		
		if (mouseOn instanceof MemberItem<?>) {
			createItemMenu(popupMenu, (MemberItem<?>) mouseOn);
		} else if (mouseOn instanceof CompositeItemGhost) {
			createItemMenu(popupMenu, ((CompositeItemGhost) mouseOn).getItem());
		} else if (mouseOn instanceof DiagramPanelTitle) {
			createDiagramMenu(popupMenu, (ClassDiagram) ((DiagramPanelTitle) mouseOn).getOwner());
		} else if (mouseOn instanceof ClassDiagram) {
			createDiagramMenu(popupMenu, (ClassDiagram) mouseOn);
		} else if (mouseOn instanceof Frame) {
			createBackgroudMenu(popupMenu, frame);
		} else if (mouseOn instanceof LabelComponent) {
			createLabelMenu(popupMenu, (LabelComponent) mouseOn);
		}
		
		return popupMenu;
	}
	
	private static MenuItem addItem(final Menu popupMenu, final String text, final Action action) {
		MenuItem item = new MenuItem(popupMenu, SWT.PUSH);
		item.setText(text);
		item.addSelectionListener(action);
		return item;
	}
	
	//// menu contribution ////

	private static void createItemMenu(final Menu popupMenu, final MemberItem<?> item) {
		if (item instanceof FieldItem || item instanceof MethodItem) {
			addItem(popupMenu, International.ViewCode, new ViewCode((IMemberModel) item.getModel()));
			new MenuItem(popupMenu, SWT.SEPARATOR);
			
			addItem(popupMenu, International.AttachLabel, new AttachLabel((IMemberModel) item.getModel()));
			addItem(popupMenu, International.RemoveAllLabels, new RemoveAllLabels((IMemberModel) item.getModel(), true));
			new MenuItem(popupMenu, SWT.SEPARATOR);
		}
		
		final ClassDiagram cd = item.getDiagram();
		final StandardItem[] items = cd.getSelectedItems();
		final IMemberModel[] models = cd.getSelectedModels();
		
		if (items.length > 1) {
			addItem(popupMenu, International.Group2, new Group(models));
			new MenuItem(popupMenu, SWT.SEPARATOR);
		} else {
			if (items[0] instanceof GroupItem) {
				addItem(popupMenu, International.Ungroup2, new Ungroup(models));
				new MenuItem(popupMenu, SWT.SEPARATOR);
			} else if (items[0] instanceof FieldItem) {
				// show as association
				final FieldItem fieldItem = (FieldItem) items[0];
				if (!fieldItem.isShownAsAssociation()) {
					addItem(popupMenu, International.ShowAsAssociation, new Action() {
						@Override
						public void fireAction() {
							fieldItem.showAsAssociation();
						}
					});
				} else {
					addItem(popupMenu, International.ShowAsField, new Action() {
						@Override
						public void fireAction() {
							fieldItem.showAsField();
						}
					});
				}
				new MenuItem(popupMenu, SWT.SEPARATOR);
			}
		}
		
		final MenuItem categoriesItem = new MenuItem(popupMenu, SWT.CASCADE);
		categoriesItem.setText(International.CategoryTo);
		final Menu categoriesMenu = new Menu(categoriesItem);
		categoriesItem.setMenu(categoriesMenu);
		
		final Category[] categories = cd.getAllCategories(false);
		for (Category category : categories) {
			addItem(categoriesMenu, category.name, new Categorize(models, category));
		}
		
		new MenuItem(categoriesMenu, SWT.SEPARATOR);
		
		addItem(categoriesMenu, International.NewCategory2, new NewCategorize(models));
		addItem(popupMenu, International.CancelCategory, new Uncategorize(models));
		
	}
	
	private static void createDiagramMenu(final Menu popupMenu, final ClassDiagram cd) {
		addItem(popupMenu, International.ViewCode, new ViewCode(cd.getModel()));
		addItem(popupMenu, International.CloseOtherDiagrams, new Action() {
			
			@Override
			public void fireAction() {
				cd.getDiagramFrame().closeDiagramsExcept(cd.getModel());
				cd.getDiagramFrame().resetDiagramLayout(cd);
			}
			
		});
		new MenuItem(popupMenu, SWT.SEPARATOR);
		addItem(popupMenu, International.AttachLabel, new AttachLabel(cd.getModel()));
		addItem(popupMenu, International.RemoveAllLabels, new RemoveAllLabels(cd.getModel(), true));
	}
	
	private static void createBackgroudMenu(final Menu popupMenu, final DiagramFrame frame) {
		
	}
	
	private static void createLabelMenu(final Menu popupMenu, final LabelComponent labelComponent) {
		addItem(popupMenu, International.SearchRelatedMembers, new SearchRelatedModelByLable(labelComponent.getLabel()));
		addItem(popupMenu, International.RemoveLabel, new RemoveLabel(labelComponent.getLabel().model == null ? labelComponent.getModel() : labelComponent.getLabel().model, true, labelComponent.getLabel()));
	}

}
