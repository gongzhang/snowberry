package fatcat.snowberry.diagram;

import java.io.InputStream;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.dp.DPFrame;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.gui.LegendItem;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.dialogs.ModelBrowser;


public class DiagramEditorToolbar extends Composite {
	
	private void setToolItemIcon(ToolItem item, String icon_name, String tool_tip) {
		InputStream is = getClass().getResourceAsStream("/fatcat/snowberry/gui/res/eclipse-icons/" + icon_name);
		org.eclipse.swt.graphics.Image image = new org.eclipse.swt.graphics.Image(getDisplay(), is);
		item.setImage(image);
		item.setToolTipText(tool_tip);
	}
	
	private final ToolBar toolBar;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public DiagramEditorToolbar(Composite parent, final DiagramEditor2 editor) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));

		toolBar = new ToolBar(this, SWT.FLAT);

		final ToolItem btnViewCode = new ToolItem(toolBar, SWT.PUSH);
		btnViewCode.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if (editor.isReady()) {
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editor.fileInput,
								"org.eclipse.jdt.ui.CompilationUnitEditor", true); //$NON-NLS-1$
					} catch (PartInitException e1) {
						SnowberryCore.showErrMsgBox(International.CannotViewCode, e1.getMessage());
					}
				}
			}
		});
//		InputStream is = getClass().getResourceAsStream("/fatcat/snowberry/gui/res/eclipse-icons/jcu_obj.gif");
//		org.eclipse.swt.graphics.Image image = new org.eclipse.swt.graphics.Image(getDisplay(), is);
//		btnViewCode.setImage(image);
		setToolItemIcon(btnViewCode, "jcu_obj.gif", International.OpenWithJavaEditor);
		
		final ToolItem btnDPOverview = new ToolItem(toolBar, SWT.PUSH);
		btnDPOverview.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if (editor.isReady()) {
					editor.dpFrame = new DPFrame(editor.getSnailShell(), editor.diagramFrame);
					editor.dpFrame.show();
				}
			}
		});
//		btnDPOverview.setText("工程总览");
		setToolItemIcon(btnDPOverview, "internal_browser.gif", International.ProjectOverview);
		
		final ToolItem btnDiagramFrame = new ToolItem(toolBar, SWT.PUSH);
		btnDiagramFrame.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if (editor.isReady()) {
					editor.diagramFrame.show();
				}
			}
		});
//		btnDiagramFrame.setText("类图");
		setToolItemIcon(btnDiagramFrame, "class_obj(1).gif", International.ClassDiagram);
		
		@SuppressWarnings("unused")
		final ToolItem sprt1 = new ToolItem(toolBar, SWT.SEPARATOR);

		final ToolItem btnAddDiagram = new ToolItem(toolBar, SWT.PUSH);
//		btnAddDiagram.setText("添加类图");
		setToolItemIcon(btnAddDiagram, "add_obj.gif", International.AddClassDiagram);

		final ToolItem btnLayout = new ToolItem(toolBar, SWT.PUSH);
		btnLayout.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if (editor.isReady()) {
					editor.diagramFrame.resetDiagramLayout();
				}
			}
		});
//		btnLayout.setText("自动排列");
		setToolItemIcon(btnLayout, "site_element(1).gif", International.AutoArrange);

		final ToolItem btnClearDiagrams = new ToolItem(toolBar, SWT.PUSH);
//		btnClearDiagrams.setText("清理");
		setToolItemIcon(btnClearDiagrams, "delete_edit(1).gif", International.Clean);

		@SuppressWarnings("unused")
		final ToolItem sprt2 = new ToolItem(toolBar, SWT.SEPARATOR);

		final ToolItem btnShowAuthor = new ToolItem(toolBar, SWT.CHECK);
//		btnShowAuthor.setText("显示作者");
		setToolItemIcon(btnShowAuthor, "help_search.gif", International.ShowAuthor);

		final ToolItem btnShowRole = new ToolItem(toolBar, SWT.CHECK);
		btnShowRole.addSelectionListener(new SelectionAdapter() {

			final HashMap<String, AuthorPolygon> polyMap = new HashMap<String, AuthorPolygon>();

			public void widgetSelected(final SelectionEvent e) {
				if (btnShowRole.getSelection()) {

					if (btnShowAuthor.getSelection()) {
						SnowberryCore.showErrMsgBox(International.ShowRole, International.PleaseCloseShowAuthor);
						btnShowRole.setSelection(false);
						return;
					}

					if (editor.getDiagramFrame().getNavigator().getCurrentPattern() == null) {
						SnowberryCore.showErrMsgBox(International.ShowRole, International.PleaseOpenDesignPattern);
						btnShowRole.setSelection(false);
						return;
					}

					final DesignPattern pattern = editor.getDiagramFrame().getNavigator().getCurrentPattern();
					final Container areaContainer = editor.getDiagramFrame().getAreaContainer();
					final Component[] cs = editor.getDiagramFrame().getDiagramContainer().getComponents();
					for (Component c : cs) {
						if (c instanceof ClassDiagram) {
							final ClassDiagram cd = (ClassDiagram) c;
							Role role = pattern.getRole(cd.getModel());
							if (role != null) {
								String roleName = role.getName();
								AuthorPolygon poly = polyMap.get(roleName);
								if (poly == null) {
									poly = new AuthorPolygon(areaContainer, cd, roleName);
									polyMap.put(roleName, poly);
									new LegendItem(editor.getDiagramFrame().getLegendPanel(), poly.getColor(), roleName);
								} else {
									poly.addArea(cd);
								}
							}
						}
					}
					editor.getDiagramFrame().getLegendPanel().setVisible(true);
					editor.getDiagramFrame().getLegendPanel().setSize();
					editor.getDiagramFrame().doLayout();
				} else {
					for (String key : polyMap.keySet()) {
						if (!polyMap.get(key).isRemoved()) {
							polyMap.get(key).remove();
						}
					}
					polyMap.clear();
					editor.getDiagramFrame().getLegendPanel().removeAll();
					editor.getDiagramFrame().getLegendPanel().setVisible(false);
				}
			}
		});
