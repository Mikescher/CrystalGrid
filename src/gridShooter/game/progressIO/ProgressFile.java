package gridShooter.game.progressIO;

import gridShooter.game.GameScreen;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import de.abscanvas.level.levelIO.TextFileUtils;

public class ProgressFile {
	private ArrayList<ProgressElement> elements = new ArrayList<>();
	
	private String path;
	
	private String username = null;
	private int userid = -1;
	
	public boolean load(String path) {
		elements.clear();
		
		this.path = path;
		try {
			String txt = decrypt(TextFileUtils.readTextFile(path));
			
			if (txt == null) return false;
			
			String[] split = txt.split("\n");
			
			if (split.length < 2) {
				return false;
			}
			
			username = split[0];
			try {
				userid = Integer.parseInt(split[1]);
			} catch (NumberFormatException e) {
				System.out.println("Could not parse - NumberFormat I");
				return false;
			}
			
			for (int i = 2; i < split.length; i++) {
				String s = split[i];
				
				if(s.length() >= 3) {
					String[] ss = s.split(";");
					if (ss.length == 2) {
						try {
							int nlvl = Integer.parseInt(ss[0].trim());
							int ndiff = Integer.parseInt(ss[1].trim());
							
							if (! check(nlvl, ndiff)) {
								elements.add(new ProgressElement(nlvl, ndiff));
							}
						} catch (NumberFormatException e) {
							System.out.println("Could not parse - NumberFormat II");
							return false;
						}
					} else {
						System.out.println("Could not parse - missing semikolon");
						return false;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Could not parse - Exception: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean check(int level, int difficulty) {
		for (ProgressElement pe : elements) {
			if (pe.level == level && pe.difficulty == difficulty) {
				return true;
			}
		}

		return false;
	}
	
	private String hash(String plainText) {
		byte messageDigest[];
		try {
			messageDigest = MessageDigest.getInstance("MD5").digest(plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<messageDigest.length;i++) {
			String hex = Integer.toHexString(0xFF & messageDigest[i]).toUpperCase();
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	private String encrypt(String plainText) {
		String hash = hash(plainText);
		if (hash == null) return null;
		
		String fulltext = hash + plainText;
		
		char[] txt = fulltext.toCharArray();
		
		for (int i = 0; i < txt.length; i++) {
			txt[i] += 32 + (i%8);
		}
		
		String ect = new String(txt);
		
		return ect;
	}

	private String decrypt(String plainText) {
		if (plainText.length() < (32)) return null;
		
		char[] txt = plainText.toCharArray();
		for (int i = 0; i < txt.length; i++) {
			txt[i] -= 32 + (i%8);
		}
		
		String fullencrypted = new String(txt);
		
		String encrypted = fullencrypted.substring(32);
		String hash = fullencrypted.substring(0, 32);
		
		String newHash = hash(encrypted);
		
		if (newHash.equals(hash)) {
			return encrypted;
		} else {
			return "";
		}
	}
	
	public void add(int level, int difficulty, GameScreen screen) {
		if (check(level, difficulty)) return;
		elements.add(new ProgressElement(level, difficulty));
		update(screen);
		save();
	}
	
	private void update(GameScreen screen) {
		screen.getHighscore().updateHighscore(screen.getPFile().getUsername(), screen.getPFile().getScore(), screen.getPFile().getUserID());
	}
	
	public String getFileText() {
		if (! nameIsSet()) return "";
		
		String txt = "";
		
		txt += getUsername();
		txt += "\n";
		txt += getUserID()+"";
		
		for (ProgressElement pe : elements) {
			txt += "\n";
			txt += pe.level + ";" + pe.difficulty;
		}
		
		return encrypt(txt);
	}
	
	public boolean saveAs(String path) {
		try {
			TextFileUtils.writeTextFile(path, getFileText());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean save() {
		return saveAs(path);
	}

	public int getUserID() {
		return userid;
	}

	public String getUsername() {
		return username.toUpperCase();
	}
	
	public boolean nameIsSet() {
		return username != null;
	}
	
	public boolean isOffline() {
		return nameIsSet() && userid == -1;
	}

	public void setNameAndID(String u, int i) {
		username = u.toUpperCase();
		userid = i;
		save();
	}
	
	public int getScore() {
		int sc = 0;
		
		int cDiff0 = 0;
		int cDiff1 = 0;
		int cDiff2 = 0;
		int cDiff3 = 0;
		
		for (ProgressElement e : elements) {
			switch (e.difficulty) {
			case 0:
				cDiff0++;
				break;
			case 1:
				cDiff1++;
				break;
			case 2:
				cDiff2++;
				break;
			case 3:
				cDiff3++;
				break;
			}
		}
		
		sc += cDiff0 * 1;
		sc += cDiff1 * 2;
		sc += cDiff2 * 4;
		sc += cDiff3 * 8;
		
		return sc;
	}

	public void reset() {
		username = null;
		userid = -1;
		elements.clear();
		save();
	}
}
