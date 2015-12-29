package gridShooter.game.menu;

import gridShooter.Main;
import gridShooter.game.GameArt;
import de.abscanvas.Screen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUICreditsScreen;
import de.abscanvas.ui.listener.ButtonListener;

public class CreditsMenu extends GUIMenu implements ButtonListener {
	private final static int BTN_BACK = -12;
	
	private GUIButton btnBack;
	private GUICreditsScreen cs_main;
	
	public CreditsMenu(Screen owner) {
		super(owner);
		create();
	}
	
	private void create() {
		setImage(GameArt.cover_small);

		btnBack = new GUIButton(32, getOwner().getScreenHeight() - 67, GameArt.btn_back, BTN_BACK, this);
		btnBack.addListener(this);
		addElement(btnBack);
		
		cs_main = new GUICreditsScreen(100, 230, getOwner().getScreenWidth() - 200, 700, this);
		
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("Crystal Grid");
		cs_main.addLines("");
		cs_main.addLines("version " + Main.VERSION);
		cs_main.addLines("");
		cs_main.addLines("");
		cs_main.addLines("created with:");
		cs_main.addLines("absCanvas v" + Screen.VERSION);
		cs_main.addLines("");
		cs_main.addLines("Programmed by:");
		cs_main.addLines("Mike Schwörer (Mikescher)");
		cs_main.addLines("");
		cs_main.addLines("Graphics by");
		cs_main.addLines("Mike Schwörer (Mikescher)");
		cs_main.addLines("");
		cs_main.addLines("Beta-Tester");
		cs_main.addLines("Benjamin Rottler");
		cs_main.addLines("Heiko Mild");
		cs_main.addLines("");
		cs_main.addLines("download officially @ http://www.mikescher.de");
		cs_main.addLines("");
		cs_main.addLines("Special Thanks to:");
		cs_main.addLines("Benjamin Rottler (mathematical help)");
		cs_main.addLines("IconArchive (great Website)");
		cs_main.addLines("Google.");
		cs_main.addLines("");
		cs_main.addLines("License:");
		cs_main.addLines("Do what ever you want (but it would be nice to say my name when you redistribute)");
		
		cs_main.setSpeed(0.5);
		cs_main.setBackgroundColor(AbsColor.TRANSPARENT);
		cs_main.setDrawBorder(false);
		
		addElement(cs_main);
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
	public void buttonPressed(MouseButtons mouse, int id) {
		if (id == BTN_BACK) {
			getOwner().popMenu();
		}
	}

}
