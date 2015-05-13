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
import cz.eeg.ui.listeners.CopyFileListener;
import cz.eeg.ui.listeners.DeleteFileListener;
import cz.eeg.ui.listeners.OpenFileListener;

public class ExplorerButton {
	public final static JButton OPEN_SELECTED = initOpen();
	public final static JButton DELETE_SELECTED = initDelete();
	public final static JButton COPY_SELECTED = initCopy();
	public final static JButton SET_OUTPUT = initOutput();
	public final static JButton SET_INPUT = initInput();

	private static JButton initOpen() {
		return initButton(
				"explorer_button_open",
				false,
				new OpenFileListener());
	}

	private static JButton initInput() {
		return initButton(
				"explorer_button_input",
				true,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						FileBrowserPanel.PANEL.setCurrentDirectory(
								DirectoryBrowserPanel.PANEL.getCurrentDirectory()
							);
					}
				});
	}

	private static JButton initOutput() {
		return initButton(
				"explorer_button_output",
				true,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						DirectoryBrowserPanel.PANEL.setCurrentDirectory(
								FileBrowserPanel.PANEL.getCurrentDirectory()
							);
					}
				});
	}

	private static JButton initCopy() {
		return initButton(
				"explorer_button_copy",
				false,
				new CopyFileListener());
	}

	private static JButton initDelete() {
		return initButton(
				"explorer_button_delete",
				false,
				new DeleteFileListener());
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
