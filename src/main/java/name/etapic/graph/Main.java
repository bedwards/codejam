package name.etapic.graph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
	public static void main(final String[] args) throws Exception {
		final InputStream stream = Graph.class.getResourceAsStream("contiguous-usa.txt");
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		Graph.Builder<String> builder = new Graph.Builder<>();
		while (reader.ready()) {
			String[] states = reader.readLine().split(" ");
			builder.add(states[0], states[1]);
		}
		Graph<String> graph = builder.build();
		graph.depthFirstSearch(new CycleFinder<String>());
	}
}
