package gridShooter.game.level.singleplayer;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.GameKeys;
import gridShooter.game.GameScreen;
import gridShooter.game.level.ImageCache;
import gridShooter.game.level.LevelFileParser;
import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;
import gridShooter.game.level.player.ComputerPlayer;
import gridShooter.game.level.player.HumanPlayer;
import gridShooter.game.level.player.NeutralPlayer;
import gridShooter.game.level.player.Player;
import gridShooter.game.level.singleplayer.entities.Bullet;
import gridShooter.game.level.singleplayer.entities.Cannon;
import gridShooter.game.level.singleplayer.entities.ReflectingPillar;
import gridShooter.game.level.singleplayer.entities.ReflectingWall;
import gridShooter.game.level.singleplayer.entities.VoidPillar;
import gridShooter.game.level.singleplayer.entities.VoidWall;
import gridShooter.game.level.singleplayer.entities.Vortex;
import gridShooter.game.level.singleplayer.hud.GameHUD;
import gridShooter.game.level.singleplayer.hud.PauseHUD;
import gridShooter.game.level.singleplayer.hud.WinLooseHUD;
import gridShooter.game.menu.LevelSelectorMenu;
import gridShooter.game.menu.MainMenu;

import java.util.ArrayList;

import de.abscanvas.Screen;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.Level;
import de.abscanvas.level.tile.EmptyTile;
import de.abscanvas.math.MPoint;

public class GameLevel extends Level implements AbsLevel{
	private static final int STANDARD_HUMAN_PID = 0;

	private NeutralPlayer player_neutral;
	private ArrayList<Player> players = new ArrayList<Player>();

	private GameHUD hud;

	private AbsCannon draggedCannon = null;

	private double speed = 1; // ++ => faster

	private int difficulty;
	private boolean ki_only;
	private int level;
	private int human_pid;
	
	private int bulletSpeedMult = 1;
	
	private boolean running = true;

