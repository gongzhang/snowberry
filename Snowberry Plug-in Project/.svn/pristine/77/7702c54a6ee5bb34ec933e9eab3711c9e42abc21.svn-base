package fatcat.snowberry.tag.ui;

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
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.TagResolveException;
import fatcat.snowberry.tag.internal.Tag;
import fatcat.snowberry.tag.internal.TagParser;


public class TagEditor extends Dialog {

	protected Object result;
	protected Shell shell;
	private Tag tag;

	/**
	 * 打开标签编辑器
	 * @param parent
	 * @param tag 初始数据或{@code null}
	 */
	public TagEditor(Shell parent, Tag tag) {
		super(parent, SWT.NONE);
		if (tag == null) {
			this.tag = new Tag();
			this.tag.setName(International.TagName);
			this.tag.addProperty(International.Attribute1, International.Value1);
			this.tag.addProperty(International.Attribute2, International.Value2);
			this.tag.addProperty(International.Attribute3, International.Value3);
		} else {
			this.tag = tag;
		}
	}
	
	/**
	 * 返回编辑后的标签，若取消则返回<code>null</code>。
	 * @return 编辑后的标签或{@code null}
	 */
	public ITag getResult() {
		return tag;
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
		shell.setSize(409, 317);
		shell.setText(International.TagEditor);

		final Group group = new Group(shell, SWT.NONE);
		group.setText(International.TagContent);
		group.setBounds(10, 10, 383, 236);

		final Label label = new Label(group, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText(International.TagName);
		label.setBounds(10, 27, 64, 17);

		final Text txtName = new Text(group, SWT.BORDER);
		txtName.setBounds(80, 24, 293, 25);

		final Label label_1 = new Label(group, SWT.NONE);
		label_1.setText(International.AttributeName);
		label_1.setBounds(10, 61, 64, 17);

		final Text txtProperpty = new Text(group, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		txtProperpty.setBounds(10, 84, 110, 121);

		final Text txtValue = new Text(group, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		txtValue.setBounds(126, 84, 247, 121);

		final Label label_2 = new Label(group, SWT.NONE);
		label_2.setText(International.AttributeValue);
		label_2.setBounds(126, 61, 96, 17);

		final Label label_3 = new Label(group, SWT.NONE);
		label_3.setText(International.EachLineContainsOneAttributeAndOneValue);
		label_3.setBounds(10, 211, 363, 17);

		final Button btnOK = new Button(shell, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(txtName.getText());
				String[] names = txtProperpty.getText().split("\n"); //$NON-NLS-1$
				String[] values = txtValue.getText().split("\n"); //$NON-NLS-1$
				if (names.length != values.length) {
					SnowberryCore.showErrMsgBox(International.CannotCreateTag, International.NumberOfAttributesAndNumberOfValuesNotEqual);
					return;
				}
				for (int i = 0; i < names.length; i++) {
					if (names[i].length() != 0)
					stringBuffer.append("\n" + names[i].replaceAll("\r", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					stringBuffer.append(" = \""); //$NON-NLS-1$
					stringBuffer.append(values[i].replaceAll("\r", "")); //$NON-NLS-1$ //$NON-NLS-2$
					stringBuffer.append("\""); //$NON-NLS-1$
				}
				try {
					tag = TagParser.parseTag(stringBuffer.toString());
				} catch (TagResolveException e1) {
					SnowberryCore.showErrMsgBox(International.CannotCreateTag, International.MalformedTag + e1.getMessage());
					return;
				}
				shell.close();
			}
		});
		shell.setDefaultButton(btnOK);
		btnOK.setText(International.Ok);
		btnOK.setBounds(229, 252, 79, 27);
		
		final Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.close();
			}
		});
		btnCancel.setText(International.Cancel);
		btnCancel.setBounds(314, 252, 79, 27);
		//
		
		txtName.setText(tag.getName());
		for (int i = 0; i < tag.size(); i++) {
			txtProperpty.append(tag.getPropertyName(i) + "\n"); //$NON-NLS-1$
			txtValue.append(tag.getPropertyValue(i) + "\n"); //$NON-NLS-1$
		}
		tag = null;
	}

}
