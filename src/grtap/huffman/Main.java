package grtap.huffman;

import grtap.huffman.util.BitArray;

import java.util.Random;

public class Main {

	public static void main(final String[] args) {
		/*
		 * final Tree tree = HuffmanEncoder.buildTree(Paths.get("test.txt")); final
		 * Map<Character, Integer> map =
		 * HuffmanEncoder.countCharactersInFile(Paths.get("test.txt")); for (final
		 * Entry<Character, BitArray> e : tree.getCharacterCodes().entrySet()) {
		 * printChar(e.getKey()); System.out.print(" : ");
		 * System.out.print(map.get(e.getKey())); System.out.print(" : ");
		 * System.out.println(e.getValue()); }
		 */
		final Random r = new Random();
		final BitArray array = new BitArray();
		for (int i = 0; i < 100; i++) {
			array.add(r.nextInt(2));
			System.out.println(array.length() + ";" +'\t' + array);
		}
	}

	private static void printChar(final char c) {
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
