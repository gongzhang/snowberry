package fatcat.snowberry.core.debug.views;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * 执行调试命令的视图。
 * <p>
 * 准备了三个按钮，可以填上代码用来调试各种方法。
 * </p>
 * <p>
 * 名为“命令按钮”视图。
 * </p>
 * @author 张弓
 *
 */
@SuppressWarnings("restriction")
public class CommandView extends ViewPart {
	
	private Button button1;
	private Button button2;
	private Button button3;

	@Override
	public void createPartControl(Composite parent) {
		
		FillLayout layout = new FillLayout();
		layout.type = SWT.VERTICAL;
		parent.setLayout(layout);
		
		button1 = new Button(parent, SWT.NONE);
		button1.setText("执行1");
		button1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 在此处添加调试代码
//				SnowberryCore.println("Button1 Clicked.");
				
//				SnowberryCore.println(UUID.randomUUID());
			}
		});
		
		button2 = new Button(parent, SWT.NONE);
		button2.setText("执行2");
		button2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 在此处添加调试代码
//				SnowberryCore.println("Button2 Clicked.");
				
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				if (editorPart != null && editorPart instanceof CompilationUnitEditor) {
					CompilationUnitEditor editor = (CompilationUnitEditor) editorPart;
					final TextSelection selection = (TextSelection) editor.getEditorSite().getSelectionProvider().getSelection();
					
					//
					ICompilationUnit iCompilationUnit = JavaCore.createCompilationUnitFrom(((IFileEditorInput) editor.getEditorInput()).getFile());
					ASTParser parser = ASTParser.newParser(AST.JLS3);
					parser.setResolveBindings(true);
					parser.setSource(iCompilationUnit);
					CompilationUnit cu = (CompilationUnit) parser.createAST(null);
					try {
						cu.accept(new ASTVisitor() {
							@Override
							public boolean visit(SimpleName node) {
								if (selection.getOffset() >= node.getStartPosition() &&
									selection.getOffset() <= node.getStartPosition() + node.getLength()) {
									IBinding binding = node.resolveBinding();
									if (binding != null) {
										IJavaElement element = binding.getJavaElement();
										if (element != null) {
											if (element instanceof IType) {
//												SnowberryCore.println(element);
											}
										}
									}
								}
								
								return true;
							}
						});
					} catch (Exception ex) {
					}
//					SnowberryCore.println(selection.getOffset());
				}
			}
		});
		
		button3 = new Button(parent, SWT.NONE);
		button3.setText("执行3");
		button3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 在此处添加调试代码
//				SnowberryCore.println("Button3 Clicked.");
				
//				IProjectModel[] projs = TagCore.getProjectModels();
//				SnowberryCore.println(projs.length);
//				for (IProjectModel proj : projs) {
//					SnowberryCore.println(proj.getResource().getName());
//				}
				
			}
		});
	}

	@Override
	public void setFocus() {
		button1.setFocus();
	}

}
