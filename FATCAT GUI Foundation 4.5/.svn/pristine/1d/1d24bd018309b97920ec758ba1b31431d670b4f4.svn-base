package fatcat.gui.test;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fatcat.gui.GUIFramework;


public class SampleSwingApp extends JFrame {

	private static final long serialVersionUID = -5014294875263657921L;
	
	public static void main(String[] args) {
		
		SampleSwingApp frame = new SampleSwingApp();
		frame.setBounds(300, 300, 400, 400);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	SampleSwingApp() {
		
		final GUIFramework framework = new GUIFramework(getContentPane());
		SampleScene scene = new SampleScene(framework);
		scene.show();
		
		JPanel panel = framework.getAWTCanvas();
		panel.setLayout(null);
		
		JButton b = new JButton();
		panel.add(b);
		b.setBounds(10, 10, 30, 30);
		b.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (framework.isRunning()) {
					framework.stop();
					System.out.println("stop");
				} else {
					framework.restart();
					System.out.println("start");
				}
			}
		});
		
	}

}
