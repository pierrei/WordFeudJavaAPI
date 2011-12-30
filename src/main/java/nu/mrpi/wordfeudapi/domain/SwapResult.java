package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;
import nu.mrpi.util.ArrayUtil;

import java.util.Arrays;

import static nu.mrpi.util.DateUtil.format;

/**
 * @author Pierre Ingmansson
 */
public class SwapResult {
    private double updated;
    private String[] new_tiles;

    public SwapResult(final double updated, final String[] newTiles) {
        this.updated = updated;
        this.new_tiles = newTiles;
    }

    public SwapResult() {
    }

    public double getUpdated() {
        return updated;
    }

    public char[] getNewTiles() {
        return ArrayUtil.convertToCharArray(new_tiles);
    }

    public static SwapResult fromJson(final String jsonString) {
        return new Gson().fromJson(jsonString, SwapResult.class);
    }

    @Override
    public String toString() {
        return "SwapResult{" +
                "updated=" + format(updated) +
                ", new_tiles=" + (new_tiles == null ? null : Arrays.asList(new_tiles)) +
                '}';
    }
}
