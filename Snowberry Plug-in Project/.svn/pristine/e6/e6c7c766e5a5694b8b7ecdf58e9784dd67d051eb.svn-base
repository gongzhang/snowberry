package fatcat.snowberry.diagram.actions;

import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IMemberModel;


public class ViewCode extends Action {
	
	private final IMemberModel model;
	
	public ViewCode(final IMemberModel model) {
		this.model = model;
	}

	@Override
	public void fireAction() {
		try {
			IEditorPart editor = IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), model.getResource(), "org.eclipse.jdt.ui.CompilationUnitEditor"); //$NON-NLS-1$
			ISourceRange range = model.getJavaElement().getNameRange();
			if (editor != null && range != null &&
				range.getOffset() != -1) {
				
				// 在编辑器中定位model
				
				if (editor instanceof ITextEditor) {
					((ITextEditor) editor).selectAndReveal(range.getOffset(), range.getLength());
				}
				
			}
		} catch (PartInitException e1) {
			SnowberryCore.showErrMsgBox(International.CannotViewCode, e1.getMessage());
		} catch (JavaModelException e) {
			SnowberryCore.log(Status.ERROR, International.CannotGetSourceRangeBecause, e);
		}
	}

}
