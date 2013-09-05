package name.etapic.codejam;

import static java.lang.System.nanoTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
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

	private final List<ProblemConfig> problemConfigs;

	CodeJam(final List<ProblemConfig> problemConfigs) {
		this.problemConfigs = problemConfigs;
	}

	void execute() {
		final String basePackageName = getClass().getPackage().getName();
		final ExecutorService executor = Executors.newCachedThreadPool();
		try {
			for (ProblemConfig problemConfig : problemConfigs) {
				System.out.println(problemConfig);
				for (SolverConfig solverConfig : problemConfig.getSolverConfigs()) {
					final Class<?> cls = Class.forName(String.format("%s.%s.%s", basePackageName,
							problemConfig.getPackageName(), solverConfig.getClassName()));
					final Class<? extends ProblemSolver> solverCls = (Class<? extends ProblemSolver>) cls
							.asSubclass(ProblemSolver.class);
					final ProblemSolver solver = solverCls.getConstructor().newInstance();
					for (String datasetName : solverConfig.getDatasetNames()) {
						final XYSeries series;
						try {
							series = solve(executor, solver, problemConfig.getPackageName(),
									solverConfig.getClassName(), datasetName);
						} catch (RuntimeException e) {
							if (e.getCause() instanceof TimeoutException) {
								continue;
							} else {
								throw e;
							}
						}
						if (!datasetName.equals("sample")) {
							renderGraph(solverConfig.getClassName(), series);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		} finally {
			executor.shutdown();
		}
	}

	private final static class Measurement {
		private final String text;
		private final int problemSize;
		private final long runningTime;

		Measurement(final Solution solution, final long runningTime) {
			this.text = solution.getText();
			this.problemSize = solution.getProblemSize();
			this.runningTime = runningTime;
		}
	}

	private static final XYSeries solve(final ExecutorService executor, final ProblemSolver solver,
			final String packageName, final String solverClassName, final String datasetName) {
		BufferedReader inReader = null;
		BufferedWriter outWriter = null;
		try {
			inReader = new BufferedReader(new InputStreamReader(CodeJam.class.getResourceAsStream(String.format(
					"%s/%s.in", packageName, datasetName))));
			File outFile = File.createTempFile(packageName, "." + datasetName);
			System.out.println(outFile.getAbsolutePath());
			outWriter = new BufferedWriter(new FileWriter(outFile));
			final XYSeries series = new XYSeries(datasetName);
			for (Measurement measurement : processDataset(executor, solver, packageName, solverClassName, datasetName,
					inReader, outWriter)) {
				series.add(measurement.problemSize, measurement.runningTime);
			}
			return series;
		} catch (Exception e) {
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		} finally {
			if (inReader != null) {
				try {
					inReader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				} finally {
					close(outWriter);
				}
			}
			close(outWriter);
		}
	}

	private static List<Measurement> processDataset(final ExecutorService executor, final ProblemSolver solver,
			final String packageName, final String solverClassName, final String datasetName,
			final BufferedReader inReader, final BufferedWriter outWriter) throws Exception {
		final int caseCount = Integer.parseInt(inReader.readLine());
		System.out.println(String.format("caseCount=%s", caseCount));
		final List<String> outLines = new ArrayList<String>(caseCount);
		final List<Measurement> measurements = new ArrayList<Measurement>();
		for (int caseNum = 0; caseNum < caseCount; caseNum++) {
			System.out.println(String.format("caseNum=%s", caseNum));
			Future<Measurement> future = executor.submit(new Callable<Measurement>() {
				@Override
				public Measurement call() throws Exception {
					long startTime = nanoTime();
					final Solution solution = solver.solve(inReader);
					long runningTime = nanoTime() - startTime;
					return new Measurement(solution, runningTime);
				}
			});
			final Measurement measurement;
			try {
				measurement = future.get(STRATEGY_TIMEOUT_SECONDS, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				System.err.println(String.format("Time out: %s.%s (%s dataset)", packageName, solverClassName,
						datasetName));
				future.cancel(true);
				throw e;
			}
			measurements.add(measurement);
			final String caseOutput = String.format("Case #%s: %s", caseNum + 1, measurement.text);
			outLines.add(caseOutput);
			System.out.println(caseOutput);
			outWriter.write(caseOutput);
			outWriter.newLine();
		}
        outWriter.flush();

		// check for correctness
		final List<String> expectedLines = readExpectedLines(packageName, datasetName, caseCount);
		for (int caseIndex = 0; caseIndex < caseCount; caseIndex++) {
			if (!outLines.get(caseIndex).equals(expectedLines.get(caseIndex))) {
				System.err.println(String.format("%s (%s dataset) Actual: '%s', Expected: '%s'", solverClassName,
						datasetName, outLines.get(caseIndex), expectedLines.get(caseIndex)));
				System.exit(1);
			}
		}
		return measurements;
	}

	private static List<String> readExpectedLines(final String packageName, final String datasetName,
			final int caseCount) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(CodeJam.class.getResourceAsStream(String
				.format("%s/%s.expected", packageName, datasetName))));
		try {
			final List<String> expectedLines = new ArrayList<String>(caseCount);
			for (int caseNum = 0; caseNum < caseCount; caseNum++) {
				expectedLines.add(reader.readLine());
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

	private static void close(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void renderGraph(final String solverClassName, final XYSeries series) {
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		final String title = String.format("%s (%s dataset)", solverClassName, series.getKey());
		final boolean legend = true;
		final boolean tooltips = true;
		final boolean urls = false;
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "Problem Size (N)", "Running Time T(N)",
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
