package cz.eeg.ui.fileeditor;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import cz.eeg.ui.GuiManager;

/**
 * Internal class representing a edit button,
 * On pressing of this button the editor opens
 * the visible file.
 * 
 * @author IT Crowd
 */
public class EditButton extends JButton {
	public EditButton() {
		super(LANG("editor_edit"));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiManager.EDITOR.edit();
			}
		});
	}
}