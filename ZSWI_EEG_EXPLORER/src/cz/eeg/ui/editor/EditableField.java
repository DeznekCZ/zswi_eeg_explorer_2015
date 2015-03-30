package cz.eeg.ui.editor;

import static cz.deznekcz.tool.Lang.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.xml.soap.Text;

import cz.eeg.ui.Application;

public class EditableField extends JPanel {
	
	//public final static Lang LANG = Aplikace.LANG;

	public static final int DEFAULT_SIZE = 0;
	
	private JLabel label;
	private JTextField field;

	private boolean plain = false;
	
	/**
	 * 
	 * @param langRef {@link Lang} reference string
	 * @param line 
	 * @param size
	 */
	public EditableField(String line, int size) {
		
		label = new JLabel(String.format("%20s", " "));
		field = new JTextField(size);
		
		field.setEditable(false);
		
		String[] splited = line.split("=");
		
		if (line.startsWith(";")) {
			label.setText(String.format("%20s", line.substring(2)));
			field.setVisible(false);
		} else if (splited.length > 1) {
			setName(splited[0]);
			label.setText(String.format("%20s", LANG("vhdr_" + splited[0])));
			field.setText(splited[1]);
		} else {
			label.setText(String.format("%20s", LANG("vhdr_" + splited[0])));
			field.setText("");
		}
		
		
		add(label);
		add(field);
	}

	public String getValue() {
		return field.getText();
	}

	public String getLabel() {
		return label.getText();
	}
	
	@Override
	public String toString() {
		return (plain ? "; "+label.getText() : label.getText() + "=" + field.getText());
	}
	
	public void setValue(String value) {
		if (plain) {
			label.setText(value);
		} else {
			field.setText(value);
		}
	}

	public EditableField editable() {
		field.setEditable(true);
		return this;
	}

	public EditableField plain() {
		label.setText(label.getText() + 
				(field.getText().equals("") ? "" : "=" + field.getText()));
		field.setVisible(false);
		return this;
	}
}
