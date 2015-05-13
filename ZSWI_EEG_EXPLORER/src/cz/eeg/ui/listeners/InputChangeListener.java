package cz.eeg.ui.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cz.eeg.ui.dialog.savedialog.Result;

public class InputChangeListener implements DocumentListener {

	private boolean enabled = true;
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		if (enabled) Result.update();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (enabled) Result.update();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
