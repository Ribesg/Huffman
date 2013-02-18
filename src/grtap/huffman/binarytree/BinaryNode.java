package grtap.huffman.binarytree;

import java.util.Map;

// This class represents a Node which have 2 children
// It may represents the case where we have only one child, as this is rare enough
public class BinaryNode extends Node {
	private Node left, right;

	public BinaryNode(final Node leftChild, final Node rightChild) {
		left = leftChild;
		right = rightChild;
		type = Type.BINARYNODE;
	}

	public Map<Character, String> getCharacterCodes(Map<Character, String> map, String codePrefix) {
		if (left.isLeaf()) {
			map.put(((Leaf) left).getVal(), codePrefix + '0');
		} else {
			((BinaryNode) left).getCharacterCodes(map, codePrefix + '0');
		}
		if (right.isLeaf()) {
			map.put(((Leaf) right).getVal(), codePrefix + '1');
		} else {
			((BinaryNode) right).getCharacterCodes(map, codePrefix + '1');
		}
		return map;
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
}
