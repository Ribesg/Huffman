package grtap.huffman;

import static grtap.huffman.Encoder.CHARSET;
import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Decoder {

    private final static int                        BUFFER_SIZE = 2048;

    private int                                     treeLength;
    private final Path                              from;
    private final Path                              to;
    private ArrayList<HashMap<BitArray, Character>> codes;

    public Decoder(final Path from, final Path to) {
        this(from, to, false);
    }

    public Decoder(final Path from, final Path to, final boolean overwrite) {
        if (Files.notExists(from)) {
            throw new IllegalArgumentException("Source file not found");
        } else if (Files.exists(to) && !overwrite) {
            throw new IllegalArgumentException("Destination file already exists");
        }

        this.from = from;
        this.to = to;
    }

    public void decode() throws IOException {

        // first, we get the codes for each character
        getCodes();

        // then we read the file and translate

        try (final FileInputStream reader = new FileInputStream(from.toFile()); final BufferedWriter writer = Files.newBufferedWriter(to, CHARSET)) {

            char[] writeBuffer = new char[BUFFER_SIZE * 8]; // TODO:
            byte[] readBuffer = new byte[BUFFER_SIZE];
            int writeBufferPos = 0;
            Character curChar;
            BitArray curCode = new BitArray();

            reader.skip(treeLength + 1); // position the reader after the tree

            int lengthByte;
            lengthByte = reader.read(readBuffer);
            while (lengthByte == readBuffer.length) {
                for (byte b : readBuffer) { // read each byte in file
                    for (int i = Byte.SIZE - 1; i >= 0; i--) {
                        curCode.add((b & 1 << i) == 0 ? 0 : 1); // add each bit to current code
                        curChar = codes.get(curCode.length() - 1).get(curCode);
                        if (curChar != null) { // if current code is a valid one, i.e. present in our HashMap List
                            writeBuffer[writeBufferPos++] = curChar; // add it to the writeBuffer
                            curCode.clear();
                            if (writeBufferPos == 8192) {
                                writer.write(writeBuffer); // write in destination file when writeBuffer is full
                                writeBufferPos = 0; // reset
                            }
                        }
                    }
                }
                lengthByte = reader.read(readBuffer);
            }
            // treat remaining bytes
            byte b;
            for (int bPos = 0; bPos < lengthByte - 2; bPos++) {
                b = readBuffer[bPos];
                for (int i = Byte.SIZE - 1; i >= 0; i--) {
                    curCode.add((b & 1 << i) == 0 ? 0 : 1);
                    curChar = codes.get(curCode.length() - 1).get(curCode);
                    if (curChar != null) {
                        writeBuffer[writeBufferPos++] = curChar;
                        curCode.clear();
                        if (writeBufferPos == 8192) {
                            writer.write(writeBuffer);
                            writeBufferPos = 0;
                        }
                    }
                }
            }
            byte lastByte = readBuffer[lengthByte - 2];
            byte lastUsedBitInLastByte = readBuffer[lengthByte - 1];
            for (int i = Byte.SIZE - 1; i >= lastUsedBitInLastByte; i--) {
                curCode.add((lastByte & 1 << i) == 0 ? 0 : 1);
                curChar = codes.get(curCode.length() - 1).get(curCode);
                if (curChar != null) {
                    writeBuffer[writeBufferPos++] = curChar;
                    curCode.clear();
                    if (writeBufferPos == 8192) {
                        writer.write(writeBuffer);
                        writeBufferPos = 0;
                    }
                }
            }
            writer.write(writeBuffer, 0, writeBufferPos); // write remaining chars
        }
    }

    private void getCodes() throws IOException {
        // First, we read the tree
        try (final BufferedReader reader = Files.newBufferedReader(from, CHARSET)) {
            treeLength = reader.read(); // first int in file is the length of the tree, including this first int

            char[] treeString = new char[treeLength];

            reader.read(treeString);

            TreeSet<CharacterCode> characterCodes = new Tree(treeString).getCharacterCodes();
            int length = 0;
            int l;
            for (CharacterCode c : characterCodes) { // find maximum code length
                l = c.getCode().length();
                if (l > length) {
                    length = l;
                }
            }

            codes = new ArrayList<HashMap<BitArray, Character>>(length);
            for (int i = 0; i < length; i++) {
                codes.add(new HashMap<BitArray, Character>());
            }

            for (CharacterCode c : characterCodes) {
                codes.get(c.getCode().length() - 1).put(c.getCode(), c.getChar());
            }
        }
    }

}
