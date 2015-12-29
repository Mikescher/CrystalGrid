package gridShooter.game.level;

import gridShooter.Main;
import gridShooter.game.level.abstractLevel.AbsLevel;

import java.io.IOException;

import de.abscanvas.level.levelIO.LIOParsingException;
import de.abscanvas.level.levelIO.LIOReadOnlyMethod;
import de.abscanvas.level.levelIO.LevelReader;
import de.abscanvas.level.levelIO.LevelReaderIterator;
import de.abscanvas.level.levelIO.TextFileUtils;

public class LevelFileParser {
	private final static int METHOD_ADDCANNON = 1;
	private final static int METHOD_SETPLAYERCOUNT = 2;
	private final static int METHOD_ADDVOIDWALL = 3;
	private final static int METHOD_ADDVOIDPILLAR = 4;
	private final static int METHOD_ADDREFLECTWALL = 5;
	private final static int METHOD_ADDREFLECTPILLAR = 6;
	
	private final static int METHOD_ADDVORTEX = 9;
	private final static int METHOD_SETBULLETSPEEDMODIFICATOR = 10;
	
	private AbsLevel level;

	public LevelFileParser(AbsLevel l) {
		level = l;
	}

	public boolean loadFile(String filep) {
		try {
			String data = TextFileUtils.readTextFile(filep);
			return load(data);
		} catch (IOException e) {
			return false;
		}
	}

	public boolean loadRessource(String resp) {
		try {
			String data = TextFileUtils.readTextResource(resp, this.getClass());
			return load(data);
		} catch (IOException e) {
			return false;
		}
	}

	private boolean load(String data) {
		LevelReader reader = new LevelReader();
		
		LevelReaderIterator it;
		
		try {
			it = reader.parse(data, Main.GAME_TITLE);
		} catch (LIOParsingException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		while(it.hasMoreElements()) {
			LIOReadOnlyMethod m = it.nextElement();
			switch (m.getMethodname()) {
			case METHOD_ADDCANNON:
				level.addCannon(m.getParameter_i(0), m.getParameter_i(1), m.getParameter_i(2));
				break;
			case METHOD_SETPLAYERCOUNT:
				level.setPlayerCount(m.getParameter_i(0));
				break;
			case METHOD_ADDVOIDWALL:
				level.addVoidWall(m.getParameter_i(0), m.getParameter_i(1), m.getParameter_b(2), m.getParameter_i(3));
				break;
			case METHOD_ADDVOIDPILLAR:
				level.addVoidPillar(m.getParameter_i(0), m.getParameter_i(1));
				break;
			case METHOD_ADDREFLECTWALL:
				level.addReflectingWall(m.getParameter_i(0), m.getParameter_i(1), m.getParameter_b(2), m.getParameter_i(3));
				break;
			case METHOD_ADDREFLECTPILLAR:
				level.addReflectingPillar(m.getParameter_i(0), m.getParameter_i(1));
				break;
			case METHOD_ADDVORTEX:
				level.addVortex(m.getParameter_i(0), m.getParameter_i(1));
				break;
			case METHOD_SETBULLETSPEEDMODIFICATOR:
				level.setBulletSpeedMult(m.getParameter_i(0));
				break;
			default:
				System.out.println("Unknonwn Method parsed: " + m.getMethodname());
				return false;
			}
		}
		
		return true;
	}
}
