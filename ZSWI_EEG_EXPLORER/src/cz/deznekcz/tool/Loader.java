package cz.deznekcz.tool;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * A singleton class represents a loading frame with
 * two lines and two bars with percentage values.
 * 
 * @author Zdeněk Novotný (DeznekCZ)
 * @version 1.0
 */
public class Loader{
	
	static {
		running = false;
		listener = new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					System.exit(0);
			}
		};
	}
	
	private static final String LOADER = "Loader";
	private static final int LOADER_WIDTH = 300;
	private static boolean running;
	private static JFrame frame;
	private static JPanel panel;
	private static JProgressBar loadBar;
	private static JProgressBar loadSubBar;
	private static JTextField loadInfo;
	private static JTextArea loadSubInfo;
	private static KeyListener listener;

	/**
	 * Start loader as new window
	 * @param firstLabel
	 * @param firstSubLabel load sub information
	 * @param centering the component in relation to which the window's location is determined
	 * @see #start(String, String)
	 */
	public static void start(String firstLabel, String firstSubLabel, Component centering) {
		if (running) abort();

		if (frame == null) {
			frame = new JFrame(LOADER);
			frame.setUndecorated(true);
		
			init(frame.getRootPane(), firstLabel, firstSubLabel);
		
			frame.pack();
		
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		frame.setLocationRelativeTo(centering);
		frame.setVisible(true);
		
		running = true;
	}
	
	/**
	 * Start loader as panel
	 * <br><font color="red">WARNING!</font>
	 * <br> - panel must be added only once to anther component
	 * @param firstLabel
	 * @param firstSubLabel load sub information
	 * @return {@link JPanel} with loading bars
	 * @see #start(String, String, Component)
	 */
	public static JPanel start(String firstLabel, String firstSubLabel) {
		if (running) abort();

		if (panel == null) {
			panel = new JPanel();
			init(panel, firstLabel, firstSubLabel);
		} else {
			panel.setVisible(true);
			newLoading(firstLabel, firstSubLabel);
			update(0.0);
		}
		
		running = true;
		return panel;
	}

	private static void init(JComponent component, String firstLabel, String firstSubLabel) {
		component.setLayout(new BorderLayout());
		component.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));

		loadBar = new JProgressBar(JProgressBar.HORIZONTAL);
		loadBar.setValue(0);
		loadBar.setMaximum(1);
		loadBar.setPreferredSize(new Dimension(LOADER_WIDTH, 15));
		loadBar.setStringPainted(true);
		loadBar.setBorder(BorderFactory.createCompoundBorder());
		
		loadSubBar = new JProgressBar(JProgressBar.HORIZONTAL);
		loadSubBar.setValue(0);
		loadSubBar.setMaximum(1);
		loadSubBar.setPreferredSize(new Dimension(LOADER_WIDTH, 15));
		loadSubBar.setStringPainted(true);
		loadSubBar.setBorder(BorderFactory.createCompoundBorder());
		loadSubBar.setVisible(false);
		
		JPanel loadBars = new JPanel();
		loadBars.setLayout(new BorderLayout());
		loadBars.add(loadSubBar, BorderLayout.NORTH);
		loadBars.add(loadBar, BorderLayout.SOUTH);
		
		component.add(loadBars, BorderLayout.SOUTH);

		loadInfo = new JTextField(firstLabel);
		loadInfo.setPreferredSize(new Dimension(LOADER_WIDTH, 30));
		loadInfo.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		loadInfo.setEditable(false);
		loadSubInfo = new JTextArea(firstSubLabel);
		loadSubInfo.setPreferredSize(new Dimension(LOADER_WIDTH, 40));
		loadSubInfo.setEditable(false);
		
		component.add(loadInfo, BorderLayout.NORTH);
		component.add(loadSubInfo, BorderLayout.CENTER);
		
		component.addKeyListener(listener);
		component.setFocusable(true);
		component.requestFocus();
	}

	/**
	 * Sets new {@link String} values of loader
	 * @param label main loading part name
	 * @param subLabel sub-loading part name
	 * @see #newSubLoading(String)
	 */
	public static void newLoading(String label, String subLabel) {
		if (running) {
			if (subLabel == null) {
				subLabel = "";
			}
			loadInfo.setText(label);
			loadSubInfo.setText(subLabel);
			loadSubBar.setVisible(false);
		}
	}
	
	/**
	 * Sets state of loading bar
	 * @param value 0.0 - 1.0
	 * @see #update(int, int)
	 * @see #update(int, int, int)
	 */
	public static void update(double value) {
		update(0, LOADER_WIDTH, (int) (LOADER_WIDTH * value));
	}
	
	/**
	 * Sets state of loading bar, minimum is 0
	 * @param max maximum
	 * @param cur current value
	 * @see #update(double)
	 * @see #update(int, int, int)
	 */
	public static void update(int max, int cur) {
		update(0, max, cur);
	}
	
	/**
	 * Sets state of loading bar
	 * @param min minimum
	 * @param max maximum
	 * @param cur current value
	 * @see #update(int, int)
	 * @see #update(double)
	 */
	public static void update(int min, int max, int cur) {
		if (running) {
			loadBar.setMinimum(min);
			loadBar.setMaximum(max);
			loadBar.setValue(cur);
		}
	}

	/**
	 * Sets new {@link String} values of loader,
	 * main label is not changed
	 * @param subLabel sub-loading part name
	 * @see #newLoading(String, String)
	 */
	public static void newSubLoading(String subLabel) {
		if (running) {
			if (subLabel == null) {
				subLabel = "";
			}
			loadSubInfo.setText(subLabel);
		}
	}
	
	/**
	 * Sets state of secondary loading bar
	 * @param value 0.0 - 1.0
	 * @see #subUpdate(int, int)
	 * @see #subUpdate(int, int, int)
	 */
	public static void subUpdate(double value) {
		subUpdate(0, LOADER_WIDTH, (int) (LOADER_WIDTH * value));
	}
	
	/**
	 * Sets state of secondary loading bar, minimum is 0
	 * @param max maximum
	 * @param cur current value
	 * @see #subUpdate(int, int)
	 * @see #subUpdate(double)
	 */
	public static void subUpdate(int max, int cur) {
		subUpdate(0, max, cur);
	}
	
	/**
	 * Sets state of secondary loading bar
	 * @param min minimum
	 * @param max maximum
	 * @param cur current value
	 * @see #subUpdate(double)
	 * @see #subUpdate(int, int, int)
	 */
	public static void subUpdate(int min, int max, int cur) {
		if (running) {	
			loadSubBar.setVisible(true);
			loadSubBar.setMinimum(min);
			loadSubBar.setMaximum(max);
			loadSubBar.setValue(cur);
		}
	}
	
	/**
	 * Method hide implemented frame or close loading window
	 */
	public static void abort() {
		if (running) {
			if (frame != null) 
				frame.setVisible(false);
			if (panel != null)
				panel.setVisible(false);
			running = false;
		}
	}
}
