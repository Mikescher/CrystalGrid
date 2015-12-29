package gridShooter.game.level;

import java.util.Random;

public class ConstantRandom extends Random {
	private static final long serialVersionUID = 3427446212385812951L;
	
	long seed;
	
	public ConstantRandom() {
		this(System.currentTimeMillis());
	}

	public ConstantRandom(long seed) {
		super(seed);
		this.seed = seed;
	}

	public double constDouble() {
		setSeed(seed);
		return super.nextDouble();
	}
}
