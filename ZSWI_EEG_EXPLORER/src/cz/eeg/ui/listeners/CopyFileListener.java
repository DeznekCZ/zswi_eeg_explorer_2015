package cz.eeg.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import cz.deznekcz.reflect.Out;
import cz.eeg.data.EegFile;
import cz.eeg.io.FileReadingException;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.dialog.DialogType;
import cz.eeg.ui.explorer.FileBrowserPanel;

/**
 * Listener connected to button of menu item copy files
 *
 * @author IT Crowd
 */
public class CopyFileListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		File[] files = FileBrowserPanel.PANEL.getSelectedFiles();
		for (File file : files) {
			if (FilesIO.isReadable(file)) {
				try {
					EegFile eegFile = FilesIO.read(file);
					DialogManagement.open(DialogType.FILE_SAVE, eegFile, new Out<Boolean>());
				} catch (FileNotFoundException
						| FileReadingException e1) {
				}
			}
		}
	}

}
