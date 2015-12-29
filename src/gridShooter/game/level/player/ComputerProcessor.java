package gridShooter.game.level.player;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.entity.LevelEntity;
import de.abscanvas.math.MDPoint;

import gridShooter.game.level.ConstantRandom;
import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;
import gridShooter.game.level.singleplayer.entities.Bullet;
import gridShooter.game.level.singleplayer.entities.Cannon;

public class ComputerProcessor {
	private final static int MAX_BULL_DISTANCE = 450;

	private int init;
	private Player owner;
	private AbsLevel level;
	
	private ConstantRandom randGenerator;

	public ComputerProcessor(int intelligence, Player owner) {
		this.init = intelligence;
		this.owner = owner;
		this.level = owner.getOwner();
		
		randGenerator = new ConstantRandom();
	}

	public void calculate(AbsCannon c) {
		switch (init) {
		case 0:
		case 1:
		case 2:
		case 3:
			calc_One(c);
			break;
		case 4: // NEVER
			calc_Two(c);
			break;
		}
	}

	private Bullet getHomingBullet(AbsCannon c) {
		Bullet ret = null;
		double minDis = Double.MAX_VALUE;
		ArrayList<LevelEntity> bullets = level.getAllEntities(Bullet.class);

		for (LevelEntity ucbullet : bullets) {
			Bullet bullet = (Bullet) ucbullet;
			if (bullet.getPossibleTarget() == c) {
				double dis = bullet.getPosition().getDistance(c.getPosition());
				if (bullet.getPlayer() != c.getPlayer() && (!bullet.getPlayer().isNeutral())) {
					if (dis < MAX_BULL_DISTANCE && dis < minDis) {
						minDis = dis;
						ret = bullet;
					}
				}
			}
		}

		return ret;
	}

	private boolean hasEyeContact(AbsCannon c, Cannon endCann) {
		List<LevelEntity> lst = level.getEntities();

		MDPoint currPoint = new MDPoint(c.getPosition());
		MDPoint vector = new MDPoint(endCann.getPosition());
		int l = (int) (vector.getLength() * 2);
		vector.sub(c.getPosition());
		vector.setLength(1);

		for (int i = 0; i < l; i++) {
			currPoint.add(vector);
			for (LevelEntity e : lst) {
				if (currPoint.isInRect(e.getBoxBounds()) && e != c) {
					if (e instanceof Bullet) {
						if (((Bullet)e).getShooter() == c) continue;
					}
					if (e == endCann) {
						return true;
					} else {
						return false;
					}
				}
			}
		}

		return false;
	}
	
	private boolean hasLowLevelEyeContact(AbsCannon c, Cannon endCann) {
		List<LevelEntity> lst = level.getEntities();

		MDPoint currPoint = new MDPoint(c.getPosition());
		MDPoint vector = new MDPoint(endCann.getPosition());
		int l = (int) (vector.getLength() * 2);
		vector.sub(c.getPosition());
		vector.setLength(1);

		for (int i = 0; i < l; i++) {
			currPoint.add(vector);
			for (LevelEntity e : lst) {
				if (currPoint.isInRect(e.getBoxBounds()) && e != c) {
					if (e instanceof Bullet) {
						continue;
					}
					if (e == endCann) {
						return true;
					} else {
						return false;
					}
				}
			}
		}

		return false;
	}
	
	private boolean hasFullFreeEyeContact(AbsCannon c, Cannon endCann) {
		List<LevelEntity> lst = level.getEntities();

		MDPoint currPoint = new MDPoint(c.getPosition());
		MDPoint vector = new MDPoint(endCann.getPosition());
		int l = (int) (vector.getLength() * 2);
		vector.sub(c.getPosition());
		vector.setLength(1);

		for (int i = 0; i < l; i++) {
			currPoint.add(vector);
			for (LevelEntity e : lst) {
				if (currPoint.isInRect(e.getBoxBounds()) && e != c) {
					if (e == endCann) {
						return true;
					} else {
						return false;
					}
				}
			}
		}

		return false;
	}
	
	private boolean nequals(double a, double b) {
		return Math.abs(a - b) < 128;
	}

