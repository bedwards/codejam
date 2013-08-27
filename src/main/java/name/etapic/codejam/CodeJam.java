package name.etapic.codejam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

final class CodeJam {

	public static interface Strategy {
		String execute(BufferedReader reader) throws Exception;
	}

	public static void jam(Strategy strategy) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int caseCount;
		try {
			caseCount = Integer.parseInt(reader.readLine());
			System.err.println(String.format("caseCount=%s", caseCount));
		} catch (Exception e) {
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		}
		for (int caseNum = 0; caseNum < caseCount; caseNum++) {
			System.err.println(String.format("caseNum=%s", caseNum));
			String solution;
			try {
				solution = strategy.execute(reader);
			} catch (Exception e) {
				throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
			}
			System.out.println(String.format("Case #%s: %s", caseNum + 1, solution));
		}
	}
}