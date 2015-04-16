package cz.eeg.ui.fileeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import cz.eeg.ui.Application;

/**
 * Internal class representing a close button,
 * On pressing of this button the editor closes
 * the visible file.
 * 
 * @author IT Crowd
 */
public class CloseButton extends JButton {
	/**  */
	private static final long serialVersionUID = 1L;

	public CloseButton() {
		super("X");
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.EDITOR.close();
			}
		});
	}
}
