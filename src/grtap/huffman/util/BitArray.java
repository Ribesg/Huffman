package grtap.huffman.util;

// Represents some bits
public class BitArray {

	// Our bits
	byte[]	bits;

	// The last written byte
	int		lastByte;

	// Position of the last bit written in the last Byte
	int		lastBit;

	// Default constructor with minimal capacity
	public BitArray() {
		this(1);
	}

	// Creates a new BitArray with an initial number of BYTES
	public BitArray(final int initialCapacity) {
		bits = new byte[initialCapacity];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = 0;
		}
		lastByte = 0;
		lastBit = 0;
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
		if (lastBit == Byte.SIZE - 1) {
			lastByte++;
			lastBit = 0;
			if (lastByte == bits.length) {
				doubleCapacity();
			}
		} else {
			lastBit++;
		}
		if (bit == 1) {
			bits[lastByte] = set(bits[lastByte], lastBit);
		} else { // Assuming bit == 0
			// We don't have to write the 0 as the BitArray is initialized full of 0s
		}
		return this; // Chain call
	}

	public int length() {
		return lastByte * Byte.SIZE + lastBit;
	}

	private void doubleCapacity() {
		final byte[] newBitsArray = new byte[2 * bits.length];
		for (int i = 0; i < bits.length; i++) {
			newBitsArray[i] = bits[i];
		}
		bits = newBitsArray;
	}

	// Set the bit at position 'pos' to 1 in the byte 'b'
	private byte set(final byte b, final int pos) {
		return (byte) (b | 1 << (Byte.SIZE - pos));
	}

	// TODO CA BUG
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder(bits.length * Byte.SIZE);
		
		for(int i = 0; i < lastByte; i++){
			final byte b = bits[i];
			for(int j=0; j<Byte.SIZE;j++){
				s.append((b & (1 << (Byte.SIZE - j))) == 0 ? '0' : '1');
			}
		}	
		
		
		/*
		for (int i = 0; i < Byte.SIZE * lastByte; i++) {
			final byte b = bits[i / Byte.SIZE];
			s.append((b << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
		}*/
		for (int i = 0; i < lastBit; i++) {
			final byte b = bits[lastByte];
			s.append((b << i & 0x80) == 0 ? '0' : '1');
		}
		return s.toString();
	}

	@Override
	public BitArray clone() {
		return new BitArray(this);
	}
}
