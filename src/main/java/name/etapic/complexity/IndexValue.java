package name.etapic.complexity;

class IndexValue implements Comparable<IndexValue> {

	final int index;
	final int[] source;

	IndexValue(final int index, final int[] source) {
		this.index = index;
		this.source = source;
	}

	@Override
	public boolean equals(Object arg0) {
		return getValue() == ((IndexValue) arg0).getValue();
	}

	@Override
	public int hashCode() {
		return getValue();
	}

	@Override
	public int compareTo(IndexValue o) {
		// negative integer: less than
		// zero: equal to
		// positive integer: greater than
		// comparison instead of subtraction guards against overflow
		int thisVal = getValue();
		int otherVal = o.getValue();
		return (thisVal < otherVal ? -1 : (thisVal == otherVal ? 0 : 1));
	}

	int getValue() {
		return source[index];
	}

	@Override
	public String toString() {
		return String.format("[%s]=%s", index, getValue());
	}
}
