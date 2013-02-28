package grtap.huffman;

import grtap.huffman.util.Timer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Random;

public class Main {

    public static void main(final String[] args) {
        final Path test = Paths.get("test.txt");
        final int nbLoop = 100;
        int time1 = 0, time2 = 0;
        for (int i = 0; i < nbLoop; i++) {
            random(test, 150);
            final Timer t1 = new Timer().start();
            for (int j = 0; j < nbLoop; j++) {
                updateFile(test);
                new StreamEncoder().countCharactersInFile(test);
            }
            time1 += t1.stop();
            final Timer t2 = new Timer().start();
            for (int j = 0; j < nbLoop; j++) {
                updateFile(test);
                new StringEncoder().countCharactersInFile(test);
            }
            time2 += t2.stop();
        }
        System.out.println(time1);
        System.out.println(time2);
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

    private static void updateFile(final Path path) {
        try {
            Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
