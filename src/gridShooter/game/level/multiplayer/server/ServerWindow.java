package gridShooter.game.level.multiplayer.server;

import gridShooter.game.level.ImageCache;
import gridShooter.game.level.abstractLevel.AbsLevel;
import de.abscanvas.additional.swinginterface.StandardServerWindow;
import de.abscanvas.network.NetworkUser;

public class ServerWindow extends StandardServerWindow {
	private static final long serialVersionUID = 5958808652513890177L;
	
	public ServerWindow(String title) {
		super(title);
		setExecuteButtonText(1, "Pause Game");
		setExecuteButtonText(2, "Start Game");
		setExecuteButtonText(3, "Reset Game");
		
		setExecuteButtonText(4, "Speed x2");
		setExecuteButtonText(5, "Prev Level");
		setExecuteButtonText(6, "Next Level");
		
		setUserCommandButtonText(1, "kick");
		
		setConsoleScreen(new ServerGameScreen(this, 10, 0, new ImageCache()));
		getScreen().start();
	}

	@Override
	public void onExecuteButton1Clicked() {
		getAbsLevel().setGameSpeed(0);
	}

	@Override
	public void onExecuteButton2Clicked() {
		getAbsLevel().setGameSpeed(1);
	}

	@Override
	public void onExecuteButton3Clicked() {
		getAbsLevel().restart();
	}

	@Override
	public void onExecuteButton4Clicked() {
		getAbsLevel().setGameSpeed(2);
	}

	@Override
	public void onExecuteButton5Clicked() {
		getAbsLevel().prev();
	}

	@Override
	public void onExecuteButton6Clicked() {
		getAbsLevel().next();
	}

	@Override
	public void onInputFieldExecuteClicked() {
		// empty
	}

	@Override
	public void onUserCommandButton1Clicked(NetworkUser arg0) {
		// empty

	}

	@Override
	public void onUserCommandButton2Clicked(NetworkUser arg0) {
		// empty
	}

	@Override
	public void onUserCommandButton3Clicked(NetworkUser arg0) {
		// empty
	}

	@Override
	public void onUserCommandButton4Clicked(NetworkUser arg0) {
		// empty
	}

	public AbsLevel getAbsLevel() {
		return (AbsLevel) getScreen().getLevel();
	}
}
