package gridShooter.game.menu;

import gridShooter.game.GameArt;
import gridShooter.game.GameScreen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.listener.ButtonListener;

public class DifficultySelectorMenu extends GUIMenu implements ButtonListener{
	private final static int BTN_EASY = 0;
	private final static int BTN_MIDDLE = 1;
	private final static int BTN_HARD = 2;
	private final static int BTN_IMPOSSIBLE = 3;
	private final static int BTN_WARFARE = 4;
	private final static int BTN_BACK = 101;
	
	private GameScreen owner;
	
	private GUIButton buttons[] = new GUIButton[5];
	
	private GUIButton btnBack;
	
	private int lvl;
	
	public DifficultySelectorMenu(GameScreen owner, int level) {
		super(owner);
		this.owner = owner;
		this.lvl = level;
		
		create();
	}

	private void create() {
		setImage(GameArt.cover_small);
		
		buttons[0] = new GUIButton(owner.getScreenWidth()/2 - 232, 250, GameArt.btn_diff_0, BTN_EASY, 		this);
		buttons[1] = new GUIButton(owner.getScreenWidth()/2 - 232, 400, GameArt.btn_diff_1, BTN_MIDDLE, 	this);
		buttons[2] = new GUIButton(owner.getScreenWidth()/2 - 232, 550, GameArt.btn_diff_2, BTN_HARD, 		this);
		buttons[3] = new GUIButton(owner.getScreenWidth()/2 - 232, 700, GameArt.btn_diff_3, BTN_IMPOSSIBLE, this);
		buttons[4] = new GUIButton(owner.getScreenWidth()/2 - 232, 850, GameArt.btn_diff_4, BTN_WARFARE,    this);
		
		buttons[0].addListener(this);
		buttons[1].addListener(this);
		buttons[2].addListener(this);
		buttons[3].addListener(this);
		buttons[4].addListener(this);
		
		addElement(buttons[0]);
		addElement(buttons[1]);
		addElement(buttons[2]);
		addElement(buttons[3]);
		addElement(buttons[4]);
		
		btnBack = new GUIButton(32, getOwner().getScreenHeight() - 67, GameArt.btn_back, BTN_BACK, this);
		btnBack.addListener(this);
		addElement(btnBack);
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
	public void buttonPressed(MouseButtons e, int id) {
		if (id == BTN_BACK) {
			owner.popMenu();
		} else {
			owner.startLevel(lvl, id);
		}
	}
}
