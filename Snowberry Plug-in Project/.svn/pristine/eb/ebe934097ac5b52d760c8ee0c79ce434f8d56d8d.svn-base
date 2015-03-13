package fatcat.snowberry.dialogs;

import java.awt.Frame;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fatcat.gui.GUIFramework;
import fatcat.snowberry.core.International;

public class AboutDialog extends Dialog {

	private Text buaazggmailcomText_1;
	private Text buaazggmailcomText;
	protected Object result;
	protected Shell shell;

	private Image IMG_ICON;
	private GUIFramework framework;

	/**
	 * Create the dialog
	 * 
	 * @param parent
	 * @param style
	 */
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * 
	 * @param parent
	 */
	public AboutDialog(Shell parent) {
		this(parent, SWT.NONE);
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
		framework.stop();
		IMG_ICON.dispose();
		return result;
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(501, 512);
		shell.setText(International.AboutSnowberryAndUserFeedback);
		final Font bold = new Font(shell.getDisplay(), "宋体", 10, SWT.BOLD); //$NON-NLS-1$
		InputStream is = getClass().getResourceAsStream("/icons/snowberry_icon.png"); //$NON-NLS-1$
		IMG_ICON = new org.eclipse.swt.graphics.Image(shell.getDisplay(), is);
		shell.setImage(IMG_ICON);

		final Composite imgLogo = new Composite(shell, SWT.EMBEDDED);
		imgLogo.setBounds(0, 0, 159, 174);
		final Frame frame = SWT_AWT.new_Frame(imgLogo);
		framework = new GUIFramework(frame, 20);

		

		final Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		shell.setDefaultButton(button);
		button.setText(International.Close);
		button.setBounds(377, 445, 108, 27);
		
		final Composite composite = new Composite(shell, SWT.BORDER);
		composite.setBounds(159, 0, 336, 174);

		final Label snowberryLabel = new Label(composite, SWT.NONE);
		snowberryLabel.setText(International.SnowberryForJavaDevelopers);
		snowberryLabel.setBounds(10, 10, 182, 17);

		final Label label = new Label(composite, SWT.NONE);
		label.setText(International.Version);
		label.setBounds(10, 33, 99, 17);

		final Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText(International.ContactUs);
		label_1.setBounds(10, 56, 60, 17);

		buaazggmailcomText = new Text(composite, SWT.READ_ONLY);
		buaazggmailcomText.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				buaazggmailcomText.setSelection(0, buaazggmailcomText.getText().length());
			}
		});
		buaazggmailcomText.setText("buaazg@gmail.com"); //$NON-NLS-1$
		buaazggmailcomText.setBounds(76, 56, 246, 17);

		final Label label_1_1 = new Label(composite, SWT.NONE);
		label_1_1.setBounds(10, 79, 60, 17);
		label_1_1.setText(International.TeamBlog);

		buaazggmailcomText_1 = new Text(composite, SWT.READ_ONLY);
		buaazggmailcomText_1.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				buaazggmailcomText_1.setSelection(0, buaazggmailcomText_1.getText().length());
			}
		});
		buaazggmailcomText_1.setBounds(76, 79, 246, 17);
		buaazggmailcomText_1.setText("http://www.cnblogs.com/fatcat"); //$NON-NLS-1$

		final Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("2010-04-16 FATCAT团队"); //$NON-NLS-1$
		label_2.setBounds(10, 143, 138, 17);

		final Label zheLabel = new Label(shell, SWT.WRAP);
		zheLabel.setBounds(38, 219,418, 38);
		zheLabel.setText("- 此版本仅供测试使用，其中有些测试性的功能可能会导致数据错误和丢失，请在使用此插件前对您的Workspace中的代码进行手工备份。"); //$NON-NLS-1$

		final Label label_3 = new Label(shell, SWT.WRAP);
		label_3.setBounds(38, 286,429, 17);
		label_3.setText("- 请登录我们的团队博客提供反馈信息，以促进我们为您提供更好的插件与服务。"); //$NON-NLS-1$

		final Label lbl1 = new Label(shell, SWT.NONE);
		lbl1.setText(International.TestAnouncement);
		lbl1.setBounds(21, 196, 167, 17);
		lbl1.setFont(bold);

		final Label lbl2 = new Label(shell, SWT.NONE);
		lbl2.setBounds(21, 263, 167, 17);
		lbl2.setText(International.UserFeedback);
		lbl2.setFont(bold);
		
		final Label lbl3 = new Label(shell, SWT.NONE);
		lbl3.setBounds(21, 311,167, 17);
		lbl3.setText(International.AboutFatcat);
		lbl3.setFont(bold);

		final Label lbl4 = new Label(shell, SWT.NONE);
		lbl4.setBounds(21, 378, 167, 17);
		lbl4.setText(International.Donation);
		lbl4.setFont(bold);

		final Label label_3_1 = new Label(shell, SWT.WRAP);
		label_3_1.setBounds(38, 334, 418, 38);
		label_3_1.setText("- FATCAT开发团队组建于2008年，我们均是来自北京航空航天大学的本科生，关注Eclipse技术、移动开发和UI设计，欢迎与我们取得联系。"); //$NON-NLS-1$

		final Label label_3_1_1 = new Label(shell, SWT.WRAP);
		label_3_1_1.setBounds(38, 401, 418, 38);
		label_3_1_1.setText("- Snowberry是开源软件，如果您觉得她给您带来了帮助，并且您愿意提供一些资金支持的话，请以电子邮件的方式联系我们。衷心感谢！"); //$NON-NLS-1$
		
		AboutScene scene = new AboutScene(framework);
		scene.show();
		//
	}

}
