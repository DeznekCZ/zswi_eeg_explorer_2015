package cz.eeg.ui.editor;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import cz.eeg.Application;

/**
 * Internal class representing a menu panel
 * for editing the visible file.
 * 
 * @author IT Crowd
 */
public class MenuPanel extends JPanel {

	public MenuPanel(boolean editable, boolean saveable, boolean closeable) {
		setLayout(new BorderLayout());
		//PANEL_TLACITEK.add(new CloseButton(), BorderLayout.EAST);
		
		// Soubor menu

		final JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		
		// Soubor menu

		final JMenu file = new JMenu(LANG("file"));
		menuBar.add(file);
		{
			// Soubor item

			final JMenuItem s1 = new JMenuItem(LANG("file_open"));
			
			s1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					Application.WINDOW.requestFocus();
				}
			}); file.add(s1);
			
			file.addSeparator();
			
			// Uložení

			JMenuItem saveAs = new JMenuItem(LANG("file_save_as"));
			
			saveAs.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Dialog.open(Dialog.SAVE_AS);
				}
			});
			
			saveAs.setEnabled(saveable);
			file.add(saveAs);
		}
		
		
		// Zaviraci tlacitko
        
		menuBar.add(Box.createHorizontalGlue());
		
		EditButton edit = new EditButton();
		menuBar.add(edit);
		edit.setEnabled(editable);
		
		CloseButton close = new CloseButton();
		menuBar.add(close);
		close.setEnabled(closeable);
	}
}
