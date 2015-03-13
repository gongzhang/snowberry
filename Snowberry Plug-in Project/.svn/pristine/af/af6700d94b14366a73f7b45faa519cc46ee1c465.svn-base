package fatcat.snowberry.diagram;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.part.FileEditorInput;


public class DiagramEditorInput implements IEditorInput {
	
	private final FileEditorInput fileEditorInput;
	
	public DiagramEditorInput(FileEditorInput fileEditorInput) {
		this.fileEditorInput = fileEditorInput;
	}
	
	public DiagramEditorInput(IFile file) {
		this(new FileEditorInput(file));
	}
	
	public IFile getFile() {
		return fileEditorInput.getFile();
	}
	
	public FileEditorInput getFileEditorInput() {
		return fileEditorInput;
	}

	@Override
	public boolean exists() {
		return fileEditorInput.exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return fileEditorInput.getImageDescriptor();
	}

	@Override
	public String getName() {
		return fileEditorInput.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return fileEditorInput.getPersistable();
	}

	@Override
	public String getToolTipText() {
		return fileEditorInput.getToolTipText();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof DiagramEditorInput) {
			return ((DiagramEditorInput) obj).getFile().equals(getFile());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getFile().getName().hashCode();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return fileEditorInput.getAdapter(adapter);
	}

}
