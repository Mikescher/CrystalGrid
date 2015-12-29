package gridShooter.game.level.player;

import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;

public abstract class Player {
	private AbsLevel owner;
	private int color;
	private int pid;
	
	public Player(AbsLevel owner, int pid, int color) {
		this.color = color;
		this.pid = pid;
		this.owner = owner;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getPID() {
		return pid;
	}
	
	public abstract boolean isNeutral();
	
	public abstract void calculateMovement(AbsCannon c);

	public AbsLevel getOwner() {
		return owner;
	}

	public abstract double getMultiplikator();
}
