package grtap.huffman;

import grtap.huffman.util.Timer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.util.Random;

public class Main {
    public final static int LOOPS = 1;

    public static void main(final String[] args) {
        final Path testFromFile = Paths.get("test.txt");
        final Path testToFile = Paths.get("test.txt.compressed");

        final Path dictionarySource = Paths.get("dictionary2.txt");
        final Path dictionaryDest = Paths.get("dictionary2.txt.compressed");

        final Path miserablesSource = Paths.get("LesMiserables.txt");
        final Path miserablesDest = Paths.get("LesMiserables.txt.compressed");
        final Path miserablesDecoded = Paths.get("LesMiserables.txt.decompressed");

        // System.out.println("Generating random file...");
        // random(testFromFile, 15_000, true);

        System.out.println("Encoding...");
        try {
            long time = 0;
            for (int i = 0; i < LOOPS; i++) {
                final Timer t = new Timer().start();
                Encoder.encode(testFromFile, testToFile, true);
                // Encoder.encode(dictionarySource, dictionaryDest, true);
                // Encoder.encode(miserablesSource, miserablesDest, true);
                t.stop();
                time += t.nanoDiff();
            }
            final DecimalFormat f = new DecimalFormat();
            System.out.println("Done! " + time / LOOPS);
            System.out.println("Source size : " + f.format(Files.size(testFromFile)) + " bytes");
            System.out.println("Destination size : " + f.format(Files.size(testToFile)) + " bytes");
            System.out.println("Compression rate : " + (100 - 100 * Files.size(testToFile) / Files.size(testFromFile)) + "%");
            // System.out.println("Source size : " + f.format(Files.size(dictionarySource)) + " bytes");
            // System.out.println("Destination size : " + f.format(Files.size(dictionaryDest)) + " bytes");
            // System.out.println("Compression rate : " + (100 - 100 * Files.size(dictionaryDest) / Files.size(dictionarySource)) + "%");
            // System.out.println("Source size : " + f.format(Files.size(miserablesSource)) + " bytes");
            // System.out.println("Destination size : " + f.format(Files.size(miserablesDest)) + " bytes");
            // System.out.println("Compression rate : " + (100 - 100 * Files.size(miserablesDest) / Files.size(miserablesSource)) + "%");
        } catch (final IOException e) {
            e.printStackTrace();
        }

        System.out.println("Decoding...");
        try {
            long time = 0;
            for (int i = 0; i < LOOPS; i++) {
                final Timer t = new Timer().start();
                // Encoder.encode(testFromFile, testToFile, true);
                // Encoder.encode(dictionarySource, dictionaryDest, true);
                Decoder.decode(miserablesDest, miserablesDecoded, true);
                t.stop();
                time += t.nanoDiff();
            }
            final DecimalFormat f = new DecimalFormat();
            System.out.println("Done! " + time / LOOPS);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void printChar(final char c) {
        switch (c) {
        case '\n':
            System.out.print("\\n");
            break;
        case '\t':
            System.out.print("\\t");
            break;
        default:
            System.out.print(c + " ");
            break;
        }
    }

    private static void random(final Path path, final int nb, final boolean alphanumeric) {
        final Random r = new Random();
        try (final BufferedWriter bw = Files.newBufferedWriter(path, Charset.defaultCharset())) {
            char c = '\n';
            if (alphanumeric) {
                for (int i = 0; i < nb; i++) {
                    do {
                        c = (char) r.nextInt(256);
                    } while (!(c >= 48 && c <= 57 || c >= 65 && c <= 90 || c >= 97 && c <= 122));
                    bw.write(c);
                }
            } else {
                for (int i = 0; i < nb; i++) {
                    do {
                        c = (char) r.nextInt(256);
                    } while (c == Encoder.INCR_CODE_LENGTH || c == Encoder.TREE_END);
                    bw.write(c);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateFile(final Path path) {
        try {
            Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
