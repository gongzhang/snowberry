package fatcat.snowberry.views.internal;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPersistableElement;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.diagram.DiagramEditor2;
import fatcat.snowberry.diagram.DiagramEditorInput;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.gui.Button;
import fatcat.snowberry.gui.MultilineLabel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.views.HyperdocFrame;

public class DPInfoItem extends Container {
	
	private final fatcat.snowberry.gui.Label title, des;
	private final MultilineLabel cmt;
	private final Button btnDetails;
	private final HyperdocFrame hyperdocFrame;
	
	private final DesignPattern pattern;
	private final IMemberModel model;

	public DPInfoItem(Container owner, DesignPattern pattern, IMemberModel model, HyperdocFrame hyperdocFrame) {
		super(owner);
		this.hyperdocFrame = hyperdocFrame;
		this.pattern = pattern;
		this.model = model;
		
		title = new fatcat.snowberry.gui.Label(this);
		title.setAutoSize(true);
		title.setFont(HyperdocFrame.FONT_DIALOG_13B);
		title.setText(pattern.getPatternName() + String.format(International.Members2, pattern.getModels().length));
		title.setLocation(18, 0);
		
		des = new fatcat.snowberry.gui.Label(this);
		des.setAutoSize(true);
		des.setFont(HyperdocFrame.FONT_DIALOG_13);
		Role role = pattern.getRole(model);
		if (role != null) {
			des.setText(International.AS + pattern.getRole(model).getName() + International.ParticipateThisDesignPattern);
		} else {
			des.setText(International.UnknownRole);
		}
		des.setLocation(18, title.getBottom() + 1);
		
		cmt = new MultilineLabel(this);
		cmt.setFont(des.getFont());
		String comment = pattern.getComment(model);
		cmt.setText(" - " + ((comment == null || comment.length() == 0) ? International.NoComment : comment)); //$NON-NLS-1$
		cmt.setLocation(18, des.getBottom() + 12);
		
		btnDetails = new Button(this);
		btnDetails.disableBuffer();
		btnDetails.setImage(ICON_DETAILS);
		btnDetails.setLocation(title.getRight() + 4, -1);
		btnDetails.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				btnDetails_Clicked();
			}
		});
		
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (cmt != null) {
			cmt.setRightSpace(0);
			cmt.setText(cmt.getText());
		}
	}
	
	public final static BufferedImage ICON_DP = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/types.gif"); //$NON-NLS-1$
	public final static BufferedImage ICON_DETAILS = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/discovery.gif"); //$NON-NLS-1$
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.drawImage(ICON_DP, 0, 0, null);
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 54;
	}
	
	private void btnDetails_Clicked() {
		hyperdocFrame.getHyperdocViewPart().getSite().getShell().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					
					DiagramEditorInput input = new DiagramEditorInput(model.getResource());
					IEditorPart part = hyperdocFrame.getHyperdocViewPart().getSite().getPage().openEditor(input, "fatcat.snowberry.diagram.editor2", true);
//					IEditorPart part = IDE.openEditor(hyperdocFrame.getHyperdocViewPart().getSite().getPage(), fileEditorInput, "fatcat.snowberry.diagram.editor2");
					if (part != null && part instanceof DiagramEditor2) {
						final DiagramEditor2 editor = (DiagramEditor2) part;
						new Thread(new Runnable() {
							@Override
							public void run() {
								while (!editor.isReady()) {
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
									}
								}
								editor.getDiagramFrame().getShell().syncExec(new Runnable() {
									@Override
									public void run() {
										editor.getDiagramFrame().showDesignPattern(pattern);
									}
								});
								
							}
						}).start();
					}
				} catch (Exception e1) {
					SnowberryCore.showErrMsgBox(International.CannotViewCode, e1.getMessage());
				}
			}
		});
	}
	
}

class WrappedFileInput implements IFileEditorInput {
	
	final IFileEditorInput oldEditorInput;
	
	public boolean equals(Object obj) {
		return false;
	}
	
	WrappedFileInput(IFileEditorInput oldEditorInput) {
		this.oldEditorInput = oldEditorInput;
	}

	@Override
	public IFile getFile() {
		return oldEditorInput.getFile();
	}

	@Override
	public IStorage getStorage() throws CoreException {
		return oldEditorInput.getStorage();
	}

	@Override
	public boolean exists() {
		return oldEditorInput.exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return oldEditorInput.getImageDescriptor();
	}

	@Override
	public String getName() {
		return oldEditorInput.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return oldEditorInput.getPersistable();
	}

	@Override
	public String getToolTipText() {
		return oldEditorInput.getToolTipText();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return oldEditorInput.getAdapter(adapter);
	}
	
}