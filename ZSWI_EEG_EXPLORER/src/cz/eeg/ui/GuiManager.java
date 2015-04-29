package cz.eeg.ui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import cz.eeg.ui.explorer.FileBrowserPanel;

/**
 * Manager of GUI
 *
 * @author IT Crowd
 */
public class GuiManager {
	/** Frame of explorer {@link Application} */
	public final static Explorer EXPLORER = new Explorer();
	/** Instance of {@link FileEditor}, {@link JFrame} linked by {@link FileEditor#WINDOW} */
	public final static FileEditor EDITOR = new FileEditor();
	
	/** Currency focused {@link FileBrowserPanel} frame */
	public static FileBrowserPanel selectionFrame = null;
	
	/** API {@link KeyboardFocusManager} */
	public static KeyboardFocusManager focusManager =
	         KeyboardFocusManager.getCurrentKeyboardFocusManager();
	
	
	static {
		focusManager.addKeyEventDispatcher(
				new KeyEventDispatcher() {
				    public boolean dispatchKeyEvent(KeyEvent e) {
				        if(GuiManager.EXPLORER.isActive()
				        		&& e.getID() == KeyEvent.KEY_PRESSED 
				        		&& e.getKeyCode() == KeyEvent.VK_ENTER
				        		&& GuiManager.selectionFrame != null
				        		&& !GuiManager.selectionFrame.getSelectedFile().isDirectory()) {
				            GuiManager.EDITOR.open(GuiManager.selectionFrame.getSelectedFiles());
				        }
				        return false;
				    }
				}
			);
	}

	/**
	 * Method starts Graphical user inteface
	 */
	public static void start() {
		// This method initialize static variables
		// EXPLORER instance creates a new GUI window
	}
}
