package gridShooter.game;

import java.awt.event.KeyEvent;

import gridShooter.Main;
import gridShooter.MainFrame;
import gridShooter.game.level.ImageCache;
import gridShooter.game.level.singleplayer.GameLevel;
import gridShooter.game.menu.LoadMenu;
import gridShooter.game.menu.MainMenu;
import gridShooter.game.progressIO.ProgressFile;
import de.abscanvas.DestkopScreen;

public class GameScreen extends DestkopScreen {
	private static final long serialVersionUID = 6871626788172466696L;
	
	public String f_letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ   " + "0123456789-.!?/%$\\=*+,;:()&#\"'";

	private GameKeys m_keys;
	
	private ProgressFile pFile = new ProgressFile();
	
	private ImageCache cache = new ImageCache();
	
	private UpdateConnector uConnector;
	private HighscoreConnector hConnector;
	
	public GameScreen(int width, int height, double scale, MainFrame owner) {
		super(width, height, new GameKeys(), scale, owner);
		m_keys = (GameKeys) getKeys();
		
		if (!pFile.load(Main.CONFIG_NAME)) {
			System.out.println("Could not load Config-File");
		}
		
		hConnector = new HighscoreConnector();
		hConnector.loadEmpty();
	}
	
	public void checkForUpdates() {
		uConnector = new UpdateConnector();
	}
	
	public void loadHighscore() {
		hConnector.loadHighscore();
	}
	
	public UpdateConnector getUpdateConnector() {
		return uConnector;
	}
	
	public void startLevel(int lvl, int difficulty) {
		clearMenus();
		setLevel(new GameLevel(this, lvl, difficulty, Main.LEVEL_WIDTH, Main.LEVEL_HEIGHT));
	}

	@Override
	public void onAfterTick() {
		//nothing
	}
	
	public ImageCache getCache() {
		return cache;
	}

	@Override
	public void onInit() {
		GameArt.loadMinimal();
		addToKeyMap(KeyEvent.VK_SPACE, m_keys.pause);
		addToKeyMap(KeyEvent.VK_ESCAPE, m_keys.pause);
		
		if (Main.MENUTEST) {
			loadOnlyMenuResources();
			checkForUpdates();
			loadHighscore();
			
			setMenu(new MainMenu(this));
		} else {
			setMenu(new LoadMenu(this));
		}
	}

	public void loadResources() {
		GameArt.loadMenu();
		GameArt.loadGame();
		GameArt.init();
		setFont(f_letters, GameArt.font);
	}
	
	public void loadOnlyMenuResources() {
		GameArt.loadMenu();
		setFont(f_letters, GameArt.font);
	}	

	@Override
	public void onStop() {
		if (! pFile.save()) {
			System.out.println("Could not Save Config");
		}
	}

	public ProgressFile getPFile() {
		return pFile;
	}

	public void scaleDown() {
		if (getScreenScale() > Main.MIN_GAME_SCALE) {
			setScreenScale(getScreenScale() - 0.05);
		}
	}

	public void scaleUp() {
		if (getScreenScale() < Main.MAX_GAME_SCALE) {
			setScreenScale(getScreenScale() + 0.05);
		}
	}

	public HighscoreConnector getHighscore() {
		return hConnector;
	}
}
