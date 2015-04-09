package cz.eeg.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Instances of {@link Vhdr} represents *.vhdr files.
 * Class extends {@link JPanel} and can be display
 * in {@link JFrame}
 * 
 * TODO NextInfo
 *
 * @author IT Crowd
 */
public class Vhdr {
	

	private String dataFormat;
	private String dataOrient;
	private int numberOfChannels;
	private int samplingInterval;
	private String binaryFormat;
	private File headerFile;
	private File dataFile;
	private File markerFile;
	private String codePage;
	private String channelInfo=null;
	private String dator=null;
	private String sampling=null;
	private Channel[] channel;
	private Vmrk vm;
	

	private boolean readable = true;
	private boolean editing = false;

	private String name;
	
	/**
	 * Constructor reads a new *.vhdr file
	 * @param inputF pointer to {@link File}
	 * @param fullReading true - check only existency of marker and data file
	 */
	public Vhdr(File inputF, boolean fullReading) {
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
	}
	
	public Vhdr() {
		this.readable = false;
	}

	private void setName(String name) {
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
	
	public String getVhdrData(){
		return new StringBuilder()
					.append("[Common Infos]\n")
					.append("Codepage="+codePage+"\n")
					.append("DataFile="+dataFile.getName()+"\n")
					.append("MarkerFile="+markerFile.getName()+"\n")
					.append("DataFormat="+dataFormat+"\n")
					.append(dator+"\n")
					.append("DataOrientation="+dataOrient+"\n")
					.append("NumberOfChannels="+numberOfChannels+"\n")
					.append(sampling+"\n")
					.append("SamplingInterval="+samplingInterval+"\n")
					.append("\n")
					.append("[Binary Infos]\n")
					.append("BinaryFormat="+binaryFormat+"\n")
					.append("\n")
					.append("[Channel Infos]\n")
					.append(channelInfo)
					.append(channelsToString())
				.toString();
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


	public String getChannelInfo() {
		return channelInfo;
	}


	public String getDator() {
		return dator;
	}


	public String getSampling() {
		return sampling;
	}


	public Channel[] getChannel() {
		return channel;
	}

	public Vmrk getVm() {
		return vm;
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

	public String getVmrkData() {
		return vm.getLn();
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

	public static Vhdr voidFile() {
		return new Vhdr();
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

	public void setChannelInfo(String channelInfo) {
		this.channelInfo = channelInfo;
	}

	public void setDator(String dator) {
		this.dator = dator;
	}

	public void setSampling(String sampling) {
		this.sampling = sampling;
	}

	public void setChannel(Channel[] channel) {
		this.channel = channel;
	}

	public void setVm(Vmrk vm) {
		this.vm = vm;
	}

	public void setReadable(boolean readable) {
		this.readable = readable;
	}
	
	
}
