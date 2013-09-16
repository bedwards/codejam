package name.etapic.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Adapted from The Algorithm Design Manual, 2nd ed. 2008, Steven S. Skiena
 * Errata: http://www.cs.sunysb.edu/~skiena/algorist/book/errata
 * 
 * Pages 169-172, 5.8 Depth-First Search: Implementation
 */
final class DepthFirstSearch<T> {
	private final Graph<T> graph;
	private final Processor<T> processor;
	private Set<T> discovered = new HashSet<>();
	private Set<T> processed = new HashSet<>();
	private Map<T, T> parents = new HashMap<>();
	private Map<T, Integer> entryTimes = new HashMap<>();
	private Map<T, Integer> exitTimes = new HashMap<>();
	private int time;

	DepthFirstSearch(final Graph<T> graph, final Processor<T> processor) {
		this.graph = graph;
		this.processor = processor;
	}

	void search() {
		discovered = new HashSet<>();
		processed = new HashSet<>();
		parents = new HashMap<>();
		entryTimes = new HashMap<>();
		exitTimes = new HashMap<>();
		time = 0;
		dfs(graph.getAnyVertex());
	}

	private void dfs(T vertex) {
		if (processor.isFinished()) {
			return;
		}
		discovered.add(vertex);
		time++;
		entryTimes.put(vertex, time);
		processor.processVertexEarly(vertex);
		for (T adjacency : graph.getAdjacencies(vertex)) {
			if (!discovered.contains(adjacency)) {
				parents.put(adjacency, vertex);
				processor.processEdge(vertex, adjacency, new HashSet<>(discovered), new HashMap<>(parents));
				dfs(adjacency);
			} else if (!processed.contains(adjacency) && !parents.get(vertex).equals(adjacency)) {
				processor.processEdge(vertex, adjacency, new HashSet<>(discovered), new HashMap<>(parents));
			}
			if (processor.isFinished()) {
				return;
			}
		}
		processor.processVertexLate(vertex);
		time++;
		exitTimes.put(vertex, time);
		processed.add(vertex);
	}
}
