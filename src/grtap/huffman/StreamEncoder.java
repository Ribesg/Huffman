package grtap.huffman;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class StreamEncoder extends Encoder {

    @Override
    public void encodeFile(final Path originalFile, final Path encodedFile) {
        // Tree huffmanTree = buildTree(originalFile);
        // TODO
    }

    // The call is always made with an existing file
    @Override
    public int[] countCharactersInFile(final Path pathToFile) {
        final int[] characterCount = new int[256]; // Size of ANSI table
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
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
