package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;

import java.util.Arrays;

/**
 * @author Pierre Ingmansson
 */
public class Notifications {
    private NotificationEntry[] entries;
    private boolean cutoff;

    public NotificationEntry[] getEntries() {
        return entries;
    }

    public boolean isCutoff() {
        return cutoff;
    }

    public static Notifications fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, Notifications.class);
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "entries=" + (entries == null ? null : Arrays.asList(entries)) +
                ", cutoff=" + cutoff +
                '}';
    }
}
