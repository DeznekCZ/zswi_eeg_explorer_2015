package cz.deznekcz.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Lanuguage configuration class<br><br>
 * 
 * An sigleton class that can use static import.
 * Class generate a custom files. Files is writen
 * in UTF-8 and can be rewrited. Used extension
 * is *.lng. Every value of {@link LangItem} can
 * use default formating symbols.
 * 
 * <br><br>Usage:<br>
 * <br>- import static cz.deznekcz.Lang.*;
 * <br>- main method: LANGload("language_fileName");
 * <br>- String s = LANG("cus-TOM_5ym bol");
 * <br>- String s = LANG("cus-TOM_5ym bol", var1, var2);
 * <br>- LANGgenerate("language_fileName"); //on close aplication
 * 
 * <br><br>GUI commands:<br>
 * <br>- LANGset("cus-TOM_5ym bol", "value %d");
 * 
 * @author Zdeněk Novotný (DeznekCZ)
 * @version 2.2.1
 */
public class Lang {
	
	/** Singleton instance */
	private static Lang instance = null;
	
	/** List of used symbols */
	private final static List<LangItem> SYMBOLS = new ArrayList<LangItem>();
	/** Load default language */
	static { LANGload("english"); }
	
	/** Current used language */
	private String langName;

	/** Singleton constructor of {@link Lang} */
	private Lang(String langName) {
		this.langName = langName;
	}

	/**
	 * Method loads language from a {@link Lang} file.
	 * by specific name. That method is used as factory
	 * method.
	 * @param langName {@link String} value
	 */
	public static void LANGload(String langName) {
		try {
			if (instance != null) {
				LANGgererate(langName);
				SYMBOLS.clear();
			}
			instance = new Lang(langName);
			Scanner scanner = new Scanner(new File("lang/"+langName+".lng"));
			
			String[] line;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine().split("=", 2);
				if (line.length == 2) {
					LANGset(line[0], line[1]);
				}
			}
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			LANGgererate(langName);
		}
	}

	/**
	 * Generates a {@link Lang} file with used symbols.
	 * <br><font color="red">WARNING!</font>
	 *  - method rewrite previous version of {@link Lang} file
	 * @param string {@link String} value
	 * @return true/false
	 */
	public static boolean LANGgererate(String string) {
		try {
			File f = new File("lang");
			if (!f.exists()) {
				f.mkdir();
			}
		
			PrintStream out = new PrintStream("lang/"+instance.langName+".lng");
			
			out.print(LANGlist());
			
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Method returns a {@link String} value of language symbol<br><br>
	 * <b>using:</b> symbol_name, out_string_param...
	 * @param symbol array of params
	 * @param args array of params
	 * @return a {@link String} value
	 * 
	 * @see String#format(String, Object...)
	 */
	public static String LANG(String symbol, Object... args) {
		LangItem langItem = LANGgetItem(symbol, args);
		
		if (args == null || args.length == 0) {
			return langItem.getValue();
		}
		
		try {
			return String.format(langItem.getValue(), args);
		} catch (FormatFlagsConversionMismatchException e) {
			return langItem.getValue();
		}
	}
	
	/**
	 * Method calls default value and replaces
	 * every "/n/" value to new line
	 * character '\n'.
	 * @param value default string
	 * @return replaced string
	 * @see #LANG(String, Object...)
	 */
	public static String LANGlined(String symbol, Object... args) {
		return LANG(symbol, args).replaceAll("/n/", ""+'\n');
	}

	/**
	 * Returns a list of language symbols. Every symbol
	 * is written in one line of {@link String}
	 * @return {@link String} value
	 */
	public static String LANGlist() {
		String out = "";
		
		Collections.sort(SYMBOLS);
		
		for (Iterator<LangItem> iterator = SYMBOLS.iterator(); iterator.hasNext();) {
			LangItem langItem = iterator.next();
			out += (langItem.getSymbol() + "=" + langItem.getValue() + "\n");
		}
		
		return out;
	}
	
	/**
	 * Method sets a value of {@link LangItem} specified by symbol.
	 * @param symbol {@link Scanner} value
	 * @param value {@link Scanner} value
	 * 
	 * @see #LANG(String, Object...)
	 */
	public static void LANGset(String symbol, String value) {
		LANGgetItem(symbol).setValue(value);
	}

	/**
	 * Method returns an instance {@link LangItem} by specific symbol.
	 * <br><font color="red">WARNING!</font>
	 *  - if NOT exists, create that symbol
	 * @param symbol {@link String} value
	 * @param args values to be formated
	 * @return instance of {@link LangItem} 
	 */
	private static LangItem LANGgetItem(String symbol, Object... args) {
		LangItem langItem = new LangItem(symbol, args);
		
		int index = SYMBOLS.indexOf(langItem);
		 
		if (index < 0) {
			SYMBOLS.add(langItem);
		} else {
			langItem = SYMBOLS.get(index);
		}
		return langItem;
	}
}

