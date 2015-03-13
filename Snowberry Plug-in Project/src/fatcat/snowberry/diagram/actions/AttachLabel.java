package fatcat.snowberry.diagram.actions;

import org.eclipse.swt.widgets.Shell;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.Label;
import fatcat.snowberry.diagram.dialogs.AddLabelDialog;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class AttachLabel extends Action {
	
	private final IMemberModel model;
	private final Label label;
	
	public AttachLabel(IMemberModel model, Label label) {
		this.model = model;
		this.label = label;
	}
	
	public AttachLabel(IMemberModel model) {
		this.model = model;
		this.label = null;
	}

	@Override
	public void fireAction() {
		ITag tag;
		if (label == null) {
			Shell shell = SnowberryCore.getDefaultShell();
			AddLabelDialog dialog = new AddLabelDialog(shell);
			Label l = dialog.open();
			if (l != null) {
				tag = l.toTag();
			} else {
				return;
			}
		} else {
			tag = label.toTag();
		}
		TagTask task = new TagTask();
		task.addTag(model, tag);
		try {
			task.execute();
		} catch (SourceEditingException e) {
			SnowberryCore.showErrMsgBox(International.CannotAddLabel, e.getMessage());
		}
	}

}
