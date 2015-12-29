package gridShooter.game.level.multiplayer.server;

import gridShooter.Main;
import gridShooter.game.level.ImageCache;
import gridShooter.game.level.LevelFileParser;
import gridShooter.game.level.abstractLevel.AbsCannon;
import gridShooter.game.level.abstractLevel.AbsLevel;
import gridShooter.game.level.multiplayer.NetworkGameConstants;
import gridShooter.game.level.multiplayer.client.ClientGameLevel;
import gridShooter.game.level.multiplayer.server.entities.Bullet;
import gridShooter.game.level.multiplayer.server.entities.Cannon;
import gridShooter.game.level.multiplayer.server.entities.ReflectingPillar;
import gridShooter.game.level.multiplayer.server.entities.ReflectingWall;
import gridShooter.game.level.multiplayer.server.entities.VoidPillar;
import gridShooter.game.level.multiplayer.server.entities.VoidWall;
import gridShooter.game.level.player.DualPlayer;
import gridShooter.game.level.player.NeutralPlayer;
import gridShooter.game.level.player.Player;

import java.net.InetAddress;
import java.util.ArrayList;

import de.abscanvas.ConsoleScreen;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.level.tile.EmptyTile;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.math.MPoint;
import de.abscanvas.network.NetworkUser;

public class ServerGameLevel extends ServerLevel implements AbsLevel {
	public final static int PORT = 8750;
	
	private NeutralPlayer player_neutral;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private double speed = 0; // ++ => faster
	
	private int difficulty;
	
	private int level;
	
	private ImageCache cache;
	
