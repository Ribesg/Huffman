package grtap.huffman.binarytree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreePrinter {

	public static void printTree(final Tree tree) {
		printNode(tree.getRoot());
	}

	private static void printNode(final Node root) {
		final int maxLevel = maxLevel(root);
		System.out.print(printNodeInternal(Collections.singletonList(root), 1, maxLevel));
	}

	private static String printNodeInternal(final List<Node> nodes, final int level, final int maxLevel) {
		if (nodes.isEmpty() || isAllElementsNull(nodes)) {
			return "\n";
		}
		final StringBuilder s = new StringBuilder();
		final int floor = maxLevel - level;
		final int endgeLines = (int) Math.pow(2, Math.max(floor - 1, 0));
		final int firstSpaces = (int) Math.pow(2, floor) - 1;
		final int betweenSpaces = (int) Math.pow(2, floor + 1) - 1;
		s.append(whitespaces(firstSpaces));
		final List<Node> newNodes = new ArrayList<Node>();
		for (final Node node : nodes) {
			if (node != null) {
				if (node.isLeaf()) {
					s.append(node.getVal());
					newNodes.add(null);
					newNodes.add(null);
				} else {
					s.append('|');
					newNodes.add(node.getLeft());
					newNodes.add(node.getRight());
				}
			} else {
				newNodes.add(null);
				newNodes.add(null);
				s.append(' ');
			}
			s.append(whitespaces(betweenSpaces));
		}
		s.append('\n');
		for (int i = 1; i <= endgeLines; i++) {
			for (int j = 0; j < nodes.size(); j++) {
				s.append(whitespaces(firstSpaces - i));
				if (nodes.get(j) == null) {
					s.append(whitespaces(endgeLines + endgeLines + i + 1));
					continue;
				}
				if (!nodes.get(j).isLeaf()) {
					s.append('/');
				} else {
					s.append(whitespaces(1));
				}
				s.append(whitespaces(i + i - 1));
				if (!nodes.get(j).isLeaf()) {
					s.append('\\');
				} else {
					s.append(whitespaces(1));
				}
				s.append(whitespaces(endgeLines + endgeLines - i));
			}
			s.append('\n');
		}
		s.append(printNodeInternal(newNodes, level + 1, maxLevel));
		return s.toString();
	}

	private static String whitespaces(final int count) {
		final StringBuilder s = new StringBuilder(count < 0 ? 0 : count);
		for (int i = 0; i < count; i++) {
			s.append(' ');
		}
		return s.toString();
	}

	private static int maxLevel(final Node node) {
		if (node == null) {
			return 0;
		}
		final int leftMaxLevel = node.isLeaf() ? 0 : maxLevel(node.getLeft());
		final int rightMaxLevel = node.isLeaf() ? 0 : maxLevel(node.getRight());
		return Math.max(leftMaxLevel, rightMaxLevel) + 1;
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
