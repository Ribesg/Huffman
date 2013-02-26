package grtap.huffman.util;

import java.util.Arrays;

// Represents some bits
public class BitArray {

    // Our bits
    private byte[] bits;

    // The last written byte
    private int    lastByte;

    // Position of the last bit written in the last Byte
    private int    lastBit;

    // Default constructor with minimal capacity
    public BitArray() {
        this(1);
    }

    // Creates a new BitArray with an initial number of BYTES
    public BitArray(final int initialCapacity) {
        bits = new byte[initialCapacity]; // Full of 0s
        lastByte = 0;
        lastBit = Byte.SIZE;
    }

    // Used in clone()
    private BitArray(final BitArray o) {
        bits = o.bits.clone();
        lastByte = o.lastByte;
        lastBit = o.lastBit;
    }

    // Add a bit to the BitArray
    // Allowed params are 0 or 1
    // Scale the BitArray if full
    public BitArray add(final int bit) {
        if (lastBit == 0) {
            lastByte++;
            lastBit = Byte.SIZE;
            if (lastByte == bits.length) {
                doubleCapacity();
            }
        }
        lastBit--;
        if (bit == 1) {
            bits[lastByte] = set(bits[lastByte], lastBit);
        } else { // Assuming bit == 0
            // We don't have to write the 0 as the BitArray is initialized full of 0s
        }
        return this; // Chain call
    }

    // Clone the current BitArray and append a bit
    public BitArray cloneThenAdd(final int bit) {
        return clone().add(bit);
    }

    public int length() {
        return lastByte * Byte.SIZE + Byte.SIZE - lastBit;
    }

    private void doubleCapacity() {
        bits = Arrays.copyOf(bits, 2 * bits.length);
    }

    // Set the bit at position 'pos' in the byte 'b'
    public byte set(final byte b, final int pos) {
        return (byte) (b | 1 << pos);
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder(bits.length * Byte.SIZE);
        for (int i = 0; i < lastByte; i++) {
            for (int j = Byte.SIZE - 1; j >= 0; j--) {
                final byte b = bits[i];
                s.append((b >> j & 1) == 0 ? '0' : '1');
            }
        }
        for (int j = Byte.SIZE - 1; j >= lastBit; j--) {
            final byte b = bits[lastByte];
            s.append((b >> j & 1) == 0 ? '0' : '1');
        }
        return s.toString();
    }

    @Override
    public BitArray clone() {
        return new BitArray(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(bits);
        result = prime * result + lastBit;
        result = prime * result + lastByte;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BitArray other = (BitArray) obj;
        if (!Arrays.equals(bits, other.bits)) {
            return false;
        }
        if (lastBit != other.lastBit) {
            return false;
        }
        if (lastByte != other.lastByte) {
            return false;
        }
        return true;
    }
}
