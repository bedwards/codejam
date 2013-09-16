package name.etapic.graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Adapted from The Algorithm Design Manual, 2nd ed. 2008, Steven S. Skiena
 * Errata: http://www.cs.sunysb.edu/~skiena/algorist/book/errata
 * 
 * Pages 151-152, 5.2 Data Structures for Graphs: Adjacency Lists
 */
final class Graph<T> {

	private final Map<T, Set<T>> adjacencies;

	static class Builder<E> {
		private final Map<E, Set<E>> adjs = new HashMap<>();

		void add(final E left, final E right) {
			final List<E> vertices = Arrays.asList(left, right);
			for (int i = 0; i < 2; i++) {
				if (!adjs.containsKey(vertices.get(0))) {
					adjs.put(vertices.get(0), new HashSet<E>(Arrays.asList(vertices.get(1))));
				} else {
					adjs.get(vertices.get(0)).add(vertices.get(1));
				}
				Collections.reverse(vertices);
			}
		}

		Graph<E> build() {
			return new Graph<E>(adjs);
		}
	}

	private Graph(final Map<T, Set<T>> adjacencies) {
		this.adjacencies = new HashMap<>(adjacencies);
	}

	@Override
	public String toString() {
		return String.format("Graph %s", adjacencies.keySet());
	}

	T getAnyVertex() {
		return adjacencies.keySet().iterator().next();
	}

	Set<T> getAdjacencies(final T vertex) {
		return new HashSet<>(adjacencies.get(vertex));
	}

	void depthFirstSearch(final Processor<T> processor) {
		new DepthFirstSearch<>(this, processor).search();
	}

}
