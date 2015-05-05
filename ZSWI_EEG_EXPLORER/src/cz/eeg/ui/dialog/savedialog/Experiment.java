package cz.eeg.ui.dialog.savedialog;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import cz.deznekcz.reflect.Out;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.dialog.SaveDialog;
import cz.eeg.ui.explorer.Scenario;

public class Experiment {
	
	private static JComboBox<Integer> yearSelect;
	private static int minYear = 1990;
	private static JComboBox<Integer> monthSelect;
	private static JComboBox<Integer> daySelect;
	private static JComboBox<String> scenarioSelect;
	private static JButton scenarioButton;

	public static JPanel panel() {
		
		yearSelect = new JComboBox<Integer>();
		yearSelect.setPreferredSize(SaveDialog.DIMENSION);
		for (int i = minYear; i <= DialogDate.getYear(); i++) {
			yearSelect.addItem(i);
		}
		yearSelect.setSelectedItem(DialogDate.getYear());
		yearSelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;
				DialogDate.setYear((int) e.getItem());
				reloadMonthOptions();
				reloadDayOptions();
				Result.update();
			}
		});
		yearSelect.setToolTipText(LANG("dialog_save_year"));
		
		monthSelect = new JComboBox<Integer>();
		monthSelect.setPreferredSize(SaveDialog.DIMENSION);
		reloadMonthOptions();
		monthSelect.setSelectedItem(DialogDate.getMonth());
		monthSelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;
				DialogDate.setMonth((int) e.getItem());
				reloadDayOptions();
				Result.update();
			}
		});
		monthSelect.setToolTipText(LANG("dialog_save_month"));
		
		daySelect = new JComboBox<Integer>();
		daySelect.setPreferredSize(SaveDialog.DIMENSION);
		reloadDayOptions();
		daySelect.setSelectedItem(DialogDate.getDay());
		daySelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;
				DialogDate.setDay((int) e.getItem());
				Result.update();
			}
		});
		daySelect.setToolTipText(LANG("dialog_save_day"));
		
		scenarioSelect = new JComboBox<String>();
		scenarioSelect.setPreferredSize(SaveDialog.DIMENSION);
		reloadScenarios();
		scenarioSelect.setSelectedItem(Scenario.getLast());
		scenarioSelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;
				Scenario.setLast((String) e.getItem());
				Result.update();
			}
		});
		scenarioSelect.setToolTipText(LANG("dialog_save_scenario"));
		
		scenarioButton = new JButton(LANG("dialog_save_new_scenario"));
		scenarioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Out<String> out = new Out<String>((String) scenarioSelect.getSelectedItem());
				DialogManagement.open(DialogManagement.SCENARIO_ADD, out);
				reloadScenarios();
				scenarioSelect.setSelectedItem(out.value());
			}
		});
		
		return Segment.panel(LANG("dialog_save_experiment"),
				yearSelect, monthSelect, daySelect, scenarioSelect, scenarioButton);
	}

	private static void reloadScenarios() {
		scenarioSelect.removeAllItems();
		scenarioSelect.addItem(Scenario.DEFAULT);
		for (int i = 0; i < Scenario.getList().size(); i++) {
			scenarioSelect.addItem(Scenario.getList().get(i));
		}
	}

	private static void reloadMonthOptions() {
		monthSelect.removeAllItems();
		for (int i = 1; i <= DialogDate.getMaxMonth(); i++) {
			monthSelect.addItem(i);
		}
	}

	private static void reloadDayOptions() {
		daySelect.removeAllItems();
		for (int i = 1; i <= DialogDate.getMaxDay(); i++) {
			daySelect.addItem(i);
		}
	}

	public static int getYear() {
		return (int) yearSelect.getSelectedItem();
	}

	public static int getMonth() {
		return (int) monthSelect.getSelectedItem();
	}

	public static int getDay() {
		return (int) daySelect.getSelectedItem();
	}

	public static String getScenario() {
		return (String) scenarioSelect.getSelectedItem();
	}

	public static boolean valid() {
		return !Scenario.DEFAULT.equals(scenarioSelect.getSelectedItem());
	}
}
