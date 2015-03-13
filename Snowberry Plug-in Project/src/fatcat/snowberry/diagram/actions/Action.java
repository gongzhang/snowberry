package fatcat.snowberry.diagram.actions;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;


abstract public class Action implements SelectionListener {

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		e.display.syncExec(new Runnable() {
			@Override
			public void run() {
				fireAction();
			}
		});
	}
	
	abstract public void fireAction();

}
