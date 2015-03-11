package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.*;
import java.awt.BorderLayout;
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

import cz.eeg.Appliacion;
import cz.eeg.data.vhdrmerge.Vhdr;
import cz.eeg.tool.Config;
import cz.eeg.ui.editor.Dialog;

public class Editor extends JTabbedPane {

	public final static Config CONFIG = Appliacion.CONFIG;
	/** Menu panel */
	public final static JPanel MENU_PANEL = new JPanel();
	/** Void tab for editor */
	public final static JPanel VOID_TAB = new JPanel();
		
	/** Window frame */
	public final static JFrame WINDOW = new JFrame(){
		private static final long serialVersionUID = 1L;

		@Override public void setVisible(boolean arg0) { instance.setVisible(arg0); super.setVisible(arg0); };
	};
	
	private static final CloseButton CLOSE_BUTTON = new CloseButton();
	/** Menu item: SAVE_AS */
	private static JMenuItem MENU_SA;
	/** Intenal instace of {@link Editor}, is needed for visibility control */
	private static Editor instance;
	
	/** List of openned files */
	private List<Vhdr> openedFiles = new ArrayList<Vhdr>();
	
	/**
	 * Default constructor of instaces of class {@link Editor}
	 */
	public Editor() {
		
		instance = this;
		
		VOID_TAB.setName(LANG("editor_no_file"));
		VOID_TAB.repaint();
		VOID_TAB.setEnabled(false);
		add(VOID_TAB);
		
		WINDOW.setLayout(new BorderLayout());
		WINDOW.add(MENU_PANEL, BorderLayout.NORTH);
		WINDOW.add(this, BorderLayout.CENTER);
		
		MENU_PANEL.setLayout(new BorderLayout());
		//PANEL_TLACITEK.add(new CloseButton(), BorderLayout.EAST);
		
		// Soubor menu

		final JMenuBar menuBar = new JMenuBar();
		MENU_PANEL.add(menuBar, BorderLayout.NORTH);
		
		// Soubor menu

		final JMenu file = new JMenu(LANG("file"));
		menuBar.add(file);
		{
			// Soubor item

			final JMenuItem s1 = new JMenuItem(LANG("file_open"));
			
			s1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					Appliacion.WINDOW.requestFocus();
				}
			}); file.add(s1);
			
			// Uložení

			MENU_SA = new JMenuItem(LANG("file_save_as"));
			
			MENU_SA.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					new Dialog(Dialog.SAVE_AS);
				}
			}); file.add(MENU_SA);
			MENU_SA.setEnabled(false);
		}
		
		
		// Zaviraci tlacitko
        
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(CLOSE_BUTTON);
		CLOSE_BUTTON.setEnabled(false);
		
		loadWindowLocation();
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
	 * @param isSelectedFiles
	 */
	public void open(boolean isSelectedFiles) {
		List<File> nonReadable = new ArrayList<File>();
		if (isSelectedFiles && Appliacion.selectionFrame != null) {
			File[] files = Appliacion.selectionFrame.getSelectedFiles();
			if (files != null) {
				for (File file : files) {
					Vhdr vhdrSoubor = new Vhdr(file, true);
					if (!vhdrSoubor.isReadable()) {
						nonReadable.add(file);
						continue;
					}
					openedFiles.add(vhdrSoubor);
					addTab(vhdrSoubor.getName(), vhdrSoubor);
					setSelectedIndex(getTabCount()-1);
				}
			}
			
			if (nonReadable.size() > 0) {
				JOptionPane.showMessageDialog(null,
						LANG("file_wrong") + list(nonReadable), 
						LANG("error"), JOptionPane.ERROR_MESSAGE);
			}
			
			if (openedFiles.size() > 0) {
				remove(VOID_TAB);
				CLOSE_BUTTON.setEnabled(true);
				MENU_SA.setEnabled(true);
			}	

			if (files == null || nonReadable.size() < files.length)
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
			add(VOID_TAB);
			CLOSE_BUTTON.setEnabled(false);
			MENU_SA.setEnabled(false);
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
}

/**
 * Internal class representing a close button,
 * On pressing of this button the editor closes
 * the visible file.
 * 
 * @author IT Crowd
 */
class CloseButton extends JButton {
	public CloseButton() {
		super("X");
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Appliacion.EDITOR.close();
			}
		});
	}
}