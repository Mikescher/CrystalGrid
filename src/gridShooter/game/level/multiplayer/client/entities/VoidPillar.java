package gridShooter.game.level.multiplayer.client.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.network.ClientEntity;

public class VoidPillar extends ClientEntity {
	public VoidPillar() {
		super();
		setRadius(32);
		setDebugging(Main.DEBUG);
		getAnimation().setAnimation(GameArt.pillar);
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// Do nothing
	}
}
