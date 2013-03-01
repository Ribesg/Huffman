package grtap.huffman;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Random;

public class Main {

    public static void main(final String[] args) {

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
