package cz.eeg.ui.explorer;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;

import cz.deznekcz.reflect.Out;
import cz.eeg.data.EegFile;
import cz.eeg.io.FileReadingException;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.GuiManager;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.dialog.DialogType;

public class ExplorerButton {
	public final static JButton OPEN_SELECTED = initOpen();
	public final static JButton DELETE_SELECTED = initDelete();
	public final static JButton COPY_SELECTED = initCopy();

	private static JButton initOpen() {
		return initButton(
				"explorer_button_open",
				false,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						GuiManager.EDITOR.open(FileBrowserPanel.PANEL.getSelectedFiles());
					}
				});
	}

	private static JButton initCopy() {
		return initButton(
				"explorer_button_copy",
				false,
				new ActionListener() {
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
				});
	}

	private static JButton initDelete() {
		return initButton(
				"explorer_button_delete",
				false,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						DialogManagement.open(DialogType.FILE_DELETE, 
						/* must be one parameter */ (Object) FileBrowserPanel.PANEL.getSelectedFiles());
					}
				});
	}

	private static JButton initButton(String name,
			boolean isEnabled,
			ActionListener action) {
		JButton button = new JButton(LANG(name));
		button.setEnabled(isEnabled);
		button.addActionListener(action);
		return button;
	}
}
