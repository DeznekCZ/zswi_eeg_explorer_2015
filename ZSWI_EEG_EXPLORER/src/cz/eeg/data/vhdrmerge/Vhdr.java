package cz.eeg.data.vhdrmerge;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Vhdr {
	private static String dataFormat;
	private static String dataOrient;
	private static int numberOfChannels;
	private static int samplingInterval;
	private static String binaryFormat;
	private static String popis2=null;
	private static String popis1=null;
	private static String popis3=null;
	private static Channel[] kanaly;




	public static String getPopis3() {
		return popis3;
	}


	public static String getDataFormat() {
		return dataFormat;
	}


	public static String getDataOrient() {
		return dataOrient;
	}


	public static int getNumberOfChannels() {
		return numberOfChannels;
	}


	public static int getSamplingInterval() {
		return samplingInterval;
	}


	public static String getBinaryFormat() {
		return binaryFormat;
	}

	public static String getPopis2() {
		return popis2;
	}


	public static String getPopis1() {
		return popis1;
	}


	public static Channel[] getKanaly() {
		return kanaly;
	}

	
	public Vhdr(String vstup){
		cti(vstup);
	}
	

	public static void cti(String file){
		try{
			InputStream ips=new FileInputStream(file); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			while ((line=br.readLine())!=null){
				if(line.equals("[Common Infos]")){
					int pocet=0;
					while(pocet<3){br.readLine();pocet+=1;}

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
