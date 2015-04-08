package cz.eeg.io;

import java.io.File;

import cz.eeg.data.DATA;
import cz.eeg.data.Vhdr;

public class FilesIO {
	public boolean isReadable(File vhdrPath) {
		return false;
	}
	public Vhdr read(File vhdrPath) {
		return new Vhdr();
	}
	public boolean write(Vhdr linkedVhdr) {
		return false;
	}
	public File backupDataFile(File dataFile) {
		return dataFile;
	}
	public Vhdr mergeVhdrs(Vhdr... vhdrInstances) throws IOArrayException {
		return new Vhdr();
	}
}
