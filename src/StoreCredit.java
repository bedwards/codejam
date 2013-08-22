import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class StoreCredit {

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

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		int caseNum = 1;
		while (reader.ready()) {
			int credit = Integer.parseInt(reader.readLine());
			reader.readLine();
			List<Integer> prices = new ArrayList<Integer>();
			for (String priceStr : reader.readLine().split(" ")) {
				prices.add(Integer.parseInt(priceStr));
			}
			int[] indexes = findIndexes(credit, prices);
			System.out.println(String.format("Case #%s: %s %s", caseNum, indexes[0], indexes[1]));
			caseNum++;
		}
	}

}
