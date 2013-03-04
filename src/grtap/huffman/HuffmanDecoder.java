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

public class HuffmanDecoder {

	public static void decode(Path originalFile, Path decodedFile) {
		decode(originalFile, decodedFile, false);
	}

	public static void decode(Path originalFile, Path decodedFile, boolean overwrite) {
		// Check files
		if (!Files.exists(originalFile)) {
			System.out.println("File not found : " + originalFile);
			return;
		}
		if (!overwrite && Files.exists(decodedFile)) {
			System.out.println("File already exists : " + decodedFile);
			return;
		}

		// Tree huffmanTree = buildTree(originalFile);
	}

	public static Tree buildTree(final Path textFile) {
		
		
		return null;
	}
}
