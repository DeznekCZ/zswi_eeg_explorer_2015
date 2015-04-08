package cz.eeg.io;

import cz.eeg.data.Vhdr;

public class FilesIO {
	public boolean isReadable(File vhdrPath);
	public Vhdr read(File vhdrPath);
	public boolean write(Vhdr linkedVhdr);
}
