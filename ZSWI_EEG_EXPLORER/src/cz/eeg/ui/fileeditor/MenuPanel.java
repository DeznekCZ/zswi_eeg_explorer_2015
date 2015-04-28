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

import cz.eeg.data.Channel;
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
			
			//file.addSeparator();
			
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
			
			close.setEnabled(vhdrFile.isReadable());
			file.add(close);
		}
		
		final JMenu data = new JMenu(LANG("menu_data"));
		menuBar.add(data);
		
		if (!vhdrFile.isPlotAble()) {
			JMenuItem noChannels = new JMenuItem(LANG("menu_data_no_channels"));
			noChannels.setEnabled(false);
			data.add(noChannels);
		} else {
			
			JMenuItem select = new JMenuItem(LANG("menu_data_select"));
			select.addActionListener(new ActionListener() {		
				@Override
				public void actionPerformed(ActionEvent e) {
					DialogManagement.open(DialogManagement.PLOTING, vhdrFile);
				}
			});
			//select.setEnabled(false);
			data.add(select);

			data.addSeparator();
			
			final Channel[] channels = vhdrFile.getChannel();
			
			for (int i = 0; i < channels.length; i++) {
				final int index = i;
				JMenuItem channel = new JMenuItem(channels[i].getName());
				channel.addActionListener(new ActionListener() {		
					@Override
					public void actionPerformed(ActionEvent e) {
						Plotter.open(vhdrFile, index);
					}
				});
				data.add(channel);
			}
			
			data.addSeparator();
			
			JMenuItem allChannels = new JMenuItem(LANG("menu_data_all_channels"));
			allChannels.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Plotter.open(vhdrFile);
				}
			});
			data.add(allChannels);
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
