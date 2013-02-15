package grtap.huffman.binarytree;

public class Leaf extends Node {
	private char val;

	public Leaf(char val) {
		setVal(val);
	}

	@Override
	public int hashCode() {
		return getVal();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Leaf other = (Leaf) obj;
		if (getVal() != other.getVal()) {
			return false;
		}
		return true;
	}

	// Getters / Setters
	private char getVal() {
		return val;
	}

	private void setVal(char newVal) {
		val = newVal;
	}
}
