package name.etapic.codejam.storecredit;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

public final class StoreCredit implements ProblemSolver {
	@Override
	public Solution solve(BufferedReader reader) throws Exception {
		int credit = Integer.parseInt(reader.readLine());
		int priceCount = Integer.parseInt(reader.readLine());
		List<Integer> prices = new ArrayList<>(priceCount);
		for (String priceStr : reader.readLine().split(" ")) {
			prices.add(Integer.parseInt(priceStr));
		}
		int[] indexes = findIndexes(credit, prices);
		String text = String.format("%s %s", indexes[0], indexes[1]);
		return new Solution(text, priceCount);
	}

	private static int[] findIndexes(final int credit, final List<Integer> prices) {
		for (int i = 0; i < prices.size(); i++) {
			int price = prices.get(i);
			if (price > credit) {
				continue;
			}
			int subListIndex = prices.subList(i + 1, prices.size()).indexOf(credit - price);
			if (subListIndex != -1) {
				return new int[] { i + 1, subListIndex + i + 2 };
			}
		}
		return null;
	}
}
