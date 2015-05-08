package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.LANG;
import static cz.deznekcz.tool.Lang.LANGgenerate;
import static cz.deznekcz.tool.Lang.LANGlined;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import cz.eeg.Application;
import cz.eeg.Config;
import cz.eeg.ui.dialog.DialogManagement;
import cz.eeg.ui.explorer.DirectoryBrowserPanel;
import cz.eeg.ui.explorer.FileBrowserPanel;
import cz.eeg.ui.explorer.Scenario;
import cz.eeg.ui.fileeditor.MenuPanel;

/**
 * An instance of an file explorer
 * 
 * @author IT Crowd
 */
public class Explorer extends JPanel {

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
	
	public Explorer() {
		
		setName(LANG("explorer_tab"));
		
		setLayout(new BorderLayout());
		add(MenuPanel.explorerMenuPanel(), BorderLayout.NORTH);
		
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new BorderLayout());
		subPanel.add(DirectoryBrowserPanel.PANEL, BorderLayout.NORTH);
		subPanel.add(FileBrowserPanel.PANEL, BorderLayout.CENTER);
		add(subPanel, BorderLayout.CENTER);
		
		setFocusable(true);
	}
	
	@Override
	public void paint(Graphics g) {
		SPLIT.setDividerLocation(getWidth()/2);
		super.paint(g);
	}
	
	/*
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
				
				//CONFIG.ex_fullscreen = getExtendedState();
				//CONFIG.ex_posx = getLocation().x;
				//CONFIG.ex_posy = getLocation().y;
				//CONFIG.ex_width = getWidth();
				//CONFIG.ex_height = getHeight();
				CONFIG.lang = LANG("lang_short");
				//CONFIG.folder_input = GuiManager.EXPLORER.getInputPath();
				//CONFIG.folder_output = GuiManager.EXPLORER.getOutputPath();
				CONFIG.save();
				
				LANGgererate();
				System.exit(0);
			}
		} else {
			super.setVisible(b);
		}
		
	}*/
}
