package cz.eeg.data;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import cz.eeg.Aplikace;

public class VHDR extends JSplitPane {

	public final File file;
	
	private String nazev;
	private File data;
	private File marker;

	private JTextArea input;
	private JTextArea markerTable;
	
	private boolean readable = true;
	
	public VHDR(String cestaSouboru, boolean viditelnost) {
		this(new File(cestaSouboru), viditelnost);
		setName(file.getName());
	}
	
	public VHDR(File soubor, boolean viditelnost) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		file = soubor;
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

	private boolean isReadable() {
		return readable && data.exists() && marker.exists();
	}

	public Component read() {
		return this;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
