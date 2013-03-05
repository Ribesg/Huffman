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
    public final static int LOOPS = 10;

    @SuppressWarnings("unused")
    public static void main(final String[] args) {
        final Path testFromFile = Paths.get("test.txt");
        final Path testToFile = Paths.get("test.txt.compressed");

        final Path dictionarySource = Paths.get("dictionary2.txt");
        final Path dictionaryDest = Paths.get("dictionary2.txt.compressed");

        final Path miserablesSource = Paths.get("LesMiserables.txt");
        final Path miserablesDest = Paths.get("LesMiserables.txt.compressed");
        final Path miserablesDecoded = Paths.get("LesMiserables.txt.decompressed");

        Path from = miserablesSource, to = miserablesDest;

        // System.out.println("Generating random file...");
        // random(testFromFile, 15_000, true);

        System.out.println("Encoding...");
        try {
            long time = 0;
            Encoder e = null;
            for (int i = 0; i < LOOPS; i++) {
                final Timer t = new Timer().start();
                e = new Encoder(from, to, true);
                e.encode();
                t.stop();
                time += t.nanoDiff();
            }
            // TreePrinter.printTree(e.huffmanTree);
            for (char c = 0; c < 256; c++) {
                if (e.codesArray[c] != null) {
                    System.out.println(c + " ; " + e.codesArray[c].length() + " ; " + e.codesArray[c]);
                }
            }
            System.out.println();
            System.out.println(e.sortedCodes.size());
            final DecimalFormat f = new DecimalFormat();
            System.out.println("Done! " + Timer.parseDiff(time / LOOPS));
            System.out.println("Source size : " + f.format(Files.size(from)) + " bytes");
            System.out.println("Destination size : " + f.format(Files.size(to)) + " bytes");
            System.out.println("Compression rate : " + (100 - 100 * Files.size(to) / Files.size(from)) + "%");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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
                    c = (char) r.nextInt(256);
                    bw.write(c);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static void updateFile(final Path path) {
        try {
            Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
