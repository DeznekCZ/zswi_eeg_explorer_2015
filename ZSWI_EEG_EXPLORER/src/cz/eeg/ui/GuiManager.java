package cz.eeg.ui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import cz.deznekcz.tool.Loader;
import cz.eeg.ui.explorer.FileBrowserPanel;

/**
 * Manager of GUI
 *
 * @author IT Crowd
 */
public class GuiManager {
	
	static {
		//Loader.start(ImageIO.read(new File("res/loading.png")));
	}
	
	/** Frame of explorer {@link Application} */
	public final static Explorer EXPLORER = new Explorer();
	/** Instance of {@link FileEditor}, {@link JFrame} linked by {@link FileEditor#WINDOW} */
	public final static FileEditor EDITOR = new FileEditor();
	
	/** Currency focused {@link FileBrowserPanel} frame */
	public static FileBrowserPanel selectionFrame = null;
	
	/** API {@link KeyboardFocusManager} */
	public static KeyboardFocusManager focusManager =
	         KeyboardFocusManager.getCurrentKeyboardFocusManager();
	
	/**
	 * Method starts Graphical user inteface
	 */
	public static void start() {
		// This method initialize static variables
		// EXPLORER instance creates a new GUI window
	}

	public static void repaint() {
		EXPLORER.getParent().repaint();
		EDITOR.getParent().repaint();
	}
}
