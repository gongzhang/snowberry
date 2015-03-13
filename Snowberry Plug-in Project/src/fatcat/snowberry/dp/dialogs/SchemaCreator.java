package fatcat.snowberry.dp.dialogs;

import java.util.LinkedList;

import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.dp.schema.Relationship;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.dp.schema.internal.Schema;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;
import fatcat.snowberry.tag.dialogs.ModelBrowser;


public class SchemaCreator extends Dialog {

	private Text txtComment;
	private Text txtRelations;
	private Text txtRoles;
	private Text txtName;
	private Text txtModel;
	protected Object result;
	protected Shell shell;
	private IMemberModel memberModel = null;

	/**
	 * Create the dialog
	 * @param parent
	 */
	public SchemaCreator(Shell parent) {
		super(parent, SWT.NONE);
	}

	/**
	 * Open the dialog
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(598, 461);
		shell.setText(International.CreateDesignPattern);

		final Group group = new Group(shell, SWT.NONE);
		group.setText(International.EmbedCode);
		group.setBounds(10, 10, 572, 100);

		final Label label = new Label(group, SWT.NONE);
		label.setText(International.PleaseSpecifyPosition);
		label.setBounds(25, 28, 336, 17);

		txtModel = new Text(group, SWT.READ_ONLY | SWT.BORDER);
		txtModel.setText(International.ClickBrowseToSpecifyPosition);
		txtModel.setBounds(36, 59, 427, 25);

		final Button btnBrowser = new Button(group, SWT.NONE);
		btnBrowser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Shell shell = SnowberryCore.getDefaultShell();
				ModelBrowser browser = new ModelBrowser(shell) {
					@Override
					protected boolean isLegalResult(Object element) {
						return element instanceof IMemberModel;
					}
				};
				IMemberModel rst = (IMemberModel) browser.open();
				if (rst != null) {
					memberModel = rst;
					if (memberModel.getKind() == IMemberModel.TYPE) {
						txtModel.setText(((ITypeModel) memberModel).getJavaElement().getFullyQualifiedName());
					} else {
						txtModel.setText(memberModel.getOwnerType().getJavaElement().getFullyQualifiedName() + "::" + memberModel.getJavaElement().getElementName()); //$NON-NLS-1$
					}
				}
			}
		});
		btnBrowser.setText(International.Browse);
		btnBrowser.setBounds(469, 57, 94, 27);

		final Group group_1 = new Group(shell, SWT.NONE);
		group_1.setText(International.DesignPatternDefinition);
		group_1.setBounds(10, 116, 572, 275);

		final Label label_1 = new Label(group_1, SWT.NONE);
		label_1.setText(International.Name);
		label_1.setBounds(45, 30, 36, 17);

		txtName = new Text(group_1, SWT.BORDER);
		txtName.setBounds(87, 27, 181, 25);

		txtRoles = new Text(group_1, SWT.BORDER);
		txtRoles.setBounds(87, 58, 181, 25);

		final Label label_1_1 = new Label(group_1, SWT.NONE);
		label_1_1.setBounds(274, 30, 213, 17);
		label_1_1.setText(International.DesignPatternExplaination);

		final Label label_1_2 = new Label(group_1, SWT.NONE);
		label_1_2.setBounds(21, 61, 60, 17);
		label_1_2.setText(International.Role);

		final Label label_1_1_1 = new Label(group_1, SWT.NONE);
		label_1_1_1.setBounds(274, 61, 153, 17);
		label_1_1_1.setText(International.UseCommaToSeparate);

		final Label label_1_2_1 = new Label(group_1, SWT.NONE);
		label_1_2_1.setBounds(21, 138, 60, 17);
		label_1_2_1.setText(International.Relationship);

		txtRelations = new Text(group_1, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		txtRelations.setBounds(87, 135, 475, 108);

		final Label label_1_2_2 = new Label(group_1, SWT.NONE);
		label_1_2_2.setBounds(45, 92, 36, 17);
		label_1_2_2.setText(International.Description);

		txtComment = new Text(group_1, SWT.BORDER);
		txtComment.setBounds(87, 89, 475, 25);

		final Label label_1_1_1_1 = new Label(group_1, SWT.NONE);
		label_1_1_1_1.setBounds(87, 249, 475, 17);
		label_1_1_1_1.setText(International.DescriptionExplanation);

		final Button btnOK = new Button(shell, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				if (memberModel == null) {
					SnowberryCore.showErrMsgBox(International.CreateDesignPattern, International.PleaseSpecifyValidPosition);
					return;
				}
				if (txtName.getText().trim().length() == 0) {
					SnowberryCore.showErrMsgBox(International.CreateDesignPattern, International.DesignPatternNameCannotBeNull);
					return;
				}
				final Schema schema = new Schema(txtName.getText().trim(), txtComment.getText().trim());
				
				String[] roles = txtRoles.getText().split(","); //$NON-NLS-1$
				LinkedList<Role> rs = new LinkedList<Role>();
				for (String role : roles) {
					if (role.trim().length() == 0) {
						SnowberryCore.showErrMsgBox(International.CreateDesignPattern, International.RoleNameCannotBeNull);
						return;
					} else {
						rs.add(new Role(schema, role.trim()));
					}
				}
				if (rs.size() == 0) {
					SnowberryCore.showErrMsgBox(International.CreateDesignPattern, International.PleaseAddOneRole);
					return;
				}
				for (Role r : rs) {
					schema.addRole(r);
				}
				
				String[] relations = txtRelations.getText().split("\n"); //$NON-NLS-1$
				for (String relation : relations) {
					if (relation.trim().length() == 0) continue;
					Relationship r = Relationship.parseRelationship(relation.trim(), schema);
					if (r != null) {
						schema.addRelationship(r);
					} else {
						SnowberryCore.showErrMsgBox(International.CreateDesignPattern, International.InvalidRelationshipDescription + relation);
						return;
					}
				}
				
				final ITag tag = schema.toTag();
				TagTask task = new TagTask();
				task.addTag(memberModel, tag);
				
				try {
					task.execute();
				} catch (SourceEditingException e1) {
//					SnowberryCore.println(e1.getMessage());
					SnowberryCore.log(Status.ERROR, International.CannotSaveDesignPatternInformation, e1);
					SnowberryCore.showErrMsgBox(International.CreateDesignPattern, International.CannotSaveDesignPatternInformation);
					return;
				}
				
				shell.dispose();
				
			}
		});
		shell.setDefaultButton(btnOK);
		btnOK.setText(International.Ok);
		btnOK.setBounds(386, 397, 95, 27);

		final Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setText(International.Cancel);
		btnCancel.setBounds(487, 397, 95, 27);
		//
	}

}
