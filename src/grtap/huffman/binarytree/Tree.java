package grtap.huffman.binarytree;

import grtap.huffman.util.BitArray;

import java.util.HashMap;
import java.util.Map;

public class Tree implements Comparable<Tree> {
    private final Node root;
    private final int  priority;

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

    public Map<Character, BitArray> getCharacterCodes() {
        final Map<Character, BitArray> map = new HashMap<Character, BitArray>();
        if (root.isLeaf()) {
            final BitArray bits = new BitArray();
            map.put(root.getVal(), bits);
            return map;
        } else {
            return root.getCharacterCodes(map, new BitArray());
        }
    }

    @Override
    public int compareTo(final Tree o) {
        final int res = Integer.compare(priority, o.priority);
        if (res == 0 && root.isLeaf() && o.root.isLeaf()) {
            // Be sure we keep the same order for char with same priorities
            return Character.compare(root.getVal(), o.root.getVal());
        }
        return res > 0 ? 1 : -1; // Do not return 0
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
