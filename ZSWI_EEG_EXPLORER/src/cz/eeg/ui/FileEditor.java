package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.LANG;
import static cz.deznekcz.tool.Lang.LANGgenerate;

















import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;
import javax.swing.text.View;

import cz.eeg.Application;
import cz.eeg.Config;
import cz.eeg.data.EegFile;
import cz.eeg.io.FileReadingException;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.dialog.DialogType;
import cz.eeg.ui.explorer.FileBrowserPanel;
import cz.eeg.ui.fileeditor.EegFilePanel;
import cz.eeg.ui.fileeditor.MenuPanel;

/**
 * Instance of {@link FileEditor} represent an file editor
 * of *.vhdr files
 *
 * @author IT Crowd
 */
public class FileEditor extends JFrame {

	/** */
	private static final long serialVersionUID = 3766108424601008291L;
	
	public final static Config CONFIG = Application.CONFIG;
	/** Void tab for editor */
	public final static JPanel EXPLORER_TAB = new Explorer();
		
	/** Tabs frame */
	public final static JTabbedPane TABS = new JTabbedPane();
	
	/** List of openned files */
	private List<EegFile> openedFiles = new ArrayList<EegFile>();

	public static class MyTabbedPaneUI extends javax.swing.plaf.basic.BasicTabbedPaneUI {
		
	}
	
	/**
	 * Default constructor of instances of class {@link FileEditor}
	 */
	public FileEditor() {
		
		TABS.setUI(new MyTabbedPaneUI());
		
		TABS.setTabPlacement(JTabbedPane.TOP);
		//TABS.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		TABS.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				int index = TABS.getSelectedIndex();
				
				TABS.setSelectedIndex(index);
				if (openedFiles.size() == 0 || index == 0) {
					FileEditor.this.setTitle(LANG("window_title"));
				} else {
					FileEditor.this.setTitle(LANG("file") + ": " + TABS.getTitleAt(index));
				}
			}
		});
		
		TABS.add(EXPLORER_TAB);
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		setLayout(new BorderLayout());
		// Panel added to each VhdrFile
		// WINDOW.add(MENU_PANEL, BorderLayout.NORTH);
		add(TABS, BorderLayout.CENTER);
		
		loadWindowLocation();
	}

	@Override
	public void setVisible(boolean b) {
		
		if (!b) {
			saveWindowLocation();
			while(isOpenedFiles() && close());
			if (!isOpenedFiles()) {
				CONFIG.folder_input = FileBrowserPanel.PANEL.getCurrentDirectory().getAbsolutePath();
				
				Application.exit();
			}
		} else if (b) {
			loadWindowLocation();
			super.setVisible(b);
		}
	}

	/**
	 * Method open an instance of {@link EegFile}
	 * as new panel
	 * @param instancedEegFile instance of {@link EegFile}
	 */
	public void open(EegFile instancedEegFile) {
		TABS.addTab(instancedEegFile.getName(), EegFilePanel.create(instancedEegFile));
		
		openedFiles.add(instancedEegFile);
		TABS.setSelectedIndex(TABS.getTabCount()-1);
	}
	
	/**
	 * Method open the {@link FileEditor}. Opens new files if is selected
	 * in focused selection frame.
	 * @param listOfFiles
	 */
	public void open(File[] listOfFiles) {
		int opened = 0;
		
		List<File> nonReadable = new ArrayList<File>();
		if (listOfFiles != null && listOfFiles.length > 0) {
			for (File file : listOfFiles) {
				// Non traversable files
				if (file.isDirectory())
					continue;
				
				try {
					EegFile vhdrSoubor = FilesIO.read(file);
					
					if (!vhdrSoubor.isReadable()) {
						throw new FileReadingException("Non reading");
					}
					
					open(vhdrSoubor);
					opened ++;
					
				} catch (Exception e) {
					e.printStackTrace();
					nonReadable.add(file);
				}
			}
			
			if (nonReadable.size() > 0) {
				DialogManagement.open(DialogType.ERROR, LANG("file_wrong") + list(nonReadable));
			}
		}
	}
	
	/**
	 * Method makes a lined {@link String} list of unreadable files
	 * from {@link List<File>}
	 * @param nonReadable {@link List} of files
	 * @return formate {@link String}
	 */
	private String list(List<File> nonReadable) {
		String out = "\n" + nonReadable.get(0).getName();
		Iterator<File> i = nonReadable.iterator();
		if (i.hasNext()) {
			i.next();
			for (; i.hasNext();) {
				out = out + ",\n" + i.next().getName();
			}
		}
		return out;
	}

	/**
	 * Method closes actual selected file
	 * @return 	true - file is succesfully closed<br>
	 * 			false - file is not closed
	 */
	public boolean close() {
		
		if (isOpenedFiles()) {
			int index = TABS.getSelectedIndex();
			if (index == 0) {
				index = 1;
			}
			int fileIndex = index - 1;
			EegFile soubor = openedFiles.get(fileIndex);
			
			if (soubor.needSave()) {
				
				boolean closed = JOptionPane.OK_OPTION == 
						JOptionPane.showConfirmDialog(null, 
								LANG("file_close", soubor.getName()), 
								LANG("file"), JOptionPane.OK_CANCEL_OPTION);
				if (closed) {
					if (soubor.isTemporary()) {
						FilesIO.freeTemporary(soubor);
					}
					openedFiles.remove(fileIndex);
					TABS.remove(index);
					return true; // file is successfully closed
				} else {
					return false; // file can't be closed
				}
				
			} else { // does not need save
				openedFiles.remove(fileIndex);
				TABS.remove(index);
				return true; // file is successfully closed
			}
		} else {
			return true; // nothing to close - closing is successful
		}
		
		
	}
	
	/**
	 * Method reads params of last window settings
	 * from instance of class {@link Config}
	 */
	private void loadWindowLocation() {
		setPreferredSize(new Dimension(CONFIG.ed_width, CONFIG.ed_height));
		if (CONFIG.isSet()) {
			setLocation(CONFIG.ed_posx, CONFIG.ed_posy);
			if ((CONFIG.ed_fullscreen & JFrame.MAXIMIZED_BOTH) != JFrame.MAXIMIZED_BOTH) {
				setPreferredSize(new Dimension(CONFIG.ed_width, CONFIG.ed_height));
			}
			setExtendedState(CONFIG.ed_fullscreen);
		}
		
		pack();
	}
	
	/**
	 * Method writes params of last window settings<br>
	 * <font color="red">WARNING!</font>Window muss be visible
	 * from instance of class {@link Config}
	 */
	private void saveWindowLocation() {
		CONFIG.ed_fullscreen = getExtendedState();
		CONFIG.ed_posx = getLocation().x;
		CONFIG.ed_posy = getLocation().y;
		CONFIG.ed_width = getWidth();
		CONFIG.ed_height = getHeight();
	}

	/**
	 * Method returns true if the List of opened files
	 * contains 1 or more files
	 * @return true/false
	 */
	public boolean isOpenedFiles() {
		return openedFiles.size() > 0;
	}

	/**
	 * Method calls a new {@link MarkerEditor} for editing markers
	 * of currency selected {@link EegFile}
	 */
	public void edit() {
		int index = TABS.getSelectedIndex() - 1;
		EegFile file = openedFiles.get(index);
		try {
			new MarkerEditor(file);
		} catch (Exception e) {
			DialogManagement.open(DialogType.ERROR, 
					LANG("marker_reading_error", e.getMessage()));
		}
	}

	public List<EegFile> getOpenedFiles() {
		return openedFiles;
	}
}
