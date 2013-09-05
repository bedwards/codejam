package name.etapic.codejam.allyourbase;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * https://code.google.com/codejam/contest/189252/dashboard#s=p0
 * <p/>
 * only characters in the 'a' to 'z' and '0' to '9' ranges (with no spaces and no punctuation)
 * <p/>
 * the number is positive
 * never starts with a zero
 * they aren't using unary (base 1).
 * <p/>
 * minimum possible number of seconds before war begins
 * <p/>
 * example, if they wrote "ab2ac999",
 * they could have meant "31536000" in base 10 -- exactly one year --
 * or they could have meant "12314555" in base 6 -- 398951 seconds, or about four and a half days
 */
public final class AllYourBase implements ProblemSolver {
    @Override
    public Solution solve(final BufferedReader reader) throws Exception {
        final byte[] message = reader.readLine().getBytes();
        final Map<Byte, String> map = new HashMap<Byte, String>();
        for (byte b : message) {
            map.put(b, null);
        }
        final int radix = map.size() > 1 ? map.size() : 2;
        final StringBuilder builder = new StringBuilder();
        int x = 0;
        for (byte b : message) {
            if (map.get(b) == null) {
                int value = x;
                if (x < 2) {
                    // can't start with zero
                    value = x == 0 ? 1 : 0;
                }
                map.put(b, Integer.toString(value, radix));
                x++;
            }
            builder.append(map.get(b));
        }
        long answer = Long.parseLong(builder.toString(), radix);
        return new Solution(Long.toString(answer), message.length);
    }
}
