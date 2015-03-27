package cz.eeg.data.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import cz.eeg.data.Vmrk;
import cz.eeg.data.vhdrmerge.Channel;
import cz.eeg.data.vhdrmerge.Vhdr;

public class SaveFiles {

	
	private File f;
	private Vhdr v;
	private String outFileName;
	
	public SaveFiles(String outFile,Vhdr vhdr) throws FileNotFoundException{
		outFileName=outFile;
		f=new File(outFile+".vhdr");
		v=vhdr;
		saveVhdr();
		saveVmrk();
	}
	
	private void saveVhdr() throws FileNotFoundException{
		PrintWriter pw=new PrintWriter(f);
		pw.write(newVhdr());
		pw.close();
	}
	
	private void saveVmrk() throws FileNotFoundException{
		String vmrk=v.getVm().getLn();
		String [] s=rozdel(v.getDataFile());
		vmrk.replace("DataFile="+v.getDataFile(), "DataFile="+outFileName+"."+s[1]);
		PrintWriter pw=new PrintWriter(new File (outFileName+".vmrk"));
		pw.write(vmrk);
		pw.close();
	}
	
	private String [] rozdel(String spl){
		String [] s = spl.split(".");
		return s;
		
	}
	
	private String newVhdr(){
		String [] s=rozdel(v.getDataFile());
		
		return new StringBuilder()
					.append("[Common Infos]\n")
					.append("Codepage="+v.getCodePage()+"\n")
					.append("DataFile="+outFileName+"."+s[1]+"\n")
					.append("MarkerFile="+outFileName+".vmrk"+"\n")
					.append("DataFormat="+v.getDataFormat()+"\n")
					.append(v.getDator()+"\n")
					.append("DataOrientation="+v.getDataOrient()+"\n")
					.append("NumberOfChannels="+v.getNumberOfChannels()+"\n")
					.append(v.getSampling()+"\n")
					.append("SamplingInterval="+v.getSamplingInterval()+"\n")
					.append("\n")
					.append("[Binary Infos]\n")
					.append("BinaryFormat="+v.getBinaryFormat()+"\n")
					.append("\n")
					.append("[Channel Infos]\n")
					.append(v.getChannelInfo())
					.append(channelToString(v.getChannel()))
				.toString();
	}
	
	private String channelToString(Channel[] channel){
		if (channel == null) return ";no channel\n";
		String s="";
		for(int i=0;i<v.getNumberOfChannels();i++){
			if (channel[i] != null)
				s+=channel[i].toString()+"\n";
		}
		return s;
	}
	
	
}
