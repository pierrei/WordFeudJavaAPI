package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson
 */
public class GameInStatus {
    private double updated;
    private int chat_count;
    private int id;

    public double getUpdated() {
        return updated;
    }

    public int getChat_count() {
        return chat_count;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GameInStatus{" +
                "updated=" + updated +
                ", chat_count=" + chat_count +
                ", id=" + id +
                '}';
    }
}
