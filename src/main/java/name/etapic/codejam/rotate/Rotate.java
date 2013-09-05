package name.etapic.codejam.rotate;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import name.etapic.codejam.ProblemSolver;
import name.etapic.codejam.Solution;

public final class Rotate implements ProblemSolver {

	private static final boolean RED = false;
	private static final boolean BLUE = true;

	@Override
	public Solution solve(final BufferedReader reader) throws Exception {
		final String[] NK = reader.readLine().split(" ");
		final int N = Integer.parseInt(NK[0]);
		final int K = Integer.parseInt(NK[1]);
		List<LinkedList<Boolean>> rotated = new ArrayList<>(N);
		for (int i = 0; i < N; i++) {
			final LinkedList<Boolean> row = new LinkedList<>();
			byte[] bytes = reader.readLine().getBytes();

			// TODO: N^2, is there a order-N algorithm?
			for (int j = 0; j < N; j++) {
				switch (bytes[j]) {
				case (byte) '.':
					// do nothing, ignoring the dots rotates and applies gravity
					break;
				case (byte) 'R':
					row.push(RED);
					break;
				case (byte) 'B':
					row.push(BLUE);
					break;
				default:
					throw new RuntimeException("Illegal slot notation (must be '.', 'R', or 'B'): " + (char) bytes[j]);
				}
			}
			if (!row.isEmpty()) {
				rotated.add(row);
			}
		}
		final Set<Boolean> winners = new HashSet<>();
		String text = "Neither";
		for (int i = 0; i < rotated.size(); i++) {
			final LinkedList<Boolean> row = rotated.get(i);
			for (int j = 0; j < row.size(); j++) {
				boolean color = row.get(j);
				if (winners.contains(color)) {
					continue;
				}
				if (isHorizontalWinner(K, color, j, row) || isVerticalWinner(K, color, i, j, rotated)
						|| isDiagonalIncreasingWinner(K, color, i, j, rotated)
						|| isDiagonalDecreasingWinner(K, color, i, j, rotated)) {
					winners.add(color);
					if (winners.size() == 2) {
						text = "Both";
						break;
					}
					text = color == BLUE ? "Blue" : "Red";
					continue;
				}
			}
			if (winners.size() == 2) {
				break;
			}
		}
		return new Solution(text, N);
	}

	private static boolean isHorizontalWinner(final int K, final boolean color, final int j,
			final LinkedList<Boolean> row) {
		int count = 1;
		for (int y = j + 1; y < row.size(); y++) {
			if (row.get(y) == color) {
				count++;
				if (count < K) {
					continue;
				}
			}
			break;
		}
		return count == K;
	}

	private static boolean isVerticalWinner(final int K, final boolean color, final int i, final int j,
			final List<LinkedList<Boolean>> rotated) {
		int count = 1;
		for (int x = i + 1; x < rotated.size(); x++) {
			if (rotated.get(x).get(j) == color) {
				count++;
				if (count < K) {
					continue;
				}
			}
			break;
		}
		return count == K;
	}

	private static boolean isDiagonalIncreasingWinner(final int K, final boolean color, final int i, final int j,
			final List<LinkedList<Boolean>> rotated) {
		int count = 1;
		for (int x = i + 1; x < rotated.size(); x++) {
			final int y = j + x - i;
			final LinkedList<Boolean> row = rotated.get(x);
			if (y < row.size() && row.get(y) == color) {
				count++;
				if (count < K) {
					continue;
				}
			}
			break;
		}
		return count == K;
	}

	private static boolean isDiagonalDecreasingWinner(final int K, final boolean color, final int i, final int j,
			final List<LinkedList<Boolean>> rotated) {
		int count = 1;
		for (int x = i + 1; x < rotated.size(); x++) {
			final int y = j - x + i;
			if (y >= 0 && rotated.get(x).get(y) == color) {
				count++;
				if (count < K) {
					continue;
				}
			}
			break;
		}
		return count == K;
	}

}
