package cz.eeg;

import static cz.deznekcz.tool.Lang.LANGload;
import cz.eeg.ui.GuiManager;

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
				+"Zdeněk Novotný, Michal Sakáč, Václav Trykar, Václav Zoubek";
	
	
	/** Method main, runs an {@link Application} */
	public static void main(String[] args) {
		GuiManager.start();
	}
}