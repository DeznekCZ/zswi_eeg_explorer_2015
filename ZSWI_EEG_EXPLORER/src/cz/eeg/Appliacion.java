package cz.eeg;

import static cz.deznekcz.tool.Lang.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import cz.eeg.tool.Config;
import cz.eeg.ui.Editor;
import cz.eeg.ui.Explorer;
import cz.eeg.ui.Select;

/**
 * Running class of EEG explorer
 * 
 * @author IT Crowd
 */
public class Appliacion extends JFrame {
	
	/** Serial version UID */
	private static final long serialVersionUID = 1L;

	//TODO Configuration
	/** Konfigurační instance třídy {@link Config} */
	public final static Config CONFIG = new Config();
	
	static {
		LANGload(CONFIG.lang);
	}
	
	/** Authors */
	public final static String AUTHOR = "IT Crowd\n"
				+"Zdeněk Novotný, Michal Sakáč, Vaclav Trykar, Václav Zoubek";
	
	//TODO Main window
	/** Main window of EEG explorer */
	public final static Appliacion WINDOW = new Appliacion();
	/** Frame of explorer {@link Appliacion} */
	public final static Explorer EXPLORER = new Explorer();
	/** Instance of {@link Editor}, {@link JFrame} linked by {@link Editor#WINDOW} */
	public final static Editor EDITOR = new Editor();
	/** API {@link KeyboardFocusManager} */
	public static KeyboardFocusManager manager =
	         KeyboardFocusManager.getCurrentKeyboardFocusManager();
	
	//TODO Active selectin window
	/** Currency focused {@link Select} frame */
	public static Select selectionFrame = null;
	
	/** Method main, runs an {@link Appliacion} */
	public static void main(String[] args) {
		WINDOW.run();

		manager.addKeyEventDispatcher(
				new KeyEventDispatcher() {
				    public boolean dispatchKeyEvent(KeyEvent e) {
				        if(Appliacion.WINDOW.isActive()
				        		&& e.getID() == KeyEvent.KEY_PRESSED 
				        		&& e.getKeyCode() == KeyEvent.VK_ENTER) {
				            Appliacion.EDITOR.open(true);
				        }
				        return false;
				    }
				}
			);
	}

	//TODO Application window
	/** Runs instance of {@link Appliacion} */
	public void run() {
		
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
		add (EXPLORER);

		setFocusable(true);
		
		super.pack();
	};
	
	@Override
	public void setVisible(boolean b) {
		if (!b) {
			Editor.WINDOW.setVisible(true);
			boolean closing = true;
			while (closing && EDITOR.isOpenedFiles()) {
				closing = EDITOR.close();
			}
			if (!EDITOR.isOpenedFiles()) {
				
				Editor.WINDOW.setVisible(false);
				
				CONFIG.ex_fullscreen = getExtendedState();
				CONFIG.ex_posx = getLocation().x;
				CONFIG.ex_posy = getLocation().y;
				CONFIG.ex_width = getWidth();
				CONFIG.ex_height = getHeight();
				CONFIG.lang = LANG("lang_short");
				CONFIG.folder_input = EXPLORER.getInputPath();
				CONFIG.folder_output = EXPLORER.getOutputPath();
				CONFIG.save();
				
				LANGgererate(CONFIG.lang);
				System.exit(0);
			}
		} else {
			super.setVisible(b);
		}
	}
}