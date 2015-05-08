package cz.eeg.ui.explorer;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import cz.eeg.Application;

public class DirectoryBrowserPanel extends JPanel implements ItemListener {

	/** */ private static final long serialVersionUID = 8141452098194695196L;

	public static final DirectoryBrowserPanel PANEL = new DirectoryBrowserPanel();
	
	private File currentDirectory = new File(Application.CONFIG.folder_output);

	private JComboBox<FileElement> combo;

	private JTextField input;
	
	public DirectoryBrowserPanel() {
		setBorder(BorderFactory.createTitledBorder(LANG("explorer_output_panel")));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		combo = new JComboBox<>();
		ListCellRenderer<FileElement> renderer = new ListCellRenderer<FileElement>() {
			protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
			@Override
			public Component getListCellRendererComponent(
					JList<? extends FileElement> list, FileElement value,
					int index, boolean isSelected, boolean cellHasFocus) {
				if (value.getLevel() == -1) {
					return new JSeparator();
				}
				
				JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
				        isSelected, cellHasFocus);
				renderer.setIcon(value.getIcon());
				return renderer;
			}
		};
		combo.setRenderer(renderer);
		combo.setPreferredSize(new Dimension(250, 25));
		
		reloadComboBox();
		combo.addItemListener(this);
		
		add(combo);
		
		input = new JTextField(currentDirectory.getAbsolutePath());
		input.setMinimumSize(new Dimension(300, 30));
		input.setEditable(false);
		add(new JScrollPane(input));
	}

	private void reloadComboBox() {
		combo.removeAllItems();
		DefaultComboBoxModel<FileElement> model = new DefaultComboBoxModel<>();
		
		addDiscs(model);
		
		FileElement currentFileElement;
		
		if (currentDirectory != null) {
			int parentCount = 0;
			File parent = currentDirectory.getParentFile();
			Stack<File> stack = new Stack<>();
			while (parent != null) {
				parentCount++;
				stack.add(parent);
				parent = parent.getParentFile();
			}
			
			currentFileElement = new FileElement(currentDirectory, parentCount);
			
			model.addElement(FileElement.separator());
			
			for (int i = parentCount; i > 0; i--) {
				model.addElement(new FileElement(stack.pop(), parentCount-i));
			}
			model.addElement(currentFileElement);
			
			File[] files = currentDirectory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return  pathname.isDirectory()
						&& !pathname.isHidden();
				}
			});
			
			if (files != null) {
				for (File file : files) {
					model.addElement(new FileElement(file, parentCount + 1));
				}
			}
			
		} else {
			currentFileElement = new FileElement(currentDirectory, 1);
			model.addElement(currentFileElement);
		}
		
		model.setSelectedItem(currentFileElement);
	    combo.setModel(model);
	}
	
	/**
	 * Adds to model all discs of Windows OS
	 * @param model list of files
	 */
	private void addDiscs(DefaultComboBoxModel<FileElement> model) {
		//TODO WINDOWS DISC NAME
		
		for (char i = 'A'; i <= 'Z'; i++) {
			File disc = new File(i+":\\");
			if (disc.exists()) {
				model.addElement(new FileElement(disc, 0));
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() != ItemEvent.SELECTED) return; // ANOTHER OPERATION
		FileElement item = (FileElement) e.getItem();
		if (item.getLevel() == -1) { // SEPARATOR
			reloadComboBox();
			return;
		}
		changeDirectory(item.getDirectory());
		reloadComboBox();
	}

	public File getCurrentDirectory() {
		return currentDirectory;
	}
	
	public void changeDirectory(File f) {
		currentDirectory = f;
		input.setText(currentDirectory.toString());
	}
}
