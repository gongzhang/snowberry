package fatcat.snowberry.tag.views;

import java.util.ArrayList;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import fatcat.snowberry.core.International;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagListener;
import fatcat.snowberry.tag.TagCore;


public class TagManager extends ViewPart implements ISelectionListener, ITagListener, ControlListener {
	
	private IMemberModel currentModel = null;
	private Composite parent = null;
	private ArrayList<ITag> tags = null;
	private Tree tree = null;
	TreeColumn nameColumn = null;
	TreeColumn valueColumn = null;
	
	private static TagManager instance;
	
	public static TagManager getInstance() {
		return instance;
	}
	
	public TagManager() {
		instance = this;
	}
	
	public IMemberModel getCurrentMemberModel() {
		return currentModel;
	}
	
	public ITag getSelectedTag() {
		TreeItem[] selectedItems = tree.getSelection();
		if (selectedItems.length == 0) return null;
		TreeItem item = selectedItems[0];
		if (item.getParentItem() != null)
			item = item.getParentItem();
		return tags.get(tree.indexOf(item));
	}

	@Override
	public void createPartControl(Composite parent) {
		
		// initialize controls
		parent.setLayout(new FillLayout());
		tree = new Tree(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		nameColumn = new TreeColumn(tree, SWT.NONE);
		nameColumn.setText(International.Name);
		valueColumn = new TreeColumn(tree, SWT.NONE);
		valueColumn.setText(International.Value);
		tree.addControlListener(this);
		
		tags = new ArrayList<ITag>();
		this.parent = parent;
		super.getSite().getPage().addSelectionListener(this);
	}

	@Override
	public void setFocus() {
		instance = this;
		tree.setFocus();
	}
	
	@Override
	public void dispose() {
		if (!tree.isDisposed())
			tree.removeControlListener(this);
		if (currentModel != null) currentModel.removeTagListener(this);
		super.getSite().getPage().removeSelectionListener(this);
		super.dispose();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			if (treeSelection.getFirstElement() instanceof IMember) {
				IMember member = (IMember) treeSelection.getFirstElement();
				IProjectModel projectModel = TagCore.getProjectModel(member.getResource());
				final IMemberModel model = projectModel.getMemberModel(member);
				parent.getDisplay().syncExec(new Runnable() {
					@Override
					public void run() {
						showTagFrom(model);
					}
				});
			}
		}
	}
	
	private void showTagFrom(IMemberModel model) {
		if (currentModel != null) currentModel.removeTagListener(this);
		currentModel = model;
		currentModel.addTagListener(this);
		tags.clear();
		ITag[] ts = currentModel.getTags();
		for (ITag t : ts) tags.add(t);
		refreshView();
	}

	@Override
	public void tagAdded(ITag tag, IMemberModel memberModel) {
		tags.add(tag);
		parent.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				refreshView();
			}
		});
	}

	@Override
	public void tagRemoved(ITag tag, IMemberModel memberModel) {
		tags.remove(tag);
		parent.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				refreshView();
			}
		});
	}
	
	private void refreshView() {
		tree.removeAll();
		for (int i = 0; i < tags.size(); i++) {
			TreeItem tagItem = new TreeItem(tree, SWT.NONE);
			tagItem.setText(tags.get(i).getName());
			for (int j = 0; j < tags.get(i).size(); j++) {
				TreeItem propertyItem = new TreeItem(tagItem, SWT.NONE);
				String[] strs = new String[2];
				strs[0] = tags.get(i).getPropertyName(j);
				strs[1] = tags.get(i).getPropertyValue(j);
				propertyItem.setText(strs);
			}
		}
	}

	@Override
	public void controlResized(ControlEvent e) {
		nameColumn.setWidth(tree.getSize().x / 2);
		valueColumn.setWidth(tree.getSize().x / 2);
	}

	@Override
	public void controlMoved(ControlEvent e) {
	}

}
