package fatcat.snowberry.tag.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;
import fatcat.snowberry.tag.views.TagManager;


public class RemoveTagAction implements IViewActionDelegate {
	
	private TagManager tagManager = null;

	@Override
	public void init(IViewPart view) {
		tagManager = (TagManager) view;
	}

	@Override
	public void run(IAction action) {
		IMemberModel memberModel = tagManager.getCurrentMemberModel();
		final ITag tag = tagManager.getSelectedTag();
		if (memberModel != null && tag != null) {
			if (SWT.OK == SnowberryCore.showMsgBox(International.RemoveTag, International.ConfirmRemoveTag, SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION)) {
				try {
					TagTask task = new TagTask();
					task.removeTag(memberModel, new ITagFilter() {
						@Override
						public boolean isAccepted(ITag t) {
							return t.equals(tag);
						}
					});
					task.execute();
//					memberModel.removeTag(tag);
				} catch (SourceEditingException e) {
					SnowberryCore.showErrMsgBox(International.CannotRemoveTag, e.getMessage());
				}
			}
		} else {
			SnowberryCore.showMsgBox(International.RemoveTag, International.NoTagSelected);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

}
