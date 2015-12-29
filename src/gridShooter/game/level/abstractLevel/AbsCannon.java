package gridShooter.game.level.abstractLevel;

import gridShooter.game.level.player.Player;
import de.abscanvas.math.MDPoint;

public interface AbsCannon {
	final static double STATE_REGEN = 0.015;
	
	final static double START_HEALTH_REGEN = 0.00025; //0,25 °/oo
	final static double END_HEALTH_REGEN = 0.00175;	  //1,75 °/oo
	// REAL_HEATH_REGEN = (START_HEALTH_REGEN + END_HEALTH_REGEN) / 2
	
	final static double HEALTH_HIT_DROP = 0.20; // on Hit
	final static double HEALTH_HIT_GEN = 0.20; // on Hit from own cannon
	
	final static int ROTATION_SPEED = 3; // n Degree per Tick
	final static int KI_INTERVALL = 100;
	
	public boolean isControllable();

	public void updateDirection(MDPoint tp);

	public boolean checkKITicker();
	
	public void forceKITicker();

	public Player getPlayer();

	public MDPoint getPosition();

	public void setTarget(MDPoint position);

	public void resetState();
}
