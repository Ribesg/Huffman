package grtap.huffman.binarytree;

// This class represents the case where we have no children
public class Leaf extends Node {
    private char val;

    public Leaf(final char val) {
        setVal(val);
    }

    @Override
    public int hashCode() {
        return getVal();
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
        if (getVal() != other.getVal()) {
            return false;
        }
        return true;
    }

    // Getters / Setters
    private char getVal() {
        return val;
    }

    private void setVal(final char newVal) {
        val = newVal;
    }
}
