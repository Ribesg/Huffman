package grtap.huffman;

import grtap.huffman.binarytree.Tree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeSet;

public abstract class Encoder {

    private static final long    MAX_FILE_SIZE_IN_MEMORY = 33_554_432;         // 32 Mio
    private static final Encoder stringEncoder           = new StringEncoder();
    private static final Encoder streamEncoder           = new StreamEncoder();

    public static void encode(final Path from, final Path to, final boolean overwrite) throws IOException {

        if (Files.notExists(from)) {
            return; //TODO Error
        } else if (Files.exists(to) && !overwrite) {
            return; // TODO Error
        }

        if (Files.size(from) > MAX_FILE_SIZE_IN_MEMORY) {
            streamEncoder.encodeFile(from, to);
        } else {
            stringEncoder.encodeFile(from, to);
        }
    }

    // Main method, encode the file
    protected abstract void encodeFile(final Path from, final Path to);

    // Returns a int[256] with the number of occurences of each char
    public abstract int[] countCharactersInFile(final Path textFile);

    // TODO make this private after testing
    protected Tree buildTree(final Path textFile) {
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
}
