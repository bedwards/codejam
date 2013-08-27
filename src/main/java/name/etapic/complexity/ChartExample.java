package name.etapic.complexity;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 * Not mine. Copied from
 * http://stackoverflow.com/questions/5290812/jfreechart-scatter-plot-lines
 */
final class ChartExample {

	public static void main(final String[] args) {
		final XYSeriesCollection dataset = new XYSeriesCollection();
		final XYSeries data = new XYSeries("Series Key");
		data.add(1, 1);
		data.add(2, 3);
		data.add(3, 2);
		data.add(4, 1);
		dataset.addSeries(data);
		final boolean legend = true;
		final boolean tooltips = true;
		final boolean urls = false;
		final JFreeChart chart = ChartFactory.createScatterPlot("Chart Title", "X Axis Label", "Y Axis Label", dataset,
				PlotOrientation.VERTICAL, legend, tooltips, urls);
		final XYPlot plot = (XYPlot) chart.getPlot();
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		plot.setRenderer(renderer);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		final ApplicationFrame frame = new ApplicationFrame("Frame Title");
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
	}

}
