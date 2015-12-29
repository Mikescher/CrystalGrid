package gridShooter.game.level.multiplayer.server;

import gridShooter.game.level.ImageCache;
import de.abscanvas.ConsoleScreen;
import de.abscanvas.additional.swinginterface.StandardServerWindow;

public class ServerGameScreen extends ConsoleScreen {

	private final int startLvl;
	private final int startDifficulty;
	private final ImageCache startCache;
	
	private StandardServerWindow owner;
	
	public ServerGameScreen(StandardServerWindow owner, int level, int difficulty, ImageCache c) {
		startLvl = level;
		startDifficulty = difficulty;
		startCache = c;
		this.owner = owner;
	}

	@Override
	public void onAfterTick() {
		//nothing
	}

	@Override
	public void onInit() {
		setLevel(new ServerGameLevel(this, startLvl, startDifficulty, startCache));
	}

	@Override
	public void onStop() {
		//nothing
	}

	@Override
	public StandardServerWindow getOwner() {
		return owner;
	}
}
