package nu.mrpi.wordfeudapi.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Pierre Ingmansson
 */
public class Rack {
    private final char[] rack;

    public Rack(final char... rack) {
        this.rack = rack;
    }

    public boolean hasWildcard() {
        for (final char character : rack) {
            if (character == Tile.WILDCARD) {
                return true;
            }
        }
        return false;
    }

    public int countChar(final char c) {
        int charCount = 0;
        for (final char currentChar : rack) {
            if (c == currentChar) {
                charCount++;
            }
        }
        return charCount;
    }

    public boolean hasDuplicateLetters() {
        return getDuplicateLetters().length > 0;
    }

    public char[] getDuplicateLetters() {
        final Set<Character> uniqueLetters = new TreeSet<Character>();
        final List<Character> duplicateLetters = new ArrayList<Character>();

        for (final char letter : rack) {
            if (letter != Tile.WILDCARD) {
                if (uniqueLetters.contains(letter)) {
                    duplicateLetters.add(letter);
                } else {
                    uniqueLetters.add(letter);
                }
            }
        }

        return convert(duplicateLetters);
    }

    private char[] convert(final List<Character> duplicateLetters) {
        final char[] chars = new char[duplicateLetters.size()];
        for (int i = 0, duplicateLettersSize = duplicateLetters.size(); i < duplicateLettersSize; i++) {
            chars[i] = duplicateLetters.get(i);
        }
        return chars;
    }

    public char[] chars() {
        return rack;
    }

    @Override
    public String toString() {
        return Arrays.toString(rack);
    }
}
