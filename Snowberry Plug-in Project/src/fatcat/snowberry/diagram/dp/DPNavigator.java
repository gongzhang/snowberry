package fatcat.snowberry.diagram.dp;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.ClassDiagram;
import fatcat.snowberry.diagram.ClosableDiagram;
import fatcat.snowberry.diagram.DiagramFrame;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.gui.Arrow;
import fatcat.snowberry.gui.Button;
import fatcat.snowberry.gui.InfoPanel;
import fatcat.snowberry.gui.Label;
import fatcat.snowberry.gui.util.QInterpolator;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITypeModel;


public class DPNavigator extends InfoPanel {
	
	private final DiagramFrame diagramFrame;
	private QInterpolator animation = null;
	private final Label title, comment;
	private final Button btnClose;
	private LinkedList<Arrow> arrows = null;

	public DPNavigator(DiagramFrame owner) {
		super(owner);
		this.diagramFrame = owner;
		
		title = new Label(this);
		title.setAutoSize(true);
		title.setFont(new Font(Font.DIALOG, Font.PLAIN, 18)); //$NON-NLS-1$
		
		comment = new Label(this);
		comment.setAutoSize(true);
		comment.setFont(new Font(Font.DIALOG, Font.PLAIN, 12)); //$NON-NLS-1$
		
		btnClose = new Button(this);
		btnClose.setImage(ClosableDiagram.ICON_CLOSE);
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				hide();
			}
		});
	}
	
	@Override
	protected void update(double dt) {
		if (animation != null) {
			if (!animation.hasDone()) {
				setTop((int) animation.update(dt));
			} else {
				setTop((int) animation.endValue);
				animation = null;
			}
		}
		super.update(dt);
	}
	
	public DiagramFrame getDiagramFrame() {
		return diagramFrame;
	}
	
	@Override
	protected boolean isLegalHeight(int height) {
		return height == getPreferredHeight();
	}
	
	@Override
	protected boolean isLegalWidth(int width) {
		return width == getPreferredWidth();
	}
	
	@Override
	public int preferredWidth(Component c) {
		return getFrame().getWidth();
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 50;
	}
	
	private void show() {
		if (isVisible()) {
			hide();
		}
		setTop(-getHeight());
		setVisible(true);
		animation = new QInterpolator(-getHeight(), 0, 0.5);
	}
	
	private void hide() {
		this.pattern = null;
		setVisible(false);
		getDiagramFrame().closeDiagrams();
		getDiagramFrame().resetDiagramLayout();
		getDiagramFrame().getAreaContainer().removeAll();
		getDiagramFrame().getLegendPanel().removeAll();
		getDiagramFrame().getLegendPanel().setVisible(false);
		if (arrows != null) {
			for (Arrow a : arrows) {
				if (!a.isRemoved()) a.remove();
			}
		}
		arrows = null;
	}
	
	private DesignPattern pattern = null;
	
	public DesignPattern getCurrentPattern() {
		return pattern;
	}
	
	public void show(DesignPattern pattern) {
		show();
		this.pattern = pattern;
		
		// information
		final IMemberModel[] models = pattern.getModels();
		title.setText(String.format(International.DesignPatternNavigatorTitle, pattern.getPatternName(), models.length));
//		title.setText(International.DesignPattern + pattern.getPatternName() + "（" + models.length + International.Members);
		String comment = pattern.getSchema().getComment();
		this.comment.setText((comment == null || comment.length() == 0) ? International.NoComment : comment);
		
		// clean up
		getDiagramFrame().closeDiagrams();
		getDiagramFrame().resetDiagramLayout();
		
		
		final LinkedList<ClassDiagram> types = new LinkedList<ClassDiagram>();
		
		// consider un-closable diagram
		final Container diagramContainer = getDiagramFrame().getDiagramContainer();
		final Component[] cs = diagramContainer.getComponents();
		for (Component c : cs) {
			if (c instanceof ClassDiagram) {
				final ClassDiagram cd = (ClassDiagram) c;
				cd.setState(ClassDiagram.STATE_MINIMIZED, true);
				types.add(cd);
			}
		}
		
		// open related diagrams
		for (IMemberModel m : models) {
			if (m instanceof ITypeModel) {
				final ClassDiagram cd = getDiagramFrame().openDiagram((ITypeModel) m, false);
				cd.setState(ClassDiagram.STATE_MINIMIZED, true);
				if (!types.contains(cd)) types.add(cd);
			}
		}
		
		// layout diagrams
		final Rectangle area = new Rectangle(0, getPreferredHeight(), getFrame().getWidth(), getFrame().getHeight() - getPreferredHeight());
		arrows = DPLayout.doLayout(area, pattern, types, getDiagramFrame().getArrowContainer());
		
		doLayout();
	}
	
	@Override
	public void refreshLayout(Container c) {
		super.refreshLayout(c);
		if (isInitialized()) {
			title.setLocation(4, 4);
			comment.setLocation(4, title.getBottom() + 2);
			btnClose.setLocation(title.getRight() + 2, 4);
		}
	}

}
