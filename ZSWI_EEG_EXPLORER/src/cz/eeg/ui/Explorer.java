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

import cz.eeg.Appliacion;
import cz.eeg.tool.Config;

public class Explorer extends JPanel {

	public final static Config CONFIG = Appliacion.CONFIG;
	//public final static Lang LANG = Aplikace.LANG;
	
	private final static JSplitPane SPLIT = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	public final Select VSTUPNI_VYBER = Select.INPUT_SELECT;
	public final Select VYSTUPNI_VYBER = Select.OUTPUT_SELECT;
	
	public Explorer() {
		Container contentPane = Appliacion.WINDOW.getContentPane();
		
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
						Appliacion.EDITOR.open(false);
					}
				});
				file.add(editor);
		        
		        file.addSeparator();
		        
				// exit explorer

				final JMenuItem exit = new JMenuItem(LANG("exit"));
				exit.repaint();

				exit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						Appliacion.WINDOW.setVisible(false);
					}
				});
				file.add(exit);
		
			}
	        
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
						area.setText(LANG("credits_info")+Appliacion.AUTHOR);
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
	}

	public String getInputPath() {
		return (VSTUPNI_VYBER == null ? "input" : VSTUPNI_VYBER.getCurrentDirectory().getAbsolutePath());
	}

	public String getOutputPath() {
		return (VYSTUPNI_VYBER == null ? "output" : VYSTUPNI_VYBER.getCurrentDirectory().getAbsolutePath());
	}
	
	@Override
	public void paint(Graphics g) {
		SPLIT.setDividerLocation(Appliacion.WINDOW.getWidth()/2);
		super.paint(g);
	}
	
//  TODO LATER USE	
//	/**
//	 * Metoda vytvářející odkaz menu pro jazyk nacházející se ve složce "lang",
//	 * pokud se jazyk ve složce nenachází načte angličtinu
//	 * (tato metoda je použita v místě, kde tato možnost nenastane, ale může nastat
//	 * chyba při parsování díky chybnému názvů parametru v souboru ".lng")
//	 * @param langName name of lang
//	 * @return instance of class {@link JMenuItem}
//	 */
//	private JMenuItem novyJazyk(final String langName) {
//		Lang newLang = new Lang(langName);
//		JMenuItem item = new JMenuItem(newLang.lang_name); // eMenuItem
//        item.addActionListener(new ActionListener() {
//        	private String language = langName;
//            public void actionPerformed(ActionEvent event) {
//                LANGload(language);
//               	Appliacion.WINDOW.pack();
//               	Appliacion.EDITOR.repaint();
//            }
//        });
//		return item;
//	}
//	
//	/**
//	 * Pomocná metoda pro získání názvu souboru a koncovky
//	 * @param fileData celý název soubru
//	 * @return pole [název, koncovka]
//	 */
//	private static String[] fileNameSplit(String fileData) {
//		String[] data = new String[]{new String(),new String()};
//		int extension = 0;
//		for (int i = 0; i < fileData.length(); i++) {
//			if (fileData.charAt(i) == '.')
//				extension = 1;
//			else
//				data[extension] = data[extension].concat(fileData.substring(i, i+1));
//		}
//		return data;
//	}
//	
//	private void nacistJazyky(JMenu lang_menu) {
//		final File lang_dir = new File("lang");
//        final File[] lang_files = lang_dir.listFiles();
//        for (int i = 0; i < lang_files.length; i++) {
//        	final String file_data = lang_files[i].getName();
//        	final String[] file_name = fileNameSplit(file_data);
//			if (lang_files[i].isFile()
//					&& file_name[1].compareTo("lng") == 0) {
//				lang_menu.add(novyJazyk(file_name[0]));
//			}
//		}
//	}
}
