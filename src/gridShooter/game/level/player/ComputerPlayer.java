package gridShooter.game.level.player;

import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;

public class ComputerPlayer extends Player{
	private final static double[] MULTIPLIKATOR = {0.80, 0.875, 0.95, 1.0};
	
	private int difficulty;
	private ComputerProcessor processor;
	
	public ComputerPlayer(AbsLevel owner, int pid, int color, int difficulty) {
		super(owner, pid, color);
		this.difficulty = difficulty;
		processor = new ComputerProcessor(difficulty, this);
	}
	
	public ComputerPlayer(AbsLevel owner, int pid, int color, int difficulty, ComputerProcessor p) {
		super(owner, pid, color);
		this.difficulty = difficulty;
		processor = p;
	}

	@Override
	public boolean isNeutral() {
		return false;
	}

	@Override
	public void calculateMovement(AbsCannon c) {
		if (c.checkKITicker()) {
			processor.calculate(c);
		}
	}

	@Override
	public double getMultiplikator() {
		return MULTIPLIKATOR[difficulty];
	}

}
