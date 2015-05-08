package cz.eeg.ui.explorer;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

public class FileElement extends JPanel {

	/** */ private static final long serialVersionUID = -6802440850394122178L;
	private String name;
	private File directory;
	private int level;
	
	public FileElement(File directory, int level) {
		this.level = level;
		if (directory == null) {
			return;
		} 
		this.name = directory.getName();
		if (this.name.length() == 0 || this.name.charAt(0) == ':') {
			this.name = directory.toString();
		}
		this.directory = directory;
		add(new JTextArea(this.name));
	}

	@Override
	public String toString() {
		return name;
	}

	public File getDirectory() {
		return directory;
	}

	public Icon getIcon() {
		final Icon icon = FileSystemView.getFileSystemView().getSystemIcon( directory );
		final int trans = icon.getIconWidth() * (level);
		return new Icon() {
			
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g;
				AffineTransform dt = g2.getTransform();
				g2.translate(trans/2, 0);
				icon.paintIcon(c, g2, x, y);
				g2.setTransform(dt);
			}
			
			@Override
			public int getIconWidth() {
				// TODO Auto-generated method stub
				return icon.getIconWidth() + trans/2;
			}
			
			@Override
			public int getIconHeight() {
				// TODO Auto-generated method stub
				return icon.getIconHeight();
			}
		};
	}

	public static FileElement separator() {
		return new FileElement(null, -1);
	}

	public int getLevel() {
		return level;
	}
}
