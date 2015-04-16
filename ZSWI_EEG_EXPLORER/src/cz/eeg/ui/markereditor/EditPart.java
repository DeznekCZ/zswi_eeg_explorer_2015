package cz.eeg.ui.markereditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import cz.eeg.data.Marker;
import cz.eeg.ui.dialog.DialogManagement;

public class EditPart extends JButton {

	public EditPart(Object value, final Marker marker, final String field) {
		super(value.toString());
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					DialogManagement.open(DialogManagement.EDIT, marker, Marker.class.getDeclaredField(field));
				} catch (NoSuchFieldException | SecurityException e1) {
					//TODO
					e1.printStackTrace();
				}
			}
		});
	}

}
