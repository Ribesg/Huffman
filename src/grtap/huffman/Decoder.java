package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.CharacterCode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.TreeSet;
import static grtap.huffman.Encoder.CHARSET;

public class Decoder {

	private int treeEndPos;
	private int separator;

	public void decode(final Path from, final Path to,
			final boolean overwrite) throws IOException {
		if (Files.notExists(from)) {
			return; // TODO Error
		} else if (Files.exists(to) && !overwrite) {
			return; // TODO Error
		}

		// First, we read the tree
		try (final BufferedReader reader = Files.newBufferedReader(from, CHARSET)) {
			treeEndPos = reader.read(); // first int in file is the position of first char after the tree
			separator = reader.read(); // second int is separator

			char[] treeString = new char[treeEndPos-2];

			reader.read(treeString);

			int[] array = new int[256]; // store chars and their code's length
			int curLength = 1;

			for (char c : treeString){
				if (c == separator) {
					curLength++;
				} else {
					array[c] = curLength;
				}
			}

		}

		try(final FileChannel channel = new FileInputStream(from.toFile()).getChannel()){

			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, treeEndPos, channel.size());

		}



		// now we build the tree from the array

		// then we read the file and translate
	}

}
