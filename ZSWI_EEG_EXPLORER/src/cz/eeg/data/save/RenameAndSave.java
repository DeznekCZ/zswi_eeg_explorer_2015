package cz.eeg.data.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;

import cz.eeg.data.Channel;
import cz.eeg.data.Vhdr;
import cz.eeg.data.Vmrk;
import cz.eeg.ui.Application;

public class RenameAndSave {

	
	private File f;
	private Vhdr v;
	private String outFileName;
	private File outPath;
	
	public RenameAndSave(File outFile, String newName, Vhdr vhdr, boolean overwrite) throws FileNotFoundException, FileAlreadyExistsException{
		outFileName=newName;
		outPath=outFile;
		f=new File(outFile.getAbsolutePath()+"/"+newName+".vhdr");
		
		if (f.exists() && !overwrite)
			throw new FileAlreadyExistsException(outFileName);
		
		v=vhdr;
		saveVhdr();
		saveVmrk();
		saveDataFile();
	}
	
	
	private void saveDataFile(){
		File oldfile = v.getDataFile();
		String [] s=rozdel(v.getDataFileName());
		File newfile =new File(outPath.getAbsolutePath()+"/"+outFileName+"."+s[1]);
 
		if(oldfile.renameTo(newfile)){
			System.out.println("Rename complete");
		}else{
			System.out.println("Rename failed");
		}
	}
	
	private void saveVhdr() throws FileNotFoundException{
		PrintWriter pw=new PrintWriter(f);
		pw.write(newVhdr());
		pw.close();
	}
	
	private void saveVmrk() throws FileNotFoundException{
		String vmrk=v.getVm().getLn();
		String [] s=rozdel(v.getDataFileName());
		vmrk.replace("DataFile="+v.getDataFileName(), "DataFile="+outFileName+"."+s[1]);
		PrintWriter pw=new PrintWriter(new File (outPath.getAbsolutePath()+"/"+outFileName+".vmrk"));
		pw.write(vmrk);
		pw.close();
	}
	
	private String [] rozdel(String spl){
		String [] s = spl.split("\\.");
		return s;
		
	}
	
	private String newVhdr(){
		String [] s=rozdel(v.getDataFileName());
		
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
