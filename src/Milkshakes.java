import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Problem Statement - https://code.google.com/codejam/contest/32016/dashboard#s=p1
 * Contest Analysis (Solution) - https://code.google.com/codejam/contest/dashboard?c=32016#s=a&a=1
 * 
 * I did not notice this little nugget in the problem statement:
 *   'At most one of the types a customer likes will be a "malted" flavor.'
 *   
 * Which turns out to be the key to solving this problem within the 8-minute time window for the large data set.
 * 
 * From the Contest Analysis -
 * 
 *   'On the surface, this problem appears to require solving the classic problem "Satisfiability," the canonical
 *   example of an NP-complete problem. The customers represent clauses, the milkshake flavors represent variables,
 *   and malted and unmalted flavors represent whether the variable is negated.
 *   
 *   We are not evil enough to have chosen a problem that hard! The restriction that makes this problem easier is that
 *   the customers can only like at most one malted flavor'
 * 
 * From http://en.wikipedia.org/wiki/NP-complete - 
 * 
 *   'the time required to solve even moderately sized versions of many of these problems can easily reach into the
 *   billions or trillions of years, using any amount of computing power available today'
 *
 * I spent a good amount of time making this code as efficient as possible. I think I got the runtime for the large
 * data set down to a few billion years. I'll save it and wait for faster hardware.
 * 
 */
public class Milkshakes {

	public static void main(String[] args) {
		List<Sample> samples = new ArrayList<Sample>();
		samples.add(buildSample0());
		samples.add(buildSample1());
		samples.add(buildSample2());
		for (Sample sample : samples) {
			BitSet actual = findLeastMaltedCustomerSatisfyingSolution(sample.size, sample.customers);
			if ((actual == null && sample.expected != null) || (actual != null && !actual.equals(sample.expected))) {
				System.err.println(String.format("%s, actual=%s", sample, render(actual, sample.size)));
				System.exit(1);
			}
		}
		CodeJam.jam(new CodeJam.Strategy() {
			@Override
			public String execute(BufferedReader reader) throws Exception {
				int size = Integer.parseInt(reader.readLine());
				int customerCount = Integer.parseInt(reader.readLine());
				List<List<Flavor>> customers = new ArrayList<List<Flavor>>(customerCount);
				for (int i = 0; i < customerCount; i++) {
					customers.add(new ArrayList<Flavor>());
					String[] flavorSpecs = reader.readLine().split(" ");
					for (int j = 1; j < flavorSpecs.length; j += 2) {
						int index = Integer.parseInt(flavorSpecs[j]) - 1;
						boolean malted = flavorSpecs[j + 1].equals("1");
						customers.get(i).add(new Flavor(index, malted));
					}
				}
				BitSet b = findLeastMaltedCustomerSatisfyingSolution(size, customers);
				return render(b, size);
			}
		});
	}

	private static class Sample {

		private final int size;
		private final List<List<Flavor>> customers;
		private final BitSet expected;

		private Sample(final int size, final List<List<Flavor>> customers, final BitSet expected) {
			this.size = size;
			this.customers = customers;
			this.expected = expected;
		}

		@Override
		public String toString() {
			return String.format("size=%s, customers=%s, expected=%s", size, customers, render(expected, size));
		}
	}

	private static class Flavor {
		private final int index;
		private final boolean malted;

		private Flavor(final int index, final boolean malted) {
			this.index = index;
			this.malted = malted;
		}

		@Override
		public String toString() {
			return String.format("index=%s, malted=%s", index, malted);
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
		int size = 5;
		int customerCount = 3;
		List<List<Flavor>> customers = new ArrayList<List<Flavor>>(customerCount);
		customers.add(Arrays.asList(new Flavor(0, true)));
		customers.add(Arrays.asList(new Flavor(0, false), new Flavor(2, false)));
		customers.add(Arrays.asList(new Flavor(4, false)));
		BitSet expected = new BitSet(size);
		expected.set(0);
		return new Sample(size, customers, expected);
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
		int customerCount = 2;
		List<List<Flavor>> customers = new ArrayList<List<Flavor>>(customerCount);
		customers.add(Arrays.asList(new Flavor(0, false)));
		customers.add(Arrays.asList(new Flavor(0, true)));
		return new Sample(1, customers, null);
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
		int size = 1;
		int customerCount = 3;
		List<List<Flavor>> customers = new ArrayList<List<Flavor>>(customerCount);
		customers.add(Arrays.asList(new Flavor(0, true)));
		customers.add(Arrays.asList(new Flavor(0, true)));
		customers.add(Arrays.asList(new Flavor(0, true)));
		BitSet expected = new BitSet(size);
		expected.set(0);
		return new Sample(size, customers, expected);
	}

