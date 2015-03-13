package fatcat.snowberry.dp.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.dp.dialogs.SchemaCreator;


public class AddSchema implements IViewActionDelegate {

	@Override
	public void init(IViewPart view) {
	}

	@Override
	public void run(IAction action) {
		Shell shell = SnowberryCore.getDefaultShell();
		SchemaCreator creator = new SchemaCreator(shell);
		creator.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
