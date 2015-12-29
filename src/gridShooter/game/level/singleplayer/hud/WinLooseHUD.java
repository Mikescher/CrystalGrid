package gridShooter.game.level.singleplayer.hud;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.level.singleplayer.GameLevel;

import java.awt.Font;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.elements.HUDButton;
import de.abscanvas.ui.hud.elements.HUDLabel;
import de.abscanvas.ui.listener.ButtonListener;

public class WinLooseHUD extends HUD implements ButtonListener {
	private final static int BTN_RESTART = 0;
	private final static int BTN_EXIT = 1;
	private final static int BTN_NEXT = 2;

	private HUDButton btnNextRestart;
	private HUDButton btnExit;
	private HUDLabel lblLevel;
	
	private int winner;
	
	private GameLevel gOwner;

	public WinLooseHUD(GameLevel owner, int winner) {
		super(owner);
		this.gOwner = owner;

		this.winner = winner;

		create();
	}

	private void create() {
		setAlignment(ALIGN_CENTER);
		
		setImage(GameArt.disp_won[winner]);

		btnExit = new HUDButton(getOwner().getOwner().getScreenWidth() / 2 - 183 + 200, 820, GameArt.hud_w_exit, BTN_EXIT, this);
		btnExit.addListener(this);
		addElement(btnExit);
		
		if (winner == gOwner.getHumanPID() && gOwner.getLevel() != Main.MAX_RES_LVL) {
			btnNextRestart = new HUDButton(getOwner().getOwner().getScreenWidth() / 2 - 183 - 200, 820, GameArt.hud_w_next, BTN_NEXT, this);
			btnNextRestart.addListener(this);
		} else {
			btnNextRestart = new HUDButton(getOwner().getOwner().getScreenWidth() / 2 - 183 - 200, 820, GameArt.hud_w_retry, BTN_RESTART, this);
			btnNextRestart.addListener(this);
		}
		addElement(btnNextRestart);
		
		lblLevel = new HUDLabel(35, getOwner().getOwner().getScreenHeight() - 60, "Level: " + gOwner.getLevel() + " (" + gOwner.dToS().toUpperCase() + ")", this);
		lblLevel.setColor(AbsColor.WHITE);
		lblLevel.setFont(new Font("Arial", 0, 24));
		addElement(lblLevel);
	}

	@Override
	public void buttonMouseDown(MouseButtons arg0, int arg1) {
		// Do nothing
	}

	@Override
	public void buttonMouseEnter(MouseButtons arg0, int arg1) {
		// Do nothing
	}

	@Override
	public void buttonMouseLeave(MouseButtons arg0, int arg1) {
		// Do nothing
	}

	@Override
	public void buttonPressed(MouseButtons arg0, int id) {
		if (id == BTN_EXIT) {
			gOwner.exit();
		} else if (id == BTN_RESTART) {
			gOwner.restart();
		} else if (id == BTN_NEXT) {
			gOwner.next();
		}
	}

}
