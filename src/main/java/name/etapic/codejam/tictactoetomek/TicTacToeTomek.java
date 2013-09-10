package name.etapic.codejam.tictactoetomek;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is much more complex than it needs to be (see the ContentAnalysis solution). This checks from starting positions
 * that are impossible to produce winners.
 */
public final class TicTacToeTomek implements ProblemSolver {
    private static final int BOARD_WIDTH = 4;
    private static final int BOARD_HEIGHT = 4;
    private static final int NUM_IN_ROW_TO_WIN = 4;

    private static String getText(final List<List<Symbol>> board) {
        List<WinnerChecker> winnerCheckers = Arrays.asList(new HorizontalChecker(), new VerticalChecker(new Column()),
                new VerticalChecker(new DiagonalIncreasing()),
                new VerticalChecker(new DiagonalDecreasing()));

        // "X won", "O won", "Draw", "Game has not completed"
        String text = "Draw";
        for (int i = 0; i < board.size(); i++) {
            List<Symbol> row = board.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) == Symbol.EMPTY) {
                    text = "Game has not completed";
                    continue;
                }
                for (WinnerChecker winnerChecker : winnerCheckers) {
                    if (winnerChecker.isWinner(i, j, board)) {
                        if (!Arrays.asList(Symbol.O, Symbol.X).contains(winnerChecker.symbol)) {
                            throw new RuntimeException(String.format("Illegal symbol for winner: %s (%s)",
                                    winnerChecker.symbol, winnerChecker));
                        }
                        return winnerChecker.symbol == Symbol.O ? "O won" : "X won";
                    }
                }
            }
        }
        return text;
    }

    @Override
    public Solution solve(final BufferedReader reader) throws Exception {
        List<List<Symbol>> board = new ArrayList<>(BOARD_HEIGHT);
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            List<Symbol> row = new ArrayList<>(BOARD_WIDTH);
            board.add(row);
            for (byte b : reader.readLine().getBytes()) {
                switch (b) {
                    case 'X':
                        row.add(Symbol.X);
                        break;
                    case 'O':
                        row.add(Symbol.O);
                        break;
                    case 'T':
                        row.add(Symbol.T);
                        break;
                    case '.':
                        row.add(Symbol.EMPTY);
                        break;
                    default:
                        throw new RuntimeException("Illegal character: " + (char) b);
                }
            }
        }

        // Each test case is followed by an empty line.
        reader.readLine();

        final String text = getText(board);
        final int N = BOARD_WIDTH * BOARD_HEIGHT;
        return new Solution(text, N);
    }

    private static enum Symbol {
        EMPTY,
        T,
        X,
        O;
    }

    private static interface YComputer {
        int computeY(int i, int j, int x);
    }

    private static abstract class WinnerChecker {
        private Symbol symbol = null;

        abstract boolean isWinner(int i, int j, List<List<Symbol>> board);

        private final boolean doesCurrentSymbolMatch(final List<Symbol> row, final int y) {
            final Symbol currentSymbol = row.get(y) == Symbol.T ? this.symbol : row.get(y);
            if (currentSymbol == Symbol.EMPTY) {
                // it is possible to get here if initial symbol was T
                return false;
            }
            if (this.symbol == Symbol.T) {
                this.symbol = currentSymbol;
            }
            return currentSymbol == this.symbol;
        }
    }

    private static final class HorizontalChecker extends WinnerChecker {
        @Override
        public boolean isWinner(final int i, final int j, final List<List<Symbol>> board) {
            List<Symbol> row = board.get(i);
            super.symbol = row.get(j);
            int count = 1;
            for (int y = j + 1; y < row.size(); y++) {
                if (!super.doesCurrentSymbolMatch(row, y)) {
                    return false;
                }
                count++;
                if (count == NUM_IN_ROW_TO_WIN) {
                    return true;
                }
            }
            // ran off board
            return false;
        }
    }

    private static final class VerticalChecker extends WinnerChecker {

        private final YComputer yComputer;

        private VerticalChecker(final YComputer yComputer) {
            this.yComputer = yComputer;
        }

        @Override
        public boolean isWinner(final int i, final int j, final List<List<Symbol>> board) {
            super.symbol = board.get(i).get(j);
            int count = 1;
            for (int x = i + 1; x < board.size(); x++) {
                final int y = yComputer.computeY(i, j, x);
                final List<Symbol> row = board.get(x);
                if (y < 0 || y == row.size() || !super.doesCurrentSymbolMatch(row, y)) {
                    return false;
                }
                count++;
                if (count == NUM_IN_ROW_TO_WIN) {
                    return true;
                }
            }
            // ran off board
            return false;
        }

        @Override
        public String toString() {
            return yComputer.toString();
        }
    }

    private static final class Column implements YComputer {
        @Override
        public int computeY(final int i, final int j, final int x) {
            return j;
        }
    }

    private static final class DiagonalIncreasing implements YComputer {
        @Override
        public int computeY(final int i, final int j, final int x) {
            return j + x - i;
        }
    }

    private static final class DiagonalDecreasing implements YComputer {
        @Override
        public int computeY(final int i, final int j, final int x) {
            return j - x + i;
        }
    }
}

