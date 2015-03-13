package fatcat.snowberry.diagram;

import fatcat.gui.GraphicsX;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IMethodModel;


public class OverloadedItem extends CompositeItem {
	
	public static final BufferedImage ICON_METHOD_GROUP;
	
	static {
		ICON_METHOD_GROUP = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/methpub_obj.gif");
		Graphics2D g2 = ICON_METHOD_GROUP.createGraphics();
		g2.drawImage(MethodItem.ICON_PUBLIC, -2, -2, null);
		g2.drawImage(MethodItem.ICON_PUBLIC, 1, 1, null);
	}

	public OverloadedItem(CategoryPanel owner, LinkedList<IMemberModel> model) {
		super(owner, model);
	}

	@Override
	public void modelChanged() {
		setImage(ICON_METHOD_GROUP);
		setText(model.get(0).getJavaElement().getElementName() + "(...)");
		StringBuffer suffix = new StringBuffer(" : ");
		if (((IMethodModel) model.get(0)).getASTNode().getReturnType2() != null)
			suffix.append(((IMethodModel) model.get(0)).getASTNode().getReturnType2());
		suffix.append(" (+");
		suffix.append(model.size());
		suffix.append(" overloads)");
		setSuffixText(suffix.toString());
	}
	
	@Override
	public boolean inSameGroupWith(IMemberModel item) {
		return item.getKind() == IMemberModel.METHOD && item.getJavaElement().getElementName().equals(model.get(0).getJavaElement().getElementName());
	}

}
