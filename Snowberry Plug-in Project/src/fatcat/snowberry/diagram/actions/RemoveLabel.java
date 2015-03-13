package fatcat.snowberry.diagram.actions;

import org.eclipse.swt.SWT;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.Label;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class RemoveLabel extends Action implements ITagFilter {
	
	private final IMemberModel model;
	private final boolean showConfirmDialog;
	private final Label label;
	
	public RemoveLabel(IMemberModel model, boolean showConfirmDialog, Label label) {
		this.model = model;
		this.showConfirmDialog = showConfirmDialog;
		this.label = label;
	}

	@Override
	public void fireAction() {
		if (showConfirmDialog) {
			if (SWT.NO == SnowberryCore.showMsgBox(International.RemoveLabel, International.ConfirmRemoveLabel, SWT.YES | SWT.NO)) {
				return;
			}
		}
		TagTask task = new TagTask();
		task.removeTag(model, this);
		try {
			task.execute();
		} catch (SourceEditingException e) {
			SnowberryCore.showErrMsgBox(International.CannotRemoveLabel, e.getMessage());
		}
	}

	@Override
	public boolean isAccepted(ITag tag) {
		if (!tag.getName().equals("label")) return false; //$NON-NLS-1$
		if (label.name.equals(tag.getPropertyValue("name"))) return true; //$NON-NLS-1$
		else return false;
	}

}
