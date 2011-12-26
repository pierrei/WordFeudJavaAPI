package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson
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

    public RuleSet getRuleset() {
        return RuleSet.fromInt(ruleset);
    }

    public String getBoardType() {
        return board_type;
    }
}
