package cz.eeg.ui.fileeditor;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;

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
import cz.eeg.ui.FileEditor;
import cz.eeg.ui.GuiManager;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.explorer.FileBrowserPanel;
import cz.eeg.ui.explorer.Scenario;

/**
 * Internal class representing a menu panel
 * for editing the visible file.
 * 
 * @author IT Crowd
 */
public class MenuPanel extends JPanel {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param vhdrFile is not an array, allows only 0 or 1 parameter
	 */
	public MenuPanel(EegFile... instanceOfFile) {
		setLayout(new BorderLayout());
		
		// menu bar

		final JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		
		if (instanceOfFile.length == 1) {
			final EegFile vhdrFile = instanceOfFile[0];
			
			// file menu
			menuBar.add(fileMenu(vhdrFile));
			
			// plotter menu
			menuBar.add(plotterMenu(vhdrFile));
			
			// merge menu
			menuBar.add(mergeMenu(vhdrFile));
			
			// fill
	        menuBar.add(Box.createHorizontalGlue());
			
	        // buttons
			EditButton edit = new EditButton();
			menuBar.add(edit);
			edit.setEnabled(vhdrFile.isEditable());
			
			CloseButton close = new CloseButton();
			menuBar.add(close);
			close.setEnabled(vhdrFile.isCloseable());
		} else {
			
			// explorer file menu
			menuBar.add(explorerMenu());

			// scenario menu
			menuBar.add(scenarioMenu());

			// fill
	        menuBar.add(Box.createHorizontalGlue());
	        
			// scenario menu
			menuBar.add(helpMenu());
		}
	}

	private JMenu helpMenu() {
		final JMenu help = new JMenu(LANG("credits"));
		
		final JMenuItem about = new JMenuItem(LANG("credits_about"));
		help.add(about);
		
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DialogManagement.open(DialogManagement.ABOUT);
			}
		});
		return help;
	}

	private JMenu scenarioMenu() {
		final JMenu scenario = new JMenu(LANG("explorer_scenario"));
		{
			final JMenuItem addScen = new JMenuItem(LANG("explorer_scenario_add"));
			addScen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DialogManagement.open(DialogManagement.SCENARIO_ADD);
				}
			});
			scenario.add(addScen);
			
			final ActionListener deleter = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String name = ((JMenuItem) e.getSource()).getText();
					DialogManagement.open(DialogManagement.SCENARIO_REMOVE, name);
				}
			};
			
			final JMenu list = new JMenu(LANG("explorer_scenario_delete"));
			ArrayList<String> scenarios = Scenario.getList();
			if (scenarios.size() == 0) {
				list.setEnabled(false);
			}
			list.addMenuListener(new MenuListener() {
				@Override
				public void menuSelected(MenuEvent e) {
					list.removeAll();
					ArrayList<String> scenarios = Scenario.getList();
					for (int i = 0; i < scenarios.size(); i++) {
						JMenuItem menuItem = new JMenuItem(scenarios.get(i));
						menuItem.addActionListener(deleter);
						list.add(menuItem);
					}
					list.setEnabled(scenarios.size() > 0);
				}
				@Override public void menuDeselected(MenuEvent e) {}
				@Override public void menuCanceled(MenuEvent e) {}
			});;
			scenario.add(list);
		}
		return scenario;
	}

	private JMenu explorerMenu() {

		final JMenu file = new JMenu(LANG("file"));
		
		final JMenuItem editor = new JMenuItem(LANG("explorer_open"));

		editor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				GuiManager.EDITOR.open( (File[]) // if is the list null
						FileBrowserPanel.PANEL.getSelectedFiles());
			}
		});
		
		file.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent e) {
				editor.setEnabled(FileBrowserPanel.PANEL.getSelectedFile() != null);
			}
			
			@Override
			public void menuDeselected(MenuEvent e) {
				
			}
			
			@Override
			public void menuCanceled(MenuEvent e) {
				
			}
		});
		
		file.add(editor);
        
        file.addSeparator();
        
		// exit explorer

		final JMenuItem exit = new JMenuItem(LANG("exit"));
		exit.repaint();

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				GuiManager.EDITOR.setVisible(false);
			}
		});
		file.add(exit);

		return file;
	}

	private JMenu fileMenu(final EegFile vhdrFile) {
		final JMenu file = new JMenu(LANG("file"));
		{
			// Soubor item

			final JMenuItem s1 = new JMenuItem(LANG("file_open"));
			
			s1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					FileEditor.TABS.setSelectedIndex(0);
				}
			}); file.add(s1);

			JMenuItem saveAs = new JMenuItem(LANG("file_save_as"));
			
			saveAs.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Out<Boolean> out = new Out<>(false);
					DialogManagement.open(DialogManagement.SAVE_AS, vhdrFile, out);
					if (out.value()) {
						FileEditor.TABS.setTitleAt(FileEditor.TABS.getSelectedIndex(), vhdrFile.getName());
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
		return file;
	}

	private JMenu plotterMenu(final EegFile vhdrFile) {
		final JMenu data = new JMenu(LANG("menu_data"));
		
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
		return data;
	}

	private JMenu mergeMenu(final EegFile vhdrFile) {
		final JMenu merge = new JMenu(LANG("merge_file"));
		
		{
			merge.addMenuListener(new MenuListener() {
				
				@Override
			    public void menuSelected(MenuEvent e) {
					merge.removeAll();
					List<EegFile> files = GuiManager.EDITOR.getOpenedFiles();
			
					Out<String> memoryError = new Out<String>("merge_no_mergeables");
					
					for (EegFile file : files) {
						if (FilesIO.isMergeable(vhdrFile, file, memoryError)) {
							merge.add(MergeItem.from(vhdrFile, file));
						}
					}
					
					if (merge.getItemCount() == 0) {
						merge.add(MergeItem.voidItem(memoryError.value()));
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
		}
		return merge;
	}

	public static JPanel explorerMenuPanel() {
		return new MenuPanel();
	}
}
