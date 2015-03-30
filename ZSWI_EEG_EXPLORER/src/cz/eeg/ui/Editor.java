package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.eeg.data.Vhdr;
import cz.eeg.tool.Config;
import cz.eeg.ui.editor.CloseButton;
import cz.eeg.ui.editor.Panels;
import cz.eeg.ui.editor.Dialog;
import cz.eeg.ui.editor.EditButton;
import cz.eeg.ui.editor.MenuPanel;

/**
 * Instance of {@link Editor} represent an file editor
 * of *.vhdr files
 *
 * @author IT Crowd
 */
public class Editor extends JTabbedPane {

	public final static Config CONFIG = Application.CONFIG;
	/** Void tab for editor */
	public final static JPanel VOID_TAB = new JPanel();
		
	/** Window frame */
	public final static JFrame WINDOW = new JFrame(){
		private static final long serialVersionUID = 1L;

		@Override public void setVisible(boolean arg0) { instance.setVisible(arg0); super.setVisible(arg0); };
	};
	/** Intenal instace of {@link Editor}, is needed for visibility control */
	private static Editor instance;
	
	/** List of openned files */
	private List<Vhdr> openedFiles = new ArrayList<Vhdr>();
	
	/**
	 * Default constructor of instaces of class {@link Editor}
	 */
	public Editor() {
		
		instance = this;
		
		add(voidTab());
		
		WINDOW.setLayout(new BorderLayout());
		// Panel added to each VhdrFile
		// WINDOW.add(MENU_PANEL, BorderLayout.NORTH);
		WINDOW.add(this, BorderLayout.CENTER);
		
		loadWindowLocation();
	}
	
	private Component voidTab() {
		VOID_TAB.setName(LANG("editor_no_file"));
		VOID_TAB.setEnabled(false);
		VOID_TAB.setLayout(new BorderLayout());
		VOID_TAB.add(new MenuPanel(Vhdr.voidFile()), BorderLayout.NORTH);
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
	 * Method open the {@link Editor}. Opens new files if is selected
	 * in focused selection frame.
	 * @param listOfFiles
	 */
	public void open(File[] listOfFiles) {
		List<File> nonReadable = new ArrayList<File>();
		if (listOfFiles != null && listOfFiles.length > 0) {
			for (File file : listOfFiles) {
				Vhdr vhdrSoubor = new Vhdr(file, true);
				if (vhdrSoubor.isReadable()) {
					openedFiles.add(vhdrSoubor);
					addTab(vhdrSoubor.getName(), Panels.filePanel(vhdrSoubor));
					setSelectedIndex(getTabCount()-1);
				} else {
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

			if (listOfFiles == null || nonReadable.size() < listOfFiles.length)
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
			Vhdr soubor = openedFiles.get(index);
			
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
			WINDOW.setLocationRelativeTo(null);
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
		Vhdr file = openedFiles.get(index);
		//TODO soubor.ulozit(nazev);
	}

	public void edit() {
		
	}
}
