package name.etapic.codejam;

import static java.lang.System.err;
import static java.lang.System.nanoTime;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

final class CodeJam {

	private static final long STRATEGY_TIMEOUT_SECONDS = 5;

	static interface Strategy {
		String getProblemName();

		Solution execute(BufferedReader reader) throws Exception;
	}

	static class Solution {
		final String text;
		final int problemSize;

		Solution(final String text, final int problemSize) {
			this.text = text;
			this.problemSize = problemSize;
		}
	}

	static class Measurement {
		final String text;
		final int problemSize;
		final long runningTime;

		Measurement(final Solution solution, final long runningTime) {
			this.text = solution.text;
			this.problemSize = solution.problemSize;
			this.runningTime = runningTime;
		}
	}

	private static BufferedReader getReader(String directory, String dataset, String extension) {
		String name = String.format("/%s/%s.%s", directory, dataset, extension);
		return new BufferedReader(new InputStreamReader(CodeJam.class.getResourceAsStream(name)));
	}

	private static String[] readExpectedLines(final String problemName, final String dataset, final int caseCount) {
		final BufferedReader reader = getReader(problemName, dataset, "expected");
		try {
			final String[] expectedLines = new String[caseCount];
			for (int caseNum = 0; caseNum < caseCount; caseNum++) {
				expectedLines[caseNum] = reader.readLine();
			}
			return expectedLines;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static final class DatasetProcessor {
		private final ExecutorService executor = Executors.newCachedThreadPool();
		private final Strategy strategy;

		private DatasetProcessor(final Strategy strategy) {
			this.strategy = strategy;
		}

		private List<Measurement> processDataset(final String datasetName, final BufferedReader inReader,
				final BufferedWriter outWriter) throws Exception {
			final int caseCount = Integer.parseInt(inReader.readLine());

			out.println(String.format("caseCount=%s", caseCount));
			final String[] outLines = new String[caseCount];
			final List<Measurement> measurements = new ArrayList<Measurement>();
			for (int caseNum = 0; caseNum < caseCount; caseNum++) {
				out.println(String.format("caseNum=%s", caseNum));
				Future<Measurement> future = executor.submit(new Callable<Measurement>() {
					@Override
					public Measurement call() throws Exception {
						long startTime = nanoTime();
						final Solution solution = strategy.execute(inReader);
						long runningTime = nanoTime() - startTime;
						return new Measurement(solution, runningTime);
					}
				});
				try {
					final Measurement measurement = future.get(STRATEGY_TIMEOUT_SECONDS, TimeUnit.SECONDS);
					measurements.add(measurement);
					final String caseOutput = String.format("Case #%s: %s", caseNum + 1, measurement.text);
					outLines[caseNum] = caseOutput;
					out.println(caseOutput);
					outWriter.write(caseOutput);
					outWriter.newLine();
				} catch (TimeoutException e) {
					err.println(String.format("Time out: %s %s", strategy.getProblemName(), datasetName));
					System.exit(1);
				}
			}

			// check for correctness
			final String[] expectedLines = readExpectedLines(strategy.getProblemName(), datasetName, caseCount);
			for (int caseNum = 0; caseNum < caseCount; caseNum++) {
				if (!outLines[caseNum].equals(expectedLines[caseNum])) {
					err.println(String.format("%s Actual: '%s', Expected: '%s'", datasetName, outLines[caseNum],
							expectedLines[caseNum]));
					System.exit(1);
				}
			}
			return measurements;
		}
	}

	static void jam(final Strategy strategy) {
		final String problemName = strategy.getProblemName();
		final DatasetProcessor processor = new DatasetProcessor(strategy);
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for (String datasetName : new String[] { "sample", "small", "large" }) {
			BufferedReader inReader = null;
			BufferedWriter outWriter = null;
			try {
				inReader = getReader(problemName, datasetName, "in");
				File outFile = File.createTempFile(problemName, "." + datasetName);
				System.out.println(outFile.getAbsolutePath());
				outWriter = new BufferedWriter(new FileWriter(outFile));
				final XYSeries series = new XYSeries(datasetName);
				for (Measurement measurement : processor.processDataset(datasetName, inReader, outWriter)) {
					series.add(measurement.problemSize, measurement.runningTime);
				}
				dataset.addSeries(series);
			} catch (Exception e) {
				throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
			} finally {
				try {
					inReader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				} finally {
					if (outWriter != null) {
						try {
							outWriter.close();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
		final boolean legend = true;
		final boolean tooltips = true;
		final boolean urls = false;
		final JFreeChart chart = ChartFactory.createScatterPlot(problemName, "Problem Size (N)", "Running Time T(N)",
				dataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
		final XYPlot plot = (XYPlot) chart.getPlot();
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		plot.setRenderer(renderer);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1500, 900));
		final ApplicationFrame frame = new ApplicationFrame("Google Code Jam Solution Analysis");
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
	}
}