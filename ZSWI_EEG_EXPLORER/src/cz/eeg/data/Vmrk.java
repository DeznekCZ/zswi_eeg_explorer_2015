package cz.eeg.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
	private File file;
	
	public String getLn() {
		return ln;
	}

	public Vmrk(File markerFile) throws Exception{
		ln=viewFile(markerFile);
		this.file = markerFile;
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
	
	public List<Marker> editFile() throws Exception{
		List<Marker> list = new ArrayList<Marker>();
		
		Scanner s = new Scanner(file);
		String line = null;
		
		while(s.hasNextLine()) {
			line = s.nextLine();
			if (line.equals("[Marker Infos]"))
				break;
		}
		
		while(s.hasNextLine()) {
			line = s.nextLine();
			if(line.startsWith(";"))
				continue;
			else
				list.add(new Marker(line));
			//String[] split = line.split("=");
		}
		s.close();
		return list;
	}
}
