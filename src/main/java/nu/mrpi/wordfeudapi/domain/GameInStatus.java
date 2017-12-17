package nu.mrpi.wordfeudapi.domain;

import static nu.mrpi.util.DateUtil.format;

/**
 * @author Pierre Ingmansson
 */
public class GameInStatus {
    private long updated;
    private int chat_count;
    private long id;

    public GameInStatus(final long id, final long updated, final int chatCount) {
        this.id = id;
        this.updated = updated;
        this.chat_count = chatCount;
    }

    public GameInStatus() {
    }

    public long getUpdated() {
        return updated;
    }

    public int getChatCount() {
        return chat_count;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GameInStatus{" +
                "updated=" + format(updated) +
                ", chat_count=" + chat_count +
                ", id=" + id +
                '}';
    }
}
