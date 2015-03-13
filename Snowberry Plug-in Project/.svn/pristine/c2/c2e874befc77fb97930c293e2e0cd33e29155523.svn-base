package fatcat.snowberry.tag.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;
import fatcat.snowberry.tag.ui.TagEditor;
import fatcat.snowberry.tag.views.TagManager;


public class AddTagAction implements IViewActionDelegate {
	
	private TagManager tagManager = null;

	@Override
	public void init(IViewPart view) {
		tagManager = (TagManager) view;
	}

	@Override
	public void run(IAction action) {
		IMemberModel memberModel = tagManager.getCurrentMemberModel();
		if (memberModel != null) {
			Shell shell = SnowberryCore.getDefaultShell();
			TagEditor tagEditor = new TagEditor(shell, null);
			tagEditor.open();
			ITag tag = tagEditor.getResult();
			if (tag == null) return; // cancel
			try {
				TagTask task = new TagTask();
				task.addTag(memberModel, tag);
				task.execute();
//				memberModel.addTag(tag);
			} catch (SourceEditingException e) {
				SnowberryCore.showErrMsgBox(International.CannotAddTag, e.getMessage());
			}
		} else {
			SnowberryCore.showMsgBox(International.AddTag, International.PleaseSelectPositionToAddTag);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

}
