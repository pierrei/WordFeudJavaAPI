package nu.mrpi.wordfeudapi.domain;

import nu.mrpi.wordfeudapi.domain.Tile;

/**
 * @author Pierre Ingmansson
 */
public class Solution {
    private final Tile[] tiles;
    private final String word;
    private final int points;
    private final boolean horizontalWord;

    public Solution(Tile[] tiles, String word, int points, boolean horizontalWord) {
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
}
