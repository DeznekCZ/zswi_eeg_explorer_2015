package cz.eeg.ui.dialog.savedialog;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.dialog.SaveDialog;
import cz.eeg.ui.explorer.DirectoryBrowserPanel;
import cz.eeg.ui.explorer.FileBrowserPanel;

public class Result {

	private static final String RESULT_FORMAT = "%s_%04d%02d%02d_%s_%02d_%03d";
	private static JTextField output;
	private static JTextField warning;

	public static JPanel panel() {
		output = new JTextField();
		output.setEditable(false);
		output.setPreferredSize(SaveDialog.DIMENSION);
		output.setHorizontalAlignment(JTextField.CENTER);
		warning = new JTextField();
		warning.setEditable(false);
		warning.setPreferredSize(SaveDialog.DIMENSION);
		warning.setHorizontalAlignment(JTextField.CENTER);
		warning.setBackground(Color.RED);
		warning.setForeground(Color.WHITE);
		warning.setFont(warning.getFont().deriveFont(Font.BOLD));
		return Segment.panel(LANG("dialog_save_result_label"),
				output, warning);
	}

	public static void update() {
		output.setText( String.format(RESULT_FORMAT,
				Experiment.getScenario(),
				Experiment.getYear(),
				Experiment.getMonth(),
				Experiment.getDay(),
				Subject.getGender(),
				Subject.getAge(),
				Subject.getCounter()
				));
		
		if(!valid())
			Result.setWarning(LANG("dialog_save_incomplete"));
		else if(exists())
			Result.setWarning(LANG("dialog_save_exists"));
		else 
			Result.setWarning();
	}

	public static boolean valid() {
		return Experiment.valid()
			&& Subject.valid();
	}

	public static void setWarning(String... message) {
		if (message == null || message.length == 0) {
			warning.setBackground(Color.GREEN.darker());
			warning.setText("");
		} else {
			warning.setBackground(Color.RED);
			warning.setText(message[0]);
			warning.setVisible(true);
		}
	}

	public static boolean exists() {
		String path = outputPath().getAbsolutePath() + "/" + get() + ".vhdr";
		File file = new File(path);
		return file.exists();
	}

	public static File outputPath() {
		return DirectoryBrowserPanel.PANEL.getCurrentDirectory();
	}

	public static String get() {
		return output.getText();
	}
}
