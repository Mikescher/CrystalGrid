package gridShooter.game;

import gridShooter.game.level.singleplayer.GameLevel;
import de.abscanvas.input.Art;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;

public class GameArt extends Art {
	public static Bitmap[][] font;
	
	public static Bitmap level_background;
	
	public static Bitmap[][] cannon; // [STATE][ROTATION(64)]
	public static Bitmap[][] cannonProgressMarker; // [Progress(64)][Rotation(64)]
	public static Bitmap bullet;
	
	public static Bitmap[] btn_start;
	public static Bitmap[] btn_starting;
	public static Bitmap[] btn_play;
	public static Bitmap[] btn_lan;
	public static Bitmap[] btn_credits;
	public static Bitmap[] btn_exit;
	public static Bitmap[] btn_level;
	public static Bitmap[] btn_back;
	public static Bitmap[] btn_refresh;
	public static Bitmap[] btn_plus;
	public static Bitmap[] btn_minus;
	public static Bitmap[] btn_updateAvailable;
	public static Bitmap[] btn_highscore;
	
	public static Bitmap[] btn_reset;
	public static Bitmap[] btn_set;
	
	public static Bitmap[] btn_diff_0;
	public static Bitmap[] btn_diff_1;
	public static Bitmap[] btn_diff_2;
	public static Bitmap[] btn_diff_3;
	public static Bitmap[] btn_diff_4;
	
	public static Bitmap[] hud_cb_x1;
	public static Bitmap[] hud_cb_x2;
	public static Bitmap[] hud_cb_x4;

	public static Bitmap cover;
	public static Bitmap cover_small;
	
	public static Bitmap hud_pMenu_Background;
	
	public static Bitmap[] hud_pMenu_resume;
	public static Bitmap[] hud_pMenu_retry;
	public static Bitmap[] hud_pMenu_exit;
	
	public static Bitmap[] hud_w_next;
	public static Bitmap[] hud_w_retry;
	public static Bitmap[] hud_w_exit;
	
	public static Bitmap[] disp_won;

	public static Bitmap[] wall;
	public static Bitmap pillar;
	public static Bitmap blackhole;
	
	public static Bitmap[] wall_reflect;
	public static Bitmap pillar_reflect;

	public static void loadMinimal() {
		btn_start = cutY("btn_start.png", 4);
		btn_starting = cutY("btn_starting.png", 4);
		
		cover = load("cover.png");
	}
	
	public static void loadMenu() {
		font = cut("font.png", 30, 2);
		cover_small = load("cover_small.png");
		
		btn_play = cutY("btn_play.png", 4);
		btn_lan = cutY("btn_lan.png", 4);
		btn_credits = cutY("btn_credits.png", 4);
		btn_exit = cutY("btn_exit.png", 4);
		btn_level = cutY("btn_level.png", 4);
		btn_back = cutY("btn_back.png", 4);
		btn_refresh = cutY("btn_refresh.png", 4);
		btn_plus = cutY("btn_plus.png", 4);
		btn_minus = cutY("btn_minus.png", 4);
		btn_updateAvailable = cutY("btn_update.png", 4);
		btn_highscore = cutY("btn_highscore.png", 4);
		
		btn_diff_0 = cutY("btn_diff_0.png", 4);
		btn_diff_1 = cutY("btn_diff_1.png", 4);
		btn_diff_2 = cutY("btn_diff_2.png", 4);
		btn_diff_3 = cutY("btn_diff_3.png", 4);
		btn_diff_4 = cutY("btn_diff_4.png", 4);
		
		btn_reset = cutY("btn_reset.png", 4);
		btn_set = cutY("btn_set.png", 4);
	}
	
	public static void loadGame() {
		bullet = load("bullet.png");
		level_background = load("background.png");

		wall = new Bitmap[2];
		wall[0] = load("wall_h.png");
		wall[1] = load("wall_v.png");
		
		pillar = load("pillar.png");
		blackhole = load("blackhole.png");
		
		wall_reflect = new Bitmap[2];
		wall_reflect[0] = load("r_wall_h.png");
		wall_reflect[1] = load("r_wall_v.png");
		
		pillar_reflect = load("r_pillar.png");
		
		hud_pMenu_Background = load("p_menu.png");
		
		hud_pMenu_resume = cutY("btn_p_resume.png", 4);
		hud_pMenu_retry = cutY("btn_p_retry.png", 4);
		hud_pMenu_exit = cutY("btn_p_exit.png", 4);
		
		hud_w_next = cutY("btn_win_next.png", 4);
		hud_w_exit = cutY("btn_win_exit.png", 4);
		hud_w_retry = cutY("btn_win_retry.png", 4);
		
		disp_won = new Bitmap[4];
		
		disp_won[0] = load("disp_won_p1.png");
		disp_won[1] = load("disp_won_p2.png");
		disp_won[2] = load("disp_won_p3.png");
		disp_won[3] = load("disp_won_p4.png");
		
		hud_cb_x1 = cutY("cb_x1.png", 3);
		hud_cb_x2 = cutY("cb_x2.png", 3);
		hud_cb_x4 = cutY("cb_x4.png", 3);
	}
	
	public static void init() {		
		// ---------------------------------  CANNON  ---------------------------------
		
		Bitmap[][] canngrid;// = new Bitmap[4][8];
		cannon = new Bitmap[7][64];
		canngrid = cut("cannon.png", 4, 8);	
		
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 7; j++) {
				cannon[j][i] = canngrid[0][j + 1].copy();
				cannon[j][i].blit(canngrid[0][0], 0, 0);
				
				cannon[j][i] = cannon[j][i].rotate((2*Math.PI) * (i/64.0));
			}
		}
		
		// ---------------------------------  MARKER  ---------------------------------

		cannonProgressMarker = new Bitmap[64][64];
		Bitmap[] dots = cut1D("p_marker.png", 8, 8);
		
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				cannonProgressMarker[i][j] = dots[i].copy().rotate((2*Math.PI) * (j/64.0));
				cannonProgressMarker[i][j].replaceColor(AbsColor.BLACK, GameLevel.NEUTRAL_COLOR);
			}
		}
	}
}
