package name.etapic.graph;

import java.util.Map;
import java.util.Set;

interface Processor<T> {
	void processVertexEarly(T vertex);

	void processEdge(T vertex, T adjacency, Set<T> discovered, Map<T, T> parents);

	void processVertexLate(T vertex);

	boolean isFinished();
}
