package gridShooter.game.level.player;

import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;

public class HumanPlayer extends Player {
	public HumanPlayer(AbsLevel owner, int pid, int color) {
		super(owner, pid, color);
	}

	@Override
	public boolean isNeutral() {
		return false;
	}

	@Override
	public void calculateMovement(AbsCannon c) {
		// Nothing here to do
	}

	@Override
	public double getMultiplikator() {
		return 1;
	}
}
