package gridShooter.game.menu;

import gridShooter.game.GameArt;
import gridShooter.game.GameScreen;
import gridShooter.game.HighscoreConnector.HighscoreEntry;
import gridShooter.game.UpdateConnector;

import java.awt.Font;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUIListBox;
import de.abscanvas.ui.gui.elements.GUINativeLabel;
import de.abscanvas.ui.gui.elements.GUINativeLinkLabel;
import de.abscanvas.ui.listener.ButtonListener;

public class HighscoreMenu extends GUIMenu implements ButtonListener {
	private final static String HIGHSCORE_URL = "http://www.mikescher.de/Highscores/list.php?gameid=6";
	
	private final static int BTN_BACK = -12;
	private final static int BTN_REFRESH = 10;
	private final static int BTN_RESET = 11;
	private final static int BTN_HSURL = 12;
	
	private GUIButton btnBack;
	public GUIListBox listBox1;
	private GUIButton btnRefresh;
	private GUINativeLabel lblName;
	private GUIButton btnReset;
	private GUINativeLinkLabel lblURL;
	
	private GameScreen owner;
	
	public HighscoreMenu(GameScreen owner) {
		super(owner);
		this.owner = owner;
		create();
	}
	
	private void create() {
		setImage(GameArt.cover_small);

		btnBack = new GUIButton(32, getOwner().getScreenHeight() - 67, GameArt.btn_back, BTN_BACK, this);
		btnBack.addListener(this);
		addElement(btnBack);
		
		listBox1 = new GUIListBox(154, 250, 1100, 640, this);
		listBox1.beginUpdate();
		for (HighscoreEntry entry : owner.getHighscore().get()) {
			listBox1.addLine(entry.score+"", entry.name);
		}
		listBox1.thickLines(true);
		listBox1.setFont(new Font("Arial", Font.BOLD, 26));
		listBox1.setScrollBarSize(20, 50);
		listBox1.setColorScheme(0, AbsColor.WHITE, AbsColor.LIGHT_GRAY, AbsColor.WHITE, AbsColor.WHITE);
		listBox1.endUpdate();
		
		addElement(listBox1);
		
		
		btnRefresh = new GUIButton(1264, 250, GameArt.btn_refresh, BTN_REFRESH, this);
		btnRefresh.addListener(this);
		addElement(btnRefresh);
		
		btnReset = new GUIButton(800, 900, GameArt.btn_reset, BTN_RESET, this);
		btnReset.addListener(this);
		addElement(btnReset);
		
		if (owner.getPFile().isOffline()) {
			lblName = new GUINativeLabel(152, 920, "YOU ARE OFFLINE", this);
		} else {
			lblName = new GUINativeLabel(152, 920, "" + owner.getPFile().getUsername() + ":" + owner.getPFile().getUserID(), this);
		}
		
		addElement(lblName);
		
		lblURL = new GUINativeLinkLabel(1100, 920, "OPEN", BTN_HSURL, this);
		lblURL.addListener(this);
		addElement(lblURL);
	}
	
	@Override
	public void tick() {
		super.tick();
		listBox1.setPos(new MPoint(154, 250));
		btnReset.setPos(new MPoint(800, 900));
		lblName.setPos(new MPoint(152, 920));
	}

	@Override
	public void buttonMouseDown(MouseButtons arg0, int arg1) {
		// nothing to do here
	}

	@Override
	public void buttonMouseEnter(MouseButtons arg0, int arg1) {
		// nothing to do here
	}

	@Override
	public void buttonMouseLeave(MouseButtons arg0, int arg1) {
		// nothing to do here
	}

	@Override
	public void buttonPressed(MouseButtons mb, int id) {
		if (id == BTN_BACK) {
			getOwner().popMenu();
		} else if (id == BTN_HSURL) {
			UpdateConnector.openInBrowser(HIGHSCORE_URL);
		} else if (id == BTN_REFRESH) {
			owner.loadHighscore();
			listBox1.beginUpdate();
			listBox1.clear();
			for (HighscoreEntry entry : owner.getHighscore().get()) {
				listBox1.addLine(entry.score+"", entry.name);
			}
			listBox1.endUpdate();
		} else if (id == BTN_RESET) {
			getOwner().popMenu();
			owner.addMenu(new SetNameMenu(owner, false));
		}
	}

}
