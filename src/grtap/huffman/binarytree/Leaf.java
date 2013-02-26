package grtap.huffman.binarytree;

// This class represents the case where we have no children
public class Leaf extends Node {
    private final char val;

    public Leaf(final char newVal) {
        val = newVal;
        type = Type.LEAF;
    }

    @Override
    public int hashCode() {
        return val;
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
        final Leaf other = (Leaf) obj;
        if (val != other.val) {
            return false;
        }
        return true;
    }

    // Getters / Setters
    public char getVal() {
        return val;
    }
}
