package name.etapic.codejam;

import java.util.List;

final class SolverConfig {
	private final String className;
	private final List<String> datasetNames;

	SolverConfig(final String className, final List<String> datasetNames) {
		this.className = className;
		this.datasetNames = datasetNames;
	}

	public String getClassName() {
		return className;
	}

	public List<String> getDatasetNames() {
		return datasetNames;
	}

}
