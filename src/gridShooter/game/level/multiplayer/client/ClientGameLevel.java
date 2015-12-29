package gridShooter.game.level.multiplayer.client;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.GameScreen;
import gridShooter.game.level.ImageCache;
import gridShooter.game.level.multiplayer.NetworkGameConstants;
import gridShooter.game.level.multiplayer.client.entities.Bullet;
import gridShooter.game.level.multiplayer.client.entities.Cannon;
import gridShooter.game.level.multiplayer.client.entities.ReflectingPillar;
import gridShooter.game.level.multiplayer.client.entities.ReflectingWall;
import gridShooter.game.level.multiplayer.client.entities.VoidPillar;
import gridShooter.game.level.multiplayer.client.entities.VoidWall;

import java.net.InetAddress;

import de.abscanvas.Screen;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.ClientLevel;
import de.abscanvas.level.tile.EmptyTile;
import de.abscanvas.math.ByteUtilities;

public class ClientGameLevel extends ClientLevel {
	public final static byte[] IDENTIFIER = { 4, 42 };
	
	public ClientGameLevel(Screen o, String username, InetAddress server_ip, int port) {
		super(o, Main.TILE_WIDTH, Main.TILE_HEIGHT, username, server_ip, port, IDENTIFIER);
		
		setBackgroundAlignement(ALIGN_CENTER);
		setBackgroundImage(GameArt.level_background);
		
		registerTile(EmptyTile.class, 0);
		
		registerEntity(Bullet.class, 1);
		registerEntity(Cannon.class, 2);
		registerEntity(ReflectingPillar.class, 3);
		registerEntity(ReflectingWall.class, 4);
		registerEntity(VoidPillar.class, 5);
		registerEntity(VoidWall.class, 6);
		
		connect();
	}

	@Override
	public void onClientKicked() {
		System.out.println("Connection kicked");
	}

	@Override
	public void onConnectionLost() {
		System.out.println("Connection lost");
	}

	@Override
	public int onCustomClientCommand(byte packageID, int commandID, byte[] data) {
		System.out.println("ok:"+commandID);
		if (commandID == NetworkGameConstants.CMD_SETCANNONINFO) {
			long uid = ByteUtilities.arr2Long(data, 0);
			int pid = ByteUtilities.arr2Int(data, 8);
			LevelEntity cann = getEntity(uid);
			if (cann instanceof Cannon) {
				((Cannon)cann).setPlayer(pid);
			} else {
				System.out.println("C: ERROR - Cannon UID is not UID from Cannon !?!");
			}
			return 12;
		} else if (commandID == NetworkGameConstants.CMD_SETBULLETINFO) {
			long uid = ByteUtilities.arr2Long(data, 0);
			int pid = ByteUtilities.arr2Int(data, 8);
			LevelEntity cann = getEntity(uid);
			if (cann instanceof Bullet) {
				((Bullet)cann).setPlayer(pid);
			} else {
				System.out.println("C: ERROR - Bullet UID is not UID from Bullet !?!");
			}
			return 12;
		} else if (commandID == NetworkGameConstants.CMD_SETRWALLINFO) {
			long uid = ByteUtilities.arr2Long(data, 0);
			int wid = ByteUtilities.arr2Int(data, 8);
			boolean horz = ByteUtilities.arr2Boolean(data, 12);
			LevelEntity cann = getEntity(uid);
			if (cann instanceof ReflectingWall) {
				((ReflectingWall)cann).setInfo(wid, horz);
			} else {
				System.out.println("C: ERROR - ReflectingWall UID is not UID from ReflectingWall !?!");
			}
			return 13;
		} else if (commandID == NetworkGameConstants.CMD_SETVWALLINFO) {
			long uid = ByteUtilities.arr2Long(data, 0);
			int wid = ByteUtilities.arr2Int(data, 8);
			boolean horz = ByteUtilities.arr2Boolean(data, 12);
			LevelEntity cann = getEntity(uid);
			if (cann instanceof VoidWall) {
				((VoidWall)cann).setInfo(wid, horz);
			} else {
				System.out.println("C: ERROR - VoidWall UID is not UID from VoidWall !?!");
			}
			return 13;
		} else {
			System.out.println("wud ?"+commandID);
			return 0;
		}
	}

	@Override
	public void onHUDPressed(MouseButtons arg0) {
		// Do nothing

	}

	@Override
	public void onMouseMove(MouseButtons arg0) {
		// Do nothing

	}

	@Override
	public void onPressed(MouseButtons arg0) {
		// Do nothing

	}

	@Override
	public void onTilePressed(MouseButtons arg0) {
		// Do nothing

	}

	public ImageCache getCache() {
		return ((GameScreen) getOwner()).getCache();
	}
}
