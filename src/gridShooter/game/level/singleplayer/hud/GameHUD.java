package gridShooter.game.level.singleplayer.hud;

import java.awt.Font;

import gridShooter.game.GameArt;
import gridShooter.game.level.singleplayer.GameLevel;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.elements.HUDCheckbox;
import de.abscanvas.ui.hud.elements.HUDLabel;
import de.abscanvas.ui.listener.CheckBoxChangeListener;

public class GameHUD extends HUD implements CheckBoxChangeListener {
	private final static int CB_SPEED_X1 = 16;
	private final static int CB_SPEED_X2 = 32;
	private final static int CB_SPEED_X4 = 64;
	
	private HUDCheckbox cbSpeedX1;
	private HUDCheckbox cbSpeedX2;
	private HUDCheckbox cbSpeedX4;
	private HUDLabel lblLevel;
	
	private GameLevel gOwner;
	
	public GameHUD(GameLevel owner) {
		super(owner);
		
		gOwner = owner;

		create();
	}

	private void create() {
		cbSpeedX1 = new HUDCheckbox(28, 0, GameArt.hud_cb_x1, CB_SPEED_X1, this);
		cbSpeedX2 = new HUDCheckbox(92, 0, GameArt.hud_cb_x2, CB_SPEED_X2, this);
		cbSpeedX4 = new HUDCheckbox(156, 0, GameArt.hud_cb_x4, CB_SPEED_X4, this);
		
		cbSpeedX1.addChangeListener(this);
		cbSpeedX2.addChangeListener(this);
		cbSpeedX4.addChangeListener(this);
		
		addElement(cbSpeedX1);
		addElement(cbSpeedX2);
		addElement(cbSpeedX4);
		
		cbSpeedX1.setChecked(true);
		
		lblLevel = new HUDLabel(35, getOwner().getOwner().getScreenHeight() - 60, "Level: " + gOwner.getLevel() + " (" + gOwner.dToS().toUpperCase() + ")", this);
		lblLevel.setColor(AbsColor.WHITE);
		lblLevel.setFont(new Font("Arial", 0, 24));
		addElement(lblLevel);
	}


	@Override
	public void checkBoxChanged(MouseButtons sender, int id, boolean state) {
		if (sender != null) {
			if (state) {
				switch(id) {
				case CB_SPEED_X1:
					gOwner.setGameSpeed(1);
					cbSpeedX2.setChecked(false);
					cbSpeedX4.setChecked(false);
					break;
				case CB_SPEED_X2:
					gOwner.setGameSpeed(2);
					cbSpeedX1.setChecked(false);
					cbSpeedX4.setChecked(false);
					break;
				case CB_SPEED_X4:
					gOwner.setGameSpeed(4);
					cbSpeedX1.setChecked(false);
					cbSpeedX2.setChecked(false);
					break;
				}
			} else {
				switch(id) {
				case CB_SPEED_X1:
					cbSpeedX1.setChecked(true);
					break;
				case CB_SPEED_X2:
					cbSpeedX2.setChecked(true);
					break;
				case CB_SPEED_X4:
					cbSpeedX4.setChecked(true);
					break;
				}
			}
		}
	}
}
