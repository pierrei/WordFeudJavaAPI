package nu.mrpi.wordfeudapi.domain;

import java.util.*;

/**
 * @author Pierre Ingmansson
 */
public class Rack {
    private char[] rack;

    public Rack(char... rack) {
        this.rack = rack;
    }

    public boolean hasWildcard() {
        for (char character : rack) {
            if (character == Tile.WILDCARD) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasDuplicateLetters() {
        return getDuplicateLetters().length > 0;
    }

    public char[] getDuplicateLetters() {
        Set<Character> uniqueLetters = new TreeSet<Character>();
        List<Character> duplicateLetters = new ArrayList<Character>();

        for (char letter : rack) {
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

    private char[] convert(List<Character> duplicateLetters) {
        char[] chars = new char[duplicateLetters.size()];
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
