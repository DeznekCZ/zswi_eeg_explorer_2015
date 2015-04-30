package cz.eeg.ui.explorer;

import static cz.deznekcz.tool.Lang.LANG;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class Scenario { //TODO
	
	private static ArrayList<String> list;
	public static String DEFAULT = LANG("dialog_save_def_scenario");
	private static String last;
	
	static {
		list = new ArrayList<>();
		last = DEFAULT;
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream("scenarios.cfg"));
			list = (ArrayList<String>) is.readObject();
			is.close();
		} catch (IOException | ClassNotFoundException e) {
			list = new ArrayList<String>();
		}
	}
	
	public static ArrayList<String> getList() {
		return list;
	}

	public static void setLast(String last) {
		Scenario.last = last;
	}

	public static String getLast() {
		return last;
	}

	public static void addScenario(String scenario) {
		list.add(scenario);
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("scenarios.cfg"));
			os.writeObject(list);
			os.close();
		} catch (IOException e) {
			
		}
	}
}
