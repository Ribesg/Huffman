package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.CharacterCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.TreeSet;

public abstract class Decoder {

    public final static char    INCR_CODE_LENGTH = 0xB9;
    public final static char    TREE_END         = 0xD7;	
    public final static Charset CHARSET          = StandardCharsets.ISO_8859_1;
	
	
	public static void decode(final Path from, final Path to, final boolean overwrite) throws IOException{
        if (Files.notExists(from)) {
            return; //TODO Error
        } else if (Files.exists(to) && !overwrite) {
            return; // TODO Error
        }
		
        //First, we read the tree
        final BufferedReader reader = Files.newBufferedReader(from, CHARSET);
        int[] array = new int[256]; //store chars and their code's length
        int curChar = reader.read();
        int curLength = 1;
        
        while (curChar != TREE_END)
        {
        	while(curChar == INCR_CODE_LENGTH){
        		curLength++;
        		curChar = reader.read();
        	}
        	while(curChar != INCR_CODE_LENGTH && curChar != TREE_END){
        		array[curChar] = curLength;
        		curChar = reader.read();        		
        	}
        }
        
        //now we build the tree from the array
        
        //then we read the file and translate
	}

	
}
