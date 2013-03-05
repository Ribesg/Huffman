package grtap.huffman.binarytree;

import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.util.TreeSet;

// This class represents the case where we have no children
public class Leaf extends Node {
    private final char val;

    public Leaf(final char newVal) {
        val = newVal;
        type = Type.LEAF;
    }

    @Override
    public TreeSet<CharacterCode> getCharacterCodes(final TreeSet<CharacterCode> codes, final BitArray codePrefix) {
        throw new UnsupportedOperationException();
    }

    public int height() {
        return 0;
    }

    public char getLeftChar() {
        return val;
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
    @Override
    public Node getLeft() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node getRight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getVal() {
        return val;
    }
}
