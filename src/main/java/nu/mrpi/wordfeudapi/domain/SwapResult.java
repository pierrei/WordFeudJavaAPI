package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;
import nu.mrpi.util.ArrayUtil;

import java.util.Arrays;

/**
 * @author Pierre Ingmansson
 */
public class SwapResult {
    private double updated;
    private String[] new_tiles;

    public double getUpdated() {
        return updated;
    }

    public char[] getNewTiles() {
        return ArrayUtil.convertToCharArray(new_tiles);
    }

    public static SwapResult fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, SwapResult.class);
    }

    @Override
    public String toString() {
        return "SwapResult{" +
                "updated=" + updated +
                ", new_tiles=" + (new_tiles == null ? null : Arrays.asList(new_tiles)) +
                '}';
    }
}
