package cz.eeg.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import cz.eeg.io.BinaryData;

/**
 * Instances of {@link EegFile} represents *.vhdr files.
 * Class extends {@link JPanel} and can be display
 * in {@link JFrame}
 * 
 * TODO NextInfo
 *
 * @author IT Crowd
 */
public class EegFile {
	
	/** FORMATING codepage, datafile, markers */
	private static final String MARKER_FILE_FORMAT = 
		new StringBuilder()
			.append("Brain Vision Data Exchange Marker File, Version 1.0")
			.append("\n\n[Common Infos]")
			.append("\nCodepage=%s")
			.append("\nDataFile=%s")
			.append("\n\n[Marker Infos]")
			.append("\n; Each entry: Mk<Marker number>=<Type>,<Description>,<Position in data points>,")
			.append("\n; <Size in data points>, <Channel number (0 = marker is related to all channels)>")
			.append("\n; Fields are delimited by commas, some fields might be omitted (empty).")
			.append("\n; Commas in type or description text are coded as \"\\1\".")
			.append("\n%s").toString();
	/** FORMATING codepage, datafile, markerfile, dataformat, orientation, chnnels,
	 * sappling, binaryformat, channels */
	private static final String HEADER_FILE_FORMAT =
		new StringBuilder()
			.append("Brain Vision Data Exchange Header File Version 1.0")
			.append("\n; Data created by the Vision Recorder")
			.append("\n\n[Common Infos]")
			.append("\nCodepage=%s")
			.append("\nDataFile=%s")
			.append("\nMarkerFile=%s")
			.append("\nDataFormat=%s")
			.append("\n; Data orientation: MULTIPLEXED=ch1,pt1, ch2,pt1 ...")
			.append("\nDataOrientation=%s")
			.append("\nNumberOfChannels=%d")
			.append("\n; Sampling interval in microseconds")
			.append("\nSamplingInterval=%d")
			.append("\nSegmentationType=MARKERBASED")
			.append("\nSegmentDataPoints=1100")
			.append("\nAveraged=YES")
			.append("\nAveragedSegments=8")
			.append("\n\n[Binary Infos]")
			.append("\nBinaryFormat=%s")
			.append("\n\n[Channel Infos]")
			.append("\n; Each entry: Ch<Channel number>=<Name>,<Reference channel name>,")
			.append("\n; <Resolution in \"Unit\">,<Unit>, Future extensions..")
			.append("\n; Fields are delimited by commas, some fields might be omitted (empty).")
			.append("\n; Commas in channel names are coded as \"\\1\".")
			.append("\n%s") // CHANNELS
			.append("\n\n[Comment]")
			.append("\nEDITED by EEG EXPLORER from IT Crowd.").toString();
	
	private String dataFormat;
	private String dataOrient;
	private int numberOfChannels;
	private int samplingInterval;
	private String binaryFormat;
	private File headerFile;
	private File dataFile;
	private File markerFile;
	private String codePage;
	private Channel[] channel;
	private List<Marker> markerList;

	private boolean readable = true;
	private boolean editing = false;

	private String name;
	private String textData;
	
	/* /**
	 * Constructor reads a new *.vhdr file
	 * @param inputF pointer to {@link File}
	 * @param fullReading true - check only existency of marker and data file
	 */
/*	public Vhdr(File inputF, boolean fullReading) {
		headerFile = inputF;
		
		setName(inputF.getName());
		
		try {
			if (fullReading) {
				
				//openFile(inputF);
				
				//String s=inputF.getAbsolutePath().replaceAll(".vhdr", ".vmrk");
				//vm = new Vmrk(s);
				vm = new Vmrk(markerFile);
				
			} else {
				//viewFile(inputF); 
			}
		} catch (Exception e) {
			readable = false;
		}
		/*	EEGDataTransformer dt = new EEGDataTransformer();
		double[] d,j,k;
		
			
			try {
				d = dt.readBinaryData(inputF.getAbsolutePath(), 1);
				for(int i=0;i<d.length;i++){
					System.out.print(d[i]+" ");
				}
				System.out.println();
				System.out.println(d.length);
				j = dt.readBinaryData(inputF.getAbsolutePath(), 2);
				for(int i=0;i<j.length;i++){
					System.out.print(j[i]+" ");
				}
				System.out.println();
				System.out.println(j.length);
				k = dt.readBinaryData(inputF.getAbsolutePath(), 3);
				for(int i=0;i<k.length;i++){
					System.out.print(k[i]+" ");
				}
				System.out.println();
				System.out.println(k.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
/*	}*/
	
	public EegFile() {
		this.readable = false;
		this.textData = "";
	}

	public void setName(String name) {
		this.name = name;
	}

	private String channelsToString(){
		if (channel == null) return ";no channel\n";
		String s="";
		for(int i=0;i<numberOfChannels;i++){
			if (channel[i] != null)
				s+=channel[i].toString()+"\n";
		}
		return s;
	}
	
	/**
	 * String for "*.vhdr" file
	 * @return value
	 */
	public String getVhdrData(){
		return String.format(HEADER_FILE_FORMAT, 
				codePage, dataFile.getName(), markerFile.getName(),
				dataFormat, dataOrient, numberOfChannels, samplingInterval,
				binaryFormat, channelsToString());
	}
	
