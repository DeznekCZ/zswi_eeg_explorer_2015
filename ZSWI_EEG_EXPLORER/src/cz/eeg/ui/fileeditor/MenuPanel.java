package cz.eeg.ui.fileeditor;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.BorderLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import cz.deznekcz.reflect.Out;
import cz.eeg.data.Channel;
import cz.eeg.data.EegFile;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.GuiManager;
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
		
		// menu bar

		final JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		
		// file menu

		final JMenu file = new JMenu(LANG("file"));
		menuBar.add(file);
		{
			// Soubor item

			final JMenuItem s1 = new JMenuItem(LANG("file_open"));
			
			s1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					GuiManager.EXPLORER.requestFocus();
				}
			}); file.add(s1);

			JMenuItem saveAs = new JMenuItem(LANG("file_save_as"));
			
			saveAs.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Out<Boolean> out = new Out<>(false);
					DialogManagement.open(DialogManagement.SAVE_AS, vhdrFile, out);
					if (out.value()) {
						GuiManager.EDITOR.setTitleAt(GuiManager.EDITOR.getSelectedIndex(), vhdrFile.getName());
					}
				}
			});
			
			saveAs.setEnabled(vhdrFile.isSaveable());
			file.add(saveAs);
			
			file.addSeparator();
			
			JMenuItem close = new JMenuItem(LANG("file_close_menu"));
			close.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					GuiManager.EDITOR.close();
				}
			});
			
			close.setEnabled(vhdrFile.isReadable());
			file.add(close);
		}
		
		// plotter menu
		
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
		
		// merge menu
		
		final JMenu merge = new JMenu(LANG("merge_file"));
		menuBar.add(merge);
		
		{
			merge.addMenuListener(new MenuListener() {
				
				@Override
			    public void menuSelected(MenuEvent e) {
					merge.removeAll();
					List<EegFile> files = GuiManager.EDITOR.getOpenedFiles();
			
					for (EegFile file : files) {
						if (FilesIO.isMergeable(vhdrFile, file)) {
							merge.add(MergeItem.from(vhdrFile, file));
						}
					}
					
					if (merge.getItemCount() == 0) {
						merge.add(MergeItem.voidItem());
					}
			    }

			    @Override
			    public void menuDeselected(MenuEvent e) {
			        //System.out.println("menuDeselected");
			    }

			    @Override
			    public void menuCanceled(MenuEvent e) {
			        //System.out.println("menuCanceled");
			    }
			});
			/*
			merge.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			});*/
		}
		
		// buttons
        
		menuBar.add(Box.createHorizontalGlue());
		
		EditButton edit = new EditButton();
		menuBar.add(edit);
		edit.setEnabled(vhdrFile.isEditable());
		
		CloseButton close = new CloseButton();
		menuBar.add(close);
		close.setEnabled(vhdrFile.isCloseable());
	}
}
