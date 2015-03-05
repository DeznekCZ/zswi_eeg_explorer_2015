package cz.eeg.data.vhdrmerge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import cz.eeg.Aplikace;
import cz.eeg.data.DATA;
import cz.eeg.data.VMRK;


public class Vhdr extends JSplitPane {
	

	private String dataFormat;
	private String dataOrient;
	private int numberOfChannels;
	private int samplingInterval;
	private String binaryFormat;
	private String dataFile;
	private String markerFile;
	private String codePage;
	private String channelInfo=null;
	private String dator=null;
	private String sampling=null;
	private Channel[] channel;

	private boolean readable = true;
	//TODO editovany

	public boolean isReadAble() {
		return readable;
	}


	private JTextArea input;
	private JTextArea markerTable;
	
	private String allChannels(){
		String s="";
		for(int i=0;i<numberOfChannels;i++){
			s+=channel[i].toString()+"\n";
		}
		return s;
	}
	
	public String vhdr(){
		return new StringBuilder()
					.append("[Common Infos]\n")
					.append("Codepage="+getCodePage()+"\n")
					.append("DataFile="+dataFile+"\n")
					.append("MarkerFile="+markerFile+"\n")
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
					//.append(projdiKanaly())
				.toString();
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


	public String getDataFile() {
		return dataFile;
	}


	public String getMarkerFile() {
		return markerFile;
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


	public Vhdr(File inputF, boolean viewable){
		super(JSplitPane.HORIZONTAL_SPLIT);
		setName(inputF.getName());
		
		if (viewable) {
			openFile(inputF);
			input = new JTextArea(vhdr());
			JScrollPane jspi = new JScrollPane(input);
			add(jspi);
			markerTable = new JTextArea("");
			JScrollPane jspm = new JScrollPane(markerTable);
			add(jspm);
			setDividerLocation(Aplikace.EDITOR.getSize().width * 2 / 3);
		}else {
			viewFile(inputF);
		}
		
		
	}
	
	private void viewFile(File iFile){
		
		
		try {
			Scanner s = new Scanner(iFile);
			while(s.hasNext()) {
				String line = s.nextLine();
				String[] split = line.split("=");
				
				//TODO Získání adresy datového souboru
				if (split.length == 2 && split[0].equals("DataFile")) {
					dataFile=split[1];
				} else
				//TODO Získání adresy markerového souboru
					if (split.length == 2 && split[0].equals("MarkerFile")) {
					markerFile=split[1];
				}
				
				//TODO Přepsání řádku na obrazovku, pokud se bude kreslit
				
			}
			s.close();
		} catch (Exception e) {
			//TODO V případě nečitelnosti se nastaví jako nečitelný Header
			readable = false;
		}

	}
	

	private void openFile(File file){
		try{
			//InputStream ips=new FileInputStream(file); 
			//InputStreamReader ipsr=new InputStreamReader(ips);
			//BufferedReader br=new BufferedReader(ipsr);
	
			Scanner s = new Scanner(file);
			String line;
			while (s.hasNextLine()){
				line=s.nextLine();
				if(line.equals("[Common Infos]")){
					codePage=s.nextLine().split("=")[1];
					dataFile=s.nextLine().split("=")[1];
					markerFile=s.nextLine().split("=")[1];
					dataFormat=s.nextLine().split("=")[1];
					dator=s.nextLine();
					dataOrient=s.nextLine().split("=")[1];
					numberOfChannels=Integer.parseInt(s.nextLine().split("=")[1]);
					sampling=s.nextLine();
					samplingInterval=Integer.parseInt(s.nextLine().split("=")[1]);
				}
				if(line.equals("[Binary Infos]")){
					binaryFormat=s.nextLine().split("=")[1];
				}
				if(line.equals("[Channel Infos]")){
					line=s.nextLine();
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
			//br.close();
	
		}       
		catch (Exception e){
			readable=false;
		}

	}
}
