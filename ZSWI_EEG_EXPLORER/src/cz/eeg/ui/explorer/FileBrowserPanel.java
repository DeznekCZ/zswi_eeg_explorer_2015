package cz.eeg.ui.explorer;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import cz.deznekcz.tool.Lang;
import cz.eeg.Application;
import cz.eeg.Config;
import cz.eeg.io.FilesIO;
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
	public static final FileBrowserPanel PANEL = new FileBrowserPanel(new File(CONFIG.folder_input), FileBrowserPanel.INPUT);

	public FileBrowserPanel(File slozka, final int type) {
		super(slozka);
		
		setBorder(BorderFactory.createTitledBorder(LANG("explorer_input_panel")));
		setControlButtonsAreShown(false);
		
		setAcceptAllFileFilterUsed(false);
		setFileFilter(buildFilter());
		setFileView(new FileView() {
			@Override
			public Icon getIcon(File f) {
				Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);
				if (!f.isDirectory() && !FilesIO.isReadable(f)) {
					return new BlockedIcon(this, icon);
				} else {
					return icon;
				}
			}

			@Override
		    public Boolean isTraversable(File f) {
				return super.isTraversable(f);// || !f.equals(FilesIO.TEMP_DIRRECTORY);
		    }
		});
		setFileSelectionMode(FILES_AND_DIRECTORIES);
		setMultiSelectionEnabled(true);
		
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String command = actionEvent.getActionCommand();
				if (command.equals(JFileChooser.APPROVE_SELECTION)) {
					GuiManager.EDITOR.open(getSelectedFiles());
				}
			}
		});
		
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
		        	boolean file = FileBrowserPanel.PANEL.getSelectedFile() != null
		        			&& FileBrowserPanel.PANEL.getSelectedFile().isFile();
		        	boolean folder = FileBrowserPanel.PANEL.getSelectedFile() != null
		        			&& FileBrowserPanel.PANEL.getSelectedFile().isDirectory();
		        	
		        	ExplorerButton.OPEN_SELECTED.setEnabled(file);
		        	ExplorerButton.COPY_SELECTED.setEnabled(file);
		        	ExplorerButton.DELETE_SELECTED.setEnabled(folder || file);
		        }

		 //Allow new events to be processed now
		     handlingEvent=false;
		    }
		  }); 
	}
	
	private FileFilter buildFilter() {
		return new FileFilter() {
			
			
			private final FileNameExtensionFilter EXTENSION =
					new FileNameExtensionFilter("", new String[] {"vhdr"});

			@Override
			public String getDescription() {
				return LANG("file_type");
			}
			
			@Override
			public boolean accept(File f) {
				return 	(	!f.isDirectory()
						&&	EXTENSION.accept(f)
						)
					||  (   f.isDirectory() && !f.isHidden()
						&& !f.getAbsolutePath().equals(
								FilesIO.TEMP_DIRRECTORY.getAbsolutePath())
						&& !f.getAbsolutePath().equals(
								GuiManager.RESOURCE_DIRRECTORY.getAbsolutePath())
						&& !f.getAbsolutePath().equals(
								Lang.FOLDER.getAbsolutePath())
						);
			}
		};
	}
	
	
}