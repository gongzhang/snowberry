package fatcat.snowberry.views;

import org.eclipse.jdt.core.IType;

import fatcat.gui.snail.Container;
import fatcat.gui.snail.Frame;
import fatcat.gui.snail.SnailShell;
import fatcat.snowberry.gui.DiagramPanel;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.TagCore;

public class WhereAmIFrame extends Frame {

	public WhereAmIFrame(SnailShell shell) {
		super(shell);
	}
	
	public void showAbout(IType type) {
		ITypeModel typeModel = TagCore.searchTypeModel(type, null);
		if (typeModel != null) {
			Diagram diagram = new Diagram(this, typeModel);
			diagram.setBounds(30, 30, 150, 70);
		}
	}

}

class Diagram extends DiagramPanel {
	
	private final ITypeModel model;

	public Diagram(Container owner, ITypeModel model) {
		super(owner);
		this.model = model;
		content.remove();
		title.remove();
		
	}
	
	public ITypeModel getModel() {
		return model;
	}
	
	@Override
	public void refreshLayout(Container c) {
	}
	
}