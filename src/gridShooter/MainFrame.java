package gridShooter;

import gridShooter.game.GameScreen;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.abscanvas.DestkopScreen;

public class MainFrame extends JFrame{
	private static final long serialVersionUID = 3769071348694961338L;
	
	private DestkopScreen screen;
	
	public MainFrame() {
		initGUI();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				screen.stop();
				System.exit(0);
			}
		});
	}

	private void initGUI() {
		JPanel panel = new JPanel(new BorderLayout());
		
		screen = new GameScreen(Main.GAME_WIDTH, Main.GAME_HEIGHT, Main.GAME_SCALE, this);
		panel.add(screen, BorderLayout.CENTER);
		setContentPane(panel);
		setResizable(false);
		pack();
		
		setTitle(Main.GAME_TITLE);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		try {
			setIconImage(ImageIO.read(this.getClass().getResource("/icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		setVisible(true);

		screen.start();
	}
}