	public GameLevel(Screen o, int lvl, int difficulty, int width, int height) {
		super(o, Main.TILE_WIDTH, Main.TILE_HEIGHT, width, height);
		
		if (difficulty < 4) {
			this.difficulty = difficulty;
			ki_only = false;
			human_pid = STANDARD_HUMAN_PID;
		} else {
			this.difficulty = 3;
			ki_only = true;
			human_pid = -100;
		}
		
		this.level = lvl;

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				setTile(x, y, new EmptyTile());
			}
		}

		setBackgroundAlignement(ALIGN_CENTER);
		setBackgroundImage(GameArt.level_background);

		player_neutral = new NeutralPlayer(this, NEUTRAL_COLOR);

		hud = new GameHUD(this);

		setHUD(hud);
		
		setPaintOrder();

		// ---------------------------
		
		LevelFileParser fp = new LevelFileParser(this);
		boolean succ = fp.loadRessource("/lvl_" + lvl + ".aclvl");
		if (!succ) {
			System.out.println("Could not load LevelFile");
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (running) {
			if (((GameKeys) getOwner().getKeys()).pause.wasReleased()) {
				if (isPause()) {
					resume();
				} else {
					pause();
				}
			}

			checkSituation();
			
			if (draggedCannon != null) {
				if (!getOwner().getMouseButtons().isDown()) {
					stopDraggingCannon();
				}
			}
		}
	}
	
	private void setPaintOrder() {
		@SuppressWarnings("unchecked")
		Class<? extends LevelEntity>[] cgPaintOrder = new Class[7];
		cgPaintOrder[0] = Vortex.class;
		cgPaintOrder[1] = VoidWall.class;
		cgPaintOrder[2] = ReflectingWall.class;
		cgPaintOrder[3] = VoidPillar.class;
		cgPaintOrder[4] = ReflectingPillar.class;
		cgPaintOrder[5] = Cannon.class;
		cgPaintOrder[6] = Bullet.class;

		setPaintOrder(cgPaintOrder);
	}

	public void checkSituation() {
		for (int i = 0; i < 4; i++) {
			int ec = getEnemyCannonCount(i);
			if (ec == 0) {
				setGameSpeed(1);
				running = false;
				setHUD(new WinLooseHUD(this, i));
				if (i == human_pid) {
					((GameScreen)getOwner()).getPFile().add(getLevel(), getDifficulty(), ((GameScreen)getOwner()));
				}
				
				return;
			}
		}
	}

	public int getCannonCount(int pid) {
		int count = 0;
		ArrayList<LevelEntity> cannons = getAllEntities(Cannon.class);

		for (LevelEntity e : cannons) {
			if (((Cannon) e).getPlayer().getPID() == pid)
				count++;
		}

		return count;
	}

	public int getEnemyCannonCount(int pid) {
		int count = 0;
		ArrayList<LevelEntity> cannons = getAllEntities(Cannon.class);

		for (LevelEntity e : cannons) {
			if (((Cannon) e).getPlayer().getPID() != pid && !((Cannon) e).getPlayer().isNeutral())
				count++;
		}

		return count;
	}

	public boolean isPause() {
		return speed == 0;
	}

	public void pause() {
		setGameSpeed(0);
		setHUD(new PauseHUD(this));
	}

	public void resume() {
		setGameSpeed(1);
		setHUD(new GameHUD(this));
	}

	@Override
	public void onHUDPressed(MouseButtons arg0) {
		// Do nothing
	}

	@Override
	public void onMouseMove(MouseButtons arg0) {
		if (draggedCannon != null) {
			calculateCannonDragging();
		}
	}

	@Override
	public void onPressed(MouseButtons arg0) {
		// Do nothing
	}

	@Override
	public void onTilePressed(MouseButtons arg0) {
		// Do nothing
	}

	@Override
	public void startDraggingCannon(AbsCannon c) {
		draggedCannon = c;
	}

	@Override
	public void stopDraggingCannon() {
		draggedCannon = null;
	}
	
	private void calculateCannonDragging() {
		if (draggedCannon.isControllable()) {
			draggedCannon.updateDirection(screenToCanvas(getOwner().getMouseButtons().getPosition()));
		}

		if (!getOwner().getMouseButtons().isDown()) {
			stopDraggingCannon();
		}
	}

	@Override
	public ImageCache getCache() {
		return ((GameScreen) getOwner()).getCache();
	}

	@Override
	public NeutralPlayer getNeutralPlayer() {
		return player_neutral;
	}

	@Override
	public double getGameSpeed() {
		return speed;
	}

	@Override
	public void setGameSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public void setPlayerCount(int pc) {
		players.clear();
		
		if (pc <= 0) return;
		
		if (ki_only) {
			players.add(new ComputerPlayer(this, 0, COLORS[0], difficulty));
		} else {
			players.add(new HumanPlayer(this, 0, COLORS[0]));
		}
		

		for (int i = 1; i < pc; i++) {
			players.add(new ComputerPlayer(this, i, COLORS[i], difficulty));
		}
	}

	@Override
	public void addCannon(int cannX, int cannY, int playerID) {
		Player plx = findPlayerByID(playerID);
		if (plx != null) {
			Cannon add = new Cannon(new MPoint(cannX, cannY), plx);
			addEntity(add);
		} else {
			System.out.println("Cant find player to add to cannon : " + playerID);
		}
	}

	@Override
	public Player findPlayerByID(int id) {
		if (id == player_neutral.getPID())
			return player_neutral;

		for (Player p : players) {
			if (id == p.getPID())
				return p;
		}

		return null;
	}

	@Override
	public void addVoidWall(int px, int py, boolean horiz, int wid) {
		VoidWall w = new VoidWall(wid, horiz);
		w.setPos(px, py);
		addEntity(w);
	}
	
	@Override
	public void addVoidPillar(int px, int py) {
		VoidPillar w = new VoidPillar();
		w.setPos(px, py);
		addEntity(w);
	}
	
	@Override
	public void addReflectingWall(int px, int py, boolean horiz, int wid) {
		ReflectingWall w = new ReflectingWall(wid, horiz);
		w.setPos(px, py);
		addEntity(w);
	}
	
	@Override
	public void addReflectingPillar(int px, int py) {
		ReflectingPillar w = new ReflectingPillar();
		w.setPos(px, py);
		addEntity(w);
	}
	
	@Override
	public void addVortex(int vx, int vy) {
		Vortex v = new Vortex();
		v.setPos(vx, vy);
		addEntity(v);
	}
	
	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getDifficulty() {
		return difficulty;
	}
	
	public String dToS() {
		switch(difficulty) {
		case 0: return "easy";
		case 1: return "middle";
		case 2: return "hard";
		case 3: return (ki_only)?"warfare":"impossible";
		default: return "error";
		}
	}
	
	public void exit() {
		getOwner().setLevel(null);
		getOwner().clearMenus();
		getOwner().addMenu(new MainMenu((GameScreen) getOwner()));
		getOwner().addMenu(new LevelSelectorMenu((GameScreen) getOwner(), (getLevel()-1) / 6));
	}
	
	@Override
	public void restart() {
		getOwner().setLevel(new GameLevel(getOwner(), getLevel(), (ki_only)?4:getDifficulty(), Main.LEVEL_WIDTH, Main.LEVEL_HEIGHT));
	}

	@Override
	public void next() {
		getOwner().setLevel(new GameLevel(getOwner(), getLevel() + 1, getDifficulty(), Main.LEVEL_WIDTH, Main.LEVEL_HEIGHT));
	}
	
	@Override
	public void prev() {
		getOwner().setLevel(new GameLevel(getOwner(), getLevel() - 1, getDifficulty(), Main.LEVEL_WIDTH, Main.LEVEL_HEIGHT));
	}

	@Override
	public AbsCannon getDraggedCannon() {
		return draggedCannon;
	}

	public int getHumanPID() {
		return human_pid;
	}

	@Override
	public void setBulletSpeedMult(int mlt) {
		bulletSpeedMult = mlt;
	}
	
	@Override
	public double getBulletSpeedMultiplikator() {
		return bulletSpeedMult;
	}
}
