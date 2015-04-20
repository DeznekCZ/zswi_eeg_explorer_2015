package cz.eeg.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import cz.zcu.kiv.signal.EEGDataTransformer;

public class BinaryData{

	/**pole reprezentujici data v danem souboru*/
	private static double [][] dat;
	
	public BinaryData(File name,int numberChannels) {
		dat = new double[numberChannels] [];
		for(int i=0;i<numberChannels;i++){
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
	public static void ctiByte(File datafile,int channel)
	{      
		EEGDataTransformer dt = new EEGDataTransformer();
			try {
				dat[channel-1]=dt.readBinaryData(datafile.getAbsolutePath(), channel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
	
}
