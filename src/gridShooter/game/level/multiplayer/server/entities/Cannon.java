package gridShooter.game.level.multiplayer.server.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.level.CannonBooster;
import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;
import gridShooter.game.level.player.HumanPlayer;
import gridShooter.game.level.player.Player;

import java.util.ArrayList;

import de.abscanvas.entity.DebugRenderer;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.Facing;
import de.abscanvas.entity.network.ServerEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.network.NetworkUser;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;

public class Cannon extends ServerEntity implements AbsCannon {	
	private Player owner;
	
	private Facing facing = new Facing(64);
	private double health = 1; // 1=Full | 0 = Neutral
	private double state = 0; // 1 = ready | 0 = recharging

	private double direction  = 0; // in Degree
	private double target_dir = 0; // in Degree
	private int KIticker = KI_INTERVALL - 1;
	private boolean invic = false;
	
	private ArrayList<CannonBooster> booster = new ArrayList<>();

	public Cannon(MPoint pos, Player p) {
		super();
		
		setPos(pos);	
		setRadius(40);

		if (pos.getX() < ((p.getOwner().getWidth()*p.getOwner().getTileWidth())/2)) {
			target_dir = direction = 180;
		} else {
			target_dir = direction = 0;
		}
		facing.set((int)Math.round(((direction-90)/360)*facing.getDivisions()));
		
		setDebugging(Main.DEBUG);
		
		owner = p;
	}

	@Override
	public void onInit(int tileW, int tileH) {
		super.onInit(tileH, tileH);
	}

	@Override
	public void tick() {
		super.tick();
		
		updateDirection();
		
		addHealth(getHealthRegen());
		if (health == 1) {
			addState(getStateRegen());
		}

		if (state == 1 && health == 1) shoot();
		
		owner.calculateMovement(this);
		
		tickBooster();
	}
	
	private void tickBooster() {
		for (int i = booster.size() - 1; i >= 0; i--) {
			if (! booster.get(i).tick(getGameSpeed())) {
				booster.remove(i);
			}
		}
	}
	
	public double getGameSpeed() {
		return getAbsOwner().getGameSpeed();
	}
	
	private double getRotationSpeed() {
		return ROTATION_SPEED * getGameSpeed();
	}
	
	private void updateDirection() {
		if (direction != target_dir) {		
			if (Math.abs(direction - target_dir) > getRotationSpeed()) {
				if (getAngleSub(direction, target_dir) < 0) {
					direction += getRotationSpeed();
				} else {
					direction -= getRotationSpeed();
				}
				fixDirectionOverflows();
			} else {
				direction = target_dir;
			}
			
			facing.set((int)Math.round(((direction-90)/360)*facing.getDivisions()));
		}
	}
	
	private void fixDirectionOverflows() {
		while (direction < 0) {
			direction += 360;
		}
		
		while (target_dir < 0) {
			target_dir += 360;
		}
		
		while (direction >= 360) {
			direction -= 360;
		}
		
		while (target_dir >= 360) {
			target_dir -= 360;
		}
	}
	
	private double getAngleSub(double a1, double a2) { // a1 - a2
		double posD = 0;
		double negD = 0;
		
		if (a1 == a2) return 0;
		
		if (a1 > a2) {
			negD = a1 - a2;
			posD = a2 + 360-a1;
		} else {
			negD = a1 + 360-a2;
			posD = a2-a1;
		}
		
		if (posD >= negD) {
			return posD;
		} else {
			return -negD;
		}
	}
	
	private Bitmap getBodyBitmap() {
		int stateBmp = (int)(state*6);

		return GameArt.cannon[stateBmp][facing.get()];
	}
	
	private Bitmap getProgressBitmap() {
		int healthBmp = (int)((1-health)*63);
		
		Bitmap x = GameArt.cannonProgressMarker[healthBmp][facing.get()].copy();
		x.replaceColor(AbsColor.WHITE, owner.getColor());
		
		return x;
	}
	
	@Override
	public void render(Surface surface) {
		if (isVisible()) {		
			Bitmap body = getBodyBitmap();
			Bitmap prog = getProgressBitmap();
			
			surface.alphaBlit(body, getPosX() - body.getWidth()/2, getPosY() - body.getHeight()/2, getAlpha());
			surface.alphaBlit(prog, getPosX() - prog.getWidth()/2, getPosY() - body.getHeight()/2, getAlpha());
			
			if (isDragging() && isControllable()) {
				MPoint p = getOwner().getOwner().screenToCanvas(getOwner().getOwner().getMouseButtons().getPosition()).trunkToMPoint();
				p.sub((int)getPosX(), (int)getPosY());
				
				surface.alphaBlit(getAbsOwner().getCache().getTargeterImage(p), getPosX() - Math.abs(p.getX()), getPosY() - Math.abs(p.getY()), getAlpha()/2);
			}
			
			if (isDebugging()) {
				(new DebugRenderer(this)).render(surface);
			}
		}
	}

