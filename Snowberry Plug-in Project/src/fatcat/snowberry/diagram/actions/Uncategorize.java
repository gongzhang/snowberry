package fatcat.snowberry.diagram.actions;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class Uncategorize extends Action implements ITagFilter {
	
	private final IMemberModel[] models;
	
	public Uncategorize(IMemberModel[] models) {
		this.models = models;
	}

	@Override
	public void fireAction() {
		TagTask task = new TagTask();
		for (IMemberModel model : models) {
			task.removeTag(model, this);
		}
		try {
			task.execute();
		} catch (SourceEditingException e1) {
			SnowberryCore.showErrMsgBox(International.CannotUncategory, e1.getMessage());
		}
	}
	
	@Override
	public boolean isAccepted(ITag tag) {
		return tag.getName().equals("category"); //$NON-NLS-1$
	}

}
