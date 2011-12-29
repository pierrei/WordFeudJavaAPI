package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Pierre Ingmansson
 */
public class Game {
    private double updated;
    private Object[][] tiles;
    private boolean is_running;
    private int bag_count;
    private int id;
    private int current_player;
    private PlayerInGame[] players;
    private int ruleset;
    private User loggedInUser;
    private int board;

    public static Game fromJson(final String json, final User loggedInUser) {
        final Game game = new Gson().fromJson(json, Game.class);
        game.setLoggedInUser(loggedInUser);
        return game;
    }

    public static Game[] fromJsonArray(final String json, final User loggedInUser) {
        final Game[] games = new Gson().fromJson(json, Game[].class);
        for (final Game game : games) {
            game.setLoggedInUser(loggedInUser);
        }
        return games;
    }

    private void setLoggedInUser(final User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public double getUpdated() {
        return updated;
    }

    public void setUpdated(final double updated) {
        this.updated = updated;
    }

    public Tile[] getTiles() {
        final Tile[] tiles = new Tile[this.tiles.length];
        for (int i = 0, tiles1Length = this.tiles.length; i < tiles1Length; i++) {
            tiles[i] = new Tile(this.tiles[i]);
        }
        return tiles;
    }

    public boolean isRunning() {
        return is_running;
    }

    public void setRunning(final boolean running) {
        this.is_running = running;
    }

    public int getBagCount() {
        return bag_count;
    }

    public void setBagCount(final int bag_count) {
        this.bag_count = bag_count;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public PlayerInGame getCurrentPlayer() {
        return players[current_player];
    }

    public PlayerInGame getMe() {
        if (players[0].getId() == loggedInUser.getId()) {
            return players[0];
        } else if (players[1].getId() == loggedInUser.getId()) {
            return players[1];
        }
        throw new IllegalStateException("Current player not found in game!");
    }

    public PlayerInGame getOpponent() {
        if (players[0].getId() != loggedInUser.getId()) {
            return players[0];
        } else if (players[1].getId() != loggedInUser.getId()) {
            return players[1];
        }
        throw new IllegalStateException("Opponent player not found in game!");
    }

    public Rack getMyRack() {
        return new Rack(getMe().getRack());
    }

    public PlayerInGame[] getPlayers() {
        return players;
    }

    public String getFirstPlayerName() {
        return players[0].getUsername();
    }

    public String getSecondPlayerName() {
        return players[1].getUsername();
    }

    public String getOpponentName() {
        return getOpponent().getUsername();
    }

    public RuleSet getRuleset() {
        return RuleSet.fromInt(ruleset);
    }

    public Locale getLanguageLocale() {
        return getRuleset().getLocale();
    }

    public boolean isMyTurn() {
        return is_running && getCurrentPlayer().getId() == loggedInUser.getId();
    }

    public boolean isInLead() {
        return getMe().getScore() > getOpponent().getScore();
    }

    public int getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "Game{" +
                "updated=" + updated +
                ", tiles=" + (tiles == null ? null : Arrays.asList(getTiles())) +
                ", isRunning=" + is_running +
                ", bagCount=" + bag_count +
                ", id=" + id +
                ", currentPlayer=" + current_player +
                ", players=" + (players == null ? null : Arrays.asList(players)) +
                ", ruleset=" + ruleset +
                ", board=" + board +
                '}';
    }
}
