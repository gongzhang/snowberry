package fatcat.snowberry.diagram;

import fatcat.gui.snail.Container;
import fatcat.snowberry.gui.RealLabel;
import fatcat.snowberry.tag.IMemberModel;


public class LabelComponent extends RealLabel {
	
	private final Label label;
	private final IMemberModel model;

	public LabelComponent(Container owner, Label label, IMemberModel model) {
		super(owner, label.name);
		this.label = label;
		this.model = model;
		setFocusable(false);
	}
	
	public IMemberModel getModel() {
		return model;
	}
	
	public Label getLabel() {
		return label;
	}

}
