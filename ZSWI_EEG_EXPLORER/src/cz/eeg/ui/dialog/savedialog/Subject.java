package cz.eeg.ui.dialog.savedialog;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import cz.eeg.ui.listeners.InputChangeListener;

public class Subject {

	private static JComboBox<Gender> genderSelect;
	private static JTextField ageInput;
	private static JTextField counterInput;
	private static InputChangeListener aiCIL;
	private static InputChangeListener ciCIL;

	public static JPanel panel() {

		aiCIL = new InputChangeListener();
		ciCIL = new InputChangeListener();
		
		genderSelect = new JComboBox<Gender>();
		genderSelect.setPreferredSize(new Dimension(300, 20));
		genderSelect.addItem(Gender.NONE);
		genderSelect.addItem(Gender.FEMALE);
		genderSelect.addItem(Gender.MALE);
		genderSelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;
				Result.update();
			}
		});
		genderSelect.setSelectedItem(Gender.NONE);

		ageInput = new JTextField("0");
		ageInput.setPreferredSize(new Dimension(300, 20));
		ageInput.getDocument()
			.addDocumentListener(aiCIL);
		
		counterInput = new JTextField("001");
		counterInput.setPreferredSize(new Dimension(300, 20));
		counterInput.getDocument()
			.addDocumentListener(ciCIL);
		
		return Segment.panel(LANG("dialog_save_subject"),
				genderSelect, ageInput, counterInput);
	}

	public static String getGender() {
		return ((Gender) genderSelect.getSelectedItem()).value();
	}

	public static int getAge() {
		try {
			return Integer.parseInt(ageInput.getText());
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getCounter() {
		try {
			return Integer.parseInt(counterInput.getText());
		} catch (Exception e) {
			return 1;
		}
	}

	public static boolean valid() {
		return 	   validGender()
				&& validAge()
				&& validCounter();
	}

	private static boolean validAge() {
		if (getAge() <= 0) {
			Result.setWarning(LANG("dialog_save_wrong_age_number"));
			return false;
		} else {
			return true;
		}
	}

	private static boolean validCounter() {
		if (getCounter() <= 0) {
			Result.setWarning(LANG("dialog_save_wrong_counter_number"));
			return false;
		} else {
			return true;
		}
	}

	private static boolean validGender() {
		if (Gender.NONE.equals(genderSelect.getSelectedItem())) {
			Result.setWarning(LANG("dialog_save_select_gender"));
			return false;
		} else {
			return true;
		}
	}

	public static void setLocked(boolean locked) {
		boolean lock = !locked;
		genderSelect.getParent().setEnabled(lock);
		genderSelect.setEnabled(lock);
		counterInput.setEnabled(lock);
		ageInput.setEnabled(lock);
	}

}
