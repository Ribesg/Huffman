package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.huffmantree.HuffmanCode;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	public static void main(final String[] args) {
		Tree tree = HuffmanCode.buildTree(Paths.get("test.txt"));
		Map<Character, Integer> map = HuffmanCode.countCharactersInFile(Paths.get("test.txt"));
		for (Entry<Character, String> e : tree.getCharacterCodes().entrySet()) {
			printChar(e.getKey());
			System.out.print(" : ");
			System.out.print(map.get(e.getKey()));
			System.out.print(" : ");
			System.out.println(e.getValue());
		}
	}

	private static void printChar(char c) {
		switch (c) {
		case '\n':
			System.out.print("\\n");
			break;
		case '\t':
			System.out.print("\\t");
			break;
		default:
			System.out.print(c + " ");
			break;
		}
	}
}
