package gridShooter;

public class Main {
	public static boolean DEBUG = true;
	public static boolean MENUTEST = false;
	
	public static final String VERSION = "1.2";
	
	public static final String GAME_TITLE = "Crystal Grid"; // Dont change without thinking - used 4 Level-Files
	
	public static final String CONFIG_NAME = "crystalgrid.sav";
	
	public static final int TILE_WIDTH = 128;
	public static final int TILE_HEIGHT = 128;
	
	public static final int LEVEL_WIDTH = 11;
	public static final int LEVEL_HEIGHT = 8;
	
	public static final int GAME_WIDTH = LEVEL_WIDTH * TILE_WIDTH;
	public static final int GAME_HEIGHT = LEVEL_HEIGHT * TILE_HEIGHT;
	
	public static final double MIN_GAME_SCALE = 0.25;
	public static final double GAME_SCALE = 0.75;
	public static final double MAX_GAME_SCALE = 1;
	
	public static final int MIN_RES_LVL = 1;
	public static final int MAX_RES_LVL = 38;

	public static void main(String[] args) {
		new MainFrame();		
	}
}