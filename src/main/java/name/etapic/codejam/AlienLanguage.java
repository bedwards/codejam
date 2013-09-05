package name.etapic.codejam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * https://code.google.com/codejam/contest/90101/dashboard
 * 
 * For the sample dictionary (abc, bca, dac, dbc, cba) this implementation builds up the following data structure:
 * 
 *   [0: {a: [abc],
 *        b: [bca],
 *        c: [cba],
 *        d: [dac, dbc],
 *        e: [],
 *        ...
 *        z: []
 *        },
 *   
 *    1: {a: [dac],
 *        b: [abc, cba, dbc],
 *        c: [bca],
 *        d: [],
 *        e: [],
 *        ...
 *        z: []
 *        },
 *   
 *    2: {a: [bca, cba],
 *        b: [],
 *        c: [dac, abc, dbc],
 *        d: [],
 *        e: [],
 *        ...
 *        z: []
 *        }
 *        
 * Then does set intersection starting with all words in the dictionary and iterating over each token in the pattern.
 */
final class AlienLanguage {

	private static final int ALPHABET_SIZE = 26;

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String[] ns = reader.readLine().split(" ");
		assert ns.length == 3;
		int wordSize = Integer.parseInt(ns[0]);
		int dictSize = Integer.parseInt(ns[1]);
		int caseCount = Integer.parseInt(ns[2]);
        List<Map<Character, Set<String>>> dictionary = new ArrayList<>(wordSize);
        for (int j = 0; j < wordSize; j++) {
			dictionary.add(new HashMap<Character, Set<String>>(ALPHABET_SIZE));
			for (char c = 'a'; c <= 'z'; c++) {
				dictionary.get(j).put(c, new HashSet<String>(dictSize));
			}
		}
        Set<String> allWords = new HashSet<>(dictSize);
        for (int i = 0; i < dictSize; i++) {
			String word = reader.readLine();
			allWords.add(word);
			for (int j = 0; j < wordSize; j++) {
				dictionary.get(j).get(word.charAt(j)).add(word);
			}
		}
		Set<String> solution, matchThisToken;
		for (int caseNum = 0; caseNum < caseCount; caseNum++) {
			System.err.println(String.format("caseNum=%s", caseNum));
			List<List<Character>> pattern = parsePattern(reader.readLine(), wordSize);
            solution = new HashSet<>(allWords);
            for (int j = 0; j < wordSize; j++) {
                matchThisToken = new HashSet<>(dictSize);
                for (char c : pattern.get(j)) {
					matchThisToken.addAll(dictionary.get(j).get(c));
				}
				solution.retainAll(matchThisToken);
			}
			System.out.println(String.format("Case #%s: %s", caseNum + 1, solution.size()));
		}
	}

	private static List<List<Character>> parsePattern(String patternStr, int wordSize) {
        List<List<Character>> pattern = new ArrayList<>(wordSize);
        List<Character> token = new ArrayList<>(ALPHABET_SIZE);
        boolean inParens = false;
		for (int i = 0; i < patternStr.length(); i++) {
			char c = patternStr.charAt(i);
			if (inParens) {
				switch (c) {
				case ')':
					inParens = false;
					pattern.add(token);
                    token = new ArrayList<>(ALPHABET_SIZE);
                    break;
				default:
					token.add(c);
					break;
				}
			} else {
				switch (c) {
				case '(':
					inParens = true;
					break;
				default:
					token.add(c);
					pattern.add(token);
                    token = new ArrayList<>(ALPHABET_SIZE);
                    break;
				}
			}
		}
		return pattern;
	}
}
