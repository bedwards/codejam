import java.util.Arrays;

public class OriginalSort implements Algorithm {

	public String getName() {
		return "Original Sort";
	}

	public int[] go(int[] source) {
		int[] gtCounts = new int[source.length];
		IndexValue[] intermediate = new IndexValue[source.length];

		// O(n)
		for (int i = 0; i < source.length; i++) {
			intermediate[i] = new IndexValue(i, source);
		}

		System.out.println(Thread.currentThread().getName());

		// O(n*log(n))
		Arrays.sort(intermediate);

		// O(n)
		for (int j = 0; j < source.length; j++) {
			int i = intermediate[j].index;
			gtCounts[i] = j;
		}
		return gtCounts;
	}
}
