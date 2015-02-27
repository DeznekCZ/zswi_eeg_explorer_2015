package cz.eeg.ui.editor;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.eeg.Aplikace;
import cz.eeg.tool.Lang;
import cz.eeg.ui.Editor;

public class Ulozit {
	
	public final static Lang LANG = Aplikace.LANG;
	
	public final static int ULOZIT = 1;
	
	public Ulozit(final int type) {
		if (type == ULOZIT) {
			
			JTextField yearField = new JTextField(4);
		    JTextField monthField = new JTextField(2);
		    JTextField dayField = new JTextField(2);
		    JTextField genderField = new JTextField(1);
		    JTextField ageField = new JTextField(3);
		
		    JPanel myPanel = new JPanel();
		    myPanel.add(new JLabel(LANG.format_year + ":"));
		    myPanel.add(yearField);
		    myPanel.add(Box.createVerticalStrut(1)); // a spacer
		    myPanel.add(new JLabel(LANG.format_month + ":"));
		    myPanel.add(monthField);
		    myPanel.add(Box.createVerticalStrut(1)); // a spacer
		    myPanel.add(new JLabel(LANG.format_day + ":"));
		    myPanel.add(dayField);
		    myPanel.add(Box.createVerticalStrut(1)); // a spacer
		    myPanel.add(new JLabel(LANG.format_gender + ":"));
		    myPanel.add(genderField);
		    myPanel.add(Box.createVerticalStrut(1)); // a spacer
		    myPanel.add(new JLabel(LANG.format_age + ":"));
		    myPanel.add(ageField);
		
		    String wrongMessage = "";
		    
		    while (true) {
			    int result = JOptionPane.showConfirmDialog(null, myPanel, 
			               LANG.editor_save, JOptionPane.OK_CANCEL_OPTION);
			    if (result == JOptionPane.OK_OPTION) {
			    	
			    	if (testYear(yearField.getText()) && testMonth(monthField.getText())
			    			&& testDay(dayField.getText(), monthField.getText())
			    			&& testGender(genderField.getText()) && testAge(ageField.getText())) {
			    		wrongMessage = LANG.format_wrong;
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
			    	
			    	Aplikace.EDITOR.ulozitSoubor(year+"-"+month+"-"+day+"-"+gender+"-"+age);
			    	
			    	break;
			    } else {
			    	break;
			    }
		    }
		}
	}

	private boolean testMonth(String month) {
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

	private boolean testYear(String year) {
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

	private boolean testDay(String day, String month) {
		if (month == null || month.equals("")) 
			return true; 
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

	private boolean testGender(String gender) {
		if (gender == null || gender.equals("")) 
			return true;
		switch (gender) {
		case "m":
		case "male":
		case "f":
		case "female":
			return false;

		default:
			return true;
		}
	}

	private boolean testAge(String age) {
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
