package fatcat.snowberry.views.internal;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.ClassDiagram;
import fatcat.snowberry.gui.Label;
import fatcat.snowberry.gui.MultilineLabel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.views.HyperdocFrame;

public class LabelItem extends Container {

	public LabelItem(Container owner, final IMemberModel model, fatcat.snowberry.diagram.Label label) {
		super(owner);
		
		// title icon
		Icon icon = new Icon(this, ClassDiagram.ICON_CLASS);
		icon.setTop(0);
		
		ITypeModel typeModel = null;
		if (model.getKind() == IMemberModel.TYPE) {
			typeModel = (ITypeModel) model;
		} else {
			typeModel = model.getOwnerType();
		}
		
		// title
		Label title = new Label(this);
		title.setFont(HyperdocFrame.FONT_DIALOG_11);
		title.setText(typeModel.getJavaElement().getFullyQualifiedName());
		title.setLocation(icon.getRight() + 2, 1);
		
		String str_type = model.getKind() == IMemberModel.TYPE ? International.Class : model.getKind() == IMemberModel.METHOD ? International.Method : International.Field;
		
		Label name = new Label(this);
		name.setFont(HyperdocFrame.FONT_DIALOG_13B);
		name.setText(str_type + model.getJavaElement().getElementName());
		name.setLocation(0, title.getBottom() + 2);
		
		MultilineLabel cmt = new MultilineLabel(this);
		String comment = label.comment;
		cmt.setFont(HyperdocFrame.FONT_DIALOG_13);
		cmt.setLocation(18, name.getBottom() + 12);
		cmt.setWidth(owner.getWidth() - 18);
		cmt.setText(" - " + ((comment == null || comment.length() == 0) ? International.NoComment : comment)); //$NON-NLS-1$
		cmt.setHeight(cmt.getPreferredHeight());
		
		setSize(owner.getWidth(), cmt.getBottom() + 10);
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
	}
	
}

