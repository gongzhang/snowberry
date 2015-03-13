package fatcat.snowberry.tag.dialogs;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IFieldModel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IMethodModel;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.TagCore;

public abstract class ModelBrowser extends Dialog implements ISelectionChangedListener {

	private Text text;
	private Tree tree;
	protected Object result = null;
	protected Shell shell;

	/**
	 * Create the dialog
	 * 
	 * @param parent
	 */
	public ModelBrowser(Shell parent) {
		super(parent, SWT.NONE);
	}

	/**
	 * Open the dialog
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		Monitor primary = shell.getMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		shell.setLocation(x, y);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents() {
		final IProjectModel[] input = TagCore.getProjectModels();

		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(597, 394);
		shell.setText(International.ModelExplorer);

		final TreeViewer treeViewer = new TreeViewer(shell, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.setBounds(10, 10, 195, 314);
		treeViewer.setLabelProvider(new LabelProvider() {

			private final Image img_proj, img_type, img_field, img_method;

			{
				img_proj = loadImage(Display.getDefault(), getClass(), "/fatcat/snowberry/gui/res/eclipse-icons/projects.gif"); //$NON-NLS-1$
				img_type = loadImage(Display.getDefault(), getClass(),
						"/fatcat/snowberry/gui/res/eclipse-icons/class_obj(1).gif"); //$NON-NLS-1$
				img_field = loadImage(Display.getDefault(), getClass(),
						"/fatcat/snowberry/gui/res/eclipse-icons/field_public_obj.gif"); //$NON-NLS-1$
				img_method = loadImage(Display.getDefault(), getClass(),
						"/fatcat/snowberry/gui/res/eclipse-icons/methpub_obj.gif"); //$NON-NLS-1$
			}

			private Image loadImage(Display display, Class<?> clazz, String string) {
				InputStream stream = clazz.getResourceAsStream(string);
				if (stream == null)
					return null;
				Image image = null;
				try {
					image = new Image(display, stream);
				} catch (SWTException ex) {} finally {
					try {
						stream.close();
					} catch (IOException ex) {}
				}
				return image;
			}

			@Override
			public Image getImage(Object element) {
				if (element instanceof IProjectModel)
					return img_proj;
				else if (element instanceof ITypeModel)
					return img_type;
				else if (element instanceof IMethodModel)
					return img_method;
				else if (element instanceof IFieldModel)
					return img_field;
				else
					return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				if (element instanceof IProjectModel)
					return ((IProjectModel) element).getResource().getName();
				else if (element instanceof ITypeModel)
					return ((ITypeModel) element).getJavaElement().getElementName();
				else if (element instanceof IMethodModel)
					return ((IMethodModel) element).getJavaElement().getElementName();
				else if (element instanceof IFieldModel)
					return ((IFieldModel) element).getJavaElement().getElementName();
				else if (element instanceof IProjectModel[])
					return International.WorkSpace;
				else
					return super.getText(element);
			}

		});
		treeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof IProjectModel)
					return ((IProjectModel) parentElement).getITypeModels();
				else if (parentElement instanceof ITypeModel)
					return ((ITypeModel) parentElement).getMemberModels();
				else if (parentElement instanceof IProjectModel[])
					return (IProjectModel[]) parentElement;
				else
					return null;
			}

			@Override
			public Object getParent(Object element) {
				if (element instanceof IMemberModel) {
					if (((IMemberModel) element).getKind() != IMemberModel.TYPE) {
						return ((IMemberModel) element).getOwnerType();
					} else {
						return ((IMemberModel) element).getOwnerProject();
					}
				} else {
					return input;
				}
			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof IProjectModel)
					return true;
				else if (element instanceof ITypeModel)
					return true;
				else if (element instanceof IProjectModel[])
					return true;
				else
					return false;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof IProjectModel)
					return ((IProjectModel) inputElement).getITypeModels();
				else if (inputElement instanceof ITypeModel)
					return ((ITypeModel) inputElement).getMemberModels();
				else if (inputElement instanceof IProjectModel[])
					return (IProjectModel[]) inputElement;
				else
					return null;
			}

			@Override
			public void dispose() {}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}
		});
		treeViewer.setInput(input);

		final Button btnOK = new Button(shell, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				Object sel = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
				if (sel == null) {
					SnowberryCore.showErrMsgBox(International.SelectModel, International.PleaseSelectModel);
					return;
				}
				if (!isLegalResult(sel)) {
					SnowberryCore.showErrMsgBox(International.SelectModel, International.PleaseSelectCorrectModel);
					return;
				}
				result = sel;
				shell.dispose();
			}
		});
		shell.setDefaultButton(btnOK);
		btnOK.setText(International.Ok);
		btnOK.setBounds(397, 331, 89, 27);

		final Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setText(International.Cancel);
		btnCancel.setBounds(492, 331, 89, 27);

		final Group group = new Group(shell, SWT.NONE);
		group.setText(International.Code);
		group.setBounds(211, 10, 370, 315);

		text = new Text(group, SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		text.setText(International.SelectProjectToViewCode);
		text.setBounds(10, 23, 350, 282);

		treeViewer.addSelectionChangedListener(this);
		//
	}

	abstract protected boolean isLegalResult(Object element);

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Object sel = ((TreeSelection) event.getSelection()).getFirstElement();
		if (sel == null) {
			text.setText(International.SelectProjectToViewCode);
		} else if (sel instanceof IProjectModel) {
			IProjectModel p = (IProjectModel) sel;
			text.setText(International.Project + p.getResource().getName());
		} else if (sel instanceof IMemberModel) {
			IMemberModel m = (IMemberModel) sel;
			try {
				text.setText(m.getJavaElement().getSource());
			} catch (JavaModelException e) {
				text.setText(International.CannotDisplayProjectCode);
			}
		}
	}

}