	public ServerGameLevel(ConsoleScreen o, int lvl, int diff, ImageCache ic) {
		super(o, Main.TILE_WIDTH, Main.TILE_HEIGHT, Main.GAME_WIDTH, Main.GAME_HEIGHT, PORT, ClientGameLevel.IDENTIFIER);
		
		registerTile(EmptyTile.class, 0);
		
		registerEntity(Bullet.class, 1);
		registerEntity(Cannon.class, 2);
		registerEntity(ReflectingPillar.class, 3);
		registerEntity(ReflectingWall.class, 4);
		registerEntity(VoidPillar.class, 5);
		registerEntity(VoidWall.class, 6);
		
		cache = ic;
		
		player_neutral = new NeutralPlayer(this, NEUTRAL_COLOR);
		
		level = lvl;
		difficulty = diff;
		
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				setTile(x, y, new EmptyTile());
			}
		}
		LevelFileParser fp = new LevelFileParser(this);
		boolean succ = fp.loadRessource("/lvl_" + lvl + ".aclvl");
		if (!succ) {
			getOwner().consoleOutput("Could not load Level-File (" + "/lvl_" + lvl + ".aclvl" + ")");
		}
	}

	@Override
	public boolean canConnectNewUser() {
		return getUsers().size() < getMaxClientCount();
	}

	@Override
	public String getGameName() {
		return Main.GAME_TITLE;
	}

	@Override
	public int getMaxClientCount() {
		return 12;
	}

	@Override
	public String getServerName() {
		return "CrystalGrid Vanilla Server";
	}

	@Override
	public String getVersionString() {
		return Main.VERSION;
	}

	@Override
	public void onAfterUserConnect(NetworkUser u) {
		((ServerGameScreen)getOwner()).getOwner().addUser(u, getUsers().size());
	}

	@Override
	public int onCustomClientCommand(byte packageID, int commandID, byte[] data, NetworkUser u) {
		if (commandID == NetworkGameConstants.CMD_REQCANNONINFO) {
			long uid = ByteUtilities.arr2Long(data, 0);
			LevelEntity cann = getEntity(uid);
			if (cann instanceof Cannon) {
				byte[] d = new byte[12];
				d = ByteUtilities.insert(d, ByteUtilities.long2Arr(uid), 0);
				d = ByteUtilities.insert(d, ByteUtilities.int2Arr(((Cannon)cann).getPlayer().getPID()), 8);
				getNetworkAdapter().send(NetworkGameConstants.CMD_SETCANNONINFO, d, u);
			} else {
				System.out.println("S: ERROR - Cannon UID is not UID from Cannon !?!");
			}
			return 8;
		} else if (commandID == NetworkGameConstants.CMD_REQBULLETINFO) {
			long uid = ByteUtilities.arr2Long(data, 0);
			LevelEntity cann = getEntity(uid);
			if (cann instanceof Bullet) {
				byte[] d = new byte[12];
				d = ByteUtilities.insert(d, ByteUtilities.long2Arr(uid), 0);
				d = ByteUtilities.insert(d, ByteUtilities.int2Arr(((Bullet)cann).getPlayer().getPID()), 8);
				getNetworkAdapter().send(NetworkGameConstants.CMD_SETBULLETINFO, d, u);
			} else {
				System.out.println("S: ERROR - Bullet UID is not UID from Bullet !?!");
			}
			return 8;
		} else if (commandID == NetworkGameConstants.CMD_REQRWALLINFO) {
			long uid = ByteUtilities.arr2Long(data, 0);
			LevelEntity cann = getEntity(uid);
			if (cann instanceof ReflectingWall) {
				byte[] d = new byte[13];
				d = ByteUtilities.insert(d, ByteUtilities.long2Arr(uid), 0);
				d = ByteUtilities.insert(d, ByteUtilities.int2Arr(((ReflectingWall)cann).getWallWidth()), 8);
				d = ByteUtilities.insert(d, ByteUtilities.boolean2Arr(((ReflectingWall)cann).isHorizontal()), 12);
				getNetworkAdapter().send(NetworkGameConstants.CMD_SETRWALLINFO, d, u);
			} else {
				System.out.println("S: ERROR - RWall UID is not UID from RW !?!");
			}
			return 8;
		} else if (commandID == NetworkGameConstants.CMD_REQVWALLINFO) {
			long uid = ByteUtilities.arr2Long(data, 0);
			LevelEntity cann = getEntity(uid);
			if (cann instanceof VoidWall) {
				byte[] d = new byte[13];
				d = ByteUtilities.insert(d, ByteUtilities.long2Arr(uid), 0);
				d = ByteUtilities.insert(d, ByteUtilities.int2Arr(((VoidWall)cann).getWallWidth()), 8);
				d = ByteUtilities.insert(d, ByteUtilities.boolean2Arr(((VoidWall)cann).isHorizontal()), 12);
				getNetworkAdapter().send(NetworkGameConstants.CMD_SETVWALLINFO, d, u);
			} else {
				System.out.println("S: ERROR - VWall UID is not UID from VW !?!");
			}
			return 8;
		} else {
			return 0;
		}
	}

	@Override
	public NetworkUser onUserConnect(String name, InetAddress ip, int port) {
		return new ServerUser(name, ip, port, getNetworkAdapter());
	}

	@Override
	public void onUserDisconnected(NetworkUser arg0) {
		redoUserList();
	}

	@Override
	public void onUserKicked(NetworkUser arg0) {
		redoUserList();
	}

	@Override
	public void onUserTimedOut(NetworkUser arg0) {
		redoUserList();
	}

	private void redoUserList() {
		((ServerGameScreen)getOwner()).getOwner().resetUserList();

		for (NetworkUser kk : getUsers()) {
			((ServerGameScreen)getOwner()).getOwner().addUser(kk, getUsers().size());
		}
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
	public void setPlayerCount(int pc) {
		players.clear();
		
		if (pc <= 0) return;

		for (int i = 0; i < pc; i++) {
			players.add(new DualPlayer(this, i, COLORS[i], difficulty));
		}
	}

	@Override
	public Player getNeutralPlayer() {
		return player_neutral;
	}

	@Override
	public void startDraggingCannon(AbsCannon cannon) {
		return; // GEHT NET, GIBTS NET
	}

	@Override
	public void stopDraggingCannon() {
		return; // GEHT NET, GIBTS NET
	}

	@Override
	public AbsCannon getDraggedCannon() {
		return null; // NEVER EVER
	}

	@Override
	public double getGameSpeed() {
		return speed;
	}

	@Override
	public ImageCache getCache() {
		return cache;
	}
	
	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getDifficulty() {
		return difficulty;
	}

	@Override
	public void setGameSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public void restart() {
		getOwner().setLevel(new ServerGameLevel(getOwner(), getLevel() + 0, getDifficulty(), cache));
	}

	@Override
	public void next() {
		getOwner().setLevel(new ServerGameLevel(getOwner(), getLevel() + 1, getDifficulty(), cache));
	}
	
	@Override
	public void prev() {
		getOwner().setLevel(new ServerGameLevel(getOwner(), getLevel() - 1, getDifficulty(), cache));
	}

	@Override
	public void addVortex(int vx, int vy) {
		//nopf
	}

	@Override
	public void setBulletSpeedMult(int mlt) {
		//later
	}

	@Override
	public double getBulletSpeedMultiplikator() {
		// later
		return 1;
	}
}
