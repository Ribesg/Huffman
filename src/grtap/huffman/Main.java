package grtap.huffman;

import grtap.huffman.util.Timer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Random;

public class Main {

    public static void main(final String[] args) {

        long timeFaster = 0, timeFast = 0, timeCharBuffer = 0, time = 0;
        final long nbLoop = 20;
        for (int j = 0; j < nbLoop; j++) {
            random(Paths.get("test.txt"), 50000);
            for (int i = 0; i < nbLoop; i++) {
                final Timer t = new Timer().start();
                HuffmanEncoder.countCharactersInFile(Paths.get("test.txt"));
                time += i == 0 ? 0 : t.stop(); // First access to file is slow
            }
            for (int i = 0; i < nbLoop; i++) {
                final Timer tCharBuffer = new Timer().start();
                HuffmanEncoder.countCharactersInFileCharBuffer(Paths.get("test.txt"));
                timeCharBuffer += i == 0 ? 0 : tCharBuffer.stop(); // First access to file is slow
            }
            for (int i = 0; i < nbLoop; i++) {
                final Timer tFast = new Timer().start();
                HuffmanEncoder.countCharactersInFileFast(Paths.get("test.txt"));
                timeFast += i == 0 ? 0 : tFast.stop(); // First access to file is slow
            }
            for (int i = 0; i < nbLoop; i++) {
                final Timer tFaster = new Timer().start();
                HuffmanEncoder.countCharactersInFileFastCharBuffer(Paths.get("test.txt"));
                timeFaster += i == 0 ? 0 : tFaster.stop(); // First access to file is slow
            }
        }
        final DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        System.out.println("HASHMAP: " + f.format((double) time / (double) nbLoop * nbLoop / (1000 * 1000)) + " ms (100.00%)");
        System.out.println("INT[]: " + f.format((double) timeFast / (double) nbLoop * nbLoop / (1000 * 1000)) + " ms (" + f.format(100.0 * timeFast / time) + "%)");
        System.out.println("HASHMAP+CHARBUFFER: " + f.format((double) timeCharBuffer / (double) nbLoop * nbLoop / (1000 * 1000)) + " ms (" + f.format(100.0 * timeCharBuffer / time) + "%)");
        System.out.println("INT[]+CHARBUFFER: " + f.format((double) timeFaster / (double) nbLoop * nbLoop / (1000 * 1000)) + " ms (" + f.format(100.0 * timeFaster / time) + "%)");

        //        final Tree tree = HuffmanEncoder.buildTree(Paths.get("test.txt"));
        //
        //        TreePrinter.printTree(tree);
        //
        //        final Map<Character, Integer> map = HuffmanEncoder.countCharactersInFile(Paths.get("test.txt"));
        //
        //        for (final Entry<Character, BitArray> e : tree.getCharacterCodes().entrySet()) {
        //            printChar(e.getKey());
        //            System.out.print(" : ");
        //            System.out.print(map.get(e.getKey()));
        //            System.out.print(" : ");
        //            System.out.println(e.getValue());
        //        }
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

    private static void random(final Path path, final int nb) {
        final Random r = new Random();
        try (final BufferedWriter bw = Files.newBufferedWriter(path, Charset.defaultCharset())) {
            for (int i = 0; i < nb; i++) {
                bw.write(r.nextInt(256));
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
