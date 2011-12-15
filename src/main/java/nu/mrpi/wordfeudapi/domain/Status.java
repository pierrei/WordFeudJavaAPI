package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;

import java.util.Arrays;

/**
 * @author Pierre Ingmansson
 */
public class Status {
    Invite[] invites_received;
    Invite[] invites_sent;
    GameInStatus[] games;

    public Invite[] getInvitesReceived() {
        return invites_received;
    }

    public Invite[] getInvitesSent() {
        return invites_sent;
    }

    public GameInStatus[] getGames() {
        return games;
    }

    public static Status fromJson(final String json) {
        return new Gson().fromJson(json, Status.class);
    }


    @Override
    public String toString() {
        return "Status{" +
                "invitesReceived=" + (invites_received == null ? null : Arrays.asList(invites_received)) +
                ", invitesSent=" + (invites_sent == null ? null : Arrays.asList(invites_sent)) +
                ", games=" + (games == null ? null : Arrays.asList(games)) +
                '}';
    }
}
