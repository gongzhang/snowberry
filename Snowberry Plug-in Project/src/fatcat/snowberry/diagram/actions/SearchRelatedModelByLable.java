package fatcat.snowberry.diagram.actions;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.Label;
import fatcat.snowberry.diagram.debug.views.TagRelatedSearchView;


public class SearchRelatedModelByLable extends Action {
	
	private final Label label;
	
	public SearchRelatedModelByLable(Label label) {
		this.label = label;
	}

	@Override
	public void fireAction() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("fatcat.snowberry.diagram.debug.views.label.search");
		} catch (PartInitException e) {
			SnowberryCore.log(Status.ERROR, International.ErrorCannotOpenView, e);
		}
		TagRelatedSearchView searchView = TagRelatedSearchView.getInstance();
		if (searchView != null) {
			searchView.search(label);
		}
	}

}
