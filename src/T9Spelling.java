import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class T9Spelling {

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

	private static String translateToKeypresses(final String desiredMessage) {
		StringBuilder builder = new StringBuilder();
		for (byte b : desiredMessage.getBytes()) {
			String keypress = keypressMap.get(b);
			if (builder.length() > 0 && builder.charAt(builder.length() - 1) == keypress.charAt(0)) {
				builder.append(' ');
			}
			builder.append(keypress);
		}
		return builder.toString();
	}

	public static void main(String[] args) throws Exception {
		List<String[]> examples = new ArrayList<String[]>(4);
		examples.add(new String[] { "hi", "44 444" });
		examples.add(new String[] { "yes", "999337777" });
		examples.add(new String[] { "foo  bar", "333666 6660 022 2777" });
		examples.add(new String[] { "hello world", "4433555 555666096667775553" });
		for (String[] example : examples) {
			String actual = translateToKeypresses(example[0]);
			assert actual.equals(example[1]);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		int caseNum = 1;
		while (reader.ready()) {
			String keypresses = translateToKeypresses(reader.readLine());
			System.out.println(String.format("Case #%s: %s", caseNum, keypresses));
			caseNum++;
		}
	}

}
