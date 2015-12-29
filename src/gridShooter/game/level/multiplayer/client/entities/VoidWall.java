package gridShooter.game.level.multiplayer.client.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.level.multiplayer.NetworkGameConstants;
import gridShooter.game.level.multiplayer.client.ClientGameLevel;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.network.ClientEntity;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.surface.Bitmap;

public class VoidWall extends ClientEntity {
	public VoidWall() {
		super();
		setDebugging(Main.DEBUG);
		createImage(1, true);
	}

	private void createImage(int width, boolean horizontal) {
		if (horizontal) {
			Bitmap b = new Bitmap(width, 8);
			b.blit(GameArt.wall[0], 0, 0);
			getAnimation().setAnimation(b);
			setRadius(width / 2, 4);
		} else {
			Bitmap b = new Bitmap(8, width);
			b.blit(GameArt.wall[1], 0, 0);
			getAnimation().setAnimation(b);
			setRadius(4, width / 2);
		}
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// Do nothing
	}
	
	@Override
	public void serverCommand_added(double x, double y, byte[] data) { // After Adding;
		super.serverCommand_added(x, y, data);
		((ClientGameLevel) super.getOwner()).getNetworkAdapter().send(NetworkGameConstants.CMD_REQVWALLINFO, ByteUtilities.long2Arr(getUID()));
	}

	public void setInfo(int wid, boolean horz) {
		createImage(wid, horz);
	}
}
