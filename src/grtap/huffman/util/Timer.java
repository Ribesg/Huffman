package grtap.huffman.util;

public class Timer {

    private long startTime = -1, endTime = -1;

    public Timer start() {
        startTime = System.nanoTime();
        return this;
    }

    public long stop() {
        endTime = System.nanoTime();
        return getDiff();
    }

    public long getDiff() {
        if (startTime != -1 && endTime != -1) {
            return endTime - startTime;
        } else {
            return -1;
        }
    }
}
