package gridShooter.game.level.singleplayer.entities;

import gridShooter.Main;
import gridShooter.game.GameArt;
import de.abscanvas.entity.Entity;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.surface.Bitmap;

public class VoidWall extends LevelEntity{
	public VoidWall(int width, boolean horizontal) {
		super();
		setDebugging(Main.DEBUG);
		createImage(width, horizontal);
	}
	
	private void createImage(int width, boolean horizontal) {
		if (horizontal) {
			Bitmap b = new Bitmap(width, 8);
			b.blit(GameArt.wall[0], 0, 0);
			getAnimation().setAnimation(b);
			setRadius(width/2, 4);
		} else {
			Bitmap b = new Bitmap(8, width);
			b.blit(GameArt.wall[1], 0, 0);
			getAnimation().setAnimation(b);
			setRadius(4, width/2);
		}
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		if (entity instanceof Bullet) {
			entity.remove();
		}
	}
}
