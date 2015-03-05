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
	private String popis2=null;
	private String popis1=null;
	private String popis3=null;
	private Channel[] kanaly;

	private boolean citelny = true;
	//TODO editovany

	public boolean isCitelny() {
		return citelny;
	}


	private JTextArea input;
	private JTextArea markerTable;
	
	private String projdiKanaly(){
		String s="";
		for(int i=0;i<numberOfChannels;i++){
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


	public Vhdr(File vstup, boolean viditelnost){
		super(JSplitPane.HORIZONTAL_SPLIT);
		setName(vstup.getName());
		
		if (viditelnost) {
			cti(vstup);
			input = new JTextArea(vhdr());
			JScrollPane jspi = new JScrollPane(input);
			add(jspi);
			markerTable = new JTextArea("");
			JScrollPane jspm = new JScrollPane(markerTable);
			add(jspm);
			setDividerLocation(Aplikace.EDITOR.getSize().width * 2 / 3);
		}else {
			ctiRychle(vstup);
		}
		
		
	}
	
	private void ctiRychle(File soubor){
		
		
		try {
			Scanner s = new Scanner(soubor);
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
			citelny = false;
		}

	}
	

	private void cti(File file){
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
					popis1=s.nextLine();
					dataOrient=s.nextLine().split("=")[1];
					numberOfChannels=Integer.parseInt(s.nextLine().split("=")[1]);
					popis3=s.nextLine();
					samplingInterval=Integer.parseInt(s.nextLine().split("=")[1]);
				}
				if(line.equals("[Binary Infos]")){
					binaryFormat=s.nextLine().split("=")[1];
				}
				if(line.equals("[Channel Infos]")){
					line=s.nextLine();
					while(line.startsWith(";")){
						popis2+=line+"\n";
						line=s.nextLine();
						
					}
					kanaly=new Channel[numberOfChannels];
					for(int j=0;j<numberOfChannels;j++){
						kanaly[j]=new Channel(line);
						line = s.nextLine();
					}
					break;
				}

			}
			s.close();
			//br.close();
	
		}       
		catch (Exception e){
			citelny=false;
		}

	}
}