	private Cannon getLowHPCannon(AbsCannon c) {
		List<Cannon> ret = new ArrayList<>();
		double minDis = Double.MAX_VALUE;

		ArrayList<LevelEntity> cannons = level.getAllEntities(Cannon.class);
		for (LevelEntity uccannon : cannons) {
			Cannon cannon = (Cannon) uccannon;
			if (cannon.getPlayer() == owner && cannon.getHealth() <= 0.5 && cannon != c) {
				double dis = cannon.getPosition().getDistance(c.getPosition());
				if (hasEyeContact(c, cannon)) {
					if (nequals(dis, minDis)) {
						ret.add(cannon);
					} else if (dis < minDis) {
						ret.clear();
						minDis = dis;
						ret.add(cannon);
					}
				}
			}
		}
		
		if (ret.isEmpty()) {
			return null;
		} else {
			return ret.get((int) (randGenerator.constDouble() * ret.size()));
		}
	}

	private Cannon getNeutralCannon(AbsCannon c) {
		List<Cannon> ret = new ArrayList<>();
		double minDis = Double.MAX_VALUE;

		ArrayList<LevelEntity> cannons = level.getAllEntities(Cannon.class);
		for (LevelEntity uccannon : cannons) {
			Cannon cannon = (Cannon) uccannon;
			if (cannon.isNeutral()) {
				double dis = cannon.getPosition().getDistance(c.getPosition());
				if (hasEyeContact(c, cannon)) {
					if (nequals(dis, minDis)) {
						ret.add(cannon);
					} else if (dis < minDis) {
						ret.clear();
						minDis = dis;
						ret.add(cannon);
					}
				}
			}
		}

		if (ret.isEmpty()) {
			return null;
		} else {
			return ret.get((int) (randGenerator.constDouble() * ret.size()));
		}
	}

	private Cannon getTargetableEnemyCannon(AbsCannon c) {
		List<Cannon> ret = new ArrayList<>();
		double minDis = Double.MAX_VALUE;

		ArrayList<LevelEntity> cannons = level.getAllEntities(Cannon.class);
		for (LevelEntity uccannon : cannons) {
			Cannon cannon = (Cannon) uccannon;
			if ((!cannon.isNeutral()) && cannon.getPlayer() != owner) {
				double dis = cannon.getPosition().getDistance(c.getPosition());
				if (hasEyeContact(c, cannon)) {
					if (nequals(dis, minDis)) {
						ret.add(cannon);
					} else if (dis < minDis) {
						ret.clear();
						minDis = dis;
						ret.add(cannon);
					}
				}
			}
		}

		if (ret.isEmpty()) {
			return null;
		} else {
			return ret.get((int) (randGenerator.constDouble() * ret.size()));
		}
	}
	
	private Cannon getLLTargetableEnemyCannon(AbsCannon c) {
		List<Cannon> ret = new ArrayList<>();
		double minDis = Double.MAX_VALUE;

		ArrayList<LevelEntity> cannons = level.getAllEntities(Cannon.class);
		for (LevelEntity uccannon : cannons) {
			Cannon cannon = (Cannon) uccannon;
			if ((!cannon.isNeutral()) && cannon.getPlayer() != owner) {
				double dis = cannon.getPosition().getDistance(c.getPosition());
				if (hasLowLevelEyeContact(c, cannon)) {
					if (nequals(dis, minDis)) {
						ret.add(cannon);
					} else if (dis < minDis) {
						ret.clear();
						minDis = dis;
						ret.add(cannon);
					}
				}
			}
		}

		if (ret.isEmpty()) {
			return null;
		} else {
			return ret.get((int) (randGenerator.constDouble() * ret.size()));
		}
	}
	
	private Cannon getEnemyCannon(AbsCannon c) {
		List<Cannon> ret = new ArrayList<>();
		double minDis = Double.MAX_VALUE;

		ArrayList<LevelEntity> cannons = level.getAllEntities(Cannon.class);
		for (LevelEntity uccannon : cannons) {
			Cannon cannon = (Cannon) uccannon;
			if ((!cannon.isNeutral()) && cannon.getPlayer() != owner) {
				double dis = cannon.getPosition().getDistance(c.getPosition());
				if (nequals(dis, minDis)) {
					ret.add(cannon);
				} else if (dis < minDis) {
					ret.clear();
					minDis = dis;
					ret.add(cannon);
				}
			}
		}

		if (ret.isEmpty()) {
			return null;
		} else {
			return ret.get((int) (randGenerator.constDouble() * ret.size()));
		}
	}
	
