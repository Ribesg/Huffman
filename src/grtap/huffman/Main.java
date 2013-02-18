package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.huffmantree.HuffmanCode;

import java.nio.file.Paths;
import java.util.Map.Entry;

public class Main {

	public static void main(final String[] args) {
		Tree tree = HuffmanCode.buildTree(Paths.get("test.txt"));
		for (Entry<Character, String> e : tree.getCharacterCodes().entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}
	}

}
