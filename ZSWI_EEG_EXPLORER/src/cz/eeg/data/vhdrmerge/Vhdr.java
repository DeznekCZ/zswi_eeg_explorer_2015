package cz.eeg.data.vhdrmerge;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import cz.eeg.Application;
import cz.eeg.data.DATA;
import cz.eeg.data.Vmrk;
import cz.eeg.ui.editor.EditableField;
import cz.zcu.kiv.signal.*;


public class Vhdr extends JPanel {
	

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

	private JTextPane input;
	private JTextPane markerTable;
	
	public Vhdr(File inputF, boolean viewable, JPanel menuPanel) {
		
		setName(inputF.getName());
		
		if (viewable) {
			
			setLayout(new BorderLayout());
			add(menuPanel, BorderLayout.NORTH);
			
			JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			add(split, BorderLayout.CENTER);
			
			input = new JTextPane();
			input.setEditable(false);
			
			openFile(inputF);
			input.setText(vhdr());
			
			String s=inputF.getAbsolutePath().replaceAll(".vhdr", ".vmrk");
			Vmrk vm = null;
			try {
				vm = new Vmrk(s);
			} catch (Exception e) {
				readable = false;
			}
			if (readable) {
				JScrollPane jspi = new JScrollPane(input);
				split.add(jspi);
				markerTable = new JTextPane();
				markerTable.setText(vm.getLn());
				markerTable.setEditable(false);
				
				JScrollPane jspm = new JScrollPane(markerTable);
				split.add(jspm);
				
				split.setDividerLocation(Application.EDITOR.getSize().width / 2);
	
		/*		EEGDataTransformer dt = new EEGDataTransformer();
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
	*/		}
			
		}else {
			viewFile(inputF);
		}
		
		
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
	
	private String vhdr(){
		return new StringBuilder()
					.append("[Common Infos]\n")
					.append("Codepage="+codePage+"\n")
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
					.append(channelsToString())
				.toString();
	}
	
	private void viewFile(File iFile){
		
		
		try {
			Scanner s = new Scanner(iFile);
			while(s.hasNext()) {
				String line = s.nextLine();
				String[] split = line.split("=");
				
				//TODO Získání adresy datového souboru
				if (split.length == 2 && split[0].equals("DataFile")) {
					String newPath = iFile.getParentFile().getAbsolutePath()
							+ "/" + split[1];
					if (!new File(newPath).exists())
						throw new FileNotFoundException();
				} else
				//TODO Získání adresy markerového souboru
				if (split.length == 2 && split[0].equals("MarkerFile")) {
					String newPath = iFile.getParentFile().getAbsolutePath()
							+ "/" + split[1];
					if (!new File(newPath).exists())
						throw new FileNotFoundException();
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
				if(line.equals("[Common Infos]")){/* codePage=s.nextLine().split("=")[1]; */
					codePage = s.nextLine().split("=")[1];
					dataFile = s.nextLine().split("=")[1];
					markerFile = s.nextLine().split("=")[1];
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
					String channelInfoText = "";
					while(line.startsWith(";")){
						channelInfoText+=line+"\n";
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
		}       
		catch (Exception e){
			readable=false;
		}

	}

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

}