	/*private void viewFile(File iFile) throws FileNotFoundException{
		
		
	}*/ // metoda dodána do FILESIO
	

	/*private void openFile(File file) throws FileNotFoundException{
			//InputStream ips=new FileInputStream(file); 
			//InputStreamReader ipsr=new InputStreamReader(ips);
			//BufferedReader br=new BufferedReader(ipsr);
	
		Scanner s = new Scanner(file);
		String line;
		while (s.hasNextLine()){
			line=s.nextLine();
			if(line.equals("[Common Infos]")){/* codePage=s.nextLine().split("=")[1]; 
				codePage = s.nextLine().split("=")[1];
				dataFile = new File(file.getParentFile().getAbsolutePath() + "/" +
						s.nextLine().split("=")[1]);
				markerFile = new File(file.getParentFile().getAbsolutePath() + "/" +
						s.nextLine().split("=")[1]);
				dataFormat = s.nextLine().split("=")[1];
				dator = s.nextLine().split("=")[1];
				dataOrient = s.nextLine().split("=")[1];
				numberOfChannels=Integer.parseInt(s.nextLine().split("=")[1]);
				sampling=s.nextLine();
				samplingInterval=Integer.parseInt(s.nextLine().split("=")[1]);
			}
			if(line.equals("[Binary Infos]")){
				binaryFormat = s.nextLine().split("=")[1];
			}
			if(line.equals("[Channel Infos]")){
				line=s.nextLine();
				channelInfo ="";
				while(line.startsWith(";")){
					channelInfo+=line+"\n";
					line=s.nextLine();
					
				}
				channel=new Channel[numberOfChannels];
				for(int j=0;j<numberOfChannels;j++){
					channel[j]=new Channel(line);
					line = s.nextLine();
				}
				break;
			}

		}
		s.close();

	}*/

	public boolean isReadable() {
		return readable;
	}

	public String getDataFormat() {
		return dataFormat;
	}


	public String getDataOrient() {
		return dataOrient;
	}


	public int getNumberOfChannels() {
		return numberOfChannels;
	}


	public int getSamplingInterval() {
		return samplingInterval;
	}


	public String getBinaryFormat() {
		return binaryFormat;
	}


	public String getDataFileName() {
		return dataFile.getName();
	}


	public String getMarkerFileName() {
		return markerFile.getName();
	}


	public String getCodePage() {
		return codePage;
	}

	public Channel[] getChannel() {
		return channel;
	}

	public boolean isEditable() {
		return readable; //TODO need function
	}

	public boolean isSaveable() {
		return readable && true; //TODO need function
	}

	public boolean isCloseable() {
		return readable && true; //OK is able
	}


	/**
	 * String for "*.vmrk" file
	 * @return value
	 */
	public String getVmrkData() {
		return String.format(MARKER_FILE_FORMAT, codePage, dataFile.getName(), markersToString());
	}
	public double[][] getDataRead(){
		BinaryData.read(getHeaderFile(), getNumberOfChannels());
		return BinaryData.getDat();
	}

	public String getName() {
		return name;
	}

	public File getDataFile() {
		return dataFile;
	}

	public File getMarkerFile() {
		return markerFile;
	}

	public File getHeaderFile() {
		return headerFile;
	}

	public boolean isOpenForEditing() {
		return editing;
	}

	public static EegFile voidFile() {
		return new EegFile();
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setDataOrient(String dataOrient) {
		this.dataOrient = dataOrient;
	}

	public void setNumberOfChannels(int numberOfChannels) {
		this.numberOfChannels = numberOfChannels;
	}

	public void setSamplingInterval(int samplingInterval) {
		this.samplingInterval = samplingInterval;
	}

	public void setBinaryFormat(String binaryFormat) {
		this.binaryFormat = binaryFormat;
	}

	public void setHeaderFile(File headerFile) {
		this.headerFile = headerFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public void setMarkerFile(File markerFile) {
		this.markerFile = markerFile;
	}

	public void setCodePage(String codePage) {
		this.codePage = codePage;
	}

	public void setChannel(Channel[] channel) {
		this.channel = channel;
	}

	public void setReadable(boolean readable) {
		this.readable = readable;
	}
	
	public List<Marker> getMarkerList() {
		return markerList;
	}

	public void setList(List<Marker> markerList) {
		this.markerList = markerList;
	}
	
	public void setTextData(String textData) {
		this.textData = textData;
	}

	public String getTextData() {
		return textData;
	}

	private String markersToString() {
		String data = "";
		for (Marker marker : markerList) {
			data += String.format("\n%s", marker.toString());
		}
		return data;
	}

	public boolean isPlotAble() {
		return readable && channel != null && channel.length > 0;
	}
	
	public EegFile clone() {
		EegFile clone = new EegFile();
		clone.setBinaryFormat(binaryFormat);
		clone.setChannel(channel);
		clone.setCodePage(codePage);
		clone.setDataFile(dataFile);
		clone.setDataFormat(dataFormat);
		clone.setDataOrient(dataOrient);
		clone.setEditing(editing);
		clone.setHeaderFile(headerFile);
		clone.setList(markerList);
		clone.setMarkerFile(markerFile);
		clone.setName(name);
		clone.setNumberOfChannels(numberOfChannels);
		clone.setReadable(readable);
		clone.setSamplingInterval(samplingInterval);
		clone.setTextData(textData);
		return clone;
	}
}
