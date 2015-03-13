package fatcat.gui.snail;

import java.awt.Container;
import java.awt.Graphics2D;

import fatcat.gui.GUIFramework;
import fatcat.gui.GXConfiguration;

public final class SnailShell {
	
	private final GUIFramework fw;
	
	boolean check_thread_safety = true;
	
	public void enableThreadSafetyChecking() {
		check_thread_safety = true;
	}
	
	public void disableThreadSafetyChecking() {
		check_thread_safety = false;
	}
	
	public SnailShell(final Container host, int max_fps, GXConfiguration config) {
		fw = new GUIFramework(host, max_fps, config);
	}
	
	public SnailShell(final Container host, GXConfiguration config) {
		fw = new GUIFramework(host, config);
	}
	
	public SnailShell(final Container host, int max_fps) {
		fw = new GUIFramework(host, max_fps);
	}
	
	public SnailShell(final Container host) {
		this(host, 45);
	}
	
	public GUIFramework getFramework() {
		return fw;
	}
	
	public void syncExec(Runnable sb) {
		fw.syncExec(sb);
	}
	
	void show(Frame frame) {
		frame.setVisible(true);
		frame.setSize();
		frame.getScene().show();
	}
	
	@Deprecated
	public void dispose() {
		fw.stop();
	}
	
	public void restart() {
		fw.restart();
	}
	
	public void stop() {
		fw.stop();
	}
	
	public void setSuspendRepaint(boolean isSuspend) {
		fw.setSuspendRepaint(isSuspend);
	}
	
	public boolean isSuspendRepaint() {
		return fw.isSuspendRepaint();
	}
	
	public Graphics2D createGraphics() {
		return fw.createGraphics();
	}
	
}
