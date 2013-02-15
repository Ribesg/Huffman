package grtap.huffman.binarytree;

public class Tree implements Comparable<Tree> {
	private Node root;
	private int priority;

	public Tree(char val) {
		setRoot(new Leaf(val));
		setPriority(1);
	}

	public Tree(Tree left, Tree right) {
		setRoot(new BinaryNode(left.getRoot(), right.getRoot()));
		setPriority(left.getPriority() + right.getPriority());
	}

	@Override
	public int compareTo(Tree o) {
		return Integer.compare(getPriority(), o.getPriority());
	}

	@Override
	public int hashCode() {
		return getRoot().hashCode();
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
		Tree other = (Tree) obj;
		if (getRoot() == null) {
			if (other.getRoot()!= null) {
				return false;
			}
		} else if (!getRoot().equals(other.getRoot())) {
			return false;
		}
		return true;
	}

	// Getters / Setters
	private Node getRoot() {
		return root;
	}

	private void setRoot(Node newRoot) {
		root = newRoot;
	}

	private int getPriority() {
		return priority;
	}

	private void setPriority(int newPriority) {
		priority = newPriority;
	}
}
