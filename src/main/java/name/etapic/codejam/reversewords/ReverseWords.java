package name.etapic.codejam.reversewords;

import java.io.BufferedReader;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

public final class ReverseWords implements ProblemSolver {

	@Override
	public Solution solve(final BufferedReader reader) throws Exception {
		final String[] words = reader.readLine().split(" ");
		final StringBuilder reversed = new StringBuilder();
		for (int i = words.length - 1; i >= 0; i--) {
			reversed.append(words[i]);
			if (i > 0) {
				reversed.append(' ');
			}
		}
		return new Solution(reversed.toString(), words.length);
	}
}
