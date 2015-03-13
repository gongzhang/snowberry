package fatcat.gui.snail.test;

import javax.swing.JFrame;

import fatcat.gui.snail.SnailShell;


public class SampleSwingApp extends JFrame {

	private static final long serialVersionUID = -5014294875263657921L;
	
	public static void main(String[] args) {
		
		SampleSwingApp frame = new SampleSwingApp();
		frame.setBounds(300, 300, 400, 400);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		SnailShell snailShell = new SnailShell(frame.getContentPane());
		SampleFrame f = new SampleFrame(snailShell);
		f.show();
		
	}

}
