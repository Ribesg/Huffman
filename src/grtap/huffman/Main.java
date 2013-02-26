package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.BitArray;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

    public static void main(final String[] args) {

        final Tree tree = HuffmanEncoder.buildTree(Paths.get("test.txt"));

        tree.print();

        final Map<Character, Integer> map = HuffmanEncoder.countCharactersInFile(Paths.get("test.txt"));

        for (final Entry<Character, BitArray> e : tree.getCharacterCodes().entrySet()) {
            printChar(e.getKey());
            System.out.print(" : ");
            System.out.print(map.get(e.getKey()));
            System.out.print(" : ");
            System.out.println(e.getValue());
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
