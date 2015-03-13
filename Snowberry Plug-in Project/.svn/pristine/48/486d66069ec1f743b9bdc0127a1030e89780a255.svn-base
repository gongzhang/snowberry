package fatcat.snowberry.diagram.dialogs;

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


public class AddLabelDialog extends Dialog {

	private Text txtComment;
	private Text txtName;
	protected fatcat.snowberry.diagram.Label result = null;
	protected Shell shell;

	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public AddLabelDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public AddLabelDialog(Shell parent) {
		this(parent, SWT.NONE);
	}

	/**
	 * Open the dialog
	 * @return the result
	 */
	public fatcat.snowberry.diagram.Label open() {
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
		shell.setSize(367, 245);
		shell.setText(International.AddLabel);

		final Group group = new Group(shell, SWT.NONE);
		group.setText(International.LabelInfomation);
		group.setBounds(10, 10, 341, 164);

		final Label label = new Label(group, SWT.NONE);
		label.setText(International.LabelName);
		label.setBounds(10, 33, 60, 17);

		txtName = new Text(group, SWT.BORDER);
		txtName.setText(International.NewLabel);
		txtName.setBounds(76, 30, 255, 25);

		final Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(34, 64, 36, 17);
		label_1.setText(International.LabelComment);

		txtComment = new Text(group, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		txtComment.setBounds(76, 61, 255, 93);

		final Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (txtName.getText().length() == 0) {
					SnowberryCore.showErrMsgBox(International.CannotAddLabel, International.LabelNameCannotBeNull);
					return;
				} else {
					result = new fatcat.snowberry.diagram.Label(txtName.getText(), txtComment.getText());
					shell.dispose();
				}
			}
		});
		shell.setDefaultButton(button);
		button.setText(International.Ok);
		button.setBounds(185, 180, 80, 27);

		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		button_1.setText(International.Cancel);
		button_1.setBounds(271, 180, 80, 27);
		//
	}

}
