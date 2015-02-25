package cz.eeg;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import cz.eeg.tool.Config;
import cz.eeg.tool.Lang;
import cz.eeg.tool.Ovladac;
import cz.eeg.ui.Editor;
import cz.eeg.ui.Prohlizec;
import cz.eeg.ui.Vyber;

public class Aplikace extends JFrame {
	
	//TODO Inicializace nastavení
	public final static Config CONFIG = new Config();
	public final static Lang LANG = new Lang(CONFIG.lang);
	public final static String AUTHOR = "IT Crowds";
	
	//TODO Hlavní okno
	public final static Aplikace OKNO = new Aplikace();
	public final static Prohlizec PROHLIZEC = new Prohlizec();
	public final static Editor EDITOR = new Editor();
	
	//TODO Vybraný výběr
	public static Vyber oknoVyberu = null;

	public static void main(String[] args) {
		OKNO.spustit();

		manager.addKeyEventDispatcher( new Ovladac() );
	}

	private static boolean ukoncovani;
	
	//TODO Okno aplikace
	public void spustit() {
		pack();
		
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
		
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		setTitle(LANG.window_name);
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
			CONFIG.lang = LANG.lang_short;
			CONFIG.folder_input = PROHLIZEC.getVstupniSoubor();
			CONFIG.folder_output = PROHLIZEC.getVystupniSoubor();
			CONFIG.save();
			System.exit(0);
		}
		super.setVisible(b);
	}

	public static boolean ukoncujeSe() {
		return ukoncovani;
	}
	
	//Hijack the keyboard manager
	static KeyboardFocusManager manager =
	         KeyboardFocusManager.getCurrentKeyboardFocusManager();
	public static boolean unlocked = true;
}