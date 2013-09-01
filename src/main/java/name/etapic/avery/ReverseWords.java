package name.etapic.avery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * "sample.in"  -->  input stream  -->  buffered reader 
 *     -->  read first line --> interpret it as a number
 *     NOW   --> use that number as the number of test cases
 */
public class ReverseWords {
	public static void main(String[] args) throws Exception {
		InputStream in = ReverseWords.class.getResourceAsStream("sample.in");
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		int caseCount = Integer.parseInt(r.readLine());

	}
}
