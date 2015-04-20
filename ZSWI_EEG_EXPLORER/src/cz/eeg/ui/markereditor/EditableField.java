package cz.eeg.ui.markereditor;

import static cz.deznekcz.tool.Lang.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import static java.lang.String.format;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.xml.soap.Text;

import layout.TableLayout;
import layout.TableLayoutConstraints;
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
		// Mk2=Stimulus,S  7,13417,0,0
		
		double size[][] =
            {{0.10, 0.10, 0.20, TableLayout.FILL, 0.20, 0.20, 0.05},
             {TableLayout.FILL}};

		field.setLayout (new TableLayout(size));
		
		field.add( new FixedPart("Mk"),
				"0,0" );
		field.add( new FixedPart(marker.getMarkerNumber()+""),
				"1,0" );
		field.add( new FixedPart(marker.getType()),
				"2,0" );
		field.add( new EditPart( marker.getDescription(), marker, "description"),
				"3,0" );
		field.add( new FixedPart(marker.getPositionInDataPoints()),
				"4,0" );
		field.add( new FixedPart(marker.getSizeInDataPoints()),
				"5,0" );
		field.add( new FixedPart(marker.getChannelNumber()),
				"6,0" );
		
		return field;
	}

	private static BasicStroke next() {
		// TODO Auto-generated method stub
		return null;
	}
}
