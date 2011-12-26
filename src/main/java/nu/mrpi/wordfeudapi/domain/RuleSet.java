package nu.mrpi.wordfeudapi.domain;

/**
* @author Pierre Ingmansson
*/
public enum RuleSet {
    American(0),
    Norwegian(1),
    Dutch(2),
    Danish(3),
    Swedish(4),
    English(5),
    Spanish(6),
    French(7),
    StrictSwedish(8);

    private final int apiIntRepresentation;

    RuleSet(int apiIntRepresentation) {
        this.apiIntRepresentation = apiIntRepresentation;
    }

    public int getApiIntRepresentation() {
        return apiIntRepresentation;
    }

    public static RuleSet fromInt(int number) {
        for (RuleSet ruleSet : values()) {
            if (ruleSet.apiIntRepresentation == number) {
                return ruleSet;
            }
        }
        throw new IllegalArgumentException("RuleSet for value " + number + " not found!");
    }

    public static RuleSet fromString(String string) {
        for (RuleSet ruleSet : values()) {
            if (ruleSet.name().equalsIgnoreCase(string)) {
                return ruleSet;
            }
        }
        throw new IllegalArgumentException("RuleSet matching \"" + string + "\" not found!");
    }

    @Override
    public String toString() {
        return super.toString() + "(" + apiIntRepresentation + ")";
    }
}
