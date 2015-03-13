package fatcat.snowberry.diagram;

import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.snowberry.gui.Arrow;


public class HierarchyArrow extends Arrow {
	
	private final ComponentAdapter REMOVER = new ComponentAdapter() {
		public void componentRemoved(fatcat.gui.snail.Component c) {
			if (!isRemoved()) remove();
			getSrc().removeComponentListener(REMOVER);
			getDst().removeComponentListener(REMOVER);
		}
	};

	public HierarchyArrow(Container owner, ClassDiagram src, ClassDiagram dst) {
		super(owner, src, dst);
		src.addComponentListener(REMOVER);
		dst.addComponentListener(REMOVER);
	}

}
