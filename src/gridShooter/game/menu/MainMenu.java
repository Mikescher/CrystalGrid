package gridShooter.game.menu;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.GameScreen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MPoint;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.listener.ButtonListener;

public class MainMenu extends GUIMenu implements ButtonListener{
	private final static int BTN_PLAY       = 1;
	private final static int BTN_LAN        = 2;
	private final static int BTN_CREDITS    = 3;
	private final static int BTN_EXIT       = 4;
	private final static int BTN_PLUS       = 5;
	private final static int BTN_MINUS      = 6;
	private final static int BTN_UPDATE     = 7;
	private final static int BTN_HIGHSCORE  = 8;
	
	private GUIButton btnPlay;
	private GUIButton btnLan;
	private GUIButton btnCredits;
	private GUIButton btnHighscore;
	private GUIButton btnExit;
	
	private GUIButton btnPlus;
	private GUIButton btnMinus;
	
	private GUIButton btnUpdateAvaible;
	
	private GameScreen owner;
	
	public MainMenu(GameScreen owner) {
		super(owner);
		this.owner = owner;
		create();
		updatePlusMinus();
	}

	public void create() {
		setImage(GameArt.cover_small);
		
		btnPlay = new GUIButton(getOwner().getScreenWidth()/2 - 232, 250, GameArt.btn_play, BTN_PLAY, this);
		btnPlay.addListener(this);
		addElement(btnPlay);
		
		btnLan = new GUIButton(getOwner().getScreenWidth()/2 - 232, 400, GameArt.btn_lan, BTN_LAN, this);
		btnLan.addListener(this);
		addElement(btnLan);
		
		btnCredits = new GUIButton(getOwner().getScreenWidth()/2 - 232, 550, GameArt.btn_credits, BTN_CREDITS, this);
		btnCredits.addListener(this);
		addElement(btnCredits);
		
		btnHighscore  = new GUIButton(getOwner().getScreenWidth()/2 - 232, 700, GameArt.btn_highscore, BTN_HIGHSCORE, this);
		btnHighscore.addListener(this);
		addElement(btnHighscore);
		
		btnExit = new GUIButton(getOwner().getScreenWidth()/2 - 232, 850, GameArt.btn_exit, BTN_EXIT, this);
		btnExit.addListener(this);
		addElement(btnExit);
		
		btnMinus = new GUIButton(30, 35, GameArt.btn_minus, BTN_MINUS, this);
		btnMinus.addListener(this);
		addElement(btnMinus);
		
		btnPlus = new GUIButton(102, 35, GameArt.btn_plus, BTN_PLUS, this);
		btnPlus.addListener(this);
		addElement(btnPlus);
		
		btnUpdateAvaible = new GUIButton(getOwner().getScreenWidth()/2 + 232, 900, GameArt.btn_updateAvailable, BTN_UPDATE, this);
		btnUpdateAvaible.addListener(this);
		if (owner.getUpdateConnector().updateAvaiable()) {
			addElement(btnUpdateAvaible);
		}
		
		btnLan.setEnabled(false);
	}
	
	@Override
	public void tick() {
		super.tick();
		btnUpdateAvaible.setPos(new MPoint(getOwner().getScreenWidth()/2 + 303, 870));
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
		if (id == BTN_PLAY) {
			getOwner().addMenu(new LevelSelectorMenu(owner, 0));
		} else if (id == BTN_EXIT) {
			getOwner().stop();
			System.exit(0);
		} else if (id == BTN_CREDITS) {
			getOwner().addMenu(new CreditsMenu(getOwner()));
		} else if (id == BTN_LAN) {
			getOwner().addMenu(new LanMenu(getOwner()));
		} else if (id == BTN_MINUS) {
			owner.scaleDown();
			updatePlusMinus();
		} else if (id == BTN_PLUS) {
			owner.scaleUp();
			updatePlusMinus();
		} else if (id == BTN_UPDATE) {
			owner.getUpdateConnector().openURL();
		} else if (id == BTN_HIGHSCORE) {
			getOwner().addMenu(new HighscoreMenu(owner));
		}
	}
	
	private void updatePlusMinus() {
		btnMinus.setEnabled(getOwner().getScreenScale() >= Main.MIN_GAME_SCALE);
		btnPlus.setEnabled(getOwner().getScreenScale() <= Main.MAX_GAME_SCALE);
	}
}
