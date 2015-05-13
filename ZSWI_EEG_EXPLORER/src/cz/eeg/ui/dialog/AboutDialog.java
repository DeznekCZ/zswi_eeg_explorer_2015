package cz.eeg.ui.dialog;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import org.w3c.dom.Document;

import cz.deznekcz.tool.Lang;

public class AboutDialog {

	public static void open()  {
		String path = "resources/about_"+LANG("lang_short")+".html";
		try {
			Scanner scanner = new Scanner(new File(path), "utf-8");
			
			String text="";
			while (scanner.hasNext())   {
				text=text+scanner.nextLine()+"\n";
			}
			
			JEditorPane jep = new JEditorPane("text/html", text);
			jep.setEditable(false);
			
			jep.addHyperlinkListener(new HyperlinkListener() {
			    public void hyperlinkUpdate(HyperlinkEvent e) {
			        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			            if (Desktop.isDesktopSupported()) {
			                try {
			                    Desktop.getDesktop().browse(e.getURL().toURI());
			                } catch (IOException e1) {
			                    // TODO Auto-generated catch block
			                    e1.printStackTrace();
			                } catch (URISyntaxException e1) {
			                    // TODO Auto-generated catch block
			                    e1.printStackTrace();
			                }
			            }
			        }
			    }
			});
			
			JFrame aboutPanel = new JFrame(LANG("credits_about"));
			aboutPanel.add(new JScrollPane(jep));
			aboutPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			aboutPanel.setPreferredSize(new Dimension(400, 400));
			
			aboutPanel.pack();
			aboutPanel.setLocationRelativeTo(null);
			
			aboutPanel.setVisible(true);
			
			scanner.close();
		} catch (IOException e) {
			DialogManagement.open(DialogType.ERROR, LANG("credits_about_html_not_exists", path));
		}
	}
	
}
