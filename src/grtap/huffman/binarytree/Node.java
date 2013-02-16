package grtap.huffman.binarytree;

// This abstract class represents any Node in the Tree
public abstract class Node {
	// Force subclasses to implement equals
	@Override
	public abstract boolean equals(final Object o);

	// Force subclasses to implement hashCode
	@Override
	public abstract int hashCode();
}
