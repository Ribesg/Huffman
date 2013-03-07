package grtap.huffman;

import grtap.huffman.api.Encoder;
import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;
import grtap.huffman.util.Timer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.TreeSet;

public class EncoderImpl implements Encoder {

	public final static Charset		CHARSET	= StandardCharsets.ISO_8859_1;

	private final Path				sourceFile;
	private final Path				destinationFile;

	private final int[]				characterCount;
	private Tree					huffmanTree;
	private TreeSet<CharacterCode>	sortedCodes;
	private final BitArray[]		codesArray;
	private Timer					localTimer, totalTimer;

	public EncoderImpl(final Path from, final Path to) {
		this(from, to, false);
	}

	public EncoderImpl(final Path from, final Path to, final boolean overwrite) {
		if (Files.notExists(from)) {
			throw new IllegalArgumentException("Source file not found");
		} else if (Files.exists(to) && !overwrite) {
			throw new IllegalArgumentException("Destination file already exists");
		}
		sourceFile = from;
		destinationFile = to;
		characterCount = new int[256];
		huffmanTree = null;
		sortedCodes = new TreeSet<CharacterCode>();
		codesArray = new BitArray[256];
	}

	@Override
	public void encode() throws IOException {
		System.out.println("Starting compression of file \"" + sourceFile.getFileName() + "\" to \"" + destinationFile.getFileName() + "\"");
		totalTimer = new Timer().start();
		// Count the number of occurences of each character in the file
		printMessage("Counting characters in file " + sourceFile.getFileName() + "... ");
		countCharactersInFile();
		printTime();

		// Build the tree from this count
		printMessage("Building Huffman Tree from character count... ");
		buildTree();
		printTime();

		// Get the character codes from the tree
		printMessage("Generating character codes from tree... ");
		sortedCodes = huffmanTree.getCharacterCodes();
		printTime();

		if (sortedCodes.size() == 0) {
			throw new IllegalArgumentException("Why do you want to compress an empty file ?");
		} else if (sortedCodes.size() == 1) {
			handleOneCharFile(sortedCodes.pollFirst().getChar());
		}
		// Write the Tree to the file
		printMessage("Writing tree string representation as output file header... ");
		writeTree();
		printTime();

		// Create the codesArray for encoding
		createCodeArray();
		// Encode source file
		printMessage("Encoding source file... ");
		try (final BufferedReader reader = Files.newBufferedReader(sourceFile, CHARSET);
				final FileOutputStream writer = new FileOutputStream(destinationFile.toFile(), true)) {
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
			if (writeBuffer.hasRemainingByte()) {
				// Last byte does not contain 8 interesting bits
				final int lastByteLength = writeBuffer.getLastByteLength();
				writer.write(writeBuffer.getLastByte());
				writer.write((byte) Byte.SIZE - lastByteLength);
			} else {
				// Last byte is full
				writer.write(0x00);
			}
		}
		printTime();
		System.out.println("Compression successful !\n    Time: " + totalTimer.stop().diffString() + "\n    Compression rate: "
				+ new DecimalFormat("#0.00").format(100 - 100.0 * Files.size(destinationFile) / Files.size(sourceFile)) + "%");
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

	private void createCodeArray() {
		for (final CharacterCode c : sortedCodes) {
			codesArray[c.getChar()] = c.getCode();
		}
	}

	private void writeTree() throws IOException {
		// First build a char[] to be written so that we can store the string length
		// before storing the string itself
		try (final BufferedWriter writer = Files.newBufferedWriter(destinationFile, CHARSET)) {
			final char[] treeString = new char[512];
			int treeStringLength = 0;
			for (final CharacterCode cur : sortedCodes) {
				treeString[treeStringLength++] = cur.getChar();
				treeString[treeStringLength++] = (char) cur.getCode().length();
			}
			writer.write(treeStringLength);
			writer.write(treeString, 0, treeStringLength);
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

	private void handleOneCharFile(final char c) {
		huffmanTree = Tree.newMonoCharacterHuffmanTree(c);
		sortedCodes = new TreeSet<CharacterCode>();
		sortedCodes.add(new CharacterCode(c, new BitArray().add(0)));
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
