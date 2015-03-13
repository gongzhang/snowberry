package fatcat.snowberry.diagram;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

import org.eclipse.core.resources.IFile;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Frame;
import fatcat.gui.snail.SnailShell;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.dp.DPFrame;
import fatcat.snowberry.tag.TagCore;


public class LoadingFrame extends Frame {
	
	private final DiagramEditor2 editor;
	private final IFile file;
	private boolean show_flag = false;

	public LoadingFrame(SnailShell shell, DiagramEditor2 editor) {
		super(shell);
		this.editor = editor;
		file = ((DiagramEditorInput) editor.getEditorInput()).getFile();
	}
	
	public void showDiagram() {
		show_flag = true;
	}
	
	public boolean hasDone() {
		return show_flag;
	}
	
	@Override
	protected void update(double dt) {
		if (show_flag && getWidth() != 0 && TagCore.isReady()) {
			DiagramFrame diagramFrame = new SelectableDiagramFrame(getShell(), file, editor);
			DPFrame dpFrame = new DPFrame(getShell(), diagramFrame);
			
			editor.diagramFrame = diagramFrame;
			editor.dpFrame = dpFrame;
			
//			if (dpFrame.getPatterns().length > 0) {
//				dpFrame.show();
//			} else {
				diagramFrame.show();
//			}
			
			editor.toolbar.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					editor.toolbar.setVisible(true);
				}
			});
			
		}
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.black);
		g2.setFont(new Font(Font.DIALOG, Font.PLAIN, 14)); //$NON-NLS-1$
		Rectangle2D rect = g2.getFontMetrics().getStringBounds(International.LoadingGraphics, g2);
		g2.drawString(International.LoadingGraphics, (getWidth() - (int)rect.getWidth()) / 2, (getHeight() - (int)rect.getHeight()) / 2);
	}

}
