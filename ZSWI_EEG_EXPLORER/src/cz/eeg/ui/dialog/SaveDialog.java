package cz.eeg.ui.dialog;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.Dimension;
import java.nio.file.FileAlreadyExistsException;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cz.deznekcz.reflect.Out;
import cz.eeg.data.EegFile;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.GuiManager;
import cz.eeg.ui.dialog.savedialog.Experiment;
import cz.eeg.ui.dialog.savedialog.Result;
import cz.eeg.ui.dialog.savedialog.Subject;

public class SaveDialog {

	public static final Dimension DIMENSION = new Dimension(200, 20);
	private static boolean customNames = false;

	static void open(EegFile file, Out<Boolean> saved) {
		
		JPanel body = new JPanel();
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		
		body.add(Subject.panel());
		body.add(Experiment.panel());
		body.add(Result.panel());
		
		JPanel saveCancel = new JPanel();
		saveCancel.setLayout(new BoxLayout(saveCancel, BoxLayout.X_AXIS));
		
		body.add(saveCancel);
		
		String[] options = new String[] {LANG("dialog_save_option"), LANG("dialog_cancel_option")};
		
		boolean fail = true;
		while (fail) {
			//JOptionPane.showOptionDialog(parentComponent, message, title, 
			//        optionType, messageType, icon, options, initialValue);
			int option = JOptionPane.showOptionDialog(GuiManager.EDITOR, body, 
					LANG("dialog_save_title", file.getName()),
					0, JOptionPane.QUESTION_MESSAGE,
					null, options, null);
			if (option == 0) {
				if (!Result.valid() && !customNames) {
					Result.setWarning(LANG("dialog_save_incomplete"));
				} else if (Result.exists()) {
					int overwrite = JOptionPane.showConfirmDialog(null, 
							LANG("dialog_save_overwrite_name", Result.get()),
							LANG("dialog_save_overwrite"), JOptionPane.YES_NO_OPTION);
					if (overwrite == JOptionPane.YES_OPTION) {
						try {
							FilesIO.write(file, Result.outputPath(), Result.get(), true);
							saved.lock(true);
							fail = false;
						} catch (FileAlreadyExistsException e) {}
					}
				} else {
					try {
						FilesIO.write(file, Result.outputPath(), Result.get(), false);
						saved.lock(true);
						fail = false;
					} catch (FileAlreadyExistsException e) {}
				}
			} else {
				fail = false;
			}
		}
	}

	public static boolean isCustomNamesAbled() {
		return customNames;
	}

	public static void ableCustomNames(boolean available) {
		SaveDialog.customNames = available;
	}
}
