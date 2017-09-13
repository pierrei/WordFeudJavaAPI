package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson
 */
public class TileMove implements Comparable<TileMove> {
    private final Tile[] tiles;
    private final String word;
    private final int points;
    private final boolean horizontalWord;

    public TileMove(final Tile[] tiles, final String word, final int points, final boolean horizontalWord) {
        this.tiles = tiles;
        this.word = word;
        this.points = points;
        this.horizontalWord = horizontalWord;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public String getWord() {
        return word;
    }

    public int getPoints() {
        return points;
    }

    public boolean isHorizontalWord() {
        return horizontalWord;
    }

    @Override
    public int compareTo(TileMove other) {
        return other.getPoints() - getPoints();
    }
}
