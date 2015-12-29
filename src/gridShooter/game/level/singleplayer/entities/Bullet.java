package gridShooter.game.level.singleplayer.entities;

import java.util.List;
import java.util.Random;

import gridShooter.Main;
import gridShooter.game.level.player.Player;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MDRect;
import de.abscanvas.math.MPoint;

public class Bullet extends LevelEntity {
	private final static double SPEED = 1.66;
	private final static int START_STASIS = 16; // n Ticks no collision
	private final static int BULL_COLL_IGNORE_TIME = 8;
	private final static int MAX_LIFETIME = 66*25; // ca 25s
	private final static int ANOMALY = 2;

	private double lifetime = 0;
	private Player owner;
	private Cannon shooter;
	private MDPoint velocity;

	private Bullet collisionIgnore = null;
	private int collisionIgnoret = 0;
	
	private static Random randFactory = new Random();

	public Bullet(Cannon shooter) {
		this.owner = shooter.getPlayer();
		this.shooter = shooter;
		
		velocity = new MDPoint(-32, 0).rotate(Math.toRadians(shooter.getDirection() + (randFactory.nextInt(ANOMALY*2+1) - ANOMALY)), 0, 0, false);
		velocity.setLength(SPEED * owner.getOwner().getBulletSpeedMultiplikator());

		setPos(shooter.getBulletPos());
		setRadius(8);
		setDebugging(Main.DEBUG);
		getAnimation().setAnimation(shooter.getAbsOwner().getCache().getBulletImage(owner));
	}
	
	public double getGameSpeed() {
		return shooter.getGameSpeed();
	}

	@Override
	public void tick() {
		super.tick();
		lifetime += getGameSpeed();

		addPos(velocity.getX() * getGameSpeed(), velocity.getY() * getGameSpeed());

		if ((getPosX() < -100) || (getPosY() < -100) || (getPosX() > getOwner().getWidth() * getOwner().getTileWidth()) || (getPosY() > getOwner().getHeight() * getOwner().getTileHeight()) || lifetime > MAX_LIFETIME) {
			remove();
		}

		if (collisionIgnoret > 0) {
			collisionIgnoret -= getGameSpeed();
			if (collisionIgnoret <= 0) {
				collisionIgnore = null;
			}
		}
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		if (entity instanceof Cannon) {
			if (lifetime > START_STASIS && entity.getPosition().getDistance(getPosition()) < (getRadius().getX() + entity.getRadius().getX())) {
				((Cannon) entity).hit(owner);
				remove();
			}
		} else if (entity instanceof Bullet) {
			if (((Bullet) entity).getPlayer() == getPlayer()) {
				collideWidth((Bullet) entity);
			} else {
				remove();
				entity.remove();
			}
		}
	}

	public void collideWidth(Bullet b) {
		if (collisionIgnore == b) return;
		
		if (getPosition().getDistance(b.getPosition()) > getRadius().getX() * 2) return; // doch keine Collision

		double dx = b.getPosX() - getPosX();
		double dy = b.getPosY() - getPosY();

		double quadDis = dx * dx + dy * dy;

		double v1d = getVelocity().getX() * dx + getVelocity().getY() * dy;

		double v2d = b.getVelocity().getX() * dx + b.getVelocity().getY() * dy;

		double k1Vx = getVelocity().getX() - dx * (v1d - v2d) / quadDis;
		double k1Vy = getVelocity().getY() - dy * (v1d - v2d) / quadDis;
		double k2Vx = b.getVelocity().getX() - dx * (v2d - v1d) / quadDis;
		double k2Vy = b.getVelocity().getY() - dy * (v2d - v1d) / quadDis;

		setVelocity(k1Vx, k1Vy);
		b.setVelocity(k2Vx, k2Vy);

		setBullIgnore(b);
		b.setBullIgnore(this);
	}
	
	public void collideWidth(ReflectingWall w) {
		MDRect r = w.getBoxBoundsRect();
		int quadrant = r.getQuadrant(getPosition());
		MPoint sup = velocity.getSuperiorAxis();
		switch(quadrant) {
		case MDRect.QUADRANT_TOP:
			velocity.setYNegative();
			break;
		case MDRect.QUADRANT_TOPLEFT:
			if (sup.getX() == 0) {
				velocity.setYNegative();
			} else if (sup.getY() == 0) {
				velocity.setXNegative();
			} else {
				velocity.setXNegative();
				velocity.setYNegative();
			}
			break;
		case MDRect.QUADRANT_LEFT:
			velocity.setXNegative();
			break;
		case MDRect.QUADRANT_BOTTOMLEFT:
			if (sup.getX() == 0) {
				velocity.setYPositive();
			} else if (sup.getY() == 0) {
				velocity.setXNegative();
			} else {
				velocity.setXNegative();
				velocity.setYPositive();
			}
			break;
		case MDRect.QUADRANT_BOTTOM:
			velocity.setYPositive();
			break;
		case MDRect.QUADRANT_BOTTOMRIGHT:
			if (sup.getX() == 0) {
				velocity.setYPositive();
			} else if (sup.getY() == 0) {
				velocity.setXPositive();
			} else {
				velocity.setXPositive();
				velocity.setYPositive();
			}
			break;
		case MDRect.QUADRANT_RIGHT:
			velocity.setXPositive();
			break;
		case MDRect.QUADRANT_TOPRIGHT:
			if (sup.getX() == 0) {
				velocity.setYNegative();
			} else if (sup.getY() == 0) {
				velocity.setXPositive();
			} else {
				velocity.setXPositive();
				velocity.setYNegative();
			}
			break;
		default:
			remove(); // Should NEVER HAPPEN
		}
	}
	
	public void collideWidth(ReflectingPillar p) {
		MDPoint conn = getClonedPos();
		conn.sub(p.getPosition());
		conn.makeOrthogonal();
		velocity.mirrorAt(conn);
	}

	public Player getPlayer() {
		return owner;
	}
	
	public Cannon getShooter() {
		return shooter;
	}

	public LevelEntity getPossibleTarget() {
		List<LevelEntity> lst = getOwner().getEntities();

		MDPoint p = new MDPoint(getPosition());

		for (int i = 0; i < MAX_LIFETIME; i++) {
			p.add(velocity.getX(), velocity.getY());

			for (LevelEntity e : lst) {
				if (! (e instanceof Bullet)) {
					if (p.isInRect(e.getBoxBounds())) {
						return e;
					}
				}
				if ((p.getX() < -100) || (p.getY() < -100) || (p.getX() > getOwner().getWidth() * getOwner().getTileWidth()) || (p.getY() > getOwner().getHeight() * getOwner().getTileHeight())) {
					return null;
				}
			}
		}
		
		return null;
	}

	public MDPoint getVelocity() {
		return velocity;
	}

	public void setVelocity(double x, double y) {
		velocity.set(x, y);
	}

	public void setBullIgnore(Bullet b) {
		collisionIgnore = b;
		collisionIgnoret = BULL_COLL_IGNORE_TIME;
	}

	public void calcVortexForce(Vortex v) {
		MDPoint nv = v.getClonedPos();
		nv.sub(getPosition());
		double ln = nv.getLength();
		if (ln <= 0) ln = 0.1;
		nv.setLength((1d/(ln*ln)) * Vortex.VORTEX_CONSTANT * owner.getOwner().getGameSpeed());
		velocity.add(nv);
	}
}
