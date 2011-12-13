package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson (pierre@ingmansson.se)
 */
public class Invite {
    private int id;
    private String inviter;
    private int ruleset;
    private String board_type;

    public int getId() {
        return id;
    }

    public String getInviter() {
        return inviter;
    }

    public int getRuleset() {
        return ruleset;
    }

    public String getBoardType() {
        return board_type;
    }
}
