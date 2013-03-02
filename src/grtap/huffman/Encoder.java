package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeSet;

public abstract class Encoder {

    public final static char    INCR_CODE_LENGTH = 0xB9;
    public final static char    TREE_END         = 0xD7;
    public final static Charset CHARSET          = StandardCharsets.ISO_8859_1;

    public static void encode(final Path from, final Path to, final boolean overwrite) throws IOException {
        //        final Timer checkTimer = new Timer();
        //        final Timer treeTimer = new Timer();
        //        final Timer codesTimer = new Timer();
        //        final Timer treeWritingTimer = new Timer();
        //        final Timer codesArrayTimer = new Timer();
        //        final Timer encodingTimer = new Timer();
        if (Files.notExists(from)) {
            return; //TODO Error
        } else if (Files.exists(to) && !overwrite) {
            return; // TODO Error
        }

        //        treeTimer.start();
        final Tree tree = buildTree(from);
        //        treeTimer.stop();

        // Get the character codes
        //        codesTimer.start();
        final TreeSet<CharacterCode> codes = tree.getCharacterCodes();
        //        codesTimer.stop();

        // Write the Tree to the file
        //        treeWritingTimer.start();
        try (final BufferedWriter writer = Files.newBufferedWriter(to, CHARSET)) {
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
        //        treeWritingTimer.stop();

        // Encode the source file
        //        codesArrayTimer.start();
        final BitArray[] codesArray = new BitArray[256];
        for (final CharacterCode c : codes) {
            codesArray[c.getChar()] = c.getCode();
        }
        //        codesArrayTimer.stop();

        //        encodingTimer.start();
        try (final BufferedReader reader = Files.newBufferedReader(from, CHARSET);
                final FileChannel writer = new FileOutputStream(to.getFileName().toString(), false).getChannel()) {
            final char[] readBuffer = new char[8192];
            // Char size = 1 byte
            // CharacterCodes max length : 256 differents chars, max length = log2(256)=8 TODO ?
            // So a buffer with a size equals to the char buffer is enough
            final BitArray writeBuffer = new BitArray(8192);
            final ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
            int length;
            do {
                length = reader.read(readBuffer);
                for (int i = 0; i < length; i++) {
                    writeBuffer.add(codesArray[readBuffer[i]]);
                }
                byteBuffer.put(writeBuffer.pollByteArray());
                writer.write(byteBuffer);
                byteBuffer.rewind();
            } while (length == readBuffer.length);
            // TODO Output the last bits of writeBuffer (< 8 bits)
            // TODO Think of how we will decode this : do we need to know what's the last written bit ?
        }
        //        encodingTimer.stop();

        //        System.out.println("Files check time : " + checkTimer.diffString());
        //        System.out.println("Tree building time : " + treeTimer.diffString());
        //        System.out.println("Codes get time : " + codesTimer.diffString());
        //        System.out.println("Tree writing time : " + treeWritingTimer.diffString());
        //        System.out.println("Codes to array time : " + codesArrayTimer.diffString());
        //        System.out.println("Encoding time : " + encodingTimer.diffString());
        //        System.out.println();
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
