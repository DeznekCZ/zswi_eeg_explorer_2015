package cz.eeg;

import static cz.deznekcz.tool.Lang.LANGload;
import static cz.deznekcz.tool.Lang.LANGgenerate;

import java.io.IOException;

import cz.eeg.ui.GuiManager;
import cz.eeg.ui.explorer.DirectoryBrowserPanel;

/**
 * Running class of EEG Explorer
 * 
 * @author IT Crowd
 */
public class Application {

	//TODO Configuration
	/** Configuration instance of class {@link Config} */
	public final static Config CONFIG = new Config();
	
	static {
		LANGload(CONFIG.lang);
	}
	
	/** Authors */
	public final static String AUTHOR = "IT Crowd\n"
				+"Zdeněk Novotný, Michal Sakáč, Václav Zoubek";
	
	
	/** Method main, runs an {@link Application} */
	public static void main(String[] args) {
		GuiManager.start();
	}

	/** Method exits an application */
	public static void exit() {
		LANGgenerate();
		try {
			CONFIG.folder_output = DirectoryBrowserPanel.PANEL.getCurrentDirectory().getAbsolutePath();
		} catch (IOException e) {/* Does non need */}
		CONFIG.save();
		
		System.exit(0);
	}
}