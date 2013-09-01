package name.etapic.codejam;

public final class Solution {
	private final String text;
	private final int problemSize;

	public Solution(final String text, final int problemSize) {
		this.text = text;
		this.problemSize = problemSize;
	}

	public String getText() {
		return text;
	}

	public int getProblemSize() {
		return problemSize;
	}
}
