package gridShooter.game.level.player;

import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;
import gridShooter.game.level.singleplayer.entities.Bullet;

import java.util.ArrayList;

import de.abscanvas.entity.LevelEntity;

public class NeutralPlayer extends Player{
	public NeutralPlayer(AbsLevel owner, int color) {
		super(owner, -1, color);
	}

	@Override
	public boolean isNeutral() {
		return true;
	}

	@Override
	public void calculateMovement(AbsCannon c) {
		ArrayList<Bullet> dangerBulls = new ArrayList<Bullet>();
		
		ArrayList<LevelEntity> lst = getOwner().getAllEntities(Bullet.class);
		
		for(LevelEntity e : lst) {
			Bullet b = (Bullet)e;
			
			if (b.getPlayer() != getOwner().getNeutralPlayer()) {
				if (b.getPossibleTarget() == c) {
					dangerBulls.add(b);
				}
			}
		}
		
		if (dangerBulls.isEmpty()) {
			c.resetState();
		} else {
			Bullet mostDanger = null;
			double maxDistance = Integer.MAX_VALUE;
			for (Bullet b : dangerBulls) {
				double d = b.getPosition().getDistance(c.getPosition());
				if (d < maxDistance) {
					maxDistance = d;
					mostDanger = b;
				}
			}
			c.setTarget(mostDanger.getPosition());
		}
	}

	@Override
	public double getMultiplikator() {
		return 0.5;
	}
}
