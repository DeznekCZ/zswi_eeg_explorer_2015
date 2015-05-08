package cz.eeg.ui.markereditor;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;

import cz.eeg.data.EegFile;
import cz.eeg.data.Marker;
import cz.eeg.ui.MarkerEditor;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.dialog.DialogType;

public class EditPart extends Part {

	/** */
	private static final long serialVersionUID = 209137326736283806L;
	private Method getterMethod;
	private Object object;

	/**
	 * Editable part of {@link EditableField}
	 * @param value value of {@link EditPart}
	 * @param marker instance of changed {@link Marker}
	 * @param setterMethod name of setter method
	 * @param getterMethod name of getter method
	 * @param eegFile instance of {@link EegFile} that owns marker
	 */
	public EditPart(
			final Object value, 
			final Marker marker, 
			final String setterMethod, 
			final String getterMethod,
			final EegFile eegFile) {
		super(value.toString());

		this.object = marker;
		
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		try {
			Method[] methods = marker.getClass().getDeclaredMethods();
			this.getterMethod = null;
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals(getterMethod)) {
					this.getterMethod = methods[i];
					break;
				}
			}
			if (this.getterMethod == null) {
				JOptionPane.showMessageDialog(null,
						LANG("method_not_exists", marker.getClass().getName(), getterMethod), 
						LANG("error"), JOptionPane.ERROR_MESSAGE);
			}
			
			addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					DialogManagement.open(DialogType.MARKER_EDIT, marker, setterMethod, value, eegFile);
					MarkerEditor.getInstance().repaint();
				}
			});
			
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null,
					LANG("method_not_accesible", getterMethod), 
					LANG("error"), JOptionPane.ERROR_MESSAGE);
		} 
	}
	
	@Override
	public void paint(Graphics g) {
		try {
			this.setText((String) getterMethod.invoke(object));
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO TESTED IN CONSTRUCTOR
		}
		super.paint(g);
	}

}
