package grtap.huffman.util;

import java.text.DecimalFormat;

public class Timer {

	private static final DecimalFormat	f	= new DecimalFormat("#0.00");

	private long						startTime	= -1, endTime = -1;

	public Timer start() {
		startTime = System.nanoTime();
		return this;
	}

	public Timer stop() {
		endTime = System.nanoTime();
		return this;
	}

	public long nanoDiff() {
		if (startTime != -1 && endTime != -1) {
			return endTime - startTime;
		} else {
			return -1;
		}
	}

	public String diffString() {
		return parseDiff(nanoDiff());
	}

	public long hotNanoDiff() {
		return System.nanoTime() - startTime;
	}

	public String hotDiffString() {
		return parseDiff(hotNanoDiff());
	}

	public static String parseDiff(final long nano) {
		if (nano < 1_000L) {
			return nano + "ns";
		} else if (nano < 1_000_000L) {
			return f.format(nano / 1_000D) + "µs";
		} else if (nano < 1_000_000_000L) {
			return f.format(nano / 1_000_000D) + "ms";
		} else {
			return f.format(nano / 1_000_000_000D) + "s";
		}
	}
}
