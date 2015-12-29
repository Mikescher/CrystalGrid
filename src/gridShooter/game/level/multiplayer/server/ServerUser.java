package gridShooter.game.level.multiplayer.server;

import java.net.InetAddress;

import de.abscanvas.network.NetworkUser;
import de.abscanvas.network.ServerAdapter;

public class ServerUser extends NetworkUser {
	public ServerUser(String name, InetAddress ip, int port, ServerAdapter k) {
		super(name, ip, port, k);
	}
}
