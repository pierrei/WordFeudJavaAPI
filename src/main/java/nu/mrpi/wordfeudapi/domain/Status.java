package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;

import java.util.Arrays;

/**
 * @author Pierre Ingmansson
 */
public class Status {
    private Invite[] invites_received;
    private Invite[] invites_sent;
    private GameInStatus[] games;

    public Status(final Invite[] invitesReceived, final Invite[] invitesSent, final GameInStatus[] games) {
        this.invites_received = invitesReceived;
        this.invites_sent = invitesSent;
        this.games = games;
    }

    public Status() {
    }

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
