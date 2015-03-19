package cz.eeg.data.vhdrmerge;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cz.eeg.Application;
import cz.eeg.data.DATA;
import cz.eeg.data.Vmrk;
import cz.eeg.ui.editor.EditableField;


public class Vhdr extends JSplitPane {
	

	private EditableField dataFormat;
	private EditableField dataOrient;
	private int numberOfChannels;
	private int samplingInterval;
	private EditableField binaryFormat;
	private EditableField dataFile;
	private EditableField markerFile;
	private EditableField codePage;
	private EditableField channelInfo=null;
	private EditableField dator=null;
	private String sampling=null;
	private Channel[] channel;

	private boolean readable = true;
	//TODO editovany

	private JPanel input;
	private JTextArea markerTable;
	
	public Vhdr(File inputF, boolean viewable){
		super(JSplitPane.HORIZONTAL_SPLIT);
		setName(inputF.getName());
		
		if (viewable) {
			input = new JPanel();
			input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
			
			openFile(inputF);
			
			String s=inputF.getAbsolutePath().replaceAll(".vhdr", ".vmrk");
			Vmrk vm= new Vmrk(s);
			
			JTextArea p = new JTextArea();
			input.add(p);
			
			JScrollPane jspi = new JScrollPane(input);
			add(jspi);
			markerTable = new JTextArea(vm.getLn());
			JScrollPane jspm = new JScrollPane(markerTable);
			add(jspm);
			
			p.setText(vhdr());
			
			setDividerLocation(Application.EDITOR.getSize().width * 2 / 3);
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
					.append("Codepage="+codePage.getValue()+"\n")
					.append("DataFile="+dataFile.getValue()+"\n")
					.append("MarkerFile="+markerFile.getValue()+"\n")
					.append("DataFormat="+dataFormat.getValue()+"\n")
					.append(dator.getValue()+"\n")
					.append("DataOrientation="+dataOrient.getValue()+"\n")
					.append("NumberOfChannels="+numberOfChannels+"\n")
					.append(sampling+"\n")
					.append("SamplingInterval="+samplingInterval+"\n")
					.append("\n")
					.append("[Binary Infos]\n")
					.append("BinaryFormat="+binaryFormat.getValue()+"\n")
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
					codePage = new EditableField(s.nextLine(), 20).editable();
					dataFile = new EditableField(s.nextLine(), 20).editable();
					markerFile = new EditableField(s.nextLine(), 20).editable();
					dataFormat = new EditableField(s.nextLine(), 20).editable();
					dator = new EditableField(s.nextLine(), 20).plain();
					dataOrient = new EditableField(s.nextLine(), 20).editable();
					numberOfChannels=Integer.parseInt(s.nextLine().split("=")[1]);
					sampling=s.nextLine();
					samplingInterval=Integer.parseInt(s.nextLine().split("=")[1]);
				}
				if(line.equals("[Binary Infos]")){
					binaryFormat=new EditableField(s.nextLine(), 20).editable();
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
			//br.close();
			
			//SEGMENT adding to JPanel
			input.add(codePage);
			input.add(dataFile);
			input.add(markerFile);
			input.add(dataFormat);
			input.add(dator);
			input.add(dataOrient);
			input.add(binaryFormat);
			//input.add(channelInfo);
	
		}       
		catch (Exception e){
			readable=false;
		}

	}

	public boolean isReadable() {
		return readable;
	}

	public String getDataFormat() {
		return dataFormat.getValue();
	}


	public String getDataOrient() {
		return dataOrient.getValue();
	}


	public int getNumberOfChannels() {
		return numberOfChannels;
	}


	public int getSamplingInterval() {
		return samplingInterval;
	}


	public String getBinaryFormat() {
		return binaryFormat.getValue();
	}


	public String getDataFile() {
		return dataFile.getValue();
	}


	public String getMarkerFile() {
		return markerFile.getValue();
	}


	public String getCodePage() {
		return codePage.getValue();
	}


	public String getChannelInfo() {
		return channelInfo.getValue();
	}


	public String getDator() {
		return dator.getValue();
	}


	public String getSampling() {
		return sampling;
	}


	public Channel[] getChannel() {
		return channel;
	}

}
