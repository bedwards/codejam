package name.etapic.codejam.fairandsquare;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Arriving at this solution required peaking at the content analysis.
 * https://code.google.com/codejam/contest/2270488/dashboard#s=a&a=2
 * <p/>
 * Precomputation is ported from netkuba's C++ solution and refactored.
 * https://code.google.com/codejam/contest/2270488/scoreboard#vf=1
 * <p/>
 * BigInteger square root function is from
 * <p/>
 * http://stackoverflow.com/questions/11425070/algorithm-to-list-all-unique-permutations-of-numbers-contain-duplicates
 */
public final class FairAndSquare implements ProblemSolver {

    // From the content analysis (https://code.google.com/codejam/contest/2270488/dashboard#s=a&a=2)
    // Let X be Fair and Square, and let its square root be Y.
    private static final List<BigInteger> Xs = precomputeFairAndSquares(100);

    /**
     * Ported from netkuba's C++ solution and refactored.
     * https://code.google.com/codejam/contest/2270488/scoreboard#vf=1
     */
    private static List<BigInteger> precomputeFairAndSquares(final int exponent) {

        // From the content analysis (https://code.google.com/codejam/contest/2270488/dashboard#s=a&a=2)
        // Let X be Fair and Square, and let its square root be Y.
        final List<List<Byte>> Ys = new ArrayList<>();

        Ys.add(Arrays.asList((byte) 1));
        Ys.add(Arrays.asList((byte) 2));
        Ys.add(Arrays.asList((byte) 3));
        final int maxLength = sqrt(BigInteger.TEN.pow(exponent)).toString().length();

        // From the content analysis (https://code.google.com/codejam/contest/2270488/dashboard#s=a&a=2)
        // Thus, the Fair and Square numbers are exactly the palindromes with no carries inside. In particular,
        // the middle digit of X is the sum of squares of all the digits of Y, so this sum has to be no larger than
        // nine. We conclude that only 0, 1, 2, and 3 can appear in Y.
        final int maxSumOfSquares = 9;

        for (int length = 2; length <= maxLength; length++) {
            List<Byte> r = new ArrayList<>(Collections.nCopies((length + 1) / 2, (byte) 0));
            final List<List<Byte>> results = generateYsOfLength(0, length, r, maxSumOfSquares);
            for (List<Byte> result : results) {
                Ys.add(new ArrayList<>(result));
                if ((length & 1) == 0) {
                    getLast(Ys).add(getLast(result));
                }
                for (int i = result.size() - 2; i >= 0; i--) {
                    getLast(Ys).add(result.get(i));
                }
            }
        }
        final List<BigInteger> fairAndSquares = new ArrayList<>(Ys.size());
        for (List<Byte> bytes : Ys) {
            final StringBuilder val = new StringBuilder();
            for (byte b : bytes) {
                val.append(b);
            }
            fairAndSquares.add(new BigInteger(val.toString()).pow(2));
        }
        return fairAndSquares;
    }

    private static boolean isMiddleDigit(final int p, final int length) {
        return p * 2 + 1 == length;
    }

    /**
     * Ported from netkuba's C++ solution and refactored.
     * https://code.google.com/codejam/contest/2270488/scoreboard#vf=1
     */
    private static List<List<Byte>> generateYsOfLength(final int position, final int length, final List<Byte> result,
                                                       final int remainingSumOfSquares) {

        // values at position 0 can be 1 or 2. values at other positions can be 0, 1 or 2.
        byte i = (byte) (!isMiddleDigit(position, length) && position == 0 ? 1 : 0);

        final List<List<Byte>> results = new ArrayList<>();
        while (true) {

            // middle digit only shows up once, other digits show up twice, so contribution to sum of squares is
            // adjusted
            final int newRemainingSumOfSquares = remainingSumOfSquares - (isMiddleDigit(position,
                    length) ? i * i : i * i * 2);

            if (newRemainingSumOfSquares < 0) {
                break;
            }
            result.set(position, i);
            if ((position + 1) * 2 + 1 <= length) {
                results.addAll(generateYsOfLength(position + 1, length, result, newRemainingSumOfSquares));
            } else {
                results.add(new ArrayList<>(result));
            }
            i++;
        }
        return results;
    }

    private static <T> T getLast(final List<T> xs) {
        return xs.get(xs.size() - 1);
    }

    /**
     * http://stackoverflow.com/questions/11425070/algorithm-to-list-all-unique-permutations-of-numbers-contain
     * -duplicates
     */
    private static BigInteger sqrt(final BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while (b.compareTo(a) >= 0) {
            final BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if (mid.multiply(mid).compareTo(n) > 0) {
                b = mid.subtract(BigInteger.ONE);
            } else {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a.subtract(BigInteger.ONE);
    }

    @Override
    public Solution solve(final BufferedReader reader) throws Exception {
        final String[] AB = reader.readLine().split(" ");
        final BigInteger A = new BigInteger(AB[0]);
        final BigInteger B = new BigInteger(AB[1]);
        long count = 0;
        for (BigInteger X : Xs) {
            if (X.compareTo(A) > -1 && X.compareTo(B) < 1) {
                count++;
            }
        }
        // TODO: change framework to accept long for N instead of int
        return new Solution(Long.toString(count), B.subtract(A).intValue() + 1);
    }
}
