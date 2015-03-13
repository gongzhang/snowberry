package fatcat.snowberry.dp.dialogs;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.UUID;

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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.dp.DesignPatternCore;
import fatcat.snowberry.dp.schema.ISchema;
import fatcat.snowberry.dp.schema.Relationship;
import fatcat.snowberry.dp.schema.Role;


public class SchemaBrowser extends Dialog implements ISelectionChangedListener {

	@SuppressWarnings("unused")
	private Tree tree;
	protected ISchema result = null;
	protected Shell shell;
	private TreeViewer treeViewer;

	/**
	 * Create the dialog
	 * @param parent
	 */
	public SchemaBrowser(Shell parent) {
		super(parent, SWT.NONE);
	}

	/**
	 * Open the dialog
	 * @return the result
	 */
	public ISchema open() {
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setLayout(new FormLayout());
		shell.setSize(265, 362);
		shell.setText(International.DesignPatternSelector);

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		final FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 295);
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.right = new FormAttachment(0, 249);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);

		treeViewer = new TreeViewer(composite, SWT.BORDER);
		tree = treeViewer.getTree();
		treeViewer.setLabelProvider(new LabelProvider() {

			private final Image img_dp, img_id, img_roles, img_rlts, img_role, img_rlt, img_cmt;

			{
				img_dp = loadImage(Display.getDefault(), getClass(), "/fatcat/snowberry/gui/res/eclipse-icons/types.gif"); //$NON-NLS-1$
				img_id = loadImage(Display.getDefault(), getClass(), "/fatcat/snowberry/gui/res/eclipse-icons/bkmrk_tsk.gif"); //$NON-NLS-1$
				img_roles = loadImage(Display.getDefault(), getClass(), "/fatcat/snowberry/gui/res/eclipse-icons/genericreggroup_obj.gif"); //$NON-NLS-1$
				img_rlts = loadImage(Display.getDefault(), getClass(), "/fatcat/snowberry/gui/res/eclipse-icons/ch_callees(3).gif"); //$NON-NLS-1$
				img_role = loadImage(Display.getDefault(), getClass(), "/fatcat/snowberry/gui/res/eclipse-icons/genericreggroup_obj.gif"); //$NON-NLS-1$
				img_rlt = loadImage(Display.getDefault(), getClass(), "/fatcat/snowberry/gui/res/eclipse-icons/ch_callees(3).gif"); //$NON-NLS-1$
				img_cmt = loadImage(Display.getDefault(), getClass(), "/fatcat/snowberry/gui/res/eclipse-icons/copy_edit_co(1).gif"); //$NON-NLS-1$
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
				if (element instanceof ISchema[]) { return img_dp;
				} else if (element instanceof ISchema) { return img_dp;
				} else if (element instanceof Role[]) { return img_roles;
				} else if (element instanceof Role) { return img_role;
				} else if (element instanceof Relationship[]) { return img_rlts;
				} else if (element instanceof Relationship) { return img_rlt;
				} else if (element instanceof UUID) { return img_id;
				} else if (element instanceof String) { return img_cmt;
				} else {
					return super.getImage(element);
				}
			}

			@Override
			public String getText(Object element) {
				if (element instanceof ISchema[]) { return International.WorkSpace;
				} else if (element instanceof ISchema) { return ((ISchema) element).getPatternName();
				} else if (element instanceof Role[]) { return ((Role[]) element).length + International.NumberOfRoles;
				} else if (element instanceof Role) { return ((Role) element).getName();
				} else if (element instanceof Relationship[]) { return ((Relationship[]) element).length + International.NumberOfRelations;
				} else if (element instanceof Relationship) { return ((Relationship) element).getQualifiedString();
				} else if (element instanceof UUID) { return ((UUID) element).toString();
				} else if (element instanceof String) { return element.toString();
				} else {
					return super.getText(element);
				}
			}
		});
		treeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof ISchema[]) {
					return (ISchema[]) parentElement;
				} else if (parentElement instanceof ISchema) {
					final ISchema schema = (ISchema) parentElement;
					final LinkedList<Object> children = new LinkedList<Object>();
					children.add(schema.getID()); // UUID
					children.add(schema.getRoles()); // Role[]
					children.add(schema.getRelationships()); // Relationship[]
					children.add(schema.getComment()); // String
					return children.toArray();
				} else if (parentElement instanceof Role[]) {
					return (Role[]) parentElement;
				} else if (parentElement instanceof Relationship[]) {
					return (Relationship[]) parentElement;
				} else {
					return null;
				}
			}

			@Override
			public Object getParent(Object element) {
				if (element instanceof ISchema) {
					return null;
				} else if (element instanceof Role) {
					return ((Role) element).getSchema();
				} else if (element instanceof Relationship) {
					return ((Relationship) element).getSchema();
				} else {
					return null;
				}
			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof ISchema[]) {
					return true;
				} else if (element instanceof ISchema) {
					return true;
				} else if (element instanceof Role[]) {
					return true;
				} else if (element instanceof Relationship[]) {
					return true;
				} else {
					return false;
				}
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return getChildren(inputElement);
			}

			@Override
			public void dispose() {}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		});
		treeViewer.addSelectionChangedListener(this);
		treeViewer.setInput(DesignPatternCore.getSchemas());
		
		final Button btnOK = new Button(shell, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (!(getSelectedItem() instanceof ISchema)) {
					SnowberryCore.showMsgBox(International.SelectDesignPattern, International.PleaseSelectDesignPattern);
					return;
				}
				result = (ISchema) getSelectedItem();
				shell.dispose();
			}
		});
		final FormData fd_btnOK = new FormData();
		fd_btnOK.bottom = new FormAttachment(0, 328);
		fd_btnOK.top = new FormAttachment(0, 301);
		fd_btnOK.right = new FormAttachment(0, 155);
		fd_btnOK.left = new FormAttachment(0, 67);
		btnOK.setLayoutData(fd_btnOK);
		shell.setDefaultButton(btnOK);
		btnOK.setText(International.Ok);

		final Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		final FormData fd_btnCancel = new FormData();
		fd_btnCancel.bottom = new FormAttachment(0, 328);
		fd_btnCancel.top = new FormAttachment(0, 301);
		fd_btnCancel.right = new FormAttachment(0, 249);
		fd_btnCancel.left = new FormAttachment(0, 161);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText(International.Cancel);
		
		//
	}
	
	private Object selectedItem = null;

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		selectedItem = ((TreeSelection) event.getSelection()).getFirstElement();
	}
	
	public Object getSelectedItem() {
		return selectedItem;
	}

}
