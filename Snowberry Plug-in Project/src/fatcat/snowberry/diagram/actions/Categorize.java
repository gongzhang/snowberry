package fatcat.snowberry.diagram.actions;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.Category;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class Categorize extends Action implements ITagFilter {
	
	private final IMemberModel[] models;
	private final Category category;
	
	public Categorize(IMemberModel[] models, Category category) {
		this.models = models;
		this.category = category;
	}

	@Override
	public void fireAction() {
		ITag categoryTag = category.toTag();
		TagTask task = new TagTask();
		for (IMemberModel model : models) {
			task.removeTag(model, this);
			task.addTag(model, categoryTag);
		}
		try {
			task.execute();
		} catch (SourceEditingException e1) {
			SnowberryCore.showErrMsgBox(International.CannotFinishCategorizeOperation, e1.getMessage());
		}
	}
	
	@Override
	public boolean isAccepted(ITag tag) {
		return tag.getName().equals("category"); //$NON-NLS-1$
	}

}
