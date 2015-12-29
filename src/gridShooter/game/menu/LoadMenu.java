package gridShooter.game.menu;

import java.awt.Font;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.GameScreen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.ui.listener.ButtonListener;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUILabel;

public class LoadMenu extends GUIMenu implements ButtonListener, Runnable{
	private final static int BTN_START = 1;
	private final static int BTN_STARTING = 2;
	
	private GUIButton btnStart;
	private GUIButton btnStarting;
	private GUILabel lblVersion;
	
	private GameScreen owner;
	
	public LoadMenu(GameScreen owner) {
		super(owner);
		this.owner = owner;
		create();
	}

	private void create() {
		setImage(GameArt.cover);
		
		btnStart = new GUIButton(getOwner().getScreenWidth()/2 - 232, 800, GameArt.btn_start, BTN_START, this);
		addElement(btnStart);
		btnStart.setVisible(true);
		btnStart.addListener(this);
		
		btnStarting = new GUIButton(getOwner().getScreenWidth()/2 - 232, 800, GameArt.btn_starting, BTN_STARTING, this);
		addElement(btnStarting);
		btnStarting.setVisible(false);
		btnStarting.addListener(this);
		
		lblVersion = new GUILabel(getOwner().getScreenWidth() - 60, getOwner().getScreenHeight() - 30, "v" + Main.VERSION, this);
		lblVersion.setColor(AbsColor.WHITE);
		lblVersion.setFont(new Font("Arial", 0, 24));
		addElement(lblVersion);
	}

	@Override
	public void buttonMouseDown(MouseButtons arg0, int arg1) {
		// nothing
	}

	@Override
	public void buttonMouseEnter(MouseButtons arg0, int arg1) {	
		// nothing
	}

	@Override
	public void buttonMouseLeave(MouseButtons arg0, int arg1) {
		// nothing
	}

	@Override
	public void buttonPressed(MouseButtons arg0, int id) {
		if (id == BTN_START) {
			btnStart.setVisible(false);
			btnStarting.setVisible(true);
			(new Thread(this)).start();
		}
	}

	@Override
	public void run() {
		owner.loadResources();
		
		owner.checkForUpdates();
		owner.loadHighscore();
		
		owner.addMenu(new MainMenu(owner));
		if (! owner.getPFile().nameIsSet()) {
			owner.addMenu(new SetNameMenu(owner, true));
		}
	}
}
