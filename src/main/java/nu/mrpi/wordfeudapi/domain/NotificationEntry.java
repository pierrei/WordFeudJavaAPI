package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson
 */
public class NotificationEntry {
    private int user_id;
    private int game_id;
    private double created;
    private String username;
    private String type;
    private String message;

    public int getUserId() {
        return user_id;
    }

    public int getGameId() {
        return game_id;
    }

    public double getCreated() {
        return created;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public boolean isChatMessage() {
        return "chat".equals(type);
    }

    @Override
    public String toString() {
        return "NotificationEntry{" +
                "user_id=" + user_id +
                ", game_id=" + game_id +
                ", created=" + created +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
