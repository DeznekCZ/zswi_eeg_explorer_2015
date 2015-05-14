package cz.eeg.io;

import java.io.File;
import java.io.FileInputStream;
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

import cz.deznekcz.reflect.Out;
import cz.eeg.data.Channel;
import cz.eeg.data.EegFile;
import cz.eeg.data.Marker;
/**
 * Class working with files I/O operation
 * reading files saving file etc.*/
public class FilesIO {

	public static final File TEMP_DIRRECTORY = new File("temp");
	
	static {
		if (!TEMP_DIRRECTORY.exists()) {
			TEMP_DIRRECTORY.mkdir();
		}
	}
	
	private static boolean[] positionTmp={true,true,true,true,true,true,true,true,true,true};
	
	public static void setPositionTmp(boolean[] positionTmp) {
		FilesIO.positionTmp = positionTmp;
	}

	/**
	 * is readable - method that check if dataset is complete
	 * @param vhdrPath - path to vhdr file
	 * @return true or false if dataset is complete or not*/
	public static boolean isReadable(File vhdrPath) {
		try {
			Scanner s = new Scanner(vhdrPath, "utf-8");
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

	/**
	 * reading datasets and return instance of loading dataset
	 * @param vhdrPath path to vhdr file
	 * @return instance of eegfile*/
	public static EegFile read(File vhdrPath) throws FileNotFoundException, FileReadingException {
		EegFile vh = new EegFile();
		vh.setHeaderFile(vhdrPath);
		try {
			Scanner scanner = new Scanner(vhdrPath, "utf-8");
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
	/**
	 * saving all dataset with new name
	 * @param linkedVhdr instance of eegfile
	 * @param outPath output path 
	 * @param newName new name for dataset header,marker,binary file
	 * @param overwrite if the dataset is can be overwrite
	 * @return true or false if the saving is ok*/
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
				if(linkedVhdr.isTemporary()){
					linkedVhdr.getDataFile().renameTo(new File(pathD));
					linkedVhdr.setTemporary(false);
					freeTemporary(linkedVhdr);
				}else if(!saveDataFile(2,pathD,linkedVhdr)){return false;}
				pathH =outPath.getAbsolutePath()+"/"+newName+".vhdr"; // ale je to cesta do input protoze neni predan output
				pathM =outPath.getAbsolutePath()+"/"+newName+".vmrk"; 
			}if(linkedVhdr.getDataFile().getName().endsWith(".eeg")){
				pathD=outPath.getAbsolutePath()+"/"+newName+".eeg";
				if(linkedVhdr.isTemporary()){
					linkedVhdr.getDataFile().renameTo(new File(pathD));
					linkedVhdr.setTemporary(false);
					freeTemporary(linkedVhdr);
				}else if(!saveDataFile(1,pathD,linkedVhdr)){return false;}
				pathH =outPath.getAbsolutePath()+"/"+newName+".vhdr"; // ale je to cesta do input protoze neni predan output
				pathM =outPath.getAbsolutePath()+"/"+newName+".vmrk";
			}
			//EegFile newVhdr = linkedVhdr.clone();
			linkedVhdr.setHeaderFile(new File(pathH));
			linkedVhdr.setMarkerFile(new File(pathM));
			linkedVhdr.setDataFile(new File(pathD));
			pw = new PrintWriter(pathH); //zapisuje header
			pw.write(linkedVhdr.getVhdrData());
			pw.close();

			pw = new PrintWriter(pathM); // zapisuje marker
			pw.write(linkedVhdr.getVmrkData());
			pw.close();
			
			linkedVhdr.saved();
			return true;
		} catch (IOException e) {
			return false;
		}



	}
	public static File backupDataFile(File dataFile) {
		return dataFile;
	}
	
	/**
	 * saving only datafile with new name
	 * @param choose type of binary file avg/eeg
	 * @param newName new name binary file
	 * @param vhdr instance of eegfile*/
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
/**
 * merging datafiles
 * @param instance instance of eegfile
 * @param fos output stream*/
	public static void mergeData(EegFile instance,FileOutputStream fos)throws IOException{
		File input = instance.getDataFile();
	    
        FileInputStream fis = new FileInputStream(input);
        byte[] fileBytes = new byte[(int) input.length()];
        int bytesRead = fis.read(fileBytes, 0,(int)  input.length());
        assert(bytesRead == fileBytes.length);
        assert(bytesRead == (int) input.length());
        fos.write(fileBytes);
        fos.flush();
        fileBytes = null;
        fis.close();
 
	}
	
	
	/**
	 * merging binary files
	 * @param numberOfChannels number of chanels both datasets
	 * @param newName new name for binary file
	 * @param vhdrInstances field of two instances eegfile
	 * @return true or false if it saved clearly
	 * */
	public static boolean mergeDataFiles(int numberOfChannels,File newName,EegFile... vhdrInstances) throws IOException{
		FileOutputStream fos = new FileOutputStream(newName,true);
		mergeData(vhdrInstances[0],fos);
		mergeData(vhdrInstances[1], fos);
	     fos.close();
		  fos = null;
		return true;
	}
	/**
	 * short value to byte array
	 * @param value one value
	 * @return field of byte in little endian*/
	public static byte[] toByteArrayEeg(short value) {
		byte[] bytes = new byte[4];
		ByteBuffer bb= ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort(value);
		return bytes;
	}
	/**
	 * double value to byte array
	 * @param value one value
	 * @return field of byte in little endian*/
	public static byte[] toByteArrayAvg(double value) {
		byte[] bytes = new byte[8];
		ByteBuffer bb= ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putDouble(value);
		return bytes;
	}

	/**
	 * merging vhdr files
	 * @param outPath outputh path
	 * @param newName new name for merged vhdr
	 * @param vhdrInstances two instances of vhdr
	 * @return new instance of eegfile */
	public static EegFile mergeVhdrs(File outPath,String newName,EegFile... vhdrInstances) throws VhdrMergeException, IOException, FileReadingException {
		EegFile merged;
		if(vhdrInstances[0].getNumberOfChannels()!=vhdrInstances[1].getNumberOfChannels())
				{
			throw new VhdrMergeException("wrong_channels");
		}else{
			
			//merged = read(vhdrInstances[0].getHeaderFile());
			merged = vhdrInstances[0].clone();
			if(merged.getDataFileName().endsWith("eeg")){
				merged.setDataFile(new File(outPath.getAbsolutePath()+"/"+newName+".eeg"));
			} else if(merged.getDataFileName().endsWith("avg")){
				merged.setDataFile(new File(outPath.getAbsolutePath()+"/"+newName+".avg"));
			} else {
				throw new VhdrMergeException("wrong_file_type");
			}
			//merged.setMarkerFile(new File(outPath.getAbsolutePath()+"/"+newName+".vmrk"));
			//merged.setHeaderFile(new File(outPath.getAbsolutePath()+"/"+newName+".vhdr"));
			
			merged.setList(mergeVmrks(vhdrInstances));
			
			if (!mergeDataFiles(
					merged.getNumberOfChannels(),
					merged.getDataFile(),
					vhdrInstances))
				throw new VhdrMergeException("data_merge_failed");
		}
		merged.setTemporary(true);
		return merged;
		
	}
	/**
	 * Merge markers 
	 * @param vhdrInstances instances of source eegfiles
	 * @return new merged list of markers*/
	public static List<Marker> mergeVmrks(EegFile... vhdrInstances){
		int lastMarkerNumber=vhdrInstances[0].getMarkerList().get(vhdrInstances[0].getMarkerList().size()-1).getMarkerNumber();
		String lastPosition=vhdrInstances[0].getMarkerList().get(vhdrInstances[0].getMarkerList().size()-1).getPositionInDataPoints();
		List<Marker> mk1=vhdrInstances[0].getMarkerList();
		List<Marker> mk2=vhdrInstances[1].getMarkerList();
		List<Marker> mkn = new ArrayList<Marker>();
		for (Marker marker : mk1) {
			mkn.add(marker.copy(-1, 0L));
		}
		for (Marker marker : mk2) {
			if(marker.getMarkerNumber()>0){
				long position=
						Long.parseLong(marker.getPositionInDataPoints())
					+   Long.parseLong(lastPosition);
				mkn.add(marker.copy(lastMarkerNumber, position));
			}
			
		}
		
		return mkn;
	}
	
	/**
	 * merge datafiles to folder temp 
	 * @param target target eegfile
	 * @param source source eegfile
	 * @throws exceptions 
	 * @return new instance of eegfile
	 **/
	public static EegFile mergeTMP(EegFile target, EegFile source) throws IOException, VhdrMergeException, FileReadingException {
		File tmp=TEMP_DIRRECTORY;
		int j = isFreespace();
		if(j > -1){
			positionTmp[j]=false;
			EegFile merged = mergeVhdrs(tmp, "tmp"+j, target,source);
			merged.setSymbolicName("tmp"+j);
			return merged;
		} else {
			throw new VhdrMergeException("full_temp_space");
		}
	}
	
	//navic
	@Deprecated
	public static boolean saveMerged(File outPath,String newName,EegFile vhdr) throws FileNotFoundException{
		File f = vhdr.getDataFile();
		String suffix;
		if(f.getName().endsWith("eeg")){
			suffix="eeg";
			vhdr.setDataFile(new File(outPath.getAbsolutePath()+"/"+newName+".eeg"));
		}else{
			suffix="avg";
			vhdr.setDataFile(new File(outPath.getAbsolutePath()+"/"+newName+".avg"));
		}
		File pathH= new File(outPath.getAbsolutePath()+"/"+newName+".vhdr");
		vhdr.setMarkerFile(new File(outPath.getAbsolutePath()+"/"+newName+".avg"));
		save(pathH, vhdr.getVhdrData());
		File pathM= new File(outPath.getAbsolutePath()+"/"+newName+".vmrk");
		save(pathM, vhdr.getVmrkData());
		 
		
		
	      File f1 = new File(outPath.getAbsolutePath()+"/"+newName+"."+suffix);
	      boolean bool = false;
	     
	         bool = f.renameTo(f1);
	         
	     
		return bool;
	}
	
	@Deprecated
	private static void save(File name,String message) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(name);
		pw.write(message);
		pw.close();
	}
	//konec casti ktera je navic
	/**check if it's free space to create temporary file
	 * @return position which is free*/
	public static int isFreespace(){
		for(int i=0;i<positionTmp.length;i++){
			if(positionTmp[i]){
				return i;
			}
		}
		return -1;
	}
	/**free temporary folder and free space for temporary eegfile
	 * @param fileName instance of eegfile*/
	public static void freeTemporary(EegFile fileName) {
		int index = fileName.getDataFile().getName().charAt(3) - '0';
		positionTmp[index] = true;
		fileName.getDataFile().delete();
	}
	
	/**
	 * Check if the files is mergeable
	 * @param target target eegfile
	 * @param source source eegfile
	 * @param error error
	 * @return true/false if you can merge this two files*/
	public static boolean isMergeable(EegFile target, EegFile source, Out<String> error) {
		if (   !source.equals(target)
			&&  source.getNumberOfChannels()==target.getNumberOfChannels() 
			&&  source.getSamplingInterval()==target.getSamplingInterval())	
		{
			if (isFreespace() == -1) {
				error.lock("full_temp_space");
				return false;
			}
			return true;
		}
		return false; 
	}
}
