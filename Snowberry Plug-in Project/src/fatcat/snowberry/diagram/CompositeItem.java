package fatcat.snowberry.diagram;

import java.util.LinkedList;

import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.tag.IMemberModel;


abstract public class CompositeItem extends MemberItem<LinkedList<IMemberModel>> {
	
	private CompositeItemViewer viewer;
	
	public static final CompositeItem createCompositeItem(CategoryPanel owner, IMemberModel m1, IMemberModel m2) {
		LinkedList<IMemberModel> models = new LinkedList<IMemberModel>();
		models.add(m1);
		models.add(m2);
		if (Group.parseGroup(m1) != null || Group.parseGroup(m2) != null) {
			return new GroupItem(owner, models);
		} else {
			return new OverloadedItem(owner, models);
		}
	}
	
	@Override
	protected void setSelected(boolean selected) {
		super.setSelected(selected);
		if (selected) {
			if (((CategoryPanel) getOwner()).getSelection().length == 1) {
				showViewer();
			}
		} else {
			hideViewer();
		}
	}
	
	public void showViewer() {
		final ClassDiagram cd = getDiagram();
		viewer = new CompositeItemViewer(cd, this);
		viewer.setLocation(cd.getWidth() + 10, getAbsTop() - cd.getAbsTop() - viewer.getHeight() / 2 + 10);
	}
	
	public void hideViewer() {
		if (viewer != null) {
			viewer.remove();
			viewer = null;
		}
	}

	public CompositeItem(CategoryPanel owner, LinkedList<IMemberModel> model) {
		super(owner, model);
	}
	
	public void addMember(IMemberModel model) {
		this.model.add(model);
		modelChanged();
	}
	
	public void removeMember(IMemberModel model) {
		this.model.remove(model);
		modelChanged();
	}
	
	public IMemberModel getModel(int index) {
		return model.get(index);
	}
	
	public IMemberModel[] getModels() {
		return model.toArray(new IMemberModel[0]);
	}
	
	public int size() {
		return model.size();
	}

}
