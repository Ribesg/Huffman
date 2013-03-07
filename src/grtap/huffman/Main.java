package grtap.huffman;

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
        Path from = Paths.get(args[0]);
        Path to = Paths.get(from.getFileName().toString() + ".compressed");
        Path decompressed = Paths.get(from.getFileName().toString() + ".decompressed");

        for (String arg : args) {
            if (arg.equalsIgnoreCase("-v") || arg.equalsIgnoreCase("--verbose")) {
                VERBOSE = true;
                break;
            }
        }

        try {
            Encoder e = new Encoder(from, to, true);
            e.encode();
            System.out.println();
            Decoder d = new Decoder(to, decompressed, true);
            d.decode();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
