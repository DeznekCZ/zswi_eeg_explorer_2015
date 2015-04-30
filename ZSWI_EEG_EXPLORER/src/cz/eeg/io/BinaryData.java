package cz.eeg.io;

import java.io.File;
import java.io.IOException;

import cz.zcu.kiv.signal.EEGDataTransformer;

public class BinaryData{

	/**field of data from binary file*/
	private static double [] [] dat;
	
	/**reading binary file
	 * @param name  vhdr file
	 * @param numberChannels number of chanels that dataset
	 * */
	public static void read(File name,int numberChannels) {
		dat = new double [numberChannels] [];
		for(int i=1;i<numberChannels+1;i++){
			ctiByte(name, i);
		}
	}

	
	/**getter that matrix
	 * @return data matrix */
	public static double [] [] getDat() {
		return dat;
	}



	/**
	 * reading one channel
	 * @param name header file*/
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
