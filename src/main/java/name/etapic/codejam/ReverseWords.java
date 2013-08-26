package name.etapic.codejam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReverseWords {

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		int caseNum = 1;
		String[] words;
		while (reader.ready()) {
			System.out.print(String.format("Case #%s: ", caseNum));
			words = reader.readLine().split("\\s");
			for (int i = words.length - 1; i >= 0; i--) {
				System.out.print(words[i]);
				if (i > 0) {
					System.out.print(" ");
				}
			}
			System.out.println();
			caseNum++;
		}
	}
}
