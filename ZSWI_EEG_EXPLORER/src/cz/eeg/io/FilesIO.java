package cz.eeg.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cz.deznekcz.tool.Lang;
import cz.eeg.data.Channel;
import cz.eeg.data.DATA;
import cz.eeg.data.Marker;
import cz.eeg.data.EegFile;

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
	
	public static EegFile read(File vhdrPath) throws FileNotFoundException, FileReadingException {
		EegFile vh = new EegFile();
		try {
			vh.setName(vhdrPath.getName());
			Scanner scanner = new Scanner(vhdrPath);
			String line;
			while (scanner.hasNextLine()){
				line=scanner.nextLine();
				if(line.equals("[Common Infos]")){/* codePage=s.nextLine().split("=")[1]; */
					vh.setCodePage(scanner.nextLine().split("=")[1]);
					vh.setDataFile(new File(vhdrPath.getParentFile().getAbsolutePath() + "/" +scanner.nextLine().split("=")[1]));
					vh.setMarkerFile(new File(vhdrPath.getParentFile().getAbsolutePath() + "/" +scanner.nextLine().split("=")[1]));
					vh.setDataFormat(scanner.nextLine().split("=")[1]);
					scanner.nextLine();
					vh.setDataOrient(scanner.nextLine().split("=")[1]);
					vh.setNumberOfChannels(Integer.parseInt(scanner.nextLine().split("=")[1]));
					scanner.nextLine();
					vh.setSamplingInterval(Integer.parseInt(scanner.nextLine().split("=")[1]));
					
				}
				if(line.equals("[Binary Infos]")){
					vh.setBinaryFormat(scanner.nextLine().split("=")[1]);
				}
				if(line.equals("[Channel Infos]")){
					line=scanner.nextLine();
					
					String channelInfo ="";
					while(line.startsWith(";")){
						channelInfo+=line+"\n";
						line=scanner.nextLine();
						
					}
					
					Channel [] channel=new Channel[vh.getNumberOfChannels()];
					for(int j=0;j<vh.getNumberOfChannels();j++){
						channel[j]=new Channel(line);
						line = scanner.nextLine();
					}
					vh.setChannel(channel);
					break;
				}
	
			}
			scanner.close();
			
			line = null;
			List<Marker> list = new ArrayList<Marker>();
			scanner = new Scanner(vh.getMarkerFile());
			while (scanner.hasNext()) {
				line = scanner.nextLine();
				if (line.startsWith("Mk")) {
					list.add(new Marker(line));
				}
			}
			vh.setList(list);
			
		} catch (FileNotFoundException e) {
			throw e;
			/*else
				throw new FileReadingException(
						Lang.LANG("exception", e.getClass().getName(), e.getMessage()));*/
		}
		vh.setReadable(true);
		
		return vh;
	}

	public static boolean write(EegFile linkedVhdr,File outPath) {
		
		
		PrintWriter pw;
			try {
				String pathH= outPath.getParent().toString(); // zde se musi predat outpath tedy kam zapisuji
				String pathM=outPath.getParent().toString();
				if(linkedVhdr.getDataFile().getName().endsWith(".avg")){
					pathH+="\\"+linkedVhdr.getDataFileName().replace(".avg", ".vhdr"); // ale je to cesta do input protoze neni predan output
					pathM+="\\"+linkedVhdr.getDataFileName().replace(".avg", ".vmrk");
				}if(linkedVhdr.getDataFile().getName().endsWith(".eeg")){
					pathH+="\\"+linkedVhdr.getDataFileName().replace(".eeg", ".vhdr");// ale je to cesta do input protoze neni predan output
					pathM+="\\"+linkedVhdr.getDataFileName().replace(".eeg", ".vmrk");
				}
				pw = new PrintWriter(pathH); //zapisuje header
				pw.write(linkedVhdr.getVhdrData());
				pw.close();
				
				pw = new PrintWriter(pathM); // zapisuje marker
				pw.write(linkedVhdr.getVmrkData());
				pw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		return false;
		
	}
	public static File backupDataFile(File dataFile) {
		return dataFile;
	}
	public static EegFile mergeVhdrs(EegFile... vhdrInstances) throws VhdrMergeException {
		return new EegFile();
	}
}
