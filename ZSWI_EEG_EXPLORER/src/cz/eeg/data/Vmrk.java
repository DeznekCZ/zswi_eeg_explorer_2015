package cz.eeg.data;

import java.io.File;
import java.util.Scanner;

/**
 * Instance of {@link Vmrk} represents *.vmrk file
 *
 * @author IT Crowd
 */
public class Vmrk{
	
	/** */
	private static final long serialVersionUID = 1L;
	private String ln;
	
	public String getLn() {
		return ln;
	}

	public Vmrk(File markerFile) throws Exception{
		ln=viewFile(markerFile);
		
	}
	
	public static String viewFile(File file) throws Exception{
		
		String line = null;
		Scanner s = new Scanner(file);
		while(s.hasNext()) {
			if(line==null){line= s.nextLine()+"\n";} 
			else{line+= s.nextLine()+"\n";}
			//String[] split = line.split("=");
		}
		s.close();
		return line;

	}
}
