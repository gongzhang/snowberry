package fatcat.snowberry.core.debug.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import fatcat.snowberry.core.SnowberryCore;

/**
 * Snowberry控制台视图。
 * <p>
 * 这个视图用来在Eclipse调试环境中输出调试信息。
 * </p>
 * @see SnowberryCore#println(Object)
 * @author 张弓
 */
public class ConsoleView extends ViewPart {
	
	private Text txtMain;
	private static ConsoleView instance = null;

	@Override
	public void createPartControl(Composite parent) {
		txtMain = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.LEFT | SWT.V_SCROLL | SWT.H_SCROLL);
		txtMain.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		instance = this;
	}

	@Override
	public void setFocus() {
		txtMain.setFocus();
	}
	
	/**
	 * 直接调用<code>SnowberryCore.println()</code>方法即可。
	 */
	public static void println(final String text) {
		if (instance != null && !instance.txtMain.isDisposed()) {
			instance.txtMain.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					if (instance != null && !instance.txtMain.isDisposed())
						instance.txtMain.append(text + "\n");
				}
			});
		}
	}
	
	@Override
	public void dispose() {
		instance = null;
		super.dispose();
	}

}
