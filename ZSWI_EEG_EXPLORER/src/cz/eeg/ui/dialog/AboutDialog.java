package cz.eeg.ui.dialog;

import static cz.deznekcz.tool.Lang.LANG;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;

import org.w3c.dom.Document;

public class AboutDialog {

	public static void open()  {
		String path = "resources/about.html";
		try {
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		String text="";
		while ((strLine = br.readLine()) != null)   {
			text=text+strLine+"\n";
		}
		
		JEditorPane jep = new JEditorPane("text/html", text);
		jep.setEditable(false);
		
		JFrame aboutPanel = new JFrame(LANG("credits_about"));
		aboutPanel.add(new JScrollPane(jep));
		aboutPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutPanel.setPreferredSize(new Dimension(400, 400));
		
		aboutPanel.pack();
		aboutPanel.setLocationRelativeTo(null);
		
		aboutPanel.setVisible(true);
		
		br.close();
		} catch (IOException e) {
			DialogManagement.open(DialogManagement.ERROR, LANG("credits_about_html_not_exists", path));
		}
	}
	
}
