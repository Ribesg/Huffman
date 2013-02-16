package grtap.huffman.huffmantree;

import grtap.huffman.binarytree.Tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

public class TreeBuilder {

	public static Tree build(final Path path) {
		// Check file
		if (!Files.exists(path)) {
			return null;
		}

		// Count characters occurences
		final HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();
		try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
			char c = (char) reader.read();
			while (c != -1) {
				if (characterMap.containsKey(c)) {
					characterMap.put(c, characterMap.get(c) + 1);
				} else {
					characterMap.put(c, 1);
				}
				c = (char) reader.read();
			}
		} catch (final IOException e) {
			System.out.println(e);
			System.exit(-1);
		}

		// Build the per-characters trees
		final TreeSet<Tree> treeSet = new TreeSet<Tree>();
		for (final Entry<Character, Integer> e : characterMap.entrySet()) {
			treeSet.add(new Tree(e.getKey(), e.getValue()));
		}

		// Merge the trees to one Tree
		while (treeSet.size() > 1) {
			final Tree left = treeSet.pollFirst();
			final Tree right = treeSet.pollFirst();
			treeSet.add(new Tree(left, right));
		}

		// There is only our final tree left
		return treeSet.pollFirst();
	}
}
