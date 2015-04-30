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

public class Subject {

	private static JComboBox<Gender> genderSelect;
	private static JTextField ageInput;
	private static JTextField counterInput;

	public static JPanel panel() {
		
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
		ageInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
				System.out.println(e.getSource());
			}
		});
		
		counterInput = new JTextField("001");
		counterInput.setPreferredSize(new Dimension(300, 20));
		counterInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
				System.out.println(e.getSource());
			}
		});
		
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
			ageInput.setText("0");
			return 0;
		}
	}

	public static int getCounter() {
		try {
			return Integer.parseInt(counterInput.getText());
		} catch (Exception e) {
			counterInput.setText("01");
			return 1;
		}
	}

	public static boolean valid() {
		return !Gender.NONE.equals(genderSelect.getSelectedItem())
				&& getAge() > 0
				&& getCounter() > 0;
	}

}
