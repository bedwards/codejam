public class SmartererFasterer implements Algorithm {

	public String getName() {
		return "Smarterer Fasterer";
	}

	public int[] go(int[] source) {
		int[] gtCounts = new int[source.length];
		
		// O(n^2)
		for (int i = 0; i < source.length; i++) {
			int left = source[i];
			for (int j = i + 1; j < source.length; j++) {
				int right = source[j];
				if (left > right) {
					gtCounts[i]++;
				} else if (right > left) {
					gtCounts[j]++;
				}
			}
		}
		
		return gtCounts;
	}
}
