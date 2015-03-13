package fatcat.snowberry.diagram;

import java.awt.image.BufferedImage;
import java.util.List;

import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;


import fatcat.gui.GraphicsX;
import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IMethodModel;


public class MethodItem extends MemberItem<IMethodModel> {
	
	private String group_name;
	
	public static final BufferedImage ICON_PRIVATE = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/methpri_obj.gif");
	public static final BufferedImage ICON_DEFAULT = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/methdef_obj.gif");
	public static final BufferedImage ICON_PROTECTED = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/methpro_obj.gif");
	public static final BufferedImage ICON_PUBLIC = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/methpub_obj.gif");

	public MethodItem(CategoryPanel owner, IMethodModel model) {
		super(owner, model);
	}

	@Override
	public void modelChanged() {
		List<?> paras = model.getASTNode().parameters();
		StringBuffer str_paras = new StringBuffer();
		if (paras.size() == 0) str_paras.append("()");
		else {
			str_paras.append("(" + ((SingleVariableDeclaration) paras.get(0)).getType().toString());
			for (int i = 1; i < paras.size(); i++) {
				str_paras.append(", " + ((SingleVariableDeclaration) paras.get(i)).getType().toString());
			}
			str_paras.append(")");
		}
		if (model.getJavaElement() == null) {
//			SnowberryCore.println("assert failed!");
			SnowberryCore.log(Status.ERROR, International.ErrorCannotSyncTheCode);
		} else {
			setText(model.getJavaElement().getElementName() + str_paras.toString()); // DONE ！发现空指针异常
			if (model.getASTNode().getReturnType2() != null)
				setSuffixText(" : " + model.getASTNode().getReturnType2());
			else
				setSuffixText("");
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
		}
		
		Group g = Group.parseGroup(model);
		group_name = g == null ? "" : g.name;
	}
	
	@Override
	public boolean inSameGroupWith(IMemberModel item) {
		Group group = Group.parseGroup(item);
		if (group != null && group_name.equals(group.name)) {
			return true;
		} else {
			return item.getKind() == IMemberModel.METHOD && item.getJavaElement().getElementName().equals(model.getJavaElement().getElementName());
		}
	}

}
