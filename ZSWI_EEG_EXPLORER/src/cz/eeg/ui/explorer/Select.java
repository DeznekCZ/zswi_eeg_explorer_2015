package cz.eeg.ui.explorer;

import static cz.deznekcz.tool.Lang.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import cz.eeg.data.Vhdr;
import cz.eeg.tool.Config;
import cz.eeg.ui.Application;

/**
 * Instances represents extendex {@link JFileChooser}.
 * Files selected in instance of {@link Select}
 * is opened in {@link FileEditor#WINDOW}
 *
 * @author IT Crowd
 */
public class Select extends JFileChooser {
	
	public final static Config CONFIG = Application.CONFIG;
	public static final int INPUT = 1;
	public static final int OUTPUT = 2;
	public static final Select INPUT_SELECT = new Select(new File("input"), Select.INPUT);
	public static final Select OUTPUT_SELECT = new Select(new File("output"), Select.OUTPUT);
	
	public Select(File slozka, final int type) {
		super(slozka);
		
		setControlButtonsAreShown(false);
		
		setAcceptAllFileFilterUsed(false);
		setFileFilter(new FileNameExtensionFilter(LANG("file_type"), new String[] {"vhdr"}));
		setFileView(new FileView() {
			@Override
			public Icon getIcon(File f) {
				Icon ikona = FileSystemView.getFileSystemView().getSystemIcon(f);
				if (!f.isDirectory() && !new Vhdr(f, false).isReadable()) {
					return new BlockedIcon(this, ikona);
				} else if (f.isDirectory() && type == OUTPUT
						&& f.equals(INPUT_SELECT.getCurrentDirectory())) {
					return new BlockedIcon(this, ikona);
				} else {
					return ikona;
				}
			}

			@Override
		    public Boolean isTraversable(File f) {
				if (type == OUTPUT)
					return (f.isDirectory()
		        		&& !f.equals(INPUT_SELECT.getCurrentDirectory())); 
				else return super.isTraversable(f);
		    }
		});
		setFileSelectionMode(FILES_ONLY);
		setMultiSelectionEnabled(true);
		
		final Select vyber = this;
		
		final ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String command = actionEvent.getActionCommand();
				if (command.equals(JFileChooser.APPROVE_SELECTION)) {
					Application.EDITOR.open(getSelectedFiles());
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
		        	Application.selectionFrame = vyber;
		        }

		 //Allow new events to be processed now
		     handlingEvent=false;
		    }
		  }); 
	}
	
	
}

/**
 * Instance of {@link BlockedIcon} draw red cross
 * to icon image from {@link Icon} of file system.
 *
 * @author IT Crowd
 */
class BlockedIcon implements Icon {
	
	private BufferedImage image;
	private int size;
	
	public BlockedIcon(FileView typIkon, Icon ikona) {
		if (ikona instanceof BlockedIcon) {
			image = ((BlockedIcon) ikona).image;
		} else {
			image = new BufferedImage(ikona.getIconWidth(), ikona.getIconHeight(), 
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) image.getGraphics();
			ikona.paintIcon(null, g2, 0, 0);
			
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.RED);
			
			size = ikona.getIconWidth();
			
			g2.drawLine(size - 10, size - 10, size - 2, size - 2);
			g2.drawLine(size - 10, size - 2, size - 2, size - 10);
			
		}
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		final Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(image, x, y, null);
	}
	
	@Override
	public int getIconWidth() {
		return size;
	}
	
	@Override
	public int getIconHeight() {
		return size;
	}
}