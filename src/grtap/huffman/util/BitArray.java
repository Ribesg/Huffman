package grtap.huffman.util;

import java.util.Arrays;

// Represents some bits
public class BitArray implements Comparable<BitArray> {

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
        bits = new byte[initialCapacity <= 1 ? 1 : initialCapacity]; // Full of 0s
        lastByte = 0;
        lastBit = Byte.SIZE;
    }

    public void clear() {
        Arrays.fill(bits, (byte) 0);
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

    // TODO There is certainly a faster and more complicated way
    public BitArray add(final BitArray o) {
        if (this.lastByte == 0 && lastBit == Byte.SIZE) {
            System.arraycopy(o.bits, 0, bits, 0, o.lastByte + 1);
            lastByte = o.lastByte;
            lastBit = o.lastBit;
        } else {
            ensureCapacity(lastByte + o.lastByte);
            byte mask = (byte) (0xFF << Byte.SIZE - lastBit);

            for (int i = 0; i <= o.lastByte; i++) {
                byte other = o.bits[i];
                bits[lastByte + i] |= (other & mask) >> Byte.SIZE - lastBit;
                bits[lastByte + i + 1] = (byte) (o.bits[i] << lastBit);
            }
            lastByte = lastByte + o.lastByte + 1;
            if (lastBit + o.lastBit >= Byte.SIZE) {
                lastByte--;
            }
            lastBit = (lastBit + o.lastBit) % Byte.SIZE;
        }

        // for (int i = 0; i < o.lastByte; i++) {
        // for (int j = Byte.SIZE - 1; j >= 0; j--) {
        // final byte b = o.bits[i];
        // add((b >> j & 1) == 0 ? 0 : 1);
        // }
        // }
        // for (int j = Byte.SIZE - 1; j >= o.lastBit; j--) {
        // final byte b = o.bits[o.lastByte];
        // add((b >> j & 1) == 0 ? 0 : 1);
        // }
        return this;
    }

    // removes last bit from the BitArray
    public void remove() {
        bits[lastByte] = clear(bits[lastByte], lastBit);
        if (lastBit++ == Byte.SIZE) {
            lastBit = 0;
            lastByte--;
        }
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

    private void ensureCapacity(int nbBytes) {
        while (bits.length < nbBytes) {
            doubleCapacity();
        }
    }

    // Sets the bit at position 'pos' in the byte 'b'
    private byte set(final byte b, final int pos) {
        return (byte) (b | 1 << pos);
    }

    // Clears the bit at position 'pos' in the byte 'b'
    public byte clear(final byte b, final int pos) {
        return (byte) (b & ~(1 << pos));
    }

    // Get a byte[] of all the "complete" bytes of the array
    // Remove those first bytes from the array
    public byte[] pollByteArray() {
        if (lastBit == 0) {
            final byte[] res = Arrays.copyOf(bits, lastByte + 1);
            bits = new byte[bits.length];
            lastByte = 0;
            lastBit = Byte.SIZE;
            return res;
        } else {
            final byte[] res = new byte[lastByte];
            final byte[] newBits = new byte[bits.length];
            System.arraycopy(bits, 0, res, 0, lastByte);
            newBits[0] = bits[lastByte];
            bits = newBits;
            lastByte = 0;
            return res;
        }
    }

    public boolean hasRemainingByte() {
        return !(lastByte > 0 || lastBit != Byte.SIZE);
    }

    public byte getLastByte() {
        return bits[lastByte];
    }

    public int getLastByteLength() {
        return Byte.SIZE - lastBit;
    }

    public boolean isPrefixedBy(BitArray o) {
        return this.toString().startsWith(o.toString());
    }

    public boolean isPrefixOf(BitArray o) {
        return o.isPrefixedBy(this);
    }

    @Override
    public String toString() {
        byte b;
        final StringBuilder s = new StringBuilder(bits.length * Byte.SIZE);
        for (int i = 0; i < lastByte; i++) {
            for (int j = Byte.SIZE - 1; j >= 0; j--) {
                b = bits[i];
                s.append((b >> j & 1) == 0 ? '0' : '1');
            }
        }
        for (int j = Byte.SIZE - 1; j >= lastBit; j--) {
            b = bits[lastByte];
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

    @Override
    public int compareTo(final BitArray o) {
        int res;
        int i = 0;
        int thisByte, otherByte;
        while (true) {
            thisByte = (i < bits.length ? bits[i] : 0) & 0xFF;
            otherByte = (i < o.bits.length ? o.bits[i] : 0) & 0xFF;
            res = Integer.compare(thisByte, otherByte);
            i++;
            if (res != 0) {
                return res;
            } else if (i >= bits.length && i >= o.bits.length) {
                return 0;
            }
        }
    }
}
