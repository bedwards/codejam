import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * https://code.google.com/codejam/contest/32016/dashboard#s=p0
 */
class MinScalarProduct {

	private static BigInteger findMinScalarProduct(List<Integer> v1, List<Integer> v2) {
		assert v1.size() == v2.size();
		List<Integer> sorted1 = new ArrayList<Integer>(v1);
		List<Integer> sorted2 = new ArrayList<Integer>(v2);
		Collections.sort(sorted1);
		Collections.sort(sorted2);
		BigInteger result = BigInteger.ZERO;
		BigInteger x, y;
		for (int i = 0; i < v1.size(); i++) {
			x = BigInteger.valueOf(sorted1.get(i));
			y = BigInteger.valueOf(sorted2.get(v1.size() - i - 1));
			result = result.add(x.multiply(y));
		}
		return result;
	}

	private static class Sample {
		private final List<Integer> v1;
		private final List<Integer> v2;
		private final BigInteger expected;

		private Sample(final List<Integer> v1, final List<Integer> v2, final long expected) {
			this.v1 = v1;
			this.v2 = v2;
			this.expected = BigInteger.valueOf(expected);
		}

		@Override
		public String toString() {
			return String.format("%s %s %s", v1, v2, expected);
		}
	}

	public static void main(String[] args) throws Exception {
		List<Sample> samples = new ArrayList<Sample>(2);
		samples.add(new Sample(Arrays.asList(1, 3, -5), Arrays.asList(-2, 4, 1), -25));
		samples.add(new Sample(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(1, 0, 1, 0, 1), 6));
		for (Sample sample : samples) {
			BigInteger actual = findMinScalarProduct(sample.v1, sample.v2);
			assert actual.equals(sample.expected);
			// System.out.println(String.format("%s %s", sample, actual));
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		int caseNum = 1;
		while (reader.ready()) {
			reader.readLine();
			List<List<Integer>> vectors = new ArrayList<List<Integer>>(2);
			for (int i = 0; i < 2; i++) {
				String[] ns = reader.readLine().split(" ");
				vectors.add(new ArrayList<Integer>(ns.length));
				for (String n : ns) {
					vectors.get(i).add(Integer.parseInt(n));
				}
			}
			BigInteger result = findMinScalarProduct(vectors.get(0), vectors.get(1));
			System.out.println(String.format("Case #%s: %s", caseNum, result));
			caseNum++;
		}
	}
}
