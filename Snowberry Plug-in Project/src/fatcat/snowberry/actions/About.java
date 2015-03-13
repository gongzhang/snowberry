package fatcat.snowberry.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.dialogs.AboutDialog;


public class About implements IWorkbenchWindowActionDelegate {

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow window) {

	}

	@Override
	public void run(IAction action) {
		Shell shell = SnowberryCore.getDefaultShell();
		AboutDialog dialog = new AboutDialog(shell);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

}
