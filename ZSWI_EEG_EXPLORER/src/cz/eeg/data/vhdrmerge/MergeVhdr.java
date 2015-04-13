package cz.eeg.data.vhdrmerge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import cz.eeg.data.Channel;
import cz.eeg.data.EegFile;

/**
 * Třída pro spojení 2 VHDR souborů
 * vstupem vždy dva soubory + název nového vhdr + nový datafile(název) + nový marker file(název)
 * */
public class MergeVhdr {

	private EegFile v1;
	private EegFile v2;
	private String newFile,dataFile,markerFile;
	
	public MergeVhdr(String file1,String file2,String newFile,String dataFile,String markerFile){
		 //v1=new Vhdr(file1);
		 //v2=new Vhdr(file2);
		 this.newFile=newFile;
		 this.dataFile=dataFile;
		 this.markerFile=markerFile;
		 spoj();
		 
	}
	
	private void spoj(){
		try {
			if(v1.getNumberOfChannels()!=v2.getNumberOfChannels() || v1.getDataFormat().equals(v2.getDataFormat())==false){
				// akce nejde spojit vyhodí hlášku a konec spojení
			}
			
			PrintWriter pw=new PrintWriter(new File(newFile));
			
			// TODO už si opravdu dělej co chceš, když ti říkám, co máš používat tak na to kašleš
			v1.setDataFile(new File(dataFile));
			v1.setMarkerFile(new File(markerFile));
			v1.setHeaderFile(new File(newFile));
			
			pw.write(v1.getVhdrData());
			
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
