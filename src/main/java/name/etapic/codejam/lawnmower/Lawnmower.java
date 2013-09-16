package name.etapic.codejam.lawnmower;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

import java.io.BufferedReader;

public final class Lawnmower implements ProblemSolver {

    private static String getText(final int[][] rows, final int[] rowMaxes, final int[] colMaxes) {
        for (int n = 0; n < rows.length; n++) {
            for (int m = 0; m < rows[n].length; m++) {
                if (rows[n][m] < rowMaxes[n] && rows[n][m] < colMaxes[m]) {
                    return "NO";

                }
            }
        }
        return "YES";
    }

    @Override
    public Solution solve(final BufferedReader reader) throws Exception {
        final String[] NM = reader.readLine().split(" ");
        final int N = Integer.parseInt(NM[0]);
        final int M = Integer.parseInt(NM[1]);
        final int[][] rows = new int[N][M];
        final int[] rowMaxes = new int[N];
        final int[] colMaxes = new int[M];
        for (int n = 0; n < N; n++) {
            final int[] row = new int[M];
            rows[n] = row;
            final String[] desiredHeightStrs = reader.readLine().split(" ");
            if (desiredHeightStrs.length != M) {
                throw new RuntimeException(String.format("row %s length %s != M %s", n, desiredHeightStrs.length, M));
            }
            for (int m = 0; m < M; m++) {
                int desiredHeight = Integer.parseInt(desiredHeightStrs[m]);
                row[m] = desiredHeight;
                rowMaxes[n] = Math.max(desiredHeight, rowMaxes[n]);
                colMaxes[m] = Math.max(desiredHeight, colMaxes[m]);
            }
        }
        return new Solution(getText(rows, rowMaxes, colMaxes), N * M);
    }
}
