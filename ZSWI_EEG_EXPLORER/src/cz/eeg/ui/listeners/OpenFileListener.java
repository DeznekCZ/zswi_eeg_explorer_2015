package cz.eeg.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cz.eeg.ui.GuiManager;
import cz.eeg.ui.explorer.FileBrowserPanel;

/**
 * Listener connected to button of menu item open files
 *
 * @author IT Crowd
 */
public class OpenFileListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		GuiManager.EDITOR.open(FileBrowserPanel.PANEL.getSelectedFiles());
	}

}
