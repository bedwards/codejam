package name.etapic.complexity;

import java.util.PriorityQueue;
import java.util.Queue;

public class QueueSort implements Algorithm {

	public String getName() {
		return "Queue Sort";
	}

	public int[] go(int[] source) {
		int[] gtCounts = new int[source.length];
		Queue<IndexValue> intermediate = new PriorityQueue<IndexValue>(source.length);

		// O(n*log(n))
		for (int i = 0; i < source.length; i++) {
			intermediate.add(new IndexValue(i, source));
		}

		// O(n)
		for (int j = 0; j < source.length; j++) {
			IndexValue iv = intermediate.poll();
			gtCounts[iv.index] = j;
		}

		return gtCounts;
	}
}
