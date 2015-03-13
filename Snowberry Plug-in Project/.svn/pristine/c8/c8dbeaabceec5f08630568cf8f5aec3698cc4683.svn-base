package fatcat.snowberry.views;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
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
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

import fatcat.gui.snail.SnailShell;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.TagCore;

@SuppressWarnings("restriction")
public class HyperdocView extends ViewPart implements IPartListener2, Runnable {

	private IWorkbenchPage page;
	private CompilationUnitEditor editor;
	private boolean dispose_flag = false;
	private Composite diagramComposite;
	private HyperdocFrame snailFrame;
	
	public CompilationUnitEditor getEditor() {
		return editor;
	}

	@Override
	public void createPartControl(Composite parent) {
		page = getSite().getPage();
		page.addPartListener(this);
		new Thread(this).start();
		
		// snail
		diagramComposite = new Composite(parent, SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(diagramComposite);
		Panel root = new Panel();
		frame.setLayout(new BorderLayout());
		frame.add(root, BorderLayout.CENTER);
		SnailShell snailShell = new SnailShell(root, 10);
		snailFrame = new HyperdocFrame(snailShell, this);
		snailFrame.show();
	}

	@Override
	public void setFocus() {
		diagramComposite.setFocus();
	}
	
	@Override
	public void dispose() {
		snailFrame.getShell().stop();
		dispose_flag = true;
		page.removePartListener(this);
		super.dispose();
	}
	
	IJavaElement old_je = null;
	
	private void refreshElement(IJavaElement element) {
		if (!TagCore.isReady()) return;
		
		if (element.equals(old_je)) return;
		else old_je = element;
		
		if (element instanceof IType) {
			IType type = (IType) element;
			ITypeModel model = TagCore.searchTypeModel(type, null);
			if (model != null) {
				snailFrame.showDoc(model);
			}
		} else if (element instanceof IMethod || element instanceof IField) {
			IJavaElement parent = element.getParent();
			if (parent instanceof IType) {
				IType type = (IType) parent;
				ITypeModel model = TagCore.searchTypeModel(type, null);
				if (model != null) {
					IMemberModel memberModel = model.searchMember(element);
					if (memberModel != null) {
						snailFrame.showDoc(memberModel);
					}
				}
			}
		}
		
	}
	
	@Override
	public void run() {
		try {
			while (!dispose_flag) {
				if (editor != null) {
					try {
						getSite().getShell().getDisplay().syncExec(new Runnable() {
							@Override
							public void run() {
								TextSelection selection = (TextSelection) editor.getSelectionProvider().getSelection();
								refreshCaretPosition(editor, selection.getOffset());
							}
						});
					} catch (Exception e) {
					}
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
		}
	}

	private int old_offset = -1; 
	private IJavaElement old_element = null;
	
	private void refreshCaretPosition(final CompilationUnitEditor editor, final int offset) {
		if (offset == old_offset) return;
		old_offset = offset;
		ICompilationUnit iCompilationUnit = JavaCore.createCompilationUnitFrom(((IFileEditorInput) editor.getEditorInput()).getFile());
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setSource(iCompilationUnit);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		try {
			cu.accept(new ASTVisitor() {
				@Override
				public boolean visit(SimpleName node) {
					if (offset >= node.getStartPosition() &&
						offset <= node.getStartPosition() + node.getLength()) {
						IBinding binding = node.resolveBinding();
						if (binding != null) {
							IJavaElement element = binding.getJavaElement();
							if (element != null && !element.equals(old_element)) {
								refreshElement(element);
								old_element = element;
							}
						}
					}
					return true;
				}
			});
		} catch (Exception ex) {
		}
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		if (part != editor && part instanceof CompilationUnitEditor) {
			editor = (CompilationUnitEditor) part;
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		if (partRef.getPart(false) == editor) {
			editor = null;
		}
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
	}

}
