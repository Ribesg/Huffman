package grtap.huffman.binarytree;

import grtap.huffman.util.BitArray;

import java.util.Map;

// This class represents a Node which have 2 children
public class BinaryNode extends Node {
    private final Node left, right;

    public BinaryNode(final Node leftChild, final Node rightChild) {
        left = leftChild;
        right = rightChild;
        type = Type.BINARYNODE;
    }

    @Override
    public Map<Character, BitArray> getCharacterCodes(Map<Character, BitArray> map, final BitArray codePrefix) {
        // First, clone the prefix before appending the 0 for the left operand
        if (left.isLeaf()) {
            map.put(left.getVal(), codePrefix.cloneThenAdd(0));
        } else {
            map = left.getCharacterCodes(map, codePrefix.cloneThenAdd(0));
        }
        // Then, use the same BitArray object with and additional 1 for the right operand
        if (right.isLeaf()) {
            map.put(right.getVal(), codePrefix.add(1));
        } else {
            map = right.getCharacterCodes(map, codePrefix.add(1));
        }
        return map;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (left == null ? 0 : left.hashCode());
        result = prime * result + (right == null ? 0 : right.hashCode());
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
        if (left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!left.equals(other.left)) {
            return false;
        }
        if (right == null) {
            if (other.right != null) {
                return false;
            }
        } else if (!right.equals(other.right)) {
            return false;
        }
        return true;
    }

    // Getters / Setters
    @Override
    public Node getLeft() {
        return left;
    }

    @Override
    public Node getRight() {
        return right;
    }

    @Override
    public char getVal() {
        throw new UnsupportedOperationException();
    }
}
