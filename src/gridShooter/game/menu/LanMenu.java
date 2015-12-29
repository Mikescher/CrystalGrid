package gridShooter.game.menu;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.level.multiplayer.client.ClientGameLevel;
import gridShooter.game.level.multiplayer.server.ServerWindow;

import java.awt.Font;

import de.abscanvas.Screen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.network.NetworkPingScanner;
import de.abscanvas.network.NetworkScanner;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUIEdit;
import de.abscanvas.ui.gui.elements.GUIProgressbar;
import de.abscanvas.ui.gui.elements.GUIServerList;
import de.abscanvas.ui.listener.ButtonListener;
import de.abscanvas.ui.listener.SelectionListener;

public class LanMenu extends GUIMenu implements ButtonListener, SelectionListener, Runnable {
	private final static int BTN_JOIN = 1;
	private final static int BTN_HOST = 2;
	private final static int BTN_BACK = 3;
	private final static int BTN_REFRESH = 4;
	private final static int LST_SERVERLIST = 5;
	
	private GUIButton btn_join;
	private GUIButton btn_host;
	private GUIButton btn_back;
	private GUIButton btn_refresh;
	
	private GUIEdit ed_name;
	
	private GUIServerList serverlist;
	private NetworkPingScanner ping_updater;
	
	private GUIProgressbar progressbar;
	
	public LanMenu(Screen owner) {
		super(owner);
		create();
	}
	
	private void create() {
		setImage(GameArt.cover_small);
		
		btn_join = new GUIButton(740, 850, GameArt.btn_lan, BTN_JOIN, this);
		btn_host = new GUIButton(204, 850, GameArt.btn_lan, BTN_HOST, this);	
		btn_back = new GUIButton(32, getOwner().getScreenHeight() - 67, GameArt.btn_back, BTN_BACK, this);
		btn_refresh = new GUIButton(1204, 200, GameArt.btn_refresh, BTN_REFRESH, this);	
		serverlist = new GUIServerList(204, 200, 1000, 600, LST_SERVERLIST, Main.VERSION, this);
		progressbar = new GUIProgressbar(204, 810, 475, 32, this);
		ed_name = new GUIEdit(729, 810, 475, 32, getOwner().getKeys(), this);
		
		btn_join.addListener(this);
		btn_host.addListener(this);
		btn_back.addListener(this);
		btn_refresh.addListener(this);
		serverlist.addListener(this);
		
		addElement(btn_join);
		addElement(btn_host);
		addElement(btn_back);
		addElement(btn_refresh);
		addElement(serverlist);
		addElement(progressbar);
		addElement(ed_name);
		
		ping_updater = new NetworkPingScanner(serverlist, ClientGameLevel.IDENTIFIER);
		
		ed_name.setFont(new Font("Arial", 0, 26));
		ed_name.setText(System.getProperty("user.name"));
		serverlist.setBigFont(new Font("Arial", Font.PLAIN, 40));
		serverlist.setSmallFont(new Font("Arial", Font.PLAIN, 24));
		serverlist.setMinorFont(new Font("Arial", Font.PLAIN, 20));
	}
	
	@Override
	public void tick() {
		super.tick();
		ping_updater.tick();
	}

	@Override
	public void itemDoubleClicked(int arg0, int arg1) {
		joinSelected();
	}

	@Override
	public void itemSelected(int arg0, int arg1) {
		// nothing
	}

	@Override
	public void buttonMouseDown(MouseButtons arg0, int arg1) {
		// nothing
	}

	@Override
	public void buttonMouseEnter(MouseButtons arg0, int arg1) {
		// nothing
	}

	@Override
	public void buttonMouseLeave(MouseButtons arg0, int arg1) {
		// nothing
	}

	@Override
	public void buttonPressed(MouseButtons mouse, int id) {
		switch (id) {
		case BTN_JOIN:
			joinSelected();
			break;
		case BTN_HOST:
			new ServerWindow(Main.GAME_TITLE + " - Server");
			break;
		case BTN_BACK:
			getOwner().popMenu();
			break;
		case BTN_REFRESH:
			(new Thread(this)).start();
			break;
		}
	}

	@Override
	public void run() {
		btn_refresh.setEnabled(false);
		
		NetworkScanner nscan = new NetworkScanner(serverlist, progressbar, ClientGameLevel.IDENTIFIER);
		nscan.scan("localhost");
		btn_refresh.setEnabled(true);
	}
	
	private void joinSelected() {
		getOwner().clearMenus();
		getOwner().setLevel(new ClientGameLevel(getOwner(), ed_name.getText(), serverlist.getSelectedServer().getIP(), serverlist.getSelectedServer().getPort()));
	}
}
