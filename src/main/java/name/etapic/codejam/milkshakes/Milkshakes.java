package name.etapic.codejam.milkshakes;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

/**
 * For this one I "cheated" (this is practice mode) and peeked at the Content Analysis.
 *   https://code.google.com/codejam/contest/32016/dashboard#s=a&a=1
 */
public final class Milkshakes implements ProblemSolver {

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

	public Solution solve(BufferedReader reader) throws Exception {
		int flavorCount = Integer.parseInt(reader.readLine());
		int customerCount = Integer.parseInt(reader.readLine());
		System.out.println(String.format("flavorCount=%s, customerCount=%s", flavorCount, customerCount));
		Customer[] customers = new Customer[customerCount];
		String[] flavorSpecs;
		List<Integer> unmaltedIndexes;
		Integer maltedIndex;
		int flavorIndex;
		int i, j;
		for (i = 0; i < customerCount; i++) {
			flavorSpecs = reader.readLine().split(" ");
			System.out.println(String.format("flavorSpecs=%s", Arrays.toString(flavorSpecs)));
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
			System.out.println(String.format("%s", customers[i]));
		}
		boolean[] batches = findLeastMaltedCustomerSatisfyingBatches(flavorCount, customers);
		return new Solution(render(batches), customerCount);
	}

}
