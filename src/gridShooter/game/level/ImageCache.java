package gridShooter.game.level;

import gridShooter.game.GameArt;
import gridShooter.game.level.abstractLevel.AbsLevel;
import gridShooter.game.level.player.Player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import de.abscanvas.math.MPoint;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;

public class ImageCache {
	private final static int TARGETER_WIDTH = 3;
	private final static int TARGETER_OFFSET = 64;
	
	private final static float DASH[] = {32.0f};
	private final static BasicStroke STROKE_DASHED = new BasicStroke(TARGETER_WIDTH, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, DASH, 0.0f);
	
	private static HashMap<Integer, Bitmap> bullet_cache = new HashMap<Integer, Bitmap>();

	public Bitmap getBulletImage(Player p) {
		return getBulletImage(p.getPID());
	}
	
	public Bitmap getBulletImage(int pid) {
		Bitmap r = bullet_cache.get(pid);
		if (r == null) {
			r = GameArt.bullet.copy();
			if (pid == -1) {
				r.replaceColor(AbsColor.BLACK, AbsLevel.NEUTRAL_COLOR);
			} else {
				r.replaceColor(AbsColor.BLACK, AbsLevel.COLORS[pid]);
			}
			
			bullet_cache.put(pid, r.copy());
			return r;
		} else {
			return r;
		}
	}
	
	public Bitmap getTargeterImage(MPoint p) {
		int w = Math.abs(p.getX() * 2) + TARGETER_WIDTH;
		int h = Math.abs(p.getY() * 2) + TARGETER_WIDTH;
		
		BufferedImage i = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D d= i.createGraphics();
		
		d.setStroke(STROKE_DASHED);
		
		MPoint k = new MPoint(p);
		k.setLength(TARGETER_OFFSET);
		
		d.setColor(new Color(AbsColor.WHITE));
		
		d.drawLine(w/2 + k.getX(), h/2 + k.getY(),  w/2 + p.getX(), h/2 + p.getY());
		
		Bitmap b = new Bitmap(w, h);
		b.setImage(i);
		
		return b;
	}
}
