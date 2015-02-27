package cz.eeg.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

/**
 * 
 * @author IT Crowds
 *
 */
public class Lang {

	/** Lang data */
	public String 
		lang = "Language",
		lang_name = "English",
		lang_loaded = "Language loaded",
		lang_not_loaded = "Language file has wrongs, default language selected",
		lang_not_defined = "Language is not defined, default language selected",
		lang_saved = "Language is not defined, default language selected",
		lang_nonexportable = "Language is not defined, default language selected",
		lang_short = "english",
		file = "File",
		file_open = "Open",
		file_save = "Save",
		file_save_as = "Save as ...",
		file_indir = "Input directory: ",
		file_outdir = "Output directory: ",
		file_mistakes = "File mistakes: ",
		file_loading = "File is loading, please wait!",
		file_type = "EEG \"*.vhdr\" file",
		file_editor = "Editor",
		file_lock = "Wrong selection",
		file_lock_info = "Locked directory, this directory can be selected as input directory",
		file_wrong = "Files is not readable: ",
		file_close = "Want to close the file ",
		format_year = "Year",
		format_day = "Day",
		format_month = "Month",
		format_gender = "Gender",
		format_age = "Age",
		format_wrong = "FIELD WRONG INPUT",
		editor_title = "EEG signal files editor",
		editor_save = "Select saving name: ",
		editor_overwrite = "Do you want overwrite a file?",
		editor_file_exists = "File exists",
		exit = "Exit",
		error = "Error",
		credits = "Help",
		credits_about = "About",
		credits_info = "About",
		window_name = "EEG file manager",
		editor_no_file = "No file opened"
	;
	
	/**
	 * Default language. <b>Warning</b> use once for one frame.
	 * Language is loaded by name. "en" does not need a file ".lng"
	 * @param lang instance of {@code String}
	 */
	public Lang(String lang) {
		File lang_dir = new File("lang");
		if (!lang_dir.exists()) {
			lang_dir.mkdir();
			generateFile("english");
		} else {
			if (!new File("lang/"+lang+".lng").exists())
				generateFile("english");
			load(lang);
		}
		
	}

	/**
	 * This method loads lang files in "lang" directory.
	 * A param accept any ".lng" files by name.
	 * @param lang instance of {@code String}
	 * @return instance of {@code String}
	 */
	public String load(String lang) {
		File lng = new File("lang/"+lang+".lng");
		Scanner s = null;
		try {
			s = new Scanner(lng);
		} catch (FileNotFoundException e) {
			return lang_not_defined;
		}
		
		Field[] defaultLang = getClass().getDeclaredFields().clone();
		
		while(s.hasNextLine()) {
			String[] data = s.nextLine().split("=");
			try {
				getClass().getDeclaredField(data[0]).set(this, unPack(data[1]));
			} catch (IllegalArgumentException | IllegalAccessException
					| NoSuchFieldException | SecurityException e) {
				
				Field[] fields = getClass().getDeclaredFields();
				for (int i = 0; i < data.length; i++) {
					try {
						fields[i].set(this,defaultLang[i].get(this));
					} catch (IllegalArgumentException | IllegalAccessException e1) {
					}
				}
				s.close();
				return lang_not_loaded;
			}
		}
		s.close();
		lang_short = lang;
		return lang_loaded;
	}
	
	/**
	 * Generates a file in "lang" directory with specified name.
	 * Method returns 
	 * @param name instance of {@code String}
	 * @return instance of {@code String}
	 */
	public String generateFile(String filename) {
		File lng = new File("lang/"+filename+".lng");
		try {
			PrintStream ps = new PrintStream(lng);
			
			Field[] fields = getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					String string = (String) fields[i].get(this);
					string = pack(string);
					ps.print(fields[i].getName() + "=" + string);
					if (i < (fields.length-1)) {
						ps.println();
					}
				}
				catch (IllegalArgumentException | IllegalAccessException e) {}
			}

			ps.close();
			return lang_saved;
		} catch (FileNotFoundException e) {
			return lang_nonexportable;
		}
	}
	
	private String pack(String string) {
		String newString = "";
		int j = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\n') {
				newString += string.substring(j, i) + "|n";
				j = i+1;
			}
		}
		if (newString.length() == 0)
			return string;
		else {
			newString += string.substring(j,string.length());
			return newString;
		}
	}
	
	private String unPack(String string) {
		String newString = "";
		boolean find = false;
		int j = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '|') {
				find = true;
			} else if (find && string.charAt(i) == 'n') {
				newString += string.substring(j, i-1) + '\n';
				j = i+1;
				find = false;
			} else {
				find = false;
			}
		}
		if (newString.length() == 0)
			return string;
		else {
			newString += string.substring(j,string.length());
			return newString;
		}
	}
}
