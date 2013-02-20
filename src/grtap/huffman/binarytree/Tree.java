package grtap.huffman.binarytree;

import grtap.huffman.util.BitArray;

import java.util.HashMap;
import java.util.Map;

public class Tree implements Comparable<Tree> {
	private Node	root;
	private int		priority;

	// Build a Tree with one value
	public Tree(final char val, final int priority) {
		setRoot(new Leaf(val));
		setPriority(priority);
	}

	// Build a Tree from 2 Trees
	public Tree(final Tree left, final Tree right) {
		setRoot(new BinaryNode(left.getRoot(), right.getRoot()));
		setPriority(left.getPriority() + right.getPriority());
	}

	public Map<Character, BitArray> getCharacterCodes() {
		final Map<Character, BitArray> map = new HashMap<Character, BitArray>();
		if (root.isLeaf()) {
			final BitArray bits = new BitArray();
			map.put(((Leaf) root).getVal(), bits);
			return map;
		} else {
			return ((BinaryNode) root).getCharacterCodes(map, new BitArray());
		}
	}

	@Override
	public int compareTo(final Tree o) {
		final int res = Integer.compare(getPriority(), o.getPriority());
		return res > 0 ? 1 : -1;
	}

	@Override
	public int hashCode() {
		return getRoot().hashCode();
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
		final Tree other = (Tree) obj;
		if (root == null) {
			if (other.root != null) {
				return false;
			}
		} else if (!root.equals(other.root)) {
			return false;
		}
		return true;
	}

	// Getters / Setters
	private Node getRoot() {
		return root;
	}

	private void setRoot(final Node newRoot) {
		root = newRoot;
	}

	private int getPriority() {
		return priority;
	}

	private void setPriority(final int newPriority) {
		priority = newPriority;
	}
}
