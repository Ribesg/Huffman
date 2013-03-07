package grtap.huffman.binarytree;

import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.util.TreeSet;

// This abstract class represents any Node in the Tree
public abstract class Node {

	// Used when the Decoder rebuild the Tree
	// isFull == True <=> We can't add any character below this node
	protected boolean	isFull	= false;

	// Force subclasses to implement equals
	@Override
	public abstract boolean equals(final Object o);

	// Force subclasses to implement hashCode
	@Override
	public abstract int hashCode();

	public abstract boolean isLeaf();

	public abstract int height();

	// Following methods are common to all Node
	// To have a clear code without tons of casts
	// /!\ Should never be called without a check to isLeaf() before

	public abstract Node getLeft();

	public abstract Node getRight();

	public abstract char getVal();

	public abstract TreeSet<CharacterCode> getCharacterCodes(final TreeSet<CharacterCode> codes, final BitArray codePrefix);

	public abstract boolean insert(final char character, final int level);
}
