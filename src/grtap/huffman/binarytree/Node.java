package grtap.huffman.binarytree;

public abstract class Node {
	// Force subclasses to implement equals
	@Override
	public abstract boolean equals(Object o);

	// Force subclasses to implement hashCode
	@Override
	public abstract int hashCode();
}
