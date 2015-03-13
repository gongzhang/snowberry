package fatcat.snowberry.diagram.actions;

import org.eclipse.swt.widgets.Shell;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.Category;
import fatcat.snowberry.diagram.dialogs.CategoryEditor;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class NewCategorize extends Action implements ITagFilter {
	
	private final IMemberModel[] models;
	
	public NewCategorize(IMemberModel[] models) {
		this.models = models;
	}

	@Override
	public void fireAction() {
		Shell shell = SnowberryCore.getDefaultShell();
		CategoryEditor editor = new CategoryEditor(shell, null);
		Category rst = editor.open();
		if (rst != null) {
			ITag categoryTag = rst.toTag();
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
	}
	
	@Override
	public boolean isAccepted(ITag tag) {
		return tag.getName().equals("category"); //$NON-NLS-1$
	}

}
