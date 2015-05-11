package cz.eeg.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cz.eeg.ui.FileEditor;

/**
 * Listener connected to button or menu item set explorer visible
 *
 * @author IT Crowd
 */
public class SelectExplorerTabListner implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		FileEditor.TABS.setSelectedIndex(0);
	}

}
