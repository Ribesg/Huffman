package grtap.huffman;

import static grtap.huffman.EncoderImpl.CHARSET;
import grtap.huffman.api.Decoder;
import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;
import grtap.huffman.util.Timer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class DecoderImpl implements Decoder {

	private final static int						BYTE_BUFFER_SIZE	= 2048;
	private final static int						CHAR_BUFFER_SIZE	= BYTE_BUFFER_SIZE * 8;

	private int										treeLength;
	private final Path								sourceFile;
	private final Path								destinationFile;
	private ArrayList<HashMap<BitArray, Character>>	codes;
	private Timer									localTimer, totalTimer;

	public DecoderImpl(final Path from, final Path to) {
		this(from, to, false);
	}

	public DecoderImpl(final Path from, final Path to, final boolean overwrite) {
		if (Files.notExists(from)) {
			throw new IllegalArgumentException("Source file not found");
		} else if (Files.exists(to) && !overwrite) {
			throw new IllegalArgumentException("Destination file already exists");
		}

		sourceFile = from;
		destinationFile = to;
	}

	@Override
	public void decode() throws IOException {
		System.out.println("Starting decompression of file \"" + sourceFile.getFileName() + "\" destinationFile \"" + destinationFile.getFileName()
				+ "\"");
		totalTimer = new Timer().start();

		// first, we get the codes for each character
		getCodes();

		// then we read the file and translate
		printMessage("Decompressing " + sourceFile.getFileName() + "... ");
		try (final FileInputStream reader = new FileInputStream(sourceFile.toFile());
				final BufferedWriter writer = Files.newBufferedWriter(destinationFile, CHARSET)) {

			final char[] writeBuffer = new char[CHAR_BUFFER_SIZE]; // TODO:
			final byte[] readBuffer = new byte[BYTE_BUFFER_SIZE];
			int writeBufferPos = 0;
			Character curChar;
			final BitArray curCode = new BitArray();

			reader.skip(treeLength + 1); // position the reader after the tree

			int lengthByte;
			lengthByte = reader.read(readBuffer);
			while (lengthByte == readBuffer.length) {
				for (final byte b : readBuffer) { // read each byte in file
					for (int i = Byte.SIZE - 1; i >= 0; i--) {
						curCode.add((b & 1 << i) == 0 ? 0 : 1); // add each bit destinationFile current code
						curChar = codes.get(curCode.length() - 1).get(curCode);
						if (curChar != null) { // if current code is a valid one, i.e. present in our HashMap List
							writeBuffer[writeBufferPos++] = curChar; // add it destinationFile the writeBuffer
							curCode.clear();
							if (writeBufferPos == CHAR_BUFFER_SIZE) {
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
						if (writeBufferPos == CHAR_BUFFER_SIZE) {
							writer.write(writeBuffer);
							writeBufferPos = 0;
						}
					}
				}
			}
			final byte lastByte = readBuffer[lengthByte - 2];
			final byte lastUsedBitInLastByte = readBuffer[lengthByte - 1];
			for (int i = Byte.SIZE - 1; i >= lastUsedBitInLastByte; i--) {
				curCode.add((lastByte & 1 << i) == 0 ? 0 : 1);
				curChar = codes.get(curCode.length() - 1).get(curCode);
				if (curChar != null) {
					writeBuffer[writeBufferPos++] = curChar;
					curCode.clear();
					if (writeBufferPos == CHAR_BUFFER_SIZE) {
						writer.write(writeBuffer);
						writeBufferPos = 0;
					}
				}
			}
			writer.write(writeBuffer, 0, writeBufferPos); // write remaining chars
		}
		printTime();
		System.out.println("Decompression successful !\n    Time: " + totalTimer.stop().diffString());
	}

	private void printMessage(final String s) {
		if (Main.VERBOSE) {
			System.out.println('\t' + s);
			localTimer = new Timer().start();
		}
	}

	private void printTime() {
		if (Main.VERBOSE) {
			System.out.println("\t    Time: " + localTimer.stop().diffString());
		}
	}

	private void getCodes() throws IOException {
		// First, we read the tree
		try (final BufferedReader reader = Files.newBufferedReader(sourceFile, CHARSET)) {
			printMessage("Reading Tree String representation... ");
			treeLength = reader.read(); // first int in file is the length of the tree, including this first int
			final char[] treeString = new char[treeLength];
			reader.read(treeString);
			printTime();

			printMessage("Building Tree and Codes... ");
			final TreeSet<CharacterCode> characterCodes = new Tree(treeString).getCharacterCodes();
			int length = 0;
			int l;
			for (final CharacterCode c : characterCodes) { // find maximum code length
				l = c.getCode().length();
				if (l > length) {
					length = l;
				}
			}
			printTime();

			printMessage("Transforming codes for translation... ");
			codes = new ArrayList<HashMap<BitArray, Character>>(length);
			for (int i = 0; i < length; i++) {
				codes.add(new HashMap<BitArray, Character>());
			}

			for (final CharacterCode c : characterCodes) {
				codes.get(c.getCode().length() - 1).put(c.getCode(), c.getChar());
			}
			printTime();
		}
	}

}
