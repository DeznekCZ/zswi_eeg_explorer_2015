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
	
	public static void open(Channel channel, int index, EegFile vhdrFile) {
		if (frame == null) {
			frame = new JFrame();
			graphPanel = new ChartPanel(null);
		}

		String visibleChannel = (channel == null ? LANG("channels_all") : channel.getName());
		frame.setTitle(LANG("plotter", vhdrFile.getName(), visibleChannel));
		
		graphPanel.setChart(
				createGraph(
						prepareDataSet(vhdrFile, channel, index)));
		
	}

	private static XYSeriesCollection prepareDataSet(EegFile vhdrFile, Channel channel,
			int index) {
		final int NUMBER_OF_CHANNELS = vhdrFile.getNumberOfChannels();
		XYSeriesCollection series = new XYSeriesCollection();
		BinaryData.read(vhdrFile.getHeaderFile(), NUMBER_OF_CHANNELS);
		
		if (index == -1) {
			double[][] data = BinaryData.getDat();
			for (int i = 0; i < NUMBER_OF_CHANNELS; i++) {
				series.addSeries(values(channel, data[i]));
			}
		} else {
			series.addSeries(values(channel, BinaryData.getDat()[index]));
		}
		
		return series;
	}

	private static XYSeries values(Channel channel, double[] data) {
		XYSeries values = new XYSeries(channel.getName());
		for (int i = 0; i < data.length; i++) {
			values.add(i / channel.getResolutionInUnit(), data[i]);
		}
		return values;
	}

	private static JFreeChart createGraph(XYSeriesCollection data) {
		JFreeChart graph = ChartFactory.createXYLineChart(
				"", 
				LANG("plot_time_label"), 
				LANG("plot_strength_label"), 
				data, 
				PlotOrientation.HORIZONTAL, 
				true, true, false); // legend, tooltips, urls
		return graph;
	}

}