//		btnShowRole.setText("显示角色");
		setToolItemIcon(btnShowRole, "genericreggroup_obj.gif", International.ShowRole);
		btnShowAuthor.addSelectionListener(new SelectionAdapter() {

			final HashMap<String, AuthorPolygon> polyMap = new HashMap<String, AuthorPolygon>();

			public void widgetSelected(final SelectionEvent e) {
				if (btnShowAuthor.getSelection()) {

					if (btnShowRole.getSelection()) {
						SnowberryCore.showErrMsgBox(International.ShowRole, International.PleaseCloseShowRole);
						btnShowAuthor.setSelection(false);
						return;
					}

					final Container areaContainer = editor.getDiagramFrame().getAreaContainer();
					final Component[] cs = editor.getDiagramFrame().getDiagramContainer().getComponents();
					for (Component c : cs) {
						if (c instanceof ClassDiagram) {
							final ClassDiagram cd = (ClassDiagram) c;
							String author = cd.getModel().getAuthor();
							AuthorPolygon poly = polyMap.get(author);
							if (poly == null) {
								poly = new AuthorPolygon(areaContainer, cd, author);
								polyMap.put(author, poly);
								new LegendItem(editor.getDiagramFrame().getLegendPanel(), poly.getColor(),
										author == null ? International.Anonymous : author);
							} else {
								poly.addArea(cd);
							}
						}
					}
					editor.getDiagramFrame().getLegendPanel().setVisible(true);
					editor.getDiagramFrame().getLegendPanel().setSize();
					editor.getDiagramFrame().doLayout();
				} else {
					for (String key : polyMap.keySet()) {
						polyMap.get(key).remove();
					}
					polyMap.clear();
					editor.getDiagramFrame().getLegendPanel().removeAll();
					editor.getDiagramFrame().getLegendPanel().setVisible(false);
				}
			}
		});
		btnClearDiagrams.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if (editor.isReady()) {

					if (btnShowRole.getSelection() || btnShowAuthor.getSelection()) {
						SnowberryCore.showErrMsgBox(International.ShowRole, International.PleaseCloseShowAuthorAndShowRole);
						return;
					}

					editor.diagramFrame.closeDiagrams();
					editor.diagramFrame.resetDiagramLayout();
				}
			}
		});
		btnAddDiagram.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if (editor.isReady()) {

					if (btnShowRole.getSelection() || btnShowAuthor.getSelection()) {
						SnowberryCore.showErrMsgBox(International.ShowRole, International.PleaseCloseShowAuthorAndShowRole);
						return;
					}

					Shell shell = SnowberryCore.getDefaultShell();
					ModelBrowser browser = new ModelBrowser(shell) {

						@Override
						protected boolean isLegalResult(Object element) {
							return element instanceof ITypeModel;
						}
					};
					final ITypeModel model = (ITypeModel) browser.open();
					if (model != null) {
						editor.diagramFrame.getShell().syncExec(new Runnable() {
							
							@Override
							public void run() {
								editor.diagramFrame.openDiagram(model);
							}
						});
					}
				}
			}
		});

		final ToolItem newItemToolItem = new ToolItem(toolBar, SWT.CHECK);
		newItemToolItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				editor.diagramFrame.getArrowContainer().setVisible(newItemToolItem.getSelection());
			}
		});
		newItemToolItem.setSelection(true);
//		newItemToolItem.setText("显示连线");
		setToolItemIcon(newItemToolItem, "link_obj.gif", (International.ShowConnection));
		
		final ToolItem btnRemoveLines = new ToolItem(toolBar, SWT.PUSH);
		btnRemoveLines.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				editor.diagramFrame.getArrowContainer().removeAll();
			}
		});
//		btnRemoveLines.setText("清除连线");
		setToolItemIcon(btnRemoveLines, "disconnect_co(1).gif", International.ClearLines);
	}

}
