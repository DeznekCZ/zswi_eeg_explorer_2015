package cz.eeg.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.dialog.DialogType;
import cz.eeg.ui.explorer.FileBrowserPanel;

/**
 * Listener connected to button or menu item deletes selected file
 *
 * @author IT Crowd
 */
public class DeleteFileListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		DialogManagement.open(DialogType.FILE_DELETE, 
		/* must be one parameter */ (Object) FileBrowserPanel.PANEL.getSelectedFiles());
	}

}
