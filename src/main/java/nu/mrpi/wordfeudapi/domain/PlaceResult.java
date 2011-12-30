package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;
import nu.mrpi.util.ArrayUtil;

import java.util.Arrays;

import static nu.mrpi.util.DateUtil.format;

/**
 * @author Pierre Ingmansson
 */
public class PlaceResult {
    private String main_word;
    private double updated;
    private String[] new_tiles;
    private int points;
    private boolean is_running;

    public PlaceResult(final String mainWord, final double updated, final String[] newTiles, final int points, final boolean isRunning) {
        this.main_word = mainWord;
        this.updated = updated;
        this.new_tiles = newTiles;
        this.points = points;
        this.is_running = isRunning;
    }

    public PlaceResult() {
    }

    public String getMainWord() {
        return main_word;
    }

    public double getUpdated() {
        return updated;
    }

    public char[] getNewTiles() {
        return ArrayUtil.convertToCharArray(new_tiles);
    }

    public int getPoints() {
        return points;
    }

    public boolean isRunning() {
        return is_running;
    }

    public static PlaceResult fromJson(final String json) {
        return new Gson().fromJson(json, PlaceResult.class);
    }

    @Override
    public String toString() {
        return "PlaceResult{" +
                "main_word='" + main_word + '\'' +
                ", updated=" + format(updated) +
                ", new_tiles=" + (new_tiles == null ? null : Arrays.asList(new_tiles)) +
                ", points=" + points +
                ", is_running=" + is_running +
                '}';
    }
}
