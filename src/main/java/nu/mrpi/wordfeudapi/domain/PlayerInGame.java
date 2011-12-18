package nu.mrpi.wordfeudapi.domain;

import nu.mrpi.util.ArrayUtil;

/**
 * @author Pierre Ingmansson
 */
public class PlayerInGame {
    private String username;
    private int position;
    private int score;
    private int id;
    private double avatar_updated;
    private String[] rack;

    public String getUsername() {
        return username;
    }

    public int getPosition() {
        return position;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public double getAvatarUpdated() {
        return avatar_updated;
    }

    public char[] getRack() {
        return ArrayUtil.convertToCharArray(rack);
    }

    @Override
    public String toString() {
        return "PlayerInGame{" +
                "username='" + username + '\'' +
                ", position=" + position +
                ", score=" + score +
                ", id=" + id +
                ", avatarUpdated=" + avatar_updated +
                '}';
    }
}
