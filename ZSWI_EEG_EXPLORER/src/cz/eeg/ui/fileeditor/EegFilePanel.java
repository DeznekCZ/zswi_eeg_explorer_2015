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

public class EegFilePanel {

	public static JPanel create(EegFile vhdrSoubor) {
		JPanel panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		panel.add(new MenuPanel(vhdrSoubor), BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {
		
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				setDividerLocation(0.5);
				super.paint(g);
			}
		};

		JTextArea areaVhdr = new JTextArea(vhdrSoubor.getVhdrData());
		JScrollPane jspVhdr = new JScrollPane(areaVhdr);
		
		JTextArea areaVmrk = new JTextArea(vhdrSoubor.getVmrkData());
		JScrollPane jspVmrk = new JScrollPane(areaVmrk);
		
		splitPane.add(jspVhdr);
		splitPane.add(jspVmrk);
		splitPane.setDividerSize(2);
		
		panel.add(splitPane);
		
		return panel;
	}
}
