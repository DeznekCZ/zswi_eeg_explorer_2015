package cz.eeg;

import static cz.deznekcz.tool.Lang.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;

import javax.swing.JFrame;

import cz.eeg.tool.Config;
//import cz.eeg.tool.Lang;
import cz.eeg.tool.Ovladac;
import cz.eeg.ui.Editor;
import cz.eeg.ui.Prohlizec;
import cz.eeg.ui.Vyber;

/**
 * Spouštěcí třída aplikace<br><br>
 * aplikace nemá zatím žádné spouštěcí parametry
 * 
 * @author IT Crowd
 */
public class Aplikace extends JFrame {
	
	//TODO Inicializace nastavení
	/** Konfigurační instance třídy {@link Config} */
	public final static Config CONFIG = new Config();
	
	static {
		LANGload(CONFIG.lang);
	}
	
	/** Konstanta s autorským podpisem */
	public final static String AUTHOR = "\n\nIT Crowd\n"
				+"Zdeněk Novotný, Michal Sakáč, Vaclav Trykar, Václav Zoubek";
	
	//TODO Hlavní okno
	/** Hlavní okno aplikace */
	public final static Aplikace OKNO = new Aplikace();
	/** Dělený panel v oknu {@link Aplikace} */
	public final static Prohlizec PROHLIZEC = new Prohlizec();
	/** Okno editoru, pro správu {@code JFrame} je nutno volat Editor.OKNO */
	public final static Editor EDITOR = new Editor();
	/** API pro zjštění stisku klávesy */
	public static KeyboardFocusManager manager =
	         KeyboardFocusManager.getCurrentKeyboardFocusManager();
	
	//TODO Vybraný výběr
	/** Aktuální používané okno pro výběr souboru */
	public static Vyber oknoVyberu = null;

	/** Promnná potřebná pro zjištění, zda se aplikace ukončuje*/
	private static boolean ukoncovani;
	
	/** Hlavní metoda aplikace */
	public static void main(String[] args) {
		OKNO.spustit();

		manager.addKeyEventDispatcher( new Ovladac() );
	}

	//TODO Okno aplikace
	/** Zapíná samotné okno {@link Aplikace} */
	public void spustit() {
		
		setSize(new Dimension(CONFIG.ex_width, CONFIG.ex_height));
		if (CONFIG.isSet()) {
			setLocation(CONFIG.ex_posx, CONFIG.ex_posy);
			if ((CONFIG.ex_fullscreen & JFrame.MAXIMIZED_BOTH) != JFrame.MAXIMIZED_BOTH) {
				setPreferredSize(new Dimension(CONFIG.ex_width, CONFIG.ex_height));
			}
			setExtendedState(CONFIG.ex_fullscreen);
		} else {
			setLocationRelativeTo(null);
		}

		pack();
		
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		setTitle(LANG("window_name"));
		super.paint(g);
	};

	@Override
	public void pack() {
		add (PROHLIZEC);

		setFocusable(true);
		
		super.pack();
	};
	
	@Override
	public void setVisible(boolean b) {
		if (!b) {
			ukoncovani = true;
			Editor.OKNO.setVisible(false);
			ukoncovani = EDITOR.jsouOtevreneSoubory();
			
			CONFIG.ex_fullscreen = getExtendedState();
			CONFIG.ex_posx = getLocation().x;
			CONFIG.ex_posy = getLocation().y;
			CONFIG.ex_width = getWidth();
			CONFIG.ex_height = getHeight();
			CONFIG.lang = LANG("lang_short");
			CONFIG.folder_input = PROHLIZEC.getVstupniSoubor();
			CONFIG.folder_output = PROHLIZEC.getVystupniSoubor();
			CONFIG.save();
			
			LANGgererate(CONFIG.lang);
			System.exit(0);
		}
		super.setVisible(b);
	}

	/**
	 * Metoda vrací stav vypínání, pokud je zavíráno okno prohlážeče
	 * @return vrací stav vypínání
	 */
	public static boolean seUkoncuje() {
		return ukoncovani;
	}
}