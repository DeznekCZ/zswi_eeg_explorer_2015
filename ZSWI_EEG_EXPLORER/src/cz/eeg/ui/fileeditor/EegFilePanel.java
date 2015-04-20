package cz.eeg.ui.fileeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import cz.eeg.data.EegFile;

public class EegFilePanel extends JPanel {
	/** */
	private static final long serialVersionUID = 9145049454231468340L;
	
	private JTextArea areaVhdr;
	private JTextArea areaVmrk;
	
	private EegFile eegFile;
	
	public EegFilePanel(EegFile eegFile) {
		this.eegFile = eegFile;
	}

	public static JPanel create(EegFile eegFile) {
		EegFilePanel panel = new EegFilePanel(eegFile);
		
		panel.setLayout(new BorderLayout());
		panel.add(new MenuPanel(eegFile), BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {
		
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				setDividerLocation(0.5);
				super.paint(g);
			}
		};

		panel.areaVhdr = new JTextArea(eegFile.getVhdrData());
		JScrollPane jspVhdr = new JScrollPane(panel.areaVhdr);
		
		panel.areaVmrk = new JTextArea(eegFile.getVmrkData());
		JScrollPane jspVmrk = new JScrollPane(panel.areaVmrk);
		
		splitPane.add(jspVhdr);
		splitPane.add(jspVmrk);
		splitPane.setDividerSize(2);
		
		panel.add(splitPane);
		
		return panel;
	}
	
	@Override
	public void paint(Graphics g) {
		// prepare to editing
		areaVhdr.setText(eegFile.getVhdrData());
		areaVmrk.setText(eegFile.getVmrkData());
		super.paint(g);
	}
}
