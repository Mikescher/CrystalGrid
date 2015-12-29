package gridShooter.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class HighscoreConnector {
	private final static int GAMEID = 6;
	private final static String BASEURL = "http://www.mikescher.com/Highscores/update.php";
	private final static String GAMESALT = "176e05";
	private final static String HIGHSCORE_URL = "http://www.mikescher.com/Highscores/list_top50.php?gameid=" + GAMEID;
	private final static String NEWID_URL = "http://www.mikescher.com/Highscores/getNewID.php?gameid=" + GAMEID;

	/**
	 * eine einzelne Zeile in der Highscore-Datenbank
	 */
	public class HighscoreEntry {
		public String name;
		public int score;
	}

	private ArrayList<HighscoreEntry> highscore;

	public HighscoreConnector() {
		//nopf
	}
	
	public void loadEmpty() {
		highscore = new ArrayList<HighscoreEntry>();
	}

	public void loadHighscore() {
		highscore = new ArrayList<HighscoreEntry>();
		String resultCode = "";
		try {
			URL url = new URL(HIGHSCORE_URL);
			HttpURLConnection conn;

			conn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println(conn.getResponseMessage());
			}
			InputStream s = conn.getInputStream();
			try {
				resultCode = streamToString(s);
			} catch (Exception e) {
				System.out.println(e);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		if (!resultCode.isEmpty()) {
			int c = 0;
			StringTokenizer toke2;
			HighscoreEntry tmp;
			String acTok;
			StringTokenizer tokenizer = new StringTokenizer(resultCode, "\n");
			while (tokenizer.hasMoreTokens()) {
				acTok = tokenizer.nextToken();
				toke2 = new StringTokenizer(acTok, "||");
				c = 0;
				tmp = new HighscoreEntry();
				while (toke2.hasMoreTokens()) {
					c++;
					if (c == 2) {
						tmp.name = toke2.nextToken();
					} else if (c == 1) {
						tmp.score = Integer.parseInt(toke2.nextToken());
					}
				}
				highscore.add(tmp);
			}
		}
	}
	
	public String streamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer bw = new StringWriter();

			char[] buffer = new char[1024];
			Reader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = br.read(buffer)) != -1) {
				bw.write(buffer, 0, n);
			}
			return bw.toString();
		} else {
			throw new IOException();
		}
	}

	public ArrayList<HighscoreEntry> get() {
		return highscore;
	}

	public ArrayList<String> getFormated() {
		ArrayList<String> s = new ArrayList<String>();
		String sco;
		for (int i = 0; i < highscore.size(); i++) {
			sco = (highscore.get(i).score) + "";
			while (sco.length() < 7) {
				sco = "0" + sco;
			}
			s.add("(" + sco + ")  " + highscore.get(i).name);
		}
		return s;
	}

	/**
	 * berechnet den MD5-Hash eines Strings in Kompatibilität mit PHP
	 * 
	 * @author Sergiy Kovalchuk (http://www.sergiy.ca/how-to-make-java-md5-and-sha-1-hashes-compatible-with-php-or-mysql/)
	 */
	private String php_md5(String input) throws NoSuchAlgorithmException {
		String result = input;
		if (input != null) {
			MessageDigest md = MessageDigest.getInstance("MD5"); // or "SHA-1"
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			if ((result.length() % 2) != 0) {
				result = "0" + result;
			}
		}
		return result;
	}
	
	public int getNewID() {
		try {
			URL url = new URL(NEWID_URL);
			HttpURLConnection conn;

			conn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println(conn.getResponseMessage());
			}
			
			int nid = Integer.parseInt(streamToString(conn.getInputStream()));
			
			return nid;
		} catch (Exception e) {
			System.out.println(e);
		}	

		return -1;
	}

	public void updateHighscore(String name, int points, int id) {
		if (name == null || id < 0 || points <= 0) return;

		String RND_DECODE = name;
		String rand = "";
		Random r = new Random();
		r.nextInt(RND_DECODE.length());
		
		for (int i = 1; i <= 8; i++) {
			rand += RND_DECODE.charAt(r.nextInt(RND_DECODE.length()));
		}

		String temp = rand + name + id + points + GAMESALT;

		String check = "";
		try {
			check = php_md5(temp);
		} catch (Exception e) {
			System.out.println(e);
		}

		String mURL = BASEURL + "?gameid=" + GAMEID + "&name=" + name + "&points=" + points + "&rand=" + rand + "&check=" + check + "&nameid=" + id;

		try {
			URL url = new URL(mURL);
			HttpURLConnection conn;

			conn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println(conn.getResponseMessage());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
