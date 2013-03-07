package grtap.huffman;

import grtap.huffman.api.Decoder;
import grtap.huffman.api.Encoder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
	public static boolean		VERBOSE	= false;
	private final static String	USAGE	= "Usage: java -jar huffman.jar <-e|--encode|-d|--decode> <InputFile> [OutputFile] [-v|--verbose] [-o|--overwrite]";

	public static void main(final String[] args) {
		if (args.length < 2) {
			outputUsage();
			return;
		}

		final ArrayList<String> argsList = new ArrayList<String>();
		for (final String arg : args) {
			argsList.add(arg.toLowerCase());
		}

		final boolean encode = argsList.contains("-e") || argsList.contains("--encode");
		final boolean decode = argsList.contains("-d") || argsList.contains("--decode");
		VERBOSE = argsList.contains("-v") || argsList.contains("--verbose");
		final boolean overwrite = argsList.contains("-o") || argsList.contains("--overwrite");

		if (encode && decode || !encode && !decode) {
			outputUsage();
			return;
		}

		Path from = null, to = null;
		for (final String s : argsList) {
			if (!s.startsWith("-")) {
				if (from == null) {
					from = Paths.get(s);
				} else if (to == null) {
					to = Paths.get(s);
				} else {
					System.out.println("Unknown argument : " + s);
				}
			}
		}
		if (from == null) {
			outputUsage();
			return;
		} else if (to == null) {
			to = Paths.get(from.toAbsolutePath().toString() + ".output");
		}

		try {
			if (encode) {
				final Encoder e = new EncoderImpl(from, to, overwrite);
				e.encode();
			} else {
				final Decoder d = new DecoderImpl(from, to, overwrite);
				d.decode();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	private static void outputUsage() {
		System.out.println(USAGE);
	}
}
