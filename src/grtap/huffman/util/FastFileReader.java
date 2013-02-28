package grtap.huffman.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class FastFileReader {

    private static class ByteBuffer {
        private byte[] byteBuffer;
        private int    lastWrite;

        public ByteBuffer() {
            byteBuffer = new byte[8192];
            lastWrite = 0;
        }

        public void put(final byte[] bytes, final int length) {
            ensureCapacity(length);
            System.arraycopy(bytes, 0, byteBuffer, lastWrite, length);
            lastWrite += length;
        }

        private void ensureCapacity(final int length) {
            final int requiredSpace = lastWrite + length;
            if (byteBuffer.length <= requiredSpace) {
                final byte[] newByteBuffer = new byte[requiredSpace * 2];
                System.arraycopy(byteBuffer, 0, newByteBuffer, 0, lastWrite);
                byteBuffer = newByteBuffer;
            }
        }

        @Override
        public String toString() {
            return new String(byteBuffer, 0, lastWrite);
        }
    }

    public static final String read(final Path filePath) throws IOException {
        return read(filePath.toString());
    }

    public static final String read(final String filePath) throws IOException {
        try (final BufferedInputStream input = new BufferedInputStream(new FileInputStream(filePath))) {
            final ByteBuffer buffer = new ByteBuffer();
            final byte[] byteArray = new byte[8192];
            int length = input.read(byteArray);
            while (length != -1) {
                buffer.put(byteArray, length);
                length = input.read(byteArray);
            }
            return buffer.toString();
        }
    }
}
