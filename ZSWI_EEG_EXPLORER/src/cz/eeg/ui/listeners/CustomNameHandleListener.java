package cz.eeg.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cz.eeg.ui.dialog.savedialog.Result;

public class CustomNameHandleListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Result.update();
	}

}
