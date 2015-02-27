package cz.eeg.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.swing.DefaultListCellRenderer.UIResource;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIDefaults;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.IconUIResource;
import javax.swing.text.IconView;

import cz.eeg.Aplikace;
import cz.eeg.data.VHDR;
import cz.eeg.tool.Config;
import cz.eeg.tool.Lang;

public class Vyber extends JFileChooser {
	
	public final static Lang LANG = Aplikace.LANG;
	public final static Config CONFIG = Aplikace.CONFIG;
	public static final int INPUT = 1;
	public static final int OUTPUT = 2;
	public static Vyber VSTUPNI_VYBER = new Vyber(new File("input"), Vyber.INPUT);
	public static Vyber VYSTUPNI_VYBER = new Vyber(new File("output"), Vyber.OUTPUT);
	
	public Vyber(File slozka, final int type) {
		super(slozka);
		
		setControlButtonsAreShown(false);
		
		setAcceptAllFileFilterUsed(false);
		setFileFilter(new FileNameExtensionFilter(LANG.file_type, new String[] {"vhdr"}));
		setFileView(new FileView() {
			@Override
			public Icon getIcon(File f) {
				Icon ikona = FileSystemView.getFileSystemView().getSystemIcon(f);
				if (!f.isDirectory() && !VHDR.isReadable(f)) {
					return new BlockedIcon(this, ikona);
				} else if (f.isDirectory() && type == OUTPUT
						&& f.equals(VSTUPNI_VYBER.getCurrentDirectory())) {
					return new BlockedIcon(this, ikona);
				} else {
					return ikona;
				}
			}

			@Override
		    public Boolean isTraversable(File f) {
				if (type == OUTPUT)
					return (f.isDirectory()
		        		&& !f.equals(VSTUPNI_VYBER.getCurrentDirectory())); 
				else return super.isTraversable(f);
		    }
		});
		setFileSelectionMode(FILES_ONLY);
		setMultiSelectionEnabled(true);
		
		final Vyber vyber = this;
		
		final ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String command = actionEvent.getActionCommand();
				if (command.equals(JFileChooser.APPROVE_SELECTION)) {
					if (getSelectedFiles().length != 0) {
						Aplikace.EDITOR.otevrit(true);
					}
				}
			}
		};
		this.addActionListener(actionListener);
		
		addPropertyChangeListener(new PropertyChangeListener() {

		      //To prevent reentry
		  private boolean handlingEvent=false;

		      public void propertyChange(PropertyChangeEvent e) {

		        //Prevent reentry
		        if (handlingEvent)
		          return;
		        else
		          //Mark it as handling the event
		          handlingEvent=true;

		        String propertyName = e.getPropertyName();

		        //We are interested in both event types
		        if(propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) ||
		           propertyName.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)){
		        	Aplikace.oknoVyberu = vyber;
		        }

		 //Allow new events to be processed now
		     handlingEvent=false;
		    }
		  }); 
	}
	
	
}

class BlockedIcon implements Icon {
	
	private BufferedImage image;
	
	public BlockedIcon(FileView typIkon, Icon ikona) {
		if (ikona instanceof BlockedIcon) {
			image = ((BlockedIcon) ikona).image;
		} else {
			image = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) image.getGraphics();
			ikona.paintIcon(null, g2, 0, 0);
			
			g2.setStroke(new BasicStroke(4));
			g2.setColor(Color.RED);
			g2.drawLine(4, 4, 12, 12);
			g2.drawLine(4, 12, 12, 4);
		}
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		final Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(image, x, y, null);
	}
	
	@Override
	public int getIconWidth() {
		return 16;
	}
	
	@Override
	public int getIconHeight() {
		return 16;
	}
}