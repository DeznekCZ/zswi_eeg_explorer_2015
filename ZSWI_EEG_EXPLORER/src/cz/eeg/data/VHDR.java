package cz.eeg.data;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import cz.eeg.Aplikace;
import cz.eeg.tool.Lang;

public class VHDR extends JSplitPane {

	public static final Lang LANG = Aplikace.LANG;
	
	/** Soubor v souborovém systému */
	public File file;
	
	/** Přilinkovaný EEG nebo jiný */
	private DATA data;
	/** Soubor markerů */
	private VMRK marker;

	private JTextArea input;
	private JTextArea markerTable;
	
	private boolean readable = true;
	
	/**
	 * Otevře a zkontroluje interně čitelnost VHDR souboru
	 * Viditelnost říká, zda je soubor pouze testván, nebo
	 * jsou informace o něm i vykresleny
	 * 
	 * @param cestaSouboru cesta k souboru
	 * @param viditelnost true/false
	 */
	public VHDR(String cestaSouboru, boolean viditelnost) {
		this(new File(cestaSouboru), viditelnost);
	}
	
	/**
	 * Otevře a zkontroluje interně čitelnost VHDR souboru
	 * Viditelnost říká, zda je soubor pouze testván, nebo
	 * jsou informace o něm i vykresleny
	 * 
	 * @param soubor odkaz na soubor
	 * @param viditelnost true/false
	 */
	public VHDR(File soubor, boolean viditelnost) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		
		file = soubor;
		setName(file.getName());
		
		//TODO Tady začíná provizorní čtení VHDR
		//TODO Viditelnost znamená, zda je soubor otevírán v editoru
				// lze zanedbat, ale může to zpomalit běh načítání
				// seznamu souborů ve složce
		if (viditelnost) {
			input = new JTextArea("");
			JScrollPane jspi = new JScrollPane(input);
			add(jspi);
			markerTable = new JTextArea("");
			JScrollPane jspm = new JScrollPane(markerTable);
			add(jspm);
			setDividerLocation(Aplikace.EDITOR.getSize().width * 2 / 3);
		}
		
		//TODO Samotné načítání
		try {
			Scanner s = new Scanner(soubor);
			while(s.hasNext()) {
				String line = s.nextLine();
				String[] split = line.split("=");
				
				//TODO Získání adresy datového souboru
				if (split.length == 2 && split[0].equals("DataFile")) {
					data = new DATA(
							soubor.getParentFile()
								.getAbsolutePath()
								.concat("\\" + split[1]));
				} else
				//TODO Získání adresy markerového souboru
					if (split.length == 2 && split[0].equals("MarkerFile")) {
					marker = new VMRK(
							soubor.getParentFile()
								.getAbsolutePath()
								.concat("\\" + split[1]));
				}
				
				//TODO Přepsání řádku na obrazovku, pokud se bude kreslit
				if (viditelnost) {
					input.append(line + '\n');
				}
			}
			s.close();
		} catch (Exception e) {
			//TODO V případě nečitelnosti se nastaví jako nečitelný Header
			readable = false;
		}
		
		//TODO Načtení markerového souboru ale to bude nakonec vypadat jinak
		/*
		 * public cz.eeg.data.Marker[] zobrazMarkery()
		 *    pro VMRK.class  
		 *    cz.eeg.data.Marker[] = řádka tabulky s přepisováním
		 */
		if (viditelnost && isReadable()) {
			try {
				Scanner s = new Scanner(marker);
				while (s.hasNext()) {
					markerTable.append(s.nextLine() + '\n');
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Statická metoda kontrolující, zda je hHeadrový soubor čitelný
	 * @param f odkaz na soubor
	 * @return true/false
	 */
	public static boolean isReadable(File f) {
		if (new VHDR(f, false).isReadable())
			return true;
		return false;
	}
	
	/**
	 * Metoda kontrolující, zda je hHeadrový soubor čitelný
	 * @return true/false
	 */
	public boolean isReadable() {
		return readable && data.exists() && marker.exists();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	/**
	 * Uložení souboru s určitým názvem,
	 * soubor je automaticky ukládán do výstupního adresáře
	 * nastaveném v editoru
	 * @param nazev cílový název souboru
	 */
	public void ulozit(String nazev) {
		File out = new File(nazev);
		if (out.exists()) {
			int prepis = JOptionPane.showConfirmDialog(
					null, LANG.editor_overwrite + "\n" + nazev, LANG.editor_file_exists, 
					JOptionPane.YES_NO_OPTION);
			
			if (prepis == JOptionPane.YES_OPTION) {
				write(nazev);
			}
		} else {
			write(nazev);
		}
	}

	/**
	 * Metoda zapisující soubor do výstuního adresáře
	 * @param nazev název výstupního souboru
	 */
	private void write(String nazev) {
		//TODO nutno vyměnit za konstrukce která nejen překopíruje obsah,
				// ale i v případě ukládání téhož souboru též uloží
		String strDestinationFile=Aplikace.CONFIG.folder_output + "\\" + nazev + ".vhdr";
        
        try
        {
                //create FileInputStream object for source file
                FileInputStream fin = new FileInputStream(file);
               
                //create FileOutputStream object for destination file
                FileOutputStream fout = new FileOutputStream(strDestinationFile);
               
                byte[] b = new byte[1024];
                int noOfBytes = 0;
               
                //System.out.println("Copying file using streams");
               
                //read bytes from source file and write to destination file
                while( (noOfBytes = fin.read(b)) != -1 )
                {
                        fout.write(b, 0, noOfBytes);
                }
               
                //System.out.println("File copied!");
               
                //close the streams
                fin.close();
                fout.close();        
                
                //TODO otevřít
               
        }
        catch(FileNotFoundException fnf)
        {
                System.out.println("Specified file not found :" + fnf);
        }
        catch(IOException ioe)
        {
                System.out.println("Error while copying file :" + ioe);
        }
	}
}
