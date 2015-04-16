package cz.eeg.ui.fileeditor;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import cz.eeg.data.EegFile;
import cz.eeg.ui.Application;
import cz.eeg.ui.FileEditor;
import cz.eeg.ui.dialog.DialogManagement;

/**
 * Internal class representing a menu panel
 * for editing the visible file.
 * 
 * @author IT Crowd
 */
public class MenuPanel extends JPanel {

	/** */
	private static final long serialVersionUID = 1L;

	public MenuPanel(final EegFile vhdrFile) {
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
/*
			JMenuItem save = new JMenuItem(LANG("file_save"));
			
			save.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//TODO Dialog.open(Dialog.SAVE);
				}
			});
			
			save.setEnabled(saveable);*/

			JMenuItem saveAs = new JMenuItem(LANG("file_save_as"));
			
			saveAs.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					DialogManagement.open(DialogManagement.SAVE_AS, vhdrFile);
				}
			});
			
			saveAs.setEnabled(vhdrFile.isSaveable());
			file.add(saveAs);
			
			file.addSeparator();
			
			JMenuItem close = new JMenuItem(LANG("file_close_menu"));
			close.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Application.EDITOR.close();
				}
			});
			
			file.add(close);
		}
		
		
		// Zaviraci tlacitko
        
		menuBar.add(Box.createHorizontalGlue());
		
		EditButton edit = new EditButton();
		menuBar.add(edit);
		edit.setEnabled(vhdrFile.isEditable());
		
		CloseButton close = new CloseButton();
		menuBar.add(close);
		close.setEnabled(vhdrFile.isCloseable());
	}
}
