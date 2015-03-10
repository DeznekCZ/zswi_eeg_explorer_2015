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

import cz.eeg.Aplikace;
import cz.eeg.data.vhdrmerge.Vhdr;
import cz.eeg.tool.Config;
import cz.eeg.tool.Lang;
import cz.eeg.ui.editor.Ulozit;

public class Editor extends JTabbedPane {

	public final static Config CONFIG = Aplikace.CONFIG;
	//public final static Lang LANG = Aplikace.LANG;
	public final static JPanel PANEL_TLACITEK = new JPanel();
	public final static JPanel HLAVNI_ZALOZKA = new JPanel() { 
		public void repaint() {setName(LANG("editor_no_file")); super.repaint();};};
		
	public final static JFrame OKNO = new JFrame(){
		@Override public void setVisible(boolean arg0) { instance.setVisible(arg0); super.setVisible(arg0); };
	};
	
	private static final CloseButton CLOSE_BUTTON = new CloseButton();
	private static JMenuItem MENU_ULOZIT;
	private static Editor instance;
	
	private List<Vhdr> otevreneSoubory = new ArrayList<Vhdr>();
	
	public Editor() {
		
		instance = this;
		
		HLAVNI_ZALOZKA.repaint();
		HLAVNI_ZALOZKA.setEnabled(false);
		add(HLAVNI_ZALOZKA);
		
		OKNO.setLayout(new BorderLayout());
		OKNO.add(PANEL_TLACITEK, BorderLayout.NORTH);
		OKNO.add(this, BorderLayout.CENTER);
		
		PANEL_TLACITEK.setLayout(new BorderLayout());
		//PANEL_TLACITEK.add(new CloseButton(), BorderLayout.EAST);
		
		// Soubor menu

		final JMenuBar menuBar = new JMenuBar();
		PANEL_TLACITEK.add(menuBar, BorderLayout.NORTH);
		
		// Soubor menu

		final JMenu soubor = new JMenu() {
			public void repaint() { setText(LANG("file")); super.repaint(); };
		}; soubor.repaint();
		menuBar.add(soubor);
		{
			// Soubor item

			final JMenuItem s1 = new JMenuItem() {
				public void repaint() { setText(LANG("file_open")); super.repaint(); };
			}; s1.repaint();
			
			s1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					Aplikace.OKNO.requestFocus();
				}
			}); soubor.add(s1);
			
			// Uložení

			MENU_ULOZIT = new JMenuItem() {
				public void repaint() { setText(LANG("file_save")); super.repaint(); };
			}; MENU_ULOZIT.repaint();
			
			MENU_ULOZIT.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					new Ulozit(Ulozit.ULOZIT);
				}
			}); soubor.add(MENU_ULOZIT);
			MENU_ULOZIT.setEnabled(false);
		}
		
		
		// Zaviraci tlacitko
        
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(CLOSE_BUTTON);
		CLOSE_BUTTON.setEnabled(false);
		
		nactiUmisteni();
	}
	
	@Override
	public void setVisible(boolean b) {
		if (!b && Aplikace.seUkoncuje()) {
			boolean zavirat = Aplikace.seUkoncuje();
			while (zavirat && otevreneSoubory.size() > 0) {
				zavirat = zavrit();
			}
		}
		
		if (!b && OKNO.isVisible()) {
			ulozUmisteni();
		} else if (b && !OKNO.isVisible()) {
			nactiUmisteni();
		}
	}

	public void otevrit(boolean nove) {
		List<File> nonReadable = new ArrayList<File>();
		if (nove && Aplikace.oknoVyberu != null) {
			File[] soubory = Aplikace.oknoVyberu.getSelectedFiles();
			if (soubory != null) {
				for (File file : soubory) {
					Vhdr vhdrSoubor = new Vhdr(file, true);
					if (!vhdrSoubor.isReadable()) {
						nonReadable.add(file);
						continue;
					}
					otevreneSoubory.add(vhdrSoubor);
					addTab(vhdrSoubor.getName(), vhdrSoubor);
					setSelectedIndex(getTabCount()-1);
				}
			}
			
			if (nonReadable.size() > 0) {
				JOptionPane.showMessageDialog(null,
						LANG("file_wrong") + list(nonReadable), 
						LANG("error"), JOptionPane.ERROR_MESSAGE);
			}
			
			if (otevreneSoubory.size() > 0) {
				remove(HLAVNI_ZALOZKA);
				CLOSE_BUTTON.setEnabled(true);
				MENU_ULOZIT.setEnabled(true);
			}	

			if (soubory == null || nonReadable.size() < soubory.length)
				OKNO.setVisible(true);
			return;
		}
		
		OKNO.setVisible(true);
	}
	
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

	public boolean zavrit() {
		boolean zavritelne = true;
		
		if (jsouOtevreneSoubory()) {
			OKNO.setVisible(true);
			int index = getSelectedIndex();
			Vhdr soubor = otevreneSoubory.get(index);
			
			int option = JOptionPane.showConfirmDialog(null, 
					LANG("file_close", soubor.getName()), LANG("file"), JOptionPane.OK_CANCEL_OPTION);
			
			if (option == JOptionPane.OK_OPTION) {
				otevreneSoubory.remove(index);
				remove(index);
			} else {
				zavritelne = false;
			}
		}
		
		if (otevreneSoubory.size() == 0) {
			add(HLAVNI_ZALOZKA);
			CLOSE_BUTTON.setEnabled(false);
			MENU_ULOZIT.setEnabled(false);
		}

		return zavritelne;
	}
	
	private void nactiUmisteni() {
		OKNO.setPreferredSize(new Dimension(CONFIG.ed_width, CONFIG.ed_height));
		if (CONFIG.isSet()) {
			OKNO.setLocation(CONFIG.ed_posx, CONFIG.ed_posy);
			if ((CONFIG.ed_fullscreen & JFrame.MAXIMIZED_BOTH) != JFrame.MAXIMIZED_BOTH) {
				setPreferredSize(new Dimension(CONFIG.ed_width, CONFIG.ed_height));
			}
			OKNO.setExtendedState(CONFIG.ed_fullscreen);
		} else {
			OKNO.setLocationRelativeTo(null);
		}
		
		OKNO.pack();
	}
	
	private void ulozUmisteni() {
		CONFIG.ed_fullscreen = OKNO.getExtendedState();
		CONFIG.ed_posx = OKNO.getLocation().x;
		CONFIG.ed_posy = OKNO.getLocation().y;
		CONFIG.ed_width = OKNO.getWidth();
		CONFIG.ed_height = OKNO.getHeight();
	}
	
	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		if (otevreneSoubory.size() == 0) {
			OKNO.setTitle(LANG("editor_title"));
		} else {
			OKNO.setTitle(LANG("file") + ": " + getTitleAt(index));
		}
	}

	public boolean jsouOtevreneSoubory() {
		return otevreneSoubory.size() > 0;
	}

	public void ulozitSoubor(String nazev) {
		int index = getSelectedIndex();
		Vhdr soubor = otevreneSoubory.get(index);
		//soubor.ulozit(nazev);
	}
}

class CloseButton extends JButton {
	public CloseButton() {
		super("X");
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Aplikace.EDITOR.zavrit();
			}
		});
	}
}