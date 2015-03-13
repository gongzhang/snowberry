package fatcat.snowberry.dp.dialogs;

import java.util.LinkedList;
import java.util.UUID;

import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.dp.schema.ISchema;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.MultiFileTask;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.internal.Tag;


public class DPCreator extends Dialog {

	private Composite list;
	private Text txtSchemaName;
	protected Object result;
	protected Shell shell;
	private ISchema schema = null;

	/**
	 * Create the dialog
	 * @param parent
	 */
	public DPCreator(Shell parent) {
		super(parent, SWT.NONE);
	}
	
	public Object open(IMemberModel initModel) {
		return open(new IMemberModel[] {initModel});
	}

	/**
	 * Open the dialog
	 * @return the result
	 */
	public Object open(IMemberModel[] initModels) {
		createContents(initModels);
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
	 * Open the dialog
	 * @return the result
	 */
	public Object open() {
		return open(new IMemberModel[0]);
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents(IMemberModel[] initModels) {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(524, 454);
		shell.setText(International.DesignPatternInstanceEditor);

		final Group group = new Group(shell, SWT.NONE);
		group.setText(International.DesignPattern);
		group.setBounds(10, 10, 498, 110);

		final Label label = new Label(group, SWT.NONE);
		label.setText(International.DesignPatternName);
		label.setBounds(24, 27, 60, 17);

		txtSchemaName = new Text(group, SWT.READ_ONLY | SWT.BORDER);
		txtSchemaName.setBounds(90, 24, 164, 25);

		final Label lblSchemaComment = new Label(group, SWT.WRAP);
		lblSchemaComment.setText(International.PleaseSelectDesignPattern);
		lblSchemaComment.setBounds(24, 58, 464, 42);

		final Button button = new Button(group, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Shell shell = SnowberryCore.getDefaultShell();
				SchemaBrowser browser = new SchemaBrowser(shell);
				ISchema rst = browser.open();
				if (rst != null) {
					schema = rst;
					txtSchemaName.setText(schema.getPatternName());
					lblSchemaComment.setText(schema.getComment());
				}
			}
		});
		button.setText(International.Pick);
		button.setBounds(260, 22, 60, 27);

		final Group group_1 = new Group(shell, SWT.NONE);
		group_1.setText(International.Member);
		group_1.setBounds(10, 126, 498, 259);

		final ScrolledComposite scrolledComposite = new ScrolledComposite(group_1, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scrolledComposite.setBounds(10, 56, 478, 193);
		scrolledComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		list = new Composite(scrolledComposite, SWT.NONE);
		list.setLocation(0, 0);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		list.setLayout(gridLayout);
		list.setSize(473, 25);
		scrolledComposite.setContent(list);

		final Button button_2 = new Button(group_1, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addNewItem();
			}
		});
		button_2.setText(International.AddMember);
		button_2.setBounds(10, 23, 60, 27);

		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (schema == null) {
					SnowberryCore.showErrMsgBox(International.CreateDesignPatternInstance, International.PleaseSpecifyDesignPattern);
					return;
				}
				final Control[] controls = list.getChildren();
				final LinkedList<DPMemberItem> items = new LinkedList<DPMemberItem>();
				for (Control control : controls) {
					if (control instanceof DPMemberItem) {
						items.add((DPMemberItem) control);
					}
				}
				if (items.size() == 0) {
					SnowberryCore.showErrMsgBox(International.CreateDesignPatternInstance, International.PleaseAddMember);
					return;
				}
				
				final MultiFileTask task = new MultiFileTask();
				final UUID id = UUID.randomUUID();
				for (DPMemberItem item : items) {
					final Tag tag = new Tag();
					tag.setName("pattern"); //$NON-NLS-1$
					tag.addProperty("id", id.toString()); //$NON-NLS-1$
					tag.addProperty("schema", schema.getID().toString()); //$NON-NLS-1$
					
					Role role = item.getRole();
					if (role == null) {
						SnowberryCore.showErrMsgBox(International.CreateDesignPatternInstance, International.UnknownRoleNameFound + item.getRoleString());
						return;
					}
					
					tag.addProperty("role", role.getName()); //$NON-NLS-1$
					tag.addProperty("comment", item.getComment()); //$NON-NLS-1$
					
					IMemberModel model = item.getModel();
					if (model == null) {
						SnowberryCore.showErrMsgBox(International.CreateDesignPatternInstance, International.PleaseRemoveBlankLines);
						return;
					}
					
					task.addTag(model, tag);
					
				}
				
				try {
					task.execute();
					shell.dispose();
				} catch (SourceEditingException e1) {
//					SnowberryCore.println(e1.getMessage());
					SnowberryCore.log(Status.ERROR, International.CannotSaveDesignPatternInstance, e1);
					SnowberryCore.showErrMsgBox(International.CreateDesignPatternInstance, International.CannotSaveDesignPatternInstance);
				}
			}
		});
		shell.setDefaultButton(button_1);
		button_1.setText(International.Ok);
		button_1.setBounds(314, 391, 94, 27);

		final Button button_1_1 = new Button(shell, SWT.NONE);
		button_1_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		button_1_1.setBounds(414, 391, 94, 27);
		button_1_1.setText(International.Cancel);
		//
		
		if (initModels != null) {
			for (IMemberModel model : initModels)
				addNewItem(model);
		}
	}
	
	public void itemRemoved(DPMemberItem item) {
		item.dispose();
		list.setSize(list.getSize().x, list.getChildren().length * 25);
	}
	
	public ISchema getSchema() {
		return schema;
	}
	
	private void addNewItem() {
		addNewItem(null);
	}
	
	private void addNewItem(IMemberModel initModel) {
		DPMemberItem i = null;
		if (initModel != null) {
			i = new DPMemberItem(list, DPCreator.this, initModel);
		} else {
			i = new DPMemberItem(list, DPCreator.this);
		}
		i.setSize(list.getSize().x, 25);
		list.setSize(list.getSize().x, list.getChildren().length * 25);
	}

}
