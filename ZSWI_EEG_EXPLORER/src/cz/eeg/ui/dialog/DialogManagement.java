package cz.eeg.ui.dialog;

import static cz.deznekcz.tool.Lang.LANG;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import cz.deznekcz.reflect.Out;
import cz.eeg.data.Channel;
import cz.eeg.data.EegFile;
import cz.eeg.data.Marker;
import cz.eeg.ui.explorer.Scenario;
import cz.eeg.ui.fileeditor.Plotter;

/**
 * File handling dialog class
 * @author IT Crowd
 *
 */
public final class DialogManagement {
	
	public final static int SAVE_AS = 1;
	public static final int EDIT = 2;
	public static final int MARKER_ERROR = 3;
	public static final int PLOTING = 4;
	public static final int SCENARIO_ADD = 5;
	public static final int SCENARIO_REMOVE = 6;

	@SuppressWarnings("unchecked")
	public static void open(int type, Object... params) {
		switch (type) {
		case SAVE_AS:
			SaveDialog.open((EegFile) params[0]);
			break;
		case EDIT:
			editMarker((Marker) params[0], (String) params[1], (String) params[2]);
			break;
		case PLOTING:
			plot((EegFile) params[0]);
			break;
		case SCENARIO_ADD:
			if (params.length < 1)
				addScenario(new Out<String>());
			else
				addScenario((Out<String>) params[0]);
			break;
		case SCENARIO_REMOVE:
			removeScenario((String) params[0]);
			break;
		case MARKER_ERROR:
			JOptionPane.showMessageDialog(
			/*frame*/	null, // null == new
			/*message*/	LANG("marker_reading_error", ((Exception) params[0]).getMessage()), 
			/*title*/	LANG("error"), 
			/*type*/	JOptionPane.ERROR_MESSAGE);
		default:
			break;
		}
	}

	private static void removeScenario(String name) {
		boolean fail = true;
		while (fail) {
			int value = JOptionPane.showConfirmDialog(null, 
					LANG("dialog_save_scenario_delete", name),
					LANG("explorer_scenario"),
					JOptionPane.OK_CANCEL_OPTION
					);
			
			if (value == JOptionPane.OK_OPTION) {
				fail = false;
				Scenario.remove(name);
			} else {
				fail = false;
			}
		}
	}

	private static void addScenario(Out<String> output) {
		boolean fail = true;
		while (fail) {
			String value = JOptionPane.showInputDialog(LANG("dialog_save_scenario_add"));
			
			if (value != null && !value.equals("")) {
				fail = false;
				Scenario.addScenario(value);
				output.lock(value);
			} else {
				fail = false;
			}
		}
	}

	private static void plot(EegFile eegFile) {
		Channel[] channels = eegFile.getChannel();
		
		JCheckBox[] possibilities = new JCheckBox[channels.length];
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < possibilities.length; i++) {
			possibilities[i] = new JCheckBox(channels[i].getName());
			myPanel.add(possibilities[i]);
		}
		
		possibilities[0].addAncestorListener(new RequestFocusListener());
		
		JLabel message = new JLabel("");
		message.setVisible(false);
		myPanel.add(message);
		
		/*String s = (String)JOptionPane.showInputDialog(
		                    null,
		                    LANG("ploting_select_channel"),
		                    "Customized Dialog",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    possibilities,
		                    possibilities[0]);*/
		boolean fail = true;
		while(fail) {
			int option = JOptionPane.showConfirmDialog(
								null, 
								myPanel, 
								LANG("ploting_select_channel"), 
								JOptionPane.OK_CANCEL_OPTION);
	
			if (option == JOptionPane.OK_OPTION) {
				ArrayList<Integer> indexes = new ArrayList<Integer>(possibilities.length);
				for (int i = 0; i < possibilities.length; i++)
					if (possibilities[i].isSelected())
						indexes.add(i);
				if (indexes.size() > 0) {
					fail = false;
					if (indexes.size() == possibilities.length) {
						Plotter.open(eegFile);
					} else {
						int[] indexArray = new int[indexes.size()];
						for (int i = 0; i < indexArray.length; i++) {
							indexArray[i] = (int) indexes.get(i);
						}
						Plotter.open(eegFile, indexArray);
					}
				} else {
					message.setText(LANG("channels_not_selected"));
					message.setVisible(true);
				}
			} else {
				fail = false;
			}
		}
	}

	private static void editMarker(Marker marker, String method, String initialValue) {
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		
		JTextPane label = new JTextPane();
		label.setText(LANG("marker_description_edit"));
		label.setEditable(false);
		myPanel.add(label);
		
		JTextField value = new JTextField(initialValue);
		value.addAncestorListener(new RequestFocusListener());
		myPanel.add(value);
		
		JTextPane message = new JTextPane();
		message.setText("");
		message.setEditable(false);
		message.setVisible(false);
		myPanel.add(message);
		
		boolean fail = true;
		while (fail) {
			
			int result = JOptionPane.showConfirmDialog(null, myPanel, 
		               LANG("marker_edit"), JOptionPane.OK_CANCEL_OPTION);
			
			if (result == JOptionPane.OK_OPTION) {
				try {
					Method[] methods = marker.getClass().getDeclaredMethods();
					Method calledMethod = null;
					for (int i = 0; i < methods.length; i++) {
						if (methods[i].getName().equals(method)) {
							calledMethod = methods[i];
							break;
						}
					}
					if (calledMethod == null) {
						JOptionPane.showMessageDialog(null,
								LANG("method_not_exists", marker.getClass().getName(), method), 
								LANG("error"), JOptionPane.ERROR_MESSAGE);
					}
					
					if (calledMethod.getParameterTypes()[0] == int.class) {
						calledMethod.invoke(marker, Integer.parseInt(value.getText()));
						fail = false;
					} else if (calledMethod.getParameterTypes()[0] == String.class) {
						calledMethod.invoke(marker, value.getText());
						fail = false;
					} else {
						message.setText(LANG("incompatible_parameter_format"));
					}
					
				} catch (NumberFormatException e) {
					message.setText(LANG("number_wrong_format"));
				} catch (IllegalAccessException | IllegalArgumentException 
					   | InvocationTargetException e) {
					message.setText(LANG("method_not_accesible"));
				}
			} else {
				fail = false;
			}
		}
	}
}