package gridShooter.game.level.multiplayer.client.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.level.abstractLevel.AbsLevel;
import gridShooter.game.level.multiplayer.NetworkGameConstants;
import gridShooter.game.level.multiplayer.client.ClientGameLevel;
import de.abscanvas.entity.DebugRenderer;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.Facing;
import de.abscanvas.entity.network.ClientEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;

public class Cannon extends ClientEntity {
	private int owner;
	
	private Facing facing = new Facing(64);
	
	private double health = 1; // 1=Full | 0 = Neutral
	
	private double state = 0; // 1 = ready | 0 = recharging
	
	private double direction  = 0; // in Degree

	public Cannon() {
		super();
		
//		setPos(pos);	
		setRadius(40);

		setDebugging(Main.DEBUG);
		
		owner = -2;
	}
	
	private Bitmap getBodyBitmap() {
		int stateBmp = (int)(state*6);

		return GameArt.cannon[stateBmp][facing.get()];
	}
	
	private Bitmap getProgressBitmap() {
		int healthBmp = (int)((1-health)*63);
		
		Bitmap x = GameArt.cannonProgressMarker[healthBmp][facing.get()].copy();
		
		x.replaceColor(AbsColor.WHITE, getColor());
		
		return x;
	}
	
	private int getColor() {
		if (owner < -1) {
			return AbsColor.white;
		} else if (owner  == -1) {
			return AbsLevel.NEUTRAL_COLOR;
		} else {
			return AbsLevel.COLORS[owner];
		}
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
				
				surface.alphaBlit(getOwner().getCache().getTargeterImage(p), getPosX() - Math.abs(p.getX()), getPosY() - Math.abs(p.getY()), getAlpha()/2);
			}
			
			if (isDebugging()) {
				(new DebugRenderer(this)).render(surface);
			}
		}
	}
	
	public boolean isDragging() {
		return false;
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// Nothing
	}

	public int getPlayer() {
		return owner;
	}
	
	public double getDirection() {
		return direction;
	}
	
	public boolean isControllable() {
		return false;
	}
	
	@Override
	public ClientGameLevel getOwner() {
		return (ClientGameLevel) super.getOwner();
	}
	
	@Override
	public void onMousePress(MouseButtons mouse) {
		// Do nothing
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

	public void setPlayer(int pid) {
		health = 0;
		owner = pid;
	}
	
	@Override
	public void serverCommand_added(double x, double y, byte[] data) { // After Adding;
		super.serverCommand_added(x, y, data);
		getOwner().getNetworkAdapter().send(NetworkGameConstants.CMD_REQCANNONINFO, ByteUtilities.long2Arr(getUID()));
	}
}
