package name.etapic.codejam.tictactoetomek;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Port of Python implementation at
 * https://code.google.com/codejam/contest/2270488/dashboard#s=a&a=0
 */
public final class ContentAnalysis implements ProblemSolver {

    private static String getText(final List<String> board) {
        for (char c : Arrays.asList('X', 'O')) {
            final Collection<Character> winChars = Arrays.asList(c, 'T');
            boolean wind1 = true;
            boolean wind2 = true;
            for (int x = 0; x < board.size(); x++) {
                boolean winh = true;
                boolean winv = true;
                for (int y = 0; y < board.size(); y++) {
                    if (!winChars.contains(board.get(y).charAt(x))) {
                        winv = false;
                    }
                    if (!winChars.contains(board.get(x).charAt(y))) {
                        winh = false;
                    }
                }
                if (winh || winv) {
                    return c + " won";
                }
                if (!winChars.contains(board.get(x).charAt(x))) {
                    wind1 = false;
                }
                if (!winChars.contains(board.get(board.size() - 1 - x).charAt(x))) {
                    wind2 = false;
                }
            }
            if (wind1 || wind2) {
                return c + " won";
            }
        }
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                if (board.get(y).charAt(x) == '.') {
                    return "Game has not completed";
                }
            }
        }
        return "Draw";
    }

    @Override
    public Solution solve(final BufferedReader reader) throws Exception {
        final int boardHeight = 4;
        List<String> board = new ArrayList<>(boardHeight);
        for (int i = 0; i < boardHeight; i++) {
            board.add(reader.readLine());
        }
        reader.readLine();
        return new Solution(getText(board), 16);
    }
}
