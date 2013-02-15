package grtap.huffman.binarytree;

public class BinaryNode extends Node {
	private Node left, right;

	public BinaryNode(Node leftChild, Node rightChild) {
		setLeft(leftChild);
		setRight(rightChild);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getLeft() == null) ? 0 : getLeft().hashCode());
		result = prime * result + ((getRight() == null) ? 0 : getRight().hashCode());
		return result;
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
		BinaryNode other = (BinaryNode) obj;
		if (getLeft() == null) {
			if (other.getLeft() != null) {
				return false;
			}
		} else if (!getLeft().equals(other.getLeft())) {
			return false;
		}
		if (getRight() == null) {
			if (other.getRight() != null) {
				return false;
			}
		} else if (!getRight().equals(other.getRight())) {
			return false;
		}
		return true;
	}

	// Getters / Setters
	private Node getLeft() {
		return left;
	}

	private void setLeft(Node leftChild) {
		left = leftChild;
	}

	private Node getRight() {
		return right;
	}

	private void setRight(Node rightChild) {
		right = rightChild;
	}
}
