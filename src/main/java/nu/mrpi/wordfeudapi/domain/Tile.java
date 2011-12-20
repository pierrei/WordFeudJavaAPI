package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson
 */
public class Tile {
    public static final char WILDCARD = '*';
    
    private int x;
    private int y;
    private String character;
    private boolean wildcard;

    public Tile(int x, int y, char character, boolean wildcard) {
        this.x = x;
        this.y = y;
        this.character = character + "";
        this.wildcard = wildcard;
    }

    public Tile(Object[] tile) {
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

    public static Object[][] convert(Tile[] tiles) {
        Object[][] ret = new Object[tiles.length][4];
        for (int i = 0, tilesLength = tiles.length; i < tilesLength; i++) {
            Tile tile = tiles[i];
            ret[i] = new Object[] {tile.getX(), tile.getY(), tile.getCharacter(), tile.isWildcard()};
        }
        return ret;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", character='" + character + '\'' +
                ", wildcard=" + wildcard +
                '}';
    }
}
