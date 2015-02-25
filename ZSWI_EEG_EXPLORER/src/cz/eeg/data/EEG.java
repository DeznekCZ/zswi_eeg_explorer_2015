package cz.eeg.data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.omg.CORBA.portable.ValueBase;

public class EEG extends File{

	/**pole reprezentujici data v danem souboru*/
	private static byte [] dat;
	
	public EEG(String name) {
		super(name);
		ctiByte(name);
	}

	
	/**Getter pro bytove pole dat*/
	public static byte[] getDat() {
		return dat;
	}



	/**
	 * Metoda naplneni pole byte daty 
	 * @param name Jmeno cteneho datoveho souboru*/
	public static void ctiByte(String name)
	{      
		try
		{
			RandomAccessFile data = new RandomAccessFile(name,"rw");
			long size = data.length();
			dat= new byte [(int)data.length()];
			for (int x=0; x < size; x++)
			{
				//data.seek(x);
				//byte b = data.readByte();
				dat[x]=data.readByte();
				//System.out.print(b+" ");
				   
			}
			//System.out.println();
			data.close();
		}
		catch (IOException ex)
		{ }
	}
	
	
}
