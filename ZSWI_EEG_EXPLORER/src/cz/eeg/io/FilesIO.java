package cz.eeg.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import cz.deznekcz.tool.Lang;
import cz.eeg.data.Channel;
import cz.eeg.data.DATA;
import cz.eeg.data.Vhdr;

public class FilesIO {
	
	
	public static boolean isReadable(File vhdrPath) {
		try {
			Scanner s = new Scanner(vhdrPath);
			while(s.hasNext()) {
				String line = s.nextLine();
				String[] split = line.split("=");
				
				//TODO Získání adresy datového souboru
				if (split.length == 2 && split[0].equals("DataFile")) {
					String newPath = vhdrPath.getParentFile().getAbsolutePath()
							+ "/" + split[1];
					if (!new File(newPath).exists())
						throw new FileNotFoundException();
				} else
				//TODO Získání adresy markerového souboru
				if (split.length == 2 && split[0].equals("MarkerFile")) {
					String newPath = vhdrPath.getParentFile().getAbsolutePath()
							+ "/" + split[1];
					if (!new File(newPath).exists())
						throw new FileNotFoundException();
				}
			}
			s.close();
		} catch (Exception e) {
			return false;
		}
		return true;		
	}
	
	public static Vhdr read(File vhdrPath) throws FileNotFoundException, FileReadingException {
		Vhdr vh = new Vhdr();
		try {
			
			Scanner s = new Scanner(vhdrPath);
			String line;
			while (s.hasNextLine()){
				line=s.nextLine();
				if(line.equals("[Common Infos]")){/* codePage=s.nextLine().split("=")[1]; */
					vh.setCodePage(s.nextLine().split("=")[1]);
					vh.setDataFile(new File(vhdrPath.getParentFile().getAbsolutePath() + "/" +s.nextLine().split("=")[1]));
					vh.setMarkerFile(new File(vhdrPath.getParentFile().getAbsolutePath() + "/" +s.nextLine().split("=")[1]));
					vh.setDataFormat(s.nextLine().split("=")[1]);
					vh.setDator(s.nextLine().split("=")[1]);
					vh.setDataOrient(s.nextLine().split("=")[1]);
					vh.setNumberOfChannels(Integer.parseInt(s.nextLine().split("=")[1]));
					vh.setSampling(s.nextLine());
					vh.setSamplingInterval(Integer.parseInt(s.nextLine().split("=")[1]));
					
				}
				if(line.equals("[Binary Infos]")){
					vh.setBinaryFormat(s.nextLine().split("=")[1]);
				}
				if(line.equals("[Channel Infos]")){
					line=s.nextLine();
					
					String channelInfo ="";
					while(line.startsWith(";")){
						channelInfo+=line+"\n";
						line=s.nextLine();
						
					}
					vh.setChannelInfo(channelInfo);
					
					Channel [] channel=new Channel[vh.getNumberOfChannels()];
					for(int j=0;j<vh.getNumberOfChannels();j++){
						channel[j]=new Channel(line);
						line = s.nextLine();
					}
					vh.setChannel(channel);
					break;
				}
	
			}
			s.close();
		} catch (Exception e) {
			if (e instanceof FileNotFoundException)
				throw e;
			else
				throw new FileReadingException(
						Lang.LANG("exception", e.getClass().getName(), e.getMessage()));
		}
		return vh;
	}
	public static boolean write(Vhdr linkedVhdr) {
		String vhdr=linkedVhdr.getVhdrData();		
		//PrintWriter pw=new PrintWriter(""); ///Do jakého File se to má zapsat???
		//pw.write(vhdr);
		//pw.close();
		return false;
		
	}
	public static File backupDataFile(File dataFile) {
		return dataFile;
	}
	public static Vhdr mergeVhdrs(Vhdr... vhdrInstances) throws VhdrMergeException {
		return new Vhdr();
	}
}
