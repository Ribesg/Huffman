package grtap.huffman;

import grtap.huffman.api.Decoder;
import grtap.huffman.api.Encoder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static boolean       VERBOSE = false;
    private final static String USAGE   = "Usage: java -jar huffman.jar <Input> [-v|--verbose]";

    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println(USAGE);
            return;
        }
        System.out.println();
        final Path from = Paths.get(args[0]);
        final Path to = Paths.get(from.getFileName().toString() + ".compressed");
        final Path decompressed = Paths.get(from.getFileName().toString() + ".decompressed");

        for (final String arg : args) {
            if (arg.equalsIgnoreCase("-v") || arg.equalsIgnoreCase("--verbose")) {
                VERBOSE = true;
                break;
            }
        }

        try {
            final Encoder e = new EncoderImpl(from, to, true);
            e.encode();
            System.out.println();
            final Decoder d = new DecoderImpl(to, decompressed, true);
            d.decode();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
