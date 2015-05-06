package cz.eeg.ui.explorer;

import static cz.deznekcz.tool.Lang.LANG;

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

import cz.eeg.Application;
import cz.eeg.Config;
import cz.eeg.io.FilesIO;
import cz.eeg.ui.Explorer;
import cz.eeg.ui.FileEditor;
import cz.eeg.ui.GuiManager;

/**
 * Instances represents extendex {@link JFileChooser}.
 * Files selected in instance of {@link FileBrowserPanel}
 * is opened in {@link FileEditor#WINDOW}
 *
 * @author IT Crowd
 */
public class FileBrowserPanel extends JFileChooser {
	
	/** */
	private static final long serialVersionUID = 843399005305757375L;
	
	public final static Config CONFIG = Application.CONFIG;
	public static final int INPUT = 1;
	public static final int OUTPUT = 2;
	public static final FileBrowserPanel INPUT_SELECT = new FileBrowserPanel(new File("input"), FileBrowserPanel.INPUT);
	public static final FileBrowserPanel OUTPUT_SELECT = new FileBrowserPanel(new File("output"), FileBrowserPanel.OUTPUT);

	//public static final File TEMT_DIRRECTORY = new File("temp");
	
	public FileBrowserPanel(File slozka, final int type) {
		super(slozka);
		
		setControlButtonsAreShown(false);
		
		setAcceptAllFileFilterUsed(false);
		setFileFilter(new FileNameExtensionFilter(LANG("file_type"), new String[] {"vhdr"}));
		setFileView(new FileView() {
			@Override
			public Icon getIcon(File f) {
				Icon ikona = FileSystemView.getFileSystemView().getSystemIcon(f);
				if (!f.isDirectory() && !FilesIO.isReadable(f)) {
					return new BlockedIcon(this, ikona);
				} else if (f.isDirectory() && type == OUTPUT
						&& f.equals(INPUT_SELECT.getCurrentDirectory())) {
					return new BlockedIcon(this, ikona);
				} else if (f.isDirectory()
						&& f.equals(Explorer.TEMT_DIRRECTORY)) {
					return new BlockedIcon(this, ikona);
				} else {
					return ikona;
				}
			}

			@Override
		    public Boolean isTraversable(File f) {
				if (type == OUTPUT)
					return !(f.isDirectory()
		        		&& (
		        			f.equals(INPUT_SELECT.getCurrentDirectory())
		        		 || f.equals(Explorer.TEMT_DIRRECTORY)
		        		)); 
				else return super.isTraversable(f);
		    }
		});
		setFileSelectionMode(FILES_ONLY);
		setMultiSelectionEnabled(true);
		
		final FileBrowserPanel vyber = this;
		
		final ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String command = actionEvent.getActionCommand();
				if (type == INPUT 
						&& command.equals(JFileChooser.APPROVE_SELECTION)) {
					GuiManager.EDITOR.open(getSelectedFiles());
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
		        	GuiManager.selectionFrame = vyber;
		        }

		 //Allow new events to be processed now
		     handlingEvent=false;
		    }
		  }); 
	}
	
	
}