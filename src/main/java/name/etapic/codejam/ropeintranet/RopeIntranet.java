package name.etapic.codejam.ropeintranet;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

/**
 * https://code.google.com/codejam/contest/619102/dashboard#s=p0
 * 
 * This has a time complexity of order N-squared, because the findSet loop is
 * called within the findIntersectPointCount loop. It is good enough to solve
 * the large data set. The content analysis offers a solution of comparing the
 * A and B endpoints of each pair of ropes one by one. This solution differs
 * from that slightly as it determines the size of the intersection of the set
 * where Aj<Ai and the set where Bj>Bi for all j>i, and then does the opposite
 * (Aj>Ai and Bj<Bi). Summing up the sizes of all those sets, removing one rope
 * from the mix each time, gives the solution.
 */
public final class RopeIntranet implements ProblemSolver {

	private static Set<Integer> findSet(int[] endpoints, int i, boolean lessThan) {
		Set<Integer> set = new HashSet<Integer>(endpoints.length - i - 1);
		for (int j = i + 1; j < endpoints.length; j++) {
			if (endpoints[j] < endpoints[i] == lessThan) {
				set.add(j);
			}
		}
		return set;
	}

	private static long findIntersectPointCount(int[] A, int[] B) {
		long sum = 0;
		Set<Integer> set;
		for (int i = 0; i < A.length; i++) {
			for (boolean lessThan : new boolean[] { false, true }) {
				set = findSet(A, i, lessThan);
				set.retainAll(findSet(B, i, !lessThan));
				sum += set.size();
			}
		}
		return sum;
	}

	@Override
	public Solution solve(BufferedReader reader) throws Exception {
		int N = Integer.parseInt(reader.readLine());
		int[] A = new int[N];
		int[] B = new int[N];
		for (int i = 0; i < N; i++) {
			String[] ns = reader.readLine().split(" ");
			A[i] = Integer.parseInt(ns[0]);
			B[i] = Integer.parseInt(ns[1]);
		}
		String text = String.format("%s", findIntersectPointCount(A, B));
		return new Solution(text, N);
	}

}
