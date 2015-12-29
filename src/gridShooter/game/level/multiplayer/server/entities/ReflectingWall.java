package gridShooter.game.level.multiplayer.server.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.network.ServerEntity;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkUser;
import de.abscanvas.surface.Bitmap;

public class ReflectingWall extends ServerEntity {
	private int w_width;
	private boolean w_horiz;
	
	public ReflectingWall(int width, boolean horizontal) {
		super();
		setDebugging(Main.DEBUG);
		createImage(width, horizontal);
	}
	
	private void createImage(int width, boolean horizontal) {
		w_width = width;
		w_horiz = horizontal;
		
		if (horizontal) {
			Bitmap b = new Bitmap(width, 8);
			b.blit(GameArt.wall_reflect[0], 0, 0);
			getAnimation().setAnimation(b);
			setRadius(width/2, 4);
		} else {
			Bitmap b = new Bitmap(8, width);
			b.blit(GameArt.wall_reflect[1], 0, 0);
			getAnimation().setAnimation(b);
			setRadius(4, width/2);
		}
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		if (entity instanceof Bullet) {
			((Bullet)entity).collideWidth(this);
		}
	}

	@Override
	public boolean isControllableByUser(NetworkUser arg0) {
		return false;
	}

	@Override
	public void onAfterClientControlMove(MDPoint arg0) {	
		// Do nothing
	}

	public int getWallWidth() {
		return w_width;
	}

	public boolean isHorizontal() {
		return w_horiz;
	}

}
