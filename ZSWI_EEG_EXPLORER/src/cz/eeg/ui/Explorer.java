package cz.eeg.ui;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cz.eeg.Application;
import cz.eeg.Config;
import cz.eeg.ui.explorer.DirectoryBrowserPanel;
import cz.eeg.ui.explorer.ExplorerButton;
import cz.eeg.ui.explorer.FileBrowserPanel;
import cz.eeg.ui.fileeditor.MenuPanel;

/**
 * An instance of an file explorer
 * 
 * @author IT Crowd
 */
public class Explorer extends JPanel {

	/** */
	private static final long serialVersionUID = -4683589648282776251L;
	
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
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add( ExplorerButton.OPEN_SELECTED ); 
		buttonPanel.add( ExplorerButton.COPY_SELECTED ); 
		buttonPanel.add( ExplorerButton.DELETE_SELECTED ); 
		buttonPanel.add( ExplorerButton.SET_OUTPUT ); 
		add(buttonPanel, BorderLayout.SOUTH);
		
		setFocusable(true);
	}
	
	@Override
	public void paint(Graphics g) {
		SPLIT.setDividerLocation(getWidth()/2);
		super.paint(g);
	}
}
