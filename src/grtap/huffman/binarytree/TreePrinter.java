package grtap.huffman.binarytree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreePrinter {

    public static void printNode(final Node root) {
        final int maxLevel = maxLevel(root);
        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static void printNodeInternal(final List<Node> nodes, final int level, final int maxLevel) {
        if (nodes.isEmpty() || isAllElementsNull(nodes)) {
            return;
        }
        final int floor = maxLevel - level;
        final int endgeLines = (int) Math.pow(2, Math.max(floor - 1, 0));
        final int firstSpaces = (int) Math.pow(2, floor) - 1;
        final int betweenSpaces = (int) Math.pow(2, floor + 1) - 1;
        printWhitespaces(firstSpaces);
        final List<Node> newNodes = new ArrayList<Node>();
        for (final Node node : nodes) {
            if (node != null) {
                if (node.isLeaf()) {
                    System.out.print(((Leaf) node).getVal());
                    newNodes.add(null);
                    newNodes.add(null);
                } else {
                    System.out.print('|');
                    newNodes.add(((BinaryNode) node).getLeft());
                    newNodes.add(((BinaryNode) node).getRight());
                }
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(' ');
            }
            printWhitespaces(betweenSpaces);
        }
        System.out.println();
        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }
                if (!nodes.get(j).isLeaf()) {
                    System.out.print('/');
                } else {
                    printWhitespaces(1);
                }
                printWhitespaces(i + i - 1);
                if (!nodes.get(j).isLeaf()) {
                    System.out.print('\\');
                } else {
                    printWhitespaces(1);
                }
                printWhitespaces(endgeLines + endgeLines - i);
            }
            System.out.println();
        }
        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(final int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(' ');
        }
    }

    private static int maxLevel(final Node node) {
        if (node == null) {
            return 0;
        }
        final int leftLevel = node.isLeaf() ? 0 : maxLevel(((BinaryNode) node).getLeft());
        final int rightLevel = node.isLeaf() ? 0 : maxLevel(((BinaryNode) node).getRight());
        return Math.max(leftLevel, rightLevel) + 1;
    }

    private static <T> boolean isAllElementsNull(final List<T> list) {
        for (final Object object : list) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }
}
