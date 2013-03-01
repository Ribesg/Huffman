package grtap.huffman.binarytree;

import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.util.TreeSet;

// This abstract class represents any Node in the Tree
public abstract class Node {

    protected Type type;

    protected enum Type {
        LEAF, BINARYNODE,
    }

    // Force subclasses to implement equals
    @Override
    public abstract boolean equals(final Object o);

    // Force subclasses to implement hashCode
    @Override
    public abstract int hashCode();

    public boolean isLeaf() {
        return type == Type.LEAF;
    }

    // Following methods are common to all Node
    // To have a clear code without tons of casts
    // /!\ Should never be called without a check to isLeaf() before

    public abstract Node getLeft();

    public abstract Node getRight();

    public abstract char getVal();

    public abstract TreeSet<CharacterCode> getCharacterCodes(final TreeSet<CharacterCode> codes, final BitArray codePrefix);
}
