package cz.eeg.ui.markereditor;

import javax.swing.JButton;

public class FixedPart extends JButton {

	public FixedPart(String string) {
		super(string);
		
		setEnabled(false);
	}

}
