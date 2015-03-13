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
import fatcat.snowberry.diagram.Category;
import fatcat.snowberry.gui.CategoryPanel;


public class CategoryEditor extends Dialog {

	private Text text;
	protected Category result = null, source;
	protected Shell shell;
	private int color;

	/**
	 * Create the dialog
	 * @param parent
	 */
	public CategoryEditor(Shell parent, Category source) {
		super(parent, SWT.NONE);
		if (source == null) {
			source = new Category(International.NewCategory);
		}
		this.source = source;
		color = source.color;
	}

	/**
	 * Open the dialog
	 * @return the result
	 */
	public Category open() {
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
		shell.setSize(334, 204);
		shell.setText(International.CategoryEditor);

		final Group group = new Group(shell, SWT.NONE);
		group.setText(International.CategoryInfomation);
		group.setBounds(10, 10, 308, 124);

		final Label label = new Label(group, SWT.NONE);
		label.setText(International.Name);
		label.setBounds(10, 28, 36, 17);

		text = new Text(group, SWT.BORDER);
		text.setBounds(52, 25, 246, 25);

		final Label label_1 = new Label(group, SWT.NONE);
		label_1.setText(International.Color);
		label_1.setBounds(10, 66, 36, 17);

		final Button button = new Button(group, SWT.RADIO);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				color = CategoryPanel.COLOR_BLUE;
			}
		});
		button.setText(International.Blue);
		button.setBounds(52, 66, 50, 17);
		button.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Button hongseButton = new Button(group, SWT.RADIO);
		hongseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				color = CategoryPanel.COLOR_RED;
			}
		});
		hongseButton.setBounds(108, 66, 50, 17);
		hongseButton.setText(International.Red);
		button.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_RED));

		final Button button_2 = new Button(group, SWT.RADIO);
		button_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				color = CategoryPanel.COLOR_GREEN;
			}
		});
		button_2.setBounds(164, 66, 50, 17);
		button_2.setText(International.Green);
		button.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_GREEN));

		final Button button_3 = new Button(group, SWT.RADIO);
		button_3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				color = CategoryPanel.COLOR_YELLOW;
			}
		});
		button_3.setBounds(108, 89, 50, 17);
		button_3.setText(International.Yellow);
		button.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));

		final Button button_4 = new Button(group, SWT.RADIO);
		button_4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				color = CategoryPanel.COLOR_PURPLE;
			}
		});
		button_4.setBounds(52, 89, 50, 17);
		button_4.setText(International.Purple);
		button.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_MAGENTA));
		
		switch (color) {
		case CategoryPanel.COLOR_GREEN:
			button_2.setSelection(true);
			break;
		case CategoryPanel.COLOR_RED:
			hongseButton.setSelection(true);
			break;
		case CategoryPanel.COLOR_YELLOW:
			button_3.setSelection(true);
			break;
		case CategoryPanel.COLOR_PURPLE:
			button_4.setSelection(true);
			break;
		default:
			button.setSelection(true);
			break;
		}
		
		text.setText(source.name);

		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (text.getText().length() == 0) {
					SnowberryCore.showErrMsgBox(International.CategoryEditor, International.CategoryNameCannotBeNull);
					return;
				} else if (text.getText().equals(Category.UNCATEGORIED.name)) {
					SnowberryCore.showErrMsgBox(International.CategoryEditor, International.CannotUseThisName);
					return;
				}
				result = new Category(text.getText(), color);
				shell.dispose();
			}
		});
		shell.setDefaultButton(button_1);
		button_1.setText(International.Ok);
		button_1.setBounds(162, 140, 75, 27);

		final Button button_5 = new Button(shell, SWT.NONE);
		button_5.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		button_5.setText(International.Cancel);
		button_5.setBounds(243, 140, 75, 27);
		//
	}

}
