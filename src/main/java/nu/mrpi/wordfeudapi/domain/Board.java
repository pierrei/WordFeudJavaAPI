package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;

import java.util.Arrays;

/**
 * @author Pierre Ingmansson (piin00)
 */
public class Board {
    private int[][] board;

    public int[][] getBoard() {
        return board;
    }

    public static Board fromJson(final String json) {
        return new Gson().fromJson(json, Board.class);
    }

    @Override
    public String toString() {
        return "Board{" +
                "board=" + (board == null ? null : Arrays.asList(board)) +
                '}';
    }
}
