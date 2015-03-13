package fatcat.snowberry.dp.actions;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.dp.schema.ISchema;
import fatcat.snowberry.dp.views.SchemaView;
import fatcat.snowberry.search.IResultListener;
import fatcat.snowberry.search.SearchCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class RemoveSchema implements IViewActionDelegate {
	
	private SchemaView view;

	@Override
	public void init(IViewPart view) {
		this.view = (SchemaView) view;
	}

	@Override
	public void run(IAction action) {
		if (view.getSelectedItem() != null &&
			view.getSelectedItem() instanceof ISchema) {
			final ITag stag = ((ISchema) view.getSelectedItem()).toTag();
			
			int rst = SnowberryCore.showMsgBox(International.RemoveDesignPattern, International.ConfirmRemoveDesignPattern, SWT.YES | SWT.NO);
			
			final ITagFilter filter = new ITagFilter() {
				@Override
				public boolean isAccepted(ITag tag) {
					return stag.equals(tag);
				}
			};
			
			if (rst == SWT.YES) {
				SearchCore.searchMember(new IResultListener<IMemberModel>() {
					
					private IMemberModel model = null;
					
					@Override
					public boolean gotResult(IMemberModel result) {
						model = result;
						return true;
					}
					
					@Override
					public void done(boolean stoppedManually) {
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								if (model == null) {
									SnowberryCore.showErrMsgBox(International.RemoveDesignPattern, International.CannotFindDesignPatternDefinition);
								} else {
									TagTask task = new TagTask();
									task.removeTag(model, filter);
									try {
										task.execute();
									} catch (SourceEditingException e) {
//										SnowberryCore.println(e.getMessage());
										SnowberryCore.log(Status.ERROR, International.CannotEditCode, e);
										SnowberryCore.showErrMsgBox(International.RemoveDesignPattern, International.CannotEditCode);
									}
								}
							}
						});
						
					}
					
				}, filter);
			}
			
		} else {
			SnowberryCore.showErrMsgBox(International.RemoveDesignPattern, International.PleaseSelectDesignPattern);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
