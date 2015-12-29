package gridShooter.game.level.multiplayer.server.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.network.ServerEntity;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkUser;

public class VoidPillar extends ServerEntity {
	public VoidPillar() {
		super();
		setRadius(32);
		setDebugging(Main.DEBUG);
		getAnimation().setAnimation(GameArt.pillar);
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		if (entity instanceof Bullet && getPosition().getDistance(entity.getPosition()) < (getRadius().getX() + entity.getRadius().getX())) {
			entity.remove();
		}
	}

	@Override
	public boolean isControllableByUser(NetworkUser arg0) {
		return false;
	}

	@Override
	public void onAfterClientControlMove(MDPoint arg0) {	
		// Do nothing
	}
}
