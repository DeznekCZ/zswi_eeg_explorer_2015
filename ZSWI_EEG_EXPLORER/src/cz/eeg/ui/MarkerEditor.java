package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.LANG;

import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cz.eeg.data.EegFile;
import cz.eeg.data.Marker;
import cz.eeg.ui.markereditor.EditableField;

public class MarkerEditor extends JFrame {

	/**  */
	private static final long serialVersionUID = 1L;
	private static MarkerEditor instance;

	public MarkerEditor(EegFile file) {
		super(LANG("marker_title"));
		instance = this;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JScrollPane jsp = new JScrollPane(panel);
		
		List<Marker> list = file.getMarkerList();
		
		for (Iterator<Marker> iterator = list.iterator(); iterator.hasNext();) {
			panel.add(EditableField.from(iterator.next(), file));
		}
		
		add(jsp);
		
		pack();
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
	
	@Override
	public void setVisible(boolean flag) {
		if (!flag)
			GuiManager.repaint();
		super.setVisible(flag);
	}

	public static MarkerEditor getInstance() {
		return instance;
	}
}
