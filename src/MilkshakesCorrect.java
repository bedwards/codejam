import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * For this one I "cheated" (this is practice mode) and peeked at the Content Analysis.
 *   https://code.google.com/codejam/contest/32016/dashboard#s=a&a=1
 *   
 * Run with:
 *   java -cp path/to/dir/with/class/file MilkshakesCorrect < B-large-practice.in | tee B-large-practice.out
 */
public class MilkshakesCorrect {

	/**
	 * Returns null if no solution can be found.
	 */
	private static boolean[] findLeastMaltedCustomerSatisfyingBatches(int flavorCount, Customer[] customers) {
		// Start with every flavor unmalted
		boolean[] batches = new boolean[flavorCount];
		// consider the customers one by one.
		int i = 0;
		while (i < customers.length) {
			if (!customers[i].isSatisfied(batches)) {
				// If there is an unsatisfied customer who only likes unmalted flavors, and all those flavors have been
				// made malted
				if (customers[i].maltedIndex == null) {
					// then no solution is possible.
					return null;
				}
				// If there is an unsatisfied customer who has one favorite malted flavor, then we must make that flavor
				// malted. We do this, then re-check all customers with new batches.
				batches[customers[i].maltedIndex] = true;
				i = 0;
				continue;
			}
			i++;
		}
		// If there are no unsatisfied customers, then we already have a valid solution and can leave the remaining
		// flavors unmalted.
		return batches;
	}

	private static class Customer {

		private final List<Integer> unmaltedIndexes;
		private final Integer maltedIndex;

		private Customer(final List<Integer> unmaltedIndexes, final Integer maltedIndex) {
			this.unmaltedIndexes = unmaltedIndexes;
			this.maltedIndex = maltedIndex;
		}

		private boolean isSatisfied(final boolean[] batches) {
			for (int unmaltedIndex : unmaltedIndexes) {
				if (!batches[unmaltedIndex]) {
					return true;
				}
			}
			return maltedIndex != null && batches[maltedIndex];
		}

		@Override
		public String toString() {
			return String.format("unmaltedIndexes=%s, maltedIndex=%s", unmaltedIndexes, maltedIndex);
		}

	}

	private static String render(boolean[] batches) {
		if (batches == null) {
			return "IMPOSSIBLE";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < batches.length; i++) {
			builder.append(batches[i] ? "1" : "0");
			if (i < batches.length - 1) {
				builder.append(" ");
			}
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		List<Sample> samples = new ArrayList<Sample>();
		samples.add(buildSample0());
		samples.add(buildSample1());
		samples.add(buildSample2());
		for (Sample sample : samples) {
			boolean[] actual = findLeastMaltedCustomerSatisfyingBatches(sample.flavorCount, sample.customers);
			if ((actual == null && sample.expected != null)
					|| (actual != null && !Arrays.equals(actual, sample.expected))) {
				System.err.println(String.format("%s, actual=%s", sample, render(actual)));
				System.exit(1);
			}
		}
		CodeJam.jam(new CodeJam.Strategy() {
			@Override
			public String execute(BufferedReader reader) throws Exception {
				int flavorCount = Integer.parseInt(reader.readLine());
				int customerCount = Integer.parseInt(reader.readLine());
				System.err.println(String.format("flavorCount=%s, customerCount=%s", flavorCount, customerCount));
				Customer[] customers = new Customer[customerCount];
				String[] flavorSpecs;
				List<Integer> unmaltedIndexes;
				Integer maltedIndex;
				int flavorIndex;
				int i, j;
				for (i = 0; i < customerCount; i++) {
					flavorSpecs = reader.readLine().split(" ");
					System.err.println(String.format("flavorSpecs=%s", Arrays.toString(flavorSpecs)));
					unmaltedIndexes = new ArrayList<Integer>(Integer.parseInt(flavorSpecs[0]));
					maltedIndex = null;
					for (j = 1; j < flavorSpecs.length; j += 2) {
						flavorIndex = Integer.parseInt(flavorSpecs[j]) - 1;
						if (flavorSpecs[j + 1].equals("0")) {
							unmaltedIndexes.add(flavorIndex);
						} else {
							maltedIndex = flavorIndex;
						}
					}
					customers[i] = new Customer(unmaltedIndexes, maltedIndex);
					System.err.println(String.format("%s", customers[i]));
				}
				boolean[] batches = findLeastMaltedCustomerSatisfyingBatches(flavorCount, customers);
				return render(batches);
			}
		});
	}

	private static class Sample {

		private final int flavorCount;
		private final Customer[] customers;
		private final boolean[] expected;

		private Sample(final int flavorCount, final Customer[] customers, final boolean[] expected) {
			this.flavorCount = flavorCount;
			this.customers = customers;
			this.expected = expected;
		}

		@Override
		public String toString() {
			return String.format("flavorCount=%s, customers=%s, expected=%s", flavorCount, customers, render(expected));
		}
	}

	private static Sample buildSample0() {
		// Input:
		//   5
		//   3
		//   1 1 1
		//   2 1 0 2 0
		//   1 5 0
		//
		// Expected:
		//   1 0 0 0 0
		//
		int flavorCount = 5;
		int customerCount = 3;
		Customer[] customers = new Customer[customerCount];
		customers[0] = new Customer(Collections.<Integer> emptyList(), 0);
		customers[1] = new Customer(Arrays.asList(0, 1), null);
		customers[2] = new Customer(Arrays.asList(4), null);
		boolean[] expected = new boolean[] { true, false, false, false, false };
		return new Sample(flavorCount, customers, expected);
	}

	private static Sample buildSample1() {
		// Input:
		//   1
		//   2
		//   1 1 0
		//   1 1 1
		//
		// Expected:
		//   null
		//
		int flavorCount = 1;
		int customerCount = 2;
		Customer[] customers = new Customer[customerCount];
		customers[0] = new Customer(Arrays.asList(0), null);
		customers[1] = new Customer(Collections.<Integer> emptyList(), 0);
		boolean[] expected = null;
		return new Sample(flavorCount, customers, expected);
	}

	private static Sample buildSample2() {
		// Input:
		//   1
		//   3
		//   1 1 1
		//   1 1 1
		//   1 1 1
		//
		// Expected:
		//   1
		int flavorCount = 1;
		int customerCount = 3;
		Customer[] customers = new Customer[customerCount];
		customers[0] = new Customer(Collections.<Integer> emptyList(), 0);
		customers[1] = new Customer(Collections.<Integer> emptyList(), 0);
		customers[2] = new Customer(Collections.<Integer> emptyList(), 0);
		boolean[] expected = new boolean[] { true };
		return new Sample(flavorCount, customers, expected);
	}
}
