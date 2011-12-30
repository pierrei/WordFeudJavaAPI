package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson
 */
public class Tile {
    public static final char WILDCARD = '*';

    private final int x;
    private final int y;
    private final String character;
    private final boolean wildcard;

    public Tile(final int x, final int y, final char character, final boolean wildcard) {
        this.x = x;
        this.y = y;
        this.character = character + "";
        this.wildcard = wildcard;
    }

    public Tile(final Object[] tile) {
        x = ((Double) tile[0]).intValue();
        y = ((Double) tile[1]).intValue();
        character = (String) tile[2];
        wildcard = (Boolean) tile[3];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getCharacter() {
        return character.charAt(0);
    }

    public boolean isWildcard() {
        return wildcard;
    }

    public static Object[][] convert(final Tile[] tiles) {
        final Object[][] ret = new Object[tiles.length][4];
        for (int i = 0, tilesLength = tiles.length; i < tilesLength; i++) {
            final Tile tile = tiles[i];
            ret[i] = new Object[]{tile.getX(), tile.getY(), tile.getCharacter(), tile.isWildcard()};
        }
        return ret;
    }

    @Override
    public String toString() {
        return "['" + character + "'" + (wildcard ? " *" : "") + " (" + x + ", " + y + ")]";
    }
}
