package cz.eeg.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

public class Config {
	/** Check existing of config file */
	private boolean loaded = false;
	/** Frame data */
	public String lang = "cestina", folder_input = "input", folder_output = "output";
	/** Editor data */
	public int ed_width=600, ed_height=600, ed_posx, ed_posy, ed_fullscreen;
	/** Eplorer data */
	public int ex_width=600, ex_height=600, ex_posx, ex_posy, ex_fullscreen;
	/** Automaton settings filename */
	private final String filename = "config.cfg";
	
	/**
	 * Load settings for automaton simulator. <b>Warning</b> use once for one frame.
	 * @param lang instance of {@code String}
	 */
	public Config() {
		while (true) {
			File coonfig = new File(filename);
			Scanner s = null;
			try {
				s = new Scanner(coonfig);
			} catch (FileNotFoundException e) {
				break;
			}
			
			Field[] defaultLang = getClass().getDeclaredFields().clone();
			
			while(s.hasNextLine()) {
				String[] data = s.nextLine().split("=");
				try {
					Field f = getClass().getDeclaredField(data[0]);
					if (f.getType() == int.class) {
						f.set(this, Integer.parseInt(data[1]));
					} else {
						f.set(this, data[1]);
					}
				} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
					
				} catch (IllegalAccessException
						| NoSuchFieldException | SecurityException e) {
					Field[] fields = getClass().getDeclaredFields();
					for (int i = 1; i < data.length; i++) {
						try {
							fields[i].set(this,defaultLang[i].get(this));
						} catch (IllegalArgumentException | IllegalAccessException e1) {
						}
					}
					s.close();
					break;
				}
			}
			s.close();
			loaded = true;
			break;
		}
	}
	
	/**
	 * Generates a configuration file
	 * Method returns 
	 * @param name instance of {@code String}
	 */
	public void save() {
		while(true) {
			File config = new File(filename);
			try {
				PrintStream ps = new PrintStream(config);
				
				Field[] fields = getClass().getDeclaredFields();
				for (int i = 1; i < fields.length; i++) {
					try { ps.print(fields[i].getName() + "=" + fields[i].get(this) + "\n"); }
					catch (IllegalArgumentException | IllegalAccessException e) {}
				}
	
				ps.close();
				break;
			} catch (FileNotFoundException e) {
				break;
			}
		}
	}

	public boolean isSet() {
		return loaded;
	}
}
