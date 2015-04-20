package cz.eeg.ui.markereditor;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class FixedPart extends Part {

	public FixedPart(String string) {
		super(string);

		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		setEnabled(false);
	}

}
