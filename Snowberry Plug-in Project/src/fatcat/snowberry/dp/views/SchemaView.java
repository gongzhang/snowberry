package fatcat.snowberry.dp.views;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import fatcat.snowberry.core.International;
import fatcat.snowberry.dp.DesignPatternCore;
import fatcat.snowberry.dp.schema.ISchema;
import fatcat.snowberry.dp.schema.ISchemaListener;
import fatcat.snowberry.dp.schema.Relationship;
import fatcat.snowberry.dp.schema.Role;

public class SchemaView extends ViewPart implements ISchemaListener, ISelectionChangedListener {

	private TreeViewer treeViewer;
	private ISchema[] schemas;

	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.NONE);
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
				} else if (element instanceof Role[]) { return ((Role[]) element).length + International.NumberOfMembers;
				} else if (element instanceof Role) { return ((Role) element).getName();
				} else if (element instanceof Relationship[]) { return ((Relationship[]) element).length + International.NumberOfRoles;
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
					return schemas;
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
		schemas = DesignPatternCore.getSchemas();
		DesignPatternCore.addSchemaListener(this);
		treeViewer.setInput(schemas);
		treeViewer.addSelectionChangedListener(this);
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	@Override
	public void dispose() {
		DesignPatternCore.removeSchemaListener(this);
		super.dispose();
	}

	@Override
	public void schemaAdded(ISchema schema) {
		final ISchema[] newSchemas = new ISchema[schemas.length + 1];
		int i = 0;
		for (ISchema s : schemas) {
			newSchemas[i++] = s;
		}
		newSchemas[i] = schema;
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				schemas = newSchemas;
				treeViewer.setInput(schemas);
			}
		});
	}

	@Override
	public void schemaRemoved(ISchema schema) {
		final ISchema[] newSchemas = new ISchema[schemas.length - 1];
		int i = 0;
		for (ISchema s : schemas) {
			if (!schema.equals(s))
				newSchemas[i++] = s;
		}
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				schemas = newSchemas;
				treeViewer.setInput(schemas);
			}
		});
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
