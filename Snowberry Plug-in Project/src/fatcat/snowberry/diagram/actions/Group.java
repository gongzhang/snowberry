package fatcat.snowberry.diagram.actions;

import org.eclipse.swt.widgets.Shell;

import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.dialogs.AddGroupDialog;
import fatcat.snowberry.tag.IMemberModel;


public class Group extends Action {
	
	private final IMemberModel[] models;
	
	public Group(IMemberModel[] models) {
		this.models = models;
	}

	@Override
	public void fireAction() {
		Shell shell = SnowberryCore.getDefaultShell();
		AddGroupDialog dialog = new AddGroupDialog(shell, models);
		dialog.open();
	}

}
