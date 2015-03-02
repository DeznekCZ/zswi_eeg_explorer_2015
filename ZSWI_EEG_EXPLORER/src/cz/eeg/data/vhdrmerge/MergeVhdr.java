package cz.eeg.data.vhdrmerge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Třída pro spojení 2 VHDR souborů
 * vstupem vždy dva soubory + název nového vhdr + nový datafile(název) + nový marker file(název)
 * */
public class MergeVhdr {

	private Vhdr v1;
	private Vhdr v2;
	private String newFile,dataFile,markerFile;
	
	public MergeVhdr(String file1,String file2,String newFile,String dataFile,String markerFile){
		 v1=new Vhdr(file1);
		 v2=new Vhdr(file2);
		 this.newFile=newFile;
		 this.dataFile=dataFile;
		 this.markerFile=markerFile;
		 spoj();
		 
	}
	
	public void spoj(){
		try {
			if(v1.getNumberOfChannels()!=v2.getNumberOfChannels() || v1.getDataFormat().equals(v2.getDataFormat())==false){
				// akce nejde spojit vyhodí hlášku a konec spojení
			}
			
			PrintWriter pw=new PrintWriter(new File(newFile));
			
			
			pw.write("[Common Infos]\n");
			pw.write("Codepage=UTF-8\n");
			pw.write("DataFile="+dataFile+"\n");
			pw.write("MarkerFile="+markerFile+"\n");
			pw.write("DataFormat="+v1.getDataFormat()+"\n");
			pw.write(v1.getPopis1()+"\n");
			pw.write("DataOrientation="+v1.getDataOrient()+"\n");
			pw.write("NumberOfChannels="+v1.getNumberOfChannels()+"\n");
			pw.write(v1.getPopis2()+"\n");
			pw.write("[Binary Infos]\n");
			pw.write("BinaryFormat="+v1.getBinaryFormat()+"\n");
			pw.write("[Channel Infos]\n");
			pw.write(v1.getPopis3()+"\n");
			Channel [] kanaly=v1.getKanaly();
			for(int i=0;i<v1.getNumberOfChannels();i++){
				pw.write(kanaly[i].toString());
			}
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
