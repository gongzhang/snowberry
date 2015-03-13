package fatcat.snowberry.diagram;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.ILayout;

import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.gui.Button;
import fatcat.snowberry.tag.ITypeModel;


public class ClosableDiagram extends ClassDiagram {
	
	private final Button closeButton;
	
	public static final BufferedImage ICON_CLOSE = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/delete_obj.gif");

	public ClosableDiagram(DiagramFrame owner, ITypeModel typeModel) {
		super(owner, typeModel);
		closeButton = new Button(title);
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				close();
			}
		});
		closeButton.setImage(ICON_CLOSE);
		title.setSpace(48);
		final ILayout old_layout = title.getLayout();
		title.setLayout(new ILayout() {
			@Override
			public void refreshLayout(Container co) {
				old_layout.refreshLayout(co);
				int left = co.getWidth() - 25;
				for (int i = co.size() - 1; i >= 0; i--) {
					Component c = co.get(i);
					if (c instanceof Button) {
						c.setLocation(left, 1);
						left -= c.getWidth() + 2;
					}
				}
			}
		});
	}
	
	public boolean close() {
//		if (getDiagramFrame().getAreaContainer().size() > 0) {
//			return false;
//		}
		return getDiagramFrame().closeDiagram(getModel());
	}

}
