package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import cz.eeg.Application;
import cz.eeg.Config;
import cz.eeg.data.EegFile;
import cz.eeg.io.FileReadingException;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.fileeditor.EegFilePanel;
import cz.eeg.ui.fileeditor.MenuPanel;

/**
 * Instance of {@link FileEditor} represent an file editor
 * of *.vhdr files
 *
 * @author IT Crowd
 */
public class FileEditor extends JTabbedPane {

	/** */
	private static final long serialVersionUID = 3766108424601008291L;
	
	public final static Config CONFIG = Application.CONFIG;
	/** Void tab for editor */
	public static JPanel voidTab = initVoidTab();
		
	/** Window frame */
	public final static JFrame WINDOW = new JFrame(){
		private static final long serialVersionUID = 1L;

		@Override public void setVisible(boolean arg0) { instance.setVisible(arg0); super.setVisible(arg0); };
	};
	/** Intenal instace of {@link FileEditor}, is needed for visibility control */
	private static FileEditor instance;
	
	/** List of openned files */
	private List<EegFile> openedFiles = new ArrayList<EegFile>();
	
	/**
	 * Default constructor of instaces of class {@link FileEditor}
	 */
	public FileEditor() {
		
		instance = this;
		
		add(initVoidTab());
		
		WINDOW.setLayout(new BorderLayout());
		// Panel added to each VhdrFile
		// WINDOW.add(MENU_PANEL, BorderLayout.NORTH);
		WINDOW.add(this, BorderLayout.CENTER);
		
		loadWindowLocation();
	}
	
	private static JPanel initVoidTab() {
		voidTab = new MenuPanel(EegFile.voidFile());
		voidTab.setName(LANG("editor_no_file"));
		return voidTab;
	}

	@Override
	public void setVisible(boolean b) {
		
		if (!b && WINDOW.isVisible()) {
			saveWindowLocation();
			GuiManager.EXPLORER.requestFocus();
		} else if (b && !WINDOW.isVisible()) {
			loadWindowLocation();
		}
	}

	/**
	 * Method open an instance of {@link EegFile}
	 * as new panel
	 * @param instancedEegFile instance of {@link EegFile}
	 */
	public void open(EegFile instancedEegFile) {
		addTab(instancedEegFile.getName(), EegFilePanel.create(instancedEegFile));
		
		openedFiles.add(instancedEegFile);
		setSelectedIndex(getTabCount()-1);
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
				DialogManagement.open(DialogManagement.ERROR, LANG("file_wrong") + list(nonReadable));
			}
			
			if (openedFiles.size() > 0) {
				remove(voidTab);
			}	

			if (listOfFiles == null || opened > 0)
				WINDOW.setVisible(true);
			return;
		}
		
		WINDOW.setVisible(true);
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
		boolean closeAble = true;
		
		if (isOpenedFiles()) {
			WINDOW.setVisible(true);
			int index = getSelectedIndex();
			EegFile soubor = openedFiles.get(index);
			
			if (soubor.needSave() &&
				JOptionPane.OK_OPTION == 
					JOptionPane.showConfirmDialog(null, 
							LANG("file_close", soubor.getName()), 
							LANG("file"), JOptionPane.OK_CANCEL_OPTION)
				) {
				if (soubor.isTemporary()) {
					FilesIO.freeTemporary(soubor);
				}
				openedFiles.remove(index);
				remove(index);
			} else if (!soubor.needSave()){
				openedFiles.remove(index);
				remove(index);
			} else {
				closeAble = false;
			}
		}
		
		if (openedFiles.size() == 0) {
			add(initVoidTab());
		}

		return closeAble;
	}
	
	/**
	 * Method reads params of last window settings
	 * from instance of class {@link Config}
	 */
	private void loadWindowLocation() {
		WINDOW.setPreferredSize(new Dimension(CONFIG.ed_width, CONFIG.ed_height));
		if (CONFIG.isSet()) {
			WINDOW.setLocation(CONFIG.ed_posx, CONFIG.ed_posy);
			if ((CONFIG.ed_fullscreen & JFrame.MAXIMIZED_BOTH) != JFrame.MAXIMIZED_BOTH) {
				setPreferredSize(new Dimension(CONFIG.ed_width, CONFIG.ed_height));
			}
			WINDOW.setExtendedState(CONFIG.ed_fullscreen);
		} else {
			WINDOW.setLocationRelativeTo(GuiManager.EXPLORER);
		}
		
		WINDOW.pack();
	}
	
	/**
	 * Method writes params of last window settings<br>
	 * <font color="red">WARNING!</font>Window muss be visible
	 * from instance of class {@link Config}
	 */
	private void saveWindowLocation() {
		CONFIG.ed_fullscreen = WINDOW.getExtendedState();
		CONFIG.ed_posx = WINDOW.getLocation().x;
		CONFIG.ed_posy = WINDOW.getLocation().y;
		CONFIG.ed_width = WINDOW.getWidth();
		CONFIG.ed_height = WINDOW.getHeight();
	}
	
	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		if (openedFiles.size() == 0) {
			WINDOW.setTitle(LANG("editor_title"));
		} else {
			WINDOW.setTitle(LANG("file") + ": " + getTitleAt(index));
		}
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
		int index = getSelectedIndex();
		EegFile file = openedFiles.get(index);
		try {
			new MarkerEditor(file);
		} catch (Exception e) {
			DialogManagement.open(DialogManagement.ERROR, 
					LANG("marker_reading_error", e.getMessage()));
		}
	}

	public List<EegFile> getOpenedFiles() {
		return openedFiles;
	}
}
