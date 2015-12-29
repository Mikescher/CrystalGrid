package gridShooter.game;

import gridShooter.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class UpdateConnector {
	private final static String HIGHSCORE_URL = "http://www.mikescher.de/update.php?Name=CrystalGrid";
	
	private String updateURL;
	private String updateVersion;
	private boolean updateAvailable;
	private String updateName;
	
	public UpdateConnector() {
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
		
		if (resultCode == null || resultCode.isEmpty()) {
			updateName = Main.GAME_TITLE;
			updateVersion = Main.VERSION;
			updateAvailable = false;
			updateURL = "";
			System.out.println("Could not recieve Update-Version");
			return;
		}
		
		updateName = resultCode.split("<hr>")[0];
		updateVersion = resultCode.split("<hr>")[1];
		updateURL = resultCode.split("<hr>")[2];
		updateAvailable = ! Main.VERSION.equals(updateVersion);
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
	
	public boolean updateAvaiable() {
		return updateAvailable;
	}
	
	public String updateURL() {
		return updateURL;
	}
	
	public String updateName() {
		return updateName;
	}
	
	public void openURL() {
		if (updateAvailable) {
			openInBrowser(updateURL);
		}
	}
	
	public static void openInBrowser(String url) {
		if (java.awt.Desktop.isDesktopSupported()) {
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			java.net.URI uri;
			try {
				uri = new java.net.URI(url);
				desktop.browse(uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Destkop not supported");
		}
	}
}
