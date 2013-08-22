public class SortNSquared implements Algorithm {

	public String getName() {
		return "Sort N-squared";
	}

	public int[] go(int[] source) {
		int[] gtCounts = new int[source.length];
		
		// O(n^2)
		for (int i = 0; i < source.length; i++) {
			int left = source[i];
			for (int j = 0; j < source.length; j++) {
				if (i == j) {
					continue;
				}
				int right = source[j];
				if (left > right) {
					gtCounts[i]++;
				}
			}
		}
		return gtCounts;
	}
}
