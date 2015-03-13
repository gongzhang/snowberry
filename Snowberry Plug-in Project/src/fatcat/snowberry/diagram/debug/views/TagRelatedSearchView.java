package fatcat.snowberry.diagram.debug.views;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.Label;
import fatcat.snowberry.search.IResultListener;
import fatcat.snowberry.search.SearchCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;


public class TagRelatedSearchView extends ViewPart implements ControlListener, IResultListener<IMemberModel>, ITagFilter {
	
	private Table table = null;
	private TableColumn nameColumn = null;
	private TableColumn classColumn = null;
	private TableColumn resourceColumn = null;
	private TableColumn projectColumn = null;
	
	private boolean busy = false;
	private ITag tag = null;
	
	private static TagRelatedSearchView instance;
	
	public static TagRelatedSearchView getInstance() {
		return instance;
	}
	
	public TagRelatedSearchView() {
		instance = this;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		table = new Table(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		nameColumn = new TableColumn(table, SWT.NONE);
		nameColumn.setText(International.MemberName);
		classColumn = new TableColumn(table, SWT.NONE);
		classColumn.setText(International.ClassPosition);
		resourceColumn = new TableColumn(table, SWT.NONE);
		resourceColumn.setText(International.FilePosition);
		projectColumn = new TableColumn(table, SWT.NONE);
		projectColumn.setText(International.ProjectPosition);
		table.addControlListener(this);
	}

	@Override
	public void setFocus() {
		instance = this;
	}

	@Override
	public void controlMoved(ControlEvent e) {
	}

	@Override
	public void controlResized(ControlEvent e) {
		nameColumn.setWidth(table.getSize().x / 4);
		classColumn.setWidth(table.getSize().x / 4);
		resourceColumn.setWidth(table.getSize().x / 4);
		projectColumn.setWidth(table.getSize().x / 4);
	}
	
	public void search(Label label) {
		if (busy) {
			SnowberryCore.showMsgBox(International.SearchRelatedLabel, International.PendingLastOperation);
			return;
		}
		if (table.isDisposed()) return;
		busy = true;
		tag = label.toTag();
		models = new LinkedList<IMemberModel>();
		table.removeAll();
		SearchCore.searchMember(this, this);
	}
	
	private LinkedList<IMemberModel> models;

	@Override
	public void done(boolean stoppedManually) {
		busy = false;
	}

	@Override
	public boolean gotResult(IMemberModel result) {
		int i = 0;
		for (IMemberModel model : models) {
			if (compareModel(result, model) <= 0) {
				break;
			}
			i++;
		}
		models.add(i, result);
		
		// controls
		
		final Integer index = i;
		final String[] text = new String[4];
		// nameString
		text[0] = result.getJavaElement().getElementName();
		// classString
		text[1] = result.getOwnerType() == null ? text[0] : result.getOwnerType().getJavaElement().getElementName();
		// resourceString
		text[2] = result.getResource().getName();
		// projectString
		text[3] = result.getOwnerProject().getResource().getName();
		
		table.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				TableItem item = new TableItem(table, SWT.NONE, index);
				item.setText(text);
			}
		});
		
		return false;
	}

	@Override
	public boolean isAccepted(ITag t) {
		return tag.equals(t);
	}
	
	private int compareModel(IMemberModel newModel, IMemberModel oldModel) {
		if (newModel.getOwnerProject().equals(oldModel.getOwnerProject())) {
			if (newModel.getOwnerType() != null && oldModel.getOwnerType() != null &&
				newModel.getOwnerType().equals(oldModel.getOwnerType())) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}

}
