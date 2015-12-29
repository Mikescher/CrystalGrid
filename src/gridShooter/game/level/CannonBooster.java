package gridShooter.game.level;

public class CannonBooster {
	private double lifetime;
	private final double boost;
	
	public CannonBooster(double boost, int lifetime) {
		this.boost = boost;
		this.lifetime = lifetime;
	}

	public boolean tick(double gameSpeed) {
		lifetime -= gameSpeed;
		return lifetime > 0;
	}
	
	public double getBoost() {
		return boost;
	}
}
