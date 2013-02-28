package grtap.huffman;

import grtap.huffman.binarytree.Tree;
import grtap.huffman.util.FastFileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

public class HuffmanEncoder {

    public static void encode(final Path originalFile, final Path encodedFile) {
        encode(originalFile, encodedFile, false);
    }

    public static void encode(final Path originalFile, final Path encodedFile, final boolean overwrite) {
        // Check files
        if (!Files.exists(originalFile)) {
            System.out.println("File not found : " + originalFile);
            return;
        }
        if (!overwrite && Files.exists(encodedFile)) {
            System.out.println("File already exists : " + originalFile);
            return;
        }

        // Tree huffmanTree = buildTree(originalFile);
    }

    // TODO make this private after testing
    public static Tree buildTree(final Path textFile) {
        // Count characters occurences
        final HashMap<Character, Integer> characterMap = countCharactersInFile(textFile);

        // Build the per-characters trees
        final TreeSet<Tree> treeSet = new TreeSet<Tree>();
        for (final Entry<Character, Integer> e : characterMap.entrySet()) {
            treeSet.add(new Tree(e.getKey(), e.getValue()));
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

    // TODO make this private after testing
    // The call is always made with an existing file
    // Faster with an int[] than with an HashMap ?
    // Use an additional char buffer
    public static HashMap<Character, Integer> countCharactersInFileFastCharBuffer(final Path pathToFile) {
        final int[] characterCount = new int[256]; // Size of ANSI table
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
            final char[] buffer = new char[64];
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
        final HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < characterCount.length; i++) {
            final int count = characterCount[i];
            if (count > 0) {
                map.put((char) i, count);
            }
        }
        return map;
    }

    // TODO make this private after testing
    // The call is always made with an existing file
    // Faster with an int[] than with an HashMap ?
    public static HashMap<Character, Integer> countCharactersInFileFast(final Path pathToFile) {
        final int[] characterCount = new int[256]; // Size of ANSI table
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
            while (reader.ready()) {
                characterCount[reader.read()]++;
            }
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        final HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < characterCount.length; i++) {
            final int count = characterCount[i];
            if (count > 0) {
                map.put((char) i, count);
            }
        }
        return map;
    }

    // TODO make this private after testing
    // The call is always made with an existing file
    public static HashMap<Character, Integer> countCharactersInFileCharBuffer(final Path pathToFile) {
        final HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
            final char[] buffer = new char[64];
            int nb = -1;
            char c;
            while (reader.ready()) {
                nb = reader.read(buffer);
                for (int i = 0; i < nb; i++) {
                    c = buffer[i];
                    if (characterMap.containsKey(c)) {
                        characterMap.put(c, characterMap.get(c) + 1);
                    } else {
                        characterMap.put(c, 1);
                    }
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return characterMap;
    }

    // TODO make this private after testing
    // The call is always made with an existing file
    public static HashMap<Character, Integer> countCharactersInFile(final Path pathToFile) {
        final HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();
        try (BufferedReader reader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
            char c;
            while (reader.ready()) {
                c = (char) reader.read();
                if (characterMap.containsKey(c)) {
                    characterMap.put(c, characterMap.get(c) + 1);
                } else {
                    characterMap.put(c, 1);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return characterMap;
    }

    // TODO make this private after testing
    // The call is always made with an existing file
    // Source of benchmarks : http://stackoverflow.com/a/11876086
    public static HashMap<Character, Integer> countCharactersInFileFastString(final Path pathToFile) {
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
            } else {
                final Field field = String.class.getDeclaredField("value");
                field.setAccessible(true); // MUAHAHA FUCK THE POLIIIIIIIIIIIIICE
                final char[] chars = (char[]) field.get(fileContent);
                for (int i = 0; i < length; i++) {
                    characterCount[chars[i]]++;
                }
            }
        } catch (final IOException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        final HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < characterCount.length; i++) {
            final int count = characterCount[i];
            if (count > 0) {
                map.put((char) i, count);
            }
        }
        return map;
    }
}
