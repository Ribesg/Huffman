package grtap.huffman.util;

public class CharacterCode implements Comparable<CharacterCode> {

    private final char     character;
    private final BitArray binaryCode;

    public CharacterCode(final char c, final BitArray code) {
        character = c;
        binaryCode = code;
    }

    @Override
    public int compareTo(final CharacterCode o) {
        return binaryCode.compareTo(o.binaryCode);
    }

    @Override
    public String toString() {
        return new StringBuilder(1 + binaryCode.length()).append(character).append(" : ").append(binaryCode.toString()).toString();
    }

    @Override
    public int hashCode() {
        return character;
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
        final CharacterCode other = (CharacterCode) obj;
        if (character != other.character) {
            return false;
        }
        return true;
    }

    // Getters / Setters
    public char getChar() {
        return character;
    }

    public BitArray getCode() {
        return binaryCode;
    }

}
