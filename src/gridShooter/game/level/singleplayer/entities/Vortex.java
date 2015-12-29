package gridShooter.game.level.singleplayer.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.LevelEntity;

public class Vortex extends LevelEntity {
	public final static int VORTEX_CONSTANT = 850;
	
	public Vortex() {
		super();
		setRadius(8);
		setDebugging(Main.DEBUG);
		getAnimation().setAnimation(GameArt.blackhole);
	}
	
	@Override
	public void tick() {
		super.tick();
		for (LevelEntity e : getOwner().getAllEntities(Bullet.class)) {
			((Bullet)e).calcVortexForce(this);
		}
		
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		if (entity instanceof Bullet && getPosition().getDistance(entity.getPosition()) < (getRadius().getX() + entity.getRadius().getX())) {
			entity.remove();
		}
	}

}
