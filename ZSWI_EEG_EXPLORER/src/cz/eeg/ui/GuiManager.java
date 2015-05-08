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

	/** Instance of {@link FileEditor}, {@link JFrame} linked by {@link FileEditor#WINDOW} */
	public final static FileEditor EDITOR;
	
	/** Currency focused {@link FileBrowserPanel} frame */
	public static FileBrowserPanel selectionFrame = null;
	
	/** API {@link KeyboardFocusManager} */
	public static KeyboardFocusManager focusManager =
	         KeyboardFocusManager.getCurrentKeyboardFocusManager();
	
	static {
		EDITOR = new FileEditor();
		focusManager.addKeyEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (	e.getKeyCode() == KeyEvent.VK_ENTER
					&&	FileBrowserPanel.PANEL.hasFocus()) {
					GuiManager.EDITOR.open(FileBrowserPanel.PANEL.getSelectedFiles());
					return true;
				}
				return false;
			}
		});
	}
	
	/**
	 * Method starts Graphical user inteface
	 */
	public static void start() {
		EDITOR.setVisible(true);
	}

	public static void repaint() {
		EDITOR.getParent().repaint();
	}
}
