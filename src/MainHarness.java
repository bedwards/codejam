import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainHarness {

	public static int[] readSource(final int rowCount) throws Exception {
		int[] source = new int[rowCount];
		BufferedReader reader = new BufferedReader(new FileReader(String.format("random_%s.txt", rowCount)));
		try {
			for (int i = 0; i < rowCount; i++) {
				source[i] = Integer.parseInt(reader.readLine());
			}
		} finally {
			reader.close();
		}
		return source;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Main Harness");
		final int lowestPower = 1;
		final int highestPower = 7;
		for (int expon = lowestPower; expon <= highestPower; expon++) {
			final int rowCount = (int) Math.round(Math.pow(10, expon));
			int[] source = readSource(rowCount);
			int[] target;

			// check that all algorithms return same result for the smallest data set
			Map<String, int[]> correctnessMap = new HashMap<String, int[]>();

			for (Algorithm algorithm : Arrays.asList(new OriginalSort(), new QueueSort())) {
				System.out.println(String.format("\n%s", algorithm.getName()));
				long startTime = System.nanoTime();
				target = algorithm.go(source);
				long estimatedTime = System.nanoTime() - startTime;
				System.out.println(String.format("%s %s", rowCount, estimatedTime));
				if (expon == 1) {
					for (int i = 0; i < rowCount; i++) {
						correctnessMap.put(algorithm.getName(), target);
					}
				}
			}
			if (expon == 1) {
				String firstName = null;
				int[] firstTarget = null;
				for (Map.Entry<String, int[]> entry : correctnessMap.entrySet()) {
					if (firstTarget == null) {
						firstName = entry.getKey();
						firstTarget = entry.getValue();
						continue;
					}
					String curName = entry.getKey();
					int[] curTarget = entry.getValue();
					for (int i = 0; i < rowCount; i++) {
						if (curTarget[i] != firstTarget[i]) {
							System.out.println(String.format("%s returned different target values than %s", firstName,
									curName));
							System.out.println(String.format("%s: %s, %s", source[i], firstTarget[i], curTarget[i]));
							System.exit(1);
						}
					}
				}
			}
		}
	}

}
