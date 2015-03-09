package cz.eeg.data;

import java.io.File;
import java.util.Scanner;

public class Vmrk extends File {
	
	
	
	public Vmrk(String name) {
		super(name);
		viewFile(name);
		
	}
	
public static String viewFile(String name){
		
	String line = null;
		try {
			Scanner s = new Scanner(new File(name));
			while(s.hasNext()) {
				 line+= s.nextLine()+"\n";
				//String[] split = line.split("=");
			}
			s.close();
		} catch (Exception e) {
		}
		return line;

	}
}
