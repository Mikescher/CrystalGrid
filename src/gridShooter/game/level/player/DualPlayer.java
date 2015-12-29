package gridShooter.game.level.player;

import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;

public class DualPlayer extends Player {
	
	private boolean isHuman;
	
	private HumanPlayer player_human;
	private ComputerPlayer player_computer;
	
	public DualPlayer(AbsLevel owner, int pid, int color, int difficulty) {
		super(owner, pid, color);
		player_human = new HumanPlayer(owner, pid, color);
		player_computer = new ComputerPlayer(owner, pid, color, difficulty, new ComputerProcessor(difficulty, this));
	}

	@Override
	public boolean isNeutral() {
		return false;
	}

	@Override
	public void calculateMovement(AbsCannon c) {
		if (isHuman) {
			player_human.calculateMovement(c);
		} else {
			player_computer.calculateMovement(c);
		}
	}

	@Override
	public double getMultiplikator() {
		if (isHuman) {
			return player_human.getMultiplikator();
		} else {
			return player_computer.getMultiplikator();
		}
	}

	public boolean isHuman() {
		return isHuman;
	}
	
	public void setHuman(boolean isHum) {
		isHuman = isHum;
	}
}
