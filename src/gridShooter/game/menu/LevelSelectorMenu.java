package gridShooter.game.menu;

import gridShooter.Main;
import gridShooter.game.GameArt;
import gridShooter.game.GameScreen;

import java.awt.Font;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MRect;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUINativeLabel;
import de.abscanvas.ui.listener.ButtonListener;

public class LevelSelectorMenu extends GUIMenu implements ButtonListener {
	private final static int BTN_NEXT = -10;
	private final static int BTN_PREV = -11;
	private final static int BTN_BACK = -12;

	private GameScreen owner;

	private GUIButton buttons[] = new GUIButton[8];

	private GUIButton btnBack;
	
	private GUINativeLabel lblScore;

	private static MRect lvl_btn_ki_rects_1 = new MRect(12, 193, 61, 243);
	private static MRect lvl_btn_ki_rects_2 = new MRect(72, 196, 122, 244);
	private static MRect lvl_btn_ki_rects_3 = new MRect(133, 193, 182, 243);
	private static MRect lvl_btn_ki_rects_4 = new MRect(195, 194, 240, 242);

	private int page;

	private int firstlvl;
	private int lastlvl;

	public LevelSelectorMenu(GameScreen owner, int page) {
		super(owner);
		this.owner = owner;
		this.page = page;
		firstlvl = page * 6 + 1;
		lastlvl = firstlvl + 5;

		create();
	}

	private void create() {
		setImage(GameArt.cover_small);

		btnBack = new GUIButton(32, getOwner().getScreenHeight() - 67, GameArt.btn_back, BTN_BACK, this);
		btnBack.addListener(this);
		addElement(btnBack);

		for (int b = 0; b < 8; b++) {
			int x;
			int y;
			int lvl = firstlvl + b - 1;

			if (lvl > Main.MAX_RES_LVL)
				continue;

			x = 74 + (b % 4) * 333;
			y = 264 + (b / 4) * 400;

			buttons[b] = createLvlButton(lvl, x, y, owner.getPFile().check(lvl, 0), owner.getPFile().check(lvl, 1), owner.getPFile().check(lvl, 2), owner.getPFile().check(lvl, 3), b == 0, b == 7);
			buttons[b].addListener(this);
			addElement(buttons[b]);
		}
		
		lblScore = new GUINativeLabel(1000, 960, "Score: " + owner.getPFile().getScore(), this);
		addElement(lblScore);
	}

	private GUIButton createLvlButton(int lvl, int x, int y, boolean ki1, boolean ki2, boolean ki3, boolean ki4, boolean first, boolean last) {
		Bitmap[] bimga = new Bitmap[4];

		bimga[0] = GameArt.btn_level[0].copy();
		bimga[1] = GameArt.btn_level[1].copy();
		bimga[2] = GameArt.btn_level[2].copy();
		bimga[3] = GameArt.btn_level[3].copy();

		int fl_MinLevel;
		int fl_MaxLevel;

		boolean visible;

		GUIButton b;

		if (first) {
			fl_MinLevel = Math.max((firstlvl - 6), Main.MIN_RES_LVL);
			fl_MaxLevel = Math.max((lastlvl - 6), Main.MIN_RES_LVL);
		} else if (last) {
			fl_MinLevel = Math.min((firstlvl + 6), Main.MAX_RES_LVL);
			fl_MaxLevel = Math.min((lastlvl + 6), Main.MAX_RES_LVL);
		} else {
			fl_MinLevel = 999;
			fl_MaxLevel = 999;
		}

		if (first) {
			visible = !((firstlvl - 6) < Main.MIN_RES_LVL && (lastlvl - 6) < Main.MIN_RES_LVL);
		} else if (last) {
			visible = !((firstlvl + 6) > Main.MAX_RES_LVL && (lastlvl + 6) > Main.MAX_RES_LVL);
		} else {
			visible = true;
		}

		if (visible) {
			if (first) {
				drawLevelString(bimga, fl_MinLevel + "-" + fl_MaxLevel);
			} else if (last) {
				drawLevelString(bimga, fl_MinLevel + "-" + fl_MaxLevel);
			} else {
				drawLevelString(bimga, lvl + "");
			}

			if (first) {
				fillKIMarker(bimga, hasAllKI(fl_MinLevel, fl_MaxLevel, 0), hasAllKI(fl_MinLevel, fl_MaxLevel, 1), hasAllKI(fl_MinLevel, fl_MaxLevel, 2), hasAllKI(fl_MinLevel, fl_MaxLevel, 3));
			} else if (last) {
				fillKIMarker(bimga, hasAllKI(fl_MinLevel, fl_MaxLevel, 0), hasAllKI(fl_MinLevel, fl_MaxLevel, 1), hasAllKI(fl_MinLevel, fl_MaxLevel, 2), hasAllKI(fl_MinLevel, fl_MaxLevel, 3));
			} else {
				fillKIMarker(bimga, ki1, ki2, ki3, ki4);
			}
		}

		if (first) {
			b = new GUIButton(x, y, bimga, BTN_PREV, this);
		} else if (last) {
			b = new GUIButton(x, y, bimga, BTN_NEXT, this);
		} else {
			b = new GUIButton(x, y, bimga, lvl, this);
		}

		b.setVisible(visible);

		return b;
	}
	
	private boolean hasAllKI(int start, int end, int difficulty) {
		boolean ret = true;
		for (int i = start; i <= end; i++) {
			ret &= owner.getPFile().check(i, difficulty);
		}
		
		return ret;
	}

	private void drawLevelString(Bitmap[] b, String s) {
		for (int j = 0; j < 4; j++) {
			b[j].drawStringCentered(s, new Font("Arial", 0, 72), new MRect(0, 0, 256, 256), AbsColor.WHITE);
		}
	}

	private void fillKIMarker(Bitmap[] b, boolean ki1, boolean ki2, boolean ki3, boolean ki4) {
		for (int j = 0; j < 4; j++) {
			if (!ki1) { // Hasen't Already KI EASY
				b[j].fill(lvl_btn_ki_rects_1, AbsColor.TRANSPARENT);
			}
			if (!ki2) { // Hasen't Already KI MIDDLE
				b[j].fill(lvl_btn_ki_rects_2, AbsColor.TRANSPARENT);
			}
			if (!ki3) { // Hasen't Already KI HARD
				b[j].fill(lvl_btn_ki_rects_3, AbsColor.TRANSPARENT);
			}
			if (!ki4) { // Hasen't Already KI IMPOSSIBLE
				b[j].fill(lvl_btn_ki_rects_4, AbsColor.TRANSPARENT);
			}
		}
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
	public void buttonPressed(MouseButtons arg0, int id) {
		if (id == BTN_NEXT) {
			owner.popMenu();
			owner.addMenu(new LevelSelectorMenu(owner, page + 1));
		} else if (id == BTN_PREV) {
			owner.popMenu();
			owner.addMenu(new LevelSelectorMenu(owner, page - 1));
		} else if (id == BTN_BACK) {
			owner.popMenu();
		} else {
			owner.addMenu(new DifficultySelectorMenu(owner, id));
		}
	}

}
