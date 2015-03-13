package fatcat.snowberry.diagram;

import java.awt.image.BufferedImage;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.JavaModelException;

import fatcat.gui.GraphicsX;
import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.tag.IFieldModel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITypeModel;


public class FieldItem extends MemberItem<IFieldModel> {
	
	private String group_name;
	
	public static final BufferedImage ICON_PRIVATE = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/field_private_obj.gif");
	public static final BufferedImage ICON_DEFAULT = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/field_default_obj.gif");
	public static final BufferedImage ICON_PROTECTED = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/field_protected_obj.gif");
	public static final BufferedImage ICON_PUBLIC = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/field_public_obj.gif");
	
	private boolean shownAsAssociation = false;
	private ITypeModel association_model = null;
	private AssociationArrow arrow = null;

	public FieldItem(CategoryPanel owner, IFieldModel model) {
		super(owner, model);
	}
	
	public boolean isShownAsAssociation() {
		return shownAsAssociation;
	}
	
	public void showAsAssociation() {
		if (!shownAsAssociation) {
			ITypeModel model = getModel().getFieldTypeModel();
			if (model != null) {
				association_model = model;
				final DiagramFrame frame = getDiagram().getDiagramFrame();
				final ClassDiagram cd = frame.openDiagram(model);
				final MemberItem<?> item = getTopLevelItem();
				arrow = new AssociationArrow(frame.getRelationContainer(), item, cd);
				shownAsAssociation = true;
			}
		}
	}
	
	public void showAsField() {
		if (shownAsAssociation) {
			getDiagram().getDiagramFrame().closeDiagram(association_model);
			association_model = null;
			if (!arrow.isRemoved()) arrow.remove();
			shownAsAssociation = false;
		}
	}
	
	@Override
	public void modelChanged() {
		List<?> names = model.getASTNode().fragments();
		
		String tmp = (names.get(0)).toString();
		int tmpindex = tmp.indexOf("=");
		if (tmpindex != -1)
			tmp = tmp.substring(0, tmpindex);
		StringBuffer name = new StringBuffer(tmp);
		for (int i = 1; i < names.size(); i++) {
			tmp = names.get(i).toString();
			tmpindex = tmp.indexOf("=");
			if (tmpindex != -1)
				tmp = tmp.substring(0, tmpindex);
			name.append(", " + tmp);
		}
		setText(name.toString());
		setSuffixText(" : " + model.getASTNode().getType());
		int f;
		try {
			f = model.getJavaElement().getFlags();
		} catch (JavaModelException e) {
			f = 0;
		}
		if (Flags.isPublic(f)) setImage(ICON_PUBLIC);
		else if (Flags.isProtected(f)) setImage(ICON_PROTECTED);
		else if (Flags.isPrivate(f)) setImage(ICON_PRIVATE);
		else setImage(ICON_DEFAULT);
		
		Group g = Group.parseGroup(model);
		group_name = (g == null ? "" : g.name);
	}
	
	@Override
	public boolean inSameGroupWith(IMemberModel item) {
		Group group = Group.parseGroup(item);
		if (group != null && group_name.equals(group.name)) {
			return true;
		} else {
			return false;
		}
	}

}
