package grtap.huffman.binarytree;

import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

// Specific Huffman Tree implementation
// Internal nodes have no value but 2 sons
// Leaves have a value and no sons
public class Tree implements Comparable<Tree> {
	protected Node		root;
	protected final int	priority;	// Used in the Huffman Tree building from 1-char-trees

	// Build a Tree with one value
	public Tree(final char val, final int prior) {
		root = new Leaf(val);
		priority = prior;
	}

	// Build a Tree from 2 Trees
	public Tree(final Tree left, final Tree right) {
		root = new BinaryNode(left.root, right.root);
		priority = left.priority + right.priority;
	}

	// Build a Tree from its String representation
	// => stringRepresentation.length() % 2 == 0
	public Tree(final char[] stringRepresentation) {
		priority = 0; // No use in this Tree
		// Transform the String to a usable data structure
		// Character ; Level
		final LinkedHashMap<Character, Integer> map = new LinkedHashMap<Character, Integer>();
		for (int i = 0; i < stringRepresentation.length; i += 2) {
			map.put(stringRepresentation[i], (int) stringRepresentation[i + 1]);
			if((int)stringRepresentation[i+1]==0){
				throw new IllegalArgumentException("Malformed File");
			}
		}
		// Insert each character into the Tree
		root = new BinaryNode(null, null);
		for (final Entry<Character, Integer> e : map.entrySet()) {
			root.insert(e.getKey(), e.getValue());
		}
	}

	private Tree() {
		root = null;
		priority = 0;
	}

	// Very special case : The file contains only one char value
	// Ex : aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
	public static Tree newMonoCharacterHuffmanTree(final char c) {
		final Tree res = new Tree();
		// Here the second char will never be used
		res.root = new BinaryNode(new Leaf(c), new Leaf(' '));
		return res;
	}

	// Returns the character codes for each character in Tree
	public TreeSet<CharacterCode> getCharacterCodes() {
		final TreeSet<CharacterCode> codes = new TreeSet<CharacterCode>();
		if (root.isLeaf()) {
			final BitArray bits = new BitArray();
			codes.add(new CharacterCode(root.getVal(), bits));
			return codes;
		} else {
			return root.getCharacterCodes(codes, new BitArray());
		}
	}

	@Override
	public int compareTo(final Tree o) {
		int res = Integer.compare(priority, o.priority);
		if (res == 0) {
			// Be sure we keep the same order for char with same priorities
			if (root.isLeaf() && o.root.isLeaf()) {
				return Character.compare(root.getVal(), o.root.getVal());
			} else {
				res = Integer.compare(root.height(), o.root.height());
				return res == 0 ? 1 : res; // Never return 0
			}
		}
		return res;
	}

	@Override
	public String toString() {
		if (root.isLeaf()) {
			return Character.toString(root.getVal());
		} else {
			return "?";
		}
	}

	@Override
	public int hashCode() {
		return root.hashCode();
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
	public Node getRoot() {
		return root;
	}
}
