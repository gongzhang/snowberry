package fatcat.snowberry.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import fatcat.snowberry.core.debug.views.ConsoleView;
import fatcat.snowberry.tag.TagCore;

/**
 * Snowberry插件核心类。
 * <p>
 * Snowberry是本项目的开发代号。
 * </p>
 * @author 张弓
 */
final public class SnowberryCore extends AbstractUIPlugin {

	/**
	 * Eclipse插件扩展ID。
	 */
	public static final String PLUGIN_ID = "fatcat.snowberry"; //$NON-NLS-1$
	
	private static SnowberryCore plugin; // Singleton

	/**
	 * @deprecated 参见{@link #log(int, String, Throwable)}
	 * 输出调试信息。
	 * <p>
	 * 不仅会在开发环境中输出，还会在调试环境中输出到“Snowberry控制台”视图。
	 * </p>
	 * @param obj 要输出的信息
	 */
	@Deprecated
	public static void println(Object obj) {
		
		// 开发环境输出
		System.out.println("[Snowberry]" + obj); //$NON-NLS-1$
		
		// 调试环境输出
		ConsoleView.println(obj == null ? "null" : obj.toString()); //$NON-NLS-1$
	}
	
	/**
	 * 弹出消息框。
	 * @param title 消息框标题
	 * @param content 消息框内容
	 * @param swt_style SWT样式
	 */
	public static int showMsgBox(String title, String content, int swt_style) {
		Shell shell = getDefaultShell();
		
		// 装配ICON
		if ((swt_style & SWT.ICON_ERROR) == 0 &&
			(swt_style & SWT.ICON_INFORMATION) == 0 &&
			(swt_style & SWT.ICON_QUESTION) == 0 &&
			(swt_style & SWT.ICON_WARNING) == 0 &&
			(swt_style & SWT.ICON_WORKING) == 0) {
			if ((swt_style & SWT.YES) != 0 && (swt_style & SWT.NO) != 0) swt_style |= SWT.ICON_QUESTION;
			if ((swt_style & SWT.OK) != 0) swt_style |= SWT.ICON_INFORMATION;
		}
		
		MessageBox msg = new MessageBox(shell, swt_style);
		msg.setText(title);
		msg.setMessage(content);
		int rst = msg.open();
		return rst;
	}
	
	/**
	 * 弹出消息框。
	 * @param title 消息框标题
	 * @param content 消息框内容
	 */
	public static void showMsgBox(String title, String content) {
		showMsgBox(title, content, SWT.ICON_INFORMATION | SWT.OK);
	}
	
	/**
	 * 弹出错误消息框。
	 * @param title 消息框标题
	 * @param content 消息框内容
	 */
	public static void showErrMsgBox(String title, String content) {
		showMsgBox(title, content, SWT.ICON_ERROR | SWT.OK);
	}

	/**
	 * 返回Snowberry插件实例，若未加载返回<code>null</code>。
	 *
	 * @return 插件实例或{@code null}
	 */
	public static SnowberryCore getPlugin() {
		return plugin;
	}
	
	public static SnowberryCore getDefault() {
		return getPlugin();
	}

	/**
	 * 插件生命周期开始。
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// Notice that I have implement the early startup extension point.
		// see class SnowberryLauncher.
		
//		// 启动部件
//		Job job = new Job("启动Snowberry插件") {
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				TagCore.start();
//				monitor.done();  
//	            return Status.OK_STATUS;  
//			}
//		};
//		job.schedule();
		
//		SnowberryCore.println("Started."); //$NON-NLS-1$
	}

	/**
	 * 插件生命周期终止。
	 */
	public void stop(BundleContext context) throws Exception {
		
		// 终止部件
		Job job = new Job(International.SnowberryTerminator) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				TagCore.stop();
				monitor.done();  
	            return Status.OK_STATUS;  
			}
		};
		job.schedule();
		
		plugin = null;
		super.stop(context);
//		SnowberryCore.println("Terminated."); //$NON-NLS-1$
	}
	
	public static Shell getDefaultShell() {
		Shell shell = null;
		try {
			shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		} catch (Exception e) {
			SnowberryCore.log(Status.ERROR, e.getMessage(), e);
//			SnowberryCore.println(e.getMessage());
		}
		return shell;
	}
	
	/**
	 * @param severity OK, ERROR, INFO, WARNING, or CANCEL
	 */
	public static void log(int severity, String message, Throwable throwable) {
		Status status = new Status(severity, PLUGIN_ID, message, throwable);
		getDefault().getLog().log(status);
	}
	
	/**
	 * @param severity OK, ERROR, INFO, WARNING, or CANCEL
	 */
	public static void log(int severity, String message) {
		log(severity, message, null);
	}

}
