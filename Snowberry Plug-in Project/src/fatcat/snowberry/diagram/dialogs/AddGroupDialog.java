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
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;
import fatcat.snowberry.tag.internal.Tag;


public class AddGroupDialog extends Dialog implements ITagFilter {

	private Text text;
	protected Object result;
	protected Shell shell;
	private final IMemberModel[] models;

	/**
	 * Create the dialog
	 * @param parent
	 */
	public AddGroupDialog(Shell parent, IMemberModel[] models) {
		super(parent, SWT.NONE);
		this.models = models;
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
		shell.setSize(346, 187);
		shell.setText(International.Group);

		final Label label = new Label(shell, SWT.NONE);
		label.setText(International.GroupMembers);
		label.setBounds(10, 10, 351, 17);

		final Group group = new Group(shell, SWT.NONE);
		group.setText(International.GroupInformation);
		group.setBounds(10, 33, 320, 84);

		final Label label_1 = new Label(group, SWT.NONE);
		label_1.setText(International.GroupName);
		label_1.setBounds(10, 24, 36, 17);

		text = new Text(group, SWT.BORDER);
		text.setBounds(20, 47, 290, 25);
		
		String name = models[0].getJavaElement().getElementName();
		for (IMemberModel model : models) {
			if (model.getJavaElement().getElementName().length() < name.length()) {
				name = model.getJavaElement().getElementName();
			}
		}
		text.setText(Character.toUpperCase(name.charAt(0)) + name.substring(1));

		final Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (text.getText().length() == 0) {
					SnowberryCore.showErrMsgBox(International.CannotCreateGroup, International.GroupNameCannotBeNull);
					return;
				}
				btnOK_Clicked();
			}
		});
		shell.setDefaultButton(button);
		button.setText(International.Ok);
		button.setBounds(164, 123, 80, 27);

		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		button_1.setText(International.Cancel);
		button_1.setBounds(250, 123, 80, 27);
		//
	}
	
	private void btnOK_Clicked() {
		TagTask task = new TagTask();
		Tag tag = new Tag();
		tag.setName("group"); //$NON-NLS-1$
		tag.addProperty("name", text.getText()); //$NON-NLS-1$
		for (IMemberModel model : models) {
			task.removeTag(model, this);
			task.addTag(model, tag);
		}
		try {
			task.execute();
		} catch (SourceEditingException e) {
			SnowberryCore.showErrMsgBox(International.CannotCreateGroup, e.getMessage());
		} finally {
			shell.dispose();
		}
	}

	@Override
	public boolean isAccepted(ITag tag) {
		return tag.getName().equals("group"); //$NON-NLS-1$
	}

}
