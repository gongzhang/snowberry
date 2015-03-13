package fatcat.snowberry.diagram;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import fatcat.gui.snail.SnailShell;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.dp.DPFrame;


public class DiagramEditor2 extends EditorPart {
	
	Composite diagramComposite = null;
	LoadingFrame loadingFrame = null;
	DPFrame dpFrame = null; // will be initialized in LoadingFrame
	DiagramFrame diagramFrame = null; // will be initialized in LoadingFrame
	SnailShell snailShell = null;
	DiagramEditorToolbar toolbar = null;
	IFile file = null;
	IFileEditorInput fileInput = null;

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}
	
	public final SnailShell getSnailShell() {
		return snailShell;
	}
	
	public final DiagramFrame getDiagramFrame() {
		return diagramFrame;
	}
	
	public final Composite getDiagramComposite() {
		return diagramComposite;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if ((input instanceof IFileEditorInput)) {
			fileInput = ((IFileEditorInput) input);
			file = fileInput.getFile();
			if (!file.getFileExtension().equals("java")) { //$NON-NLS-1$
				throw new PartInitException(International.JavaClassDiagramEditorCanOnlyEditJavaFiles);
			}
			
			setPartName(file.getName() + International.ClassDiagramSuffix);
			setTitleToolTip(file.getName() + International.ClassDiagramSuffix);
			setSite(site);
			
			setInput(new DiagramEditorInput(file));
		} else if ((input instanceof DiagramEditorInput)) {
			fileInput = ((DiagramEditorInput) input).getFileEditorInput();
			file = fileInput.getFile();
			if (!file.getFileExtension().equals("java")) { //$NON-NLS-1$
				throw new PartInitException(International.JavaClassDiagramEditorCanOnlyEditJavaFiles);
			}
			
			setPartName(file.getName() + International.ClassDiagramSuffix);
			setTitleToolTip(file.getName() + International.ClassDiagramSuffix);
			setSite(site);
			
			setInput(input);
		} else {
			throw new PartInitException(International.JavaClassDiagramEditorCanOnlyEditJavaFiles);
		}
	}
	
	public boolean isReady() {
		return diagramFrame != null;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(final Composite parent) {
		toolbar = new DiagramEditorToolbar(parent, this);
		toolbar.setVisible(true);
		diagramComposite = new Composite(parent, SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(diagramComposite);
		Panel root = new Panel(); // DONE （低优先）应当在frame中新建一个Panel做为顶层容器
		frame.setLayout(new BorderLayout());
		frame.add(root, BorderLayout.CENTER);
		snailShell = new SnailShell(root, 30);
		snailShell.disableThreadSafetyChecking(); // DONE 检查线程安全
		loadingFrame = new LoadingFrame(snailShell, this);
		loadingFrame.show();
		loadingFrame.showDiagram();
		
		// layout
		parent.setLayout(null);
		parent.addControlListener(new ControlAdapter() {
			
			final int TOOL_BAR_HEIGHT = 24;
			
			@Override
			public void controlResized(ControlEvent e) {
				
				toolbar.setBounds(0, 0, parent.getSize().x, TOOL_BAR_HEIGHT);
				diagramComposite.setBounds(0, TOOL_BAR_HEIGHT, parent.getSize().x, parent.getSize().y - TOOL_BAR_HEIGHT);
			}
		});
	}

	@Override
	public void setFocus() {
		diagramComposite.setFocus();
	}

	@Override
	public void dispose() {
		if (diagramFrame != null)
			diagramFrame.dispose();
		if (snailShell != null)
			snailShell.stop();
		super.dispose();
	}
}
