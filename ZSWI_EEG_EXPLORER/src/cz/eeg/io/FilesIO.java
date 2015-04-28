package cz.eeg.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cz.deznekcz.tool.Lang;
import cz.eeg.data.Channel;
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
		vh.setHeaderFile(vhdrPath);
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

	public static boolean write(EegFile linkedVhdr,File outPath,String newName, boolean overwrite) throws FileAlreadyExistsException {

		if (new File(outPath.getAbsolutePath()+"/"+newName+".vhdr").exists() && !overwrite)
			throw new FileAlreadyExistsException(newName);

		PrintWriter pw;
		try {
			String pathH = null;
			String pathM = null;
			String pathD=null;
			if(linkedVhdr.getDataFile().getName().endsWith(".avg")){
				pathD=outPath.getAbsolutePath()+"/"+newName+".avg";
				if(!saveDataFile(2,pathD,linkedVhdr)){return false;}
				pathH =outPath.getAbsolutePath()+"/"+newName+".vhdr"; // ale je to cesta do input protoze neni predan output
				pathM =outPath.getAbsolutePath()+"/"+newName+".vmrk"; 
			}if(linkedVhdr.getDataFile().getName().endsWith(".eeg")){
				pathD=outPath.getAbsolutePath()+"/"+newName+".eeg";
				if(!saveDataFile(1,pathD,linkedVhdr)){return false;}
				pathH =outPath.getAbsolutePath()+"/"+newName+".vhdr"; // ale je to cesta do input protoze neni predan output
				pathM =outPath.getAbsolutePath()+"/"+newName+".vmrk";
			}
			EegFile newVhdr = read(linkedVhdr.getHeaderFile());
			newVhdr.setMarkerFile(new File(pathM));
			newVhdr.setDataFile(new File(pathD));
			pw = new PrintWriter(pathH); //zapisuje header
			pw.write(newVhdr.getVhdrData());
			pw.close();

			pw = new PrintWriter(pathM); // zapisuje marker
			pw.write(newVhdr.getVmrkData());
			pw.close();
			return true;
		} catch (IOException | FileReadingException e) {
			return false;
		}



	}
	public static File backupDataFile(File dataFile) {
		return dataFile;
	}
	public static boolean saveDataFile(int choose,String newName,EegFile vhdr) throws IOException{
		FileOutputStream fos= new FileOutputStream(newName);
		switch(choose){
		case 1 :
			BinaryData.read(vhdr.getHeaderFile(), vhdr.getNumberOfChannels());
			for(int i = 0;i<BinaryData.getDat()[0].length;i++){
				for (int k=0;k<vhdr.getNumberOfChannels();k++){
					short d1=(short)(BinaryData.getDat()[k][i]);
					byte [] zapis= toByteArrayEeg(d1);
					fos.write(zapis);
				}
			}
			fos.close();
			return true;
			
		case 2 :
			BinaryData.read(vhdr.getHeaderFile(),vhdr.getNumberOfChannels());
			for(int i = 0;i<BinaryData.getDat()[0].length;i++){
				for (int k=0;k<vhdr.getNumberOfChannels();k++){
					short d1=(short)(BinaryData.getDat()[k][i]);
					byte [] zapis= toByteArrayAvg(d1);
					fos.write(zapis);
				}
			}
			fos.close();
			return true; 
		}
		return false;
	}

	public boolean mergeDataFiles(int numberOfChannels,String newName,EegFile... vhdrInstances) throws IOException{

		try {
			FileOutputStream fos= new FileOutputStream(newName);
			if(vhdrInstances[0].getDataFileName().endsWith(".eeg")){
				BinaryData.read(vhdrInstances[0].getHeaderFile(), numberOfChannels);
				for(int i = 0;i<BinaryData.getDat()[0].length;i++){
					for (int k=0;k<vhdrInstances[0].getNumberOfChannels();k++){
						short d1=(short)(BinaryData.getDat()[k][i]);
						byte [] zapis= toByteArrayEeg(d1);
						fos.write(zapis);
					}
				}
				BinaryData.read(vhdrInstances[1].getHeaderFile(), numberOfChannels);
				for(int i = 0;i<BinaryData.getDat()[0].length;i++){
					for (int k=0;k<vhdrInstances[0].getNumberOfChannels();k++){
						short d2=(short)(BinaryData.getDat()[k][i]);
						byte [] zapis= toByteArrayEeg(d2);
						fos.write(zapis);
					}
				}
				fos.close();
				return true;
			}else{
				if(vhdrInstances[0].getDataFileName().endsWith(".avg")){
					BinaryData.read(vhdrInstances[0].getHeaderFile(), vhdrInstances[0].getNumberOfChannels());
					for(int i = 0;i<BinaryData.getDat()[0].length;i++){
						for (int k=0;k<vhdrInstances[0].getNumberOfChannels();k++){
							short d1=(short)(BinaryData.getDat()[k][i]);
							byte [] zapis= toByteArrayAvg(d1);
							fos.write(zapis);
						}
					}
					BinaryData.read(vhdrInstances[1].getHeaderFile(), vhdrInstances[1].getNumberOfChannels());
					for(int i = 0;i<BinaryData.getDat()[0].length;i++){
						for (int k=0;k<vhdrInstances[1].getNumberOfChannels();k++){
							short d2=(short)(BinaryData.getDat()[k][i]);
							byte [] zapis= toByteArrayAvg(d2);
							fos.write(zapis);
						}
					}
					fos.close();
					return true;
				}
			}

			fos.close();
		} catch (FileNotFoundException e) {
			return false;
		}

		return false;

	}

	public static byte[] toByteArrayEeg(short value) {
		byte[] bytes = new byte[4];
		ByteBuffer bb= ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort(value);
		return bytes;
	}
	public static byte[] toByteArrayAvg(double value) {
		byte[] bytes = new byte[8];
		ByteBuffer bb= ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putDouble(value);
		return bytes;
	}

	public static EegFile mergeVhdrs(String newName,EegFile... vhdrInstances) throws VhdrMergeException {

		if(vhdrInstances[0].getNumberOfChannels()!=vhdrInstances[1].getNumberOfChannels() 
				|| 	!vhdrInstances[0].getBinaryFormat().equals(vhdrInstances[1].getBinaryFormat())
				){
			throw new VhdrMergeException();
		}else{
			EegFile merged = new EegFile();
			merged=vhdrInstances[0];
			merged.setDataFile(new File(newName+".eeg"));
			merged.setMarkerFile(new File(newName+".vmrk"));

		}


		return new EegFile();
	}
}
