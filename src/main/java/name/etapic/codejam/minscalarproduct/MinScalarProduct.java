package name.etapic.codejam.minscalarproduct;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * https://code.google.com/codejam/contest/32016/dashboard#s=p0
 */
public final class MinScalarProduct implements ProblemSolver {

    private static BigInteger findMinScalarProduct(List<Integer> v1, List<Integer> v2) {
        assert v1.size() == v2.size();
        List<Integer> sorted1 = new ArrayList<Integer>(v1);
        List<Integer> sorted2 = new ArrayList<Integer>(v2);
        Collections.sort(sorted1);
        Collections.sort(sorted2);
        BigInteger result = BigInteger.ZERO;
        BigInteger x, y;
        for (int i = 0; i < v1.size(); i++) {
            x = BigInteger.valueOf(sorted1.get(i));
            y = BigInteger.valueOf(sorted2.get(v1.size() - i - 1));
            result = result.add(x.multiply(y));
        }
        return result;
    }

    @Override
    public Solution solve(final BufferedReader reader) throws Exception {
        final int N = Integer.parseInt(reader.readLine());
        List<List<Integer>> vectors = new ArrayList<List<Integer>>(2);
        for (int i = 0; i < 2; i++) {
            String[] ns = reader.readLine().split(" ");
            vectors.add(new ArrayList<Integer>(N));
            for (String n : ns) {
                vectors.get(i).add(Integer.parseInt(n));
            }
        }
        BigInteger result = findMinScalarProduct(vectors.get(0), vectors.get(1));
        return new Solution(result.toString(), N);
    }
}
