package cz.eeg.ui.fileeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.UIManager;

import cz.eeg.ui.FileEditor;

/**
 * Internal class representing a back button,
 * On pressing of this button set view to Explorer tab.
 * 
 * @author IT Crowd
 */
public class BackButton extends JButton {
	/**  */
	private static final long serialVersionUID = 1L;

	public BackButton(boolean clickable) {
		super(UIManager.getIcon("FileChooser.newFolderIcon"));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.TABS.setSelectedIndex(0);
			}
		});
		setEnabled(clickable);
	}
}
