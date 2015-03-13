package fatcat.snowberry.dp.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.dp.schema.ISchema;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.dialogs.ModelBrowser;


public class DPMemberItem extends Composite implements ControlListener {

	private Button btnDel;
	private Button btnBrowse;
	private Text txtComment;
	private Text txtModel;
	private Combo txtRole;
	
	private final DPCreator editor;
	private IMemberModel model;
	
	/**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
	public DPMemberItem(Composite parent, final DPCreator editor) {
		super(parent, SWT.NONE);
		addControlListener(this);

		txtModel = new Text(this, SWT.READ_ONLY | SWT.BORDER);
		txtModel.setBounds(0, 3, 126, 23);

		btnBrowse = new Button(this, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
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
					model = rst;
					txtModel.setText(model.getJavaElement().getElementName());
				}
			}
		});
		btnBrowse.setBounds(132, 0, 28, 26);
		btnBrowse.setText("...");

		txtRole = new Combo(this, SWT.NONE);
		txtRole.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				ISchema schema = editor.getSchema();
				if (schema != null) {
					Role[] roles = schema.getRoles();
					String[] items = new String[roles.length];
					for (int i = 0; i < roles.length; i++) {
						items[i] = roles[i].getName();
					}
					txtRole.setItems(items);
				}
			}
		});
		txtRole.setBounds(166, 3, 148, 23);

		txtComment = new Text(this, SWT.BORDER);
		txtComment.setBounds(320, 3, 148, 23);

		btnDel = new Button(this, SWT.NONE);
		btnDel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				editor.itemRemoved(DPMemberItem.this);
			}
		});
		btnDel.setText("X");
		btnDel.setBounds(477, 1, 28, 27);
		//
		
		this.editor = editor;
	}
	
	public DPMemberItem(Composite parent, final DPCreator editor, IMemberModel initModel) {
		this(parent, editor);
		model = initModel;
		txtModel.setText(model.getJavaElement().getElementName());
	}

	@Override
	public void controlMoved(ControlEvent e) {
	}

	@Override
	public void controlResized(ControlEvent e) {
		txtModel.setLocation(0, 0);
		txtModel.setSize(128, getSize().y);
		btnBrowse.setLocation(txtModel.getSize().x, 0);
		btnBrowse.setSize(getSize().y, getSize().y);
		txtRole.setLocation(btnBrowse.getLocation().x + btnBrowse.getSize().x, 0);
		txtRole.setSize(128, getSize().y);
		txtComment.setLocation(txtRole.getLocation().x + txtRole.getSize().x, 0);
		txtComment.setSize(Math.max(64, getSize().x - txtComment.getLocation().x - getSize().y), getSize().y);
		btnDel.setLocation(getSize().x - getSize().y, 0);
		btnDel.setSize(btnBrowse.getSize());
	}
	
	public IMemberModel getModel() {
		return model;
	}
	
	public Role getRole() {
		ISchema schema = editor.getSchema();
		if (schema == null) return null;
		Role[] roles = schema.getRoles();
		for (Role r : roles) {
			if (r.getName().equals(txtRole.getText())) {
				return r;
			}
		}
		return null;
	}
	
	public String getRoleString() {
		return txtRole.getText();
	}
	
	public String getComment() {
		return txtComment.getText();
	}

}
