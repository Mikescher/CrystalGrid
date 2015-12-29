package gridShooter.game.level.multiplayer.client.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.level.multiplayer.NetworkGameConstants;
import gridShooter.game.level.multiplayer.client.ClientGameLevel;
import de.abscanvas.entity.network.ClientEntity;
import de.abscanvas.math.ByteUtilities;

public class Bullet extends ClientEntity {
	public Bullet() {
		getAnimation().setAnimation(GameArt.bullet);
		setRadius(8);
		setDebugging(Main.DEBUG);
	}
	
	@Override
	public void serverCommand_added(double x, double y, byte[] data) { // After Adding;
		super.serverCommand_added(x, y, data);
		((ClientGameLevel) super.getOwner()).getNetworkAdapter().send(NetworkGameConstants.CMD_REQBULLETINFO, ByteUtilities.long2Arr(getUID()));
	}

	public void setPlayer(int pid) {
		getAnimation().setAnimation(((ClientGameLevel) super.getOwner()).getCache().getBulletImage(pid));
	}
}