	private Cannon getFriendlyCannon(AbsCannon c) {
		List<Cannon> ret = new ArrayList<>();
		double minDis = Double.MAX_VALUE;

		ArrayList<LevelEntity> cannons = level.getAllEntities(Cannon.class);
		for (LevelEntity uccannon : cannons) {
			Cannon cannon = (Cannon) uccannon;
			if (cannon.getPlayer() == owner) {
				double dis = cannon.getPosition().getDistance(c.getPosition());
				if (hasEyeContact(c, cannon)) {
					if (nequals(dis, minDis)) {
						ret.add(cannon);
					} else if (dis < minDis) {
						ret.clear();
						minDis = dis;
						ret.add(cannon);
					}
				}
			}
		}

		if (ret.isEmpty()) {
			return null;
		} else {
			return ret.get((int) (randGenerator.constDouble() * ret.size()));
		}
	}
	
	private Cannon getTargetableFriendlyCannon(AbsCannon c) {
		List<Cannon> ret = new ArrayList<>();
		double minDis = Double.MAX_VALUE;

		ArrayList<LevelEntity> cannons = level.getAllEntities(Cannon.class);
		for (LevelEntity uccannon : cannons) {
			Cannon cannon = (Cannon) uccannon;
			if (cannon.getPlayer() == owner) {
				double dis = cannon.getPosition().getDistance(c.getPosition());
				if (hasFullFreeEyeContact(c, cannon)) {
					if (nequals(dis, minDis)) {
						ret.add(cannon);
					} else if (dis < minDis) {
						ret.clear();
						minDis = dis;
						ret.add(cannon);
					}
				}
			}
		}

		if (ret.isEmpty()) {
			return null;
		} else {
			return ret.get((int) (randGenerator.constDouble() * ret.size()));
		}
	}
	
	private void calc_One(AbsCannon c) {
		// ------------------------ Dodging incoming missiles ------------------------

		Bullet targ = getHomingBullet(c);

		if (targ != null) {
			c.setTarget(targ.getPosition());
			return;
		}

		// ------------------------ Help Low HP Freinds ------------------------

		Cannon tann = getLowHPCannon(c);

		if (tann != null) {
			c.setTarget(tann.getPosition());
			return;
		}

		// ------------------------ Conquer Neutral Bases ------------------------

		Cannon tnca = getNeutralCannon(c);

		if (tnca != null) {
			c.setTarget(tnca.getPosition());
			return;
		}

		// ------------------------ Attack Opponents ------------------------

		Cannon toca = getTargetableEnemyCannon(c);

		if (toca != null) {
			c.setTarget(toca.getPosition());
			return;
		}
		
		// ------------------------ Support Teamplayers ------------------------

		Cannon tfca = getTargetableFriendlyCannon(c);

		if (tfca != null) {
			c.setTarget(tfca.getPosition());
			return;
		}
		
		// ------------------------ Attack who you can ------------------------

		Cannon eoca = getLLTargetableEnemyCannon(c);

		if (eoca != null) {
			c.setTarget(eoca.getPosition());
			return;
		}
		
		// ------------------------ Support who you can ------------------------

		Cannon tefc = getFriendlyCannon(c);

		if (tefc != null) {
			c.setTarget(tefc.getPosition());
			return;
		}
		
		// ------------------------ Just Guess ------------------------
		
		Cannon roca = getEnemyCannon(c);

		if (roca != null) {
			MDPoint vel = roca.getPosition();
			vel = vel.rotate(Math.toRadians(120 * (Math.random() - 0.5)), c.getPosition().getX(), c.getPosition().getY(), false);
			c.setTarget(vel);
			return;
		}
	}

	/**
	 * @param c Cannon to Calc 
	 */
	private void calc_Two(AbsCannon c) {
		//Todo implement better KI for ??? (not impossible - or it would be impossible ^^)
	}
}
