package cz.eeg.ui.fileeditor;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;

import cz.eeg.data.EegFile;
import cz.eeg.io.FileReadingException;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.GuiManager;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.dialog.DialogType;

public class MergeItem {

	public static JMenuItem from(final EegFile target, final EegFile source) {
		JMenuItem menuItem = new JMenuItem(source.getName());
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					GuiManager.EDITOR.open(FilesIO.mergeTMP(target, source));
				} catch (IOException | FileReadingException e1) {
					DialogManagement.open(DialogType.ERROR, e1.getMessage());
				}
			}
		});
		return menuItem;
	}

	public static JMenuItem voidItem(String type) {
		JMenuItem menuItem = new JMenuItem(LANG(type));
		menuItem.setEnabled(false);
		return menuItem;
	}

}
