package cz.eeg.data.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;

import cz.eeg.data.Channel;
import cz.eeg.data.EegFile;

public class RenameAndSave {

	
	private File f;
	private EegFile v;
	private String outFileName;
	private File outPath;
	
	public RenameAndSave(File outDirectory, String newName, EegFile vhdr, boolean overwrite) throws FileNotFoundException, FileAlreadyExistsException{
		outFileName=newName;
		outPath=outDirectory;
		f=new File(outDirectory.getAbsolutePath()+"/"+newName+".vhdr");
		
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
		String vmrk=v.getVmrkData();
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
		
		v.setHeaderFile(f);
		v.setMarkerFile(new File(outPath.getAbsolutePath() + "/" + outFileName + "vmrk"));
		v.setDataFile(new File(outPath.getAbsolutePath() + "/" + outFileName + s[1]));
		
		return v.getVhdrData();
	}
	
	
}
