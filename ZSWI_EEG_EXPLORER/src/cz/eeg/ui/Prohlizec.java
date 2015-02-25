package cz.eeg.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import cz.eeg.Aplikace;
import cz.eeg.tool.Config;
import cz.eeg.tool.Lang;

public class Prohlizec extends JPanel {

	public final static Config CONFIG = Aplikace.CONFIG;
	public final static Lang LANG = Aplikace.LANG;
	
	private final static JSplitPane SPLIT = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	public final Vyber VSTUPNI_VYBER = Vyber.VSTUPNI_VYBER;
	public final Vyber VYSTUPNI_VYBER = Vyber.VYSTUPNI_VYBER;
	
	public Prohlizec() {
		Container okno = Aplikace.OKNO.getContentPane();
		
		setLayout(new BorderLayout());
		JMenuBar menuLista = new JMenuBar();
		
		{
			
			//TODO Menu soubor
			final JMenu soubor = new JMenu() {
				public void repaint() {
					setText(LANG.file);
					super.repaint();
				};
			};
			soubor.repaint();
			
			menuLista.add(soubor);
			{
				// Spouštění editoru

				final JMenuItem editor = new JMenuItem() {
					public void repaint() {
						setText(LANG.file_editor);
						super.repaint();
					};
				};
				editor.repaint();

				editor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						Aplikace.EDITOR.otevrit(false);
					}
				});
				soubor.add(editor);
		        
		        soubor.addSeparator();
		        
				// Ukončení

				final JMenuItem exit = new JMenuItem() {
					public void repaint() {
						setText(LANG.exit);
						super.repaint();
					};
				};
				exit.repaint();

				exit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						Aplikace.OKNO.setVisible(false);
					}
				});
				soubor.add(exit);
		
			}
	        
			// Help menu

			final JMenu help = new JMenu() {
				public void repaint() {
					setText(LANG.credits);
					super.repaint();
				};
			};
			help.repaint();
			menuLista.add(help);
	        {
	        	// Jazyky

				final JMenu jazyk = new JMenu() {
					public void repaint() {
						setText(LANG.lang);
						super.repaint();
					};
				};
				jazyk.repaint();

				jazyk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						//TODO action
					}
				});
				help.add(jazyk);

				nacistJazyky(jazyk);
	        	
				help.addSeparator();
	        	// O programu

				final JMenuItem about = new JMenuItem() {
					public void repaint() {
						setText(LANG.credits_about);
						super.repaint();
					};
				};
				about.repaint();

				about.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						final JTextArea area = new JTextArea();
						area.setText(LANG.credits_info+Aplikace.AUTHOR);
						area.setEditable(false);
						area.setWrapStyleWord(true);
						area.setLineWrap(true);
						area.setMargin(new Insets(5, 5, 5, 5));
						final JDialog aboutFrame = new JDialog();
		                aboutFrame.setSize(new Dimension(300, 300));
		                aboutFrame.add(area);
		                aboutFrame.setTitle(LANG.credits_about);
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
		

		//TODO nastavení rozdělení
		SPLIT.setDividerLocation(CONFIG.ex_width / 2);
		SPLIT.setDividerSize(3);
		SPLIT.setEnabled(false);
	}

	public String getVstupniSoubor() {
		return (VSTUPNI_VYBER == null ? "." : VSTUPNI_VYBER.getCurrentDirectory().getAbsolutePath());
	}

	public String getVystupniSoubor() {
		return (VYSTUPNI_VYBER == null ? "." : VYSTUPNI_VYBER.getCurrentDirectory().getAbsolutePath());
	}
	
	/**
	 * Metoda vytvářející odkaz menu pro jazyk nacházející se ve složce "lang",
	 * pokud se jazyk ve složce nenachází načte angličtinu
	 * (tato metoda je použita v místě, kde tato možnost nenastane, ale může nastat
	 * chyba při parsování díky chybnému názvů parametru v souboru ".lng")
	 * @param nazevJazyka název jazyka
	 * @return instance třídy {@code JMenuItem}
	 */
	private JMenuItem novyJazyk(final String nazevJazyka) {
		Lang newLang = new Lang(nazevJazyka);
		JMenuItem item = new JMenuItem(newLang.lang_name); // eMenuItem
        item.addActionListener(new ActionListener() {
        	private String language = nazevJazyka;
            public void actionPerformed(ActionEvent event) {
                Aplikace.LANG.load(language);
               	Aplikace.OKNO.pack();
               	Aplikace.EDITOR.repaint();
            }
        });
		return item;
	}
	
	/**
	 * Pomocná metoda pro získání názvu souboru a koncovky
	 * @param fileData celý název soubru
	 * @return pole [název, koncovka]
	 */
	private static String[] fileNameSplit(String fileData) {
		String[] data = new String[]{new String(),new String()};
		int extension = 0;
		for (int i = 0; i < fileData.length(); i++) {
			if (fileData.charAt(i) == '.')
				extension = 1;
			else
				data[extension] = data[extension].concat(fileData.substring(i, i+1));
		}
		return data;
	}
	
	private void nacistJazyky(JMenu lang_menu) {
		final File lang_dir = new File("lang");
        final File[] lang_files = lang_dir.listFiles();
        for (int i = 0; i < lang_files.length; i++) {
        	final String file_data = lang_files[i].getName();
        	final String[] file_name = fileNameSplit(file_data);
			if (lang_files[i].isFile()
					&& file_name[1].compareTo("lng") == 0) {
				lang_menu.add(novyJazyk(file_name[0]));
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		SPLIT.setDividerLocation(Aplikace.OKNO.getWidth()/2);
		super.paint(g);
	}
}