	/**
	 * Returns null if no solution can be found.
	 */
	private static BitSet findLeastMaltedCustomerSatisfyingSolution(int size, List<List<Flavor>> customers) {
		CustomerSatisficer satisficer = new CustomerSatisficer(size, customers);
		int cardinalityPivot = size / 2 + 1;
		if (size % 2 == 1) {
			cardinalityPivot++;
		}
		int cardinality;
		for (cardinality = 0; cardinality < cardinalityPivot; cardinality++) {
			findBitSets(size, cardinality, satisficer);
			if (satisficer.solution != null) {
				return satisficer.solution;
			}
		}
		satisficer.reverseLogic = true;
		for (cardinality = cardinalityPivot; cardinality >= 0; cardinality--) {
			findBitSets(size, cardinality, satisficer);
			if (satisficer.solution != null) {
				return satisficer.solution;
			}
		}
		return null;
	}

	private static String render(BitSet b, int size) {
		if (b == null) {
			return "IMPOSSIBLE";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			builder.append(b.get(i) ? "1" : "0");
			if (i < size - 1) {
				builder.append(" ");
			}
		}
		return builder.toString();
	}

	private static interface BitSetStrategy {
		/**
		 * Return true if processing should stop.
		 */
		boolean execute(BitSet b);
	}

	private static class BitSetCacher implements BitSetStrategy {
		final Set<BitSet> cache = new HashSet<BitSet>();

		@Override
		public boolean execute(BitSet b) {
			cache.add((BitSet) b.clone());
			return false;
		}

		@Override
		public String toString() {
			return String.format("cache=%s", cache);
		}
	}

	private static class CustomerSatisficer implements BitSetStrategy {

		private final int size;
		private final List<List<Flavor>> customers;
		private boolean reverseLogic = false;
		private BitSet solution = null;

		private CustomerSatisficer(final int size, final List<List<Flavor>> customers) {
			this.size = size;
			this.customers = customers;
		}

		@Override
		public boolean execute(BitSet b) {
			boolean foundFlavor = false;
			for (List<Flavor> customer : customers) {
				foundFlavor = false;
				for (Flavor flavor : customer) {
					foundFlavor = b.get(flavor.index) == flavor.malted;
					if (reverseLogic) {
						foundFlavor = !foundFlavor;
					}
					if (foundFlavor) {
						// this customer is satisfied
						break;
					}
				}
				if (!foundFlavor) {
					// fail fast on first dissatisfied customer
					return false;
				}
			}
			if (reverseLogic) {
				b.flip(0, size);
			}
			solution = b;
			// If it is possible to satisfy all your customers, there will be only one answer which minimizes the 
			// number of malted batches.
			return true;
		}

		@Override
		public String toString() {
			return String.format("customers=%s, reverseLogic=%s, solution=%s", customers, reverseLogic, solution);
		}
	}

	/**
	 * Find all BitSets of specified size and cardinality (number of bits set).
	 */
	private static void findBitSets(int size, int cardinality, BitSetStrategy strategy) {
		if (size < cardinality) {
			return;
		}
		BitSet b = new BitSet(size);
		if (cardinality == 0) {
			strategy.execute(b);
			return;
		}
		if (size == cardinality) {
			b.set(0, size);
			strategy.execute(b);
			return;
		}
		BitSetCacher cacher = new BitSetCacher();
		findBitSets(size - 1, cardinality, cacher);
		for (BitSet bSub : cacher.cache) {
			b.clear();
			b.or(bSub);
			if (strategy.execute(b)) {
				return;
			}
		}
		cacher.cache.clear();
		findBitSets(size - 1, cardinality - 1, cacher);
		for (BitSet bSub : cacher.cache) {
			b.clear();
			b.or(bSub);
			b.set(size - 1);
			if (strategy.execute(b)) {
				return;
			}
		}
	}

}
