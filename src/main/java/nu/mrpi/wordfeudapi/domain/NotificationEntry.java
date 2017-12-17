package nu.mrpi.wordfeudapi.domain;

import static nu.mrpi.util.DateUtil.format;

/**
 * @author Pierre Ingmansson
 */
public class NotificationEntry {
    private long user_id;
    private long game_id;
    private double created;
    private String username;
    private String type;
    private String message;

    public NotificationEntry(final long userId, final long gameId, final double created, final String username, final String type, final String message) {
        this.user_id = userId;
        this.game_id = gameId;
        this.created = created;
        this.username = username;
        this.type = type;
        this.message = message;
    }

    public NotificationEntry() {
    }

    public long getUserId() {
        return user_id;
    }

    public long getGameId() {
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
        return new StringBuilder()
                .append("NotificationEntry{")
                .append("type='").append(type).append('\'')
                .append(", username='").append(username).append('\'')
                .append(", user_id=").append(user_id)
                .append(", game_id=").append(game_id)
                .append(", created=").append(format(created))
                .append(message != null ? ", message='" + message + '\'' : "")
                .append('}').toString();
    }
}
