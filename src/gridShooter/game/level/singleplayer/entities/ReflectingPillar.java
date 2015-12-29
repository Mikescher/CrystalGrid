package gridShooter.game.level.singleplayer.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.LevelEntity;

public class ReflectingPillar extends LevelEntity {

	public ReflectingPillar() {
		super();
		setRadius(32);
		setDebugging(Main.DEBUG);
		getAnimation().setAnimation(GameArt.pillar_reflect);
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		if (entity instanceof Bullet && getPosition().getDistance(entity.getPosition()) < (getRadius().getX() + entity.getRadius().getX())) {
			((Bullet)entity).collideWidth(this);
		}
	}
}
