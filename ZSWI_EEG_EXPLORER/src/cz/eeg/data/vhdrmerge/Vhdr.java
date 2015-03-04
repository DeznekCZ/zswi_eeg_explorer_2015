package cz.eeg.data.vhdrmerge;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;


public class Vhdr extends JSplitPane {
	

	private String dataFormat;
	private String dataOrient;
	private int numberOfChannels;
	private int samplingInterval;
	private String binaryFormat;
	private String dataFile;
	private String markerFile;
	private String codePage;
	private String popis2=null;
	private String popis1=null;
	private String popis3=null;
	private Channel[] kanaly;


	private JTextArea input;
	private JTextArea markerTable;
	
	private String projdiKanaly(){
		String s="";
		for(int i=0;i<kanaly.length;i++){
			s+=kanaly[i].toString()+"\n";
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
					.append(popis1+"\n")
					.append("DataOrientation="+dataOrient+"\n")
					.append("NumberOfChannels="+numberOfChannels+"\n")
					.append(popis3+"\n")
					.append("SamplingInterval="+samplingInterval+"\n")
					.append("\n")
					.append("[Binary Infos]\n")
					.append("BinaryFormat="+binaryFormat+"\n")
					.append("\n")
					.append("[Channel Infos]\n")
					.append(popis2)
					.append(projdiKanaly())
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


	public String getPopis2() {
		return popis2;
	}


	public String getPopis1() {
		return popis1;
	}


	public String getPopis3() {
		return popis3;
	}


	public Channel[] getKanaly() {
		return kanaly;
	}


	public Vhdr(String vstup){
		cti(vstup);
	}
	

	public void cti(String file){
		try{
			InputStream ips=new FileInputStream(file); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			while ((line=br.readLine())!=null){
				if(line.equals("[Common Infos]")){
					codePage=br.readLine().split("=")[1];
					dataFile=br.readLine().split("=")[1];
					markerFile=br.readLine().split("=")[1];
					dataFormat=br.readLine().split("=")[1];
					popis1=br.readLine();
					dataOrient=br.readLine().split("=")[1];
					numberOfChannels=Integer.parseInt(br.readLine().split("=")[1]);
					popis3=br.readLine();
					samplingInterval=Integer.parseInt(br.readLine().split("=")[1]);
				}
				if(line.equals("[Binary Infos]")){
					binaryFormat=br.readLine().split("=")[1];
				}
				if(line.equals("[Channel Infos]")){
					line=br.readLine();
					while(line.startsWith(";")==true){
						popis2+=br.readLine()+"\n";
					}
					kanaly=new Channel[numberOfChannels];
					for(int j=0;j<numberOfChannels;j++){
						kanaly[j]=new Channel(br.readLine());
					}
				}

			}
			br.close();
	
		}       
		catch (Exception e){
			System.out.println(e.toString());
		}

	}
}
