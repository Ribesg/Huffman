package grtap.huffman;

import grtap.huffman.binarytree.Tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeSet;

public abstract class Encoder {

    public static void encode(final Path from, final Path to, final boolean overwrite) throws IOException {
        if (Files.notExists(from)) {
            return; //TODO Error
        } else if (Files.exists(to) && !overwrite) {
            return; // TODO Error
        }

        //final Tree tree = buildTree(from);
    }

    private static Tree buildTree(final Path textFile) {
        // Count characters occurences
        final int[] counts = countCharactersInFile(textFile);

        // Build the per-characters trees
        final TreeSet<Tree> treeSet = new TreeSet<Tree>();
        for (char i = 0; i < 256; i++) {
            treeSet.add(new Tree(i, counts[i]));
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

    // Returns a int[256] with the number of occurences of each char
    public static int[] countCharactersInFile(final Path pathToFile) {
        final int[] characterCount = new int[256]; // Size of ANSI table
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
            final char[] buffer = new char[8192];
            int nb = -1;
            while (reader.ready()) {
                nb = reader.read(buffer);
                for (int i = 0; i < nb; i++) {
                    characterCount[buffer[i]]++;
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return characterCount;
    }
}
