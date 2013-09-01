package name.etapic.codejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class ProblemConfig {

	private final String name;
	private final String packageName;
	private final String url;
	private final List<SolverConfig> solverConfigs;

	ProblemConfig(final String name, final String packageName, final String url, final List<SolverConfig> solverConfigs) {
		this.name = name;
		this.packageName = packageName;
		this.url = url;
		this.solverConfigs = Collections.unmodifiableList(solverConfigs);
	}

	String getName() {
		return name;
	}

	String getPackageName() {
		return packageName;
	}

	String getUrl() {
		return url;
	}

	List<SolverConfig> getSolverConfigs() {
		return new ArrayList<SolverConfig>(solverConfigs);
	}

	@Override
	public String toString() {
		return String.format("Problem: %s (%s)", name, url);
	}

}
