package cz.eeg.ui.markereditor;

import java.awt.Dimension;

import javax.swing.JButton;

public abstract class Part extends JButton {
	
	/** */
	private static final long serialVersionUID = 4442395370848687612L;

	public Part(String textValue) {
		super(textValue);
	}
	
	public Part width(int width) {
		Dimension dim = new Dimension(width, 25);
		this.setSize(dim);
		this.setPreferredSize(dim);
		return this;
	}
}
