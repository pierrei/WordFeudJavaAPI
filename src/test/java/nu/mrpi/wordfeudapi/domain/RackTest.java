package nu.mrpi.wordfeudapi.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Pierre Ingmansson
 */
public class RackTest {
    @Test
    public void getDuplicatesForNoDuplicates() throws Exception {
        final Rack rack = new Rack('a', 'b', 'c');

        final char[] duplicateLetters = rack.getDuplicateLetters();
        assertEquals(0, duplicateLetters.length);
    }

    @Test
    public void getDuplicatesForOneDuplicate() throws Exception {
        final Rack rack = new Rack('a', 'b', 'c', 'a');

        final char[] duplicateLetters = rack.getDuplicateLetters();
        assertEquals(1, duplicateLetters.length);
        assertEquals('a', duplicateLetters[0]);
    }

    @Test
    public void getDuplicatesForTwoDuplicatesOfDifferentLetter() throws Exception {
        final Rack rack = new Rack('a', 'b', 'c', 'a', 'c');

        final char[] duplicateLetters = rack.getDuplicateLetters();
        assertEquals(2, duplicateLetters.length);
        assertEquals('a', duplicateLetters[0]);
        assertEquals('c', duplicateLetters[1]);
    }

    @Test
    public void getDuplicatesForThreeDuplicatesOfOneLetter() throws Exception {
        final Rack rack = new Rack('a', 'b', 'a', 'n', 'a', 'c');

        final char[] duplicateLetters = rack.getDuplicateLetters();
        assertEquals(2, duplicateLetters.length);
        assertEquals('a', duplicateLetters[0]);
        assertEquals('a', duplicateLetters[1]);
    }

    @Test
    public void getDuplicatesForTwoWildcardsThatAreIgnored() throws Exception {
        final Rack rack = new Rack('a', '*', 'n', '*', 'c');

        final char[] duplicateLetters = rack.getDuplicateLetters();
        assertEquals(0, duplicateLetters.length);
    }

    @Test
    public void countCharForCharNotInRack() throws Exception {
        final Rack rack = new Rack('a', '*', 'n', '*', 'c');

        assertEquals(0, rack.countChar('b'));
    }

    @Test
    public void countCharForTwoCharsInRack() throws Exception {
        final Rack rack = new Rack('a', '*', 'n', '*', 'c');

        assertEquals(2, rack.countChar('*'));
    }
}
