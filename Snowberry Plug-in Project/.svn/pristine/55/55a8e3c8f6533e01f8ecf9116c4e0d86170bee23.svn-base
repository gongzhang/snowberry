package fatcat.snowberry.diagram.actions;

import org.eclipse.swt.SWT;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class Ungroup extends Action implements ITagFilter {
	
	private final IMemberModel[] models;
	
	public Ungroup(IMemberModel[] models) {
		this.models = models;
	}

	@Override
	public void fireAction() {
		if (SWT.YES == SnowberryCore.showMsgBox(International.Ungroup, International.ConfirmUngroup, SWT.YES | SWT.NO)) {
			TagTask task = new TagTask();
			for (IMemberModel model : models) {
				task.removeTag(model, this);
			}
			try {
				task.execute();
			} catch (SourceEditingException e) {
				SnowberryCore.showErrMsgBox(International.CannotUngroup, e.getMessage());
			}
		}
	}
	
	@Override
	public boolean isAccepted(ITag tag) {
		return tag.getName().equals("group"); //$NON-NLS-1$
	}

}
