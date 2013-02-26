package grtap.huffman;

import grtap.huffman.binarytree.Tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

public class HuffmanEncoder {

    public static void encode(final Path originalFile, final Path encodedFile) {
        encode(originalFile, encodedFile, false);
    }

    public static void encode(final Path originalFile, final Path encodedFile, final boolean overwrite) {
        // Check files
        if (!Files.exists(originalFile)) {
            System.out.println("File not found : " + originalFile);
            return;
        }
        if (!overwrite && Files.exists(encodedFile)) {
            System.out.println("File already exists : " + originalFile);
            return;
        }

        // Tree huffmanTree = buildTree(originalFile);
    }

    // TODO make this private after testing
    public static Tree buildTree(final Path textFile) {
        // Count characters occurences
        final HashMap<Character, Integer> characterMap = countCharactersInFile(textFile);

        // Build the per-characters trees
        final TreeSet<Tree> treeSet = new TreeSet<Tree>();
        for (final Entry<Character, Integer> e : characterMap.entrySet()) {
            treeSet.add(new Tree(e.getKey(), e.getValue()));
        }

        // Merge the trees to one Tree
        Tree left;
        Tree right;
        while (treeSet.size() > 1) {
            left = treeSet.pollFirst();
            right = treeSet.pollFirst();
            treeSet.add(new Tree(left, right));
        }

        // There is only our final tree left
        return treeSet.pollFirst();
    }

    // TODO make this private after testing
    // The call is always made with an existing file
    public static HashMap<Character, Integer> countCharactersInFile(final Path pathToFile) {
        final HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
            char c;
            while (reader.ready()) {
                c = (char) reader.read();
                if (characterMap.containsKey(c)) {
                    characterMap.put(c, characterMap.get(c) + 1);
                } else {
                    characterMap.put(c, 1);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return characterMap;
    }
}
