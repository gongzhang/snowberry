package fatcat.snowberry.diagram.actions;

import org.eclipse.swt.SWT;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class RemoveAllLabels extends Action implements ITagFilter {
	
	private final IMemberModel model;
	private final boolean showConfirmDialog;
	
	public RemoveAllLabels(IMemberModel model, boolean showConfirmDialog) {
		this.model = model;
		this.showConfirmDialog = showConfirmDialog;
	}

	@Override
	public void fireAction() {
		if (showConfirmDialog) {
			// DONE （低优先）加上对话框图标
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
		return tag.getName().equals("label"); //$NON-NLS-1$
	}

}
