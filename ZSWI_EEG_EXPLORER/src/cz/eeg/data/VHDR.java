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
	
	public File file;
	
	private String nazev;
	private File data;
	private File marker;

	private JTextArea input;
	private JTextArea markerTable;
	
	private boolean readable = true;
	
	public VHDR(String cestaSouboru, boolean viditelnost) {
		this(new File(cestaSouboru), viditelnost);
	}
	
	public VHDR(File soubor, boolean viditelnost) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		
		file = soubor;
		setName(file.getName());
		nazev = file.getName().replace(".hdr", "");
		if (viditelnost) {
			input = new JTextArea("");
			JScrollPane jspi = new JScrollPane(input);
			add(jspi);
			markerTable = new JTextArea("");
			JScrollPane jspm = new JScrollPane(markerTable);
			add(jspm);
			setDividerLocation(Aplikace.EDITOR.getSize().width * 2 / 3);
		}
		try {
			Scanner s = new Scanner(soubor);
			while(s.hasNext()) {
				String line = s.nextLine();
				String[] split = line.split("=");
				if (split.length == 2 && split[0].equals("DataFile")) {
					data = new File(
							soubor.getParentFile()
								.getAbsolutePath()
								.concat("\\" + split[1]));
				} else if (split.length == 2 && split[0].equals("MarkerFile")) {
					marker = new File(
							soubor.getParentFile()
								.getAbsolutePath()
								.concat("\\" + split[1]));
				}
				
				if (viditelnost) {
					input.append(line + '\n');
				}
			}
			s.close();
		} catch (Exception e) {
			readable = false;
		}
		
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

	public static boolean isReadable(File f) {
		if (new VHDR(f, false).isReadable())
			return true;
		return false;
	}

	public boolean isReadable() {
		return readable && data.exists() && marker.exists();
	}

	public Component read() {
		return this;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	
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

	private void write(String nazev) {
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
