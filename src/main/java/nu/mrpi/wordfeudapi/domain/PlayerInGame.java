package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson (pierre@ingmansson.se)
 */
public class PlayerInGame {
    private String username;
    private int position;
    private int score;
    private int id;
    private double avatar_updated;
    private char[] rack;

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
        return rack;
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