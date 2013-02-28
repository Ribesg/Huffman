package grtap.huffman;

import grtap.huffman.util.FastFileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class StringEncoder extends Encoder {

    @Override
    public void encodeFile(final Path originalFile, final Path encodedFile) {
        // Tree huffmanTree = buildTree(originalFile);
        // TODO
    }

    // TODO make this private after testing
    // The call is always made with an existing file
    @Override
    public int[] countCharactersInFile(final Path pathToFile) {
        final int[] characterCount = new int[256]; // Size of ANSI table
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
            final String fileContent = FastFileReader.read(pathToFile);
            final int length = fileContent.length();
            if (length <= 64) {
                for (int i = 0; i < length; i++) {
                    characterCount[fileContent.charAt(i)]++;
                }
            } else if (length <= 500) {
                final char[] chars = new char[length];
                fileContent.getChars(0, length, chars, 0);
                for (int i = 0; i < length; i++) {
                    characterCount[chars[i]]++;
                }
            } else { // The fastest way is reflexion // TODO Reflection != OOP !
                final Field field = String.class.getDeclaredField("value");
                field.setAccessible(true);
                final char[] chars = (char[]) field.get(fileContent);
                for (int i = 0; i < length; i++) {
                    characterCount[chars[i]]++;
                }
            }
        } catch (final IOException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return characterCount;
    }
}
