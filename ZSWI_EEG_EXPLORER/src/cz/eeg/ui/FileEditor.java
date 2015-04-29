package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.eeg.data.EegFile;
import cz.eeg.io.FileReadingException;
import cz.eeg.io.FilesIO;
import cz.eeg.tool.Config;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.fileeditor.MenuPanel;
import cz.eeg.ui.fileeditor.EegFilePanel;

/**
 * Instance of {@link FileEditor} represent an file editor
 * of *.vhdr files
 *
 * @author IT Crowd
 */
public class FileEditor extends JTabbedPane {

	public final static Config CONFIG = Application.CONFIG;
	/** Void tab for editor */
	public static JPanel VOID_TAB = voidTab();
		
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
		
		add(voidTab());
		
		WINDOW.setLayout(new BorderLayout());
		// Panel added to each VhdrFile
		// WINDOW.add(MENU_PANEL, BorderLayout.NORTH);
		WINDOW.add(this, BorderLayout.CENTER);
		
		loadWindowLocation();
	}
	
	private static JPanel voidTab() {
		VOID_TAB = new MenuPanel(EegFile.voidFile());
		VOID_TAB.setName(LANG("editor_no_file"));
		return VOID_TAB;
	}

	@Override
	public void setVisible(boolean b) {
		
		if (!b && WINDOW.isVisible()) {
			saveWindowLocation();
		} else if (b && !WINDOW.isVisible()) {
			loadWindowLocation();
		}
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
					
					addTab(vhdrSoubor.getName(), EegFilePanel.create(vhdrSoubor));
					openedFiles.add(vhdrSoubor);
					setSelectedIndex(getTabCount()-1);
					opened ++;
					
				} catch (Exception e) {
					e.printStackTrace();
					nonReadable.add(file);
				}
			}
			
			if (nonReadable.size() > 0) {
				JOptionPane.showMessageDialog(null,
						LANG("file_wrong") + list(nonReadable), 
						LANG("error"), JOptionPane.ERROR_MESSAGE);
			}
			
			if (openedFiles.size() > 0) {
				remove(VOID_TAB);
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
			
			int option = JOptionPane.showConfirmDialog(null, 
					LANG("file_close", soubor.getName()), LANG("file"), JOptionPane.OK_CANCEL_OPTION);
			
			if (option == JOptionPane.OK_OPTION) {
				openedFiles.remove(index);
				remove(index);
			} else {
				closeAble = false;
			}
		}
		
		if (openedFiles.size() == 0) {
			add(voidTab());
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
			WINDOW.setLocationRelativeTo(Application.EXPLORER);
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
	 * Method saves the selected file to output
	 * directory with a specific name
	 * @param name name of file
	 */
	public void saveAs(String name) {
		int index = getSelectedIndex();
		EegFile file = openedFiles.get(index);
		//TODO soubor.ulozit(nazev);
	}

	public void edit() {
		int index = getSelectedIndex();
		EegFile file = openedFiles.get(index);
		try {
			new MarkerEditor(file.getMarkerList());
		} catch (Exception e) {
			DialogManagement.open(DialogManagement.MARKER_ERROR, e);
		}
	}
}