	public boolean isDragging() {
		return getAbsOwner().getDraggedCannon() == this;
	}
	
	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// Nothing
	}

	@Override
	public Player getPlayer() {
		return owner;
	}
	
	public double getDirection() {
		return direction;
	}
	
	private void shoot() {
		Bullet bull = new Bullet(this);
		getOwner().addEntity(bull);
		
		state = 0;
	}
	
	public MDPoint getBulletPos() {
		MDPoint pos = getClonedPos();
		pos.subY(48);
		return pos.rotate(Math.PI*2/64 * facing.get(), pos.getX(), pos.getY() + 48, false);
	}
	
	public AbsLevel getAbsOwner() {
		return (AbsLevel) super.getOwner();
	}
	
	@Override
	public boolean isControllable() {
		return owner instanceof HumanPlayer;
	}
	
	@Override
	public void onMousePress(MouseButtons mouse) {
		getAbsOwner().startDraggingCannon(this);
		if (Main.DEBUG && mouse.isDoubleClick()) {
			setPlayer(getAbsOwner().findPlayerByID(0));
			addHealth(100);
			addState(100);
			invic = true;
		}
	}

	@Override
	public void onMouseMove(MouseButtons mouse) {
		// Do nothing
	}

	@Override
	public void onMouseRelease(MouseButtons mouse) {
		// Do nothing
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		// Do nothing
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		// Do nothing
	}

	@Override
	public void onMouseHover(MouseButtons mouse) {
		// Do nothing
	}

	@Override
	public void updateDirection(MDPoint mopo) {	
		target_dir = mopo.getAngle(getPosition());
	}
	
	public boolean isNeutral() {
		return owner.isNeutral();
	}
	
	public double getBoost() {
		double b = 0;
		for (CannonBooster cb : booster) {
			b += cb.getBoost();
		}
		return b;
	}
	
	public double getStateRegen() {
		return STATE_REGEN * owner.getMultiplikator() * getGameSpeed() * (1 + getBoost());
	}
	
	public double getHealthRegen() {
		if (isNeutral()) {
			return 0;
		} else {
			return (START_HEALTH_REGEN + (END_HEALTH_REGEN - START_HEALTH_REGEN) * getHealth()) * getGameSpeed(); // exponential, bitches
		}
	}

	public void hit(Player o) {
		if (invic) return;
		
		if (o == owner) {
			addHealth(HEALTH_HIT_GEN * getGameSpeed());
			booster.add(new CannonBooster(0.5, (int)(1/(STATE_REGEN * owner.getMultiplikator()))));
		} else if (o.isNeutral()) {
			// Nothing, too
		} else {
			if (owner.isNeutral()) {
				setPlayer(o);
			} else {
				double plushealth = leakHealth(HEALTH_HIT_DROP * getGameSpeed());
				
				if (health == 0) {
					setPlayer(getAbsOwner().getNeutralPlayer());
					setPlayer(o);
					addHealth(plushealth);
				}
			}
		}
	}
	
	public void setPlayer(Player p) {
		health = 0;
		booster.clear();
		owner = p;
	}
	
	private double leakHealth(double h) {
		health -= h;
		if (health < 0) {
			double p = -health;
			health = 0;
			return p;
		}
		return 0;
	}
	
	private void addHealth(double h) {
		health += h;
		if (health > 1) health = 1;
	}
	
	private void addState(double s) {
		state += s;
		if (state > 1) state = 1;
	}

	@Override
	public void resetState() {
		state = 0;
	}
	
	@Override
	public void setTarget(MDPoint tg) {
		target_dir = tg.getAngle(getPosition());
	}

	public double getHealth() {
		return health;
	}
	
	@Override
	public boolean checkKITicker() {// only every 100 ticks
		KIticker++;
		if (KIticker >= KI_INTERVALL) {
			KIticker = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public void forceKITicker() {
		KIticker = 0;
	}

	@Override
	public boolean isControllableByUser(NetworkUser arg0) {
		return true;
	}

	@Override
	public void onAfterClientControlMove(MDPoint arg0) {
		// Do nothing
	}
}
