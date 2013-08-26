package name.etapic.codejam;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://code.google.com/codejam/contest/635101/dashboard#s=p0
 */
final class FileFixit {

	private static interface Strategy {
		void execute(List<String> slice);
	}

	private static void processDirectories(int count, BufferedReader reader, Strategy strategy) throws Exception {
		int i, j;
		List<String> parts;
		for (i = 0; i < count; i++) {
			parts = Arrays.asList(reader.readLine().split("/"));
			for (j = 2; j <= parts.size(); j++) {
				strategy.execute(parts.subList(1, j));
			}
		}
	}

	public static void main(final String[] args) {
		CodeJam.jam(new CodeJam.Strategy() {
			@Override
			public String execute(BufferedReader reader) throws Exception {
				String[] NM = reader.readLine().split(" ");
				int N = Integer.parseInt(NM[0]);
				int M = Integer.parseInt(NM[1]);
				final Set<List<String>> existing = new HashSet<List<String>>(N);
				processDirectories(N, reader, new Strategy() {
					@Override
					public void execute(List<String> slice) {
						existing.add(slice);
					}
				});
				final int[] sum = new int[] { 0 };
				processDirectories(M, reader, new Strategy() {
					@Override
					public void execute(List<String> slice) {
						if (!existing.contains(slice)) {
							sum[0]++;
							existing.add(slice);
						}
					}
				});
				return String.format("%s", sum[0]);
			}
		});
	}
}