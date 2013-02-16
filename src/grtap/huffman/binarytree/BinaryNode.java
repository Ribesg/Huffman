package grtap.huffman.binarytree;

// This class represents a Node which have 2 children
// It may represents the case where we have only one child, as this is rare enough
public class BinaryNode extends Node {
    private Node left, right;

    public BinaryNode(final Node leftChild, final Node rightChild) {
        setLeft(leftChild);
        setRight(rightChild);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getLeft() == null ? 0 : getLeft().hashCode());
        result = prime * result + (getRight() == null ? 0 : getRight().hashCode());
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

    private void setLeft(final Node leftChild) {
        left = leftChild;
    }

    private Node getRight() {
        return right;
    }

    private void setRight(final Node rightChild) {
        right = rightChild;
    }
}
