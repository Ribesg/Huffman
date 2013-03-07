package grtap.huffman.binarytree;

import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.util.TreeSet;

// This class represents a Node which have 2 children
public class BinaryNode extends Node {
	private Node	left, right;

	public BinaryNode(final Node leftChild, final Node rightChild) {
		left = leftChild;
		right = rightChild;
		type = Type.BINARYNODE;
	}

	@Override
	public TreeSet<CharacterCode> getCharacterCodes(TreeSet<CharacterCode> codes, final BitArray codePrefix) {
		// First, clone the prefix before appending the 0 for the left operand
		if (left.isLeaf()) {
			codes.add(new CharacterCode(left.getVal(), codePrefix.cloneThenAdd(0)));
		} else {
			codes = left.getCharacterCodes(codes, codePrefix.cloneThenAdd(0));
		}
		// Then, use the same BitArray object with and additional 1 for the right operand
		if (right.isLeaf()) {
			codes.add(new CharacterCode(right.getVal(), codePrefix.add(1)));
		} else {
			codes = right.getCharacterCodes(codes, codePrefix.add(1));
		}
		return codes;
	}

	@Override
	public int height() {
		return Math.max(left.height(), right.height()) + 1;
	}

	@Override
	public char getLeftChar() {
		return left.getLeftChar();
	}

	@Override
	public boolean insert(final char character, final int level) {
		if (level == 1) { // Char should be one of sons
			// We checked isFull at the previous level so one of them is null
			if (left == null) {
				left = new Leaf(character);
			} else { // right == null
				right = new Leaf(character);
				isFull = true;
			}
			return isFull;
		} else {
			if (left == null) {
				left = new BinaryNode(null, null);
				left.insert(character, level - 1);
				return false;
			} else if (!left.isFull) {
				left.insert(character, level - 1);
				return false;
			} else if (right == null) {
				right = new BinaryNode(null, null);
				right.insert(character, level - 1);
				return false;
			} else if (!right.isFull) {
				isFull = right.insert(character, level - 1);
				return isFull;
			} else {
				throw new IllegalArgumentException("Malformed file");
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (left == null ? 0 : left.hashCode());
		result = prime * result + (right == null ? 0 : right.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BinaryNode other = (BinaryNode) obj;
		if (left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!left.equals(other.left)) {
			return false;
		}
		if (right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!right.equals(other.right)) {
			return false;
		}
		return true;
	}

	// Getters / Setters
	@Override
	public Node getLeft() {
		return left;
	}

	@Override
	public Node getRight() {
		return right;
	}

	@Override
	public char getVal() {
		throw new UnsupportedOperationException();
	}
}
