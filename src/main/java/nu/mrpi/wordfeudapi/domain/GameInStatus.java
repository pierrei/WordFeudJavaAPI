package nu.mrpi.wordfeudapi.domain;

import static nu.mrpi.util.DateUtil.format;

/**
 * @author Pierre Ingmansson
 */
public class GameInStatus {
    private double updated;
    private int chat_count;
    private int id;

    public GameInStatus(final int id, final double updated, final int chatCount) {
        this.id = id;
        this.updated = updated;
        this.chat_count = chatCount;
    }

    public GameInStatus() {
    }

    public double getUpdated() {
        return updated;
    }

    public int getChatCount() {
        return chat_count;
    }

    public int getId() {
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
