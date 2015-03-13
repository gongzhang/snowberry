package fatcat.snowberry.diagram;

import java.awt.image.BufferedImage;
import java.util.LinkedList;


import fatcat.gui.GraphicsX;
import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.tag.IMemberModel;


public class GroupItem extends CompositeItem {
	
	private String group_name;
	
	public static final BufferedImage ICON_MEMBER_GROUP = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/members.gif");

	public GroupItem(CategoryPanel owner, LinkedList<IMemberModel> model) {
		super(owner, model);
	}

	@Override
	public void modelChanged() {
		for (IMemberModel model : this.model) {
			Group group = Group.parseGroup(model);
			if (group != null) {
				group_name = group.name;
				setText(group_name);
				setSuffixText(" (Member Group)");
//				setImage(ICON_MEMBER_GROUP);
				setImage(OverloadedItem.ICON_METHOD_GROUP);
				break;
			}
		}
	}
	
	@Override
	public boolean inSameGroupWith(IMemberModel item) {
		if (item.getKind() == IMemberModel.METHOD &&
			item.getJavaElement().getElementName().equals(model.get(0).getJavaElement().getElementName())) {
			return true;
		} else {
			Group group = Group.parseGroup(item);
			if (group != null && group_name.equals(group.name)) {
				return true;
			} else {
				return false;
			}
		}
	}

}
