package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.BitArray;
import grtap.huffman.util.CharacterCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeSet;
import static grtap.huffman.Encoder.CHARSET;

public class Decoder {

	private int treeEndPos;
	private char separator;
	private Path from;
	private Path to;
	private HashMap<BitArray, Character> codes;

	public Decoder(final Path from, final Path to)
	{
		this(from, to, false);
	}
	
	public Decoder(final Path from, final Path to,final boolean overwrite)
	{
		if (Files.notExists(from)) {
			throw new IllegalArgumentException("Source file not found");
		} else if (Files.exists(to) && !overwrite) {
			throw new IllegalArgumentException("Destination file already exists");
		}
		
		this.from = from;
		this.to = to;
	}	
	
	public void decode() throws IOException {

		//first, we get the codes for each character
		codes = getCodes();
		
		// then we read the file and translate

		try(final FileInputStream inputStream = new FileInputStream(from.toFile());final FileChannel channel = inputStream.getChannel()){

			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, treeEndPos, channel.size());
			BufferedWriter writer = Files.newBufferedWriter(to, CHARSET);
			char[] writeBuffer = new char[8192];
			int writeBufferPos = 0;
			Character curChar;
			BitArray curCode = new BitArray(2);
			
			for(int i = 0; i < buffer.capacity(); i++){ 			//read each byte in file
				for(int j = 0; j < 7; j++){							
					curCode.add(0x80 & (buffer.get(i) << j));		//add each bit to current code 
					if((curChar = codes.get(curCode)) != null){		//if current code is a valid one
						writeBuffer[writeBufferPos++] = curChar;	//add it to the writeBuffer
						if(writeBufferPos == 8192){					
							writer.write(writeBuffer);				//write in destination file when writeBuffer is full
							writeBufferPos = 0;						//reset
						}
					}
				}
			}
			writer.write(writeBuffer, 0, writeBufferPos); //write remaining chars
			}
	}
	
	private HashMap<BitArray, Character> getCodes() throws IOException
	{
		HashMap<BitArray, Character> res = new HashMap<BitArray, Character>();
		
		// First, we read the tree
		try (final BufferedReader reader = Files.newBufferedReader(from, CHARSET)) {
			treeEndPos = reader.read(); // first int in file is the position of first char after the tree
			separator = (char)reader.read(); // second int is separator

			char[] treeString = new char[treeEndPos];

			reader.read(treeString);

			int[] array = new int[256]; 
			LinkedHashMap<Character, Integer> charCodesLength = new LinkedHashMap<Character,Integer>();// store chars and their code's length
			int curLength = 1;

			for (char c : treeString){
				if (c == separator) {
					curLength++;
				} else {
					charCodesLength.put(c, curLength);
					array[c] = curLength;
				}
			}
			
			BitArray curCode = new BitArray(2);
			for(Entry<Character,Integer> e : charCodesLength.entrySet()){
				if(e.getValue() > 0){
					for(int i = 0; i < e.getValue(); i++){
						curCode.add(0);
						if(res.containsKey(curCode)){
							curCode.remove();
							curCode.add(1);
						}
					}
					res.put(curCode, e.getKey());
					curCode = new BitArray(2);
				}
			}
		}
		
		return res;
	}
	

}
