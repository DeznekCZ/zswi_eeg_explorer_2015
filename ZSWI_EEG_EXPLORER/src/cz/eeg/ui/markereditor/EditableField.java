package cz.eeg.ui.markereditor;

import static cz.deznekcz.tool.Lang.*;

import java.util.List;

import static java.lang.String.format;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.xml.soap.Text;

import cz.eeg.data.Marker;
import cz.eeg.ui.Application;


public class EditableField extends JPanel {
	
	/** */
	private static final long serialVersionUID = 1L;

	//public final static Lang LANG = Aplikace.LANG;

	public static final int DEFAULT_SIZE = 0;
	
	private boolean plain = false;
	
	private Marker marker;
	
	/**
	 * 
	 * @param marker
	 * @return
	 */
	public static EditableField from(Marker marker) {
		EditableField field = new EditableField();
		field.marker = marker;
		field.setLayout(new BoxLayout(field, BoxLayout.X_AXIS));
		// Mk2=Stimulus,S  7,13417,0,0
		field.add( new FixedPart("Mk") );
		//TODO Constants
		field.add( new EditPart(marker.getMarkerNumber(), marker, "markerNumber") );
		field.add( new FixedPart(marker.getType()) );
		field.add( new EditPart(marker.getDescription(), marker, "description") );
		field.add( new FixedPart(marker.getPositionInDataPoints()) );
		field.add( new FixedPart(marker.getSizeInDataPoints()) );
		field.add( new FixedPart(marker.getChannelNumber()) );
		
		return field;
	}
}
