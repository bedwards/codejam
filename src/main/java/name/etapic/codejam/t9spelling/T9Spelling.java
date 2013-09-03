package name.etapic.codejam.t9spelling;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

public final class T9Spelling implements ProblemSolver {

	private static final Map<Byte, String> keypressMap = new HashMap<Byte, String>();

	static {
		keypressMap.put((byte) ' ', "0");
		keypressMap.put((byte) 'a', "2");
		keypressMap.put((byte) 'b', "22");
		keypressMap.put((byte) 'c', "222");
		keypressMap.put((byte) 'd', "3");
		keypressMap.put((byte) 'e', "33");
		keypressMap.put((byte) 'f', "333");
		keypressMap.put((byte) 'g', "4");
		keypressMap.put((byte) 'h', "44");
		keypressMap.put((byte) 'i', "444");
		keypressMap.put((byte) 'j', "5");
		keypressMap.put((byte) 'k', "55");
		keypressMap.put((byte) 'l', "555");
		keypressMap.put((byte) 'm', "6");
		keypressMap.put((byte) 'n', "66");
		keypressMap.put((byte) 'o', "666");
		keypressMap.put((byte) 'p', "7");
		keypressMap.put((byte) 'q', "77");
		keypressMap.put((byte) 'r', "777");
		keypressMap.put((byte) 's', "7777");
		keypressMap.put((byte) 't', "8");
		keypressMap.put((byte) 'u', "88");
		keypressMap.put((byte) 'v', "888");
		keypressMap.put((byte) 'w', "9");
		keypressMap.put((byte) 'x', "99");
		keypressMap.put((byte) 'y', "999");
		keypressMap.put((byte) 'z', "9999");
	}

	@Override
	public Solution solve(BufferedReader reader) throws Exception {
		final String desiredMessage = reader.readLine();
		StringBuilder keypresses = new StringBuilder();
		for (byte b : desiredMessage.getBytes()) {
			String keypress = keypressMap.get(b);
			if (keypresses.length() > 0 && keypresses.charAt(keypresses.length() - 1) == keypress.charAt(0)) {
				keypresses.append(' ');
			}
			keypresses.append(keypress);
		}
		return new Solution(keypresses.toString(), desiredMessage.length());
	}
}
