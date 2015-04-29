package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import cz.eeg.Application;
import cz.eeg.Config;
import cz.eeg.ui.explorer.FileBrowserPanel;

/**
 * An instance of an file explorer
 * 
 * @author IT Crowd
 */
public class Explorer extends JFrame {

	/** */
	private static final long serialVersionUID = -4683589648282776251L;

	public static final File TEMT_DIRRECTORY = new File("./temp");
	static {
		if (!TEMT_DIRRECTORY.exists()) {
			TEMT_DIRRECTORY.mkdir();
		}
	}
	
	public final static Config CONFIG = Application.CONFIG;
	//public final static Lang LANG = Aplikace.LANG;
	
	private final static JSplitPane SPLIT = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	public final FileBrowserPanel VSTUPNI_VYBER = FileBrowserPanel.INPUT_SELECT;
	public final FileBrowserPanel VYSTUPNI_VYBER = FileBrowserPanel.OUTPUT_SELECT;
	
	public Explorer() {
		
		super(LANG("window_name"));
		
		setLayout(new BorderLayout());
		JMenuBar menuLista = new JMenuBar();
		
		{
			
			//TODO Menu soubor
			final JMenu file = new JMenu(LANG("file"));
			
			menuLista.add(file);
			{
				// Open editor

				final JMenuItem editor = new JMenuItem(LANG("file_editor"));

				editor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						GuiManager.EDITOR.open(null);
					}
				});
				file.add(editor);
		        
		        file.addSeparator();
		        
				// exit explorer

				final JMenuItem exit = new JMenuItem(LANG("exit"));
				exit.repaint();

				exit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						GuiManager.EXPLORER.setVisible(false);
					}
				});
				file.add(exit);
		
			}
	        
			// Edit
			
			final JMenu edit = new JMenu(LANG("file_edit"));
			menuLista.add(edit);
			{
				final JMenuItem move = new JMenuItem(LANG("file_move"));
				
				//Dialog.open(Dialog.COPY | Dialog.DELETE);
				
				move.setEnabled(false);
				edit.add(move);
				
				final JMenuItem copy = new JMenuItem(LANG("file_copy"));
				
				//Dialog.open(Dialog.COPY);
				
				copy.setEnabled(false);
				edit.add(copy);
			}
			
			// Separator
			
			menuLista.add(Box.createHorizontalGlue());
			
			// Help menu

			final JMenu help = new JMenu(LANG("credits"));
			menuLista.add(help);
	        {
	        	// Jazyky

				final JMenu languages = new JMenu(LANG("lang"));
				languages.setEnabled(false);
				help.add(languages);

				//TODO nacistJazyky(languages);
	        	
				help.addSeparator();
	        	// O programu

				final JMenuItem about = new JMenuItem(LANG("credits_about"));

				about.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						final JTextArea area = new JTextArea();
						area.setText(LANGlined("credits_info", Application.AUTHOR));
						area.setEditable(false);
						area.setWrapStyleWord(true);
						area.setLineWrap(true);
						area.setMargin(new Insets(5, 5, 5, 5));
						final JDialog aboutFrame = new JDialog();
		                aboutFrame.setSize(new Dimension(300, 300));
		                aboutFrame.add(area);
		                aboutFrame.setTitle(LANG("credits_about"));
		                aboutFrame.setVisible(true);
		                aboutFrame.setLocationRelativeTo(null);
					}
				});
				help.add(about);

			}
		}
		
		add(menuLista, BorderLayout.NORTH);
		add(SPLIT);
		
		SPLIT.add(VSTUPNI_VYBER);
		SPLIT.add(VYSTUPNI_VYBER);
		

		//TODO frame splitter settings
		SPLIT.setDividerLocation(CONFIG.ex_width / 2);
		SPLIT.setDividerSize(3);
		SPLIT.setEnabled(false);
		
		setSize(new Dimension(CONFIG.ex_width, CONFIG.ex_height));
		if (CONFIG.isSet()) {
			setLocation(CONFIG.ex_posx, CONFIG.ex_posy);
			if ((CONFIG.ex_fullscreen & JFrame.MAXIMIZED_BOTH) != JFrame.MAXIMIZED_BOTH) {
				setPreferredSize(new Dimension(CONFIG.ex_width, CONFIG.ex_height));
			}
			setExtendedState(CONFIG.ex_fullscreen);
		} else {
			
		}

		setFocusable(true);
		
		pack();
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}

	public String getInputPath() {
		return (VSTUPNI_VYBER == null ? "input" : VSTUPNI_VYBER.getCurrentDirectory().getAbsolutePath());
	}

	public String getOutputPath() {
		return (VYSTUPNI_VYBER == null ? "output" : VYSTUPNI_VYBER.getCurrentDirectory().getAbsolutePath());
	}
	
	@Override
	public void paint(Graphics g) {
		SPLIT.setDividerLocation(getWidth()/2);
		super.paint(g);
	}
	
	@Override
	public void setVisible(boolean b) {
		if (!b) {
			if (GuiManager.EDITOR.isOpenedFiles()) {
				FileEditor.WINDOW.setVisible(true);
				boolean closing = true;
				while (closing && GuiManager.EDITOR.isOpenedFiles()) {
					closing = GuiManager.EDITOR.close();
				}
			}
			if (!GuiManager.EDITOR.isOpenedFiles()) {
				
				FileEditor.WINDOW.setVisible(false);
				
				CONFIG.ex_fullscreen = getExtendedState();
				CONFIG.ex_posx = getLocation().x;
				CONFIG.ex_posy = getLocation().y;
				CONFIG.ex_width = getWidth();
				CONFIG.ex_height = getHeight();
				CONFIG.lang = LANG("lang_short");
				CONFIG.folder_input = GuiManager.EXPLORER.getInputPath();
				CONFIG.folder_output = GuiManager.EXPLORER.getOutputPath();
				CONFIG.save();
				
				LANGgererate(CONFIG.lang); // call deprecated
				System.exit(0);
			}
		} else {
			super.setVisible(b);
		}
	}
}
