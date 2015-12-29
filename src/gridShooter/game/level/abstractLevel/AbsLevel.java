package gridShooter.game.level.abstractLevel;

import gridShooter.game.level.ImageCache;
import gridShooter.game.level.player.Player;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.entity.LevelEntity;
import de.abscanvas.surface.AbsColor;

public interface AbsLevel {
	public static final int NEUTRAL_COLOR = AbsColor.GRAY;
	public static final int[] COLORS = { AbsColor.GREEN, AbsColor.RED, AbsColor.BLUE, AbsColor.YELLOW, AbsColor.CYAN, AbsColor.ORANGE, AbsColor.PINK, AbsColor.MAGENTA };
	
	public void addVoidWall(int px, int py, boolean horiz, int wid);
	public void addVoidPillar(int px, int py);
	public void addReflectingWall(int px, int py, boolean horiz, int wid);
	public void addReflectingPillar(int px, int py);
	public void addCannon(int cannX, int cannY, int playerID);
	public void addVortex(int vx, int vy);
	public double getBulletSpeedMultiplikator();
	
	public void setPlayerCount(int pc);
	public void setBulletSpeedMult(int mlt);
	
	public ArrayList<LevelEntity> getAllEntities(Class<? extends LevelEntity> c);
	public List<LevelEntity> getEntities();
	
	public Player getNeutralPlayer();
	public Player findPlayerByID(int id);
	
	public int getWidth();
	public int getHeight();
	public int getTileWidth();
	public int getTileHeight();
	
	public void startDraggingCannon(AbsCannon cannon);
	public void stopDraggingCannon();
	public AbsCannon getDraggedCannon();
	
	public double getGameSpeed();
	public void setGameSpeed(double speed);
	
	public ImageCache getCache();
	
	public int getLevel();
	public int getDifficulty();
	
	public void restart();
	public void prev();
	public void next();
}
