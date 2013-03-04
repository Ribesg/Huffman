package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.binarytree.TreePrinter;
import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeSet;

public class Encoder {

    public final static Charset    CHARSET = StandardCharsets.ISO_8859_1;

    private Path                   sourceFile;
    private Path                   destinationFile;

    private int[]                  characterCount;
    private Tree                   huffmanTree;
    private TreeSet<CharacterCode> sortedCodes;
    private BitArray[]             codesArray;

    public Encoder() {
        characterCount = new int[256];
        huffmanTree = null;
        sortedCodes = new TreeSet<CharacterCode>();
        codesArray = new BitArray[256];
    }

    // Dest : Tree size + separator + tree + encoded file
    public void encode(final Path from, final Path to, final boolean overwrite) throws IOException {
        if (Files.notExists(from)) {
            return; // TODO Error
        } else if (Files.exists(to) && !overwrite) {
            return; // TODO Error
        }

        sourceFile = from;
        destinationFile = to;

        // Count the number of occurences of each character in the file
        countCharactersInFile();

        // Build the tree from this count
        buildTree();

        // Get the character codes from the tree
        sortedCodes = huffmanTree.getCharacterCodes();

        // Write the Tree to the file
        writeTree();

        // Encode the source file // TODO method
        for (final CharacterCode c : sortedCodes) {
            codesArray[c.getChar()] = c.getCode();
        }

        try (final BufferedReader reader = Files.newBufferedReader(from, CHARSET); final FileOutputStream writer = new FileOutputStream(to.getFileName().toString(), true)) {
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
                writer.write(writeBuffer.pollByteArray());
            } while (length == readBuffer.length);
            // TODO Output the last bits of writeBuffer (< 8 bits)
            // TODO Think of how we will decode this : do we need to know what's the last written bit ?
        }
    }

    private void writeTree() {
        try (final BufferedWriter writer = Files.newBufferedWriter(destinationFile, CHARSET)) {
            int currentLength = 1;
            for (final CharacterCode cur : sortedCodes) {
                while (cur.getCode().length() > currentLength) {
                    writer.write(separateur); // TODO Get first char with 0 occurences
                    currentLength++;
                }
                writer.write(cur.getChar());
            }
        }
    }

    private void buildTree() {
        // Build the per-characters trees
        final TreeSet<Tree> treeSet = new TreeSet<Tree>();
        for (char i = 0; i < 256; i++) {
            if (characterCount[i] > 0) {
                treeSet.add(new Tree(i, characterCount[i]));
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
        huffmanTree = treeSet.pollFirst();
    }

    // Set a int[256] with the number of occurences of each char
    private void countCharactersInFile() {
        try (BufferedReader reader = Files.newBufferedReader(sourceFile, CHARSET)) {
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
    }
}
