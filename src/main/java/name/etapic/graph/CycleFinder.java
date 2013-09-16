package name.etapic.graph;

import java.util.Map;
import java.util.Set;

/**
 * Adapted from The Algorithm Design Manual, 2nd ed. 2008, Steven S. Skiena
 * Errata: http://www.cs.sunysb.edu/~skiena/algorist/book/errata
 *
 * Page 165, 5.6.2 Finding Paths
 * Page 173, 5.9.1 Finding Cycles
 */
public class CycleFinder<T> implements Processor<T> {

	@Override
	public void processVertexEarly(final T vertex) {
	}

	@Override
	public void processEdge(final T vertex, final T adjacency, final Set<T> discovered, final Map<T, T> parents) {
		if (discovered.contains(adjacency) && parents.containsKey(vertex) && !parents.get(vertex).equals(adjacency)) {
			System.out.printf("Cycle from %s to %s:", adjacency, vertex);
			findPath(adjacency, vertex, parents);
			System.out.println("\n");
		}
	}

	private void findPath(final T start, final T end, final Map<T, T> parents) {
		if (start.equals(end) || end == null) {
			System.out.printf("\n%s", start);
		} else {
			T nextEnd = parents.containsKey(end) ? parents.get(end) : null;
			findPath(start, nextEnd, parents);
			System.out.printf(" %s", end);
		}
	}

	@Override
	public void processVertexLate(final T vertex) {
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
