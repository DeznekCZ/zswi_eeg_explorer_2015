package cz.eeg.ui.dialog.savedialog;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class Segment {

	public static JPanel panel(String title, JComponent... components) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(title));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for (int i = 0; i < components.length; i++) {
			panel.add(components[i]);
		}
		return panel;
	}
	
}
