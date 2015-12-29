package gridShooter.game.level.multiplayer.client.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.network.ClientEntity;

public class ReflectingPillar extends ClientEntity {

	public ReflectingPillar() {
		super();
		setRadius(32);
		setDebugging(Main.DEBUG);
		getAnimation().setAnimation(GameArt.pillar_reflect);
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// Do nothing
	}
}
