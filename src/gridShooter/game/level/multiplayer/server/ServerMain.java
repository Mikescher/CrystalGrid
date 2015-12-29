package gridShooter.game.level.multiplayer.server;

import gridShooter.game.GameArt;

public class ServerMain {
	public static void main(String[] args) {
		GameArt.loadMenu();
		GameArt.loadGame();
		GameArt.init();
		new ServerWindow("Crystal Grid - Server");		
	}
}
