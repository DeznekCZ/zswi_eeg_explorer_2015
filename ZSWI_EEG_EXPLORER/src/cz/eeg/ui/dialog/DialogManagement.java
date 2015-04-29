package cz.eeg.ui.dialog;

import static cz.deznekcz.tool.Lang.LANG;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import cz.eeg.data.Channel;
import cz.eeg.data.EegFile;
import cz.eeg.data.Marker;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.GuiManager;
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

	public static void open(int type, Object... params) {
		switch (type) {
		case SAVE_AS:
			saveAs((EegFile) params[0]);
			break;
		case EDIT:
			editMarker((Marker) params[0], (String) params[1], (String) params[2]);
			break;
		case PLOTING:
			plot((EegFile) params[0]);
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

	private static void saveAs(EegFile file) {
		JTextField yearField = new JTextField(4);
	    JTextField monthField = new JTextField(2);
	    JTextField dayField = new JTextField(2);
	    JTextField genderField = new JTextField(1);
	    JTextField ageField = new JTextField(3);
	
	    JPanel myPanel = new JPanel();
	    myPanel.add(new JLabel(LANG("format_year") + ":"));
	    myPanel.add(yearField);
	    myPanel.add(Box.createVerticalStrut(1)); // a spacer
	    myPanel.add(new JLabel(LANG("format_month") + ":"));
	    myPanel.add(monthField);
	    myPanel.add(Box.createVerticalStrut(1)); // a spacer
	    myPanel.add(new JLabel(LANG("format_day") + ":"));
	    myPanel.add(dayField);
	    myPanel.add(Box.createVerticalStrut(1)); // a spacer
	    myPanel.add(new JLabel(LANG("format_gender") + ":"));
	    myPanel.add(genderField);
	    myPanel.add(Box.createVerticalStrut(1)); // a spacer
	    myPanel.add(new JLabel(LANG("format_age") + ":"));
	    myPanel.add(ageField);
	
	    String wrongMessage = "";
	    
	    String lastName = "";
	    boolean overwrite = false;
	    
	    while (true) {
		    int result = JOptionPane.showConfirmDialog(null, myPanel, 
		               LANG("editor_save"), JOptionPane.OK_CANCEL_OPTION);
		    if (result == JOptionPane.OK_OPTION) {
		    	
		    	if (testYear(yearField.getText()) 
		    			|| testMonth(monthField.getText())
		    			|| testDay(dayField.getText(), monthField.getText())
		    			|| testGender(genderField.getText())
		    			|| testAge(ageField.getText())) {
		    		wrongMessage = LANG("format_wrong");
		    		continue;
		    	}
		    	
		    	String year = String.format("%04d", Integer.parseInt(yearField.getText()));
		    	String day = String.format("%02d", Integer.parseInt(dayField.getText()));
		    	String month = String.format("%02d", Integer.parseInt(monthField.getText()));
		    	String gender = genderField.getText();
		    	String age = String.format("%03d", Integer.parseInt(ageField.getText()));
		    	
		    	//System.out.println(LANG.format_year + ": " + year);
		    	//System.out.println(LANG.format_month + ": " + month);
		    	//System.out.println(LANG.format_day + ": " + day);
		    	//System.out.println(LANG.format_gender + ": " + gender);
		    	//System.out.println(LANG.format_age + ": " + age);
		    	//System.out.println(year+"-"+month+"-"+day+"-"+gender+"-"+age+".vhdr");
		    	
		    	String newName = "";
		    	
		    	try {
		    		newName = gender+"_"+age+"_"+year+"_"+month+"_"+day;//TODO next field
					if	(FilesIO.write(
							file, 
							new File(GuiManager.EXPLORER.getOutputPath()), 
							newName, 
							overwrite)
							)
						break;
					
					wrongMessage = LANG("file_writing_failed");
					continue;
				} catch (FileAlreadyExistsException e) {
					wrongMessage = LANG("file_exists");
					lastName = newName;
					continue;
				}
		    } else {
		    	break;
		    }
	    }
	}
	
	private static boolean testMonth(String month) {
		if (month == null || month.equals("")) 
			return true; 
		try {
			int i = Integer.parseInt(month);
			if (i < 1 || i > 12) {
				return true;
			}
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

	private static boolean testYear(String year) {
		if (year == null || year.equals("")) 
			return true; 
		try {
			int i = Integer.parseInt(year);
			if (i < 1900) {
				return true;
			}
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

	private static boolean testDay(String day, String month) {
		try {
			int[] dayConst = new int[] {
				31,29,31,30,31,30,31,31,30,31,30,31
			};
			int m = Integer.parseInt(month);
			int d = Integer.parseInt(day);
			if (m < 1 || m > 12) {
				return true;
			} else if (d < 1 || d > dayConst[(m-1)]) {
				return true;
			}
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

	private static boolean testGender(String gender) {
		if (gender == null || gender.equals("")) 
			return true;
		switch (gender) {
		case "m":
		case "f":
			return false;

		default:
			return true;
		}
	}

	private static boolean testAge(String age) {
		if (age == null || age.equals("")) 
			return true;
		try {
			int i = Integer.parseInt(age);
			if (i < 1) {
				return true;
			}
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}
	
}

/**
 *  Convenience class to request focus on a component.
 *
 *  When the component is added to a realized Window then component will
 *  request focus immediately, since the ancestorAdded event is fired
 *  immediately.
 *
 *  When the component is added to a non realized Window, then the focus
 *  request will be made once the window is realized, since the
 *  ancestorAdded event will not be fired until then.
 *
 *  Using the default constructor will cause the listener to be removed
 *  from the component once the AncestorEvent is generated. A second constructor
 *  allows you to specify a boolean value of false to prevent the
 *  AncestorListener from being removed when the event is generated. This will
 *  allow you to reuse the listener each time the event is generated.
 */
class RequestFocusListener implements AncestorListener
{
	private boolean removeListener;

	/*
	 *  Convenience constructor. The listener is only used once and then it is
	 *  removed from the component.
	 */
	public RequestFocusListener()
	{
		this(true);
	}

	/*
	 *  Constructor that controls whether this listen can be used once or
	 *  multiple times.
	 *
	 *  @param removeListener when true this listener is only invoked once
	 *                        otherwise it can be invoked multiple times.
	 */
	public RequestFocusListener(boolean removeListener)
	{
		this.removeListener = removeListener;
	}

	@Override
	public void ancestorAdded(AncestorEvent e)
	{
		JComponent component = e.getComponent();
		component.requestFocusInWindow();

		if (removeListener)
			component.removeAncestorListener( this );
	}

	@Override
	public void ancestorMoved(AncestorEvent e) {}

	@Override
	public void ancestorRemoved(AncestorEvent e) {}
}

