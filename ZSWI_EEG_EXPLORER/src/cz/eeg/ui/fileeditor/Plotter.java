package cz.eeg.ui.fileeditor;

import static cz.deznekcz.tool.Lang.LANG;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import cz.eeg.data.Channel;
import cz.eeg.data.EegFile;
import cz.eeg.io.BinaryData;

public class Plotter {

	private static ChartPanel graphPanel;
	private static JFrame frame;
	
	public static void open(EegFile vhdrFile, int... index) {
		if (frame == null) {
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			graphPanel = new ChartPanel(null);
			frame.add(graphPanel);
			frame.pack();
			frame.setLocationRelativeTo(null);
		}
		
		String channelList = LANG("channels_all");
		
		if (index != null && index.length > 0) {
			Channel[] channels = vhdrFile.getChannel();
			channelList = channels[index[0]].getName();
			for (int i = 1; i < index.length; i++) {
				channelList += ", " + channels[index[i]].getName();
			}
		} else {
			index = new int[vhdrFile.getNumberOfChannels()];
			for (int i = 0; i < index.length; i++) {
				index[i] = i;
			}
		}
		
		frame.setTitle(LANG("plotter", vhdrFile.getName(), channelList));
		
		graphPanel.setChart(
				createGraph(
						prepareDataSet(vhdrFile, index)));
		
		frame.setVisible(true);
		
	}
	
	private static XYSeriesCollection prepareDataSet(EegFile vhdrFile, int[] index) {
		final int NUMBER_OF_CHANNELS = vhdrFile.getNumberOfChannels();
		XYSeriesCollection series = new XYSeriesCollection();
		BinaryData.read(vhdrFile.getHeaderFile(), NUMBER_OF_CHANNELS);
		Channel[] channels = vhdrFile.getChannel();
		double[][] data = BinaryData.getDat();
		
		for (int i = 0; i < index.length; i++) {
			series.addSeries(values(channels[index[i]], data[index[i]]));
		}
		
		return series;
	}

	private static XYSeries values(Channel channel, double[] data) {
		XYSeries values = new XYSeries(channel.getName());
		for (int i = 0; i < data.length; i++) {
			values.add(i * channel.getResolutionInUnit(), data[i]);
		}
		return values;
	}

	private static JFreeChart createGraph(XYSeriesCollection data) {
		JFreeChart graph = ChartFactory.createXYLineChart(
				"", 
				LANG("plot_time_label"), 
				LANG("plot_strength_label"), 
				data, 
				PlotOrientation.VERTICAL, 
				true, true, false); // legend, tooltips, urls
		return graph;
	}

}
