package cz.eeg.ui.markereditor;


public class FixedPart extends Part {

	public FixedPart(String string) {
		super(string);

		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		setEnabled(false);
	}

}
