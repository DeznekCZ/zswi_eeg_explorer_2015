package cz.eeg.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

import cz.zcu.kiv.signal.EEGDataTransformer;

public class BinaryData{

	/**pole reprezentujici data v danem souboru*/
	private static double [] [] dat;
	
	public static void read(File name,int numberChannels) {
		dat = new double [numberChannels] [];
		for(int i=1;i<numberChannels+1;i++){
			ctiByte(name, i);
		}
	}

	
	/**Getter pro bytove pole dat*/
	public static double [] [] getDat() {
		return dat;
	}



	/**
	 * Metoda naplneni pole byte daty 
	 * @param name Jmeno cteneho datoveho souboru*/
	public static void ctiByte(File headerFile,int channel)
	{      
		EEGDataTransformer dt = new EEGDataTransformer();
			try {
				dat[channel-1]=dt.readBinaryData(headerFile.getAbsolutePath(), channel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
	
}
