package cz.eeg.data;

import java.io.File;
import java.util.Scanner;

public class Vmrk extends File {
	
	private String ln;
	
	public String getLn() {
		return ln;
	}

	public Vmrk(String name) throws Exception{
		super(name);
		ln=viewFile(name);
		
	}
	
public static String viewFile(String name) throws Exception{
		
		String line = null;
		Scanner s = new Scanner(new File(name));
		while(s.hasNext()) {
			if(line==null){line= s.nextLine()+"\n";} 
			else{line+= s.nextLine()+"\n";}
			//String[] split = line.split("=");
		}
		s.close();
		return line;

	}
}
