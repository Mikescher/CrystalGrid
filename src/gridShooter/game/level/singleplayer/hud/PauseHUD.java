package gridShooter.game.level.singleplayer.hud;

import gridShooter.game.GameArt;
import gridShooter.game.level.singleplayer.GameLevel;

import java.awt.Font;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.elements.HUDButton;
import de.abscanvas.ui.hud.elements.HUDLabel;
import de.abscanvas.ui.listener.ButtonListener;

public class PauseHUD extends HUD implements ButtonListener {
	private final static int BTN_RESUME = 101;
	private final static int BTN_RETRY = 102;
	private final static int BTN_EXIT = 103;
	
	private HUDButton btnResume;
	private HUDButton btnRetry;
	private HUDButton btnExit;
	
	private HUDLabel lblLevel;
	
	private GameLevel gOwner;
	
	public PauseHUD(GameLevel owner) {
		super(owner);
		
		gOwner = owner;

		create();
	}
	
	private void create() {
		setAlignment(ALIGN_CENTER);
		setImage(GameArt.hud_pMenu_Background);
		
		btnResume = new HUDButton(getOwner().getOwner().getScreenWidth()/2 - 183, 350, GameArt.hud_pMenu_resume, BTN_RESUME, this);
		btnRetry  = new HUDButton(getOwner().getOwner().getScreenWidth()/2 - 183, 500, GameArt.hud_pMenu_retry,  BTN_RETRY,  this);
		btnExit   = new HUDButton(getOwner().getOwner().getScreenWidth()/2 - 183, 650, GameArt.hud_pMenu_exit,   BTN_EXIT,   this);
		
		btnResume.addListener(this);
		btnRetry.addListener(this);
		btnExit.addListener(this);
		
		addElement(btnResume);
		addElement(btnRetry);
		addElement(btnExit);
		
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
		switch(id) {
		case BTN_RESUME:
			gOwner.resume();
			break;
		case BTN_RETRY:
			gOwner.restart();
			break;
		case BTN_EXIT:
			gOwner.exit();
			break;
		}
	}

}
