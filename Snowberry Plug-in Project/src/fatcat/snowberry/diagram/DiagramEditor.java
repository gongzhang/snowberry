package fatcat.snowberry.diagram;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

import fatcat.gui.snail.SnailShell;
import fatcat.snowberry.core.International;

/**
 * 这个类已经彻底废弃（不能正常工作）。
 * @see DiagramEditor2
 * @author 张弓
 *
 */
@Deprecated
@SuppressWarnings("restriction")
public class DiagramEditor extends MultiPageEditorPart implements IPageChangedListener {

	CompilationUnitEditor javaEditor = null;
	Composite diagramComposite = null;
	LoadingFrame loadingFrame = null;
	DiagramFrame diagramFrame = null; // will be initialized in LoadingFrame
	SnailShell snailShell = null;
	
	@Override
	protected void createPages() {
		javaEditor = new CompilationUnitEditor();
		
		diagramComposite = new Composite(getContainer(), SWT.EMBEDDED);
//		Frame frame = SWT_AWT.new_Frame(diagramComposite);
//		snailShell = new SnailShell(frame);
//		loadingFrame = new LoadingFrame(snailShell, this);
		loadingFrame.show();
		
		addPageChangedListener(this);
		
		try {
			super.addPage(javaEditor, getEditorInput());
			super.addPage(diagramComposite);
			setPageText(0, International.JavaCode);
			setPageText(1, International.ClassDiagramEditor);
		} catch (PartInitException e) {
//			SnowberryCore.println(e);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		javaEditor.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		javaEditor.doSaveAs();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return javaEditor.isSaveAsAllowed();
	}

	@Override
	public void dispose() {
		if (diagramFrame != null)
			diagramFrame.dispose();
//		if (snailShell != null)
//			snailShell.getFramework().dispose();
		removePageChangedListener(this);
		super.dispose();
	}

	@Override
	public void pageChanged(PageChangedEvent event) {
		if (event.getSelectedPage() == diagramComposite) {
			if (!loadingFrame.hasDone()) loadingFrame.showDiagram();
		}
	}
	
}
