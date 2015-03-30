package cz.eeg.ui.meditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import cz.eeg.data.Marker;
import cz.eeg.ui.feditor.Dialog;

public class EditPart extends JButton {

	public EditPart(String string, final Marker marker, final String field) {
		super(string);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Dialog.open(Dialog.EDIT, marker, Marker.class.getDeclaredField(field));
				} catch (NoSuchFieldException | SecurityException e1) {
					//TODO
					e1.printStackTrace();
				}
			}
		});
	}

}
