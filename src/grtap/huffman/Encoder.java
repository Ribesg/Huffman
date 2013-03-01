package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.TreeSet;

public abstract class Encoder {

    public final static char    INCR_CODE_LENGTH = 0xB9;
    public final static char    TREE_END         = 0xD7;
    public final static Charset CHARSET          = StandardCharsets.ISO_8859_1;

    public static void encode(final Path from, final Path to, final boolean overwrite) throws IOException {
        if (Files.notExists(from)) {
            return; //TODO Error
        } else if (Files.exists(to) && !overwrite) {
            return; // TODO Error
        }

        final Tree tree = buildTree(from);

        // Get the character codes
        final TreeSet<CharacterCode> codes = tree.getCharacterCodes();
        try (final BufferedWriter writer = Files.newBufferedWriter(to, CHARSET)) {
            // Write the Tree to the file
            int currentLength = 1;
            for (final CharacterCode cur : codes) {
                while (cur.getCode().length() > currentLength) {
                    writer.write(INCR_CODE_LENGTH);
                    currentLength++;
                }
                writer.write(cur.getChar());
            }
            writer.write(TREE_END);
        }

        // Encode the source file
        final BitArray[] codesArray = new BitArray[256];
        for (final CharacterCode c : codes) {
            codesArray[c.getChar()] = c.getCode();
        }
        try (final BufferedReader reader = Files.newBufferedReader(from, CHARSET);
                final BufferedOutputStream writer = new BufferedOutputStream(Files.newOutputStream(to, StandardOpenOption.APPEND))) {
            final char[] readBuffer = new char[8192];
            // Char size = 1 byte
            // CharacterCodes max length : 256 differents chars, max length = log2(256)=8 TODO ?
            // So a buffer with a size equals to the char buffer is enough
            final BitArray writeBuffer = new BitArray(8192);
            int length;
            do {
                length = reader.read(readBuffer);
                for (int i = 0; i < length; i++) {
                    writeBuffer.add(codesArray[readBuffer[i]]);
                }
                final byte[] toBeWritten = writeBuffer.pollByteArray();
                writer.write(toBeWritten);
            } while (length == readBuffer.length);
            // TODO Output the last bits of writeBuffer (< 8 bits)
            // TODO Think of how we will decode this : do we need to know what's the last written bit ?
            writer.flush();
        }
    }

    private static Tree buildTree(final Path textFile) {
        // Count characters occurences
        final int[] counts = countCharactersInFile(textFile);

        // Build the per-characters trees
        final TreeSet<Tree> treeSet = new TreeSet<Tree>();
        for (char i = 0; i < 256; i++) {
            if (counts[i] > 0) {
                treeSet.add(new Tree(i, counts[i]));
            }
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
    private static int[] countCharactersInFile(final Path pathToFile) {
        final int[] characterCount = new int[256]; // Size of ANSI table
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, CHARSET)) {
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
