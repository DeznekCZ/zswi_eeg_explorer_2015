package cz.eeg.ui.editor;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import cz.eeg.Aplikace;
import cz.eeg.tool.Lang;

public class EditableField extends JPanel {
	
	public final static Lang LANG = Aplikace.LANG;
	
	private JLabel label;
	private JTextField field;
	
	/**
	 * 
	 * @param langRef {@link Lang} reference string
	 * @param line 
	 * @param size
	 */
	public EditableField(String line, int size) {
		
		label = new JLabel(String.format("%20s", " "));
		field = new JTextField(size);
		
		String[] splited = line.split("=");
		
		setName(splited[0]);
		label.setText(String.format("%20s", LANG.get("vhdr_" + splited[0])));
		field.setText(splited[1]);
		
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
		return label.getText() + "=" + field.getText();
	}
}
