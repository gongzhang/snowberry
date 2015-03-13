package fatcat.snowberry.gui.test;

import fatcat.gui.snail.SnailShell;


public class SampleApplication extends javax.swing.JFrame {

	private static final long serialVersionUID = 3543397289170224010L;
	
	public static void main(String[] args) {
		SampleApplication sa = new SampleApplication();
		sa.setLocation(100, 100);
		sa.setSize(800, 600);
		sa.setTitle("Sample Application");
		sa.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		final SnailShell shell = new SnailShell(sa.getContentPane());
		shell.syncExec(new Runnable() {
			@Override
			public void run() {
				SampleFrame frame = new SampleFrame(shell);
				frame.show();
			}
		});
		
		sa.setVisible(true);
	}

}
